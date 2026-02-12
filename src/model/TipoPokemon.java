package model;

//Enum de tipo pokemon 

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

	/*
	 * Método multiplicador, calcula el daño que hace un tipo de pokemon a otro tipo de pokemon.
	 * @param oponente es el tipo pokemon que recibe el golpe.
	 * @return Un numero decimal (double).
	 */

	public double multiplicador(TipoPokemon oponente) {

		
		// Switch con this que se refiere al tipo pokemon actual. (Si soy Agua entra en case Agua)
		switch (this) {
		
		// CASO 1: TIPO AGUA 
		// ¿Contra quien es fuerte AGUA?
		case AGUA:
			// Oponentes contra los que el tipo AGUA tiene ventaja, devuelven 1.5
			if (oponente == FUEGO || oponente == TIERRA || oponente == ROCA) {
				return 1.5;
			}
			//¿Contra quien es debil AGUA?
			// Oponentes que tienen ventaja sobre el tipo AGUA, devuelve 0.5
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

			// Tierra es inmune a Electrico
			if (oponente == TIERRA) {
				return 0.0;
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
			// Fantasma no afecta a normal ni a psiquico.
			if (oponente == NORMAL || oponente == PSIQUICO) {
				return 0.0;
			}
			break;

		// CASO 6: TIPO FUEGO
		case FUEGO:
			if (oponente == BICHO || oponente == HIELO || oponente == PLANTA) {
				return 1.5;
			}
			if (oponente == AGUA || oponente == DRAGON || oponente == FUEGO || oponente == ROCA) {
				return 0.5;
			}
			break;

		// CASO 7: TIPO HIELO
		case HIELO:
			if (oponente == DRAGON || oponente == PLANTA || oponente == TIERRA || oponente == VOLADOR) {
				return 1.5;
			}
			if (oponente == AGUA || oponente == HIELO || oponente == FUEGO) {
				return 0.5;
			}
			break;

		// CASO 8: TIPO LUCHA
		case LUCHA:
			if (oponente == HIELO || oponente == NORMAL || oponente == ROCA) {
				return 1.5;
			}
			if (oponente == BICHO || oponente == PSIQUICO || oponente == VENENO || oponente == VOLADOR) {
				return 0.5;
			}
			if (oponente == FANTASMA) {
				return 0.0;
			}
			break;

		// CASO 9: TIPO NORMAL
		case NORMAL:
			if(oponente == ROCA) {
				return 0.5;
			}
			if(oponente == FANTASMA) {
				return 0.0;
			}
			break;
			
		// CASO 10: TIPO PLANTA
		case PLANTA:
			if(oponente == AGUA || oponente == ROCA || oponente == TIERRA) {
				return 1.5;
			}
			if(oponente == BICHO || oponente == DRAGON || oponente == FUEGO || oponente == PLANTA || oponente == VENENO || oponente == VOLADOR) {
				return 0.5;
			}
			break;	

		// CASO 11: TIPO PSIQUICO
		case PSIQUICO:
			if(oponente == LUCHA || oponente == VENENO) {
				return 1.5;
			}
			if(oponente == PSIQUICO) {
				return 0.5;
			}
			break;
		
		// CASO 12: TIPO ROCA
		case ROCA:
			if(oponente == BICHO || oponente == FUEGO || oponente == HIELO || oponente == VOLADOR) {
				return 1.5;
			}
			if (oponente == LUCHA || oponente == TIERRA) {
				return 0.5;
			}
			break;
		
		// CASO 13: TIPO TIERRA
		case TIERRA:
			if(oponente == ELECTRICO || oponente == FUEGO || oponente == ROCA || oponente == VENENO) {
				return 1.5;
			}
			if(oponente == BICHO || oponente == PLANTA) {
				return 0.5;
			}
			if(oponente == VOLADOR) {
				return 0.0;
			}
			break;

		// CASO 14: TIPO VENENO
		case VENENO:
			if(oponente == BICHO || oponente == PLANTA) {
				return 1.5;
			}
			if(oponente == FANTASMA || oponente == ROCA || oponente == TIERRA || oponente == VENENO) {
				return 0.5;
			}
			break;
			
		// CASO 15: TIPO VOLADOR
		case VOLADOR:
			if(oponente == BICHO || oponente == LUCHA || oponente == PLANTA) {
				return 1.5;
			}
			if(oponente == ELECTRICO || oponente == ROCA) {
				return 0.5;
			}
			break;
		}

		return 1.0;

	}

	/*
	 * Calculamos el multiplicador cuando el oponente tiene dos tipos pokemon.
	 * @param oponente1 Primer tipo pokemon del rival.
	 * @param oponente2 Segundo tipo pokemon del rival (null en caso de que no tenga).
	 * @return Resultado del multiplicador
	 */
	
	public double multiplicador(TipoPokemon oponente1, TipoPokemon oponente2) {

		// Calculamos como afecta un ataque al primer tipo pokemon.
		double m1 = multiplicador(oponente1);

		// Variable para el segundo tipo pokemon, asumiendo que es neutro.
		double m2 = 1.0;
	
		// Si el segundo tipo pokemon existe no es null.
		if(oponente2 != null) {
			// Calculamos como afecta el ataque a el segundo tipo pokemon.
			m2 = multiplicador(oponente2);
		}
		// Multiplicamos ambos resultados.
		return m1 * m2;
	}

	/*
	 * @param oponente1 Primer tipo pokemon del rival.
	 * @param oponente2 Segundo tipo pokemon del rival.
	 * @return "VENTAJA", "DESVENTAJA", "DOBLE VENTAJA" o "NEUTRO".
	 */

	public String obtenerVentaja(TipoPokemon oponente1, TipoPokemon oponente2) {

		// Obtenemos el numero
		double multi = multiplicador(oponente1, oponente2);

		//Ventaja que corresponde a cada numero.
		if(multi >= 2.0) {
			// Si es 2.25 o más (resultado de 1.5 * 1.5), es DOBLE VENTAJA
			return "DOBLE VENTAJA";

		} else if (multi > 1.0) {
			// Si es mayor que 1 es ventaja simple.
			return "VENTAJA";

		} else if (multi < 1.0) {
			// Si es menor que 1.0 es desventaja.
			return "DESVENTAJA";

		} else {
			// Si es exactamente 1.0 es neutro.
			return "NEUTRO";
		}
	} 
}