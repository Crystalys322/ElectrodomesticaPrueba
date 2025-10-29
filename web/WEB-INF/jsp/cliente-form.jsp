<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${cliente == null ? 'Nuevo cliente' : 'Editar cliente'}" />
<%@ include file="includes/header.jspf" %>
<h2>${cliente == null ? 'Nuevo cliente' : 'Editar cliente'}</h2>
<form action="${pageContext.request.contextPath}/clientes" method="post" class="row g-3">
    <input type="hidden" name="idCliente" value="${cliente.idCliente}" />
    <div class="col-md-6">
        <label for="nombres" class="form-label">Nombres</label>
        <input type="text" class="form-control" id="nombres" name="nombres" value="${cliente.nombres}" required>
    </div>
    <div class="col-md-6">
        <label for="apellidos" class="form-label">Apellidos</label>
        <input type="text" class="form-control" id="apellidos" name="apellidos" value="${cliente.apellidos}" required>
    </div>
    <div class="col-md-4">
        <label for="dni" class="form-label">DNI</label>
        <input type="text" class="form-control" id="dni" name="dni" value="${cliente.dni}" required>
    </div>
    <div class="col-md-4">
        <label for="telefono" class="form-label">Teléfono</label>
        <input type="text" class="form-control" id="telefono" name="telefono" value="${cliente.telefono}">
    </div>
    <div class="col-md-4">
        <label for="correo" class="form-label">Correo</label>
        <input type="email" class="form-control" id="correo" name="correo" value="${cliente.correo}">
    </div>
    <div class="col-12">
        <label for="direccion" class="form-label">Dirección</label>
        <input type="text" class="form-control" id="direccion" name="direccion" value="${cliente.direccion}">
    </div>
    <div class="col-12">
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="${pageContext.request.contextPath}/clientes" class="btn btn-secondary">Cancelar</a>
    </div>
</form>
<%@ include file="includes/footer.jspf" %>
