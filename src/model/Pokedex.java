package model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Pokedex {

	private int numPokedex;
	private String nomPokemon;
	private String rutaImgFront;
	private String rutaImgBack;
	private String sonido;
	private LinkedList<TipoPokemon> tipos; // 
	private ArrayList<Movimiento> listaMovimientosPosibles;
	

	// Constructor por defecto
	public Pokedex() {
		this.numPokedex = 0;
		this.nomPokemon = "Sin Nombre";
		this.rutaImgFront = "";
		this.rutaImgBack = "";
		this.sonido = "";
		this.tipos = new LinkedList<>();
		this.listaMovimientosPosibles = new ArrayList<>();
	}

	// Constructor parametrixado
	public Pokedex(int numPokedex, String nomPokemon, String rutaImgFront, String rutaImgBack, String sonido, LinkedList<TipoPokemon> tipos, ArrayList<Movimiento> movimientos) {
		this.numPokedex = numPokedex;
		this.nomPokemon = nomPokemon;
		this.rutaImgFront = rutaImgFront;
		this.rutaImgBack = rutaImgBack;
		this.sonido = sonido;
		this.tipos = tipos;
		this.listaMovimientosPosibles = movimientos;
	}

	// Constructor copia
	public Pokedex(Pokedex p) {
			this.numPokedex = p.numPokedex;
			this.nomPokemon = p.nomPokemon;
			this.rutaImgFront = p.rutaImgFront;
			this.rutaImgBack = p.rutaImgBack;
			this.sonido = p.sonido;
			this.tipos = new LinkedList<>(p.tipos); 
			this.listaMovimientosPosibles = new ArrayList<>(p.listaMovimientosPosibles); 
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
	
	// MÉTODOS 
	
	public LinkedList<TipoPokemon> getTipos() {
		return tipos;
	}

	public void setTipos(LinkedList<TipoPokemon> tipos) {
		this.tipos = tipos;
	}

	public ArrayList<Movimiento> getListaMovimientosPosibles() {
		return listaMovimientosPosibles;
	}

	public void setListaMovimientosPosibles(ArrayList<Movimiento> listaMovimientosPosibles) {
		this.listaMovimientosPosibles = listaMovimientosPosibles;
	}

	// Busca el movimiento que corresponde aprender al nivel actual, uno nuevo cada 3 niveles
	public Movimiento obtenerMovimientoPorNivel(int nivel) {
		for (Movimiento m : listaMovimientosPosibles) {
			if (m.getNivelAprendizaje() == nivel) {
				return m;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Especie: " + nomPokemon + " (Nº " + numPokedex + ")";
	}
}