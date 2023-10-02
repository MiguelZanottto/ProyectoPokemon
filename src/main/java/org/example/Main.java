package org.example;

import org.example.controllers.ExportarCSV;
import org.example.controllers.LeerCSV;
import org.example.controllers.PokemonController;
import org.example.models.Pokemon;
import org.example.repositories.PokemonRepositoryImpl;
import org.example.services.DatabaseManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Pokemons en la Pokedex");
        PokemonController pokemons = new PokemonController();
        System.out.println();

        System.out.println("================== Nombre de los 10 primeros pokemons ==================");
        pokemons.nombrePrimerosPokemons().forEach(System.out::println);

        System.out.println("================== Nombre de los 5 ultimos pokemons ==================");
        pokemons.nombreUltimosPokemons().forEach(System.out::println);

        System.out.println("================== Datos de Pikachu ==================");
        if(pokemons.obtenerPikachu().isPresent()) {
            System.out.println(pokemons.obtenerPikachu().get());
        } else {
            System.out.println("No se encuentra Pikachu en la pokedex");
        }

        System.out.println("================== Evolucion de Charmander ==================");
        if(pokemons.obtenerEvolucionCharmander().isPresent()) {
            System.out.println(pokemons.obtenerEvolucionCharmander().get());
        } else {
            System.out.println("No se encuentra la evolucion de charmander en la pokedex");
        }

        System.out.println("================== Pokemones Fuego ==================");
        pokemons.obtenerPokemonesFuego().forEach(System.out::println);

        System.out.println("================== Pokemons con debilidad al Agua y a la Electricidad ==================");
        pokemons.debilidadWaterElectric().forEach(System.out::println);

        System.out.println("================== Numero de Pokemons con una sola debilidad ==================");
        Long pokemonsUnaDebilidad = pokemons.pokemonsUnaDebilidad();
        System.out.println(pokemonsUnaDebilidad);

        System.out.println("================== Pokemon con mas debilidades ==================");
        if(pokemons.pokemonMasDebilidades().isPresent()) {
            System.out.println(pokemons.pokemonMasDebilidades().get());;
        } else {
            System.out.println("No se encontro al Pokemon con mas debilidades en la pokedex");
        }

        System.out.println("================== Pokemon con menos evoluciones ==================");
        if(pokemons.pokemonMenosEvoluciones().isPresent()) {
            System.out.println(pokemons.pokemonMenosEvoluciones().get());;
        } else {
            System.out.println("No se encontro al Pokemon con menos evoluciones en la pokedex");
        }

        System.out.println("================== Pokemon con al menos una evolucion que no sea tipo Fire ==================");
        pokemons.pokemonSinEvolucionFuego().forEach(System.out::println);

        System.out.println("================== Pokemon mas pesado ==================");
        if(pokemons.pokemonMasPesado().isPresent()) {
            System.out.println(pokemons.pokemonMasPesado().get());;
        } else {
            System.out.println("No se encontro al Pokemon mas pesado en la pokedex");
        }

        System.out.println("================== Pokemon mas alto ================");
        if(pokemons.pokemonMasAlto().isPresent()) {
            System.out.println(pokemons.pokemonMasAlto().get());;
        } else {
            System.out.println("No se encontro al Pokemon mas alto en la pokedex");
        }

        System.out.println("================== Pokemon con el nombre mas largo ================");
        if(pokemons.pokemonConElNombreMasLargo().isPresent()) {
            System.out.println(pokemons.pokemonConElNombreMasLargo().get());;
        } else {
            System.out.println("No se encontro al Pokemon con el nombre mas largo en la pokedex");
        }

        System.out.println("================== Media de peso de los Pokemons ================");
        Double mediaPeso = pokemons.mediaDePeso();
        System.out.println(mediaPeso);

        System.out.println("================== Media de altura de los Pokemons ==================");
        Double mediaAltura = pokemons.mediaAltura();
        System.out.println(mediaAltura);

        System.out.println("================== Media de evoluciones de los Pokemons ==================");
        Double mediaEvoluciones = pokemons.mediaEvoluciones();
        System.out.println(mediaEvoluciones);

        System.out.println("================== Media de debilidades de los Pokemons ==================");
        Double mediaDebilidades = pokemons.mediaDebilidades();
        System.out.println(mediaDebilidades);

        System.out.println("================== Pokemons agrupados por tipo ==================");
        pokemons.pokemonsAgrupadosTipo().forEach((a,b) -> System.out.println(a + " " + b));

        System.out.println("================== Numero de Pokemons agrupados por debilidad ==================");
        pokemons.pokemonsAgrupadosPorDebilidad().forEach((a,b) -> System.out.println(a + " " + b));

        System.out.println("================== Pokemons agrupados por numero de evoluciones ==================");
        pokemons.pokemonsAgrupadosPorEvolucion().forEach((a,b) -> System.out.println(a + " " + b));

        System.out.println("================== Debilidad mas comun ==================");
        String debilidadMasComun = pokemons.debilidadMasComun();
        System.out.println(debilidadMasComun);

        System.out.println("Exportamos a un fichero CSV...");
        ExportarCSV exportadorCSV = new ExportarCSV();
        exportadorCSV.export();

        System.out.println("Leemos el fichero CSV exportado...");
        LeerCSV lectorCSV = new LeerCSV();
        var lista = lectorCSV.read(); //Almacenados los datos leidos en una lista para usarlos en la BD
        lista.forEach(System.out::println);

        System.out.println("Realizamos la conexion con la base de datos...");
        DatabaseManager db = DatabaseManager.getInstance();

        System.out.println("Empezamos a realizar operaciones en la base de datos...");
        PokemonRepositoryImpl p = PokemonRepositoryImpl.getInstance(db);

        System.out.println("Almacenados los datos leidos de la Lista en la BD...");
        lista.forEach(pokemon -> {
            try {
                p.save(pokemon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } );

        System.out.println("Recuperamos a Pikachu por su nombre en la BD e imprimimos sus datos...");
        System.out.println("================== Datos de Pikachu ==================");
        System.out.println(p.findByNombre("Pikachu"));
    }
}

