package com.mycompany.apuestatodook;

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
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        String destino;
        
        Usuario usuario = (Usuario)request.getSession().getAttribute("userLogueado"); 
        
        int IDusuario = usuario.getIDusuario();

        double dineroDisponible = usuarioDAO.getDineroPorIdUsuario(IDusuario);
        
        request.setAttribute("dinero", dineroDisponible );
        
        destino = "WEB-INF/jsp/billetera.jsp";
        
        request.getRequestDispatcher(destino).forward(request, response);
    }
 
    
    
    @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    Usuario usuario = (Usuario) req.getSession().getAttribute("userLogueado");
    int IDusuario = usuario.getIDusuario();

    double dineroDisponible = usuarioDAO.getDineroPorIdUsuario(IDusuario);
    String operacion = req.getParameter("Modificar");
    String montoSTR = req.getParameter("monto");
    double monto = 0;
    if(montoSTR != ""){
        monto = Double.parseDouble(montoSTR);   
    } else {
            req.setAttribute("hayError", true);
            req.setAttribute("mensajeError", "Debe ingresar un monto por favor. ");
            req.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(req, resp);
    }

    if (operacion.equals("ingreso")) {
        dineroDisponible += monto;
    } else {
        if (dineroDisponible >= monto) {
            dineroDisponible -= monto;
        } else {
            req.setAttribute("dinero", dineroDisponible );
            req.setAttribute("hayError", true);
            req.setAttribute("mensajeError", "Saldo insuficiente para el retiro.");
            req.getRequestDispatcher("WEB-INF/jsp/billetera.jsp").forward(req, resp);
            return;
        }
    }

    usuario.setDinero(dineroDisponible);
    usuarioDAO.updateDinero(usuario);
    
    req.getSession().setAttribute("userLogueado", usuario);

    resp.sendRedirect(req.getContextPath() + "/Billetera");
}
    
} 

