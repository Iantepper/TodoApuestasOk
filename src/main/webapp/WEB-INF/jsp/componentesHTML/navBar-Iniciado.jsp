<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-expand-lg navbar-custom">
  <div class="container-fluid">
    <a href="${pageContext.request.contextPath}" class="navbar-brand">
      <img src="${pageContext.request.contextPath}/img/logoAP.jpg" id="logo-nav" class="img-fluid navbar-logo">
      <img src="${pageContext.request.contextPath}/img/apuestatodoNav.png" id="apuestaTodo" class="img-fluid navbar-logo">
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
            <c:choose>
                <c:when test="${userLogueado == null}">
                    <li class="nav-item">
                        <a href="Index?action=inicioSesion" class="nav-link mi-link-barra">Iniciar Sesion</a>
                    </li>
                    <li class="nav-item">
                        <a href="Index?action=newUsuarios" class="nav-link mi-link-barra">Crear Usuario</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/Partidos" class="nav-link mi-link-barra">Partidos</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <c:if test="${userLogueado.puedeGestionarPartidos()}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/Partidos?admin=true" class="nav-link mi-link-barra">Admin Partidos</a>
                        </li>
                    </c:if>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/Partidos" class="nav-link mi-link-barra">Partidos</a>
                    </li>
                    <c:if test="${!userLogueado.puedeGestionarPartidos()}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/Billetera" class="nav-link mi-link-barra">Billetera</a>
                        </li>
                    </c:if>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/Resultados" class="nav-link mi-link-barra">Resultados</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/ApuestasMostrar" class="nav-link mi-link-barra">Apuestas</a>
                    </li>
                    <!-- OCULTAR PERFIL PARA ADMIN -->
                    <c:if test="${!userLogueado.puedeGestionarPartidos()}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/Perfil" class="nav-link mi-link-barra">Perfil</a>
                        </li>
                    </c:if>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/Salir" class="btn btn-danger btn-navbar">Logout</a>
                    </li>
                </c:otherwise>
            </c:choose> 
        </ul>
    </div>
  </div>
</nav>

<hr id="lineadebajo-navbar">