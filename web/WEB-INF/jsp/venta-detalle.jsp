<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Detalle de venta" />
<%@ include file="includes/header.jspf" %>
<h2>Detalle de venta #${venta.idVenta}</h2>
<div class="row mb-3">
    <div class="col-md-4">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Información</h5>
                <p class="card-text mb-1"><strong>Fecha:</strong> ${venta.fecha}</p>
                <p class="card-text mb-1"><strong>Cliente:</strong> ${venta.idCliente}</p>
                <p class="card-text mb-1"><strong>Usuario:</strong> ${venta.idUsuario}</p>
                <p class="card-text mb-0"><strong>Estado:</strong> ${venta.estado}</p>
            </div>
        </div>
    </div>
    <div class="col-md-8">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Detalle</h5>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Precio</th>
                            <th>Subtotal</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="detalle" items="${detalles}">
                            <tr>
                                <td>${detalle.idProducto}</td>
                                <td>${detalle.cantidad}</td>
                                <td>${detalle.precioUnitario}</td>
                                <td>${detalle.subtotal}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="3" class="text-end">Total</th>
                            <th>${venta.total}</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<a href="${pageContext.request.contextPath}/ventas" class="btn btn-secondary">Volver</a>
<%@ include file="includes/footer.jspf" %>
