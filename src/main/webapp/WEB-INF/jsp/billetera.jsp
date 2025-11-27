<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />

<div class="container mt-4">
    <div class="row">
        <!-- Tarjeta de Saldo -->
        <div class="col-lg-6 mb-4">
            <div class="card card-billetera shadow">  
                <div class="card-header text-center bg-success text-white">
                    <h3 class="mb-0"><i class="fas fa-wallet me-2"></i>Tu Billetera</h3>
                </div>
                <div class="card-body text-center">
                    <c:if test="${hayError}">
                        <div class="alert alert-danger">
                            <h4>${mensajeError}</h4>
                        </div>
                    </c:if>
                    
                    <div class="saldo-container">
                        <h4 class="card-title text-muted">Saldo Disponible</h4>
                        <p class="display-4 fw-bold text-success">$${dinero}</p>
                        <small class="text-muted">Saldo actualizado en tiempo real</small>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Tarjeta de Operaciones -->
        <div class="col-lg-6">
            <div class="card card-billetera shadow">  
                <div class="card-body">
                    <h5 class="card-title text-center mb-4"><i class="fas fa-exchange-alt me-2"></i>Operaciones</h5>
                    
                    <!-- Formulario Agregar Dinero -->
                    <div class="mb-4 p-3 border rounded">
                        <h6 class="text-success"><i class="fas fa-plus-circle me-2"></i>Agregar Dinero</h6>
                        <form action="Billetera" method="POST" class="mt-3">
                            <input type="hidden" name="Modificar" value="ingreso">
                            <div class="mb-3">
                                <label class="form-label">Monto a ingresar:</label>
                                <input type="number" class="form-control" name="monto" 
                                       placeholder="Ej: 1000" min="100" step="100" required>
                                <div class="form-text">Monto mínimo: $100</div>
                            </div>
                            <button type="submit" class="btn btn-success w-100">
                                <i class="fas fa-check me-1"></i>Confirmar Ingreso
                            </button>
                        </form>
                    </div>
                    
                    <!-- Formulario Retirar Dinero -->
                    <div class="p-3 border rounded">
                        <h6 class="text-succes"><i class="fas fa-minus-circle me-2"></i>Retirar Dinero</h6>
                        <form action="Billetera" method="POST" class="mt-3">
                            <input type="hidden" name="Modificar" value="retiro">
                            <div class="mb-3">
                                <label class="form-label">Monto a retirar:</label>
                                <input type="number" class="form-control" name="monto" 
                                       placeholder="Ej: 500" min="100" step="100" required>
                                <div class="form-text">Monto mínimo: $100 | Saldo disponible: $${dinero}</div>
                            </div>
                            <button type="submit" class="btn btn-success w-100">
                                <i class="fas fa-check me-1"></i>Confirmar Retiro
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<c:import url="componentesHTML/footer.jsp" /> 
</body>
</html>