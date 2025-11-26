package com.mycompany.apuestatodook.model;
import jakarta.persistence.*;

@Entity
@Table(name = "partido")
public class Partido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private int idPartido;
    
    @Column(name = "local", nullable = false)
    private String local;
    
    @Column(name = "visitante", nullable = false)
    private String visitante;
    
    @Column(name = "fecha", nullable = false)
    private String fecha;
    
    @OneToOne(mappedBy = "partido", cascade = CascadeType.ALL)
    private Resultado resultado;

    // Constructor vacío (OBLIGATORIO para JPA)
    public Partido() {}

    // Constructor con parámetros
    public Partido(String local, String visitante, String fecha, int idPartido) {
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.idPartido = idPartido;
    }
    //otroconstructor
    public Partido(String local, String visitante, String fecha, Resultado resultado) {
    this.local = local;
    this.visitante = visitante;
    this.fecha = fecha;
    this.resultado = resultado;
    }

    // Getters y setters
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getVisitante() {
        return visitante;
    }

    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public Resultado getResultado() {
        return resultado;
    }
    
    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
}