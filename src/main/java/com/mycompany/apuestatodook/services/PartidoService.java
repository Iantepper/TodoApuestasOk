package com.mycompany.apuestatodook.services;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.repositories.PartidoRepository;
import com.mycompany.apuestatodook.repositories.ApuestaRepository;
import com.mycompany.apuestatodook.repositories.ResultadoRepository;
import com.mycompany.apuestatodook.model.Resultado;
import java.time.LocalDateTime;
import java.util.List;

public class PartidoService {
    
    private final PartidoRepository partidoRepository;
    private final ApuestaRepository apuestaRepository;
    private final ResultadoRepository resultadoRepository;
    
    public PartidoService() {
        this.partidoRepository = new PartidoRepository();
        this.apuestaRepository = new ApuestaRepository();
        this.resultadoRepository = new ResultadoRepository();
    }
    

    public List<Partido> obtenerPartidosFuturos() {
        return partidoRepository.obtenerPartidosFuturos();
    }
    

    public List<Partido> obtenerTodosLosPartidos() {
        return partidoRepository.obtenerTodos();
    }
    

    public void crearPartido(String local, String visitante, String fecha) {

        if (local == null || local.trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo local es requerido");
        }
        if (visitante == null || visitante.trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo visitante es requerido");
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        

        if (!esFechaFutura(fecha)) {
            throw new IllegalArgumentException("La fecha del partido debe ser futura");
        }
        

        Partido partido = new Partido(local, visitante, fecha, 0);
        partidoRepository.guardar(partido);
    }
    

    public void actualizarResultado(int partidoId, String ganador) {
 
        if (ganador == null || ganador.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar un ganador");
        }
        
        if (!ganador.equals("local") && !ganador.equals("visitante") && !ganador.equals("empate")) {
            throw new IllegalArgumentException("Ganador debe ser: local, visitante o empate");
        }
        

        partidoRepository.actualizarResultadoYProcesarApuestas(partidoId, ganador);
    }
    

    public void eliminarPartido(int partidoId) {
        partidoRepository.eliminar(partidoId);
    }
    

    private boolean esFechaFutura(String fechaStr) {
        try {
            LocalDateTime fechaPartido = LocalDateTime.parse(fechaStr.replace(" ", "T"));
            return fechaPartido.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }
    

    public Partido obtenerPartido(int partidoId) {
        return partidoRepository.obtenerPorId(partidoId);
    }
    

    
    public List<Partido> obtenerPartidosConResultado() {

        List<Partido> partidos = partidoRepository.obtenerPartidosConResultado();

        for (Partido partido : partidos) {
            Resultado resultado = resultadoRepository.obtenerPorPartido(partido.getIdPartido());
            partido.setResultado(resultado);
        }
        
        return partidos;
    }
    

    public void close() {
        partidoRepository.close();
        apuestaRepository.close();
        resultadoRepository.close();
    }
}