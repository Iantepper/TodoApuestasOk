package com.mycompany.apuestatodook;


import com.mycompany.apuestatodook.model.Partido;
import com.mycompany.apuestatodook.model.PartidoDAO;
import com.mycompany.apuestatodook.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvResultados", urlPatterns = {"/Resultados"})
public class ResultadosServlet extends HttpServlet {
    

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PartidoDAO partidoDAO = new PartidoDAO();
    String destino;
     
    List<Partido> partidosConResultado = partidoDAO.getAllPartidosConResultado();
 
    request.setAttribute("partidosConResultado", partidosConResultado);

    destino = "WEB-INF/jsp/resultados.jsp";
    Usuario usuario = (Usuario) request.getSession().getAttribute("userLogueado");
    

    request.getRequestDispatcher(destino).forward(request, response);
}
}