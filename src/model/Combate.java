package model;

import java.util.LinkedList;

/**
 * La Clase Combate gestiona la lógica de un enfrentamiento entre dos entrenadores,
 * controla los turnos, el cálculo de daño, las alteraciones de estado,
 * el registro en el log y las recompensas (experiencia y dinero),
 * incluyendo las reglas especiales de la Liga Pokémon.
 */

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
    // Atributos para la lógica de la Liga Pókemon
    private boolean esLiga = false;
    private int numCombateLiga = 1;
    private boolean descansoPrevioUsado = false;

    /**
     * Constructor principal para iniciar un combate.
     * Aquí se inicializan los marcadores de KO, el historial y el sistema de log.
     * @param jugador Entrenador principal (el que controal el usuario).
     * @param rival Entrenador oponente.
     */
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
    /**
     * Metodo para obtener el ganador
     * @return El entrenador ganador.
     */
	public Entrenador getGanador() { 
        return ganador; 
    }

    /**
     * Metodo para obtener el entrenador del perdedor
     * @return El entrenador perdedor.
     */
	public Entrenador getPerdedor() { 
        return perdedor; 
    }

    /**
     * Metodo para obtener el contador de turnos
     * @return Número de turno actual.
     */
	public int getContadorTurnos() { 
        return contadorTurnos; 
    }

    /**
     * Metodo para obtener el historial de turnos en LinkedList
     * @return Lista de turnos jugados.
     */
	public LinkedList<Turno> getHistorialTurnos() { 
        return historialTurnos; 
    }

    /**
     * Metodo para obtener los KOs del jugador
     * @return KOs sufridos por el jugador.
     */
	public int getKoJugador() { 
        return koJugador; 
    }

    /**
     * Metodo para obtener los KO dekl rival
     * @return KOs sufridos por el rival.
     */
	public int getKoRival() { 
        return koRival; 
    }

    /**
     * Metodo para obtener el dinero ganado total
     * @return Cantidad de dinero ganada.
     */
    public int getTotalDineroGanado() { 
        return totalDineroGanado; 
    }

    /**
     * Metodo para obtener la experiencia total ganada
     * @return Cantidad de experiencia ganada.
     */
    public int getTotalExpGanada() { 
        return totalExpGanada; 
    }
    
    /**
     * Configuración para activar las reglas de la Liga Pókemon.
     * @param numCombate Numero del combate dentro de la Liga (1-5).
     * @param descansoUsado Indica si el jugador descansó antes.
     */
    public void configurarComoLiga(int numCombate, boolean descansoUsado) {
        this.esLiga = true;
        this.numCombateLiga = numCombate;
        this.descansoPrevioUsado = descansoUsado;
    }
    
    /**
     * Lógica completa de los turnos en el combate
     * Gestiona la prioridad de movimiento que se basa en la velocidad y estados.
     * Resuelve las acciones, aplica daño residual y comprueba la victoria.
     * @param t Turno Objeto que contiene las decisiones que toman ambos jugadores.
     */
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
    
    /**
     * Procesa la acción como el Ataque, Cambio, Huida.
     * En caso de ataque, se calcula el daño final basandose en el nivel, potencia, estadística,
     * y los multiplicadores elementales.
     * @param accion Tipo de acción que se va a realizar.
     * @param entrenador Entrenador que ejecuta la acción.
     * @param actuante Pokemon que ejecuta la accion.
     * @param enemigo Pokemon que recibe la acción.
     * @param t Turno actual para registrar el daño.
     */
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

    /**
     * Metodo auxiliar para calcular la efectividad de un tipo contra otro. 
     * @param tipoAtaque Tipo del movimiento ejecutado.
     * @param tipoDefensa Tipo del Pokemon defensor.
     * @return Multiplicador de danio, 2.0 Eficaz, 0.5 Resistida, 0.0 Inmune y 1.0 Neutro.
     */
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
    
    /**
     * Verificamos si un Pókemon tiene permitido actuar según el estado en el que se encuentra actualmente.
     * Gestiona las probabilidades de parálisis, confusión y sueño.
     * @param p Pokemon a comprobar.
     * @return true si puede moverse, false si no puede.
     */
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
    
    /**
     * Aplica los daños o curaciones al finalizar cada turno según el estado en el que se encuentre.
     * @param p Pokemon afectado.
     * @param enemigo Pokemon rival.
     */
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
    
    /**
     * Comprueba si los puntos de salud de los Pókemons han llegado a cero.
     * Actualiza el estado Debilitado e incrementa los contadores de KO.
     * @param pJ Pokemon del jugador.
     * @param pR Pokemon del rival.
     */
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
    
    /**
     * Verifica si un entrenador aún tiene Pókemons para seguir luchando-
     * @param ent Entrenador a revisar.
     * @return true si tiene al menos un Pókemon en el equipo principal con la Vitalidad mayor que cero.
     */
    private boolean equipoVivo(Entrenador ent) {
        if (ent == null || ent.getEquipoPrincipal() == null) return false;
        for (Pokemon p : ent.getEquipoPrincipal()) {
            if (p != null && p.getVitalidad() > 0) return true;
        }
        return false;
    }
    
    /**
     * Comprobamos los KO en este metodo para determinar la victoria
     */
    private void comprobarVictoria() {
		if (koRival >= 6 || !equipoVivo(rival)) {
			this.ganador = jugador;
			this.perdedor = rival;
		} else if (koJugador >= 6 || !equipoVivo(jugador)) {
			this.ganador = rival;
			this.perdedor = jugador;
		} 
    }
    
    /**
     * Ejecuta las transacciones económcias y la subida de experiencia.
     * En la Liga, el dinero que se gana depende del uso del descanso y el número de combate.
     * En los combates normales, se da un tercio del dinero del perdedor al ganador.
     * @param pJugador Pokemon actual del jugador para dar la experiencia..
     * @param pRival Pokemon actual del rival para calcular la base de experiencia.
     */
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