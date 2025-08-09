package com.mycompany.apuestatodook.filtros;

import com.mycompany.apuestatodook.filtros.UtilFilter;
import com.mycompany.apuestatodook.model.Usuario;
import com.mycompany.apuestatodook.model.UsuarioDAO;
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

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        if (session != null && session.getAttribute("userLogueado") != null) {
                Usuario userLogueado = (Usuario) session.getAttribute("userLogueado");

                Usuario usuarioEnBaseDeDatos = usuarioDAO.autenticar(userLogueado.getUsuario(), userLogueado.getContrasena());

                if (usuarioEnBaseDeDatos != null) {
                    chain.doFilter(httpRequest, httpResponse);
                } else {
                    session.invalidate();
                    UtilFilter.generarError(httpRequest, httpResponse, "No es ADMIN. Fuera de aquí");
                }
            } else {
                UtilFilter.generarError(httpRequest, httpResponse, "Debe iniciar sesión para entrar aquí");
            }
    }
}