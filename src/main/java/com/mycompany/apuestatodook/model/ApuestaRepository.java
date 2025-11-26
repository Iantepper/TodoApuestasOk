package com.mycompany.apuestatodook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ApuestaRepository {
    
    private EntityManager em;
    
    public ApuestaRepository() {
        this.em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
    }
    
    // CREATE - Reemplaza add()
    public void guardar(Apuesta apuesta) {
        try {
            em.getTransaction().begin();
            em.persist(apuesta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar apuesta", e);
        }
    }
    
    // UPDATE - Reemplaza updateEstado()
    public void actualizarEstado(Apuesta apuesta) {
        try {
            em.getTransaction().begin();
            em.merge(apuesta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar estado de apuesta", e);
        }
    }
    
    // READ - Reemplaza getAllApuestasConResultado() (para admin)
    public List<Apuesta> obtenerTodasConDetalles() {
        TypedQuery<Apuesta> query = em.createQuery(
            "SELECT a FROM Apuesta a " +
            "LEFT JOIN FETCH a.usuario " +
            "LEFT JOIN FETCH a.partido " +
            "LEFT JOIN FETCH a.resultado " +
            "ORDER BY a.idApuesta DESC", 
            Apuesta.class
        );
        
        List<Apuesta> apuestas = query.getResultList();
        
        // Cargar datos transient para compatibilidad
        for (Apuesta apuesta : apuestas) {
            if (apuesta.getPartido() != null) {
                apuesta.setLocal(apuesta.getPartido().getLocal());
                apuesta.setVisitante(apuesta.getPartido().getVisitante());
                apuesta.setFecha(apuesta.getPartido().getFecha());
            }
            if (apuesta.getUsuario() != null) {
                apuesta.setNombreUsuario(apuesta.getUsuario().getUsuario());
            }
        }
        
        return apuestas;
    }
    
    // READ - Reemplaza getApuestasConResultadoPorUsuario() (para usuario normal)
    public List<Apuesta> obtenerPorUsuarioConDetalles(int idUsuario) {
        TypedQuery<Apuesta> query = em.createQuery(
            "SELECT a FROM Apuesta a " +
            "LEFT JOIN FETCH a.partido " +
            "LEFT JOIN FETCH a.resultado " +
            "WHERE a.fkIdUsuario = :idUsuario " +
            "ORDER BY a.idApuesta DESC", 
            Apuesta.class
        );
        query.setParameter("idUsuario", idUsuario);
        
        List<Apuesta> apuestas = query.getResultList();
        
        // Cargar datos transient para compatibilidad
        for (Apuesta apuesta : apuestas) {
            if (apuesta.getPartido() != null) {
                apuesta.setLocal(apuesta.getPartido().getLocal());
                apuesta.setVisitante(apuesta.getPartido().getVisitante());
                apuesta.setFecha(apuesta.getPartido().getFecha());
            }
        }
        
        return apuestas;
    }
    
    // Método auxiliar para obtener apuesta por ID
    public Apuesta obtenerPorId(int idApuesta) {
        return em.find(Apuesta.class, idApuesta);
    }
    
    // Método para procesar apuestas cuando se define un resultado
    public void procesarApuestasPorResultado(int idPartido, String ganadorReal) {
        try {
            em.getTransaction().begin();
            
            TypedQuery<Apuesta> query = em.createQuery(
                "SELECT a FROM Apuesta a WHERE a.fkIdPartido = :idPartido AND a.estado = 'A'", 
                Apuesta.class
            );
            query.setParameter("idPartido", idPartido);
            
            List<Apuesta> apuestasActivas = query.getResultList();
            
            for (Apuesta apuesta : apuestasActivas) {
                if (ganadorReal.equals(apuesta.getpor_quien())) {
                    apuesta.setEstado('G'); // Ganada
                } else {
                    apuesta.setEstado('P'); // Perdida
                }
            }
            
            em.getTransaction().commit();
            System.out.println("✅ Procesadas " + apuestasActivas.size() + " apuestas para partido " + idPartido);
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al procesar apuestas", e);
        }
    }
    
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}