package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.ApuestaDAO;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.model.ResultadoDAO;
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

        // Verificar que no sea Admin
        if (usuario.puedeGestionarPartidos()) {
            mostrarError(request, response, "Los administradores no pueden realizar apuestas");
            return;
        }

        UsuarioService usuarioService = null;
        try {
            usuarioService = new UsuarioService();
            
            // Validar monto
            int monto = Integer.parseInt(request.getParameter("monto"));
            if (!validarMonto(usuario, monto)) {
                mostrarError(request, response, "Saldo insuficiente para la apuesta.");
                return;
            }

            // Crear apuesta
            Apuesta apuesta = crearApuestaDesdeRequest(request, usuario);
            procesarApuesta(apuesta, usuario, usuarioService);

            // Configurar respuesta
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
        }
    }

    private boolean validarMonto(UsuarioBase usuario, int monto) {
        // Verificar que sea Usuario (no Admin) y tenga saldo
        if (usuario instanceof Usuario) {
            Usuario usuarioNormal = (Usuario) usuario;
            return monto <= usuarioNormal.getDinero();
        }
        return false; // Admin no puede apostar
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

    private void procesarApuesta(Apuesta apuesta, UsuarioBase usuario, UsuarioService usuarioService) {
    ApuestaDAO apuestaDAO = new ApuestaDAO();
    
    if (!(usuario instanceof Usuario)) {
        throw new IllegalArgumentException("Los administradores no pueden apostar");
    }
    
    Usuario usuarioNormal = (Usuario) usuario;
    
    // Guardar apuesta con estado 'A' (Activa) - NO procesar resultado aún
    apuesta.setEstado('A'); // A = Activa (esperando resultado real)
    apuestaDAO.add(apuesta);
    
    // Restar el monto de la apuesta del saldo del usuario
    usuarioNormal.setDinero(usuarioNormal.getDinero() - apuesta.getMonto());
    usuarioService.updateDinero(usuarioNormal);
    
    // NO procesar el resultado inmediatamente si es "pendiente"
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    Resultado resultado = resultadoDAO.getResultadoByIdPartido(apuesta.getIdPartido());
    
    if (resultado != null && !"pendiente".equals(resultado.getGanador())) {
        // Solo procesar si el resultado NO es "pendiente"
        if (resultado.getGanador().equals(apuesta.getpor_quien())) {
            usuarioNormal.setDinero(usuarioNormal.getDinero() + (apuesta.getMonto() * 2));
            apuesta.setEstado('G'); // G = Ganada
        } else {
            apuesta.setEstado('P'); // P = Perdida
        }
        usuarioService.updateDinero(usuarioNormal);
        apuestaDAO.updateEstado(apuesta);
    }
    // Si es "pendiente", la apuesta queda como 'A' (Activa) hasta que el admin defina el resultado
}

private void configurarRespuesta(HttpServletRequest request, Apuesta apuesta, UsuarioBase usuario) {
    PartidoDAO partidoDAO = new PartidoDAO();
    Partido partido = partidoDAO.getPartidoPorId(apuesta.getIdPartido());
    
    request.setAttribute("apuesta", apuesta);
    request.setAttribute("partido", partido);
    request.setAttribute("premio", apuesta.getMonto() * 2);
    request.setAttribute("mensajeExito", "¡Apuesta realizada con éxito! El resultado se procesará cuando el partido finalice.");
    request.getSession().setAttribute("userLogueado", usuario);
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