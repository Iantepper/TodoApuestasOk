package com.mycompany.apuestatodook.services;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.repositories.PartidoRepository;
import com.mycompany.apuestatodook.repositories.ApuestaRepository;
import java.time.LocalDateTime;
import java.util.List;

public class PartidoService {
    
    private PartidoRepository partidoRepository;
    private ApuestaRepository apuestaRepository;
    
    public PartidoService() {
        this.partidoRepository = new PartidoRepository();
        this.apuestaRepository = new ApuestaRepository();
    }
    
    /**
     * Obtener partidos futuros para usuarios normales
     */
    public List<Partido> obtenerPartidosFuturos() {
        return partidoRepository.obtenerPartidosFuturos();
    }
    
    /**
     * Obtener todos los partidos (para admin)
     */
    public List<Partido> obtenerTodosLosPartidos() {
        return partidoRepository.obtenerTodos();
    }
    
    /**
     * Crear nuevo partido
     */
    public void crearPartido(String local, String visitante, String fecha) {
        // Validaciones
        if (local == null || local.trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo local es requerido");
        }
        if (visitante == null || visitante.trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo visitante es requerido");
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        
        // Validar que la fecha sea futura
        if (!esFechaFutura(fecha)) {
            throw new IllegalArgumentException("La fecha del partido debe ser futura");
        }
        
        // Crear y guardar partido
        Partido partido = new Partido(local, visitante, fecha, 0);
        partidoRepository.guardar(partido);
    }
    
    /**
     * Actualizar resultado de un partido y procesar apuestas
     */
    public void actualizarResultado(int partidoId, String ganador) {
        // Validaciones
        if (ganador == null || ganador.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar un ganador");
        }
        
        if (!ganador.equals("local") && !ganador.equals("visitante") && !ganador.equals("empate")) {
            throw new IllegalArgumentException("Ganador debe ser: local, visitante o empate");
        }
        
        // Actualizar resultado y procesar apuestas
        partidoRepository.actualizarResultadoYProcesarApuestas(partidoId, ganador);
    }
    
    /**
     * Eliminar partido
     */
    public void eliminarPartido(int partidoId) {
        partidoRepository.eliminar(partidoId);
    }
    
    /**
     * Validar si una fecha es futura
     */
    private boolean esFechaFutura(String fechaStr) {
        try {
            LocalDateTime fechaPartido = LocalDateTime.parse(fechaStr.replace(" ", "T"));
            return fechaPartido.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtener partido por ID
     */
    public Partido obtenerPartido(int partidoId) {
        return partidoRepository.obtenerPorId(partidoId);
    }
    
    /**
     * Obtener partidos con resultados
     */
    public List<Partido> obtenerPartidosConResultado() {
        return partidoRepository.obtenerPartidosConResultado();
    }
    
    /**
     * Cerrar conexiones
     */
    public void close() {
        partidoRepository.close();
        apuestaRepository.close();
    }
}