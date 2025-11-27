<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<style>
    @import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');
    
    #bets {
        text-align: center;
        font-family: 'Oswald', sans-serif;
        margin-top: 50px;
    }
    
    img {
        display: block;
        margin: 20px auto;
    }
</style>

<h1 id="bets">¡Nada a la vista Baby!</h1>
<img src="${pageContext.request.contextPath}/img/terminator.jpg" width="500px" alt="Error 404"/>

<c:import url="componentesHTML/footer.jsp" />