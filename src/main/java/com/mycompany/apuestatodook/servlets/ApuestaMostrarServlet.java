package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.services.ApuestaService; // OJO: Service, no Repo
import com.mycompany.apuestatodook.model.UsuarioBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ApuestaMostrarServlet", urlPatterns = {"/ApuestasMostrar"})
public class ApuestaMostrarServlet extends HttpServlet {
    
    private ApuestaService apuestaService;

    @Override
    public void init() {
        this.apuestaService = new ApuestaService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");

        try {
            if (usuario != null && usuario.puedeGestionarPartidos()) {
                request.setAttribute("esAdmin", true);
                request.setAttribute("mensajeAdmin", "Modo Administrador");    
                
                // todas
                List<Apuesta> apuestas = apuestaService.obtenerTodasLasApuestas();
                request.setAttribute("apuestas", apuestas);
                
            } else if (usuario != null && "user".equalsIgnoreCase(usuario.getTipo())) {
                // por usuario
                List<Apuesta> apuestas = apuestaService.obtenerApuestasPorUsuario(usuario.getId());
                request.setAttribute("apuestas", apuestas);
                
            } else {
                request.setAttribute("hayError", true);
                request.setAttribute("mensajeError", "Por favor, inicie sesión.");
                request.getRequestDispatcher("WEB-INF/jsp/inicioSesion.jsp").forward(request, response);
                return;
            }
            
            request.getRequestDispatcher("WEB-INF/jsp/apuestasMostrar.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error en Mostrar Apuesta: " + e.getMessage());
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Error al cargar las apuestas.");
            request.getRequestDispatcher("WEB-INF/jsp/apuestasMostrar.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        if (apuestaService != null) apuestaService.close();
    }
}