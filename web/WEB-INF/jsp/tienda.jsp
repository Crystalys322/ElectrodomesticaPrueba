<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tienda Electrodomestica</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" crossorigin="anonymous">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <span class="navbar-brand">Electrodomestica</span>
        <div class="collapse navbar-collapse show">
            <ul class="navbar-nav ms-auto">
                <c:if test="${not empty sessionScope.usuario}">
                    <li class="nav-item">
                        <span class="nav-link">Hola, ${sessionScope.usuario.nombreUsuario}</span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Salir</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>
<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h1 class="display-6">Bienvenido a la tienda</h1>
                    <p class="lead">Desde aquí podrás administrar tus compras y navegar por el catálogo de Electrodomestica.</p>
                    <hr>
                    <p class="text-muted">Utiliza el menú superior para salir de la sesión cuando lo desees.</p>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
