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

    private final PartidoDAO partidoDAO = new PartidoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar si es admin y quiere ver vista admin
        Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
        boolean esAdmin = usuario != null && "admin".equals(usuario.getTipo());
        boolean modoAdmin = "true".equals(request.getParameter("admin"));
        
        // Manejar eliminación si es admin
        if (esAdmin && request.getParameter("eliminar") != null) {
            int idPartido = Integer.parseInt(request.getParameter("eliminar"));
            partidoDAO.delete(idPartido);
        }
        
        List<Partido> partidos = partidoDAO.getAll();
        request.setAttribute("listaDePartidos", partidos);
        
        if (esAdmin && modoAdmin) {
            request.setAttribute("esAdmin", true);
        }
        
        request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
        if (usuario == null || !"admin".equals(usuario.getTipo())) {
            response.sendRedirect(request.getContextPath() + "/Index?action=inicioSesion");
            return;
        }

        // Crear nuevo partido
        String local = request.getParameter("local");
        String visitante = request.getParameter("visitante");
        String fecha = request.getParameter("fecha");

        if (local != null && !local.trim().isEmpty() && 
            visitante != null && !visitante.trim().isEmpty() &&
            fecha != null && !fecha.trim().isEmpty()) {
            
            Partido partido = new Partido(local, visitante, fecha, 0);
            partidoDAO.add(partido);
        }
        
        response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
    }
}