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

	// Eñ equipo secundario lo creamos como una linked list ya que no sabemos el
	// número exacto de pokemons que tendremos guardados aquí
	private LinkedList<Pokemon> equipoSecundario;
	// Hacemos lo mismo con los objetos. Creamos una linked list para los objetos
	// que guardaremos en nuestra mochila
	private LinkedList<Objeto> mochila;

	// Constructor por defecto
	public Entrenador() {
		super();
		this.idEntrenador = 0;
		this.nombreEntrenador = "";
		this.tipoEntrenador = "";
		this.pokedollar = 0;
		this.nivelEntrenador = 0;
		this.password = "";
		this.rutaImgFrontal = "";
		this.equipoPrincipal = new Pokemon[6];
		this.equipoSecundario = new LinkedList<Pokemon>();
		this.mochila = new LinkedList<Objeto>();
	}

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
			this.equipoSecundario = new LinkedList<Pokemon>(e.equipoSecundario); // Aqui usamos la misma lógica, creamos
			// una nueva lista con los datos que ya teníamos para evitar que las
			// modificaciones alteren los demás pokemons
		} else {
			this.equipoSecundario = new LinkedList<Pokemon>();
		}
		// Comprobamos mochila y copiamos de manera profunda
		if (this.mochila != null) {
			this.mochila = new LinkedList<Objeto>(e.mochila);
		} else {
			this.mochila = new LinkedList<Objeto>();
		}
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
	
	//Método para mover un pokemon del equipo principal al secundario.
	public boolean moverPokemon(int indice) {
		//Comprobamos si el índice es válido
			if (indice < 0 || indice > this.equipoPrincipal.length || this.equipoPrincipal[indice] == null) {
				return false;
			}
			int contador = 0;
			//Contamos cuántos pokemos tengo en el equipo principal.
			for(Pokemon p : this.equipoPrincipal) {
				if (p != null) {
					contador++;
				}
				// Creamos la condición para evitar quedarnos a 0 de pokemons
				if(contador <= 1) {
					return false;
				}
			}
			//Generamos la condición para mover al pokemon
			Pokemon p = this.equipoPrincipal[indice]; //Cogemos al pokemon específico del índice que elegimos
			this.equipoSecundario.add(p); //Lo metemos en la lista
			this.equipoPrincipal[indice] = null; // Lo borramos del equipo principal
		
			return true;
	}
	//Método para mover el pokemon de la caja secundaria a la principal
	public boolean moverPokeSecundario(int indice) {
		//Comprobamos si el índice es válido
		if(indice < 0 || indice < this.equipoSecundario.size()) {
			return false;
		}
			//Recorremos el equipo principal en busca de un hueco
			int huecoLibre = -1; //BANDERA QUE DECLARA QUE SIGNIFICA NINGÚN SITIO!
			for(int i = 0; i < this.equipoPrincipal.length; i++) {
				if(this.equipoPrincipal[i] == null) {
					huecoLibre = i; //Encontramos el hueco !
					break;
				}
			}
			//Condición para saber que si el hueco sigue siendo -1, es que no había realmente sitio ya que el equipo está lleno!
			if(huecoLibre == -1) {
				return false; // Cortamos aquí para que no siga y no dé error
			}
			
			//Al pasar las condiciones podemos usarlo y rellenar el array
			Pokemon p = this.equipoSecundario.get(indice); // Cogemos al Pokémon de la caja
			this.equipoPrincipal[huecoLibre] = p; // Lo ponemos en el hueco libre
			this.equipoSecundario.remove(indice); // Lo borramos de la caja
			
		return true;
	}
	
	//Método entrenar
	public boolean entrenarPokemon(Pokemon p, int pokedollarGastado) {
		
		
		return true;
	}

	// Método toString, ocultando la contraseña.
	@Override
	public String toString() {
	    return "--- DETALLES DEL ENTRENADOR " + idEntrenador + " ---\n" +
	           "Nombre: " + nombreEntrenador + "\n" +
	           "Tipo: " + tipoEntrenador + " | Nivel: " + nivelEntrenador + "\n" +
	           "Dinero: " + pokedollar + " pokedollars\n" +
	           "----------------------------\n" +
	           "EQUIPO Y MOCHILA:\n" +
	           "Principal: " + Arrays.toString(equipoPrincipal) + "\n" +
	           "Secundario: " + equipoSecundario + "\n" +
	           "Mochila: " + mochila + "\n" +
	           "----------------------------";
	}

}
