package model;

public class Pokedex {

	private int numPokedex;
	private String nomPokemon;
	private String rutaImgFront;
	private String rutaImgBack;
	private String sonido;

	// Constructor por defecto
	public Pokedex() {
		this.numPokedex = 0;
		this.nomPokemon = "Sin Nombre";
		this.rutaImgFront = "";
		this.rutaImgBack = "";
		this.sonido = "";
	}

	// Constructor parametrixado
	public Pokedex(int numPokedex, String nomPokemon, String rutaImgFront, String rutaImgBack, String sonido) {
		this.numPokedex = numPokedex;
		this.nomPokemon = nomPokemon;
		this.rutaImgFront = rutaImgFront;
		this.rutaImgBack = rutaImgBack;
		this.sonido = sonido;
	}

	// Constructor copia
	public Pokedex(Pokedex p) {
			this.numPokedex = p.numPokedex;
			this.nomPokemon = p.nomPokemon;
			this.rutaImgFront = p.rutaImgFront;
			this.rutaImgBack = p.rutaImgBack;
			this.sonido = p.sonido;
	}

	// Métodos get y set
	public int getNumPokedex() {
		return numPokedex;
	}

	public void setNumPokedex(int numPokedex) {
		this.numPokedex = numPokedex;
	}

	public String getNomPokemon() {
		return nomPokemon;
	}

	public void setNomPokemon(String nomPokemon) {
		this.nomPokemon = nomPokemon;
	}

	public String getRutaImgFront() {
		return rutaImgFront;
	}

	public void setRutaImgFront(String rutaImgFront) {
		this.rutaImgFront = rutaImgFront;
	}

	public String getRutaImgBack() {
		return rutaImgBack;
	}

	public void setRutaImgBack(String rutaImgBack) {
		this.rutaImgBack = rutaImgBack;
	}

	public String getSonido() {
		return sonido;
	}

	public void setSonido(String sonido) {
		this.sonido = sonido;
	}

	@Override
	public String toString() {
		return "Especie: " + nomPokemon + " (Nº " + numPokedex + ")";
	}
}