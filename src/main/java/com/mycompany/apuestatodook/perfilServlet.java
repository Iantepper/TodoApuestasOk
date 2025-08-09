package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Persona;
import com.mycompany.apuestatodook.model.PersonaDAO;
import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "SvPerfil", urlPatterns = "/Perfil")

public class perfilServlet extends HttpServlet{
    
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String destino;
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
        
        int IDusuario = usuario.getIDusuario();
        
        PersonaDAO PersonaDAO = new PersonaDAO();

        Persona persona = PersonaDAO.getPersonaPorId(IDusuario);
        request.setAttribute("persona", persona);
        
        destino = "WEB-INF/jsp/perfil.jsp";
        
        request.getRequestDispatcher(destino).forward(request, response);
    }
 
    
    
    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
    }
    
} 

