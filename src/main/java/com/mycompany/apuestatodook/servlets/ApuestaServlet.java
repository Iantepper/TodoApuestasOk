package com.mycompany.apuestatodook.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.services.PartidoService;
import com.mycompany.apuestatodook.services.UsuarioService;

@WebServlet(name = "SvApuesta", urlPatterns = {"/Apuesta"})
public class ApuestaServlet extends HttpServlet {

    private PartidoService partidoService;
    private UsuarioService usuarioService;

    @Override
    public void init() {
        this.partidoService = new PartidoService();
        this.usuarioService = new UsuarioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
    
        if (usuario != null && usuario.puedeGestionarPartidos()) {
            response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
            return;
        }
        String error = request.getParameter("error");
    String msg = request.getParameter("msg");
    
    if ("true".equals(error) && msg != null) {
        request.setAttribute("hayError", true);
        // Decodificar el mensaje (quitar los %20, etc.)
        request.setAttribute("mensajeError", java.net.URLDecoder.decode(msg, java.nio.charset.StandardCharsets.UTF_8));
    }
        try {
            int partidoId = Integer.parseInt(request.getParameter("id"));
            
            // sericios
            Partido partido = partidoService.obtenerPartido(partidoId);      
            double dineroUsuario = usuarioService.getDineroPorIdUsuario(usuario.getId());


            if (usuario instanceof Usuario) {
                ((Usuario) usuario).setDinero(dineroUsuario);
            }
            
            request.setAttribute("partido", partido);
            request.setAttribute("dineroUsuario", dineroUsuario);
            request.getRequestDispatcher("WEB-INF/jsp/apuesta.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error en Apuesta: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Partidos");
        }
    }

    @Override
    public void destroy() {
        if (partidoService != null) partidoService.close();
        if (usuarioService != null) usuarioService.close();
    }
}