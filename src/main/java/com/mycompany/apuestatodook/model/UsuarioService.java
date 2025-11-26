package com.mycompany.apuestatodook.model;

public class UsuarioService {
    private UsuarioRepository usuarioRepo;
    
    public UsuarioService() {
        this.usuarioRepo = new UsuarioRepository();
    }
    

    public UsuarioBase autenticar(String usuario, String contrasenia) {
        return usuarioRepo.autenticar(usuario, contrasenia);
    }
    
    public int crearUsuario(String usuario, String contrasenia, String nombre, 
                           String apellido, int edad, String dni) {
        return usuarioRepo.crearUsuario(usuario, contrasenia, nombre, apellido, edad, dni);
    }
    
 
    public double getDineroPorIdUsuario(int idUsuario) {
        return usuarioRepo.getDineroPorIdUsuario(idUsuario);
    }
    
    public void updateDinero(UsuarioBase usuario) {
        if (usuario instanceof Usuario) {
            usuarioRepo.actualizarDinero(usuario.getId(), ((Usuario) usuario).getDinero());
        }
    }
    

    public boolean existeUsuario(String usuario) {
        return usuarioRepo.existeUsuario(usuario);
    }
    
    public void close() {
        if (usuarioRepo != null) {
            usuarioRepo.close();
        }
    }
}