<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
    
<div class="contenedor">
   <div class="card mb-3" style="max-width: 540px;">
        <div class="row g-0">
          <div class="col-md-4">
              <img src= "img/usuario-negro.jpg" style="width: 400px" class="img-fluid rounded-start" alt="...">
          </div>
            <div class="col-md-8">
            <div class="card-body">
              <h5 class="card-title">Perfil</h5>
              <p class="card-text">nombre: ${persona.nombre}</p>
              <p class="card-text">dni: ${persona.apellido}</p>
              <p class="card-text">dni: ${persona.dni}</p>
              <p class="card-text">edad: ${persona.edad}</p>
              <p class="card-text"><small class="text-body-secondary">Last updated 3 mins ago</small></p>
            </div>
          </div>
        </div>
      </div> 
    
</div>
        

        <c:import url="componentesHTML/footer.jsp" /> 
        
</html>