<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Clientes" />
<%@ include file="includes/header.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Clientes</h2>
    <a href="${pageContext.request.contextPath}/clientes?accion=nuevo" class="btn btn-primary"><i class="bi bi-plus"></i> Nuevo</a>
</div>
<div class="table-responsive">
    <table class="table table-striped align-middle">
        <thead>
        <tr>
            <th>#</th>
            <th>Nombres</th>
            <th>Apellidos</th>
            <th>DNI</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Dirección</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cliente" items="${clientes}">
            <tr>
                <td>${cliente.idCliente}</td>
                <td>${cliente.nombres}</td>
                <td>${cliente.apellidos}</td>
                <td>${cliente.dni}</td>
                <td>${cliente.telefono}</td>
                <td>${cliente.correo}</td>
                <td>${cliente.direccion}</td>
                <td class="text-end">
                    <a href="${pageContext.request.contextPath}/clientes?accion=editar&id=${cliente.idCliente}" class="btn btn-sm btn-outline-secondary">Editar</a>
                    <a href="${pageContext.request.contextPath}/clientes?accion=eliminar&id=${cliente.idCliente}" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Eliminar cliente?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="includes/footer.jspf" %>
