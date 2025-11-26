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
        if (usuario.puedeGestionarPartidos()) {
            mostrarError(request, response, "Los administradores no pueden realizar apuestas");
            return;
        }

        UsuarioService usuarioService = null;
        ApuestaRepository apuestaRepo = null;
        try {
            usuarioService = new UsuarioService();
            apuestaRepo = new ApuestaRepository();
            
            // auth
            int monto = Integer.parseInt(request.getParameter("monto"));
            if (!validarMonto(usuario, monto)) {
                mostrarError(request, response, "Saldo insuficiente para la apuesta.");
                return;
            }


            Apuesta apuesta = crearApuestaDesdeRequest(request, usuario);
            
            //guardar apuesta
            procesarApuesta(apuesta, usuario, usuarioService, apuestaRepo);

 
            configurarRespuesta(request, apuesta, usuario);
            request.getRequestDispatcher("WEB-INF/jsp/ApuestaCreada.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            mostrarError(request, response, "Monto inválido");
        } catch (Exception e) {
            mostrarError(request, response, "Error al procesar apuesta: " + e.getMessage());
        } finally {
            if (usuarioService != null) {
                usuarioService.close();
            }
            if (apuestaRepo != null) {
                apuestaRepo.close();
            }
        }
    }

    private boolean validarMonto(UsuarioBase usuario, int monto) {
        if (usuario instanceof Usuario) {
            Usuario usuarioNormal = (Usuario) usuario;
            return monto <= usuarioNormal.getDinero();
        }
        return false;
    }

    private Apuesta crearApuestaDesdeRequest(HttpServletRequest request, UsuarioBase usuario) {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        String porQuien = request.getParameter("por");
        int monto = Integer.parseInt(request.getParameter("monto"));
        
        ResultadoRepository resultadoRepo = null;
        try {
            resultadoRepo = new ResultadoRepository();
            int idResultado = resultadoRepo.obtenerIdResultadoPorPartido(idPartido);
            
            System.out.println("🎯 CREANDO APUESTA CON RESULTADO REPOSITORY - " +
                              "Usuario: " + usuario.getId() + 
                              ", Partido: " + idPartido + 
                              ", Resultado: " + idResultado);
            
            return new Apuesta(monto, porQuien, 'A', usuario.getId(), idPartido, idResultado);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR al obtener resultado: " + e.getMessage());
            throw new RuntimeException("No se puede apostar en este partido aún. El resultado no está disponible.");
        } finally {
            if (resultadoRepo != null) {
                resultadoRepo.close();
            }
        }
    }

    private void procesarApuesta(Apuesta apuesta, UsuarioBase usuario, UsuarioService usuarioService, ApuestaRepository apuestaRepo) {
        if (!(usuario instanceof Usuario)) {
            throw new IllegalArgumentException("Los administradores no pueden apostar");
        }
        
        Usuario usuarioNormal = (Usuario) usuario;
        

        apuestaRepo.guardar(apuesta);
        

        usuarioNormal.setDinero(usuarioNormal.getDinero() - apuesta.getMonto());
        usuarioService.updateDinero(usuarioNormal);
        

        ResultadoRepository resultadoRepo = null;
        try {
            resultadoRepo = new ResultadoRepository();
            Resultado resultado = resultadoRepo.obtenerPorPartido(apuesta.getIdPartido());
            
            if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
                //  no pendiene
                if (resultado.getGanador().equals(apuesta.getpor_quien())) {
                    usuarioNormal.setDinero(usuarioNormal.getDinero() + (apuesta.getMonto() * 2));
                    apuesta.setEstado('G'); // G = Ganada
                } else {
                    apuesta.setEstado('P'); // P = Perdida
                }
                usuarioService.updateDinero(usuarioNormal);
                apuestaRepo.actualizarEstado(apuesta);
            }

            
        } catch (Exception e) {
            System.out.println("⚠️  Error al verificar resultado: " + e.getMessage());
        } finally {
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
            request.getSession().setAttribute("userLogueado", usuario);
            
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
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("WEB-INF/jsp/apuesta.jsp").forward(request, response);
    }

    private void redirigirALogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", "Ingrese Nuevamente!");
        request.getRequestDispatcher("WEB-INF/jsp/iniciosesion.jsp").forward(request, response);
    }
}