package model;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Representa a un Entrenador Pokémon dentro del juego.
 * Gestiona la información del jugador, su dinero, su equipo principal,
 * los Pokémon almacenados en la caja y los objetos de su mochila.
 * * @author KodeX
 */
public class Entrenador {

	private int idEntrenador;
	private String nombreEntrenador;
	private String tipoEntrenador;
	private int pokedollar;
	private int nivelEntrenador;
	private String password;
	private String rutaImgFrontal;

	/** Equipo principal del entrenador (máximo 6 Pokémon). */
	private Pokemon[] equipoPrincipal = new Pokemon[6];

	/** Caja de almacenamiento para Pokémon que no caben en el equipo principal. */
	private LinkedList<Pokemon> equipoSecundario;

	/** Inventario de objetos que el entrenador ha comprado o conseguido. */
	private LinkedList<Objeto> mochila;

	/**
	 * Constructor completo para instanciar un Entrenador con todos sus datos.
	 * * @param idEntrenador     El ID con el que se identifica al Entrenador en la Base de Datos.
	 * @param nombreEntrenador El nombre del Entrenador.
	 * @param tipoEntrenador   Identifica el rol del Entrenador dentro del juego (ej: "JUGADOR").
	 * @param pokedollar       La cantidad de dinero que posee.
	 * @param nivelEntrenador  El nivel actual del Entrenador.
	 * @param password         La contraseña de acceso del usuario.
	 * @param rutaImgFrontal   Ruta de la imagen de su avatar.
	 * @param equipoPrincipal  Array con los Pokémon actuales en el equipo.
	 * @param equipoSecundario Lista con los Pokémon almacenados en el PC.
	 * @param mochila          Lista de objetos que posee.
	 */
	public Entrenador(int idEntrenador, String nombreEntrenador, String tipoEntrenador, int pokedollar,
			int nivelEntrenador, String password, String rutaImgFrontal, Pokemon[] equipoPrincipal,
			LinkedList<Pokemon> equipoSecundario, LinkedList<Objeto> mochila) {
		super();
		this.idEntrenador = idEntrenador;
		this.nombreEntrenador = nombreEntrenador;
		this.tipoEntrenador = tipoEntrenador;
		this.pokedollar = pokedollar;
		this.nivelEntrenador = nivelEntrenador;
		this.password = password;
		this.rutaImgFrontal = rutaImgFrontal;
		this.equipoPrincipal = equipoPrincipal;
		this.equipoSecundario = equipoSecundario;
		this.mochila = mochila;
	}

	/**
	 * Constructor para un Entrenador recién registrado (nivel 1 y sin dinero base inicial).
	 * Inicializa las colecciones vacías para evitar NullPointerException.
	 * * @param usuario  Nombre de usuario elegido en el registro.
	 * @param password Contraseña elegida en el registro.
	 */
	public Entrenador(String usuario, String password) {
		this.idEntrenador = 0;
		this.nombreEntrenador = usuario;
		this.tipoEntrenador = "";
		this.pokedollar = 0;
		this.nivelEntrenador = 1;
		this.password = password;
		this.rutaImgFrontal = "";
		this.equipoPrincipal = new Pokemon[6];
		this.equipoSecundario = new LinkedList<Pokemon>();
		this.mochila = new LinkedList<Objeto>();
	}

	/**
	 * Constructor copia. Realiza una copia profunda de las listas y arrays
	 * para evitar que las modificaciones alteren al objeto original.
	 * * @param e Objeto Entrenador que se desea clonar.
	 */
	public Entrenador(Entrenador e) {
		super();
		this.idEntrenador = e.idEntrenador;
		this.nombreEntrenador = e.nombreEntrenador;
		this.tipoEntrenador = e.tipoEntrenador;
		this.pokedollar = e.pokedollar;
		this.nivelEntrenador = e.nivelEntrenador;
		this.password = e.password;
		this.rutaImgFrontal = e.rutaImgFrontal;

		if (e.equipoPrincipal != null) {
			this.equipoPrincipal = e.equipoPrincipal.clone();
		} else {
			this.equipoPrincipal = new Pokemon[6];
		}

		if (e.equipoSecundario != null) {
			this.equipoSecundario = new LinkedList<Pokemon>(e.equipoSecundario);
		} else {
			this.equipoSecundario = new LinkedList<Pokemon>();
		}

		if (e.mochila != null) {
			this.mochila = new LinkedList<Objeto>(e.mochila);
		} else {
			this.mochila = new LinkedList<Objeto>();
		}
	}

	/**
	 * Constructor para recuperar un Entrenador existente desde la Base de Datos.
	 * * @param idBD           ID del entrenador en la BD.
	 * @param loginUsuario     Nombre del entrenador.
	 * @param loginContrasenia Contraseña del entrenador.
	 * @param dineroDB         Pokedóllares actuales almacenados.
	 */
	public Entrenador(int idBD, String loginUsuario, String loginContrasenia, int dineroDB) {
		this.idEntrenador = idBD;
		this.nombreEntrenador = loginUsuario;
		this.tipoEntrenador = "";
		this.pokedollar = dineroDB;
		this.nivelEntrenador = 1;
		this.password = loginContrasenia;
		this.rutaImgFrontal = "";
		this.equipoPrincipal = new Pokemon[6];
		this.equipoSecundario = new LinkedList<Pokemon>();
		this.mochila = new LinkedList<Objeto>();
	}

	/* Getters and setters Entrenador */

	public int getIdEntrenador() { return idEntrenador; }
	public void setIdEntrenador(int idEntrenador) { this.idEntrenador = idEntrenador; }
	public String getNombreEntrenador() { return nombreEntrenador; }
	public void setNombreEntrenador(String nombreEntrenador) { this.nombreEntrenador = nombreEntrenador; }
	public String getTipoEntrenador() { return tipoEntrenador; }
	public void setTipoEntrenador(String tipoEntrenador) { this.tipoEntrenador = tipoEntrenador; }
	public int getPokedollar() { return pokedollar; }
	public void setPokedollar(int pokedollar) { this.pokedollar = pokedollar; }
	public int getNivelEntrenador() { return nivelEntrenador; }
	public void setNivelEntrenador(int nivelEntrenador) { this.nivelEntrenador = nivelEntrenador; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getRutaImgFrontal() { return rutaImgFrontal; }
	public void setRutaImgFrontal(String rutaImgFrontal) { this.rutaImgFrontal = rutaImgFrontal; }
	public Pokemon[] getEquipoPrincipal() { return equipoPrincipal; }
	public void setEquipoPrincipal(Pokemon[] equipoPrincipal) { this.equipoPrincipal = equipoPrincipal; }
	public LinkedList<Pokemon> getEquipoSecundario() { return equipoSecundario; }
	public void setEquipoSecundario(LinkedList<Pokemon> equipoSecundario) { this.equipoSecundario = equipoSecundario; }
	public LinkedList<Objeto> getMochila() { return mochila; }
	public void setMochila(LinkedList<Objeto> mochila) { this.mochila = mochila; }

	/* Métodos de la lógica del Entrenador */

	/**
	 * Mueve un Pokémon del equipo principal a la caja (equipo secundario).
	 * No permite realizar el movimiento si es el último Pokémon del equipo.
	 * * @param indice La posición (0-5) del Pokémon en el equipo principal.
	 * @return {@code true} si se movió con éxito, {@code false} si el índice es inválido o dejaría el equipo vacío.
	 */
	public boolean moverPokemon(int indice) {
		// Comprobamos si el índice es válido
		if (indice < 0 || indice >= this.equipoPrincipal.length || this.equipoPrincipal[indice] == null) {
			return false;
		}

		int contador = 0;
		// Contamos cuántos pokemos tengo en el equipo principal.
		for (Pokemon p : this.equipoPrincipal) {
			if (p != null) {
				contador++;
			}
		}

		// Creamos la condición para evitar quedarnos a 0 de pokemons
		if (contador <= 1) {
			return false;
		}

		// Generamos la condición para mover al pokemon
		Pokemon p = this.equipoPrincipal[indice]; 
		this.equipoSecundario.add(p); 
		this.equipoPrincipal[indice] = null; 

		return true;
	}

	/**
	 * Mueve un Pokémon de la caja (equipo secundario) al equipo principal.
	 * Requiere que haya al menos un hueco libre en el equipo principal.
	 * * @param indice La posición del Pokémon dentro de la lista de la caja.
	 * @return {@code true} si se movió con éxito, {@code false} si el equipo está lleno o el índice no existe.
	 */
	public boolean moverPokeSecundario(int indice) {
		// Comprobamos si el índice es válido
		if (indice < 0 || indice >= this.equipoSecundario.size()) {
			return false;
		}

		// Recorremos el equipo principal en busca de un hueco
		int huecoLibre = -1;
		for (int i = 0; i < this.equipoPrincipal.length; i++) {
			if (this.equipoPrincipal[i] == null) {
				huecoLibre = i; 
				break;
			}
		}

		// Si sigue siendo -1, es que el equipo está lleno
		if (huecoLibre == -1) {
			return false; 
		}

		// Al pasar las condiciones podemos usarlo y rellenar el array
		Pokemon p = this.equipoSecundario.get(indice); 
		this.equipoPrincipal[huecoLibre] = p; 
		this.equipoSecundario.remove(indice); 

		return true;
	}

	/**
	 * Realiza un entrenamiento a un Pokémon especificado, cobrando el coste
	 * en Pokédollares y aplicando la mejora de estadísticas correspondiente.
	 * * @param p       El Pokémon que va a recibir el entrenamiento.
	 * @param entreno El tipo de entrenamiento a realizar.
	 */
	public void entrenarPokemon(Pokemon p, Entrenamiento entreno) {
		// Calculamos cuánto cuesta el entrenamiento.
		int coste = entreno.calcularCoste(p);

		// Comprobamos si tenemos suficientes pokedollares.
		if (this.pokedollar < coste) {
			System.out.println("No tienes suficientes Pokedollares.");
			System.out.println("Coste necesario: " + coste + " | tienes: " + this.pokedollar + "");
			return;
		}

		// Realizamos el pago
		this.pokedollar -= coste;
		System.out.println("Has pagado " + coste + " Pokedollares");

		// Aplicamos la mejora
		entreno.aplicarMejora(p);
	}

	/**
	 * Introduce a dos Pokémon en la guardería para criar un nuevo huevo.
	 * Realiza comprobaciones de sexo, especie, fertilidad y saldo del entrenador.
	 * * @param p1 El primer Pokémon progenitor.
	 * @param p2 El segundo Pokémon progenitor.
	 * @return Un nuevo objeto {@link Pokemon} de nivel 1 con el ID de su fase cría, 
	 * o {@code null} si no cumplen los requisitos para criar.
	 */
	public Pokemon ponerACriar(Pokemon p1, Pokemon p2) {
		// Generamos comprobaciones básicas antes de poner a criar
		if (p1.getIdPokemon() == p2.getIdPokemon()) {
			System.out.println("¡No puedes criar al mismo Pokémon consigo mismo!");
			return null;
		}

		if (p1.getSexo().equals(p2.getSexo())) {
			System.out.println("¡Necesitas un macho y una hembra!");
			return null;
		}

		if (p1.getNumPokedex().getNumPokedex() != p2.getNumPokedex().getNumPokedex()) {
			System.out.println("¡Deben ser de la misma especie para criar!");
			return null;
		}

		if (p1.getFertilidad() <= 0 || p2.getFertilidad() <= 0) {
			System.out.println("Uno de los Pokémon ya no es fértil.");
			return null;
		}

		int costeCrianza = 200;
		if (this.pokedollar < costeCrianza) {
			System.out.println("No tienes suficiente dinero para la guardería.");
			return null;
		}

		// Si todo está en orden, procedemos a cobrar
		this.pokedollar -= costeCrianza; 

		// Restamos fertilidad a los padres
		p1.setFertilidad(p1.getFertilidad() - 1);
		p2.setFertilidad(p2.getFertilidad() - 1);

		System.out.println("¡Han puesto un huevo!");

		// Llamamos al método de Pokémon para generar la genética del hijo
		Pokemon bebe = Pokemon.engendrarHijo(p1, p2);

		// Usamos método para saber el ID real (Si la madre es 3, devuelve 1)
		int idBebeReal = Pokemon.obtenerIdBebe(p1.getNumPokedex().getNumPokedex());

		// Creamos un molde de Pokedex nueva y limpia solo para el bebé
		model.Pokedex pokedexBebe = new model.Pokedex();
		pokedexBebe.setNumPokedex(idBebeReal); 
		bebe.setNumPokedex(pokedexBebe); 
		bebe.setIdEntrenador(this.idEntrenador);

		return bebe; 
	}

	/**
	 * Guarda un Pokémon recién obtenido (por captura o crianza). 
	 * Si hay hueco, lo asigna al equipo principal; si está lleno, a la caja.
	 * * @param nuevoPokemon El Pokémon a almacenar.
	 * @return Un String que indica el destino del Pokémon: {@code "EQUIPO"} o {@code "CAJA"}.
	 */
	public String guardarPokemon(Pokemon nuevoPokemon) {
		nuevoPokemon.setIdEntrenador(this.idEntrenador);

		// Buscamos hueco en el array principal (Equipo principal)
		for (int i = 0; i < this.equipoPrincipal.length; i++) {
			if (this.equipoPrincipal[i] == null) {
				this.equipoPrincipal[i] = nuevoPokemon;
				System.out.println("¡" + nuevoPokemon.getMote() + " ha sido añadido a tu equipo principal!");
				return "EQUIPO";
			}
		}

		// En caso de que no hubiera hueco lo guardamos en el equipo secundario.
		this.equipoSecundario.add(nuevoPokemon);
		System.out.println("Equipo lleno. " + nuevoPokemon.getMote() + " ha sido enviado a la caja.");

		return "CAJA";
	}

	/**
	 * Intenta atrapar un Pokémon salvaje consumiendo una Pokéball de la mochila.
	 * Tiene una probabilidad base de éxito del 66.6%.
	 * * @param salvaje  El Pokémon que se desea capturar.
	 * @param pokeball El objeto (Pokéball) a utilizar en el intento.
	 * @return {@code true} si se capturó con éxito, {@code false} si escapó o no hay Pokéballs.
	 */
	public boolean capturarPokemon(Pokemon salvaje, Objeto pokeball) {
		// Comprobamos si nos han pasado una pokéball y la gastamos de la mochila
		if (pokeball != null && this.mochila != null && this.mochila.contains(pokeball)) {
			this.mochila.remove(pokeball);
			System.out.println(this.nombreEntrenador + " usó " + pokeball.getNomObjeto() + "...");
		} else {
			System.out.println("No tienes Pokéballs en la mochila...");
			return false;
		}

		// La probabilidad de atraparlo
		double probabilidadBase = 2.0 / 3.0;
		double suerte = Math.random(); 

		// Comprobamos si hay suerte
		if (suerte <= probabilidadBase) {
			System.out.println("¡" + salvaje.getMote() + " atrapado en la Pokéball!");
			return true; 
		} else {
			System.out.println("¡Vaya! " + salvaje.getMote() + " se ha escapado.");
			return false; 
		}
	}

	/**
	 * Genera una representación en formato texto con los datos generales del Entrenador.
	 * * @return Cadena de texto con detalles del entrenador, saldo, equipo y mochila.
	 */
	@Override
	public String toString() {
		return "--- DETALLES DEL ENTRENADOR " + idEntrenador + " ---\n" + "Nombre: " + nombreEntrenador + "\n"
				+ "Tipo: " + tipoEntrenador + " | Nivel: " + nivelEntrenador + "\n" + "Dinero: " + pokedollar
				+ " pokedollars\n" + "----------------------------\n" + "EQUIPO Y MOCHILA:\n" + "Principal: "
				+ Arrays.toString(equipoPrincipal) + "\n" + "Secundario: " + equipoSecundario + "\n" + "Mochila: "
				+ mochila + "\n" + "----------------------------";
	}
}