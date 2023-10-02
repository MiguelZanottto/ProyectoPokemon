package org.example.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *  Esta clase NextEvolution representa la siguiente evolucion de un determinado Pokemon
 *   En ella se Almacena el numero del Pokemon en la Pokedex y el nombre de su siguiente evolucion.
 */
@Data
public class NextEvolution{
	private String num;
	private String name;

	/**
	 * Constructor de NextEvolution con parametros
	 * @param num Numero de la Evolucion en la Pokedex
	 * @param name Nombre de la Evolucion en la Pokedex
	 */
	public NextEvolution(String num, String name) {
		this.num = this.num;
		this.name = this.name;
	}

	/**
	 * Constructor sin parametros
	 */
	public NextEvolution() {

	}


	/**
     * Devuelve el numero de la Evolucion en la Pokedex
     * @return Numero de la Evolucion en la Pokedex
     */
	public String getNum() {
		return num;
	}

	/**
	 * Modifica el numero de la Evolucion en la Pokedex
	 * @param num Numero de la Evolucion en la Pokedex
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
     * Devuelve el nombre de la Evolucion en la Pokedex
     * @return Nombre de la Evolucion en la Pokedex
     */
	public String getName() {
		return name;
	}

	/**
     * Modifica el nombre de la Evolucion en la Pokedex
     * @param name Nombre de la Evolucion en la Pokedex
     */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Devuelve los atributos del NextEvolution con su valor representado en cadena de texto
	 * @return Cadena que contiene la informacion del NextEvolution
	 */
	@Override
	public String toString() {
		return "NextEvolution{" +
				"num='" + num + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
