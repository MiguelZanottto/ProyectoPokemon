package org.example.controllers;

import com.opencsv.CSVWriter;
import org.example.models.Pokemon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * La clase ExportarCSV sirve para exportar datos de una lista de Pokemon del sistema a un archivo CSV.
 * Utiliza la informacion de Pokemon proporcionada por un controlador de Pokemon.
 */
public class ExportarCSV {

    /**
     * Exporta los datos de una lista de Pokemon a un archivo CSV determinado.
     * Los datos se obtienen a traves de un controlador de Pokemon y se guardan en el archivo CSV de destino.
     */
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

    /**
     * Sirve para formatear los datos Peso y Altura de un Pokemon quitandole los espacios en blanco y las letras.
     * @param s La cadena que se quiere formatear
     * @return La cadena sin espacios en blanco
     */
    public String formatear(String s){
        return s.substring(0, s.indexOf(32));
    }
}
