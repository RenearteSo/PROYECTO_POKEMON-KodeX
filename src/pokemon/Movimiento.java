package pokemon;

public class Movimiento {
	private int idMovimiento, potencia, PP, numTurnos, costeEstamina;
	//Clase movimiento si es de: mejora, estado, ataque.
	private String tipoMovimiento;
	private String nomMovimiento;
	private String descripMovimiento;
	private TipoPokemon tipoPokemon; // cogemos el tipo de la clase
	
	
	// Constructor por defecto
	public Movimiento() {
		super();
		this.idMovimiento = 0;
		this.potencia = 0;
		PP = 0;
		this.numTurnos = 0;
		this.costeEstamina = 0;
		this.tipoMovimiento = "";
		this.nomMovimiento = "";
		this.descripMovimiento = "";
		this.tipoPokemon = TipoPokemon.NORMAL;
	}

	
	public Movimiento(int idMovimiento, int potencia, int pP, int numTurnos, int costeEstamina, String tipoMovimiento,
			String nomMovimiento, String descripMovimiento, TipoPokemon tipoPokemon) {
		super();
		this.idMovimiento = idMovimiento;
		this.potencia = potencia;
		PP = pP;
		this.numTurnos = numTurnos;
		this.costeEstamina = costeEstamina;
		this.tipoMovimiento = tipoMovimiento;
		this.nomMovimiento = nomMovimiento;
		this.descripMovimiento = descripMovimiento;
		this.tipoPokemon = tipoPokemon;
	}
	
	

	
	
	
}
