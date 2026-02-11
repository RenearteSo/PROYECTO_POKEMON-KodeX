package pokemon;

public enum TipoPokemon {
	AGUA,
	BICHO,
	DRAGON,
	ELECTRICO,
	FANTASMA,
	FUEGO, 
    HIELO,
    LUCHA, 
    NORMAL, 
    PLANTA, 
    PSIQUICO, 
    ROCA, 
    TIERRA,
    VENENO, 
    VOLADOR;
	
	//Método multiplicador, calcula el daño que hace un tipo de pokemon a otro tipo de pokemon.
	public double multiplicador(TipoPokemon oponente) {
		
		switch (this) {
		// CASO 1: TIPO AGUA 
		case AGUA:
			// Oponentes que tienen ventaja sobre el tipo AGUA, devuelven 1.5
			if (oponente == FUEGO || oponente == TIERRA || oponente == ROCA) {
				return 1.5;
			}
			// Oponentes que tienen desventaja sobre el tipo AGUA, devuelve 0.5
			if (oponente == AGUA || oponente == PLANTA || oponente == DRAGON) {
				return 0.5;
			}
			break; // Fin del caso tipo AGUA
		
		// CASO 2: TIPO BICHO
		case BICHO:
			if (oponente == PLANTA || oponente == PSIQUICO || oponente == VENENO) {
				return 1.5;
			}
			if (oponente == FANTASMA || oponente == FUEGO || oponente == LUCHA || oponente == VOLADOR) {
				return 0.5;
			}
			break;
		
		// CASO 3: TIPO DRAGON
		case DRAGON:
			if (oponente == DRAGON) {
				return 1.5;
			}
			break;
		
		// CASO 4 : TRIPO ELECTRICO
		case ELECTRICO:
			if (oponente == AGUA || oponente == VOLADOR) {
				return 1.5;
			}
			if (oponente == TIERRA) {
				return 0;
			}
			if (oponente == DRAGON || oponente == ELECTRICO || oponente == PLANTA) {
				return 0.5;
			}
			break;
		
		// CASO 5: TIPO FANTASMA
		case FANTASMA:
			if (oponente == FANTASMA) {
				return 1.5;
			}
			if (oponente == NORMAL || oponente == PSIQUICO) {
				return 0;
			}
			break;
		
		// CASO 6: TIPO FUEGO
		case FUEGO:
			if (oponente == )
			
		}
	}
}
