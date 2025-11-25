package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Resultado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;

@WebServlet(name = "SvPartidos", urlPatterns = {"/Partidos"})
public class PartidosServlet extends HttpServlet {

    private final PartidoDAO partidoDAO = new PartidoDAO();

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    // Verificar si es admin y quiere ver vista admin
    UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
    boolean esAdmin = usuario != null && usuario.puedeGestionarPartidos();
    boolean modoAdmin = "true".equals(request.getParameter("admin"));
    
    // Manejar eliminación si es admin
    if (esAdmin && request.getParameter("eliminar") != null) {
        int idPartido = Integer.parseInt(request.getParameter("eliminar"));
        partidoDAO.delete(idPartido);
    }
    
    List<Partido> partidos = partidoDAO.getAll();
    
    // Para admin, cargar los resultados de cada partido
    if (esAdmin && modoAdmin) {
        for (Partido partido : partidos) {
            Resultado resultado = partidoDAO.getResultadoPorPartido(partido.getIdPartido());
            partido.setResultado(resultado);
        }
    }
    
    request.setAttribute("listaDePartidos", partidos);
    
    if (esAdmin && modoAdmin) {
        request.setAttribute("esAdmin", true);
    }
    
    request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
    if (usuario == null || !usuario.puedeGestionarPartidos()) {
        response.sendRedirect(request.getContextPath() + "/Index?action=inicioSesion");
        return;
    }

    // Si es para actualizar resultado
    if (request.getParameter("resultadoPartido") != null) {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        String ganador = request.getParameter("ganador");
        
        partidoDAO.actualizarResultado(idPartido, ganador);
        
    } else {
        // Crear nuevo partido (con validación de fecha)
        String local = request.getParameter("local");
        String visitante = request.getParameter("visitante");
        String fecha = request.getParameter("fecha");

        if (local != null && !local.trim().isEmpty() && 
            visitante != null && !visitante.trim().isEmpty() &&
            fecha != null && !fecha.trim().isEmpty()) {
            
            // Validar que la fecha no sea pasada
            if (esFechaFutura(fecha)) {
                Partido partido = new Partido(local, visitante, fecha, 0);
                partidoDAO.add(partido);
            } else {
                // Podés agregar un mensaje de error aquí
                System.out.println("No se puede agregar partido con fecha pasada");
            }
        }
    }
    
    response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
}

// Método para validar fecha futura
private boolean esFechaFutura(String fechaStr) {
    try {
        LocalDateTime fechaPartido = LocalDateTime.parse(fechaStr.replace(" ", "T"));
        return fechaPartido.isAfter(LocalDateTime.now());
    } catch (Exception e) {
        return false;
    }
}
}