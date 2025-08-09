package com.mycompany.apuestatodook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.mycompany.apuestatodook.model.PartidoDAO;


@WebServlet(name = "SvPartidos", urlPatterns = {"/Partidos"})
public class PartidosServlet extends HttpServlet {

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PartidoDAO partidosDAO = new PartidoDAO();
        String destino;
        
        request.setAttribute("listaDePartidos", partidosDAO.getAll());
        destino = "WEB-INF/jsp/partidos.jsp";
        
        request.getRequestDispatcher(destino).forward(request, response);
    }

   



}
