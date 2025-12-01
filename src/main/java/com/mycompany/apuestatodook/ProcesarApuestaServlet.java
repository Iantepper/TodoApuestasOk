package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.ApuestaRepository;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoRepository;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.model.ResultadoRepository;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SvprocesarApuesta", urlPatterns = {"/SvprocesarApuesta"})
public class ProcesarApuestaServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        if (usuario == null) {
            redirigirALogin(request, response);
            return;
        }

        // auth
        if (!usuario.puedeApostar()) {
            mostrarError(request, response, "Este tipo de usuario no puede realizar apuestas");
            return;
        }

        UsuarioService usuarioService = null;
        ApuestaRepository apuestaRepo = null;
        ResultadoRepository resultadoRepo = null;
        
        try {
            usuarioService = new UsuarioService();
            apuestaRepo = new ApuestaRepository();
            resultadoRepo = new ResultadoRepository();
            

            int monto = Integer.parseInt(request.getParameter("monto"));
            int idPartido = Integer.parseInt(request.getParameter("idPartido"));
            String porQuien = request.getParameter("por");
            
 
            if (!(usuario instanceof Usuario)) {
                mostrarError(request, response, "Los administradores no pueden apostar");
                return;
            }
            
            Usuario usuarioNormal = (Usuario) usuario;
            

            if (monto <= 0) {
                mostrarError(request, response, "Monto debe ser mayor a 0");
                return;
            }
            

            double saldoActual = usuarioService.getDineroPorIdUsuario(usuarioNormal.getId());
            
 
            usuarioNormal.setDinero((int) saldoActual);

            request.getSession().setAttribute("userLogueado", usuarioNormal);
            
            if (monto > saldoActual) {
                mostrarError(request, response, 
                    String.format("Saldo insuficiente. Monto: $%d | Tu saldo: $%.0f", 
                    monto, saldoActual));
                return;
            }
            

            int idResultado = resultadoRepo.obtenerIdResultadoPorPartido(idPartido);
            Resultado resultado = resultadoRepo.obtenerPorPartido(idPartido);
            
          
            if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
                mostrarError(request, response, "No se puede apostar en este partido. El resultado ya está determinado.");
                return;
            }
            
            
            Apuesta apuesta = new Apuesta(monto, porQuien, 'A', usuarioNormal.getId(), idPartido, idResultado);
            
           
            usuarioNormal.setDinero((int)(saldoActual - monto));
            
            
            usuarioService.updateDinero(usuarioNormal);
            
            
            apuestaRepo.guardar(apuesta);
            
           
            if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
                
                if (resultado.getGanador().equals(apuesta.getpor_quien())) {
                    double nuevoSaldo = usuarioNormal.getDinero() + (apuesta.getMonto() * 2);
                    usuarioNormal.setDinero((int) nuevoSaldo);
                    apuesta.setEstado('G'); 
                } else {
                    apuesta.setEstado('P'); 
                }
                usuarioService.updateDinero(usuarioNormal);
                apuestaRepo.actualizarEstado(apuesta);
            }
            
     
            configurarRespuesta(request, apuesta, usuarioNormal);
            request.getRequestDispatcher("WEB-INF/jsp/ApuestaCreada.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            mostrarError(request, response, "Monto inválido. Ingrese un número válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError(request, response, "Error al procesar apuesta: " + e.getMessage());
        } finally {
            if (usuarioService != null) {
                usuarioService.close();
            }
            if (apuestaRepo != null) {
                apuestaRepo.close();
            }
            if (resultadoRepo != null) {
                resultadoRepo.close();
            }
        }
    }

    private void configurarRespuesta(HttpServletRequest request, Apuesta apuesta, UsuarioBase usuario) {
        PartidoRepository partidoRepo = null;
        try {
            partidoRepo = new PartidoRepository();
            
            Partido partido = partidoRepo.obtenerPorId(apuesta.getIdPartido());
            
            request.setAttribute("apuesta", apuesta);
            request.setAttribute("partido", partido);
            request.setAttribute("premio", apuesta.getMonto() * 2);
            request.setAttribute("mensajeExito", "¡Apuesta realizada con éxito! El resultado se procesará cuando el partido finalice.");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (partidoRepo != null) {
                partidoRepo.close();
            }
        }
    }

private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje) 
        throws ServletException, IOException {

    UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
    
    if (usuario instanceof Usuario) {
        UsuarioService usuarioService = null;
        try {
            usuarioService = new UsuarioService();
            double saldoActual = usuarioService.getDineroPorIdUsuario(usuario.getId());
            ((Usuario) usuario).setDinero((int) saldoActual);
            request.getSession().setAttribute("userLogueado", usuario);
            
            
            request.setAttribute("dineroUsuario", (int) saldoActual);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (usuarioService != null) {
                usuarioService.close();
            }
        }
    }
    
    String idPartidoStr = request.getParameter("idPartido");
    if (idPartidoStr != null) {
        try {
            PartidoRepository partidoRepo = new PartidoRepository();
            Partido partido = partidoRepo.obtenerPorId(Integer.parseInt(idPartidoStr));
            request.setAttribute("partido", partido);
            partidoRepo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    request.setAttribute("hayError", true);
    request.setAttribute("mensajeError", mensaje);
    request.getRequestDispatcher("WEB-INF/jsp/apuesta.jsp").forward(request, response);
}

    private void redirigirALogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", "Sesión expirada. Ingrese nuevamente!");
        request.getRequestDispatcher("WEB-INF/jsp/iniciosesion.jsp").forward(request, response);
    }
}