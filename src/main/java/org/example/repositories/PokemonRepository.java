package org.example.repositories;

import org.example.models.Pokemon;

import java.sql.SQLException;
import java.util.List;

/**
 *  La interfaz PokemonRepository extiende la interfaz CrudRepository y proporciona metodos especificos
 *  para realizar operaciones relacionadas con Pokemon en un repositorio de datos.
 */
public interface PokemonRepository extends CrudRepository<Pokemon, Long> {

    /**
     * Busca Pokemon en el repositorio por su nombre.
     *
     * @param nombre El nombre del Pokemon que se desea buscar.
     * @return Una lista de Pokemon que tienen el nombre especificado.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos o almacen de datos.
     */
    List<Pokemon> findByNombre(String nombre) throws SQLException;
}