package com.mycompany.apuestatodook.model;

public class UsuarioService {
    private UsuarioRepository usuarioRepo;
    private UsuarioDAO usuarioDAO; // Temporal - para compatibilidad
    
    public UsuarioService() {
        this.usuarioRepo = new UsuarioRepository();
        this.usuarioDAO = new UsuarioDAO(); // Temporal
    }
    
    // Métodos que usan el NUEVO Repository
    public UsuarioBase autenticar(String usuario, String contrasenia) {
        return usuarioRepo.autenticar(usuario, contrasenia);
    }
    
    public int crearUsuario(String usuario, String contrasenia, String nombre, 
                           String apellido, int edad, String dni) {
        return usuarioRepo.crearUsuario(usuario, contrasenia, nombre, apellido, edad, dni);
    }
    
    // Métodos que todavía usan el VIEJO DAO (temporal)
    public double getDineroPorIdUsuario(int idUsuario) {
        return usuarioDAO.getDineroPorIdUsuario(idUsuario);
    }
    
    public void updateDinero(UsuarioBase usuario) {
        usuarioDAO.updateDinero(usuario);
    }
    
    public void close() {
        if (usuarioRepo != null) {
            usuarioRepo.close();
        }
    }
}