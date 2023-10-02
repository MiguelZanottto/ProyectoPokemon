package org.example.repositories;

import org.example.models.Pokemon;
import org.example.services.DatabaseManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase PokemonRepositoryImpl implementa la interfaz PokemonRepository y proporciona una implementacion
 * concreta para realizar operaciones relacionadas con Pokemon en una base de datos utilizando la clase DatabaseManager.
 */
public class PokemonRepositoryImpl implements PokemonRepository {
    private static PokemonRepositoryImpl instance;
    private final DatabaseManager db;

    /**
     * Constructor privado para la clase PokemonRepositoryImpl.
     *
     * @param db El objeto DatabaseManager utilizado para interactuar con la base de datos.
     */
    private PokemonRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }

    /**
     * Obtiene una instancia unica de PokemonRepositoryImpl.
     *
     * @param db El objeto DatabaseManager utilizado para interactuar con la base de datos.
     * @return La instancia unica de PokemonRepositoryImpl.
     */
    public static PokemonRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new PokemonRepositoryImpl(db);
        }
        return instance;
    }


    /**
     * Busca Pokemon en la base de datos por su nombre.
     *
     * @param name El nombre del Pokemon que se desea buscar.
     * @return Una lista de Pokemon que tienen el nombre especificado.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    @Override
    public List<Pokemon> findByNombre(String name) throws SQLException {
        var lista = new ArrayList<Pokemon>();
        String query = "SELECT * FROM Pokemon WHERE name LIKE ?";
        try (var connection = db.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + name + "%");
            var rs = stmt.executeQuery();
            while (rs.next()) {
                var pokemon = new Pokemon(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
                lista.add(pokemon);
            }
        }
        return lista;
    }

    /**
     * Guarda un Pokemon en la base de datos.
     *
     * @param pokemon El Pokemon que se va a guardar en la base de datos.
     * @return El Pokemon guardado en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    @Override
    public Pokemon save(Pokemon pokemon) throws SQLException {
        String query = "INSERT INTO Pokemon (id, num, name, height, weight) VALUES (?,?,?,?,?)";
        try (var connection = db.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, pokemon.getId());
            stmt.setString(2, pokemon.getNum());
            stmt.setString(3, pokemon.getName());
            stmt.setString(4, pokemon.getHeight());
            stmt.setString(5, pokemon.getWeight());

            stmt.executeUpdate();
        }
        return pokemon;

    }
}