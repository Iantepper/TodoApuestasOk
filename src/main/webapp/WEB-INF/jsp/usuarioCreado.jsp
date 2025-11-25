<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="3;url=Index?action=inicioSesion">
        <title>ApuestaTodo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
        <link href="css/estilo.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>

<c:import url="componentesHTML/navBar-Iniciado.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card border-0 shadow-lg" style="background: linear-gradient(135deg, #FFD700, #FFA500);">
                <div class="card-body text-center text-dark p-5">
                    <div class="mb-4">
                        <h1 class="display-4 fw-bold">🎉 FELICITACIONES</h1>
                    </div>
                    <h2 class="mb-3">SU CUENTA HA SIDO CREADA</h2>
                    <h4 class="mb-4">Gracias por unirte a la comunidad de Apuesta Todo</h4>
                    <h4 class="mb-4">Se han acreditado 1000$ a su cuenta de regalo</h4>
                    <div class="alert alert-dark mb-4" role="alert">
                        <h5 class="mb-0">💰 Apostá con responsabilidad 💰</h5>
                    </div>
                    <div class="spinner-border text-dark mb-3" role="status">
                        <span class="visually-hidden">Cargando...</span>
                    </div>
                    <p class="text-muted">Serás redirigido automáticamente en 3 segundos...</p>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="componentesHTML/footer.jsp" /> 
    </body>
</html>
