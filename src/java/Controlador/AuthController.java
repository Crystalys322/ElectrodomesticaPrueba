package Controlador;

import Modelo.Usuario;
import ModeloDAO.UsuarioDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para autenticación de usuarios.
 */
@WebServlet(name = "AuthController", urlPatterns = {"/login", "/logout"})
public class AuthController extends HttpServlet {

    private static final String USUARIO_DEMO = "cliente";
    private static final String CLAVE_DEMO = "tienda123";

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

        Optional<Usuario> usuarioOpt = autenticar(nombreUsuario, clave);

        if (usuarioOpt.isPresent()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuarioOpt.get());
            response.sendRedirect(request.getContextPath() + "/tienda");
            return;
        }

        request.setAttribute("error", "Credenciales inválidas o usuario inactivo");
        request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
    }

    private Optional<Usuario> autenticar(String nombreUsuario, String clave) {
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            boolean claveCorrecta = clave != null && clave.equals(usuario.getClave());
            boolean activo = "ACTIVO".equalsIgnoreCase(usuario.getEstado());
            if (claveCorrecta && activo) {
                return usuarioOpt;
            }
            return Optional.empty();
        }

        if (USUARIO_DEMO.equalsIgnoreCase(nombreUsuario) && CLAVE_DEMO.equals(clave)) {
            Usuario usuarioDemo = new Usuario();
            usuarioDemo.setNombreUsuario(USUARIO_DEMO);
            usuarioDemo.setRol("CLIENTE");
            usuarioDemo.setEstado("ACTIVO");
            return Optional.of(usuarioDemo);
        }

        return Optional.empty();
    }
}
