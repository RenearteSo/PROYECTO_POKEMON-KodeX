package model;

public class Movimiento {
	private int idMovimiento, potencia, PP, numTurnos, costeEstamina;
	// Clase movimiento si es de: mejora, estado, ataque.
	private String tipoMovimiento;
	private String nomMovimiento;
	private String descripMovimiento;
	private TipoPokemon tipoPokemon; // cogemos el tipo de la clase

	// Constructor por defecto
	public Movimiento() {
		super();
		this.idMovimiento = 0;
		this.potencia = 0;
		this.PP = 0;
		this.numTurnos = 0;
		this.costeEstamina = 0;
		this.tipoMovimiento = "";
		this.nomMovimiento = "";
		this.descripMovimiento = "";
		this.tipoPokemon = TipoPokemon.NORMAL;
	}

	// Constructor con parámetros
	public Movimiento(int idMovimiento, int potencia, int PP, int numTurnos, int costeEstamina, String tipoMovimiento,
			String nomMovimiento, String descripMovimiento, TipoPokemon tipoPokemon) {
		super();
		this.idMovimiento = idMovimiento;
		this.potencia = potencia; // OJO! El orden de los parámetros aquí importa, Se ha cambiado la potencia y el
									// tipo de movimeitno a antes de costeEstamina y de que aplique el método para
									// que no salga un valor 0 siempre
		this.tipoMovimiento = tipoMovimiento;
		this.PP = PP;
		this.numTurnos = numTurnos;
		this.costeEstamina = calcularCosteEstamina();// Pasamos el método de calcular el coste de estamina al
														// constructor con parámetros para que automáticamente se
														// calcule el oste del movimiento.
		this.nomMovimiento = nomMovimiento;
		this.descripMovimiento = descripMovimiento;
		this.tipoPokemon = tipoPokemon;
	}

	// Constructor copia
	public Movimiento(Movimiento m) {
		super();
		this.idMovimiento = m.idMovimiento;
		this.potencia = m.potencia;
		this.PP = m.PP;
		this.numTurnos = m.numTurnos;
		this.costeEstamina = m.costeEstamina;
		this.tipoMovimiento = m.tipoMovimiento;
		this.nomMovimiento = m.nomMovimiento;
		this.descripMovimiento = m.descripMovimiento;
		this.tipoPokemon = m.tipoPokemon;
	}

	// Setters and getters
	public int getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(int idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public int getPP() {
		return PP;
	}

	public void setPP(int pP) {
		PP = pP;
	}

	public int getNumTurnos() {
		return numTurnos;
	}

	public void setNumTurnos(int numTurnos) {
		this.numTurnos = numTurnos;
	}

	public int getCosteEstamina() {
		return costeEstamina;
	}

	public void setCosteEstamina(int costeEstamina) {
		this.costeEstamina = costeEstamina;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getNomMovimiento() {
		return nomMovimiento;
	}

	public void setNomMovimiento(String nomMovimiento) {
		this.nomMovimiento = nomMovimiento;
	}

	public String getDescripMovimiento() {
		return descripMovimiento;
	}

	public void setDescripMovimiento(String descripMovimiento) {
		this.descripMovimiento = descripMovimiento;
	}

	public TipoPokemon getTipoPokemon() {
		return tipoPokemon;
	}

	public void setTipoPokemon(TipoPokemon tipoPokemon) {
		this.tipoPokemon = tipoPokemon;
	}

	// Método Coste de estamina
	// Calculamos el coste de estamina en base al tipo de movimiento.
	public int calcularCosteEstamina() {
		if (this.tipoMovimiento.equalsIgnoreCase("ATAQUE")) {
			this.costeEstamina = this.potencia / 2;
		} else if (this.tipoMovimiento.equalsIgnoreCase("ESTADO")) {
			this.costeEstamina = this.numTurnos * 10;
		} else if (this.tipoMovimiento.equalsIgnoreCase("MEJORA")) {
			this.costeEstamina = this.numTurnos * 10;
		}
		//Se necesita para la clase pokemon

		return this.costeEstamina;
	}

	// Método toString
	@Override
	public String toString() {
		return "Movimiento [idMovimiento=" + idMovimiento + ", potencia=" + potencia + ", PP=" + PP + ", numTurnos="
				+ numTurnos + ", costeEstamina=" + costeEstamina + ", tipoMovimiento=" + tipoMovimiento
				+ ", nomMovimiento=" + nomMovimiento + ", descripMovimiento=" + descripMovimiento + ", tipoPokemon="
				+ tipoPokemon + "]";
	}

}
