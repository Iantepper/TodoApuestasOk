<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="componentesHTML/inicioHTML.jsp" />
<c:import url="componentesHTML/navBar-Iniciado.jsp" />
<c:import url="componentesHTML/ul-BarraDeportes.jsp" />
<section class="h-100 ">
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col">
        <div class="card card-registration my-4">
          <div class="row g-0">
              <div id="fondoNewUsuario" class="col-xl-6 d-none d-xl-block">
            </div>
            <div class="col-xl-6">
              <div class="card-body p-md-5 text-black">
                  <div>
                      <h3 class="mb-5 text-uppercase">Crear Usuario</h3> 
                       <c:if test="${hayError}">
                                        <div class="container mt-3 p-3 bg-danger text-light">
                                            <h2>${mensajeError}</h2>
                                        </div>
                        </c:if>
                  </div>
                  <form action="NuevoUsuario" method="POST">
                             
                      <div class="form-outline mb-4">
                             <input type="text" id="form2Example11" class="form-control"
                             placeholder="User" name="usuario"/>
                             <label class="form-label" for="form2Example11">Usuario</label>
                             </div>
                      
                            <div class="form-outline mb-4">
                            <input type="text" name="nombre" id="form2Example22" class="form-control" placeholder="Nombre"/>
                            <label class="form-label" for="form2Example22">Nombre</label>
                            </div>
                            
                            <div class="form-outline mb-4">
                            <input type="text" name="apellido" id="form2Example22" class="form-control" placeholder="Apellido"/>
                            <label class="form-label" for="form2Example22">Apellido</label>
                            </div>
                      
                            <div class="form-outline mb-4">
                            <input type="text" name="dni" id="form2Example22" class="form-control" placeholder="DNI"/>
                            <label class="form-label" for="form2Example22">DNI</label>
                            </div>
                      
                            <div class="form-outline mb-4">
                            <input type="number" name="edad" id="form2Example22" class="form-control" placeholder="Edad"/>
                            <label class="form-label" for="form2Example22">Edad</label>
                            </div>

                            <div class="form-outline mb-4">
                            <input type="password" name="password" id="form2Example22" class="form-control" placeholder="Contraseña" />
                            <label class="form-label" for="form2Example22">Comtraseña</label>
                            </div>

                            <div class="form-outline mb-4">
                            <input type="password" name="cpassword" id="form2Example22" class="form-control" placeholder="Confirme Contraseña"/>
                            <label class="form-label" for="form2Example22">Confirme Contraseña</label>
                            </div>

                            <div class="d-flex justify-content-end pt-3">
                            <input class="btn btn-success btn-block fa-lg gradient-custom-2" type="submit" name="Enviar datos" value="Enviar datos">
                            </div>                 
                  </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
    <c:import url="componentesHTML/footer.jsp" /> 
</section>

