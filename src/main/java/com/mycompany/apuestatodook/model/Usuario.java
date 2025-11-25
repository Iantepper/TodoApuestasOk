
package com.mycompany.apuestatodook.model;


public class Usuario {
    private int IDusuario;
    private String usuario;
    private String contrasenia;
    private double dinero;
    private String tipo;

    // Constantes para tipos de usuario
    public static final String TIPO_ADMIN = "admin";
    public static final String TIPO_USER = "user";

    public Usuario(int IDusuario, String usuario, String contrasenia, double dinero, String tipo) {
        this.IDusuario = IDusuario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.dinero = dinero;
        this.tipo = tipo;
    }
    
    public String getTipo() {
        return tipo;
    }

    // Métodos helper para verificar tipo
    public boolean esAdmin() {
        return TIPO_ADMIN.equalsIgnoreCase(this.tipo);
    }

    public boolean esUser() {
        return TIPO_USER.equalsIgnoreCase(this.tipo);
    }
    
    public Usuario(int IDusuario, String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.IDusuario= IDusuario;
    }

    public double getDinero() {
        return dinero;
    }

    public int getIDusuario() {
        return IDusuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setDinero(double dinero) {
        this.dinero = dinero;
    }


    public String getNombre() {
        return usuario;
    }

    public String getContrasena() {
        return contrasenia;
    }

    public void setIDusuario(int IDusuario) {
        this.IDusuario = IDusuario;
    }

    @Override
    public String toString() {
        return "Usuario{" + "IDusuario=" + IDusuario + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", dinero=" + dinero + '}';
    }

}


