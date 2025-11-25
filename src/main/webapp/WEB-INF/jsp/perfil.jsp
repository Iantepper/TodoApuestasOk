<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
    
<div class="contenedor">
   <div class="card mb-3" style="max-width: 540px;">
        <div class="row g-0">
          <div class="col-md-4">
              <img src="img/usuario-negro.jpg" style="width: 400px" class="img-fluid rounded-start" alt="...">
          </div>
            <div class="col-md-8">
            <div class="card-body">
              <h5 class="card-title">Perfil</h5>
              <p class="card-text">Usuario: ${usuario.usuario}</p>
              <p class="card-text">Nombre: ${usuario.nombre}</p>
              <p class="card-text">Apellido: ${usuario.apellido}</p>
              <p class="card-text">DNI: ${usuario.dni}</p>
              <p class="card-text">Edad: ${usuario.edad}</p>
              <p class="card-text">Saldo: $${usuario.dinero}</p>
              <p class="card-text"><small class="text-body-secondary">Last updated 3 mins ago</small></p>
            </div>
          </div>
        </div>
      </div> 
</div>

<c:import url="componentesHTML/footer.jsp" />
</body>
</html>