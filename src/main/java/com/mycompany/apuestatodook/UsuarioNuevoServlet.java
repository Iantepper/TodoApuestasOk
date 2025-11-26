package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NuevoUsuario", urlPatterns = {"/NuevoUsuario"})
public class UsuarioNuevoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String edadStr = request.getParameter("edad");
        int edad = 0;
        
        if(edadStr != null && !edadStr.isEmpty()){
          edad = Integer.parseInt(edadStr);  
        }
        
        String dni = request.getParameter("dni");

        if((nombre == null || nombre.isEmpty()) || (usuario == null || usuario.isEmpty()) || 
           password == null || password.isEmpty() || cpassword == null || cpassword.isEmpty() || 
           apellido == null || apellido.isEmpty() || dni == null || dni.isEmpty()) {
           request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Se deben completar todos los campos.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            return;
        } else if (edad < 18) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "La edad debe ser mayor o igual a 18 años.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            return;
        } else if (!password.equals(cpassword)) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            return;
        } else {
            UsuarioRepository usuarioRepo = null;
            try {
                usuarioRepo = new UsuarioRepository();
                
                // Verificar si usuario ya existe
                if (usuarioRepo.existeUsuario(usuario)) {
                    request.setAttribute("hayError", true);
                    request.setAttribute("mensajeError", "El nombre de usuario ya existe.");
                    request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
                    return;
                }
                
                int idUsuario = usuarioRepo.crearUsuario(usuario, password, nombre, apellido, edad, dni);
                request.getRequestDispatcher("/WEB-INF/jsp/usuarioCreado.jsp").forward(request, response);
                
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("hayError", true);
                request.setAttribute("mensajeError", "Error al crear usuario. Intente más tarde.");
                request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
            } finally {
                if (usuarioRepo != null) {
                    usuarioRepo.close();
                }
            }
        }
    }
}