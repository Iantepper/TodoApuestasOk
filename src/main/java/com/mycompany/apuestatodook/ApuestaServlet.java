package com.mycompany.apuestatodook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioDAO;

@WebServlet(name = "SvApuesta", urlPatterns = {"/Apuesta"})
public class ApuestaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int partidoId = Integer.parseInt(request.getParameter("id"));
        
        PartidoDAO PartidoDAO = new PartidoDAO();
        
        Partido partido = PartidoDAO.getPartidoPorId(partidoId);      
        
        request.setAttribute("partido", partido);
        Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        double dineroUsuario = usuarioDAO.getDineroPorIdUsuario(usuario.getIDusuario());

        usuario.setDinero(dineroUsuario);
        request.getSession().setAttribute("userLogueado", usuario);
        
        request.setAttribute("dineroUsuario", dineroUsuario);
        
        
        request.getRequestDispatcher("WEB-INF/jsp/apuesta.jsp").forward(request, response);
    }
}