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
	private int turnosEstado;
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
		this.estadoActual = Estado.SALUDABLE;
		this.turnosEstado = 0;
		this.ubicacion = "";
	}

	// Constructor con parámetros
	public Pokemon(int idPokemon, int idEntrenador, Pokedex numPokedex, Objeto objetoEquipado, String mote,
			int vitalidad, int ataque, int defensa, int velocidad, int estamina, int exp, int nivel, int fertilidad,
			int ataqueEspecial, int defensaEspecial, Movimiento[] movimiento, LinkedList<TipoPokemon> tipoPokemon,
			String sexo, Estado estadoActual, String ubicacion, int turnosEstado) {
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
		this.turnosEstado = turnosEstado;
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
		this.turnosEstado = p.turnosEstado;
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
		this.estadoActual = Estado.SALUDABLE;
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

	public int getTurnosEstado() {
		return turnosEstado;
	}

	public void setTurnosEstado(int turnosEstado) {
		this.turnosEstado = turnosEstado;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	// Método para obtener el ataque total con el objeto aplicado al pokemon.
	public int ataqueObjetoAplicado() {
		double modificador = 1.0;
		// Si tiene un objeto leemos su modificador
		if (this.objetoEquipado != null) {
			modificador = this.objetoEquipado.getModAtaque();
		}
		// Devolvemos el ataque base * el modificador
		return (int) (this.ataque * modificador);
	}

	// Método para obtener la defensa total con el objeto aplicado al pokemon.
	public int defensaObjetoAplicado() {
		double modificador = 1.0;
		// Si tiene un objeto leemos su modificador
		if (this.objetoEquipado != null) {
			modificador = this.objetoEquipado.getModDefensa();
		}
		// Devolvemos el ataque base * el modificador
		return (int) (this.defensa * modificador);
	}

	// Método para obtener la velocidad total con el objeto aplicado al pokemon.
	public int velocidadaObjetoAplicado() {
		double modificador = 1.0;
		// Si tiene un objeto leemos su modificador
		if (this.objetoEquipado != null) {
			modificador = this.objetoEquipado.getModVelocidad();
		}
		// Devolvemos el ataque base * el modificador
		return (int) (this.velocidad * modificador);
	}

	// Método para obtener la defensa especial total con el objeto aplicado al
	// pokemon.
	public int defEspecialObjetoAplicado() {
		double modificador = 1.0;
		// Si tiene un objeto leemos su modificador
		if (this.objetoEquipado != null) {
			modificador = this.objetoEquipado.getModDefEsp();
		}
		// Devolvemos el ataque base * el modificador
		return (int) (this.defensaEspecial * modificador);
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
		return (Math.random() < 0.5) ? "M" : "H";
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

	// Creamos método para concebir a un pokemon.
	public static Pokemon engendrarHijo(Pokemon padre, Pokemon madre) {
		// Hereda la especie de la madre (regla clásica de Pokémon)
		// Usamos el constructor de copia o nacimiento
		Pokemon hijo = new Pokemon(madre.getNumPokedex(), "Huevo de " + madre.getMote());

		// Hacemos que herede la media de los STATS de padre y madre
		// Vitalidad
		hijo.setVitalidad((padre.getVitalidad() + madre.getVitalidad()) / 2);
		// Ataque
		hijo.setAtaque((padre.getAtaque() + madre.getAtaque()) / 2);
		// Defensa
		hijo.setDefensa((padre.getDefensa() + madre.getDefensa()) / 2);
		// Velocidad
		hijo.setVelocidad((padre.getVelocidad() + madre.getVelocidad()) / 2);
		// Ataque Especial
		hijo.setAtaqueEspecial((padre.getAtaqueEspecial() + madre.getAtaqueEspecial()) / 2);
		// Defensa Especial
		hijo.setDefensaEspecial((padre.getDefensaEspecial() + madre.getDefensaEspecial()) / 2);

		// Nivel al que nace el hijo
		hijo.setNivel(1);
		hijo.setEstamina(100); // Nace descansado
		hijo.setExp(0); // Nace sin exp.
		hijo.setFertilidad(5); // Nacse con el máximo de fertilidad

		return hijo;
	}

	// Creamos el método atacar a otro pokemon
	// Le pasamos como argumentos un pokemon al que atacar y un movimiento.
	public void atacar(Pokemon pokemonEnemigo, Movimiento movElegido) {

		int coste = movElegido.calcularCosteEstamina();

		if (this.estamina >= coste) {
			this.estamina -= coste;

			// Ajuste por estado quemado
			int ataqueReal = this.ataqueObjetoAplicado();
			if (this.estadoActual == Estado.QUEMADO && movElegido.getClaseMovimiento().equals("ataque")) {
				ataqueReal = ataqueReal / 2;
			}

			// Calculamos la efectividad de tipos primero
			TipoPokemon tipoAtaque = movElegido.getTipoPokemon();
			TipoPokemon tipoDefensor1 = pokemonEnemigo.getTipoPokemon().get(0);
			TipoPokemon tipoDefensor2 = (pokemonEnemigo.getTipoPokemon().size() > 1)
					? pokemonEnemigo.getTipoPokemon().get(1)
					: null;

			double efectividad = tipoAtaque.calculoDaño(tipoDefensor1, tipoDefensor2);

			// Comprobar inmunidad total (x0) según tabla de efectividades
			if (efectividad == 0.0) {
				System.out.println("¡No afecta a " + pokemonEnemigo.getMote() + "!");
				return;
			}

			// 3. Lógica de daño para movimientos de clase "ataque"
			if (movElegido.getClaseMovimiento().equalsIgnoreCase("ataque")) {
				// Usamos la fórmula mas estandar: (Ataque + Potencia) / (DefensaRival / 2)
				double factorDefensa = pokemonEnemigo.defensaObjetoAplicado() / 10.0;
				if (factorDefensa < 1.0)
					factorDefensa = 1.0;

				int danioBase = (int) ((ataqueReal + movElegido.getPotencia()) / factorDefensa);

				int danioFinal = (int) (danioBase * efectividad);

				// Daño mínimo garantizado para que el combate fluya [cite: 1, 130]
				if (danioFinal < 10) {
					danioFinal = 10 + (this.nivel * 2);
				}

				pokemonEnemigo.setVitalidad(pokemonEnemigo.getVitalidad() - danioFinal);

				// Mensajes de efectividad
				if (efectividad >= 2.0) {
					System.out.println("¡Es súper eficaz! (" + danioFinal + " PS)");
				} else if (efectividad < 1.0) {
					System.out.println("No es muy eficaz... (" + danioFinal + " PS)");
				} else {
					System.out.println("¡Golpe directo! (" + danioFinal + " PS)");
				}
			}

			// Lógica de estados e inmunidades
			if (movElegido.getClaseMovimiento().equalsIgnoreCase("estado") && movElegido.getEstado() != null) {
				if (pokemonEnemigo.getEstadoActual() == Estado.SALUDABLE) {
					boolean esInmune = false;
					for (TipoPokemon tipo : pokemonEnemigo.getTipoPokemon()) {
						if (movElegido.getEstado() == Estado.QUEMADO && tipo == TipoPokemon.FUEGO)
							esInmune = true;
						if (movElegido.getEstado() == Estado.CONGELADO && tipo == TipoPokemon.HIELO)
							esInmune = true;
						if (movElegido.getEstado() == Estado.HELADO && tipo == TipoPokemon.HIELO)
							esInmune = true;
						if (movElegido.getEstado() == Estado.DRENADORAS && tipo == TipoPokemon.PLANTA)
							esInmune = true;
						if (movElegido.getEstado() == Estado.PARALIZADO && tipo == TipoPokemon.ELECTRICO)
							esInmune = true;
						if ((movElegido.getEstado() == Estado.ENVENENADO
								|| movElegido.getEstado() == Estado.GRAVEMENTE_ENVENENADO)
								&& tipo == TipoPokemon.VENENO)
							esInmune = true;
					}

					if (!esInmune) {
						pokemonEnemigo.setEstadoActual(movElegido.getEstado());
						System.out.println(
								"¡" + pokemonEnemigo.getMote() + " ahora está " + movElegido.getEstado() + "!");
						if (movElegido.getEstado() == Estado.DORMIDO) {
							pokemonEnemigo.setTurnosEstado((int) (Math.random() * 3) + 1);
						}
					} else {
						System.out.println("¡No afectó a " + pokemonEnemigo.getMote() + " por su tipo!");
					}
				}
			}
		} else {
			this.descansar();
			System.out.println(this.mote + " está demasiado cansado y descansa.");
		}
	}

	// Creamos el método descansar para recuperar la estamina perdida que gastamos
	// en combate o haceindo algún movimiento
	public void descansar() {
		double modRecup = 1.0;
		// Calculamos la recuperación con objeto
		if (this.objetoEquipado != null) {
			modRecup = this.objetoEquipado.getModRecupEstamina();
		}
		int cantRecuperada = (int) (20 * modRecup);
		this.estamina += cantRecuperada;

		// Cálculo para halla el tope máximo de recuperación
		double modMax = 1.0;
		if (this.objetoEquipado != null) {
			modMax = this.objetoEquipado.getModEstamina();
		}
		// El tope ya no es 100 fijo, es 100 * modificador.
		int topeMax = (int) (100 * modMax);
		if (this.estamina > topeMax) { // Ajustamos el tope si nos pasamos de él.
			this.estamina = topeMax;
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

	// Método que sabe qué bebé sale de cada evolución
	public static int obtenerIdBebe(int idMadre) {
		return switch (idMadre) {
		// Iniciales
		case 2, 3 -> 1; // Ivysaur, Venusaur -> Bulbasaur
		case 5, 6 -> 4; // Charmeleon, Charizard -> Charmander
		case 8, 9 -> 7; // Wartortle, Blastoise -> Squirtle

		// Bichos de bosque
		case 11, 12 -> 10; // Metapod, Butterfree -> Caterpie
		case 14, 15 -> 13; // Kakuna, Beedrill -> Weedle

		// Pájaros y ratones
		case 17, 18 -> 16; // Pidgeotto, Pidgeot -> Pidgey
		case 20 -> 19; // Raticate -> Rattata
		case 22 -> 21; // Fearow -> Spearow
		case 24 -> 23; // Arbok -> Ekans
		case 26 -> 25; // Raichu -> Pikachu
		case 28 -> 27; // Sandslash -> Sandshrew

		// Nidoran y Montaña
		case 30, 31 -> 29; // Nidorina, Nidoqueen -> Nidoran ♀
		case 33, 34 -> 32; // Nidorino, Nidoking -> Nidoran ♂
		case 36 -> 35; // Clefable -> Clefairy
		case 38 -> 37; // Ninetales -> Vulpix
		case 40 -> 39; // Wigglytuff -> Jigglypuff
		case 42 -> 41; // Golbat -> Zubat

		// Plantas y setas
		case 44, 45 -> 43; // Gloom, Vileplume -> Oddish
		case 47 -> 46; // Parasect -> Paras
		case 49 -> 48; // Venomoth -> Venonat

		// Cuevas y ciudad
		case 51 -> 50; // Dugtrio -> Diglett
		case 53 -> 52; // Persian -> Meowth
		case 55 -> 54; // Golduck -> Psyduck
		case 57 -> 56; // Primeape -> Mankey
		case 59 -> 58; // Arcanine -> Growlithe

		// Agua y psíquicos
		case 61, 62 -> 60; // Poliwhirl, Poliwrath -> Poliwag
		case 64, 65 -> 63; // Kadabra, Alakazam -> Abra
		case 67, 68 -> 66; // Machoke, Machamp -> Machop
		case 70, 71 -> 69; // Weepinbell, Victreebel -> Bellsprout
		case 73 -> 72; // Tentacruel -> Tentacool

		// Rocas y fuego
		case 75, 76 -> 74; // Graveler, Golem -> Geodude
		case 78 -> 77; // Rapidash -> Ponyta
		case 80 -> 79; // Slowbro -> Slowpoke
		case 82 -> 81; // Magneton -> Magnemite

		// Varios
		case 85 -> 84; // Dodrio -> Doduo
		case 87 -> 86; // Dewgong -> Seel
		case 89 -> 88; // Muk -> Grimer
		case 91 -> 90; // Cloyster -> Shellder
		case 93, 94 -> 92; // Haunter, Gengar -> Gastly

		// Drowzee a Rhyhorn
		case 97 -> 96; // Hypno -> Drowzee
		case 99 -> 98; // Kingler -> Krabby
		case 101 -> 100; // Electrode -> Voltorb
		case 103 -> 102; // Exeggutor -> Exeggcute
		case 105 -> 104; // Marowak -> Cubone
		case 110 -> 109; // Weezing -> Koffing
		case 112 -> 111; // Rhydon -> Rhyhorn

		// Agua late-game
		case 117 -> 116; // Seadra -> Horsea
		case 119 -> 118; // Seaking -> Goldeen
		case 121 -> 120; // Starmie -> Staryu
		case 130 -> 129; // Gyarados -> Magikarp

		// Eevee y Fósiles
		case 134, 135, 136 -> 133; // Vaporeon, Jolteon, Flareon -> Eevee
		case 139 -> 138; // Omastar -> Omanyte
		case 141 -> 140; // Kabutops -> Kabuto
		case 148, 149 -> 147; // Dragonair, Dragonite -> Dratini

		// Pokémon base, legendarios o sin evolución
		default -> idMadre; // Devuelven su propio ID
		};
	}

	// Método toString
	@Override
	public String toString() {
		return "Pokemon [idPokemon=" + idPokemon + ", idEntrenador=" + idEntrenador + ", numPokedex=" + numPokedex
				+ ", objetoEquipado=" + objetoEquipado + ", mote=" + mote + ", vitalidad=" + vitalidad + ", ataque="
				+ ataque + ", defensa=" + defensa + ", velocidad=" + velocidad + ", estamina=" + estamina + ", exp="
				+ exp + ", nivel=" + nivel + ", fertilidad=" + fertilidad + ", ataqueEspecial=" + ataqueEspecial
				+ ", defensaEspecial=" + defensaEspecial + ", movimiento=" + Arrays.toString(movimiento)
				+ ", tipoPokemon=" + tipoPokemon + ", sexo=" + sexo + ", estadoActual=" + estadoActual + ", ubicacion="
				+ ubicacion + "]";
	}
}
