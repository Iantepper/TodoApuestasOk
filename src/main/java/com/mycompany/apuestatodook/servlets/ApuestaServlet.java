package com.mycompany.apuestatodook.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.repositories.PartidoRepository;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.services.UsuarioService;

@WebServlet(name = "SvApuesta", urlPatterns = {"/Apuesta"})
public class ApuestaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
    
        if (usuario != null && usuario.puedeGestionarPartidos()) {
            response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
            return;
        }
        
        int partidoId = Integer.parseInt(request.getParameter("id"));
        
        PartidoRepository partidoRepo = null;
        UsuarioService usuarioService = null;
        
        try {
            partidoRepo = new PartidoRepository();
            usuarioService = new UsuarioService();
            
            Partido partido = partidoRepo.obtenerPorId(partidoId);      
            request.setAttribute("partido", partido);

            double dineroUsuario = usuarioService.getDineroPorIdUsuario(usuario.getId());

            if (usuario instanceof Usuario) {
                Usuario usuarioNormal = (Usuario) usuario;
                usuarioNormal.setDinero(dineroUsuario);
            }
            
            request.setAttribute("dineroUsuario", dineroUsuario);
            request.getRequestDispatcher("WEB-INF/jsp/apuesta.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error en Apuesta]: " + e.getMessage());

        } finally {
            if (partidoRepo != null) {
                partidoRepo.close();
            }
            if (usuarioService != null) {
                usuarioService.close();
            }
        }
    }
}