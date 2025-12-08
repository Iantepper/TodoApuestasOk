package com.mycompany.apuestatodook.servlets;

import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SvBilletera", urlPatterns = "/Billetera")
public class BilleteraServlet extends HttpServlet {

    private UsuarioService usuarioService;

    @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        

        if (!(usuario instanceof Usuario)) {
            response.sendRedirect(request.getContextPath() + "/Partidos");
            return;
        }

        try {
 
            double dineroActual = usuarioService.getDineroPorIdUsuario(usuario.getId());
            
 
            ((Usuario) usuario).setDinero(dineroActual);
            
            request.setAttribute("dinero", dineroActual);
            request.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(request, response);
            
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/Partidos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioBase usuario = (UsuarioBase) req.getSession().getAttribute("userLogueado");

        if (!(usuario instanceof Usuario)) {
            resp.sendRedirect(req.getContextPath() + "/Partidos");
            return;
        }

        try {
            String operacion = req.getParameter("Modificar");
            String montoSTR = req.getParameter("monto");
            double monto = (montoSTR != null && !montoSTR.isEmpty()) ? Double.parseDouble(montoSTR) : 0;


            double nuevoSaldo = usuarioService.operarBilletera(usuario.getId(), monto, operacion);


            ((Usuario) usuario).setDinero(nuevoSaldo);
            req.getSession().setAttribute("userLogueado", usuario);
            resp.sendRedirect(req.getContextPath() + "/Billetera");

        } catch (IllegalArgumentException | IllegalStateException e) {

            req.setAttribute("hayError", true);
            req.setAttribute("mensajeError", e.getMessage());
            doGet(req, resp); 
        } catch (Exception e) {
            req.setAttribute("hayError", true);
            req.setAttribute("mensajeError", "Error inesperado.");
            doGet(req, resp);
        }
    }

    @Override
    public void destroy() {
        if (usuarioService != null) usuarioService.close();
    }
}