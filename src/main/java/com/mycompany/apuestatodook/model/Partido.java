package com.mycompany.apuestatodook.model;


public class Partido {
    private String local;
    private String visitante;
    private String fecha;
    private int idPartido;
    private Resultado resultado;

    public Resultado getResultado() {
        return resultado;
    }
    
    public void setResultado(Resultado resultado) {
    this.resultado = resultado;
    }

    public Partido(String local, String visitante, String fecha, int idPartido) {
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.idPartido = idPartido;
    }
     public Partido(String local, String visitante, String fecha, Resultado resultado) {
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.resultado = resultado;
    }
    

    public void setLocal(String local) {
        this.local = local;
    }

    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
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

    public int getIdPartido() {
        return idPartido;
    }
    
    
   

    
    
    
    
}
