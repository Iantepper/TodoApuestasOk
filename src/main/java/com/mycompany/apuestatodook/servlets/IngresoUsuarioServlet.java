package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SvIngresosoUsuario", urlPatterns = {"/IngresoUsuario"})
public class IngresoUsuarioServlet extends HttpServlet {
    
    private UsuarioService usuarioService;

    @Override
    public void init() {
        this.usuarioService = new UsuarioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String origen = request.getParameter("origen");
        request.setAttribute("deDondeViene", origen);
        request.getRequestDispatcher("/WEB-INF/jsp/inicioSesion.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasenia = request.getParameter("Contraseña");
        
        try {

            UsuarioBase user = usuarioService.autenticar(usuario, contrasenia);
            
            if (user != null) {
                String haciaDondeIba = request.getParameter("deDondeViene");
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(3600);
                session.setAttribute("userLogueado", user);
                session.setAttribute("tipoUsuario", user.getTipo());
                
                if (haciaDondeIba != null && !haciaDondeIba.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + haciaDondeIba);
                } else {
                    response.sendRedirect(request.getContextPath() + "/Partidos");         
                }
            } else {
                request.setAttribute("hayError", true);
                request.setAttribute("mensajeError", "Credenciales incorrectas!");
                doGet(request, response);
            }
        } catch (IllegalArgumentException e) {
 
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Error en el sistema.");
            doGet(request, response);
        }
    }

    @Override
    public void destroy() {
        if (usuarioService != null) usuarioService.close();
    }
}