package com.mycompany.apuestatodook.model;
import jakarta.persistence.*;

@Entity
@Table(name = "apuesta")
public class Apuesta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apuesta")
    private int idApuesta;
    
    @Column(name = "monto", nullable = false)
    private int monto;
    
    @Column(name = "por_quien", nullable = false)
    private String por_quien;
    
    @Column(name = "estado")
    private char estado;
    
    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "fk_id_partido")
    private Partido partido;
    
    @ManyToOne
    @JoinColumn(name = "fk_id_resultado")
    private Resultado resultado;

    // Campos transient (no se persisten)
    @Transient
    private String local;
    
    @Transient
    private String visitante;
    
    @Transient
    private String fecha;
    
    @Transient
    private String nombreUsuario;


    public Apuesta() {}

    // Constructores
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
        // Estos parámetros ya no se usan directamente - se setean los objetos
    }

    // Getters y setters CORREGIDOS
    public int getFk_id_resultado() {
        return (resultado != null) ? resultado.getIdResultado() : 0;
    }

    public void setFk_id_resultado(int fk_id_resultado) {
        // Este método ya no es necesario con JPA
    }
    
    public int getIdUsuario() {
        return (usuario != null) ? usuario.getId() : 0;
    }

    public void setIdUsuario(int idUsuario) {
        // Este método ya no es necesario con JPA
    }

    public int getIdPartido() {
        return (partido != null) ? partido.getIdPartido() : 0;
    }

    public void setIdPartido(int idPartido) {
        // Este método ya no es necesario con JPA
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

    public void setIdApuesta(int idApuesta) {
        this.idApuesta = idApuesta;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public void setPor_quien(String por_quien) {
        this.por_quien = por_quien;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEstadoLegible() {
        switch (estado) {
            case 'G': return "Ganada";
            case 'P': return "Perdida";
            case 'A': return "Activa";
            default: return "Desconocido";
        }
    }

    // Getters para las relaciones JPA
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    // Getters para campos transient
    public String getLocal() {
        return local;
    }

    public String getVisitante() {
        return visitante;
    }

    public String getFecha() {
        return fecha;
    }
}