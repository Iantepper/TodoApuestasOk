<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');
</style>
        
<h1 id="bets">Apuestas</h1>
<c:if test="${esAdmin}">
    <h1 id="bets">${mensajeAdmin}</h1>
</c:if>

<div class="row">
    <div class="col-md-12">
        <div class="partidos-container">
            <c:forEach items="${apuestas}" var="apuesta" varStatus="loop">
                <div class="partido-burbuja">
                    <div class="equipo-local">${apuesta.local}</div>
                    <div class="equipo-visitante">${apuesta.visitante}</div>
                    <div class="fecha">${apuesta.fecha}</div>
                    <div class="por-quien">Por Quién: ${apuesta.por_quien}</div>
                    <div class="monto">Monto: ${apuesta.monto}</div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
        <c:import url="componentesHTML/footer.jsp" /> 
    </body>
</html>

