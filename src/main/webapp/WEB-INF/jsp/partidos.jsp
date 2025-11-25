<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');
</style>

<c:choose>
    <c:when test="${esAdmin}">
        <!-- VISTA ADMIN -->
        <div class="container mt-4">
            <h2>Gestión de Partidos - Admin</h2>
            <a href="#" class="btn btn-success mb-3">Nuevo Partido</a>
            
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Local</th>
                            <th>Visitante</th>
                            <th>Fecha</th>
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
                                    <a href="#" class="btn btn-warning btn-sm">Editar</a>
                                    <a href="#" class="btn btn-danger btn-sm">Eliminar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <!-- VISTA USUARIO NORMAL -->
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