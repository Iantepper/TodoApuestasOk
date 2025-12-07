package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.repositories.PartidoRepository;
import com.mycompany.apuestatodook.model.Resultado;
import com.mycompany.apuestatodook.model.UsuarioBase;
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

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
    boolean esAdmin = usuario != null && usuario.puedeGestionarPartidos();
    boolean modoAdmin = "true".equals(request.getParameter("admin"));
    
    List<Partido> partidos;
    PartidoRepository partidoRepo = null;
    
    try {
        partidoRepo = new PartidoRepository();
        
        if (esAdmin && modoAdmin) {
            // odos los partidos
            System.out.println("👑 Admin en MODO ADMIN - viendo TODOS los partidos");
            partidos = partidoRepo.obtenerTodos();
            request.setAttribute("esAdmin", true);
            
            
            for (Partido partido : partidos) {
                Resultado resultado = partidoRepo.obtenerResultadoPorPartido(partido.getIdPartido());
                partido.setResultado(resultado);
            }
            
        } else {
            //normal futuros
            System.out.println((esAdmin ? "👑 Admin" : "👤 User") + " en MODO NORMAL - viendo PARTIDOS FUTUROS");
            partidos = partidoRepo.obtenerPartidosFuturos();
            request.setAttribute("esAdmin", false);
        }
        
        System.out.println("🎲 Partidos a mostrar: " + partidos.size());
        for (Partido p : partidos) {
            System.out.println("   - " + p.getLocal() + " vs " + p.getVisitante() + " (" + p.getFecha() + ")");
        }
        
        request.setAttribute("listaDePartidos", partidos);
        request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
        
    } catch (Exception e) {
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", "Error al cargar los partidos");
        request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
    } finally {
        if (partidoRepo != null) {
            partidoRepo.close();
        }
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

        PartidoRepository partidoRepo = null;
        try {
            partidoRepo = new PartidoRepository();
            
            if (request.getParameter("resultadoPartido") != null) {
                int idPartido = Integer.parseInt(request.getParameter("idPartido"));
                String ganador = request.getParameter("ganador");
                
                //actualizar resultado
                partidoRepo.actualizarResultadoYProcesarApuestas(idPartido, ganador);
                
            } else if (request.getParameter("eliminar") != null) {
                // liminar partido
                int idPartido = Integer.parseInt(request.getParameter("eliminar"));
                partidoRepo.eliminar(idPartido);
                
            } else {
                String local = request.getParameter("local");
                String visitante = request.getParameter("visitante");
                String fecha = request.getParameter("fecha");

                if (local != null && !local.trim().isEmpty() && 
                    visitante != null && !visitante.trim().isEmpty() &&
                    fecha != null && !fecha.trim().isEmpty()) {
                
                    if (esFechaFutura(fecha)) {
                        Partido partido = new Partido(local, visitante, fecha, 0);
                        

                        partidoRepo.guardar(partido);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (partidoRepo != null) {
                partidoRepo.close();
            }
        }
    
        response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
    }

    private boolean esFechaFutura(String fechaStr) {
        try {
            LocalDateTime fechaPartido = LocalDateTime.parse(fechaStr.replace(" ", "T"));
            return fechaPartido.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }
}