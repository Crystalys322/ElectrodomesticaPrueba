package Controlador;

import ModeloDAO.CategoriaDAO;
import ModeloDAO.ClienteDAO;
import ModeloDAO.ProductoDAO;
import ModeloDAO.VentaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador principal del panel.
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalProductos", productoDAO.listar().size());
        request.setAttribute("totalClientes", clienteDAO.listar().size());
        request.setAttribute("totalVentas", ventaDAO.listar().size());
        request.setAttribute("totalCategorias", categoriaDAO.listar().size());
        request.getRequestDispatcher("WEB-INF/jsp/dashboard.jsp").forward(request, response);
    }
}
