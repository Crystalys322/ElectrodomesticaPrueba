package Interfaces;

import Modelo.DetalleVenta;
import Modelo.Venta;
import java.util.List;

/**
 * Interface específica para la gestión de ventas y sus detalles.
 */
public interface IVentaDAO extends ICRUD<Venta> {

    boolean registrarVentaConDetalles(Venta venta, List<DetalleVenta> detalles);

    List<DetalleVenta> listarDetallesPorVenta(int idVenta);
}
