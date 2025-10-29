<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Ventas" />
<%@ include file="includes/header.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Ventas</h2>
    <a href="${pageContext.request.contextPath}/ventas?accion=nueva" class="btn btn-primary"><i class="bi bi-plus"></i> Nueva venta</a>
</div>
<div class="table-responsive">
    <table class="table table-striped align-middle">
        <thead>
        <tr>
            <th>#</th>
            <th>Fecha</th>
            <th>Cliente</th>
            <th>Total</th>
            <th>Estado</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="venta" items="${ventas}">
            <tr>
                <td>${venta.idVenta}</td>
                <td>${venta.fecha}</td>
                <td>${venta.idCliente}</td>
                <td>${venta.total}</td>
                <td>${venta.estado}</td>
                <td class="text-end">
                    <a href="${pageContext.request.contextPath}/ventas?accion=detalle&id=${venta.idVenta}" class="btn btn-sm btn-outline-primary">Ver detalle</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="includes/footer.jspf" %>
