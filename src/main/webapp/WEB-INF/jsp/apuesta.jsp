<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container mt-4">
    <div class="apuesta-container">
        <div class="row g-0">
            <!-- Equipo Local -->
            <div class="col-md-4 p-4">
                <div class="equipo-card local">
                    <div class="equipo-nombre text-success">
                        ${partido.local}
                    </div>
                    <form action="SvprocesarApuesta" method="POST" class="form-apuesta">
                        <div class="mb-3">
                            <input type="number" class="form-control input-monto" 
                                   placeholder="$$$$" name="monto" min="1" required
                                   oninput="validarMonto(this)"
                                   style="color: #000000 !important; background-color: #ffffff !important; border: 2px solid #28a745 !important;">
>
                            <input type="hidden" name="por" value="local">
                            <input type="hidden" name="idPartido" value="${partido.idPartido}">
                        </div>
                        <button type="submit" class="btn-apostar">
                            <i class="fas fa-coins me-2"></i>Apostar por Local
                        </button>
                    </form>
                </div>
            </div>
            
            <!-- Información Central -->
            <div class="col-md-4 p-4">
                <div class="info-central">
                    <c:if test="${hayError}">
                        <div class="alert alert-error mb-4">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            ${mensajeError}
                        </div>
                    </c:if>
                    
                    <div class="partido-fecha">
                        <i class="fas fa-calendar-alt me-2"></i>
                        ${partido.fecha}
                    </div>
                    
                    <div class="vs-text">VS</div>
                    
                    <div class="saldo-info">
                        <i class="fas fa-wallet me-2"></i>
                        <div class="info-text">Tu Saldo</div>
                        <div class="saldo-amount">
    $ <fmt:formatNumber value="${dineroUsuario}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
</div>
                    </div>
                    
                    <div class="mt-3 info-text">
                        <small>
                            <i class="fas fa-trophy me-1"></i>
                            Gana el doble si aciertas
                        </small>
                    </div>
                </div>
            </div>
            
            <!-- Equipo Visitante -->
            <div class="col-md-4 p-4">
                <div class="equipo-card visitante">
                    <div class="equipo-nombre text-danger">
                        ${partido.visitante}
                    </div>
                    <form action="SvprocesarApuesta" method="POST" class="form-apuesta">
                        <div class="mb-3">
                            <input type="number" class="form-control input-monto" 
                                   placeholder="$$$$" name="monto" min="1" required
                                   oninput="validarMonto(this)"
                                   style="color: #000000 !important; background-color: #ffffff !important; border: 2px solid #28a745 !important;">
>
                            <input type="hidden" name="por" value="visitante">
                            <input type="hidden" name="idPartido" value="${partido.idPartido}">
                        </div>
                        <button type="submit" class="btn-apostar">
                            <i class="fas fa-coins me-2"></i>Apostar por Visitante
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function validarMonto(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
    
    const saldoDisponible = ${dineroUsuario};
    const montoIngresado = parseInt(input.value) || 0;
    
    if (montoIngresado > saldoDisponible) {
        input.style.borderColor = 'var(--color-accent)';
        input.style.backgroundColor = '#fff5f5';
    } else if (montoIngresado > 0) {
        input.style.borderColor = 'var(--color-success)';
        input.style.backgroundColor = '#f0fff4';
    } else {
        input.style.borderColor = '#e9ecef';
        input.style.backgroundColor = 'var(--color-light)';
    }
}
</script>

<c:import url="componentesHTML/footer.jsp" /> 
</body>
</html>