package org.example.models;

import lombok.Data;
import lombok.Getter;

import java.util.List;
/**
 * La clase Pokedex representa una lista de Pokemon en una Pokedex.
 *
 * Contiene una lista de objetos Pokemon que almacenan informacion sobre Pokemon individuales.
 */
@Data
public class Pokedex {
	public List<Pokemon> pokemon;
}