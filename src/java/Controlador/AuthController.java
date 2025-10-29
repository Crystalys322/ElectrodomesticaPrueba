package Controlador;

import Modelo.Usuario;
import ModeloDAO.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para autenticación de usuarios.
 */
@WebServlet(name = "AuthController", urlPatterns = {"/login", "/logout"})
public class AuthController extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/logout".equals(servletPath)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreUsuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getClave().equals(clave) && "ACTIVO".equalsIgnoreCase(usuarioOpt.get().getEstado())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuarioOpt.get());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Credenciales inválidas o usuario inactivo");
            request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
