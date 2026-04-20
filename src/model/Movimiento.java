package model;


public class Movimiento {
	private int idMovimiento, potencia, pp, numTurnos, costeEstamina, nivelAprendizaje;
    private String claseMovimiento; // ataque, estado, mejora
    private String nomMovimiento;
    private String descripMovimiento;
    private Estado estado;  // El efecto a aplica - ejemplo: Quemado
    private String mejora;  // La estadistica que mejora - ejemplo: Defensa 
    private TipoPokemon tipoPokemon;
    
    
	// Constructor por defecto
    public Movimiento() {
		super();
		this.claseMovimiento = "ataque";
		this.nomMovimiento = "Sin nombre";
		this.tipoPokemon = TipoPokemon.NORMAL;
	}

 // constructor parametrizado
    public Movimiento(int idMovimiento, int potencia, int pp, int numTurnos, int costeEstamina, int nivelAprendizaje,
            String claseMovimiento, String nomMovimiento, String descripMovimiento, Estado estado, String mejora,
            TipoPokemon tipoPokemon) {
        super();
        this.idMovimiento = idMovimiento;
        this.potencia = potencia;
        this.pp = pp;
        this.numTurnos = numTurnos;
        
        // asignamos la clase y la estamina
        this.claseMovimiento = (claseMovimiento != null) ? claseMovimiento.toLowerCase() : "ataque";
        this.costeEstamina = calcularCosteEstamina(); 
        
        this.nivelAprendizaje = nivelAprendizaje;
        this.nomMovimiento = nomMovimiento;
        this.descripMovimiento = descripMovimiento;
        this.estado = estado;
        this.mejora = mejora;
        this.tipoPokemon = tipoPokemon;
    }
 	
 	// constructor copia
 	public Movimiento(Movimiento m) {
 		super();
 		this.idMovimiento = m.idMovimiento;
 		this.potencia = m.potencia;
 		this.pp = m.pp;
 		this.numTurnos = m.numTurnos;
 		this.costeEstamina = m.costeEstamina;
 		this.nivelAprendizaje = m.nivelAprendizaje;
 		this.claseMovimiento = m.claseMovimiento;
 		this.nomMovimiento = m.nomMovimiento;
 		this.descripMovimiento = m.descripMovimiento;
 		this.estado = m.estado;
 		this.mejora = m.mejora;
 		this.tipoPokemon = m.tipoPokemon;
 	}

 	// Getters para obtener informacion del objeto (los setters no son necesarios al no modificar los valores)

	public int getIdMovimiento() {
		return idMovimiento;
	}

	public int getPotencia() {
		return potencia;
	}

	public int getPp() {
		return pp;
	}

	public int getNumTurnos() {
		return numTurnos;
	}

	public int getCosteEstamina() {
		return costeEstamina;
	}

	public int getNivelAprendizaje() {
		return nivelAprendizaje;
	}

	public String getClaseMovimiento() {
		return claseMovimiento;
	}

	public String getNomMovimiento() {
		return nomMovimiento;
	}

	public String getDescripMovimiento() {
		return descripMovimiento;
	}

	public Estado getEstado() {
		return estado;
	}

	public String getMejora() {
		return mejora;
	}

	public TipoPokemon getTipoPokemon() {
		return tipoPokemon;
	}
	

	public void setIdMovimiento(int idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public void setNumTurnos(int numTurnos) {
		this.numTurnos = numTurnos;
	}

	public void setCosteEstamina(int costeEstamina) {
		this.costeEstamina = costeEstamina;
	}

	public void setNivelAprendizaje(int nivelAprendizaje) {
		this.nivelAprendizaje = nivelAprendizaje;
	}

	public void setClaseMovimiento(String claseMovimiento) {
		this.claseMovimiento = claseMovimiento;
	}

	public void setNomMovimiento(String nomMovimiento) {
		this.nomMovimiento = nomMovimiento;
	}

	public void setDescripMovimiento(String descripMovimiento) {
		this.descripMovimiento = descripMovimiento;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setMejora(String mejora) {
		this.mejora = mejora;
	}

	public void setTipoPokemon(TipoPokemon tipoPokemon) {
		this.tipoPokemon = tipoPokemon;
	}

	// Método privado para el coste de estamina
	// Calculamos el coste de estamina en base al tipo de movimiento.
	public int calcularCosteEstamina() {
		if (this.claseMovimiento.equals("ataque")){ // si el tipo es ataque, dividimos la potencia entre 2
			this.costeEstamina = this.potencia / 2;
		} else if (this.claseMovimiento.equals("estado")){ // si es el estado multiplicamos * 10 el numero de turnos
			this.costeEstamina = this.numTurnos * 10;
		} else if (this.claseMovimiento.equals("mejora")){
			this.costeEstamina = this.numTurnos * 10;
		}

		return this.costeEstamina;
	}

	public boolean puedeRealizarTurno(int estaminaActual) {
		int costeEstamina = this.costeEstamina;
		
		if (estaminaActual >= costeEstamina) { // si el coste es menor a la estamina se puede atacar
			return true;
		} else {
			return false;
		}
	}

	// toString
	@Override
	public String toString() {
	    return "--- DETALLES DEL MOVIMIENTO " + idMovimiento + " ---\n" +
	           "Nombre: " + nomMovimiento + " [" + tipoPokemon + "]\n" +
	           "Clase: " + claseMovimiento + " | Estado: " + estado + "\n" +
	           "Descripción: " + descripMovimiento + "\n" +
	           "----------------------------\n" +
	           "ESTADÍSTICAS DE COMBATE:\n" +
	           "Potencia: " + potencia + " | PP: " + pp + " | Estamina: " + costeEstamina + "\n" +
	           "Turnos: " + numTurnos + " | Nivel aprendizaje: " + nivelAprendizaje + "\n" +
	           "----------------------------\n" +
	           "EFECTOS ADICIONALES:\n" +
	           "Mejora: " + mejora + "\n" +
	           "----------------------------";
	}

}
