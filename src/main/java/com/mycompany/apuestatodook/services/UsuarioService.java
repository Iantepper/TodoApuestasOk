package com.mycompany.apuestatodook.services;

import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.repositories.UsuarioRepository;

public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }
    

    public UsuarioBase autenticar(String usuario, String contrasenia) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Usuario es requerido");
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException("Contraseña es requerida");
        }
        
        return usuarioRepository.autenticar(usuario, contrasenia);
    }
    

    public int crearUsuario(String usuario, String contrasenia, String nombre, 
                           String apellido, int edad, String dni) {

        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Usuario es requerido");
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException("Contraseña es requerida");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre es requerido");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido es requerido");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("DNI es requerido");
        }
        if (edad < 18) {
            throw new IllegalArgumentException("La edad debe ser mayor o igual a 18 años");
        }
        

        if (usuarioRepository.existeUsuario(usuario)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        return usuarioRepository.crearUsuario(usuario, contrasenia, nombre, apellido, edad, dni);
    }
    

    public double obtenerDineroUsuario(int usuarioId) {
        return usuarioRepository.getDineroPorIdUsuario(usuarioId);
    }
    
    public double getDineroPorIdUsuario(int idUsuario) {
        return obtenerDineroUsuario(idUsuario);
    }
    

    public void actualizarDinero(int usuarioId, double nuevoDinero) {
        if (nuevoDinero < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
        usuarioRepository.actualizarDinero(usuarioId, nuevoDinero);
    }
    
        public void updateDinero(UsuarioBase usuario) {
        if (usuario instanceof Usuario) {
            Usuario usuarioNormal = (Usuario) usuario;
            actualizarDinero(usuarioNormal.getId(), usuarioNormal.getDinero());
        }
    }

    public double operarBilletera(int usuarioId, double monto, String operacion) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        
        double saldoActual = obtenerDineroUsuario(usuarioId);
        double nuevoSaldo;
        
        if ("ingreso".equals(operacion)) {
            nuevoSaldo = saldoActual + monto;
        } else if ("retiro".equals(operacion)) {
            if (saldoActual < monto) {
                throw new IllegalStateException("Saldo insuficiente para el retiro");
            }
            nuevoSaldo = saldoActual - monto;
        } else {
            throw new IllegalArgumentException("Operación no válida: " + operacion);
        }
        
        actualizarDinero(usuarioId, nuevoSaldo);
        return nuevoSaldo;
    }
    

    public void close() {
        usuarioRepository.close();
    }
}