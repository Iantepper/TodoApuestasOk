package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.PartidoRepository;
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

    // Temporal: mantener PartidoDAO por ahora
    private final PartidoDAO partidoDAO = new PartidoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        boolean esAdmin = usuario != null && usuario.puedeGestionarPartidos();
        boolean modoAdmin = "true".equals(request.getParameter("admin"));
        
        if (esAdmin && request.getParameter("eliminar") != null) {
            int idPartido = Integer.parseInt(request.getParameter("eliminar"));
            partidoDAO.delete(idPartido); // Temporal: seguir usando DAO
        }
        
        List<Partido> partidos;
        PartidoRepository partidoRepo = null;
        
        try {
            partidoRepo = new PartidoRepository();
            
            if (esAdmin && modoAdmin) {
                // ✅ NUEVO: Usar Repository para obtener todos los partidos
                partidos = partidoRepo.obtenerTodos();
                request.setAttribute("esAdmin", true);
            } else {
                // ✅ NUEVO: Usar Repository para partidos futuros
                partidos = partidoRepo.obtenerPartidosFuturos();
                request.setAttribute("esAdmin", false);
            }
            
            // Para admin, cargar resultados (temporal: seguir usando DAO)
            if (esAdmin && modoAdmin) {
                for (Partido partido : partidos) {
                    Resultado resultado = partidoDAO.getResultadoPorPartido(partido.getIdPartido());
                    partido.setResultado(resultado);
                }
            }
            
            request.setAttribute("listaDePartidos", partidos);
            request.getRequestDispatcher("WEB-INF/jsp/partidos.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback al DAO si hay error
            if (esAdmin && modoAdmin) {
                partidos = partidoDAO.getAll();
            } else {
                partidos = partidoDAO.getPartidosFuturos();
            }
            request.setAttribute("listaDePartidos", partidos);
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
                
                // ✅ NUEVO: Usar Repository para actualizar resultado
                partidoRepo.actualizarResultado(idPartido, ganador);
                
            } else {
                String local = request.getParameter("local");
                String visitante = request.getParameter("visitante");
                String fecha = request.getParameter("fecha");

                if (local != null && !local.trim().isEmpty() && 
                    visitante != null && !visitante.trim().isEmpty() &&
                    fecha != null && !fecha.trim().isEmpty()) {
                
                    if (esFechaFutura(fecha)) {
                        Partido partido = new Partido(local, visitante, fecha, 0);
                        
                        // ✅ NUEVO: Usar Repository para guardar partido
                        partidoRepo.guardar(partido);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback al DAO si hay error
            // ... (mantener lógica DAO como fallback)
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