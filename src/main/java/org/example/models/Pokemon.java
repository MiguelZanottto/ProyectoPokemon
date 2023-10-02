package org.example.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;

/**
 *  Esta clase "Pokemon" sirve para representar a un Pokemon con sus atributos y caracteristicas.
 *  Se utiliza para almacenar la informacion de un Pokemon.
 */
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

	/**
	 * Constructor con Pokemon pasandole parametros esenciales que servira al momento de leer un CSV
	 * @param id Id del Pokemon
	 * @param num Numero del Pokemon en la Pokedex
	 * @param name Nombre del Pokemon
	 * @param height Altura del Pokemon
	 * @param weight Peso del Pokemon
	 */
	public Pokemon(int id, String num, String name, String height, String weight) {
		this.id = id;
		this.num = num;
		this.name = name;
		this.height = height;
		this.weight = weight;
	}

	/**
	 * Constructor un Pokemon sin pasarle parametros
	 */
	public Pokemon() {
	}


	/**
	 * Devuelve los atributos del Pokemon con su valor representado en cadena de texto
	 * @return Cadena que contiene la informacion del Pokemon
	 */
	@Override
	public String toString() {
		return "Pokemon{" +
				"img='" + img + '\'' +
				", egg='" + egg + '\'' +
				", candy='" + candy + '\'' +
				", num='" + num + '\'' +
				", weight='" + weight + '\'' +
				", type=" + type +
				", weaknesses=" + weaknesses +
				", name='" + name + '\'' +
				", avgSpawns=" + avgSpawns +
				", multipliers=" + multipliers +
				", id=" + id +
				", spawnTime='" + spawnTime + '\'' +
				", height='" + height + '\'' +
				", spawnChance=" + spawnChance +
				", prevEvolution=" + prevEvolution +
				", candyCount=" + candyCount +
				", nextEvolution=" + nextEvolution +
				'}';
	}
}

