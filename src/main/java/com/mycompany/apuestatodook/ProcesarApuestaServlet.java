package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.ApuestaDAO;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.model.ResultadoDAO;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SvprocesarApuesta", urlPatterns = {"/SvprocesarApuesta"})
public class ProcesarApuestaServlet extends HttpServlet {
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
    if (usuario == null) {
        redirigirALogin(request, response);
        return;
    }

    try {
        // Validar monto
        int monto = Integer.parseInt(request.getParameter("monto"));
        if (!validarMonto(usuario, monto)) {
            mostrarError(request, response, "Saldo insuficiente para la apuesta.");
            return;
        }

        // Crear apuesta
        Apuesta apuesta = crearApuestaDesdeRequest(request, usuario);
        procesarApuesta(apuesta, usuario);

        // Configurar respuesta
        configurarRespuesta(request, apuesta, usuario);
        request.getRequestDispatcher("WEB-INF/jsp/ApuestaCreada.jsp").forward(request, response);

    } catch (NumberFormatException e) {
        mostrarError(request, response, "Monto inválido");
    } catch (Exception e) {
        mostrarError(request, response, "Error al procesar apuesta: " + e.getMessage());
    }
}

private boolean validarMonto(Usuario usuario, int monto) {
    return monto <= usuario.getDinero();
}

private Apuesta crearApuestaDesdeRequest(HttpServletRequest request, Usuario usuario) {
    int idPartido = Integer.parseInt(request.getParameter("idPartido"));
    String porQuien = request.getParameter("por");
    int monto = Integer.parseInt(request.getParameter("monto"));
    
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    int idResultado = resultadoDAO.getIdResultadoByIdPartido(idPartido);
    
    return new Apuesta(monto, porQuien, usuario.getIDusuario(), idPartido, idResultado);
}

private void procesarApuesta(Apuesta apuesta, Usuario usuario) {
    ApuestaDAO apuestaDAO = new ApuestaDAO();
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    // Guardar apuesta
    apuestaDAO.add(apuesta);
    
    // Procesar resultado
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    Resultado resultado = resultadoDAO.getResultadoByIdPartido(apuesta.getIdPartido());
    
    if (resultado != null && resultado.getGanador().equals(apuesta.getpor_quien())) {
        usuario.setDinero(usuario.getDinero() + apuesta.getMonto());
        apuesta.setEstado('G');
    } else {
        usuario.setDinero(usuario.getDinero() - apuesta.getMonto());
        apuesta.setEstado('P');
    }
    
    // Actualizar en BD
    usuarioDAO.updateDinero(usuario);
    apuestaDAO.updateEstado(apuesta);
}

private void configurarRespuesta(HttpServletRequest request, Apuesta apuesta, Usuario usuario) {
    PartidoDAO partidoDAO = new PartidoDAO();
    Partido partido = partidoDAO.getPartidoPorId(apuesta.getIdPartido());
    
    request.setAttribute("apuesta", apuesta);
    request.setAttribute("partido", partido);
    request.setAttribute("premio", apuesta.getMonto() * 2);
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
