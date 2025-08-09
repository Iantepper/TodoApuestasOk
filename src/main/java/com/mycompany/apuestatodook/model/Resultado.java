package com.mycompany.apuestatodook.model;


public class Resultado {
    private int idResultado;
    private String ganador;
    private int idPartido;

    public void setIdResultado(int idResultado) {
        this.idResultado = idResultado;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public Resultado(int idResultado, String ganador, int idPartido) {
        this.idResultado = idResultado;
        this.ganador = ganador;
        this.idPartido = idPartido;
    }
    
    public Resultado(String ganador) {
        this.ganador = ganador;
    }

    public int getIdResultado() {
        return idResultado;
    }

    public String getGanador() {
        return ganador;
    }

    public int getIdPartido() {
        return idPartido;
    }
}