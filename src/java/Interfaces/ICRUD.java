package Interfaces;

import java.util.List;

/**
 * Interface genérica para operaciones CRUD básicas.
 *
 * @param <T> tipo de entidad a gestionar.
 */
public interface ICRUD<T> {

    List<T> listar();

    T buscarPorId(int id);

    boolean registrar(T entidad);

    boolean actualizar(T entidad);

    boolean eliminar(int id);
}
