package pokemon;

import java.util.Arrays;
import java.util.LinkedList;

public class Pokemon {

	// Constantes para el nivel, y fertilidad por defecto.
	private final int NIVELDEF = 1;
	private final int FERTILIDADDEF = 5;

	private int idPokemon;
	private int idEntrenador;
	private Pokedex numPokedex;
	private Objeto objetoEquipado; // Objeto que el pokemon puede o no llevar consigo
	private String mote;
	private int vitalidad, ataque, defensa, velocidad, estamina, exp, nivel, fertilidad, ataqueEspecial,
			defensaEspecial;
	private Movimiento[] movimiento = new Movimiento[4]; // Array para la lista de movimientos que tiene el pokemon.
	private LinkedList<TipoPokemon> tipoPokemon; // Creamos una lista para guardar los tipos pokemons
	private String sexo;
	private Estado estadoActual; // Usamos composición para poder asignar los efectos de cada estado específico a
									// nuestro pokemon.
	private String ubicacion;

	// Constructor por defecto
	public Pokemon() {
		super();
		this.idPokemon = 0;
		this.idEntrenador = 0;
		this.numPokedex = new Pokedex();
		this.objetoEquipado = new Objeto();
		this.mote = "";
		this.vitalidad = generarStatAleatoria();
		this.ataque = generarStatAleatoria();
		this.defensa = generarStatAleatoria();
		this.velocidad = generarStatAleatoria();
		this.estamina = 100;
		this.exp = 0;
		this.nivel = NIVELDEF;
		this.fertilidad = FERTILIDADDEF;
		this.ataqueEspecial = generarStatAleatoria();
		this.defensaEspecial = generarStatAleatoria();
		this.movimiento = new Movimiento[4];
		this.tipoPokemon = new LinkedList<TipoPokemon>();
		this.sexo = generarSexoAleatorio();
		this.estadoActual = new Estado();
		this.ubicacion = "";
	}

	// Constructor con parámetros
	public Pokemon(int idPokemon, int idEntrenador, Pokedex numPokedex, Objeto objetoEquipado, String mote,
			int vitalidad, int ataque, int defensa, int velocidad, int estamina, int exp, int nivel, int fertilidad,
			int ataqueEspecial, int defensaEspecial, Movimiento[] movimiento, LinkedList<TipoPokemon> tipoPokemon,
			String sexo, Estado estadoActual, String ubicacion) {
		super();
		this.idPokemon = idPokemon;
		this.idEntrenador = idEntrenador;
		this.numPokedex = numPokedex;
		this.objetoEquipado = objetoEquipado;
		this.mote = mote;
		this.vitalidad = vitalidad;
		this.ataque = ataque;
		this.defensa = defensa;
		this.velocidad = velocidad;
		this.estamina = estamina;
		this.exp = exp;
		this.nivel = nivel;
		this.fertilidad = fertilidad;
		this.ataqueEspecial = ataqueEspecial;
		this.defensaEspecial = defensaEspecial;
		this.movimiento = movimiento;
		this.tipoPokemon = tipoPokemon;
		this.sexo = sexo;
		this.estadoActual = estadoActual;
		this.ubicacion = ubicacion;
	}

	// Constructor copia
	public Pokemon(Pokemon p) {
		super();
		this.idPokemon = p.idPokemon;
		this.idEntrenador = p.idEntrenador;
		this.numPokedex = p.numPokedex;
		this.objetoEquipado = p.objetoEquipado;
		this.mote = p.mote;
		this.vitalidad = p.vitalidad;
		this.ataque = p.ataque;
		this.defensa = p.defensa;
		this.velocidad = p.velocidad;
		this.estamina = p.estamina;
		this.exp = p.exp;
		this.nivel = p.nivel;
		this.fertilidad = p.fertilidad;
		this.ataqueEspecial = p.ataqueEspecial;
		this.defensaEspecial = p.defensaEspecial;
		// Realizamos una copia profunda en la variable movimiento para que si
		// modificamos el array, los movimientos de un pokemon no se modificquen en los
		// otros.
		if (p.movimiento != null) {
			this.movimiento = p.movimiento.clone(); // Creamos un nuevo array con los mismos datos con clone. Lo
													// clonamos para evitar errores.
		} else {
			this.movimiento = new Movimiento[4];
		}
		if (p.tipoPokemon != null) {
			this.tipoPokemon = new LinkedList<>(p.tipoPokemon); // Aqui usamos la misma lógica, creamos una nueva lista
																// con los datos que ya teníamos para evitar que las
																// modificaciones alteren los demás pokemons
		} else {
			this.tipoPokemon = new LinkedList<>();
		}
		this.sexo = p.sexo;
		this.estadoActual = p.estadoActual;
		this.ubicacion = p.ubicacion;
	}
	
	//Creamos un constructor Nacimiento, en el que solamente se pidan los valores que no se van a conocer cuando el pokemon nace.
	public Pokemon(Pokedex numPokedex, String mote) {
		super();
		this.idPokemon = 0;
		this.idEntrenador = 0;
		this.numPokedex = numPokedex;
		this.objetoEquipado = new Objeto();
		this.mote = mote;
		this.vitalidad = generarStatAleatoria();
		this.ataque = generarStatAleatoria();
		this.defensa = generarStatAleatoria();
		this.velocidad = generarStatAleatoria();
		this.estamina = 100;
		this.exp = 0;
		this.nivel = NIVELDEF;
		this.fertilidad = FERTILIDADDEF;
		this.ataqueEspecial = generarStatAleatoria();
		this.defensaEspecial = generarStatAleatoria();
		this.movimiento = new Movimiento[4];
		this.tipoPokemon = new LinkedList<TipoPokemon>();
		this.sexo = generarSexoAleatorio();
		this.estadoActual = new Estado();
		this.ubicacion = "";
	}

	// Creamos un método de clase para generar las stats aleatorias cuando el
	// pokemon nace. Las generamos por defecto
	private static int generarStatAleatoria() {
		return (int) (Math.random() * 10) + 1;
	}
	
	//Creamos método para la aleatoriedad del sexo.
	private static String generarSexoAleatorio() {
		//Operador terniario
		//Condición: Numero que genera Math.random es menor que 0.5? Si es verdadero es macho, si es falso es hembra.
		return (Math.random() < 0.5) ? "MACHO" : "HEMBRA";
	}

	//Método toString
	@Override
	public String toString() {
		return "Pokemon [NIVELDEF=" + NIVELDEF + ", FERTILIDADDEF=" + FERTILIDADDEF + ", idPokemon=" + idPokemon
				+ ", idEntrenador=" + idEntrenador + ", numPokedex=" + numPokedex + ", objetoEquipado=" + objetoEquipado
				+ ", mote=" + mote + ", vitalidad=" + vitalidad + ", ataque=" + ataque + ", defensa=" + defensa
				+ ", velocidad=" + velocidad + ", estamina=" + estamina + ", exp=" + exp + ", nivel=" + nivel
				+ ", fertilidad=" + fertilidad + ", ataqueEspecial=" + ataqueEspecial + ", defensaEspecial="
				+ defensaEspecial + ", movimiento=" + Arrays.toString(movimiento) + ", tipoPokemon=" + tipoPokemon
				+ ", sexo=" + sexo + ", estadoActual=" + estadoActual + ", ubicacion=" + ubicacion + "]";
	}
	
	
}
