package com.mycompany.apuestatodook.model;

public class Usuario extends UsuarioBase {
    private double dinero;
    private String dni;
    private String nombre;
    private String apellido;
    private int edad;

    public Usuario(int id, String usuario, String contrasenia, double dinero, 
                   String dni, String nombre, String apellido, int edad) {
        super(id, usuario, contrasenia, "user");
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
    
    public boolean tieneSaldoSuficiente(double monto) {
        return this.dinero >= monto;
    }
}

