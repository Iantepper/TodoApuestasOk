<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<div class="container mt-4">
    <div class="card success-card shadow-lg">               
        <div class="card-body text-center py-5">
            <div class="mb-4">
                <i class="fas fa-check-circle success-icon"></i>
                <h2 class="card-title mt-3">¡Apuesta Creada Exitosamente!</h2>
                <p class="text-muted">Tu apuesta ha sido registrada en el sistema</p>
            </div>
            
            <div class="info-section">
                <div class="row text-center">
                    <div class="col-md-6 mb-3">
                        <div class="info-item">
                            <i class="fas fa-trophy me-2"></i>
                            <strong>Partido:</strong><br>
                            ${partido.local} vs ${partido.visitante}
                        </div>
                        <div class="info-item">
                            <i class="fas fa-calendar me-2"></i>
                            <strong>Fecha:</strong><br>
                            ${partido.fecha}
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <div class="info-item">
                            <i class="fas fa-coins me-2"></i>
                            <strong>Apuesta:</strong><br>
                            $${apuesta.monto} por ${apuesta.por_quien}
                        </div>
                        <div class="info-item">
                            <i class="fas fa-gem me-2"></i>
                            <strong>Premio potencial:</strong><br>
                            <span class="premio-text">$${premio}</span>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="alert alert-info-custom mt-4">
                <h5><i class="fas fa-info-circle me-2"></i>Estado: <strong>APUESTA ACTIVA</strong></h5>
                <p class="mb-0">El resultado se procesará automáticamente cuando el partido finalice y el administrador cargue el resultado.</p>
            </div>
            
            <div class="mt-5">
                <a href="${pageContext.request.contextPath}/Partidos" class="btn btn-success-custom me-3">
                    <i class="fas fa-play-circle me-2"></i>Seguir Apostando
                </a>
                <a href="${pageContext.request.contextPath}/ApuestasMostrar" class="btn btn-outline-custom me-3">
                    <i class="fas fa-list me-2"></i>Ver Mis Apuestas
                </a>
                <a href="${pageContext.request.contextPath}" class="btn btn-secondary">
                    <i class="fas fa-home me-2"></i>Inicio
                </a>
            </div>
        </div>
    </div>
</div>

<c:import url="componentesHTML/footer.jsp" /> 
</body>
</html>