package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.services.ApuestaService;
import com.mycompany.apuestatodook.services.PartidoService;
import com.mycompany.apuestatodook.model.Apuesta;
import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProcesarApuestaController", urlPatterns = {"/SvprocesarApuesta"})
public class ProcesarApuestaServlet extends HttpServlet {
    


    
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ApuestaService apuestaService = new ApuestaService();
        PartidoService partidoService = new PartidoService();
        
        try {
            UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");

            if (usuario == null) {
                redirigirALogin(request, response);
            
 
            } else if (!usuario.puedeApostar() || !(usuario instanceof Usuario)) {
                mostrarError(request, response, "Este tipo de usuario no puede realizar apuestas");
            

            } else {
                int monto = Integer.parseInt(request.getParameter("monto"));
                int partidoId = Integer.parseInt(request.getParameter("idPartido"));
                String porQuien = request.getParameter("por");
                
                Apuesta apuesta = apuestaService.crearApuesta(monto, porQuien, usuario.getId(), partidoId);
                
                Partido partido = partidoService.obtenerPartido(partidoId);
                
                request.setAttribute("apuesta", apuesta);
                request.setAttribute("partido", partido);
                request.setAttribute("premio", apuesta.getMonto() * 2);
                request.setAttribute("mensajeExito", "¡Apuesta realizada con éxito!");
                
                request.getRequestDispatcher("WEB-INF/jsp/ApuestaCreada.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            mostrarError(request, response, "Monto inválido. Ingrese un número válido.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(request, response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); 
            mostrarError(request, response, "Error al procesar apuesta: " + e.getMessage());
        } finally {
            if (apuestaService != null) apuestaService.close();
            if (partidoService != null) partidoService.close();
        }
    }
    
    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje) 
            throws ServletException, IOException {
        
 
        String partidoId = request.getParameter("idPartido");
        
        // no rompa espacios, tildes
        String mensajeCodificado = java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);

        if (partidoId != null) {
            response.sendRedirect(request.getContextPath() + "/Apuesta?id=" + partidoId + "&error=true&msg=" + mensajeCodificado);
        } else {
            response.sendRedirect(request.getContextPath() + "/Partidos");
        }
    }
    
    private void redirigirALogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", "Sesión expirada. Ingrese nuevamente!");
        request.getRequestDispatcher("WEB-INF/jsp/inicioSesion.jsp").forward(request, response);
    }
    
}