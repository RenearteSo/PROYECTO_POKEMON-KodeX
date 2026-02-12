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
		this.nivelEntrenador = nivelEntrenador;
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
		//Comprobamos mochila y copiamos de manera profunda
		if (this.mochila != null) {
			this.mochila = new LinkedList<Objeto>(e.mochila);
		} else {
			this.mochila = new LinkedList<Objeto>();
		}
	}
	
	//Getters and setters Entrenador
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
	//Método toString
	@Override
	public String toString() {
		return "Entrenador [idEntrenador=" + idEntrenador + ", nombreEntrenador=" + nombreEntrenador
				+ ", tipoEntrenador=" + tipoEntrenador + ", pokedollar=" + pokedollar + ", nivelEntrenador="
				+ nivelEntrenador + ", password=" + password + ", rutaImgFrontal=" + rutaImgFrontal
				+ ", equipoPrincipal=" + Arrays.toString(equipoPrincipal) + ", equipoSecundario=" + equipoSecundario
				+ ", mochila=" + mochila + "]";
	}
	
	
}
