package com.mycompany.apuestatodook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PartidoRepository {
    
    private EntityManager em;
    
    public PartidoRepository() {
        this.em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
    }
    
    // CREATE - Reemplaza add()
public void guardar(Partido partido) {
    try {
        em.getTransaction().begin();
        
        if (partido.getIdPartido() == 0) {
            em.persist(partido); // INSERT
            em.flush(); // Forzar el INSERT para obtener el ID
            
            // ✅ CREAR RESULTADO AUTOMÁTICAMENTE después de persistir el partido
            crearResultadoInicial(partido.getIdPartido());
        } else {
            em.merge(partido); // UPDATE
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
        // Verificar si ya existe resultado
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(r) FROM Resultado r WHERE r.partido.idPartido = :idPartido", 
            Long.class
        );
        query.setParameter("idPartido", idPartido);
        
        Long count = query.getSingleResult();
        
        if (count == 0) {
            // Crear resultado inicial
            Resultado resultado = new Resultado();
            resultado.setGanador("pendiente");
            
            // Asociar con el partido
            Partido partido = em.find(Partido.class, idPartido);
            resultado.setPartido(partido);
            
            em.persist(resultado);
            System.out.println("✅ Resultado inicial creado para partido ID: " + idPartido);
        }
    } catch (Exception e) {
        System.out.println("⚠️  No se pudo crear resultado inicial: " + e.getMessage());
        // No lanzar excepción para no revertir la transacción del partido
    }
}
    
    // READ - Reemplaza getAll()
    public List<Partido> obtenerTodos() {
        TypedQuery<Partido> query = em.createQuery(
            "SELECT p FROM Partido p ORDER BY p.fecha ASC", Partido.class
        );
        return query.getResultList();
    }
    
    // READ - Reemplaza getPartidoPorId()
    public Partido obtenerPorId(int idPartido) {
        return em.find(Partido.class, idPartido);
    }
    
    // READ - Reemplaza getPartidosFuturos()
    public List<Partido> obtenerPartidosFuturos() {
        TypedQuery<Partido> query = em.createQuery(
            "SELECT p FROM Partido p WHERE p.fecha > CURRENT_TIMESTAMP ORDER BY p.fecha ASC", 
            Partido.class
        );
        return query.getResultList();
    }
    
    // READ - Reemplaza getAllPartidosConResultado()
    public List<Partido> obtenerPartidosConResultado() {
        TypedQuery<Partido> query = em.createQuery(
            "SELECT p FROM Partido p JOIN FETCH p.resultado r ORDER BY p.fecha ASC", 
            Partido.class
        );
        return query.getResultList();
    }
    
    // DELETE - Reemplaza delete()
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
    
    // Método para actualizar resultado (combinación de lógica existente)
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
        
        // Procesar apuestas después de actualizar resultado
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