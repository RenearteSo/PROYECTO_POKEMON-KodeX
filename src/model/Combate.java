package model;

import java.util.LinkedList;

public class Combate {

    private Entrenador jugador;
    private Entrenador rival;
    private int contadorTurnos; 
    private int koJugador; 
    private int koRival;   
    private Entrenador ganador; 
    private Entrenador perdedor; 
    private LinkedList<Turno> historialTurnos; 
    private Log logCombate;
    
    private int totalDineroGanado = 0;
    private int totalExpGanada = 0;

    // constructor con entrenador y rival
 	public Combate(Entrenador jugador, Entrenador rival) {
 		this.jugador = jugador;
 		this.rival = rival;
 		this.historialTurnos = new LinkedList<Turno>();
 		this.ganador = null;
 		this.perdedor = null;
 		this.contadorTurnos = 1; 
 		this.koJugador = 0;      
 		this.koRival = 0;
        this.logCombate = new Log();
 	}

    // METÓDOS GET Y SET
	public Entrenador getGanador() { 
        return ganador; 
    }

	public Entrenador getPerdedor() { 
        return perdedor; 
    }

	public int getContadorTurnos() { 
        return contadorTurnos; 
    }

	public LinkedList<Turno> getHistorialTurnos() { 
        return historialTurnos; 
    }

	public int getKoJugador() { 
        return koJugador; 
    }

	public int getKoRival() { 
        return koRival; 
    }

    public int getTotalDineroGanado() { 
        return totalDineroGanado; 
    }

    public int getTotalExpGanada() { 
        return totalExpGanada; 
    }
    
    // método para ejecutar el turno
    public void ejecutarTurno (Turno t) {
    	
    	// obtenemos los respectivos pokemon
    	Pokemon pJugador = t.getPokemonActivoEntrenador();
    	Pokemon pRival = t.getPokemonActivoRival();
    	
        // si alguno no tiene pokemon
    	if (pJugador == null || pRival == null) {
    		comprobarVictoria();
    		return;
    	}
    	
        if (this.contadorTurnos == 1) {
            logCombate.registrarAccion("inicioCombate", pJugador, pRival, jugador, rival, contadorTurnos);
        }
    	
    	boolean jugadorAtaca = (t.getAccionEntrenador() == Turno.TipoAccion.ATACAR);
    	boolean rivalAtaca = (t.getAccionRival() == Turno.TipoAccion.ATACAR);
    	
        // primero lo que no son ataques
    	if (!jugadorAtaca) resolverAccion(t.getAccionEntrenador(), jugador, pJugador, pRival, t);
    	if (!rivalAtaca) resolverAccion(t.getAccionRival(), rival, pRival, pJugador, t);
    			
    	// si el ganador ya no es null, el combate ha acabado
    	if (ganador != null) {
            if (ganador == jugador) logCombate.registrarAccion("finGanaCombate", pJugador, pRival, jugador, rival, contadorTurnos);
            else logCombate.registrarAccion("finPierdeCombate", pJugador, pRival, jugador, rival, contadorTurnos);
            logCombate.guardarFichero();
    		repartirRecompensas(pJugador, pRival);
    		historialTurnos.add(t);
    		return; 
    	}
    	
        // comprobamos quien ataca primero basandonos en la velocidad
    	if (jugadorAtaca && rivalAtaca) {
    		int velJugador = pJugador.getVelocidad();
    		int velRival = pRival.getVelocidad();
    		
    		if (pJugador.getEstadoActual() == Estado.PARALIZADO) velJugador = velJugador / 4;
    		if (pRival.getEstadoActual() == Estado.PARALIZADO) velRival = velRival / 4;
    		
    		if (velJugador >= velRival) {
    			resolverAccion(Turno.TipoAccion.ATACAR, jugador, pJugador, pRival, t);
    			if (pRival.getVitalidad() > 0) {
    				resolverAccion(Turno.TipoAccion.ATACAR, rival, pRival, pJugador, t);
    			}
    		} else {
    			resolverAccion(Turno.TipoAccion.ATACAR, rival, pRival, pJugador, t);
    			if (pJugador.getVitalidad() > 0) {
    				resolverAccion(Turno.TipoAccion.ATACAR, jugador, pJugador, pRival, t);
    			} 
    		}
    	}
    	else if (jugadorAtaca && pJugador.getVitalidad() > 0) {
    		resolverAccion(Turno.TipoAccion.ATACAR, jugador, pJugador, pRival, t);	
    	}
    	else if (rivalAtaca && pRival.getVitalidad() > 0) {
    		resolverAccion(Turno.TipoAccion.ATACAR, rival, pRival, pJugador, t);
    	}
    	
        // quitamos vida residual si hay estados alterados
    	aplicarEfectosFinDeTurno(pJugador, pRival);
		aplicarEfectosFinDeTurno(pRival, pJugador);
		
		comprobarDebilitados(pJugador, pRival);
		comprobarVictoria(); 
		
        // si termina el combate guardamos el log y damos recompensas
		if (ganador != null) {
            if (ganador == jugador) logCombate.registrarAccion("finGanaCombate", pJugador, pRival, jugador, rival, contadorTurnos);
            else logCombate.registrarAccion("finPierdeCombate", pJugador, pRival, jugador, rival, contadorTurnos);
            logCombate.guardarFichero(); 
			repartirRecompensas(pJugador, pRival); 
		}
		
		historialTurnos.add(t);
		contadorTurnos++;
    } 
    
    // ejecuta la accion seleccionada por el jugador o el rival
    private void resolverAccion(Turno.TipoAccion accion, Entrenador entrenador, Pokemon actuante, Pokemon enemigo, Turno t) {
    	if (actuante.getVitalidad() <= 0) return; 
    	
    	switch(accion) {
    		case ATACAR:
    			Movimiento mov = (entrenador == jugador) ? t.getMovEntrenador() : t.getMovRival();
    			if (puedeMoverse(actuante)) {
    				actuante.atacar(enemigo, mov);
    			}
    			break;
    		case MOCHILA: break;
    		case CAMBIO:
    			if (actuante.getEstadoActual() == Estado.ATRAPADO) return;	
                if (entrenador == jugador) logCombate.registrarAccion("cambio1", actuante, enemigo, jugador, rival, t.getTurnoActual());
                else logCombate.registrarAccion("cambio2", enemigo, actuante, jugador, rival, t.getTurnoActual());
    			break;
    		case HUIR:
    			if (actuante.getEstadoActual() == Estado.ATRAPADO) return;
    			this.perdedor = entrenador;
				this.ganador = (entrenador == jugador) ? rival : jugador;
				break;
			default: break;
        }
    }  
    
    // valida si un pokemon puede hacer una accion dependiendo de su estado
    private boolean puedeMoverse(Pokemon p) {
    	if(p.getEstadoActual() == Estado.DORMIDO) return false;
    	if(p.getEstadoActual() == Estado.CONGELADO) return false;
    	if(p.getEstadoActual() == Estado.PARALIZADO && Math.random() < 0.25) return false;
    	if(p.getEstadoActual() == Estado.ENAMORADO && Math.random() < 0.50) return false;
    	if(p.getEstadoActual() == Estado.SOMNOLIENTO && Math.random() < 0.33) return false;
    	if(p.getEstadoActual() == Estado.AMEDRENTADO) { p.setEstadoActual(Estado.SALUDABLE); return false; }
    	if(p.getEstadoActual() == Estado.CONFUSO) {
    		if (Math.random() < 0.50) {
				int dañoConfusion = (40 + p.ataqueObjetoAplicado()) - p.defensaObjetoAplicado();
				if (dañoConfusion < 1) dañoConfusion = 1;
				p.setVitalidad(p.getVitalidad() - dañoConfusion);
				return false;
    		}
    	}
    	return true; 
    }
    
    // aplica efectos residuales
    private void aplicarEfectosFinDeTurno(Pokemon p, Pokemon enemigo) {
		if (p == null || p.getVitalidad() <= 0) return;
		int danioResidual = 0;
		switch(p.getEstadoActual()) {
			case ENVENENADO:
			case QUEMADO:
			case HELADO:
				danioResidual = p.getVitalidad() / 16;
				if (danioResidual < 1) danioResidual = 1; 
				p.setVitalidad(p.getVitalidad() - danioResidual);
				break;
			case GRAVEMENTE_ENVENENADO:
				p.setTurnosEstado(p.getTurnosEstado() + 1); 
				danioResidual = (p.getVitalidad() / 16) * p.getTurnosEstado();
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual);
				break;
			case MALDITO:
				danioResidual = p.getVitalidad() / 4; 
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual);
				break;
			case ATRAPADO:
				danioResidual = p.getVitalidad() / 16;
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual);
				break;
			case DRENADORAS:
				danioResidual = p.getVitalidad() / 16;
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual); 
				if (enemigo != null && enemigo.getVitalidad() > 0) enemigo.setVitalidad(enemigo.getVitalidad() + danioResidual);
				break;
			case CANTO_MORTAL:
				p.setTurnosEstado(p.getTurnosEstado() + 1);
				if (p.getTurnosEstado() >= 3) p.setVitalidad(0); 
				break;
			default: break;
		}
    }
    
    // si esta debilitado, y lo logeamos
    private void comprobarDebilitados(Pokemon pJ, Pokemon pR) {
    	if (pJ != null && pJ.getVitalidad() <= 0 && pJ.getEstadoActual() != Estado.DEBILITADO) {
			pJ.setEstadoActual(Estado.DEBILITADO);
			koJugador++; 
            logCombate.registrarAccion("debilitado1", pJ, pR, jugador, rival, contadorTurnos);
        }
    	if (pR != null && pR.getVitalidad() <= 0 && pR.getEstadoActual() != Estado.DEBILITADO) {
			pR.setEstadoActual(Estado.DEBILITADO);
			koRival++; 
            logCombate.registrarAccion("debilitado2", pJ, pR, jugador, rival, contadorTurnos);
        }
    }
    
    // mira si nos queda algun pokemon vivo en la reserva
    private boolean equipoVivo(Entrenador ent) {
        if (ent == null || ent.getEquipoPrincipal() == null) return false;
        for (Pokemon p : ent.getEquipoPrincipal()) {
            if (p != null && p.getVitalidad() > 0) return true;
        }
        return false;
    }
    
    // comprobamos los KO en este metodo para dar la victoria
    private void comprobarVictoria() {
		if (koRival >= 6 || !equipoVivo(rival)) {
			this.ganador = jugador;
			this.perdedor = rival;
		} else if (koJugador >= 6 || !equipoVivo(jugador)) {
			this.ganador = rival;
			this.perdedor = jugador;
		} 
    }
    
    // calcula la experiencia, resta el dinero si pierdes y lo suma si ganas
    private void repartirRecompensas(Pokemon pJugador, Pokemon pRival) {
		int dineroRobado = perdedor.getPokedollar() / 3;
		perdedor.setPokedollar(perdedor.getPokedollar() - dineroRobado); 
		ganador.setPokedollar(ganador.getPokedollar() + dineroRobado);   
		
        if (ganador == jugador) {
            this.totalDineroGanado = dineroRobado;
        }

		if (ganador == jugador && pJugador != null && pJugador.getVitalidad() > 0 && pRival != null) {
			int expGanada = ((pJugador.getNivel() + (pRival.getNivel() * 10)) / 4) * Math.max(1, koRival);
			pJugador.setExp(pJugador.getExp() + expGanada);
            this.totalExpGanada = expGanada;
			pJugador.subirNivel();
		} 
		else if (ganador == rival && pRival != null && pRival.getVitalidad() > 0 && pJugador != null) {
			int expGanada = (pRival.getNivel() + (pJugador.getNivel() * 10)) / 4;
			pRival.setExp(pRival.getExp() + expGanada);
			pRival.subirNivel();
		} 
	}


}