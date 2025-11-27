<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');

.estado-apuesta {
    padding: 5px 10px;
    border-radius: 20px;
    font-weight: bold;
    font-size: 0.9em;
    text-transform: uppercase;
    display: inline-block;
    margin-bottom: 10px;
}

.estado-ganada {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.estado-perdida {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.estado-activa {
    background-color: #fff3cd;
    color: #856404;
    border: 1px solid #ffeaa7;
}

.estado-desconocido {
    background-color: #e2e3e5;
    color: #383d41;
    border: 1px solid #d6d8db;
}
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
            <c:if test="${esAdmin}">
                <div class="usuario-apuesta" style="background: #f8f9fa; padding: 5px; border-radius: 5px; margin-bottom: 10px;">
                    <strong>Usuario:</strong> ${apuesta.nombreUsuario}
                </div>
            </c:if>
            
            <!-- Estado de la apuesta - USANDO SOLO getEstadoLegible() -->
            <div class="text-center">
                <c:set var="estadoTexto" value="${apuesta.estadoLegible}" />
                <c:choose>
                    <c:when test="${estadoTexto == 'Ganada'}">
                        <span class="estado-apuesta estado-ganada">GANADA</span>
                        <div class="mt-2 p-2 text-center" style="background: #d4edda; border-radius: 5px;">
                            <small>¡Apuesta Ganada! - Resultado confirmado</small>
                        </div>
                    </c:when>
                    <c:when test="${estadoTexto == 'Perdida'}">
                        <span class="estado-apuesta estado-perdida">PERDIDA</span>
                        <div class="mt-2 p-2 text-center" style="background: #f8d7da; border-radius: 5px;">
                            <small>Apuesta Perdida - Resultado confirmado</small>
                        </div>
                    </c:when>
                    <c:when test="${estadoTexto == 'Activa'}">
                        <span class="estado-apuesta estado-activa">ACTIVA</span>
                        <div class="mt-2 p-2 text-center" style="background: #fff3cd; border-radius: 5px;">
                            <small>Esperando resultado del partido</small>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <span class="estado-apuesta estado-desconocido">DESCONOCIDO</span>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="equipo-local">${apuesta.local}</div>
            <div class="equipo-visitante">${apuesta.visitante}</div>
            <div class="fecha">${apuesta.fecha}</div>
            <div class="por-quien">Por: ${apuesta.por_quien}</div>
            <div class="monto">Monto: $${apuesta.monto}</div>
        </div>
    </c:forEach>
</div>
    </div>
</div>

<c:import url="componentesHTML/footer.jsp" /> 
</body>
</html>