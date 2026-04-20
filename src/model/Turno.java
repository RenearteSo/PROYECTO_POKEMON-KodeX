package model;

public class Turno {
	
    // opciones que podemos elegir en cada ronda
	public enum TipoAccion {
		ATACAR, MOCHILA, CAMBIO, HUIR, NADA
	}

	private int turnoActual;
	private Entrenador entrenador;
	private Entrenador rival;
	private Pokemon pokemonActivoEntrenador;
	private Pokemon pokemonActivoRival;

	private TipoAccion accionEntrenador;
	private Movimiento movEntrenador;
	private Objeto objetoEntrenador;
	private Pokemon cambioEntrenador;
	private int danioEntrenador = -1; 

	private TipoAccion accionRival;
	private Movimiento movRival;
	private Objeto objetoRival;
	private Pokemon cambioRival;
	private int danioRival = -1; 

	public Turno(int turnoActual, Entrenador entrenador, Entrenador rival) {
		this.turnoActual = turnoActual;
		this.entrenador = entrenador;
		this.rival = rival; 
		
		// Arrancamos el turno pillando qué pokemon tiene cada uno activo
		this.pokemonActivoEntrenador = buscarPokemonActivo(entrenador);
		this.pokemonActivoRival = buscarPokemonActivo(rival);
		
        // Por defecto ponemos que no hacen nada hasta que sepamos qué boton han pulsado
		this.accionEntrenador = TipoAccion.NADA;
		this.accionRival = TipoAccion.NADA;
	}

    // Buscamos en el equipo el primer pokemon que tenga vida y devolvemos su valor
	private Pokemon buscarPokemonActivo(Entrenador e) {
		if (e != null && e.getEquipoPrincipal() != null) {
			for (Pokemon p : e.getEquipoPrincipal()) {
				if (p != null && p.getVitalidad() > 0) {
					return p;
				}
			}
		}
		return null;
	}
	
    // Dejamos lista y guardada la acción que ha decidido hacer el jugador
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

    // Hacemos lo mismo para registrar lo que nos hace el rival
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

	// GETTERS Y SETTERS 

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

	public void setDanioEntrenador(int danio) { 
        this.danioEntrenador = danio; 
    }

	public void setDanioRival(int danio) { 
        this.danioRival = danio; 
    }

    public int getDanioEntrenador() { 
        return this.danioEntrenador; 
    }

    public int getDanioRival() { 
        return this.danioRival; 
    }

    // MODIFICAMOS EL TOSTRING PARA OBTENER INFORMACION MAS DETALLADA
	@Override
	public String toString() {
        
        // Protegemos por si algún pokémon ya está KO y salta null, evitamos problemas
		String nombreJugador = (entrenador != null) ? entrenador.getNombreEntrenador() : "Jugador";
		String nombreRival = (rival != null) ? rival.getNombreEntrenador() : "Rival";
		String moteJugador = (pokemonActivoEntrenador != null) ? pokemonActivoEntrenador.getMote() : "Ninguno";
		String moteRival = (pokemonActivoRival != null) ? pokemonActivoRival.getMote() : "Ninguno";

        // guardamos paso a paso la cadena de texto que se verá en la caja de la interfaz
	    StringBuilder sb = new StringBuilder();
        sb.append("=== TURNO ").append(turnoActual).append(" ===\n");
	    
        // Comprobamos qué hemos hecho nosotros y lo escribimos informando de cuánto danio real hemos hecho
	    if (accionEntrenador == TipoAccion.ATACAR && movEntrenador != null) {
	        sb.append(">>> ").append(moteJugador).append(" usa ").append(movEntrenador.getNomMovimiento()).append("!\n");
            if (danioEntrenador > 0) {
                sb.append("    ¡Quita ").append(danioEntrenador).append(" PS de vida!\n");
            } else if (danioEntrenador == 0) {
                sb.append("    ¡Pero no tuvo ningún efecto o fue bloqueado!\n");
            }
	    } else if (accionEntrenador == TipoAccion.CAMBIO && cambioEntrenador != null) {
	        sb.append(">>> ").append(nombreJugador).append(" cambia a ").append(cambioEntrenador.getMote()).append("!\n");
	    } else if (accionEntrenador == TipoAccion.NADA) {
            sb.append(">>> ").append(moteJugador).append(" ha descansado.\n");
        } else if (accionEntrenador == TipoAccion.HUIR) {
			sb.append(">>> ").append(nombreJugador).append(" ha huido del combate.\n");
		}
	    
        // Repetimos lo mismo para ver que ha hecho el rival en su turno
	    if (accionRival == TipoAccion.ATACAR && movRival != null) {
	        sb.append("<<< El rival ").append(moteRival).append(" usa ").append(movRival.getNomMovimiento()).append("!\n");
            if (danioRival > 0) {
                sb.append("    ¡Te quita ").append(danioRival).append(" PS de vida!\n");
            } else if (danioRival == 0) {
                sb.append("    ¡Pero consigues esquivarlo!\n");
            }
	    } else if (accionRival == TipoAccion.CAMBIO && cambioRival != null) {
	        sb.append("<<< ").append(nombreRival).append(" saca a ").append(cambioRival.getMote()).append("!\n");
	    } else if (accionRival == TipoAccion.NADA) {
            sb.append("<<< ").append(moteRival).append(" enemigo ha descansado.\n");
        }
	    
	    return sb.toString();
	}
}