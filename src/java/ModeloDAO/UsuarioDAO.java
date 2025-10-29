package ModeloDAO;

import Config.ClsConexion;
import Interfaces.ICRUD;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Usuario}.
 */
public class UsuarioDAO implements ICRUD<Usuario> {

    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class.getName());

    private static final String SQL_LISTAR = "SELECT idUsuario, nombreUsuario, clave, rol, estado FROM usuario";
    private static final String SQL_BUSCAR_ID = "SELECT idUsuario, nombreUsuario, clave, rol, estado FROM usuario WHERE idUsuario = ?";
    private static final String SQL_BUSCAR_USUARIO = "SELECT idUsuario, nombreUsuario, clave, rol, estado FROM usuario WHERE nombreUsuario = ?";
    private static final String SQL_INSERTAR = "INSERT INTO usuario (nombreUsuario, clave, rol, estado) VALUES (?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE usuario SET nombreUsuario = ?, clave = ?, rol = ?, estado = ? WHERE idUsuario = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM usuario WHERE idUsuario = ?";

    private final ClsConexion conexion;

    public UsuarioDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar usuarios", e);
        }
        return usuarios;
    }

    @Override
    public Usuario buscarPorId(int id) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al buscar usuario", e);
        }
        return null;
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR_USUARIO)) {

            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al buscar usuario por nombre", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean registrar(Usuario usuario) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getClave());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al registrar usuario", e);
        }
        return false;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getClave());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getEstado());
            ps.setInt(5, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar usuario", e);
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al eliminar usuario", e);
        }
        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombreUsuario(rs.getString("nombreUsuario"));
        usuario.setClave(rs.getString("clave"));
        usuario.setRol(rs.getString("rol"));
        usuario.setEstado(rs.getString("estado"));
        return usuario;
    }
}
