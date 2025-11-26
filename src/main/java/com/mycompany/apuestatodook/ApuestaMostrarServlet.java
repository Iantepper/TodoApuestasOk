package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.ApuestaRepository;
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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");

        ApuestaRepository apuestaRepo = null;
        try {
            apuestaRepo = new ApuestaRepository();
            
            if (usuario != null && usuario.puedeGestionarPartidos()) {
                request.setAttribute("esAdmin", true);
                request.setAttribute("mensajeAdmin", "Modo Administrador");    
                
                // ✅ NUEVO: Usar Repository para obtener todas las apuestas
                List<Apuesta> apuestas = apuestaRepo.obtenerTodasConDetalles();
                request.setAttribute("apuestas", apuestas);
                
            } else if (usuario != null && "user".equalsIgnoreCase(usuario.getTipo())) {
                // ✅ NUEVO: Usar Repository para obtener apuestas del usuario
                List<Apuesta> apuestas = apuestaRepo.obtenerPorUsuarioConDetalles(usuario.getId());
                request.setAttribute("apuestas", apuestas);
                
            } else {
                request.setAttribute("hayError", true);
                request.setAttribute("mensajeError", "Por favor, inicie sesión.");
                request.getRequestDispatcher("WEB-INF/jsp/inicioSesion.jsp").forward(request, response);
                return;
            }
            
            request.getRequestDispatcher("WEB-INF/jsp/apuestasMostrar.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback opcional al DAO si es necesario
        } finally {
            if (apuestaRepo != null) {
                apuestaRepo.close();
            }
        }
    }
}