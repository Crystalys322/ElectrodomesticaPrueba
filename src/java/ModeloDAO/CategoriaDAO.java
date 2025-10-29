package ModeloDAO;

import Config.ClsConexion;
import Interfaces.ICRUD;
import Modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación DAO para la entidad {@link Categoria}.
 */
public class CategoriaDAO implements ICRUD<Categoria> {

    private static final Logger LOG = Logger.getLogger(CategoriaDAO.class.getName());

    private static final String SQL_LISTAR = "SELECT idCategoria, nombre, descripcion FROM categoria";
    private static final String SQL_BUSCAR_ID = "SELECT idCategoria, nombre, descripcion FROM categoria WHERE idCategoria = ?";
    private static final String SQL_INSERTAR = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE idCategoria = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM categoria WHERE idCategoria = ?";

    private final ClsConexion conexion;

    public CategoriaDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("idCategoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar categorías", e);
        }
        return categorias;
    }

    @Override
    public Categoria buscarPorId(int id) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(rs.getInt("idCategoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    categoria.setDescripcion(rs.getString("descripcion"));
                    return categoria;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al buscar categoría por ID", e);
        }
        return null;
    }

    @Override
    public boolean registrar(Categoria categoria) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al registrar categoría", e);
        }
        return false;
    }

    @Override
    public boolean actualizar(Categoria categoria) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar categoría", e);
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
            LOG.log(Level.SEVERE, "Error al eliminar categoría", e);
        }
        return false;
    }
}
