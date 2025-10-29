package Controlador;

import Modelo.DetalleVenta;
import Modelo.Venta;
import ModeloDAO.ClienteDAO;
import ModeloDAO.ProductoDAO;
import ModeloDAO.VentaDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar el flujo de ventas.
 */
@WebServlet(name = "VentaController", urlPatterns = {"/ventas"})
public class VentaController extends HttpServlet {

    private final VentaDAO ventaDAO = new VentaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null || accion.equals("listar")) {
            request.setAttribute("ventas", ventaDAO.listar());
            request.getRequestDispatcher("WEB-INF/jsp/ventas.jsp").forward(request, response);
            return;
        }

        switch (accion) {
            case "nueva" -> {
                request.setAttribute("clientes", clienteDAO.listar());
                request.setAttribute("productos", productoDAO.listar());
                request.getRequestDispatcher("WEB-INF/jsp/venta-form.jsp").forward(request, response);
            }
            case "detalle" -> {
                int idVenta = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("venta", ventaDAO.buscarPorId(idVenta));
                request.setAttribute("detalles", ventaDAO.listarDetallesPorVenta(idVenta));
                request.getRequestDispatcher("WEB-INF/jsp/venta-detalle.jsp").forward(request, response);
            }
            default -> response.sendRedirect(request.getContextPath() + "/ventas");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idCliente = parseEntero(request.getParameter("idCliente"));
        int idUsuario = parseEntero(request.getParameter("idUsuario"));
        String[] idProductos = request.getParameterValues("idProducto");
        String[] cantidades = request.getParameterValues("cantidad");
        String[] precios = request.getParameterValues("precioUnitario");

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        if (idProductos != null) {
            for (int i = 0; i < idProductos.length; i++) {
                int idProducto = parseEntero(idProductos[i]);
                int cantidad = parseEntero(cantidades[i]);
                BigDecimal precio = parseDecimal(precios[i]);
                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));

                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdProducto(idProducto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precio);
                detalle.setSubtotal(subtotal);
                detalles.add(detalle);

                total = total.add(subtotal);
            }
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setIdCliente(idCliente);
        venta.setIdUsuario(idUsuario);
        venta.setTotal(total);
        venta.setEstado("REGISTRADA");

        ventaDAO.registrarVentaConDetalles(venta, detalles);
        response.sendRedirect(request.getContextPath() + "/ventas");
    }

    private int parseEntero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private BigDecimal parseDecimal(String valor) {
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException | NullPointerException e) {
            return BigDecimal.ZERO;
        }
    }
}
