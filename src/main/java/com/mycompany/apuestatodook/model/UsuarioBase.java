package com.mycompany.apuestatodook.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class UsuarioBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int id;
    
    @Column(name = "usuario", unique = true, nullable = false)
    private String usuario;
    
    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;
    

    @Column(name = "tipo", insertable = false, updatable = false)
    private String tipo;
    
    public UsuarioBase() {}

    public UsuarioBase(int id, String usuario, String contrasenia) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    
    public String getTipo() { return this.tipo; }


    public boolean esAdmin() {
        return "admin".equals(this.tipo);
    }


    public abstract boolean puedeApostar();
    public abstract boolean puedeGestionarPartidos();
    public abstract boolean puedeVerTodasLasApuestas();
}