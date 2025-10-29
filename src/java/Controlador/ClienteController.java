package Controlador;

import Modelo.Cliente;
import ModeloDAO.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador para operaciones CRUD de clientes.
 */
@WebServlet(name = "ClienteController", urlPatterns = {"/clientes"})
public class ClienteController extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null || accion.equals("listar")) {
            request.setAttribute("clientes", clienteDAO.listar());
            request.getRequestDispatcher("WEB-INF/jsp/clientes.jsp").forward(request, response);
            return;
        }

        switch (accion) {
            case "nuevo" -> request.getRequestDispatcher("WEB-INF/jsp/cliente-form.jsp").forward(request, response);
            case "editar" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("cliente", clienteDAO.buscarPorId(id));
                request.getRequestDispatcher("WEB-INF/jsp/cliente-form.jsp").forward(request, response);
            }
            case "eliminar" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                clienteDAO.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/clientes");
            }
            default -> response.sendRedirect(request.getContextPath() + "/clientes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idCliente");
        Cliente cliente = new Cliente();
        if (idParam != null && !idParam.isBlank()) {
            cliente.setIdCliente(parseEntero(idParam));
        }
        cliente.setNombres(request.getParameter("nombres"));
        cliente.setApellidos(request.getParameter("apellidos"));
        cliente.setDni(request.getParameter("dni"));
        cliente.setTelefono(request.getParameter("telefono"));
        cliente.setCorreo(request.getParameter("correo"));
        cliente.setDireccion(request.getParameter("direccion"));

        if (cliente.getIdCliente() > 0) {
            clienteDAO.actualizar(cliente);
        } else {
            clienteDAO.registrar(cliente);
        }
        response.sendRedirect(request.getContextPath() + "/clientes");
    }

    private int parseEntero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
