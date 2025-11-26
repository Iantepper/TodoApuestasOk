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
    protected int id;
    
    @Column(name = "usuario", unique = true, nullable = false)
    protected String usuario;
    
    @Column(name = "contrasenia", nullable = false)
    protected String contrasenia;
    
    @Column(name = "tipo", insertable = false, updatable = false)
    protected String tipo;
    
    public UsuarioBase() {}

    public UsuarioBase(int id, String usuario, String contrasenia, String tipo) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasenia; }
    
 //tipo desde la discriminadora
    public String getTipo() {
        if (this instanceof Admin) {
            return "admin";
        } else if (this instanceof Usuario) {
            return "user";
        }
        return null;
    }


    public void setId(int id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }


    public abstract boolean puedeApostar();
    public abstract boolean puedeGestionarPartidos();
    public abstract boolean puedeVerTodasLasApuestas();
    
    public boolean esAdmin() {
        return "admin".equalsIgnoreCase(this.getTipo());
    }
}