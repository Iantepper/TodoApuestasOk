<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
                <div class="card">               
                <div class="card-body">
                  <h3 class="card-title">Felicitaciones su apuesta ha sido creada</h3>
                  <h3 class="card-title">Datos del partido</h3>
                  <h5 class="card-text">${partido.local} vs ${partido.visitante}</h5>
                  <h5 class="card-text">${partido.fecha}</h5>
                  <h5 class="card-text">Su apuesta ha sido de $ ${apuesta.monto} por el equipo ${apuesta.por_quien}</h3>
                  <h5 class="card-text">En caso de ganar su premio sera de $ ${premio}</h5>
                 
                  <a style="padding: 10px" href="${pageContext.request.contextPath}/Partidos" class="btn btn-success">Seguir apostando</a>
                   <a style="padding: 10px" href="${pageContext.request.contextPath}" class="btn btn-success">Inicio</a>
                </div>
                  
                  <div class="card-header">
                  Gracias por confiar en nosotros
                  <c:choose>
                    <c:when test="${hayGanador}">
                        <h1>¡Felicidades, has ganado la apuesta!</h1>
                    </c:when>
                    <c:when test="${hayPerdedor}">
                        <h1>Lo siento, has perdido la apuesta.</h1>
                    </c:when>
                </c:choose>
                </div>
                  
              </div>           
            
        
            
        <c:import url="componentesHTML/footer.jsp" /> 
</html>
