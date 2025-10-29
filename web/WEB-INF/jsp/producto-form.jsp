<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${producto == null ? 'Nuevo producto' : 'Editar producto'}" />
<%@ include file="includes/header.jspf" %>
<h2>${producto == null ? 'Nuevo producto' : 'Editar producto'}</h2>
<form action="${pageContext.request.contextPath}/productos" method="post" class="row g-3">
    <input type="hidden" name="idProducto" value="${producto.idProducto}" />
    <div class="col-md-6">
        <label for="nombre" class="form-label">Nombre</label>
        <input type="text" class="form-control" id="nombre" name="nombre" value="${producto.nombre}" required>
    </div>
    <div class="col-md-6">
        <label for="precio" class="form-label">Precio</label>
        <input type="number" step="0.01" class="form-control" id="precio" name="precio" value="${producto.precio}" required>
    </div>
    <div class="col-12">
        <label for="descripcion" class="form-label">Descripción</label>
        <textarea class="form-control" id="descripcion" name="descripcion" rows="3">${producto.descripcion}</textarea>
    </div>
    <div class="col-md-4">
        <label for="stock" class="form-label">Stock</label>
        <input type="number" class="form-control" id="stock" name="stock" value="${producto.stock}" required>
    </div>
    <div class="col-md-4">
        <label for="idCategoria" class="form-label">Categoría</label>
        <select class="form-select" id="idCategoria" name="idCategoria" required>
            <option value="" disabled ${producto.idCategoria == 0 ? 'selected' : ''}>Seleccione</option>
            <c:forEach var="categoria" items="${categorias}">
                <option value="${categoria.idCategoria}" ${categoria.idCategoria == producto.idCategoria ? 'selected' : ''}>${categoria.nombre}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-md-4">
        <label for="estado" class="form-label">Estado</label>
        <select class="form-select" id="estado" name="estado">
            <option value="ACTIVO" ${producto.estado == 'ACTIVO' ? 'selected' : ''}>Activo</option>
            <option value="INACTIVO" ${producto.estado == 'INACTIVO' ? 'selected' : ''}>Inactivo</option>
        </select>
    </div>
    <div class="col-12">
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">Cancelar</a>
    </div>
</form>
<%@ include file="includes/footer.jspf" %>
