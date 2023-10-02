package org.example.services;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Clase que gestiona la base de datos
 */
public class DatabaseManager implements AutoCloseable {
    private static DatabaseManager instance;
    private final boolean initTables = false;
    private String init;

    private final String url = "jdbc:sqlite:Pokemon.db";
    private Connection conn;


    /**
     * Constructor del DatabaseManager
     */
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

    /**
     * Lee la configuracion desde el archivo "config.properties" y asigna el valor de
     * la propiedad "database.initScript" a la variable "init". Si la propiedad no
     * esta presente en el archivo de configuracion, se asigna el valor predeterminado
     * "init.sql" a la variable "init".
     *
     * @throws IOException Si ocurre un error al leer el archivo de configuración.
     */
    private void leerConfig(){
        try{
            Properties prop = new Properties();
            prop.load(DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties"));
            init = prop.getProperty("database.initScript", "init.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para obtener la instancia de la base de datos
     *
     * @return La base de datos
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Metodo para abrir la conexion con la base de datos
     */
    private void openConnection() throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    /**
     * Metodo para cerrar la conexion con la base de datos
     */
    private void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }


    /**
     * Método para inicializar la base de datos y las tablas
     * Esto puede ser muy complejo y mejor usar un script, ademas podemos usar datos de ejemplo en el script
     */
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

    /**
     * Metodo para ejecutar un script de SQL
     *
     * @param scriptSqlFile nombre del fichero de script SQL
     * @param logWriter     si queremos que nos muestre el log de la ejecucion
     * @throws FileNotFoundException excepcion que arroja si no existe el archivo
     */
    public void executeScript(String scriptSqlFile, boolean logWriter) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResourceAsStream(scriptSqlFile);
        InputStreamReader reader = new InputStreamReader(file);
        sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
        sr.runScript(reader);
    }

    /**
     * Metodo para obtener la conexion con la base de datos
     *
     * @return Devuelve la conexion con la base de datos
     */
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

    /**
     * Metodo para cerrar la conexion con la base de datos
     */
    @Override
    public void close() throws SQLException {
        closeConnection();
    }
}