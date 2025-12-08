package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.services.PartidoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvPartidos", urlPatterns = {"/Partidos"})
public class PartidosServlet extends HttpServlet {

    private PartidoService partidoService;

    @Override
    public void init() {
        this.partidoService = new PartidoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        boolean esAdmin = usuario != null && usuario.puedeGestionarPartidos();
        boolean modoAdmin = "true".equals(request.getParameter("admin"));

        List<Partido> partidos;

        try {
            if (esAdmin && modoAdmin) {

                partidos = partidoService.obtenerTodosLosPartidos();
                request.setAttribute("esAdmin", true);
            } else {

                partidos = partidoService.obtenerPartidosFuturos();
                request.setAttribute("esAdmin", false);
            }

            request.setAttribute("listaDePartidos", partidos);
            request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("hayError", true);
            request.setAttribute("mensajeError", "Error al cargar los partidos");
            request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        if (usuario == null || !usuario.puedeGestionarPartidos()) {
            response.sendRedirect(request.getContextPath() + "/Index?action=inicioSesion");
            return;
        }

        try {
            if (request.getParameter("resultadoPartido") != null) {

                int idPartido = Integer.parseInt(request.getParameter("idPartido"));
                String ganador = request.getParameter("ganador");
                partidoService.actualizarResultado(idPartido, ganador);

            } else if (request.getParameter("eliminar") != null) {

                int idPartido = Integer.parseInt(request.getParameter("eliminar"));
                partidoService.eliminarPartido(idPartido);

            } else {

                String local = request.getParameter("local");
                String visitante = request.getParameter("visitante");
                String fecha = request.getParameter("fecha");
                

                partidoService.crearPartido(local, visitante, fecha);
            }
        } catch (IllegalArgumentException e) {

            System.err.println("Error validación: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
    }
    
    @Override
    public void destroy() {
        if (partidoService != null) partidoService.close();
    }
}