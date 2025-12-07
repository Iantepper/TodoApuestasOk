package com.mycompany.apuestatodook.repositories;

import com.mycompany.apuestatodook.model.EntityManagerUtil;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UsuarioRepository {
    
    private EntityManager em;
    
    public UsuarioRepository() {
        this.em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
    }
    
    // aunt
    public UsuarioBase autenticar(String usuario, String contrasenia) {
        try {
            TypedQuery<UsuarioBase> query = em.createQuery(
                "SELECT u FROM UsuarioBase u WHERE u.usuario = :usuario AND u.contrasenia = :contrasenia", 
                UsuarioBase.class
            );
            query.setParameter("usuario", usuario);
            query.setParameter("contrasenia", contrasenia);
            
            UsuarioBase usuarioBase = query.getSingleResult();
            return usuarioBase;
            
        } catch (NoResultException e) {
            return null;
        }
    }
    

    public int crearUsuario(String usuario, String contrasenia, String nombre, String apellido, int edad, String dni) {
        try {
            em.getTransaction().begin();
            
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsuario(usuario);
            nuevoUsuario.setContrasenia(contrasenia);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellido(apellido);
            nuevoUsuario.setEdad(edad);
            nuevoUsuario.setDni(dni);
            nuevoUsuario.setDinero(0.0);
            
            em.persist(nuevoUsuario);
            em.getTransaction().commit();
            
            return nuevoUsuario.getId();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al crear usuario", e);
        }
    }
    
 
    public double getDineroPorIdUsuario(int idUsuario) {
        Usuario usuario = em.find(Usuario.class, idUsuario);
        return (usuario != null) ? usuario.getDinero() : 0.0;
    }
    
    public void actualizarDinero(UsuarioBase usuarioBase) {
        if (usuarioBase instanceof Usuario) {
            try {
                em.getTransaction().begin();
                Usuario usuario = (Usuario) usuarioBase;
                em.merge(usuario);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new RuntimeException("Error al actualizar dinero", e);
            }
        }
    }
    

    public boolean existeUsuario(String usuario) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM UsuarioBase u WHERE u.usuario = :usuario", 
            Long.class
        );
        query.setParameter("usuario", usuario);
        return query.getSingleResult() > 0;
    }
    
    public UsuarioBase autenticarPorId(int idUsuario) {
    try {
        return em.find(UsuarioBase.class, idUsuario);
    } catch (Exception e) {
        System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        return null;
    }
}
    
    public void actualizarDinero(int idUsuario, double nuevoDinero) {
    try {
        em.getTransaction().begin();
        
        Usuario usuario = em.find(Usuario.class, idUsuario);
        if (usuario != null) {
            usuario.setDinero(nuevoDinero);
        }
        
        em.getTransaction().commit();
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al actualizar dinero", e);
    }
}
    
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}