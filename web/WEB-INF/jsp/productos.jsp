<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Productos" />
<%@ include file="includes/header.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Productos</h2>
    <a href="${pageContext.request.contextPath}/productos?accion=nuevo" class="btn btn-primary"><i class="bi bi-plus"></i> Nuevo</a>
</div>
<div class="table-responsive">
    <table class="table table-striped align-middle">
        <thead>
        <tr>
            <th>#</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Estado</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="producto" items="${productos}">
            <tr>
                <td>${producto.idProducto}</td>
                <td>${producto.nombre}</td>
                <td>
                    <c:forEach var="categoria" items="${categorias}">
                        <c:if test="${categoria.idCategoria == producto.idCategoria}">
                            ${categoria.nombre}
                        </c:if>
                    </c:forEach>
                </td>
                <td>${producto.precio}</td>
                <td>${producto.stock}</td>
                <td>${producto.estado}</td>
                <td class="text-end">
                    <a href="${pageContext.request.contextPath}/productos?accion=editar&id=${producto.idProducto}" class="btn btn-sm btn-outline-secondary">Editar</a>
                    <a href="${pageContext.request.contextPath}/productos?accion=eliminar&id=${producto.idProducto}" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Eliminar producto?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="includes/footer.jspf" %>
