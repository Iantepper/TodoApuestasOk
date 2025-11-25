
package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SvBilletera", urlPatterns = "/Billetera")
public class BilleteraServlet extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UsuarioBase usuario = (UsuarioBase) request.getSession().getAttribute("userLogueado");
        
        // Redirigir si es admin
        if (usuario != null && usuario.puedeGestionarPartidos()) {
            response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
            return;
        }
        
        // Verificar que sea Usuario normal
        if (!(usuario instanceof Usuario)) {
            response.sendRedirect(request.getContextPath() + "/Partidos");
            return;
        }
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioNormal = (Usuario) usuario;
        int IDusuario = usuarioNormal.getId();

        double dineroDisponible = usuarioDAO.getDineroPorIdUsuario(IDusuario);
        
        // Actualizar el dinero en el objeto de sesión
        usuarioNormal.setDinero(dineroDisponible);
        request.getSession().setAttribute("userLogueado", usuarioNormal);
        
        request.setAttribute("dinero", dineroDisponible);
        request.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioBase usuario = (UsuarioBase) req.getSession().getAttribute("userLogueado");
        
        // Verificar que sea Usuario normal
        if (!(usuario instanceof Usuario)) {
            resp.sendRedirect(req.getContextPath() + "/Partidos");
            return;
        }
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioNormal = (Usuario) usuario;
        int IDusuario = usuarioNormal.getId();

        double dineroDisponible = usuarioDAO.getDineroPorIdUsuario(IDusuario);
        String operacion = req.getParameter("Modificar");
        String montoSTR = req.getParameter("monto");
        double monto = 0;
        
        if(montoSTR != null && !montoSTR.trim().isEmpty()){
            monto = Double.parseDouble(montoSTR);   
        } else {
            req.setAttribute("hayError", true);
            req.setAttribute("mensajeError", "Debe ingresar un monto por favor.");
            req.setAttribute("dinero", dineroDisponible);
            req.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(req, resp);
            return;
        }

        if (operacion.equals("ingreso")) {
            dineroDisponible += monto;
        } else {
            if (dineroDisponible >= monto) {
                dineroDisponible -= monto;
            } else {
                req.setAttribute("dinero", dineroDisponible);
                req.setAttribute("hayError", true);
                req.setAttribute("mensajeError", "Saldo insuficiente para el retiro.");
                req.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(req, resp);
                return;
            }
        }

        // Actualizar dinero en el objeto y en la BD
        usuarioNormal.setDinero(dineroDisponible);
        usuarioDAO.updateDinero(usuarioNormal);
        
        // Actualizar sesión
        req.getSession().setAttribute("userLogueado", usuarioNormal);

        resp.sendRedirect(req.getContextPath() + "/Billetera");
    }
} 