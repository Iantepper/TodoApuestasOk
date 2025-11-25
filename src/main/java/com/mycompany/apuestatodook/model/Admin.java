package com.mycompany.apuestatodook.model;

public class Admin extends UsuarioBase {
    private String nombreCompleto;

    public Admin(int id, String usuario, String contrasenia, String nombreCompleto) {
        super(id, usuario, contrasenia, "admin");
        this.nombreCompleto = nombreCompleto;
    }

    @Override
    public boolean puedeApostar() {
        return false;
    }

    @Override
    public boolean puedeGestionarPartidos() {
        return true;
    }

    @Override
    public boolean puedeVerTodasLasApuestas() {
        return true;
    }

    public String getNombreCompleto() { return nombreCompleto; }
}