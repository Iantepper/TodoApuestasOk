<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');
</style>

<c:choose>
    <c:when test="${esAdmin}">

        <div class="container mt-4">
            <h2>Gestión de Partidos - Admin</h2>
            
            <div class="card mb-4">
                <div class="card-header">
                    <h5>Agregar Nuevo Partido</h5>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/Partidos" method="POST">
                        <div class="row">
                            <div class="col-md-4">
                                <input type="text" name="local" class="form-control" placeholder="Equipo local" required>
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="visitante" class="form-control" placeholder="Equipo visitante" required>
                            </div>
                            <div class="col-md-3">
                                <input type="datetime-local" name="fecha" class="form-control" required>
                            </div>
                            <div class="col-md-1">
                                <button type="submit" class="btn btn-success">Agregar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Local</th>
                            <th>Visitante</th>
                            <th>Fecha</th>
                            <th>Resultado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listaDePartidos}" var="partido">
                            <tr>
                                <td>${partido.local}</td>
                                <td>${partido.visitante}</td>
                                <td>${partido.fecha}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/Partidos" method="POST" class="form-inline">
                                        <input type="hidden" name="resultadoPartido" value="true">
                                        <input type="hidden" name="idPartido" value="${partido.idPartido}">
                                        <select name="ganador" class="form-control form-control-sm">
                                            <option value="">Sin resultado</option>
                                            <option value="local" ${partido.resultado != null && partido.resultado.ganador == 'local' ? 'selected' : ''}>Local gana</option>
                                            <option value="visitante" ${partido.resultado != null && partido.resultado.ganador == 'visitante' ? 'selected' : ''}>Visitante gana</option>
                                            <option value="empate" ${partido.resultado != null && partido.resultado.ganador == 'empate' ? 'selected' : ''}>Empate</option>
                                        </select>
                                        <button type="submit" class="btn btn-info btn-sm ml-2">Guardar</button>
                                    </form>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/Partidos?admin=true&eliminar=${partido.idPartido}" 
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('¿Estás seguro de eliminar este partido?')">Eliminar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:when>
    <c:otherwise>

        <h1 id="bets">PARTIDOS DISPONIBLES</h1>
        <div class="row">
            <div class="col-md-12">
                <div class="partidos-container">
                    <c:forEach items="${listaDePartidos}" var="partido" varStatus="loop">
                        <div class="partido-burbuja">
                            <div class="equipo-local">${partido.local}</div>
                            <div class="equipo-visitante">${partido.visitante}</div>
                            <div class="fecha">${partido.fecha}</div>
                            <a class="btn btn-success" href="/ApuestaTodook/Apuesta?id=${partido.idPartido}">Apostar</a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<c:import url="componentesHTML/footer.jsp" />
</body>
</html>