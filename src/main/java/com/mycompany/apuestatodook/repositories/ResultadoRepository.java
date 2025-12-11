package com.mycompany.apuestatodook.repositories;

import com.mycompany.apuestatodook.utils.EntityManagerUtil;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.Resultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ResultadoRepository {
    
    private final EntityManager em;
    
    public ResultadoRepository() {
        this.em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
    }
    

    public void guardar(Resultado resultado) {
        try {
            em.getTransaction().begin();
            em.persist(resultado);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar resultado", e);
        }
    }
    

    public List<Resultado> obtenerTodos() {
        TypedQuery<Resultado> query = em.createQuery(
            "SELECT r FROM Resultado r JOIN FETCH r.partido ORDER BY r.idResultado DESC", 
            Resultado.class
        );
        return query.getResultList();
    }
    

public Resultado obtenerPorPartido(int idPartido) {

    Resultado resultado = null;

    TypedQuery<Resultado> query = em.createQuery(
        "SELECT r FROM Resultado r WHERE r.partido.idPartido = :idPartido", 
        Resultado.class
    );
    query.setParameter("idPartido", idPartido);
    
    try {
        resultado = query.getSingleResult();
    } catch (Exception e) {

        resultado = null; 
    }
    return resultado;
}
    
  
public int obtenerIdResultadoPorPartido(int idPartido) {
    int idResultado = 0;

    TypedQuery<Integer> query = em.createQuery(
        "SELECT r.idResultado FROM Resultado r WHERE r.partido.idPartido = :idPartido", 
        Integer.class
    );
    query.setParameter("idPartido", idPartido);
    
    try {
        idResultado = query.getSingleResult();
    } catch (Exception e) {
        throw new RuntimeException("No se encontró resultado para el partido con id " + idPartido);
    }
    
    return idResultado;
}
    

    public void actualizar(Resultado resultado) {
        try {
            em.getTransaction().begin();
            em.merge(resultado);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar resultado", e);
        }
    }
    

    public void crearResultadoParaPartido(int idPartido, String ganador) {
        try {
            em.getTransaction().begin();
            
            Partido partido = em.find(Partido.class, idPartido);
            if (partido != null) {
                Resultado resultado = new Resultado();
                resultado.setGanador(ganador);
                resultado.setPartido(partido);
                em.persist(resultado);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al crear resultado para partido", e);
        }
    }
    
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}