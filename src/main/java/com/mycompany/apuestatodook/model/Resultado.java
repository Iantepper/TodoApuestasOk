package com.mycompany.apuestatodook.model;
import jakarta.persistence.*;

@Entity
@Table(name = "resultado")
public class Resultado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private int idResultado;
    
    @Column(name = "ganador", nullable = false)
    private String ganador;
    
    @OneToOne
    @JoinColumn(name = "fk_id_partido")
    private Partido partido;

    // Constructor vacío (OBLIGATORIO para JPA)
    public Resultado() {}

    // Tus constructores existentes
    public Resultado(int idResultado, String ganador, int idPartido) {
        this.idResultado = idResultado;
        this.ganador = ganador;
        // El partido se setea después con setPartido()
    }
    
    public Resultado(String ganador) {
        this.ganador = ganador;
    }

    // Getters y setters
    public int getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(int idResultado) {
        this.idResultado = idResultado;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }
}