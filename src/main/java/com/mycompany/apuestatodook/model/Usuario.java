package com.mycompany.apuestatodook.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("user")
public class Usuario extends UsuarioBase {
    
    @Column(name = "dinero")
    private double dinero;
    
    @Column(name = "dni")
    private String dni;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "apellido")
    private String apellido;
    
    @Column(name = "edad")
    private int edad;

    // Constructor vacío
    public Usuario() {}

public Usuario(int id, String usuario, String contrasenia, double dinero, 
               String dni, String nombre, String apellido, int edad) {
    super(id, usuario, contrasenia, "user"); // user fuera
    this.dinero = dinero;
    this.dni = dni;
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
}

    @Override
    public boolean puedeApostar() {
        return true;
    }

    @Override
    public boolean puedeGestionarPartidos() {
        return false;
    }

    @Override
    public boolean puedeVerTodasLasApuestas() {
        return false;
    }

    // Getters específicos
    public double getDinero() { return dinero; }
    public void setDinero(double dinero) { this.dinero = dinero; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    
    public void setUsuario(String usuario) {
    this.usuario = usuario;
    }

public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
    }


    public boolean tieneSaldoSuficiente(double monto) {
        return this.dinero >= monto;
    }
    public void setDni(String dni) {
    this.dni = dni;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public void setApellido(String apellido) {
    this.apellido = apellido;
}

public void setEdad(int edad) {
    this.edad = edad;
}
}

