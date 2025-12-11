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
    
    private final ApuestaRepository apuestaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PartidoRepository partidoRepository;
    private final ResultadoRepository resultadoRepository;
    

    public ApuestaService() {
        this.apuestaRepository = new ApuestaRepository();
        this.usuarioRepository = new UsuarioRepository();
        this.partidoRepository = new PartidoRepository();
        this.resultadoRepository = new ResultadoRepository();
    }
    

    public ApuestaService(ApuestaRepository apuestaRepository,
                         UsuarioRepository usuarioRepository,
                         PartidoRepository partidoRepository,
                         ResultadoRepository resultadoRepository) {
        this.apuestaRepository = apuestaRepository;
        this.usuarioRepository = usuarioRepository;
        this.partidoRepository = partidoRepository;
        this.resultadoRepository = resultadoRepository;
    }
    

    public void validarApuesta(int usuarioId, int monto, int partidoId) {

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        

        Usuario usuario = obtenerUsuarioValidado(usuarioId);
        

        if (!usuario.tieneSaldoSuficiente(monto)) {
            throw new IllegalStateException(
                String.format("Saldo insuficiente. Monto: $%d | Saldo actual: $%.0f", 
                monto, usuario.getDinero())
            );
        }
        

        obtenerPartidoValidado(partidoId);
        

        validarPartidoSinResultado(partidoId);
    }
    

    public Apuesta crearApuesta(int monto, String porQuien, int usuarioId, int partidoId) {

        validarApuesta(usuarioId, monto, partidoId);
        

        int resultadoId = resultadoRepository.obtenerIdResultadoPorPartido(partidoId);
        

        Apuesta apuesta = new Apuesta(monto, porQuien, 'A', usuarioId, partidoId, resultadoId);
        

        actualizarSaldoUsuario(usuarioId, -monto);
        

        apuestaRepository.guardar(apuesta);
        
        return apuesta;
    }
    
 
    public void procesarApuestasGanadoras(int partidoId, String ganadorReal) {
        if (ganadorReal == null || ganadorReal.trim().isEmpty()) {
            throw new IllegalArgumentException("Ganador no puede estar vacío");
        }
        
        // Procesar repository
        apuestaRepository.procesarApuestasPorResultado(partidoId, ganadorReal);

        pagarPremiosGanadores(partidoId, ganadorReal);
    }
    

    
    private Usuario obtenerUsuarioValidado(int usuarioId) {
        Usuario usuario = (Usuario) usuarioRepository.autenticarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return usuario;
    }
    
    private Partido obtenerPartidoValidado(int partidoId) {
        Partido partido = partidoRepository.obtenerPorId(partidoId);
        if (partido == null) {
            throw new IllegalArgumentException("Partido no encontrado");
        }
        return partido;
    }
    
    private void validarPartidoSinResultado(int partidoId) {
        Resultado resultado = resultadoRepository.obtenerPorPartido(partidoId);
        if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
            throw new IllegalStateException("No se puede apostar en este partido. El resultado ya está determinado.");
        }
    }
    
    private void actualizarSaldoUsuario(int usuarioId, double cambio) {
        Usuario usuario = obtenerUsuarioValidado(usuarioId);
        double nuevoSaldo = usuario.getDinero() + cambio;
        
        if (nuevoSaldo < 0) {
            throw new IllegalStateException("Saldo no puede ser negativo");
        }
        
        usuario.setDinero(nuevoSaldo);
        usuarioRepository.actualizarDinero(usuarioId, nuevoSaldo);
    }
    
    private void pagarPremiosGanadores(int partidoId, String ganadorReal) {
        List<Apuesta> apuestas = apuestaRepository.obtenerPorPartidoConDetalles(partidoId);
        
        for (Apuesta apuesta : apuestas) {
            if (apuesta.getEstado() == 'G') { // Ganada
                double premio = apuesta.getMonto() * 2; // Doble del monto apostado
                actualizarSaldoUsuario(apuesta.getFkIdUsuario(), premio);
            }
        }
    }
    

    
    public List<Apuesta> obtenerApuestasPorUsuario(int usuarioId) {
        return apuestaRepository.obtenerPorUsuarioConDetalles(usuarioId);
    }
    
    public List<Apuesta> obtenerTodasLasApuestas() {
        return apuestaRepository.obtenerTodasConDetalles();
    }
    
    public Apuesta obtenerApuestaPorId(int apuestaId) {
        return apuestaRepository.obtenerPorId(apuestaId);
    }
    
    public double obtenerSaldoUsuario(int usuarioId) {
        return usuarioRepository.getDineroPorIdUsuario(usuarioId);
    }
    

    
    public void close() {
        apuestaRepository.close();
        usuarioRepository.close();
        partidoRepository.close();
        resultadoRepository.close();
    }
}