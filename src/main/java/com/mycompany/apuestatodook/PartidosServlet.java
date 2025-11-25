package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvPartidos", urlPatterns = {"/Partidos"})
public class PartidosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PartidoDAO partidosDAO = new PartidoDAO();
        List<Partido> partidos = partidosDAO.getAll();
        request.setAttribute("listaDePartidos", partidos);
        
        // Verificar si es admin y quiere ver vista admin
        Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
        boolean esAdmin = usuario != null && "admin".equals(usuario.getTipo());
        boolean modoAdmin = "true".equals(request.getParameter("admin"));
        
        if (esAdmin && modoAdmin) {
            request.setAttribute("esAdmin", true);
            request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
        }
    }
}