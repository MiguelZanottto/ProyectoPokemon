package org.example.models;

import lombok.Data;

/**
 * Esta clase PrevEvolution representa la informacion sobre la evolucion previa de un Pokemon.
 * Contiene el numero de la Preevolucion en la Pokedex y su nombre.
 */
@Data
public class PrevEvolution {
	private String num;
	private String name;

	/**
	 * Devuelve una representacion en cadena de los datos de la evolucion previa.
	 *
	 * @return Una cadena que contiene el numero de la evolucion previa y su nombre en cadena de texto.
	 */
	@Override
	public String toString() {
		return "PrevEvolution{" +
				"num='" + num + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
