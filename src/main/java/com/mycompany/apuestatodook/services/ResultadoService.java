package com.mycompany.apuestatodook.services;

import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.repositories.ResultadoRepository;
import java.util.List;

public class ResultadoService {
    
    private final ResultadoRepository resultadoRepository;
    
    public ResultadoService() {
        this.resultadoRepository = new ResultadoRepository();
    }
    
   
    public List<Resultado> obtenerTodos() {
        return resultadoRepository.obtenerTodos();
    }
    

    public Resultado obtenerPorPartido(int partidoId) {
        return resultadoRepository.obtenerPorPartido(partidoId);
    }

    public int obtenerIdResultadoPorPartido(int partidoId) {
        return resultadoRepository.obtenerIdResultadoPorPartido(partidoId);
    }
    

    public void crearResultadoParaPartido(int partidoId, String ganador) {
        if (ganador == null || ganador.trim().isEmpty()) {
            throw new IllegalArgumentException("El ganador no puede estar vacío");
        }
        
        resultadoRepository.crearResultadoParaPartido(partidoId, ganador);
    }
    

    public void actualizar(Resultado resultado) {
        resultadoRepository.actualizar(resultado);
    }
    

    public Resultado obtenerPorId(int idResultado) {

        return resultadoRepository.obtenerTodos().stream()
            .filter(r -> r.getIdResultado() == idResultado)
            .findFirst()
            .orElse(null);
    }
    
    public void close() {
        resultadoRepository.close();
    }
}