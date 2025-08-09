<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />        
        <div id="nav-bar">
            <h1>AP</h1>  
        </div>
        
        <h1>Usuario o Contraseña Erronea</h1>
        <h3>Ingrese Nuevamnete</h3>
        
        <a id="botonRegreso" href="Index?action=inicioSesion">Volver a Ingresar</a>   
        <a id="botonRegreso" href="Index?action=newUsuarios">Crear Cuenta</a> 
        
        
        <c:import url="componentesHTML/footer.jsp" /> 
    </body>
</html>