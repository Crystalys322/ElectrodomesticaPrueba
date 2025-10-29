<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Nueva venta" />
<%@ include file="includes/header.jspf" %>
<h2>Nueva venta</h2>
<form action="${pageContext.request.contextPath}/ventas" method="post" id="ventaForm">
    <div class="row g-3 mb-3">
        <div class="col-md-6">
            <label for="idCliente" class="form-label">Cliente</label>
            <select name="idCliente" id="idCliente" class="form-select" required>
                <option value="" selected disabled>Seleccione un cliente</option>
                <c:forEach var="cliente" items="${clientes}">
                    <option value="${cliente.idCliente}">${cliente.nombres} ${cliente.apellidos}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-6">
            <label for="idUsuario" class="form-label">Usuario responsable</label>
            <input type="number" class="form-control" id="idUsuario" name="idUsuario" placeholder="ID del usuario" required>
        </div>
    </div>
    <div class="table-responsive mb-3">
        <table class="table table-bordered align-middle" id="detalleTabla">
            <thead class="table-light">
            <tr>
                <th style="width: 40%">Producto</th>
                <th style="width: 15%">Cantidad</th>
                <th style="width: 20%">Precio unitario</th>
                <th style="width: 20%">Subtotal</th>
                <th style="width: 5%"></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <select name="idProducto" class="form-select" required onchange="actualizarPrecio(this)">
                        <option value="" disabled selected>Seleccione</option>
                        <c:forEach var="producto" items="${productos}">
                            <option value="${producto.idProducto}" data-precio="${producto.precio}">${producto.nombre}</option>
                        </c:forEach>
                    </select>
                </td>
                <td><input type="number" name="cantidad" class="form-control" min="1" value="1" required onchange="recalcularFila(this)"></td>
                <td><input type="number" name="precioUnitario" class="form-control" step="0.01" min="0" value="0" required onchange="recalcularFila(this)"></td>
                <td class="subtotal">0.00</td>
                <td class="text-center"><button type="button" class="btn btn-sm btn-danger" onclick="eliminarFila(this)"><i class="bi bi-trash"></i></button></td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <button type="button" class="btn btn-outline-primary" onclick="agregarFila()"><i class="bi bi-plus-circle"></i> Agregar producto</button>
        <div class="h4">Total: S/ <span id="totalVenta">0.00</span></div>
    </div>
    <div>
        <button type="submit" class="btn btn-success">Registrar venta</button>
        <a href="${pageContext.request.contextPath}/ventas" class="btn btn-secondary">Cancelar</a>
    </div>
</form>
<script>
    function agregarFila() {
        const tabla = document.querySelector('#detalleTabla tbody');
        const fila = tabla.rows[0].cloneNode(true);
        fila.querySelectorAll('select, input').forEach(elem => {
            if (elem.tagName === 'SELECT') {
                elem.selectedIndex = 0;
            } else {
                elem.value = elem.name === 'cantidad' ? '1' : '0';
            }
        });
        fila.querySelector('.subtotal').textContent = '0.00';
        tabla.appendChild(fila);
    }

    function eliminarFila(boton) {
        const tabla = document.querySelector('#detalleTabla tbody');
        if (tabla.rows.length > 1) {
            boton.closest('tr').remove();
            recalcularTotal();
        }
    }

    function actualizarPrecio(select) {
        const precio = select.options[select.selectedIndex].dataset.precio || '0';
        const fila = select.closest('tr');
        fila.querySelector('input[name="precioUnitario"]').value = parseFloat(precio).toFixed(2);
        recalcularFila(select);
    }

    function recalcularFila(elemento) {
        const fila = elemento.closest('tr');
        const cantidad = parseInt(fila.querySelector('input[name="cantidad"]').value) || 0;
        const precio = parseFloat(fila.querySelector('input[name="precioUnitario"]').value) || 0;
        const subtotal = (cantidad * precio).toFixed(2);
        fila.querySelector('.subtotal').textContent = subtotal;
        recalcularTotal();
    }

    function recalcularTotal() {
        let total = 0;
        document.querySelectorAll('#detalleTabla tbody .subtotal').forEach(cell => {
            total += parseFloat(cell.textContent) || 0;
        });
        document.getElementById('totalVenta').textContent = total.toFixed(2);
    }

    document.querySelectorAll('#detalleTabla input, #detalleTabla select').forEach(elem => {
        elem.addEventListener('change', () => recalcularFila(elem));
    });
</script>
<%@ include file="includes/footer.jspf" %>
