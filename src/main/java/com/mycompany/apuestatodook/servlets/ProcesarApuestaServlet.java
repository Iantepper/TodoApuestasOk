package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.services.ApuestaService;
import com.mycompany.apuestatodook.services.PartidoService;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProcesarApuestaController", urlPatterns = {"/procesarApuesta"})
public class ProcesarApuestaServlet extends HttpServlet {
    
    private ApuestaService apuestaService;
    private PartidoService partidoService;
    
    @Override
    public void init() throws ServletException {
        this.apuestaService = new ApuestaService();
        this.partidoService = new PartidoService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        
        // 1. Validar sesión
        if (usuario == null) {
            redirigirALogin(request, response);
            return;
        }
        
        // 2. Validar tipo de usuario
        if (!usuario.puedeApostar() || !(usuario instanceof Usuario)) {
            mostrarError(request, response, "Este tipo de usuario no puede realizar apuestas");
            return;
        }
        
        try {
            // 3. Obtener parámetros
            int monto = Integer.parseInt(request.getParameter("monto"));
            int partidoId = Integer.parseInt(request.getParameter("idPartido"));
            String porQuien = request.getParameter("por");
            
            //Llamar al servicio 
            var apuesta = apuestaService.crearApuesta(monto, porQuien, usuario.getId(), partidoId);
            
            // Obtener datos 
            var partido = partidoService.obtenerPartido(partidoId);
            
 
            request.setAttribute("apuesta", apuesta);
            request.setAttribute("partido", partido);
            request.setAttribute("premio", apuesta.getMonto() * 2);
            request.setAttribute("mensajeExito", "¡Apuesta realizada con éxito!");
            
 
            request.getRequestDispatcher("WEB-INF/jsp/ApuestaCreada.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            mostrarError(request, response, "Monto inválido. Ingrese un número válido.");
        } catch (IllegalArgumentException | IllegalStateException e) {

            mostrarError(request, response, e.getMessage());
        } catch (Exception e) {

            mostrarError(request, response, "Error al procesar apuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            apuestaService.close();
            partidoService.close();
        }
    }
    
    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje) 
            throws ServletException, IOException {
        
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", mensaje);
        
        // Redirigir a la página de apuesta con el error
        String partidoId = request.getParameter("idPartido");
        if (partidoId != null) {
            response.sendRedirect(request.getContextPath() + "/apuesta?id=" + partidoId);
        } else {
            request.getRequestDispatcher("WEB-INF/jsp/apuesta.jsp").forward(request, response);
        }
    }
    
    private void redirigirALogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", "Sesión expirada. Ingrese nuevamente!");
        request.getRequestDispatcher("WEB-INF/jsp/inicioSesion.jsp").forward(request, response);
    }
    
    @Override
    public void destroy() {
        if (apuestaService != null) {
            apuestaService.close();
        }
        if (partidoService != null) {
            partidoService.close();
        }
    }
}