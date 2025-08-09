package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.ApuestaDAO;
import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ApuestaMostrarServlet", urlPatterns = {"/ApuestasMostrar"})
public class ApuestaMostrarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");

        if (usuario != null && usuario.getTipo().equalsIgnoreCase("admin"))
            {
            request.setAttribute("esAdmin", true);
            request.setAttribute("mensajeAdmin", "Modo Administrador");    
            ApuestaDAO apuestaDAO = new ApuestaDAO();
            List<Apuesta> apuestas = apuestaDAO.getAllApuestasConResultado();
            request.setAttribute("apuestas", apuestas);
            request.getRequestDispatcher("WEB-INF/jsp/apuestasMostrar.jsp").forward(request, response);
        } else if (usuario != null && usuario.getTipo().equalsIgnoreCase("user")) 
            {
            ApuestaDAO apuestaDAO = new ApuestaDAO();
            List<Apuesta> apuestas = apuestaDAO.getApuestasConResultadoPorUsuario(usuario.getIDusuario());
            request.setAttribute("apuestas", apuestas);
            request.getRequestDispatcher("WEB-INF/jsp/apuestasMostrar.jsp").forward(request, response);
        }else {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Por favor, inicie sesión.");
            request.getRequestDispatcher("WEB-INF/jsp/iniciosesion.jsp").forward(request, response);
        }
    }
}