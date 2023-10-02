package org.example.repositories;

import java.sql.SQLException;

/**
 * La interfaz CrudRepository proporciona metodos para realizar operaciones basicas de CRUD
 * (Crear, Leer, Actualizar y Eliminar) en una base de datos o almacen de datos relacionado
 * con objetos de tipo T utilizando identificadores de tipo ID.
 *
 * @param <T>  El tipo de objeto que se almacena
 * @param <ID> El tipo de identificador utilizado para acceder a los objetos
 */
public interface CrudRepository<T, ID> {

    /**
     * Guarda un objeto de tipo T en el repositorio.
     *
     * @param t El objeto de tipo T que se va a guardar en el repositorio.
     * @return El objeto de tipo T guardado en el repositorio.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos o almacen de datos.
     */
    T save(T t) throws SQLException;
}