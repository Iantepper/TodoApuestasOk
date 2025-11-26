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
            } else {
                em.merge(partido); // UPDATE
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar partido", e);
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