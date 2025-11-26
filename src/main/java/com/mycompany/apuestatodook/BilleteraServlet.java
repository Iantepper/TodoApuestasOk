package com.mycompany.apuestatodook;

import com.mycompany.apuestatodook.model.UsuarioBase;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioService;
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
        
        if (usuario != null && usuario.puedeGestionarPartidos()) {
            response.sendRedirect(request.getContextPath() + "/Partidos?admin=true");
            return;
        }
        
        if (!(usuario instanceof Usuario)) {
            response.sendRedirect(request.getContextPath() + "/Partidos");
            return;
        }
        
        UsuarioService usuarioService = null;
        try {
            usuarioService = new UsuarioService();
            Usuario usuarioNormal = (Usuario) usuario;
            int IDusuario = usuarioNormal.getId();

            double dineroDisponible = usuarioService.getDineroPorIdUsuario(IDusuario);
            
            usuarioNormal.setDinero(dineroDisponible);
            request.getSession().setAttribute("userLogueado", usuarioNormal);
            
            request.setAttribute("dinero", dineroDisponible);
            request.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(request, response);
        } finally {
            if (usuarioService != null) {
                usuarioService.close();
            }
        }
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioBase usuario = (UsuarioBase) req.getSession().getAttribute("userLogueado");
        
        if (!(usuario instanceof Usuario)) {
            resp.sendRedirect(req.getContextPath() + "/Partidos");
            return;
        }
        
        UsuarioService usuarioService = null;
        try {
            usuarioService = new UsuarioService();
            Usuario usuarioNormal = (Usuario) usuario;
            int IDusuario = usuarioNormal.getId();

            double dineroDisponible = usuarioService.getDineroPorIdUsuario(IDusuario);
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

            usuarioNormal.setDinero(dineroDisponible);
            usuarioService.updateDinero(usuarioNormal);
            
            req.getSession().setAttribute("userLogueado", usuarioNormal);
            resp.sendRedirect(req.getContextPath() + "/Billetera");
        } finally {
            if (usuarioService != null) {
                usuarioService.close();
            }
        }
    }
} 