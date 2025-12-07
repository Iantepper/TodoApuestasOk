package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.repositories.PartidoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvResultados", urlPatterns = {"/Resultados"})
public class ResultadosServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        PartidoRepository partidoRepo = null;
        try {
            partidoRepo = new PartidoRepository();
            
            // partidos con resultado
            List<Partido> partidosConResultado = partidoRepo.obtenerPartidosConResultado();
            
            request.setAttribute("partidosConResultado", partidosConResultado);
            request.getRequestDispatcher("WEB-INF/jsp/resultados.jsp").forward(request, response);
            
        } catch (Exception e) {
System.err.println("❌ Error en en mostrar resultados: " + e.getMessage());
        } finally {
            if (partidoRepo != null) {
                partidoRepo.close();
            }
        }
    }
}