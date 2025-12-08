package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.services.PartidoService;
import com.mycompany.apuestatodook.model.Partido;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ResultadosServlet", urlPatterns = {"/Resultados"})
public class ResultadosServlet extends HttpServlet {
    
    
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        

        PartidoService partidoService = null;
        
        try {
            partidoService = new PartidoService();
            
            List<Partido> partidosConResultado = partidoService.obtenerPartidosConResultado();
            
            request.setAttribute("partidosConResultado", partidosConResultado);
            request.getRequestDispatcher("WEB-INF/jsp/resultados.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error en ResultadosServlet: " + e.getMessage());
            request.setAttribute("error", "Error al cargar resultados");
            request.getRequestDispatcher("WEB-INF/jsp/resultados.jsp").forward(request, response);
        } finally {
            if (partidoService != null) {
                partidoService.close();
            }
        }
    }
}