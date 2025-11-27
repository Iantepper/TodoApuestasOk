<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
    
<div class="contenedor">
   <div class="card card-perfil mb-3" style="max-width: 540px;">  <!-- AGREGAR card-perfil -->
        <div class="row g-0">
          <div class="col-md-4">
              <img src="img/usuario-negro.jpg" style="width: 400px" class="img-fluid rounded-start" alt="...">
          </div>
            <div class="col-md-8">
            <div class="card-body">
              <h5 class="card-title fw-bold">Tu Perfil</h5>
              <p class="card-text"><strong>Usuario:</strong> ${usuario.usuario}</p>
              <p class="card-text"><strong>Nombre:</strong> ${usuario.nombre}</p>
              <p class="card-text"><strong>Apellido:</strong> ${usuario.apellido}</p>
              <p class="card-text"><strong>DNI:</strong> ${usuario.dni}</p>
              <p class="card-text"><strong>Edad:</strong> ${usuario.edad}</p>
              <p class="card-text"><strong>Saldo:</strong> $${usuario.dinero}</p>
            </div>
          </div>
        </div>
      </div>
</div>

    <c:import url="componentesHTML/footer.jsp" />
</body>
</html>