<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-expand-lg">
  <div class="container-fluid">
    <a href="${pageContext.request.contextPath}">
      <img src="img/logoAP.jpg" id="logo-nav" class="img-fluid navbar-logo">
      <img src="img/apuestatodoNav.png" id = "apuestaTodo" class="img-fluid navbar-logo">
    </a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    
    <form class="d-flex">
                <c:choose>
                    <c:when test="${userLogueado == null}">
                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav ms-auto">
                        <li class="nav-item" >
                          <a href="Index?action=inicioSesion" class="nav-link">Iniciar Sesion</a>
                        </li>
                        <li class="nav-item" >
                          <a href="Index?action=newUsuarios" class="nav-link">Crear Usuario</a>
                        </li>
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/Partidos" class="nav-link">Partidos</a>
                        </li>
                            </ul>
                          </div>
                    </c:when>
                    <c:otherwise>
                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav ms-auto">
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/Partidos" class="nav-link">Partidos</a>
                        </li>
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/Billetera" class="nav-link">billetera</a>
                        </li>
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/Resultados" class="nav-link">Resultados</a>
                        </li>
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/ApuestasMostrar" class="nav-link">Apuestas</a>
                        </li>
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/Perfil" class="nav-link">perfil</a>
                        </li>
                        <li class="nav-item" >
                          <a href="${pageContext.request.contextPath}/Salir" class="btn btn-danger">Logout</a>
                        </li>
                      </ul>
                          </div>
                    </c:otherwise>
                </c:choose> 
            </form>

    
  </div>
</nav

<hr id="lineadebajo-navbar">