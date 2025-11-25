<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />


<div class="container mt-4">
    <div class="row">
        <div class="col-lg-6">
            <div class="card card-billetera">  <!-- AGREGAR card-billetera -->
                <div class="card-header text-center">
                    <h3 class="mb-0">Tu Billetera</h3>
                </div>
                <div class="card-body text-center">
                    <c:if test="${hayError}">
                        <div class="alert alert-danger">
                            <h4>${mensajeError}</h4>
                        </div>
                    </c:if>
                    <h4 class="card-title">Saldo Disponible</h4>
                    <p class="display-6 fw-bold">$${dinero}</p>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card card-billetera">  <!-- AGREGAR card-billetera -->
                <div class="card-body">
                    <form action="Billetera" method="POST">
                        <h4 class="card-title">Agregar Dinero</h4>
                        <div class="form-group">
                            <input type="number" class="form-control" name="monto" placeholder="Monto a agregar" id="montoAgregar">
                            <input type="hidden" class="form-control" name="Modificar" value="ingreso" id="montoAgregar">
                        </div>
                        <button class="btn btn-success mt-2" type="submit">Enviar</button>
                    </form>

                    <form action="Billetera" method="POST" class="mt-4">
                        <h4 class="card-title">Retirar Dinero</h4>
                        <div class="form-group">
                            <input type="number" class="form-control" name="monto" placeholder="Monto a retirar" id="montoRetirar"> 
                            <input type="hidden" class="form-control" name="Modificar" value="retiro" id="montoAgregar">
                        </div>
                        <button class="btn btn-success mt-2" type="submit">Enviar</button> 
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

        <c:import url="componentesHTML/footer.jsp" /> 
    </body>
</html>

