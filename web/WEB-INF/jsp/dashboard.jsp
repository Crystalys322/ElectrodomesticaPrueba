<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Panel principal" />
<%@ include file="includes/header.jspf" %>
<div class="row g-4">
    <div class="col-md-3">
        <div class="card text-bg-primary h-100">
            <div class="card-body">
                <h5 class="card-title">Productos</h5>
                <p class="display-6">${totalProductos}</p>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card text-bg-success h-100">
            <div class="card-body">
                <h5 class="card-title">Clientes</h5>
                <p class="display-6">${totalClientes}</p>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card text-bg-warning h-100">
            <div class="card-body">
                <h5 class="card-title">Ventas</h5>
                <p class="display-6">${totalVentas}</p>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card text-bg-info h-100">
            <div class="card-body">
                <h5 class="card-title">Categorías</h5>
                <p class="display-6">${totalCategorias}</p>
            </div>
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
