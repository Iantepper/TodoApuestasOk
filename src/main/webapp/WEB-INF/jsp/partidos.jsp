<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');
</style>

<c:choose>
    <c:when test="${esAdmin}">

        <div class="container mt-4">
            <h2>Gestión de Partidos - Admin</h2>
            <c:if test="${hayError}">
        <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i> ${mensajeError}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty mensajeExito}">
        <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
            <i class="fas fa-check-circle me-2"></i> ${mensajeExito}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
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
        <%
        
            com.mycompany.apuestatodook.model.Partido p = (com.mycompany.apuestatodook.model.Partido) pageContext.getAttribute("partido");
            boolean yaSeJugo = false;
            
         
            String[] formatosPosibles = {
                "yyyy-MM-dd HH:mm:ss", 
                "yyyy-MM-dd HH:mm",   
                "yyyy-MM-dd'T'HH:mm",  
                "dd/MM/yyyy HH:mm"     
            };

            String fechaStr = p.getFecha();
            Date fechaPartido = null;

           
            for (String formato : formatosPosibles) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(formato);
                    sdf.setLenient(false); // Ser estricto con el formato
                    fechaPartido = sdf.parse(fechaStr);
                    break; // Si funcionó, salimos del loop
                } catch (Exception e) {
                    
                }
            }


            if (fechaPartido != null) {
                Date fechaActual = new Date();
                if (fechaActual.after(fechaPartido)) {
                    yaSeJugo = true;
                }
            } else {

                yaSeJugo = true;
            }
            
            pageContext.setAttribute("yaSeJugo", yaSeJugo);
        %>

        <tr>
            <td>${partido.local}</td>
            <td>${partido.visitante}</td>
            <td>${partido.fecha}</td> <td>
                <form action="${pageContext.request.contextPath}/Partidos" method="POST" class="form-inline"
                      onsubmit="return confirm('¿Confirmar el resultado del partido? Esta acción calculará los ganadores.');">
                    
                    <input type="hidden" name="resultadoPartido" value="true">
                    <input type="hidden" name="idPartido" value="${partido.idPartido}">
                    
                    <select name="ganador" class="form-control form-control-sm" ${!yaSeJugo ? 'disabled' : ''} 
                            style="${!yaSeJugo ? 'background-color: #e9ecef; cursor: not-allowed;' : ''}">
                        
                        <option value="" ${partido.resultado == null ? 'selected' : ''}>
                            ${!yaSeJugo ? 'Esperando fecha...' : 'Sin resultado'}
                        </option>
                        <option value="local" ${partido.resultado != null && partido.resultado.ganador == 'local' ? 'selected' : ''}>
                            Local gana
                        </option>
                        <option value="visitante" ${partido.resultado != null && partido.resultado.ganador == 'visitante' ? 'selected' : ''}>
                            Visitante gana
                        </option>
                        <option value="empate" ${partido.resultado != null && partido.resultado.ganador == 'empate' ? 'selected' : ''}>
                            Empate
                        </option>
                    </select>

                    <c:if test="${yaSeJugo}">
                        <button type="submit" class="btn btn-info btn-sm ml-2">Guardar</button>
                    </c:if>
                </form>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/Partidos?admin=true&eliminar=${partido.idPartido}" 
                   class="btn btn-danger btn-sm"
                   onclick="return confirm('¿Estás seguro de eliminar este partido? ATENCIÓN: Si tiene apuestas asociadas no se podrá borrar.')">Eliminar</a>
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