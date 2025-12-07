package com.mycompany.apuestatodook.repositories;

import com.mycompany.apuestatodook.utils.EntityManagerUtil;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.repositories.ApuestaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidoRepository {
    
    private EntityManager em;
    
    public PartidoRepository() {
        this.em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
    }
    

public void guardar(Partido partido) {
    try {
        em.getTransaction().begin();
        
        if (partido.getIdPartido() == 0) {
            em.persist(partido); 
            em.flush(); 
            

            crearResultadoInicial(partido.getIdPartido());
        } else {
            em.merge(partido);
        }
        
        em.getTransaction().commit();
        System.out.println("✅ Partido guardado ID: " + partido.getIdPartido() + " con resultado inicial");
        
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al guardar partido", e);
    }
}

private void crearResultadoInicial(int idPartido) {
    try {
   
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(r) FROM Resultado r WHERE r.partido.idPartido = :idPartido", 
            Long.class
        );
        query.setParameter("idPartido", idPartido);
        
        Long count = query.getSingleResult();
        
        if (count == 0) {

            Resultado resultado = new Resultado();
            resultado.setGanador("pendiente");
            

            Partido partido = em.find(Partido.class, idPartido);
            resultado.setPartido(partido);
            
            em.persist(resultado);
            System.out.println("✅ Resultado inicial creado para partido ID: " + idPartido);
        }
    } catch (Exception e) {
        System.out.println("⚠️  No se pudo crear resultado inicial: " + e.getMessage());

    }
}

public Resultado obtenerResultadoPorPartido(int idPartido) {
    TypedQuery<Resultado> query = em.createQuery(
        "SELECT r FROM Resultado r WHERE r.partido.idPartido = :idPartido", 
        Resultado.class
    );
    query.setParameter("idPartido", idPartido);
    
    try {
        return query.getSingleResult();
    } catch (Exception e) {
        return null; 
    }
}
    

    public List<Partido> obtenerTodos() {
        TypedQuery<Partido> query = em.createQuery(
            "SELECT p FROM Partido p ORDER BY p.fecha ASC", Partido.class
        );
        return query.getResultList();
    }
    

    public Partido obtenerPorId(int idPartido) {
        return em.find(Partido.class, idPartido);
    }
    
 
    public List<Partido> obtenerPartidosFuturos() {
    try {
        System.out.println("🔍 Buscando partidos FUTUROS...");
        

        TypedQuery<Partido> queryTodos = em.createQuery(
            "SELECT p FROM Partido p ORDER BY p.fecha ASC", 
            Partido.class
        );
        List<Partido> todos = queryTodos.getResultList();
        
        System.out.println("📊 TOTAL partidos en BD: " + todos.size());
        

        List<Partido> futuros = new ArrayList<>();
        for (Partido partido : todos) {
            System.out.println("   📅 " + partido.getLocal() + " vs " + partido.getVisitante() + 
                             " - Fecha: '" + partido.getFecha() + "'");
            
            if (esFechaFutura(partido.getFecha())) {
                futuros.add(partido);
                System.out.println("     ✅ ES FUTURO");
            } else {
                System.out.println("     ❌ ES PASADO");
            }
        }
        
        System.out.println("🎯 Partidos futuros encontrados: " + futuros.size());
        return futuros;
        
    } catch (Exception e) {
        System.out.println("💥 ERROR en obtenerPartidosFuturos: " + e.getMessage());
        return new ArrayList<>();
    }
}
    
    private boolean esFechaFutura(String fechaStr) {
    try {
 
        String fechaNormalizada = fechaStr.replace(" ", "T");
        if (!fechaNormalizada.contains(".")) {
            fechaNormalizada += ":00"; 
        }
        
        LocalDateTime fechaPartido = LocalDateTime.parse(fechaNormalizada);
        LocalDateTime ahora = LocalDateTime.now();
        
        System.out.println("   ⏰ Comparando: " + fechaPartido + " > " + ahora + " = " + fechaPartido.isAfter(ahora));
        
        return fechaPartido.isAfter(ahora);
        
    } catch (Exception e) {
        System.out.println("⚠️  Error parseando fecha '" + fechaStr + "': " + e.getMessage());
        return false;
    }
}
    

    public List<Partido> obtenerPartidosConResultado() {
        TypedQuery<Partido> query = em.createQuery(
            "SELECT p FROM Partido p JOIN FETCH p.resultado r ORDER BY p.fecha ASC", 
            Partido.class
        );
        return query.getResultList();
    }
    

    public void eliminar(int idPartido) {
        try {
            em.getTransaction().begin();
            
            Partido partido = em.find(Partido.class, idPartido);
            if (partido != null) {
                em.remove(partido);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar partido", e);
        }
    }
    

    public void actualizarResultado(int idPartido, String ganador) {
        try {
            em.getTransaction().begin();
            
            Partido partido = em.find(Partido.class, idPartido);
            if (partido != null) {
                Resultado resultado = partido.getResultado();
                if (resultado == null) {
                    resultado = new Resultado();
                    resultado.setGanador(ganador);
                    resultado.setPartido(partido);
                    partido.setResultado(resultado);
                } else {
                    resultado.setGanador(ganador);
                }
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar resultado", e);
        }
    }
    public void actualizarResultadoYProcesarApuestas(int idPartido, String ganador) {
    try {
        em.getTransaction().begin();
        
        Partido partido = em.find(Partido.class, idPartido);
        if (partido != null) {
            Resultado resultado = partido.getResultado();
            if (resultado == null) {
                resultado = new Resultado();
                resultado.setGanador(ganador);
                resultado.setPartido(partido);
                partido.setResultado(resultado);
            } else {
                resultado.setGanador(ganador);
            }
        }
        
        em.getTransaction().commit();
        

        ApuestaRepository apuestaRepo = new ApuestaRepository();
        try {
            apuestaRepo.procesarApuestasPorResultado(idPartido, ganador);
        } finally {
            apuestaRepo.close();
        }
        
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al actualizar resultado", e);
    }
}
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}