package com.mycompany.apuestatodook.filtros;

import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        Usuario usuario = (Usuario) session.getAttribute("userLogueado");
        
        if (usuario != null && usuario.esAdmin()) {
            chain.doFilter(request, response);
        } else {
            UtilFilter.generarError(httpRequest, (HttpServletResponse) response, 
                "Acceso denegado. Se requieren privilegios de administrador.");
        }
    }
}