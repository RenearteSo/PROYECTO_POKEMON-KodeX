package model;

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

	// Creamos un constructor Nacimiento, en el que solamente se pidan los valores
	// que no se van a conocer cuando el pokemon nace.
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

	// Getters and setters
	public int getIdPokemon() {
		return idPokemon;
	}

	public void setIdPokemon(int idPokemon) {
		this.idPokemon = idPokemon;
	}

	public int getIdEntrenador() {
		return idEntrenador;
	}

	public void setIdEntrenador(int idEntrenador) {
		this.idEntrenador = idEntrenador;
	}

	public Pokedex getNumPokedex() {
		return numPokedex;
	}

	public void setNumPokedex(Pokedex numPokedex) {
		this.numPokedex = numPokedex;
	}

	public Objeto getObjetoEquipado() {
		return objetoEquipado;
	}

	public void setObjetoEquipado(Objeto objetoEquipado) {
		this.objetoEquipado = objetoEquipado;
	}

	public String getMote() {
		return mote;
	}

	public void setMote(String mote) {
		this.mote = mote;
	}

	public int getVitalidad() {
		return vitalidad;
	}

	public void setVitalidad(int vitalidad) {
		this.vitalidad = vitalidad;
		if (this.vitalidad < 0) {
			this.vitalidad = 0; // Condición para que la vida no baje a negativo
		}
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefensa() {
		return defensa;
	}

	public void setDefensa(int defensa) {
		this.defensa = defensa;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getEstamina() {
		return estamina;
	}

	public void setEstamina(int estamina) {
		this.estamina = estamina;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getFertilidad() {
		return fertilidad;
	}

	public void setFertilidad(int fertilidad) {
		this.fertilidad = fertilidad;
	}

	public int getAtaqueEspecial() {
		return ataqueEspecial;
	}

	public void setAtaqueEspecial(int ataqueEspecial) {
		this.ataqueEspecial = ataqueEspecial;
	}

	public int getDefensaEspecial() {
		return defensaEspecial;
	}

	public void setDefensaEspecial(int defensaEspecial) {
		this.defensaEspecial = defensaEspecial;
	}

	public Movimiento[] getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(Movimiento[] movimiento) {
		this.movimiento = movimiento;
	}

	public LinkedList<TipoPokemon> getTipoPokemon() {
		return tipoPokemon;
	}

	public void setTipoPokemon(LinkedList<TipoPokemon> tipoPokemon) {
		this.tipoPokemon = tipoPokemon;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Estado getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(Estado estadoActual) {
		this.estadoActual = estadoActual;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	// Creamos un método de clase para generar las stats aleatorias cuando el
	// pokemon nace. Las generamos por defecto
	private static int generarStatAleatoria() {
		return (int) (Math.random() * 10) + 1;
	}

	// Creamos método para la aleatoriedad del sexo.
	private static String generarSexoAleatorio() {
		// Operador terniario
		// Condición: Numero que genera Math.random es menor que 0.5? Si es verdadero es
		// macho, si es falso es hembra.
		return (Math.random() < 0.5) ? "MACHO" : "HEMBRA";
	}

	// Método para la subida de nivel del pokemon
	public boolean subirNivel() {
		boolean subidaNivel = false;
		// Usamos un bucle While para los caso en los que se gane mucha experiencia.
		while (this.exp >= this.nivel * 10) {
			this.exp -= (this.nivel * 10); // Gastamos la experiencia que teníamos
			this.nivel++;
			subidaNivel = true;

			// Al subir de nivel se mejorará de 1 a 5 aleatoriamente cada estadística de
			// nuestro pokemon.
			this.vitalidad += (int) (Math.random() * (5 - 1 + 1) + 1);
			this.ataque += (int) (Math.random() * (5 - 1 + 1) + 1);
			this.defensa += (int) (Math.random() * (5 - 1 + 1) + 1);
			this.ataqueEspecial += (int) (Math.random() * (5 - 1 + 1) + 1);
			this.defensaEspecial += (int) (Math.random() * (5 - 1 + 1) + 1);
			this.velocidad += (int) (Math.random() * (5 - 1 + 1) + 1);

			// Creamos la condición para que cada 3 niveles nuestro pokemon pueda aprender
			// un nuevo movimiento!
			if (this.nivel % 3 == 0) {
				System.out.println("¡El pokemon puede aprender un nuevo movimiento!");
			}
		}

		return subidaNivel;
	}

	// Creamos el método atacar a otro pokemon
	// Le pasamos como argumentos un pokemon al que atacar y un movimiento.
	public void atacar(Pokemon pokemonEnemigo, Movimiento movElegido) {
		int coste = movElegido.calcularCosteEstamina(); // Llamamos al método gestCosteEstamina de la calse movimiento
		if (this.estamina >= coste) {
			this.estamina -= coste; // Restamos el coste de la estamina del movimiento a nuestras estamina actual.

			// Generamos la lógica del daño y se la aplicamos al pokemon rival restánbdole
			// su defensa
			int danio = (this.ataque + movElegido.getPotencia()) - pokemonEnemigo.getDefensa();
			if (danio < 0) {
				danio = 0; // Condición para evitar daños negativos
			}

			// Metemos en el método atacar la comprobación de ventaja y desventaja que es
			// dependiente de la clase TipoPokemon
			double efectividad = 1.0; // Es la efectividad estandar

			// Obtenemos el Tipo del Movimiento que se usa.
			TipoPokemon tipoAtaque = movElegido.getTipoPokemon();

			// Obtenemos los tipos del enemigo. Puede ser de 1 o 2 tipos. Usamos la Linked
			// list para guardarlos y recuperarlos de ahí.
			TipoPokemon tipoDefensor1 = pokemonEnemigo.getTipoPokemon().get(0);
			TipoPokemon tipoDefensor2 = null;
			

			// Si el enemigo tiene un segundo tipo, lo cogemos también.
			if (pokemonEnemigo.getTipoPokemon().size() > 1) {
				tipoDefensor2 = pokemonEnemigo.getTipoPokemon().get(1);
			}
			
			efectividad = tipoAtaque.calculoDaño(tipoDefensor1, tipoDefensor2);

			int danioFinal = 0;
			danioFinal = (int) (danio * efectividad);// fórmula de daño para conocer el daño final aplicando la
														// EFECTIVIDAD
														// de la VENTAJA o DESVENTAJA

			pokemonEnemigo.setVitalidad(pokemonEnemigo.getVitalidad() - danioFinal); // actualizamos la vida del Pokemon
			// enemigo
		} else {
			this.descansar();
		}
	}

	// Creamos el método descansar para recuperar la estamina perdida que gastamos
	// en combate o haceindo algún movimiento
	public void descansar() {
		// Cuando el pokemon descansa recupera 20 puntos de estamina.
		this.estamina += 20;
		// Condición para que la estamina no supere los 100 puntos
		if (this.estamina > 100) {
			this.estamina = 100;
		}
	}

	// Método aprender nuevo movimiento.
	public Movimiento aprenderMovimiento(Movimiento nuevoMov, int indice) {
		// Condición para evitar que se introduzca un índice erróneo en nuestro array
		if (indice < 0 || indice >= this.movimiento.length) {
			return null;
		}
		// Reemplazamos el movimiento que queremos olvidar y lo guardamos en una
		// variable para luego en el MAIN guardarlo en una COLECCIÓN.
		Movimiento movOlvidado = this.movimiento[indice];
		// Sobreescribimos el nuevo movimiento encima y lo guardamos en nuestro array de
		// movimientos actuales del pokemon
		this.movimiento[indice] = nuevoMov;

		// Devolvemos el viejo para que luego el MAIN lo gestione.
		return movOlvidado;
	}

	// Método toString
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
