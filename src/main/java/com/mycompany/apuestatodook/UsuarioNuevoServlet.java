
package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.PersonaDAO;
import com.mycompany.apuestatodook.model.UsuarioDAO;
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
        
        if(edadStr != ""){
          edad = Integer.parseInt(edadStr);  
        }
        
        String dni = request.getParameter("dni");

         if((nombre.equals("")) || ("" == usuario) || "" == password || "" == cpassword || "" == apellido || "" == dni) {
           request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Se deben completar todos los campos.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
         }
        else if (edad < 18) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "La edad debe ser mayor o igual a 18 años.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
        } else if (!password.equals(cpassword)) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/WEB-INF/jsp/crearUsuario.jsp").forward(request, response);
        } else {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int idUsuario = usuarioDAO.add(usuario, password);
            PersonaDAO personaDAO = new PersonaDAO();
            personaDAO.agregarPersona(idUsuario, nombre, apellido, edad, dni);
            request.getRequestDispatcher("/WEB-INF/jsp/usuarioCreado.jsp").forward(request, response);
        }
    }
}
