<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:import url="WEB-INF/jsp/componentesHTML/inicioHTML.jsp" />
<c:import url="WEB-INF/jsp/componentesHTML/navBar-Iniciado.jsp" />
<c:import url="WEB-INF/jsp/componentesHTML/ul-BarraDeportes.jsp" />

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&family=Oswald:wght@600&display=swap');
</style>


<h2 id = "bets" >BETS DESTACADAS</h2>
        <div id="carouselExample" class="carousel slide">


      <div class="carousel-inner">
        <div class="carousel-item active">
            <a href="${pageContext.request.contextPath}/Partidos">
          <img src="img/Boca-Racing-ESPN.jpg" class="d-block " alt="...">
          </a>
        </div>
        <div class="carousel-item">
            <a href="${pageContext.request.contextPath}/Partidos">
          <img src="img/Barcelona-Madrid-Aguero.jpg" class="d-block " alt="...">
          </a>
        </div>
        <div class="carousel-item">
            <a href="${pageContext.request.contextPath}/Partidos">
          <img src="img/espn-Final.jpg" class="d-block " alt="...">
          </a>
        </div>
      </div>
      <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>

<div class="contenedor">
 
    <div class="card" style="width: 18rem;">
        <img src="img/tarjetas-credito.jpeg" class="card-img-top" alt="...">
  <div class="card-body">
    <h5 class="card-title">Depositos y extracciones</h5>
    <p class="card-text">Contamos con depositos y extracciones al instante y al banco/billetera virtual que quieras!</p>
    <a href="${pageContext.request.contextPath}/Partidos" class="btn btn-primary">Go</a>
  </div>
</div>
    
    <div class="card" style="width: 18rem; ">
        <img src="img/Barcelona-Madrid-Aguero.jpg" class="card-img-top" alt="...">
    <div class="card-body">
      <h5 class="card-title">X2</h5>
      <p class="card-text">Solo por hoy todas sus apuestas seran pagadas X2.</p>
      <a href="${pageContext.request.contextPath}/Partidos" class="btn btn-primary">Go</a>
    </div>
  </div>
    
    <div class="card" style="width: 18rem; ">
        <img src="img/espn-vs-boca-falta-de-respeto-vivo-png..png" class="card-img-top" alt="...">
    <div class="card-body">
      <h5 class="card-title">Apoya a Argentina</h5>
      <p class="card-text">Cuotas mejoradas en la final de la Libertadores Boca-Fluiminense</p>
      <a href="${pageContext.request.contextPath}/Partidos" class="btn btn-primary">Go</a>
    </div>
  </div>
    
</div>  






        
   <c:import url="WEB-INF/jsp/componentesHTML/footer.jsp" /> 
   
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js" integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+" crossorigin="anonymous"></script>


</html>