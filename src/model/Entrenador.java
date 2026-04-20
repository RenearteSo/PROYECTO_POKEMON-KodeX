package model;

import java.util.Arrays;
import java.util.LinkedList;

public class Entrenador {

	private int idEntrenador;
	private String nombreEntrenador;
	private String tipoEntrenador;
	private int pokedollar;
	private int nivelEntrenador;
	private String password;
	private String rutaImgFrontal;

	// Creamos 2 arrays de pokemons que serán nuestros equipos
	private Pokemon[] equipoPrincipal = new Pokemon[6];

	// El equipo secundario lo creamos como una linked list ya que no sabemos el
	// número exacto de pokemons que tendremos guardados aquí
	private LinkedList<Pokemon> equipoSecundario;

	// Hacemos lo mismo con los objetos. Creamos una linked list para los objetos
	// que guardaremos en nuestra mochila
	private LinkedList<Objeto> mochila;

	// Constructor con parámetros
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

	// Constructor para login
	public Entrenador(String usuario, String password) {
		this.idEntrenador = 0;
		this.nombreEntrenador = usuario;
		this.tipoEntrenador = "";
		this.pokedollar = 0;
		this.nivelEntrenador = 1;
		this.password = password;
		this.rutaImgFrontal = "";
		// Inicializamos las colecciones para evitar NullPointerException
		this.equipoPrincipal = new Pokemon[6];
		this.equipoSecundario = new LinkedList<Pokemon>();
		this.mochila = new LinkedList<Objeto>();
	}

	// Constructor copia
	public Entrenador(Entrenador e) {
		super();
		this.idEntrenador = e.idEntrenador;
		this.nombreEntrenador = e.nombreEntrenador;
		this.tipoEntrenador = e.tipoEntrenador;
		this.pokedollar = e.pokedollar;
		this.nivelEntrenador = e.nivelEntrenador;
		this.password = e.password;
		this.rutaImgFrontal = e.rutaImgFrontal;

		// Realizamos copia profunda del equipo principal CLONÁNDOLO
		if (e.equipoPrincipal != null) {
			this.equipoPrincipal = e.equipoPrincipal.clone();
		} else {
			this.equipoPrincipal = new Pokemon[6];
		}

		// Comprobamos igual con los LinkdeList
		if (e.equipoSecundario != null) {
			// Aqui usamos la misma lógica, creamos una nueva lista con los datos
			// que ya teníamos para evitar que las modificaciones alteren los demás pokemons
			this.equipoSecundario = new LinkedList<Pokemon>(e.equipoSecundario);
		} else {
			this.equipoSecundario = new LinkedList<Pokemon>();
		}

		// Comprobamos mochila y copiamos de manera profunda
		if (e.mochila != null) {
			this.mochila = new LinkedList<Objeto>(e.mochila);
		} else {
			this.mochila = new LinkedList<Objeto>();
		}
	}

	// Creamos constructor específico para el Login controller
	public Entrenador(int idBD, String loginUsuario, String loginContrasenia, int dineroDB) {
		this.idEntrenador = idBD;
		this.nombreEntrenador = loginUsuario;
		this.tipoEntrenador = "";
		this.pokedollar = dineroDB;
		this.nivelEntrenador = 1;
		this.password = loginContrasenia;
		this.rutaImgFrontal = "";
		// Inicializamos las colecciones para evitar NullPointerException al capturar o
		// abrir mochila
		this.equipoPrincipal = new Pokemon[6];
		this.equipoSecundario = new LinkedList<Pokemon>();
		this.mochila = new LinkedList<Objeto>();
	}

	// Getters and setters Entrenador 

	public int getIdEntrenador() {
		return idEntrenador;
	}

	public void setIdEntrenador(int idEntrenador) {
		this.idEntrenador = idEntrenador;
	}

	public String getNombreEntrenador() {
		return nombreEntrenador;
	}

	public void setNombreEntrenador(String nombreEntrenador) {
		this.nombreEntrenador = nombreEntrenador;
	}

	public String getTipoEntrenador() {
		return tipoEntrenador;
	}

	public void setTipoEntrenador(String tipoEntrenador) {
		this.tipoEntrenador = tipoEntrenador;
	}

	public int getPokedollar() {
		return pokedollar;
	}

	public void setPokedollar(int pokedollar) {
		this.pokedollar = pokedollar;
	}

	public int getNivelEntrenador() {
		return nivelEntrenador;
	}

	public void setNivelEntrenador(int nivelEntrenador) {
		this.nivelEntrenador = nivelEntrenador;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRutaImgFrontal() {
		return rutaImgFrontal;
	}

	public void setRutaImgFrontal(String rutaImgFrontal) {
		this.rutaImgFrontal = rutaImgFrontal;
	}

	public Pokemon[] getEquipoPrincipal() {
		return equipoPrincipal;
	}

	public void setEquipoPrincipal(Pokemon[] equipoPrincipal) {
		this.equipoPrincipal = equipoPrincipal;
	}

	public LinkedList<Pokemon> getEquipoSecundario() {
		return equipoSecundario;
	}

	public void setEquipoSecundario(LinkedList<Pokemon> equipoSecundario) {
		this.equipoSecundario = equipoSecundario;
	}

	public LinkedList<Objeto> getMochila() {
		return mochila;
	}

	public void setMochila(LinkedList<Objeto> mochila) {
		this.mochila = mochila;
	}

	//  Métodos de la lógica del Entrenador 

	// Método para mover un pokemon del equipo principal al secundario.
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
		Pokemon p = this.equipoPrincipal[indice]; // Cogemos al pokemon específico del índice que elegimos
		this.equipoSecundario.add(p); // Lo metemos en la lista
		this.equipoPrincipal[indice] = null; // Lo borramos del equipo principal

		return true;
	}

	// Método para mover el pokemon de la caja secundaria a la principal
	public boolean moverPokeSecundario(int indice) {
		// Comprobamos si el índice es válido
		if (indice < 0 || indice >= this.equipoSecundario.size()) {
			return false;
		}

		// Recorremos el equipo principal en busca de un hueco
		int huecoLibre = -1; // BANDERA QUE DECLARA QUE SIGNIFICA NINGÚN SITIO!
		for (int i = 0; i < this.equipoPrincipal.length; i++) {
			if (this.equipoPrincipal[i] == null) {
				huecoLibre = i; // Encontramos el hueco !
				break;
			}
		}

		// Condición para saber que si el hueco sigue siendo -1, es que no había
		// realmente sitio ya que el equipo está lleno!
		if (huecoLibre == -1) {
			return false; // Cortamos aquí para que no siga y no dé error
		}

		// Al pasar las condiciones podemos usarlo y rellenar el array
		Pokemon p = this.equipoSecundario.get(indice); // Cogemos al Pokémon de la caja
		this.equipoPrincipal[huecoLibre] = p; // Lo ponemos en el hueco libre
		this.equipoSecundario.remove(indice); // Lo borramos de la caja

		return true;
	}

	// Método entrenar
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

	// Método para poner a criar
	public Pokemon ponerACriar(Pokemon p1, Pokemon p2) {
		// Generamos comprobaciones básicas antes de poner a criar
		if (p1.getIdPokemon() == p2.getIdPokemon()) {
			System.out.println("¡No puedes criar al mismo Pokémon consigo mismo!");
			return null;
		}

		// Comprobamos sexo
		if (p1.getSexo().equals(p2.getSexo())) {
			System.out.println("¡Necesitas un macho y una hembra!");
			return null;
		}

		// Comprobamos que sean de la misma especie
		if (p1.getNumPokedex().getNumPokedex() != p2.getNumPokedex().getNumPokedex()) {
			System.out.println("¡Deben ser de la misma especie para criar!");
			return null;
		}

		// Comprobamos fertilidad
		if (p1.getFertilidad() <= 0 || p2.getFertilidad() <= 0) {
			System.out.println("Uno de los Pokémon ya no es fértil.");
			return null;
		}

		// Comprobamos dinero
		int costeCrianza = 200;
		if (this.pokedollar < costeCrianza) {
			System.out.println("No tienes suficiente dinero para la guardería.");
			return null;
		}

		// Si todo está en orden, procedemos
		this.pokedollar -= costeCrianza; // Cobramos

		// Restamos fertilidad a los padres
		p1.setFertilidad(p1.getFertilidad() - 1);
		p2.setFertilidad(p2.getFertilidad() - 1);

		System.out.println("¡Han puesto un huevo!");

		// Llamamos al método de Pókemon que creamos antes.
		Pokemon bebe = Pokemon.engendrarHijo(p1, p2);

		// Usamos método para saber el ID real (Si la madre es 3, devuelve 1)
		int idBebeReal = Pokemon.obtenerIdBebe(p1.getNumPokedex().getNumPokedex());

		// Creamos un molde de Pokedex nueva y limpia solo para el bebé
		model.Pokedex pokedexBebe = new model.Pokedex();
		pokedexBebe.setNumPokedex(idBebeReal); // Le ponemos el ID 1
		bebe.setNumPokedex(pokedexBebe); // Se la asignamos al bebé
		// Asignamos el ID del entrenador al bebé
		bebe.setIdEntrenador(this.idEntrenador);

		return bebe; // Devolvemos el bebé al Main para que le ponga nombre
	}

	// Creamos un método para guardar el pokemon cuando lo capturamos.
	// Devuelve String ("EQUIPO" o "CAJA") para saber dónde se guardó y mandarlo a
	// la base de datos.
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

	// Método modificado para la vista de captura directa (Boletín 1)
	public boolean capturarPokemon(Pokemon salvaje, Objeto pokeball) {
		// Comprobamos si nos han pasado una pokéball y la gastamos de la mochila
		if (pokeball != null && this.mochila != null && this.mochila.contains(pokeball)) {
			this.mochila.remove(pokeball);
			System.out.println(this.nombreEntrenador + " usó " + pokeball.getNomObjeto() + "...");
		} else {
			// Si no le pasas un objeto o no lo tiene, cortamos.
			// Hay que gastar una pokéball cada vez.
			System.out.println("No tienes Pokéballs en la mochila...");
			return false;
		}

		// La probabilidad de 2/3 de atraparlo
		double probabilidadBase = 2.0 / 3.0;
		double suerte = Math.random(); // genera numero entre 0.0 y 1.0

		// Comprobamos si hay suerte
		if (suerte <= probabilidadBase) {
			System.out.println("¡" + salvaje.getMote() + " atrapado en la Pokéball!");
			// El guardado se hace ahora desde el controlador tras validar el mote
			return true; // Éxito
		} else {
			System.out.println("¡Vaya! " + salvaje.getMote() + " se ha escapado.");
			return false; // Fracaso
		}
	}

	// Método toString, ocultando la contraseña.
	@Override
	public String toString() {
		return "--- DETALLES DEL ENTRENADOR " + idEntrenador + " ---\n" + "Nombre: " + nombreEntrenador + "\n"
				+ "Tipo: " + tipoEntrenador + " | Nivel: " + nivelEntrenador + "\n" + "Dinero: " + pokedollar
				+ " pokedollars\n" + "----------------------------\n" + "EQUIPO Y MOCHILA:\n" + "Principal: "
				+ Arrays.toString(equipoPrincipal) + "\n" + "Secundario: " + equipoSecundario + "\n" + "Mochila: "
				+ mochila + "\n" + "----------------------------";
	}
}