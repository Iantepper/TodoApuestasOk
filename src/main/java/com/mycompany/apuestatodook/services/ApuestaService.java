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
    

    public void validarApuesta(int usuarioId, int monto, int partidoId) {

        Usuario usuario = (Usuario) usuarioRepository.autenticarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        

        if (!usuario.tieneSaldoSuficiente(monto)) {
            throw new IllegalStateException("Saldo insuficiente. Saldo actual: $" + usuario.getDinero());
        }
        

        Partido partido = partidoRepository.obtenerPorId(partidoId);
        if (partido == null) {
            throw new IllegalArgumentException("Partido no encontrado");
        }
        

        Resultado resultado = resultadoRepository.obtenerPorPartido(partidoId);
        if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
            throw new IllegalStateException("No se puede apostar en este partido. El resultado ya está determinado.");
        }
    }
    

    public Apuesta crearApuesta(int monto, String porQuien, int usuarioId, int partidoId) {
        try {

            validarApuesta(usuarioId, monto, partidoId);
            

            int resultadoId = resultadoRepository.obtenerIdResultadoPorPartido(partidoId);
            

            Apuesta apuesta = new Apuesta(monto, porQuien, 'A', usuarioId, partidoId, resultadoId);
            
   
            Usuario usuario = (Usuario) usuarioRepository.autenticarPorId(usuarioId);
            double nuevoSaldo = usuario.getDinero() - monto;
            usuario.setDinero(nuevoSaldo);
            usuarioRepository.actualizarDinero(usuarioId, nuevoSaldo);
            

            apuestaRepository.guardar(apuesta);
            
            return apuesta;
            
        } catch (Exception e) {

            System.err.println("Error en ApuestaService.crearApuesta: " + e.getMessage());
            throw new RuntimeException("Error al crear apuesta: " + e.getMessage(), e);
        }
    }
    

    public List<Apuesta> obtenerApuestasPorUsuario(int usuarioId) {
        return apuestaRepository.obtenerPorUsuarioConDetalles(usuarioId);
    }
    

    public List<Apuesta> obtenerTodasLasApuestas() {
        return apuestaRepository.obtenerTodasConDetalles();
    }
    

    public void close() {
        apuestaRepository.close();
        usuarioRepository.close();
        partidoRepository.close();
        resultadoRepository.close();
    }
}