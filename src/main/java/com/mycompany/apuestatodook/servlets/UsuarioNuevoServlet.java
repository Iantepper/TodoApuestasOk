package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NuevoUsuario", urlPatterns = {"/NuevoUsuario"})
public class UsuarioNuevoServlet extends HttpServlet {

    private UsuarioService usuarioService;

    @Override
    public void init() {
        this.usuarioService = new UsuarioService();
    }

@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String edadStr = request.getParameter("edad");

        // Validación ui
        if (password != null && !password.equals(cpassword)) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            
        } else {
            try {
                int edad = (edadStr != null && !edadStr.isEmpty()) ? Integer.parseInt(edadStr) : 0;

                usuarioService.crearUsuario(usuario, password, nombre, apellido, edad, dni);

                request.getRequestDispatcher("/WEB-INF/jsp/usuarioCreado.jsp").forward(request, response);

            } catch (IllegalArgumentException e) {
                request.setAttribute("hayError", true);
                request.setAttribute("mensajeError", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            } catch (Exception e) {
                System.err.println("❌ Error sistema: " + e.getMessage());
                request.setAttribute("hayError", true);
                request.setAttribute("mensajeError", "Error del sistema. Intente más tarde.");
                request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        if (usuarioService != null) usuarioService.close();
    }
}