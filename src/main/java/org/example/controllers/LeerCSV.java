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

/**
 * La clase LeerCSV se encarga de leer datos de un archivo CSV que contiene informacion de Pokemon.
 * Los datos se leen y se almacenan en una lista de objetos Pokemon.
 */
public class LeerCSV {

    /**
     * Se encarga de leer los datos de un archivo CSV que contiene informacion de Pokemon.
     * Los datos se leen y se almacen en una lista de Pokemon.
     * @return una lista de objetos Pokemon que han sido leidos del fichero.
     */
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