package com.mycompany.apuestatodook.servlets;


import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        //si action es null
        String destino = "/WEB-INF/jsp/inicioSesion.jsp"; 

        if ("inicioSesion".equals(action)) {
            destino = "/WEB-INF/jsp/inicioSesion.jsp";
        } else if ("newUsuarios".equals(action)) {
            destino = "/WEB-INF/jsp/crearUsuario.jsp";
        } else if ("errorIngresoUsuario".equals(action)) {
            destino = "/WEB-INF/jsp/errorIngresoUsuario.jsp";
        } else if ("usuarioCreado".equals(action)) {
            destino = "/WEB-INF/jsp/usuarioCreado.jsp";
        }

        request.getRequestDispatcher(destino).forward(request, response);
    }

}