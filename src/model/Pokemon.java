package model;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Representa a un Pokémon individual dentro del juego.
 * Gestiona sus estadísticas, movimientos, estado de salud, 
 * experiencia, nivel y la lógica de combate y crianza.
 */
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
	private Estado estadoActual; // Usamos composición para poder asignar los efectos de cada estado específico a nuestro pokemon.
	private int turnosEstado;
	private String ubicacion;

	/**
	 * Constructor por defecto.
	 * Genera un Pokémon base con estadísticas aleatorias y valores iniciales por defecto.
	 */
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

	/**
	 * Constructor con parámetros completos.
	 * Utilizado principalmente para reconstruir un Pokémon extrayendo sus datos de la Base de Datos.
	 * * @param idPokemon El ID único del Pokémon en la partida.
	 * @param idEntrenador El ID del entrenador dueño.
	 * @param numPokedex Objeto Pokedex con los datos de su especie.
	 * @param objetoEquipado Objeto que lleva el Pokémon (si lo hay).
	 * @param mote El apodo dado por el entrenador.
	 * @param vitalidad Puntos de vida actuales.
	 * @param ataque Estadística de ataque físico.
	 * @param defensa Estadística de defensa física.
	 * @param velocidad Estadística de velocidad.
	 * @param estamina Energía actual para realizar movimientos.
	 * @param exp Experiencia actual acumulada.
	 * @param nivel Nivel de poder actual.
	 * @param fertilidad Número de veces que puede criar.
	 * @param ataqueEspecial Estadística de ataque especial.
	 * @param defensaEspecial Estadística de defensa especial.
	 * @param movimiento Array con los 4 movimientos que conoce.
	 * @param tipoPokemon Lista con los tipos elementales del Pokémon.
	 * @param sexo Género del Pokémon ("M" o "H").
	 * @param estadoActual Alteración de estado que sufre (ej: SALUDABLE, QUEMADO).
	 * @param ubicacion Lugar donde está guardado (EQUIPO o CAJA).
	 * @param turnosEstado Turnos restantes si sufre un estado temporal.
	 */
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

	/**
	 * Constructor copia.
	 * Crea un Pokémon exactamente igual a otro, realizando copias profundas de sus arrays y colecciones.
	 * * @param p El Pokémon que se desea clonar.
	 */
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
		
		// Realizamos una copia profunda en la variable movimiento para que si modificamos el array, 
		// los movimientos de un pokemon no se modifiquen en los otros.
		if (p.movimiento != null) {
			this.movimiento = p.movimiento.clone(); 
		} else {
			this.movimiento = new Movimiento[4];
		}
		if (p.tipoPokemon != null) {
			this.tipoPokemon = new LinkedList<>(p.tipoPokemon); 
		} else {
			this.tipoPokemon = new LinkedList<>();
		}
		this.sexo = p.sexo;
		this.estadoActual = p.estadoActual;
		this.turnosEstado = p.turnosEstado;
		this.ubicacion = p.ubicacion;
	}

	/**
	 * Constructor de Nacimiento / Captura.
	 * Pide solamente los valores esenciales, el resto (stats, sexo) se generan de forma aleatoria por defecto.
	 * * @param numPokedex Los datos de la especie extraídos de la Pokédex.
	 * @param mote El nombre que se le asigna al nacer o ser capturado.
	 */
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
	public int getIdPokemon() { return idPokemon; }
	public void setIdPokemon(int idPokemon) { this.idPokemon = idPokemon; }
	public int getIdEntrenador() { return idEntrenador; }
	public void setIdEntrenador(int idEntrenador) { this.idEntrenador = idEntrenador; }
	public Pokedex getNumPokedex() { return numPokedex; }
	public void setNumPokedex(Pokedex numPokedex) { this.numPokedex = numPokedex; }
	public Objeto getObjetoEquipado() { return objetoEquipado; }
	public void setObjetoEquipado(Objeto objetoEquipado) { this.objetoEquipado = objetoEquipado; }
	public String getMote() { return mote; }
	public void setMote(String mote) { this.mote = mote; }
	
	public int getVitalidad() { return vitalidad; }
	public void setVitalidad(int vitalidad) {
		this.vitalidad = vitalidad;
		if (this.vitalidad < 0) {
			this.vitalidad = 0; // Condición para que la vida no baje a negativo
		}
	}

	public int getAtaque() { return ataque; }
	public void setAtaque(int ataque) { this.ataque = ataque; }
	public int getDefensa() { return defensa; }
	public void setDefensa(int defensa) { this.defensa = defensa; }
	public int getVelocidad() { return velocidad; }
	public void setVelocidad(int velocidad) { this.velocidad = velocidad; }
	public int getEstamina() { return estamina; }
	public void setEstamina(int estamina) { this.estamina = estamina; }
	public int getExp() { return exp; }
	public void setExp(int exp) { this.exp = exp; }
	public int getNivel() { return nivel; }
	public void setNivel(int nivel) { this.nivel = nivel; }
	public int getFertilidad() { return fertilidad; }
	public void setFertilidad(int fertilidad) { this.fertilidad = fertilidad; }
	public int getAtaqueEspecial() { return ataqueEspecial; }
	public void setAtaqueEspecial(int ataqueEspecial) { this.ataqueEspecial = ataqueEspecial; }
	public int getDefensaEspecial() { return defensaEspecial; }
	public void setDefensaEspecial(int defensaEspecial) { this.defensaEspecial = defensaEspecial; }
	public Movimiento[] getMovimiento() { return movimiento; }
	public void setMovimiento(Movimiento[] movimiento) { this.movimiento = movimiento; }
	public LinkedList<TipoPokemon> getTipoPokemon() { return tipoPokemon; }
	public void setTipoPokemon(LinkedList<TipoPokemon> tipoPokemon) { this.tipoPokemon = tipoPokemon; }
	public String getSexo() { return sexo; }
	public void setSexo(String sexo) { this.sexo = sexo; }
	public Estado getEstadoActual() { return estadoActual; }
	public void setEstadoActual(Estado estadoActual) { this.estadoActual = estadoActual; }
	public String getUbicacion() { return ubicacion; }
	public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
	public int getTurnosEstado() { return turnosEstado; }
	public void setTurnosEstado(int turnosEstado) { this.turnosEstado = turnosEstado; }

	/**
	 * Método para obtener el ataque total con el plus del objeto que lleve equipado.
	 * * @return El stat de ataque modificado o el base si no lleva objeto.
	 */
	public int ataqueObjetoAplicado() {
		if (this.objetoEquipado == null || this.objetoEquipado.getNomObjeto() == null) return this.ataque;
		return (int) (this.ataque + (this.ataque * this.objetoEquipado.getModAtaque() / 100));
	}

	/**
	 * Método para obtener la defensa total con el plus del objeto que lleve equipado.
	 * * @return El stat de defensa modificado o el base si no lleva objeto.
	 */
	public int defensaObjetoAplicado() {
		if (this.objetoEquipado == null || this.objetoEquipado.getNomObjeto() == null) return this.defensa;
		return (int) (this.defensa + (this.defensa * this.objetoEquipado.getModDefensa() / 100));
	}

	/**
	 * Método para obtener la velocidad total con el plus del objeto que lleve equipado.
	 * * @return El stat de velocidad modificado o el base si no lleva objeto.
	 */
	public int velocidadaObjetoAplicado() {
		if (this.objetoEquipado == null || this.objetoEquipado.getNomObjeto() == null) return this.velocidad;
		return (int) (this.velocidad + (this.velocidad * this.objetoEquipado.getModVelocidad() / 100));
	}

	/**
	 * Método para obtener la defensa especial total con el plus del objeto que lleve equipado.
	 * * @return El stat de defensa especial modificado o el base si no lleva objeto.
	 */
	public int defEspecialObjetoAplicado() {
		if (this.objetoEquipado == null || this.objetoEquipado.getNomObjeto() == null) return this.defensaEspecial;
		return (int) (this.defensaEspecial + (this.defensaEspecial * this.objetoEquipado.getModDefEsp() / 100));
	}

	/**
	 * Método de clase para generar stats aleatorias cuando el Pokémon nace.
	 * * @return Un número entero aleatorio entre 1 y 10.
	 */
	private static int generarStatAleatoria() {
		return (int) (Math.random() * 10) + 1;
	}

	/**
	 * Método para la aleatoriedad del sexo.
	 * Utiliza un operador ternario: Si el número generado es menor que 0.5 es macho, si es mayor es hembra.
	 * * @return Una cadena "M" o "H".
	 */
	private static String generarSexoAleatorio() {
		return (Math.random() < 0.5) ? "M" : "H";
	}

	/**
	 * Método para gestionar la subida de nivel del Pokémon mediante la experiencia acumulada.
	 * Resta la experiencia consumida y recalcula las estadísticas de forma justa.
	 * * @return true si el Pokémon ha logrado subir al menos un nivel, false en caso contrario.
	 */
	public boolean subirNivel() {
		boolean subidaNivel = false;
		// Usamos un bucle While para los caso en los que se gane mucha experiencia de golpe.
		while (this.exp >= this.nivel * 10) {
			this.exp -= (this.nivel * 10); // Gastamos la experiencia que teníamos
			this.nivel++;
			subidaNivel = true;

			// Al subir de nivel se mejorará de forma justa usando la fórmula del DAO
			int statBaseBD = 10;
			this.vitalidad = 15 + statBaseBD + (this.nivel * 3);
			this.ataque = statBaseBD + (this.nivel * 2);
			this.defensa = statBaseBD + (this.nivel * 2);
			this.velocidad = statBaseBD + (this.nivel * 2);
			this.ataqueEspecial = statBaseBD + (this.nivel * 2);
			this.defensaEspecial = statBaseBD + (this.nivel * 2);

			// Reestablecemos la vida, estamina y estado al subir de nivel (como se pidió)
			this.estamina = 100;
			this.estadoActual = Estado.SALUDABLE;

			// Condición para que cada 3 niveles el pokemon avise de nuevo movimiento
			if (this.nivel % 3 == 0) {
				System.out.println("¡El pokemon puede aprender un nuevo movimiento!");
			}
		}

		return subidaNivel;
	}

	/**
	 * Comprueba si el Pokémon ha alcanzado el nivel necesario para evolucionar a su siguiente fase.
	 * * @return Devuelve el ID de la nueva especie si le toca evolucionar, o -1 si no tiene evolución pendiente.
	 */
	public int comprobarEvolucion() {
		int[] datosEvo = obtenerDatosEvolucion();

		// Si tiene una evolución programada y su nivel actual es mayor o igual al requerido evolucionará
		if (datosEvo != null && this.nivel >= datosEvo[1]) {
			return datosEvo[0]; 
		}

		return -1; 
	}

	/**
	 * Método para mutar al Pokémon a su nueva forma tras evolucionar.
	 * Se encarga de cambiar su especie, otorgar el plus de estadísticas y curarlo.
	 * * @param nuevaEspecie Objeto Pokedex con los datos de la evolución.
	 */
	public void evolucionar(Pokedex nuevaEspecie) {
		this.numPokedex = nuevaEspecie; // Cambiamos su especie

		// Plus de estadísticas extra por evolucionar
		this.vitalidad += 15;
		this.ataque += 10;
		this.defensa += 10;
		this.ataqueEspecial += 10;
		this.defensaEspecial += 10;
		this.velocidad += 10;

		// Curamos al pokemon
		this.estamina = 100;
	}

	/**
	 * Método para concebir a un Pokémon nuevo mediante crianza.
	 * Genera un hijo que hereda la especie de la madre y la media matemática de las estadísticas de los padres.
	 * * @param padre El Pokémon macho.
	 * @param madre El Pokémon hembra.
	 * @return Un nuevo Pokémon recién nacido (Nivel 1).
	 */
	public static Pokemon engendrarHijo(Pokemon padre, Pokemon madre) {
		// Hereda la especie de la madre (regla clásica de Pokémon)
		// Usamos el constructor de copia o nacimiento
		Pokemon hijo = new Pokemon(madre.getNumPokedex(), "Huevo de " + madre.getMote());

		// Hacemos que herede la media de los STATS de padre y madre
		hijo.setVitalidad((padre.getVitalidad() + madre.getVitalidad()) / 2);
		hijo.setAtaque((padre.getAtaque() + madre.getAtaque()) / 2);
		hijo.setDefensa((padre.getDefensa() + madre.getDefensa()) / 2);
		hijo.setVelocidad((padre.getVelocidad() + madre.getVelocidad()) / 2);
		hijo.setAtaqueEspecial((padre.getAtaqueEspecial() + madre.getAtaqueEspecial()) / 2);
		hijo.setDefensaEspecial((padre.getDefensaEspecial() + madre.getDefensaEspecial()) / 2);

		// Nivel al que nace el hijo
		hijo.setNivel(1);
		hijo.setEstamina(100); // Nace descansado
		hijo.setExp(0); // Nace sin exp.
		hijo.setFertilidad(5); // Nace con el máximo de fertilidad

		return hijo;
	}

	
	/** * Creamos el método atacar a otro pokemon.
	 * Gestiona el coste de estamina, cálculo de daño y efectividad elemental, 
	 * e impone estados alterados si corresponde.
	 * * @param pokemonEnemigo Parametro para pasarle el pokemon enemigo que recibe el golpe.
	 * @param movElegido Parametro para enviar el movimiento que se va a ejecutar.
	 */
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

				// Daño mínimo garantizado para que el combate fluya
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
						if (movElegido.getEstado() == Estado.QUEMADO && tipo == TipoPokemon.FUEGO) esInmune = true;
						if (movElegido.getEstado() == Estado.CONGELADO && tipo == TipoPokemon.HIELO) esInmune = true;
						if (movElegido.getEstado() == Estado.HELADO && tipo == TipoPokemon.HIELO) esInmune = true;
						if (movElegido.getEstado() == Estado.DRENADORAS && tipo == TipoPokemon.PLANTA) esInmune = true;
						if (movElegido.getEstado() == Estado.PARALIZADO && tipo == TipoPokemon.ELECTRICO) esInmune = true;
						if ((movElegido.getEstado() == Estado.ENVENENADO || movElegido.getEstado() == Estado.GRAVEMENTE_ENVENENADO)
								&& tipo == TipoPokemon.VENENO) esInmune = true;
					}

					if (!esInmune) {
						pokemonEnemigo.setEstadoActual(movElegido.getEstado());
						System.out.println("¡" + pokemonEnemigo.getMote() + " ahora está " + movElegido.getEstado() + "!");
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


	/**
	 * Creamos el método descansar para recuperar la estamina perdida que gastamos
	 * en combate o haciendo algún movimiento.
	 */
	public void descansar() {
		double modRecup = 1.0;
		double modMax = 1.0;
		
		// Solo aplicamos mods si el objeto es real y tiene nombre
		if (this.objetoEquipado != null && this.objetoEquipado.getNomObjeto() != null) {
			if (this.objetoEquipado.getModRecupEstamina() > 0) modRecup = this.objetoEquipado.getModRecupEstamina();
			if (this.objetoEquipado.getModEstamina() > 0) modMax = this.objetoEquipado.getModEstamina();
		}
		
		// Aseguramos un mínimo de 20 de recuperación para evitar bucles infinitos
		int cantRecuperada = (int) (20 * modRecup);
		if (cantRecuperada <= 0) cantRecuperada = 20; 
		
		this.estamina += cantRecuperada;
		
		int topeMax = (int) (100 * modMax);
		if (topeMax <= 0) topeMax = 100;
		
		if (this.estamina > topeMax) { 
			this.estamina = topeMax;
		}
	}


	/**
	 * Método para aprender un nuevo movimiento
	 * Reemplaza el movimiento situado en el índice indicado y lo devuelve.
	 * * @param nuevoMov Le pasamos el movimiento que queremos aprender.
	 * @param indice El hueco (0-3) que va a ocupar.
	 * @return El movimiento antiguo que ha sido reemplazado (para que el Controller lo guarde o descarte).
	 */
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

	/**
	 * Método que sabe qué bebé sale de cada evolución.
	 * Devuelve el ID de la primera fase evolutiva dependiendo de qué ID tenga la madre.
	 * * @param idMadre El número en la Pokédex de la madre.
	 * @return El número en la Pokédex correspondiente a su fase inicial.
	 */
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

	/**
	 * Método que define a qué ID evoluciona cada Pokémon y a qué nivel.
	 * * @return Un array con dos valores [ID Nueva Especie, Nivel Requerido]. 
	 * Si no evoluciona por nivel, devuelve null.
	 */
	public int[] obtenerDatosEvolucion() {
		// Obtenemos el ID de la especie actual
		int miIdEspecie = this.numPokedex.getNumPokedex();

		// Creamos un array para luego que la posición 0 sea el ID al que evolucionará y
		// la posición 1 sea el nivel requerido para dicha evolución
		return switch (miIdEspecie) {
		// Iniciales Planta
		case 1 -> new int[] { 2, 16 }; // Bulbasaur (1) -> Ivysaur (2) al Nv 16
		case 2 -> new int[] { 3, 32 }; // Ivysaur (2) -> Venusaur (3) al Nv 32

		// Iniciales Fuego
		case 4 -> new int[] { 5, 16 }; // Charmander (4) -> Charmeleon (5) al Nv 16
		case 5 -> new int[] { 6, 36 }; // Charmeleon (5) -> Charizard (6) al Nv 36

		// Iniciales Agua
		case 7 -> new int[] { 8, 16 }; // Squirtle (7) -> Wartortle (8) al Nv 16
		case 8 -> new int[] { 9, 36 }; // Wartortle (8) -> Blastoise (9) al Nv 36

		// Ejemplo de pájaros
		case 16 -> new int[] { 17, 18 }; // Pidgey (16) -> Pidgeotto (17) al Nv 18
		case 17 -> new int[] { 18, 36 }; // Pidgeotto (17) -> Pidgeot (18) al Nv 36

		case 19 -> new int[] { 20, 20 }; // Rattata -> Raticate
		case 21 -> new int[] { 22, 20 }; // Spearow -> Fearow
		case 23 -> new int[] { 24, 22 }; // Ekans -> Arbok
		case 27 -> new int[] { 28, 22 }; // Sandshrew -> Sandslash

		// Nidoran y Cuevas
		case 29 -> new int[] { 30, 16 }; // Nidoran ♀ -> Nidorina (Nidoqueen es por piedra)
		case 32 -> new int[] { 33, 16 }; // Nidoran ♂ -> Nidorino (Nidoking es por piedra)
		case 41 -> new int[] { 42, 22 }; // Zubat -> Golbat
		case 46 -> new int[] { 47, 24 }; // Paras -> Parasect
		case 50 -> new int[] { 51, 26 }; // Diglett -> Dugtrio
		case 52 -> new int[] { 53, 28 }; // Meowth -> Persian

		// Plantas, Psíquicos y Luchadores
		case 43 -> new int[] { 44, 21 }; // Oddish -> Gloom (Vileplume es por piedra)
		case 48 -> new int[] { 49, 31 }; // Venonat -> Venomoth
		case 54 -> new int[] { 55, 33 }; // Psyduck -> Golduck
		case 56 -> new int[] { 57, 28 }; // Mankey -> Primeape
		case 60 -> new int[] { 61, 25 }; // Poliwag -> Poliwhirl (Poliwrath es por piedra)
		case 63 -> new int[] { 64, 16 }; // Abra -> Kadabra (Alakazam es por intercambio)
		case 66 -> new int[] { 67, 28 }; // Machop -> Machoke (Machamp es por intercambio)
		case 69 -> new int[] { 70, 21 }; // Bellsprout -> Weepinbell (Victreebel es por piedra)
		case 72 -> new int[] { 73, 30 }; // Tentacool -> Tentacruel

		// Rocas y Fuego
		case 74 -> new int[] { 75, 25 }; // Geodude -> Graveler (Golem es por intercambio)
		case 77 -> new int[] { 78, 40 }; // Ponyta -> Rapidash
		case 79 -> new int[] { 80, 37 }; // Slowpoke -> Slowbro
		case 81 -> new int[] { 82, 30 }; // Magnemite -> Magneton

		// Variados Mid-Game
		case 84 -> new int[] { 85, 31 }; // Doduo -> Dodrio
		case 86 -> new int[] { 87, 34 }; // Seel -> Dewgong
		case 88 -> new int[] { 89, 38 }; // Grimer -> Muk
		case 92 -> new int[] { 93, 25 }; // Gastly -> Haunter (Gengar es por intercambio)
		case 96 -> new int[] { 97, 26 }; // Drowzee -> Hypno
		case 98 -> new int[] { 99, 28 }; // Krabby -> Kingler
		case 100 -> new int[] { 101, 30 }; // Voltorb -> Electrode
		case 104 -> new int[] { 105, 28 }; // Cubone -> Marowak
		case 109 -> new int[] { 110, 35 }; // Koffing -> Weezing
		case 111 -> new int[] { 112, 42 }; // Rhyhorn -> Rhydon

		// Agua Late-Game y Fósiles
		case 116 -> new int[] { 117, 32 }; // Horsea -> Seadra
		case 118 -> new int[] { 119, 33 }; // Goldeen -> Seaking
		case 129 -> new int[] { 130, 20 }; // Magikarp -> Gyarados
		case 138 -> new int[] { 139, 40 }; // Omanyte -> Omastar
		case 140 -> new int[] { 141, 40 }; // Kabuto -> Kabutops

		// Dragones
		case 147 -> new int[] { 148, 30 }; // Dratini -> Dragonair
		case 148 -> new int[] { 149, 55 }; // Dragonair -> Dragonite

		default -> null; // Si no tiene evolución por nivel
		};
	}

	/**
	 * Método toString.
	 * Devuelve un desglose completo con todas las propiedades de la instancia del Pokémon actual.
	 */
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