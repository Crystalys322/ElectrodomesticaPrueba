package ModeloDAO;

import Config.ClsConexion;
import Interfaces.ICRUD;
import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para gestionar clientes.
 */
public class ClienteDAO implements ICRUD<Cliente> {

    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    private static final String SQL_LISTAR = "SELECT idCliente, nombres, apellidos, dni, telefono, correo, direccion FROM cliente";
    private static final String SQL_BUSCAR_ID = "SELECT idCliente, nombres, apellidos, dni, telefono, correo, direccion FROM cliente WHERE idCliente = ?";
    private static final String SQL_INSERTAR = "INSERT INTO cliente (nombres, apellidos, dni, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE cliente SET nombres = ?, apellidos = ?, dni = ?, telefono = ?, correo = ?, direccion = ? WHERE idCliente = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM cliente WHERE idCliente = ?";

    private final ClsConexion conexion;

    public ClienteDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar clientes", e);
        }
        return clientes;
    }

    @Override
    public Cliente buscarPorId(int id) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al buscar cliente", e);
        }
        return null;
    }

    @Override
    public boolean registrar(Cliente cliente) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al registrar cliente", e);
        }
        return false;
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        try (Connection cn = conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());
            ps.setInt(7, cliente.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar cliente", e);
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
            LOG.log(Level.SEVERE, "Error al eliminar cliente", e);
        }
        return false;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNombres(rs.getString("nombres"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setDireccion(rs.getString("direccion"));
        return cliente;
    }
}
