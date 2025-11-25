package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SvPerfil", urlPatterns = "/Perfil")
public class PerfilServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        

        if (usuario != null && usuario.puedeGestionarPartidos()) {
            response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
            return;
        }
        

        if (!(usuario instanceof Usuario)) {
            response.sendRedirect(request.getContextPath() + "/Partidos");
            return;
        }
        
 
        Usuario usuarioNormal = (Usuario) usuario;
        
  
        request.setAttribute("usuario", usuarioNormal);
        
        request.getRequestDispatcher("WEB-INF/jsp/perfil.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //actualizaciones de perfil
    }
}