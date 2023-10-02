package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.models.NextEvolution;
import org.example.models.Pokedex;
import org.example.models.Pokemon;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Esta clase PokemonController es un controlador para gestionar la Pokedex y realizar diversas operaciones relacionadas con Pokemon.
 */
public class PokemonController {
    public Pokedex pokedex;

    /**
     * Constructor de la clase PokemonController.
     */
    public PokemonController(){
        loadPokedex();
    }

    /**
     * Carga la informacion de la Pokedex desde un archivo JSON en el sistema de archivos.
     * El archivo JSON debe estar ubicado en la carpeta "data" y debe llamarse "pokemon.json".
     * La informacion cargada se almacena en el atributo "pokedex" de la instancia de PokemonController.
     */
    private void loadPokedex(){
        Path relativePath = Paths.get("");
        String rutaRelativa = relativePath.toAbsolutePath().toString();
        String directorio = rutaRelativa + File.separator + "data";
        String jsonFichero = directorio + File.separator + "pokemon.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Reader reader = Files.newBufferedReader(Paths.get(jsonFichero))) {
            this.pokedex = gson.fromJson(reader, new TypeToken<Pokedex>() {}.getType());
            System.out.println("Pokedex loaded! There are: " + pokedex.pokemon.size());
        } catch (Exception e) {
            System.out.println("Error loading Pokedex!");
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Muestra los datos de todos los Pokemon en la Pokedex en la consola.
     */
    public void mostrarDatos(){pokedex.pokemon.forEach(System.out::println);
    }

    /**
     * Obtiene los nombres de los primeros 10 Pokemon en la lista de Pokemon de la Pokedex.
     *
     * @return Una lista de cadenas que contiene los nombres de los primeros 10 Pokemon.
     */
    public List<String> nombrePrimerosPokemons(){
        List<String> nombrePokemons = pokedex.pokemon.stream()
                .limit(10)
                .map(Pokemon::getName)
                .toList();

        return nombrePokemons;
    }

    /**
     * Obtiene los nombres de los ultimos 5 Pokemon en la lista de Pokemon de la Pokedex.
     *
     * @return Una lista de cadenas que contiene los nombres de los ultimos 5 Pokemon.
     */
    public List<String> nombreUltimosPokemons(){
        List<String> nombrePokemons = pokedex.pokemon.stream()
                .sorted(Comparator.comparing(Pokemon::getId).reversed())
                .limit(5)
                .map(Pokemon::getName)
                .toList();

        return nombrePokemons;
    }

    /**
     * Obtiene un Pokemon Pikachu de la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene un Pokemon Pikachu si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista.
     */
    public Optional<Pokemon> obtenerPikachu(){
        Optional<Pokemon> pikachu = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getName().equalsIgnoreCase("Pikachu"))
                .findFirst();

        if(pikachu.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pikachu;
    }

    /**
     * Obtiene la informacion de la evolucion de Charmander de la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene la informacion de la evolucion de Charmander si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista o si no tiene evolucion registrada.
     */
    public Optional<NextEvolution> obtenerEvolucionCharmander(){
        Predicate <Pokemon> charmander = pokemon -> pokemon.getName().equalsIgnoreCase("Charmander");
        Predicate <Pokemon> tieneEvolucion = pokemon -> pokemon.getNextEvolution() != null;
        Optional <NextEvolution> evolucion = pokedex.pokemon.stream()
                .filter(charmander.and(tieneEvolucion))
                .map(pokemon -> pokemon.getNextEvolution().get(0))
                .findFirst();

        if(evolucion.isEmpty()){
            return Optional.of(new NextEvolution());
        }
        return evolucion;
    }

    /**
     * Obtiene los nombres de los Pokemon de tipo Fuego de la lista de Pokemon de la Pokedex.
     *
     * @return Una lista de cadenas que contiene los nombres de los Pokemon de tipo Fuego presentes en la lista.
     */
    public List<String> obtenerPokemonesFuego(){
        List <String> nombrePokemonsFuego = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getType().contains("Fire"))
                .map(pokemon -> pokemon.getName())
                .toList();

        return nombrePokemonsFuego;
    }

    /**
     * Obtiene los nombres de los Pokemon que tienen debilidad de tipo Agua (Water) o de tipo Electrico (Electric).
     *
     * @return Una lista de cadenas que contiene los nombres de los Pokemon con debilidad a los tipos Agua o Electrico.
     */
    public List<String> debilidadWaterElectric(){
        Predicate <Pokemon> debilidadWater = pokemon -> pokemon.getWeaknesses().contains("Water");
        Predicate <Pokemon> debilidadElectric = pokemon -> pokemon.getWeaknesses().contains("Electric");
        List <String> nombrePokemonsDebilidadWaterElectric = pokedex.pokemon
                .stream()
                .filter(debilidadWater.or(debilidadElectric))
                .map(pokemon -> pokemon.getName())
                .toList();

        return nombrePokemonsDebilidadWaterElectric;
    }

    /**
     * Calcula el numero de Pokemon que tienen solo una debilidad.
     *
     * @return El numero de Pokemon con solo una debilidad.
     */
    public Long pokemonsUnaDebilidad(){
        Long numero = pokedex.pokemon
                .stream()
                .filter(pokemon -> pokemon.getWeaknesses().size() == 1)
                .count();

        return numero;
    }

    /**
     * Obtiene el Pokemon que tiene el mayor numero de debilidades en la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene el Pokemon con el mayor numero de debilidades si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista.
     */
    public Optional<Pokemon> pokemonMasDebilidades(){
        Optional <Pokemon> pokemonMasDebil = pokedex.pokemon
                .stream()
                .max(Comparator.comparing(pokemon -> pokemon.getWeaknesses().size()));

        if (pokemonMasDebil.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pokemonMasDebil;
    }

    /**
     * Obtiene el Pokemon con el menor numero de evoluciones de la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene el Pokemon con el menor numero de evoluciones si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista o si ningun Pokemon tiene evoluciones registradas.
     */
    public Optional<Pokemon> pokemonMenosEvoluciones(){
        Optional<Pokemon> pokemonsMenosEvoluciones = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getNextEvolution() != null)
                .min(Comparator.comparing(pokemon -> pokemon.getNextEvolution().size()));

        if (pokemonsMenosEvoluciones.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pokemonsMenosEvoluciones;
    }

    /**
     * Devuelve una lista de Pokémon que tienen al menos una evolucion que no es de tipo "Fire".
     *
     * @return Lista de Pokémon con evoluciones que no son de tipo "Fire".
     */
      public List<Pokemon> pokemonSinEvolucionFuego(){
        List <String> evolucionesNoFuego = pokedex.pokemon.stream()
                .filter(pokemon -> !pokemon.getType().contains("Fire") && pokemon.getPrevEvolution() != null)
                .map(pokemon -> pokemon.getNum())
                .toList();

        List <Pokemon> pokemons = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getNextEvolution()!= null && pokemon.getNextEvolution().stream()
                                                                                                  .map(evolucion -> evolucion.getNum())
                                                                                                  .anyMatch(numeroEvolucion -> evolucionesNoFuego.contains(numeroEvolucion)))
                .toList();

                return pokemons;
    }

    /**
     * Sirve para formatear los datos Peso y Altura de un Pokemon quitandole los espacios en blanco y las letras.
     * @param texto La cadena que se quiere formatear
     * @return La cadena sin espacios en blanco
     */
    public static Double formatearTexto(String texto){
        return Double.parseDouble(texto.substring(0, texto.indexOf(32)));
    }

    /**
     * Obtiene el Pokemon con el mayor peso de la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene el Pokemon con el mayor peso si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista.
     * @throws NumberFormatException Si no se puede convertir el peso de un Pokémon a un valor numerico valido.
     */
    public Optional<Pokemon> pokemonMasPesado(){
        OptionalDouble maximoPeso = pokedex.pokemon.stream().mapToDouble(pokemon -> formatearTexto(pokemon.getWeight())).max();
        Optional<Pokemon> pokemonMasPesado = pokedex.pokemon.stream()
                .filter(pokemon -> formatearTexto(pokemon.getWeight()) == maximoPeso.getAsDouble())
                .findFirst();

        if (pokemonMasPesado.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pokemonMasPesado;
    }

    /**
     * Obtiene el Pokemon mas alto de la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene el Pokemon mas alto si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista.
     * @throws NumberFormatException Si no se puede convertir la altura de un Pokemon a un valor numerico valido.
     */
    public Optional<Pokemon> pokemonMasAlto(){
        OptionalDouble alturaMaxima = pokedex.pokemon.stream().mapToDouble(pokemon -> formatearTexto(pokemon.getHeight())).max();
        Optional <Pokemon> pokemonMasAlto = pokedex.pokemon.stream()
                .filter(pokemon -> formatearTexto(pokemon.getHeight()) == alturaMaxima.getAsDouble())
                .findFirst();

        if (pokemonMasAlto.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pokemonMasAlto;
    }

    /**
     * Obtiene el Pokemon cuyo nombre es el mas largo en la lista de Pokemon de la Pokedex, si esta presente.
     *
     * @return Un objeto Optional que contiene el Pokemon con el nombre mas largo si se encuentra en la lista,
     *         o un Optional vacio si no se encuentra en la lista.
     */
    public Optional<Pokemon> pokemonConElNombreMasLargo(){
        Optional <Pokemon> pokemonNombreLargo = pokedex.pokemon.stream()
                .max(Comparator.comparing(pokemon -> pokemon.getName().length()));

        if (pokemonNombreLargo.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pokemonNombreLargo;
    }


    /**
     * Calcula y devuelve la media de peso de los Pokemon en la lista de la Pokedex.
     *
     * @return La media de peso de los Pokemon en la lista como un valor de tipo double.
     *         Si la lista esta vacia, se devuelve 0.0.
     * @throws NumberFormatException Si no se puede convertir el peso de un Pokemon a un valor numerico valido.
     */
    public Double mediaDePeso(){
        Double mediaPeso = pokedex.pokemon.stream()
                .mapToDouble(pokemon -> formatearTexto(pokemon.getWeight()))
                .average()
                .getAsDouble();

        return mediaPeso != null ? mediaPeso: 0.0;
    }


    /**
     * Calcula y devuelve la media de altura de los Pokemon en la lista de la Pokedex.
     *
     * @return La media de altura de los Pokemon en la lista como un valor de tipo double.
     *         Si la lista esta vacía, se devuelve 0.0.
     * @throws NumberFormatException Si no se puede convertir la altura de un Pokémon a un valor numerico valido.
     */
    public Double mediaAltura(){
        Double mediaAltura = pokedex.pokemon.stream()
                .mapToDouble(pokemon -> formatearTexto(pokemon.getHeight()))
                .average()
                .getAsDouble();

        return mediaAltura != null ? mediaAltura: 0.0;
    }

    /**
     * Calcula y devuelve la media del numero de evoluciones de los Pokemon que tienen al menos una evolucion registrada en la lista de la Pokedex.
     *
     * @return La media del numero de evoluciones de los Pokémon en la lista como un valor de tipo double.
     *         Si la lista esta vacia o si no hay Pokemon con evoluciones registradas, se devuelve 0.0.
     */
    public Double mediaEvoluciones(){
        Double mediaEvoluciones = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getNextEvolution()!= null)
                .mapToDouble(pokemon -> pokemon.getNextEvolution().size())
                .average()
                .getAsDouble();

        return mediaEvoluciones != null ? mediaEvoluciones: 0.0;
    }

    /**
     * Calcula y devuelve la media del numero de debilidades de los Pokemon que tienen al menos una debilidad registrada en la lista de la Pokedex.
     *
     * @return La media del numero de debilidades de los Pokemon en la lista como un valor de tipo double.
     *         Si la lista esta vacia o si no hay Pokemon con debilidades registradas, se devuelve 0.0.
     */
    public Double mediaDebilidades(){
        Double mediaDebilidades = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getWeaknesses()!= null)
                .mapToDouble(pokemon -> pokemon.getWeaknesses().size())
                .average()
                .getAsDouble();

        return mediaDebilidades != null ? mediaDebilidades: 0.0;
    }

    /**
     * Agrupa los Pokemon por tipo y devuelve un mapa donde las claves son los tipos y los valores son listas de Pokemon de ese tipo.
     *
     * @return Un mapa donde las claves son los tipos de Pokemon y los valores son listas de Pokémon de ese tipo.
     */
    public Map<String, List<Pokemon>> pokemonsAgrupadosTipo(){
        Map<String, List<Pokemon>> pokemonPorTipo = pokedex.pokemon.stream()
                .flatMap(pokemon -> pokemon.getType().stream())
                .distinct()
                .collect(Collectors.toMap(
                        tipo -> tipo,
                        tipo -> pokedex.pokemon.stream()
                                .filter(pokemon -> pokemon.getType().contains(tipo))
                                .collect(Collectors.toList())));

        return pokemonPorTipo;
    }

    /**
     * Agrupa los Pokemon por debilidad y devuelve un mapa donde las claves son las debilidades y los valores son la cantidad de Pokemon que tienen esa debilidad.
     *
     * @return Un mapa donde las claves son las debilidades de Pokémon y los valores son la cantidad de Pokémon que tienen esa debilidad.
     */
    public Map<String, Long> pokemonsAgrupadosPorDebilidad(){
        Map<String, Long> pokemonsAgrupados = pokedex.pokemon.stream()
                .map(pokemon -> pokemon.getWeaknesses())
                .flatMap(pokemon -> pokemon.stream())
                .collect(
                        Collectors.groupingBy(pokemon -> pokemon, Collectors.counting()));

        return pokemonsAgrupados;
    }

    /**
     * Agrupa los Pokemon por el numero de evoluciones que tienen y devuelve un mapa donde las claves son
     * el numero de evoluciones y los valores son listas de Pokemon que tienen ese numero de evoluciones.
     *
     * @return Un mapa donde las claves son el numero de evoluciones de Pokémon y los valores son listas de Pokémon
     *         que tienen ese numero de evoluciones.
     */
    public Map<Long, List<Pokemon>> pokemonsAgrupadosPorEvolucion(){
        Map<Long, List<Pokemon>> pokemonPorNumeroDeEvoluciones = pokedex.pokemon.stream()
                .collect(Collectors.groupingBy(
                        pokemon -> pokemon.getNextEvolution() == null? 0 : (long) pokemon.getNextEvolution().size()
                ));

        return pokemonPorNumeroDeEvoluciones;
        }

    /**
     * Encuentra la debilidad mas comun entre los Pokemon y la devuelve como una cadena de texto.
     *
     * @return La debilidad mas comun entre los Pokemon como una cadena de texto.
     *         Si hay varias debilidades igualmente comunes, se devuelve la primera encontrada.
     *         Si no hay debilidades registradas en la lista, se devuelve una cadena vacia.
     */
    public String debilidadMasComun(){
        Map<String, Long> pokemonsAgrupados = pokedex.pokemon.stream()
                .map(pokemon -> pokemon.getWeaknesses())
                .flatMap(pokemon -> pokemon.stream())
                .collect(
                        Collectors.groupingBy(pokemon -> pokemon, Collectors.counting()));
        String debilidadComun = pokemonsAgrupados.entrySet().stream().max(Comparator.comparingLong(e -> e.getValue())).get().getKey();

        return debilidadComun;
    }
}
