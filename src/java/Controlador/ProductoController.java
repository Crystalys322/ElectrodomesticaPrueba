package Controlador;

import Modelo.Producto;
import ModeloDAO.CategoriaDAO;
import ModeloDAO.ProductoDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Controlador para gestionar productos.
 */
@WebServlet(name = "ProductoController", urlPatterns = {"/productos"})
public class ProductoController extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null || accion.equals("listar")) {
            request.setAttribute("productos", productoDAO.listar());
            request.setAttribute("categorias", categoriaDAO.listar());
            request.getRequestDispatcher("WEB-INF/jsp/productos.jsp").forward(request, response);
            return;
        }

        switch (accion) {
            case "nuevo" -> {
                request.setAttribute("categorias", categoriaDAO.listar());
                request.getRequestDispatcher("WEB-INF/jsp/producto-form.jsp").forward(request, response);
            }
            case "editar" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("producto", productoDAO.buscarPorId(id));
                request.setAttribute("categorias", categoriaDAO.listar());
                request.getRequestDispatcher("WEB-INF/jsp/producto-form.jsp").forward(request, response);
            }
            case "eliminar" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                productoDAO.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/productos");
            }
            default -> response.sendRedirect(request.getContextPath() + "/productos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idProducto");
        Producto producto = new Producto();
        if (idParam != null && !idParam.isBlank()) {
            producto.setIdProducto(parseEntero(idParam));
        }
        producto.setNombre(request.getParameter("nombre"));
        producto.setDescripcion(request.getParameter("descripcion"));
        producto.setPrecio(parseDecimal(request.getParameter("precio")));
        producto.setStock(parseEntero(request.getParameter("stock")));
        producto.setIdCategoria(parseEntero(request.getParameter("idCategoria")));
        producto.setEstado(request.getParameter("estado"));

        if (producto.getIdProducto() > 0) {
            productoDAO.actualizar(producto);
        } else {
            productoDAO.registrar(producto);
        }
        response.sendRedirect(request.getContextPath() + "/productos");
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
