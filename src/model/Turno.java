package model;

public class Turno {
	public enum TipoAccion {
		ATACAR, MOCHILA, CAMBIO, HUIR, NADA
	}

	private int turnoActual;
	private Entrenador entrenador;
	private Entrenador rival;
	// Pokemons que están luchando
	private Pokemon pokemonActivoEntrenador;
	private Pokemon pokemonActivoRival;

	// Creamos las variables de las acciones para los dos jugadores.
	private TipoAccion accionEntrenador;
	private Movimiento movEntrenador;
	private Objeto objetoEntrenador;
	private Pokemon cambioEntrenador;

	private TipoAccion accionRival;
	private Movimiento movRival;
	private Objeto objetoRival;
	private Pokemon cambioRival;

	// Constructor modificado para evitar errores y campos de atributos vacíos
	public Turno(int turnoActual, Entrenador entrenador, Entrenador rival) {
		this.turnoActual = turnoActual;
		this.entrenador = entrenador;
		// Empezamos el turno e identificamos quién está luchando.
		this.pokemonActivoEntrenador = buscarPokemonActivo(entrenador);
		this.pokemonActivoRival = buscarPokemonActivo(rival);
		// Inicializamos las acciones a NADA al principio del turno
		this.accionEntrenador = TipoAccion.NADA;
		this.accionRival = TipoAccion.NADA;
	}

	// Generamos getters and setters de nuestro constructor modificado
	public int getTurnoActual() {
		return turnoActual;
	}

	public void setTurnoActual(int turnoActual) {
		this.turnoActual = turnoActual;
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public Entrenador getRival() {
		return rival;
	}

	public void setRival(Entrenador rival) {
		this.rival = rival;
	}

	public Pokemon getPokemonActivoEntrenador() {
		return pokemonActivoEntrenador;
	}

	public void setPokemonActivoEntrenador(Pokemon pokemonActivoEntrenador) {
		this.pokemonActivoEntrenador = pokemonActivoEntrenador;
	}

	public Pokemon getPokemonActivoRival() {
		return pokemonActivoRival;
	}

	public void setPokemonActivoRival(Pokemon pokemonActivoRival) {
		this.pokemonActivoRival = pokemonActivoRival;
	}

	public TipoAccion getAccionEntrenador() {
		return accionEntrenador;
	}

	public void setAccionEntrenador(TipoAccion accionEntrenador) {
		this.accionEntrenador = accionEntrenador;
	}

	public Movimiento getMovEntrenador() {
		return movEntrenador;
	}

	public void setMovEntrenador(Movimiento movEntrenador) {
		this.movEntrenador = movEntrenador;
	}

	public Objeto getObjetoEntrenador() {
		return objetoEntrenador;
	}

	public void setObjetoEntrenador(Objeto objetoEntrenador) {
		this.objetoEntrenador = objetoEntrenador;
	}

	public Pokemon getCambioEntrenador() {
		return cambioEntrenador;
	}

	public void setCambioEntrenador(Pokemon cambioEntrenador) {
		this.cambioEntrenador = cambioEntrenador;
	}

	public TipoAccion getAccionRival() {
		return accionRival;
	}

	public void setAccionRival(TipoAccion accionRival) {
		this.accionRival = accionRival;
	}

	public Movimiento getMovRival() {
		return movRival;
	}

	public void setMovRival(Movimiento movRival) {
		this.movRival = movRival;
	}

	public Objeto getObjetoRival() {
		return objetoRival;
	}

	public void setObjetoRival(Objeto objetoRival) {
		this.objetoRival = objetoRival;
	}

	public Pokemon getCambioRival() {
		return cambioRival;
	}

	public void setCambioRival(Pokemon cambioRival) {
		this.cambioRival = cambioRival;
	}

	// Método para hallar el pokemon en combate
	private Pokemon buscarPokemonActivo(Entrenador e) {
		// Buscamos los pokemons del entrenador en el quipo principal de la clase
		// entrenador
		for (Pokemon p : e.getEquipoPrincipal()) {
			if (p != null && p.getVitalidad() > 0) {
				return p;
			}
		}
		return null;
	}

	// Métodos específicos para las acciones
	public void prepararAtaqueEntrenador(Movimiento mov) {
		this.accionEntrenador = TipoAccion.ATACAR;
		this.movEntrenador = mov;
	}

	public void prepararObjetoEntrenador(Objeto obj) {
		this.accionEntrenador = TipoAccion.MOCHILA;
		this.objetoEntrenador = obj;
	}

	public void prepararCambioEntrenador(Pokemon nuevoPoke) {
		this.accionEntrenador = TipoAccion.CAMBIO;
		this.cambioEntrenador = nuevoPoke;
	}

	// Métodos para el rival
	public void prepararAtaqueRival(Movimiento mov) {
		this.accionRival = TipoAccion.ATACAR;
		this.movRival = mov;
	}

	public void prepararObjetoRival(Objeto obj) {
		this.accionRival = TipoAccion.MOCHILA;
		this.objetoRival = obj;
	}

	public void prepararCambioRival(Pokemon nuevoPoke) {
		this.accionRival = TipoAccion.CAMBIO;
		this.cambioRival = nuevoPoke;
	}
	
	//ToString modificado para obtener solamente lo que se desea de un turno
	@Override
	public String toString() {
	    return "=== TURNO " + turnoActual + " ===\n" +
	           "JUGADOR: " + entrenador.getNombreEntrenador() + 
	           " [" + pokemonActivoEntrenador.getMote() + "] " +
	           "--> Decisión: " + accionEntrenador + "\n" +
	           
	           "RIVAL: " + rival.getNombreEntrenador() + 
	           " [" + pokemonActivoRival.getMote() + "] " +
	           "--> Decisión: " + accionRival + "\n" +
	           "===================================";
	}
	
}
