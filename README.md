# Proyecto Java con SQLite
***
Este proyecto es una aplicación simple en Java que utiliza SQLite como base de datos, asi como tambien, crea un controlador de Pokemon para realizar consultas en el programa de datos provenientes de un fichero json. A continuación, se describen los pasos para configurar y ejecutar el proyecto.

## Requisitos
***
* Java 8 o superior
* Gradle

## Configuración
***
### Paso 1: Dependencias de Gradle

Agrega las siguientes dependencias a tu archivo `build.gradle`:



```kotlin
plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
// Lombok para generar código
    implementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
// Gson para leer y escribir JSON
    implementation("com.google.code.gson:gson:2.10.1")
// OpenCSV para leer archivos CSV
    implementation("com.opencsv:opencsv:5.8")
// SQLite para la base de datos
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
// Ibatis para leer los scripts SQL desde archivos
    implementation("org.mybatis:mybatis:3.5.13")
}
```

### Paso 2: Crear la clase PrevEvolution

Crear una clase `PrevEvolution` con los atributos  `num` y `name`.

```java
 package org.example.models;

import lombok.Data;

@Data
public class PrevEvolution {
    private String num;
    private String name;
}
 ```

### Paso 3: Crear la clase NextEvolution

Crear una clase `NextEvolution` con los atributos  `num` y `name` y sus constructores.

```java
 package org.example.models;

import lombok.Data;

@Data
public class NextEvolution{
	private String num;
	private String name;
    
    // Constructor con parametros
	public NextEvolution(String num, String name) {
		this.num = this.num;
		this.name = this.name;
	}
    
    // Constructor sin parametros
	public NextEvolution() {}
}
```

### Paso 4: Crear la clase Pokemon

Crear una clase `Pokemon` con los atributos `id`, `num`, `name`, `img`, `type`, `height`, `weight`, `candy`, `candyCount`, `egg`, `spawnChances`, `avgSpawns`, `spawnTime`, `multipliers`, `weaknesses`, `nexEvolution` y `prevEvolution`. También utilizaremos anotaciones `@SerializedName` y `@Expose` para serializar los atributos segun el nombre que aparece en el JSON, asi evitamos conflicto al leer el archivo y guardar datos. Por ultimo, crearemos un `toString()` y 2 constructores: uno con parametros y otro sin parametros, el constructor con parametros nos servirá al momento de leer el fichero CSV que solo contendra en el, esos atributos especificos.

```java
package org.example.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;


import java.util.List;

@Data
@Getter
@Setter
public class Pokemon{
	private int id;
	private String num;
	private String name;
	private String img;
	private List<String> type;
	private String height;
	private String weight;
	private String candy;
	@SerializedName("candy_count")
	@Expose
	private int candyCount;
	private String egg;
	private Object spawnChance;
	@SerializedName("avg_spawns")
	@Expose
	private double avgSpawns;
	@SerializedName("spawn_time")
	@Expose
	private String spawnTime;
	private List<Object> multipliers;
	private List<String> weaknesses;
	@SerializedName("next_evolution")
	@Expose
	private List<NextEvolution> nextEvolution;
	@SerializedName("prev_evolution")
	@Expose
	private List<PrevEvolution> prevEvolution;

	
	public Pokemon(int id, String num, String name, String height, String weight) {
		this.id = id;
		this.num = num;
		this.name = name;
		this.height = height;
		this.weight = weight;
	}
    
	public Pokemon() {
	}
    
	//toString()
	}
```

### Paso 5: Crear la clase Pokedex

Crear una clase `Pokedex` que solo tendra un atributo `Pokemon` que será una lista que guardara todos los Pokemons.

```java
package org.example.models;

import lombok.Data;
import java.util.List;

@Data
public class Pokedex {
	public List<Pokemon> pokemon;
}
```

### Paso 6: Crear la clase PokemonController

Crear una clase `PokemonController` con un atributo 'Pokedex', esta clase sirve como un controlador para interactuar con los Pokemons que se encuentra guardados en la Pokedex, esto se logra a traves de consultas que nos duelven resultados segun especificacion. Entre los metodos más importantes tenemos: `loadPokedex()` que nos permite leer el fichero `pokemon.json` y cargar la informacion que contiene de los Pokemons en la pokedex.

```java 
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

public class PokemonController {
    public Pokedex pokedex;
    
    // Constructor
    public PokemonController(){
        loadPokedex();
    }
    
    // Carga los pokemon en la pokedex leyendolos del archivo JSON
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
    
    // Muestra datos de los pokemon en la pokedex
    public void mostrarDatos(){pokedex.pokemon.forEach(System.out::println);
    }
    
    // Devuelve los primeros 10 nombres de pokemons en la pokedex
    public List<String> nombrePrimerosPokemons(){
        List<String> nombrePokemons = pokedex.pokemon.stream()
                .limit(10)
                .map(Pokemon::getName)
                .toList();

        return nombrePokemons;
    }
    
    // Devuelve los ultimo 5 nombres de pokemons en la pokedex
    public List<String> nombreUltimosPokemons(){
        List<String> nombrePokemons = pokedex.pokemon.stream()
                .sorted(Comparator.comparing(Pokemon::getId).reversed())
                .limit(5)
                .map(Pokemon::getName)
                .toList();

        return nombrePokemons;
    }
    
    // Devuelve el Pokemon con el nombre "Pikachu" en la pokedex
    public Optional<Pokemon> obtenerPikachu(){
        Optional<Pokemon> pikachu = pokedex.pokemon.stream()
                .filter(pokemon -> pokemon.getName().equalsIgnoreCase("Pikachu"))
                .findFirst();

        if(pikachu.isEmpty()){
            return Optional.of(new Pokemon());
        }
        return pikachu;
    }
    
    //Resto de codigo
}
```

### Paso 7: Crear la clase ExportarCSV

Crear una clase `ExportarCSV` sin atributos, esta clase servirá para leer la lista en la Pokedex y escribir un archivo CSV con los datos de los pokemons almacenado en ella. Su metodo principal es el de `export()` y es el que realiza dicha exportacion desde el programa a un fichero `pokemon_exportado.csv` en la carpeta `data` del proyecto.

```java
package org.example.controllers;

import com.opencsv.CSVWriter;
import org.example.models.Pokemon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExportarCSV {
    
    // Exporta los datos de la Pokedex a un fichero csv llamado pokemon_exportado.csv
    public void export(){
        PokemonController pc = new PokemonController();
        List <Pokemon> pokemon = pc.pokedex.pokemon;

        Path relativePath = Paths.get("");
        String rutaRelativa = relativePath.toAbsolutePath().toString();
        String directorio = rutaRelativa + File.separator + "data";
        String csvFichero = directorio + File.separator + "pokemon_exportado.csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFichero))) {
            for (Pokemon p : pokemon) {
                String[] fila = {String.valueOf(p.getId()), p.getNum(), p.getName(), formatear(p.getHeight()), formatear(p.getWeight())};
                writer.writeNext(fila);
            }
            System.out.println("Los datos se han exportado correctamente a " + csvFichero);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Sirve para formatear los atributos height y weight quitando los espacio y letras.
    public String formatear(String s){
        return s.substring(0, s.indexOf(32));
    }
}
```

### Paso 8: Crear la clase LeerCSV

Crea una clase `LeerCSV` sin atributos, con un unico metodo `read()`, que nos permitira leer el archivo CSV anteriormente creado `pokemon_exportado.csv` y almacenara toda la informacion leida en una lista de tipo `Pokemon` y lo retornara.

```java
package org.example.controllers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.example.models.Pokemon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LeerCSV {
    
    // lee el archivo pokemon_exportado.csv y almacena la info en una lista del mismo tipo
    public ArrayList<Pokemon> read(){
        Path relativePath = Paths.get("");
        String rutaRelativa = relativePath.toAbsolutePath().toString();
        String directorio = rutaRelativa + File.separator + "data";
        String CSVFichero = directorio + File.separator + "pokemon_exportado.csv";
        ArrayList<Pokemon> lista = new ArrayList<Pokemon>();
        try (CSVReader reader = new CSVReader(new FileReader(CSVFichero))) {
            String[] fila;
            while ((fila = reader.readNext()) != null) {
                lista.add(new Pokemon(
                        Integer.parseInt(fila[0]), fila[1], fila[2], fila[3], fila[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}
```
### Paso 9: Crear la clase DatabaseManager

Crea la clase `DatabaseManager` que maneja la conexión a la base de datos SQLite y la inicialización de las tablas. Esta clase es un singleton y tiene un método `getInstance` para obtener la instancia. También tiene un método `executeScript` que ejecuta un script SQL desde un archivo.

```java
package org.example.services;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseManager implements AutoCloseable {
    private static DatabaseManager instance;
    private final boolean initTables = false;
    private String init;

    private final String url = "jdbc:sqlite:Pokemon.db";
    private Connection conn;
    
    private DatabaseManager() {
        try {
            openConnection();
            leerConfig();
            if (initTables) {
                initTables();
            }
            executeScript(init , true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private void leerConfig(){
        try{
            Properties prop = new Properties();
            prop.load(DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties"));
            init = prop.getProperty("database.initScript", "init.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void openConnection() throws SQLException {
        conn = DriverManager.getConnection(url);
    }
    
    private void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    
    private void initTables() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Pokemon (id INTEGER PRIMARY KEY, num TEXT, name TEXT, height TEXT, weight TEXT)");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
    
    public void executeScript(String scriptSqlFile, boolean logWriter) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResourceAsStream(scriptSqlFile);
        InputStreamReader reader = new InputStreamReader(file);
        sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
        sr.runScript(reader);
    }
    
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                openConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
    
    @Override
    public void close() throws SQLException {
        closeConnection();
    }
}
   ```
## Ejecucion
***

### Paso 1: Iniciar la Base de Datos

En el método main, obtén la instancia de `DatabaseManager` y usa el método executeScript para ejecutar un script SQL desde un archivo:
```java

public class Main {
    public static void main(String[] args) {
        // Obtener la instancia de DatabaseManager y la conexión
        DatabaseManager db = DatabaseManager.getInstance();
    }
}

```

## Repository
***
Crea una clase `PokemonRepositoryImp` que será singleton y que nos servira para poder realizar consultas `SQL` a la base de datos, en otras palabras, interactuar con ella. Entre los metodos que tenemos: `save()` que guarda un `Pokemon` pasado por parametro en la base de datos y `findByName()` que devuelve una lista de `Pokemon` los cuales su nombre coincidan con el nombre pasado por parametro.  
```java
package org.example.repositories;

import org.example.models.Pokemon;
import org.example.services.DatabaseManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PokemonRepositoryImpl implements PokemonRepository {
    private static PokemonRepositoryImpl instance;
    private final DatabaseManager db;
    
    private PokemonRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }
    
    public static PokemonRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new PokemonRepositoryImpl(db);
        }
        return instance;
    }
    
    // Devuelve todos los pokemons de la base de datos que tengan el mismo nombre que el nombre pasado por parametros
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
    
    // Guardar el pokemon pasado por parametros en la tabla Pokemon en la base de datos
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
```

y luego en tu main o donde quieras ejecuta tanto las consultas del `PokemonController` como las `consultas SQL` a la base de datos: 

```java
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
```