package ModeloDAO;

import Config.ClsConexion;
import Interfaces.IVentaDAO;
import Modelo.DetalleVenta;
import Modelo.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para gestionar ventas y sus detalles.
 */
public class VentaDAO implements IVentaDAO {

    private static final Logger LOG = Logger.getLogger(VentaDAO.class.getName());

    private static final String SQL_LISTAR = "SELECT idVenta, fecha, idCliente, idUsuario, total, estado FROM venta";
    private static final String SQL_BUSCAR_ID = "SELECT idVenta, fecha, idCliente, idUsuario, total, estado FROM venta WHERE idVenta = ?";
    private static final String SQL_INSERTAR = "INSERT INTO venta (fecha, idCliente, idUsuario, total, estado) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE venta SET fecha = ?, idCliente = ?, idUsuario = ?, total = ?, estado = ? WHERE idVenta = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM venta WHERE idVenta = ?";

    private static final String SQL_INSERTAR_DETALLE = "INSERT INTO detalle_venta (idVenta, idProducto, cantidad, precioUnitario, subtotal) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_LISTAR_DETALLES = "SELECT idDetalle, idVenta, idProducto, cantidad, precioUnitario, subtotal FROM detalle_venta WHERE idVenta = ?";

    private final ClsConexion conexion;

    public VentaDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<Venta> listar() {
        List<Venta> ventas = new ArrayList<>();
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ventas.add(mapearVenta(rs));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar ventas", e);
        }
        return ventas;
    }

    @Override
    public Venta buscarPorId(int id) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVenta(rs);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al buscar venta", e);
        }
        return null;
    }

    @Override
    public boolean registrar(Venta venta) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERTAR)) {

            ps.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            ps.setInt(2, venta.getIdCliente());
            ps.setInt(3, venta.getIdUsuario());
            ps.setBigDecimal(4, venta.getTotal());
            ps.setString(5, venta.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al registrar venta", e);
        }
        return false;
    }

    @Override
    public boolean actualizar(Venta venta) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            ps.setInt(2, venta.getIdCliente());
            ps.setInt(3, venta.getIdUsuario());
            ps.setBigDecimal(4, venta.getTotal());
            ps.setString(5, venta.getEstado());
            ps.setInt(6, venta.getIdVenta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar venta", e);
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
            LOG.log(Level.SEVERE, "Error al eliminar venta", e);
        }
        return false;
    }

    @Override
    public boolean registrarVentaConDetalles(Venta venta, List<DetalleVenta> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return false;
        }

        try (Connection cn = conexion.getConnection()) {
            try {
                cn.setAutoCommit(false);

                int idVentaGenerado;
                try (PreparedStatement psVenta = cn.prepareStatement(SQL_INSERTAR, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psVenta.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
                    psVenta.setInt(2, venta.getIdCliente());
                    psVenta.setInt(3, venta.getIdUsuario());
                    psVenta.setBigDecimal(4, venta.getTotal());
                    psVenta.setString(5, venta.getEstado());
                    psVenta.executeUpdate();

                    try (ResultSet rs = psVenta.getGeneratedKeys()) {
                        if (rs.next()) {
                            idVentaGenerado = rs.getInt(1);
                        } else {
                            cn.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement psDetalle = cn.prepareStatement(SQL_INSERTAR_DETALLE)) {
                    for (DetalleVenta detalle : detalles) {
                        psDetalle.setInt(1, idVentaGenerado);
                        psDetalle.setInt(2, detalle.getIdProducto());
                        psDetalle.setInt(3, detalle.getCantidad());
                        psDetalle.setBigDecimal(4, detalle.getPrecioUnitario());
                        psDetalle.setBigDecimal(5, detalle.getSubtotal());
                        psDetalle.addBatch();
                    }
                    psDetalle.executeBatch();
                }

                cn.commit();
                return true;
            } catch (SQLException e) {
                cn.rollback();
                LOG.log(Level.SEVERE, "Error al registrar venta con detalles", e);
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error en la transacción de venta", e);
        }
        return false;
    }

    @Override
    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LISTAR_DETALLES)) {

            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearDetalle(rs));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar detalles de venta", e);
        }
        return detalles;
    }

    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setIdVenta(rs.getInt("idVenta"));
        Timestamp timestamp = rs.getTimestamp("fecha");
        venta.setFecha(timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now());
        venta.setIdCliente(rs.getInt("idCliente"));
        venta.setIdUsuario(rs.getInt("idUsuario"));
        venta.setTotal(rs.getBigDecimal("total"));
        venta.setEstado(rs.getString("estado"));
        return venta;
    }

    private DetalleVenta mapearDetalle(ResultSet rs) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdDetalle(rs.getInt("idDetalle"));
        detalle.setIdVenta(rs.getInt("idVenta"));
        detalle.setIdProducto(rs.getInt("idProducto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precioUnitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        return detalle;
    }
}
