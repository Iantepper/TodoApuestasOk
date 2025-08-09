package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SvIngresosoUsuario", urlPatterns = {"/IngresoUsuario"})
public class IngresoUsuarioServlet extends HttpServlet {
    
    
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
        String n = request.getParameter("usuario");
        String p = request.getParameter("Contraseña");
        Usuario user = new UsuarioDAO().autenticar(n, p);
        if (user != null) {
      
            String haciaDondeIba = request.getParameter("deDondeViene");
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(3600);
            session.setAttribute("userLogueado", user);
            session.setAttribute("tipoUsuario", user.getTipo());
           if (haciaDondeIba != null) {
                response.sendRedirect(request.getContextPath() + haciaDondeIba);
            } else {
                response.sendRedirect(request.getContextPath() + "/principaliniciado.jsp");            
            }
            }else {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Credenciales incorrectas!");
            doGet(request, response);

        }
    }
    
    

}
