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
    
 // LIGA POKEMON
    private boolean esLiga = false;
    private int numCombateLiga = 1;
    private boolean descansoPrevioUsado = false;

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
    
 // configurar la liga pokemon
    public void configurarComoLiga(int numCombate, boolean descansoUsado) {
        this.esLiga = true;
        this.numCombateLiga = numCombate;
        this.descansoPrevioUsado = descansoUsado;
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
                
                if (actuante.getEstamina() >= mov.getCosteEstamina()) {
                    actuante.setEstamina(actuante.getEstamina() - mov.getCosteEstamina());

                    // Cálculo de efectividad y STAB
                    double multiplicador = 1.0;
                    if (actuante.getTipoPokemon().contains(mov.getTipoPokemon())) multiplicador *= 1.5;
                    
                    if (enemigo.getTipoPokemon() != null) {
                        for (TipoPokemon tp : enemigo.getTipoPokemon()) {
                            multiplicador *= calcularEfectividad(mov.getTipoPokemon(), tp);
                        }
                    }

                    if (mov.getClaseMovimiento().equalsIgnoreCase("ataque")) {
                        // Fórmula de daño equilibrada y segura
                        double ataqueActuante = actuante.ataqueObjetoAplicado();
                        double defensaEnemigo = Math.max(1, enemigo.defensaObjetoAplicado());
                        
                        int danioBase = (int) ((( (2 * actuante.getNivel() / 5.0 + 2) * mov.getPotencia() * (ataqueActuante / defensaEnemigo) ) / 50) + 2);
                        int danioFinal = (int) (danioBase * multiplicador);
                        
                        if (multiplicador == 0) danioFinal = 0; // Inmunidad
                        
                        // Aplicamos el daño al enemigo actual
                        enemigo.setVitalidad(Math.max(0, enemigo.getVitalidad() - danioFinal));
                        
                        if (entrenador == jugador) t.setDanioEntrenador(danioFinal);
                        else t.setDanioRival(danioFinal);
                        
                    } else if (mov.getClaseMovimiento().equalsIgnoreCase("estado")) {
                        enemigo.setEstadoActual(mov.getEstado());
                    }
                }
                break;
            case CAMBIO:
                // El cambio tiene prioridad y no debe procesar daño contra el pokemon que sale
                break;
            case HUIR:
                this.perdedor = entrenador;
                this.ganador = (entrenador == jugador) ? rival : jugador;
                break;
            default: break;
        }
    }

    // Método auxiliar para calcular la efectividad de un tipo contra otro (Tabla Gen 1)
    private double calcularEfectividad(TipoPokemon tipoAtaque, TipoPokemon tipoDefensa) {
        if (tipoAtaque == null || tipoDefensa == null) return 1.0;
        
        switch (tipoAtaque) {
            case NORMAL:
                if (tipoDefensa == TipoPokemon.ROCA) return 0.5;
                if (tipoDefensa == TipoPokemon.FANTASMA) return 0.0;
                break;
            case FUEGO:
                if (tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.BICHO || tipoDefensa == TipoPokemon.HIELO) return 2.0;
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.AGUA || tipoDefensa == TipoPokemon.ROCA || tipoDefensa == TipoPokemon.DRAGON) return 0.5;
                break;
            case AGUA:
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.TIERRA || tipoDefensa == TipoPokemon.ROCA) return 2.0;
                if (tipoDefensa == TipoPokemon.AGUA || tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.DRAGON) return 0.5;
                break;
            case PLANTA:
                if (tipoDefensa == TipoPokemon.AGUA || tipoDefensa == TipoPokemon.TIERRA || tipoDefensa == TipoPokemon.ROCA) return 2.0;
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.VENENO || tipoDefensa == TipoPokemon.VOLADOR || tipoDefensa == TipoPokemon.BICHO || tipoDefensa == TipoPokemon.DRAGON) return 0.5;
                break;
            case ELECTRICO:
                if (tipoDefensa == TipoPokemon.AGUA || tipoDefensa == TipoPokemon.VOLADOR) return 2.0;
                if (tipoDefensa == TipoPokemon.ELECTRICO || tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.DRAGON) return 0.5;
                if (tipoDefensa == TipoPokemon.TIERRA) return 0.0;
                break;
            case HIELO:
                if (tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.TIERRA || tipoDefensa == TipoPokemon.VOLADOR || tipoDefensa == TipoPokemon.DRAGON) return 2.0;
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.AGUA || tipoDefensa == TipoPokemon.HIELO) return 0.5;
                break;
            case LUCHA:
                if (tipoDefensa == TipoPokemon.NORMAL || tipoDefensa == TipoPokemon.HIELO || tipoDefensa == TipoPokemon.ROCA) return 2.0;
                if (tipoDefensa == TipoPokemon.VENENO || tipoDefensa == TipoPokemon.VOLADOR || tipoDefensa == TipoPokemon.PSIQUICO || tipoDefensa == TipoPokemon.BICHO) return 0.5;
                if (tipoDefensa == TipoPokemon.FANTASMA) return 0.0;
                break;
            case VENENO:
                if (tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.BICHO) return 2.0;
                if (tipoDefensa == TipoPokemon.VENENO || tipoDefensa == TipoPokemon.TIERRA || tipoDefensa == TipoPokemon.ROCA || tipoDefensa == TipoPokemon.FANTASMA) return 0.5;
                break;
            case TIERRA:
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.ELECTRICO || tipoDefensa == TipoPokemon.VENENO || tipoDefensa == TipoPokemon.ROCA) return 2.0;
                if (tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.BICHO) return 0.5;
                if (tipoDefensa == TipoPokemon.VOLADOR) return 0.0;
                break;
            case VOLADOR:
                if (tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.LUCHA || tipoDefensa == TipoPokemon.BICHO) return 2.0;
                if (tipoDefensa == TipoPokemon.ELECTRICO || tipoDefensa == TipoPokemon.ROCA) return 0.5;
                break;
            case PSIQUICO:
                if (tipoDefensa == TipoPokemon.LUCHA || tipoDefensa == TipoPokemon.VENENO) return 2.0;
                if (tipoDefensa == TipoPokemon.PSIQUICO) return 0.5;
                break;
            case BICHO:
                if (tipoDefensa == TipoPokemon.PLANTA || tipoDefensa == TipoPokemon.PSIQUICO || tipoDefensa == TipoPokemon.VENENO) return 2.0;
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.LUCHA || tipoDefensa == TipoPokemon.VOLADOR || tipoDefensa == TipoPokemon.FANTASMA) return 0.5;
                break;
            case ROCA:
                if (tipoDefensa == TipoPokemon.FUEGO || tipoDefensa == TipoPokemon.HIELO || tipoDefensa == TipoPokemon.VOLADOR || tipoDefensa == TipoPokemon.BICHO) return 2.0;
                if (tipoDefensa == TipoPokemon.LUCHA || tipoDefensa == TipoPokemon.TIERRA) return 0.5;
                break;
            case FANTASMA:
                if (tipoDefensa == TipoPokemon.FANTASMA) return 2.0;
                if (tipoDefensa == TipoPokemon.NORMAL || tipoDefensa == TipoPokemon.PSIQUICO) return 0.0;
                break;
            case DRAGON:
                if (tipoDefensa == TipoPokemon.DRAGON) return 2.0;
                break;
            default:
                return 1.0;
        }
        return 1.0;
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
    
    // calcula la experiencia, resta el dinero si pierdes y lo suma si ganas, incluyendo la liga pokemon
    private void repartirRecompensas(Pokemon pJugador, Pokemon pRival) {
        
        // LÓGICA DE DINERO
        if (esLiga) {
            if (ganador == jugador) {
                // premios específicos de la Liga Pokémon
                int premio = 0;
                switch (numCombateLiga) {
                    case 1: premio = 1000; break;
                    case 2: premio = descansoPrevioUsado ? 1000 : 2000; break;
                    case 3: premio = descansoPrevioUsado ? 2000 : 4000; break;
                    case 4: premio = descansoPrevioUsado ? 3000 : 6000; break;
                    case 5: premio = descansoPrevioUsado ? 4000 : 8000; break;
                }
                ganador.setPokedollar(ganador.getPokedollar() + premio);
                this.totalDineroGanado = premio;
            } else if (ganador == rival) {
                // si pierdes en la liga, pierdes la mitad de TODOS tus ahorros
                int penalizacion = perdedor.getPokedollar() / 2;
                perdedor.setPokedollar(perdedor.getPokedollar() - penalizacion);
            }
        } else {
            // Lógica normal de combate aleatorio (se roba 1/3)
            int dineroRobado = perdedor.getPokedollar() / 3;
            perdedor.setPokedollar(perdedor.getPokedollar() - dineroRobado); 
            ganador.setPokedollar(ganador.getPokedollar() + dineroRobado);   
            if (ganador == jugador) {
                this.totalDineroGanado = dineroRobado;
            }
        }

        // LOGICA DE EXPERIENCIA
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