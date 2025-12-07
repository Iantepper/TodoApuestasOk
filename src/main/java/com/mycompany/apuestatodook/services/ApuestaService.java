package com.mycompany.apuestatodook.services;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.repositories.ApuestaRepository;
import com.mycompany.apuestatodook.repositories.UsuarioRepository;
import com.mycompany.apuestatodook.repositories.PartidoRepository;
import com.mycompany.apuestatodook.repositories.ResultadoRepository;
import java.util.List;

public class ApuestaService {
    
    private ApuestaRepository apuestaRepository;
    private UsuarioRepository usuarioRepository;
    private PartidoRepository partidoRepository;
    private ResultadoRepository resultadoRepository;
    
    public ApuestaService() {
        this.apuestaRepository = new ApuestaRepository();
        this.usuarioRepository = new UsuarioRepository();
        this.partidoRepository = new PartidoRepository();
        this.resultadoRepository = new ResultadoRepository();
    }
    
    /**
     * Validar si un usuario puede realizar una apuesta
     */
    public void validarApuesta(int usuarioId, int monto, int partidoId) {
        // Validar usuario
        Usuario usuario = (Usuario) usuarioRepository.autenticarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        // Validar saldo
        if (!usuario.tieneSaldoSuficiente(monto)) {
            throw new IllegalStateException("Saldo insuficiente. Saldo actual: $" + usuario.getDinero());
        }
        
        // Validar partido (debe existir y ser futuro)
        Partido partido = partidoRepository.obtenerPorId(partidoId);
        if (partido == null) {
            throw new IllegalArgumentException("Partido no encontrado");
        }
        
        // Validar que no haya resultado ya cargado
        Resultado resultado = resultadoRepository.obtenerPorPartido(partidoId);
        if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
            throw new IllegalStateException("No se puede apostar en este partido. El resultado ya está determinado.");
        }
    }
    
    /**
     * Crear una nueva apuesta
     */
    public Apuesta crearApuesta(int monto, String porQuien, int usuarioId, int partidoId) {
        try {
            // Validaciones
            validarApuesta(usuarioId, monto, partidoId);
            
            // Obtener ID del resultado
            int resultadoId = resultadoRepository.obtenerIdResultadoPorPartido(partidoId);
            
            // Crear apuesta
            Apuesta apuesta = new Apuesta(monto, porQuien, 'A', usuarioId, partidoId, resultadoId);
            
            // Actualizar saldo del usuario
            Usuario usuario = (Usuario) usuarioRepository.autenticarPorId(usuarioId);
            double nuevoSaldo = usuario.getDinero() - monto;
            usuario.setDinero(nuevoSaldo);
            usuarioRepository.actualizarDinero(usuarioId, nuevoSaldo);
            
            // Guardar apuesta
            apuestaRepository.guardar(apuesta);
            
            return apuesta;
            
        } catch (Exception e) {
            // Log error
            System.err.println("Error en ApuestaService.crearApuesta: " + e.getMessage());
            throw new RuntimeException("Error al crear apuesta: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtener apuestas de un usuario
     */
    public List<Apuesta> obtenerApuestasPorUsuario(int usuarioId) {
        return apuestaRepository.obtenerPorUsuarioConDetalles(usuarioId);
    }
    
    /**
     * Obtener todas las apuestas (para admin)
     */
    public List<Apuesta> obtenerTodasLasApuestas() {
        return apuestaRepository.obtenerTodasConDetalles();
    }
    
    /**
     * Cerrar conexiones
     */
    public void close() {
        apuestaRepository.close();
        usuarioRepository.close();
        partidoRepository.close();
        resultadoRepository.close();
    }
}