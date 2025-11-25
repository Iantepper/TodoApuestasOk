package com.mycompany.apuestatodook.model;

public abstract class UsuarioBase {
    protected int id;
    protected String usuario;
    protected String contrasenia;
    protected String tipo;

    public UsuarioBase(int id, String usuario, String contrasenia, String tipo) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.tipo = tipo;
    }

 
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasenia; }
    public String getTipo() { return tipo; }

    public abstract boolean puedeApostar();
    public abstract boolean puedeGestionarPartidos();
    public abstract boolean puedeVerTodasLasApuestas();
    
    
    public boolean esAdmin() {
        return "admin".equalsIgnoreCase(this.tipo);
    }
}