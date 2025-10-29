package ModeloDAO;

import Config.ClsConexion;
import Interfaces.ICRUD;
import Modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Producto}.
 */
public class ProductoDAO implements ICRUD<Producto> {

    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());

    private static final String SQL_LISTAR = "SELECT idProducto, nombre, descripcion, precio, stock, idCategoria, estado FROM producto";
    private static final String SQL_BUSCAR_ID = "SELECT idProducto, nombre, descripcion, precio, stock, idCategoria, estado FROM producto WHERE idProducto = ?";
    private static final String SQL_INSERTAR = "INSERT INTO producto (nombre, descripcion, precio, stock, idCategoria, estado) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, stock = ?, idCategoria = ?, estado = ? WHERE idProducto = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM producto WHERE idProducto = ?";

    private final ClsConexion conexion;

    public ProductoDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar productos", e);
        }
        return productos;
    }

    @Override
    public Producto buscarPorId(int id) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al buscar producto", e);
        }
        return null;
    }

    @Override
    public boolean registrar(Producto producto) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getIdCategoria());
            ps.setString(6, producto.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al registrar producto", e);
        }
        return false;
    }

    @Override
    public boolean actualizar(Producto producto) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getIdCategoria());
            ps.setString(6, producto.getEstado());
            ps.setInt(7, producto.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar producto", e);
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
            LOG.log(Level.SEVERE, "Error al eliminar producto", e);
        }
        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setIdCategoria(rs.getInt("idCategoria"));
        producto.setEstado(rs.getString("estado"));
        return producto;
    }
}
