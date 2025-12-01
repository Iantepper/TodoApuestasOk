package com.mycompany.apuestatodook.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("admin")
public class Admin extends UsuarioBase {

    public Admin() {}

    public Admin(int id, String usuario, String contrasenia) {
        super(id, usuario, contrasenia); 
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
}