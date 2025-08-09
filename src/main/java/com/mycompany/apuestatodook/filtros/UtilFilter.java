package com.mycompany.apuestatodook.filtros;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UtilFilter {

    public static void generarError(HttpServletRequest request, HttpServletResponse response, String mensajeError) throws ServletException, IOException {
        request.setAttribute("hayError", true);
        request.setAttribute("mensajeError", mensajeError);
        
        request.getRequestDispatcher("/Index?action=inicioSesion").forward(request, response);
    }
}
