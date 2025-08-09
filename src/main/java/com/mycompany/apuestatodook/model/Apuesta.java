package com.mycompany.apuestatodook.model;


public class Apuesta {
    private int idApuesta;
    private int monto;
    private String por_quien;
    private int idUsuario;
    private int idPartido;
    private char estado;
    private int fk_id_resultado;
    private String local;
    private String visitante;
    private String fecha;

    public Apuesta(String local, String visitante, String fecha, int monto, String por_quien) {
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.monto = monto;
        this.por_quien = por_quien;
    }
    
    public Apuesta(int monto, String por_quien, int idUsuario, int idPartido, int fk_id_resultado) {
        this.monto = monto;
        this.por_quien = por_quien;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.fk_id_resultado = fk_id_resultado;
    }
    
    public String getLocal() {
        return local;
    }

    public String getVisitante() {
        return visitante;
    }

    public String getFecha() {
        return fecha;
    }
    
    public int getFk_id_resultado() {
        return fk_id_resultado;
    }

    public void setFk_id_resultado(int fk_id_resultado) {
        this.fk_id_resultado = fk_id_resultado;
    }
    
    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
    
    public int getIdApuesta() {
        return idApuesta;
    }

    public int getMonto() {
        return monto;
    }

    public String getpor_quien() {
        return por_quien;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdApuesta(int idApuesta) {
        this.idApuesta = idApuesta;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public void setPor_quien(String por_quien) {
        this.por_quien = por_quien;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

}