package model;

import java.util.LinkedList;

public class Combate {
	
	// TAREAS POR HACER EN LA CLASE COMBATE:
	// comprobar si turno es tipo turno o un numero (deberia ser int creo pero comprobar cuando se haga la clase)
	// todos los metodos del combate
	// LOG (registro del combate, puede ir aqui o en clase Turno)

	
    private Entrenador jugador;
    private Entrenador rival;
    private int contadorTurnos; // Empieza en 1. Sirve para saber si vamos por la ronda 1, 2, 3...
    
    private int koJugador; // pokemons debilitados NUESTROS
    private int koRival;   // pokemons debilitados del ENEMIGO
    
    private Entrenador ganador; // Aquí guardamos el nombre del que gana al final.
    
    private Entrenador perdedor; // Lo guardamos para poder quitarle su dinero como castigo.
    private LinkedList<Turno>historialTurnos; // Una lista para guardar todo el texto de la batalla y exportarlo después.
    
 // Constructor: Se ejecuta justo antes de empezar la pelea para preparar todo.
 	public Combate(Entrenador jugador, Entrenador rival) {
 		this.jugador = jugador;
 		this.rival = rival;
 		this.historialTurnos = new LinkedList<Turno>();
 		this.ganador = null;
 		this.perdedor = null;
 		this.contadorTurnos = 1; // El combate empieza en la ronda 1
 		this.koJugador = 0;      // Nadie ha muerto todavía
 		this.koRival = 0;
 	}

    // el combate finaliza si alguno de los dos ha tumbado 6 pokemon, hacer metodo

    // ----MÉTODO PRINCIPLAR QUE EJECUTA LOS TURNOS----
    // Este método se llamará desde el Controlador de JavaFX cada vez que le demos a Atacar.
    
    public void ejecutarTurno (Turno t) {
    	
    	// Lee que pokemon esta luchando en este turno
    	Pokemon pJugador = t.getPokemonActivoEntrenador();
    	Pokemon pRival = t.getPokemonActivoRival();
    	
    	// Si alguno no tiene pokemon vivos termina el turno.
    	if (pJugador == null || pRival == null) {
    		comprobarVictoria();
    		return;
    	}
    	
    	// AQUI SE REGISTRA LO DEL LOG, INICIO DEL TURNO X (RIVAL O JUGADOR) y el contador de turnos
    	
    	// Miramos que ha decidido hacer cada jugador en este turno (Atacar, cambiar, etc.)
    	boolean jugadorAtaca = (t.getAccionEntrenador() == Turno.TipoAccion.ATACAR);
    	boolean rivalAtaca = (t.getAccionRival() == Turno.TipoAccion.ATACAR);
    	
    	// ---- PRIMERA SECCIÓN ----
    	// ACCIONES DE PRIORIDAD ALTA (Usar mochila, cambiar pokemon o huir)
    			// Curarse o cambiar de pokemon siempre es más rapido que atacar.
    	
    			if (!jugadorAtaca) { // Si el jugador ha elegido mochila o cambio...
    				resolverAccion(t.getAccionEntrenador(), jugador, pJugador, pRival, t);
    			}
    			if (!rivalAtaca) { // Si el rival ha elegido mochila o cambio...
    				resolverAccion(t.getAccionRival(), rival, pRival, pJugador, t);
    			}
    			
    			// Si un jugador ha elegido HUIR, el combate se termina al instante.
    			if (ganador != null) {
    				// LOG: COMBATE FINALIZADO POR RETIRADA!!!
    				repartirRecompensas(pJugador, pRival);
    				historialTurnos.add(t);
    				return; // Cortamos el método para que no siga leyendo
    			}
    	
    	// ---- SEGUNDA SECCIÓN ----
    	// RESOLUCIÓN DE LOS ATAQUES (VELOCIDAD)
    	// CASO 1: Ambos jugadores presionan el botón de ATACAR.
    	if (jugadorAtaca && rivalAtaca) {
    		
    		// Leemos la velocidad de los Pókemon
    		int velJugador = pJugador.velocidadaObjetoAplicado();
    		int velRival = pRival.velocidadaObjetoAplicado();
    		
    		// REGLA ESTADOS (PARÁLISIS): La parálisis reduce la velocidad un 75% por lo que se divide entre 4.
    		if (pJugador.getEstadoActual() == Estado.PARALIZADO) velJugador = velJugador / 4;
    		if (pRival.getEstadoActual() == Estado.PARALIZADO) velRival = velRival / 4;
    		
    		// Calculamos quien es más rápido, este pegará primero
    		if (velJugador >= velRival) {
    			// Jugador ataca
    			resolverAccion(Turno.TipoAccion.ATACAR, jugador, pJugador, pRival, t);
    			
    			// El rival SOLO responde si ha SOBREVIVIDO al golpe.
    			if (pRival.getVitalidad() > 0) {
    				resolverAccion(Turno.TipoAccion.ATACAR, rival, pRival, pJugador, t);
    			}else {
    				//LOG "RIVAL NO PUDO ATACAR PORQUE SE DEBILITÓ"
    			}
    		} else {
    			// El rival es más rápido, asi que ataca EL PRIMERO.
    			resolverAccion(Turno.TipoAccion.ATACAR, rival, pRival, pJugador, t);
    			
    			// El JUGADOR solo responde si sobrevive
    			if (pJugador.getVitalidad() > 0) {
    				resolverAccion(Turno.TipoAccion.ATACAR, jugador, pJugador, pRival, t);
    				
    			} else {
    				// LOG "ENTRENADOR NO PUDO ATACAR PORQUE SE DEBILITÓ"
    			}
    		}
    	}
    	
    	// Si solo UNO eligio ATACAR y el otro no atacó porque usó curación en la PRIMERA SECCIÓN.
    	else if (jugadorAtaca && pJugador.getVitalidad() > 0) {
    		resolverAccion(Turno.TipoAccion.ATACAR, jugador, pJugador, pRival, t);	
    	}
    	else if (rivalAtaca && pRival.getVitalidad() > 0) {
    		resolverAccion(Turno.TipoAccion.ATACAR, rival, pRival, pJugador, t);
    	}
    	
    	
    	// ---- TERCERA SECCIÓN ----
    	// DAÑO DE ENFERMEDADES (Fin del turno)
    	// Después de los golpes, si alguien está envenenado o quemado, le toca perder un poco de vida por la enfermedad.
    	
    	aplicarEfectosFinDeTurno(pJugador, pRival);
		aplicarEfectosFinDeTurno(pRival, pJugador);
		
		// Comprobamos si la enfermedad termino de matar al pokemon
		comprobarDebilitados(pJugador, pRival);
		
		
		// ---- CUARTA SECCIÓN ----
		// COMPROBAR VICTORIA (Si alguien ya gano)
		
		comprobarVictoria(); // Si alguien ha llegado a 6 bajas, se acaba el juego.
		
		if (ganador != null) {
			// LOG -FIN DEL COMBATE-
			// LOG EL GANADOR ES + ganador.getNombreEntrenador()
			repartirRecompensas(pJugador, pRival); // Repartimos el dinero
		}
		
		// Fin de la ronda. Guardamos esto en la memoria y sumamos 1 al contador.
		historialTurnos.add(t);
		contadorTurnos++;
    } 
    
    // ---- MÉTODOS DE APOYO ----
    // Ejecuta la acción elegida, si la acción es ATACAR, comprueba si alguna enfermedad se lo impide.
    
    private void resolverAccion(Turno.TipoAccion accion, Entrenador entrenador, Pokemon actuante, Pokemon enemigo, Turno t) {
    	if (actuante.getVitalidad() <= 0) {
    		return; // Los muertos no pueden hacer nada
    	}
    	
    	switch(accion) {
    		case ATACAR:
    			Movimiento mov = (entrenador == jugador) ? t.getMovEntrenador() : t.getMovRival();
    			
    			// El metodo puedeMoverse comprueba si estás dormido, en este caso frena y no deja atacar en este turno.
    			if (puedeMoverse(actuante)) {
    				actuante.atacar(enemigo,  mov);
    				// LOG actuante.getMote() + usa + mov.getNomMovimiento() + "."
    			}
    			break;
    			
    		case MOCHILA:
    			// LOG El entrenador usa un objeto.
    			break;
    			
    		case CAMBIO:
    			// EL estado ATRAPADO unas cuerdas te agarran y no puedes cambiar de pokemon.
    			if (actuante.getEstadoActual() == Estado.ATRAPADO) {
    				//LOG actuante.getMote() + " está atrapado y no puede cambiarse.
    				return;	
    			}
    			// LOG El entrenador cambia de pokemon
    			break;
    			
    		case HUIR:
    			// Tampoco se puede huir si estas atrapado
    			if (actuante.getEstadoActual() == Estado.ATRAPADO) {
    				// LOG No puedes huir, el pokemon esta atrapado.
    				return;
    			}
    			// LOG - El entrenador se ha retirado del combate.
    			
    			// Al retirarse, la partida termina al instante y gana el otro entrenador.
    			this.perdedor = entrenador;
				this.ganador = (entrenador == jugador) ? rival : jugador;
				break;
			
			default:
				break;
        }
    }  
    
    // Revisamos los estados del pokemon
    // Si devuelve TRUE, puede atacar normal, si devuelve FALSE, pierde el turno.
    
    private boolean puedeMoverse(Pokemon p) {
    	if(p.getEstadoActual() == Estado.DORMIDO) {
    		// LOG - X esta profundamente dormido.
    		return false;
    	}
    	if(p.getEstadoActual() == Estado.CONGELADO) {
    		// LOG - X esta congelado
    		return false;
    	}
    	if(p.getEstadoActual() == Estado.PARALIZADO && Math.random() < 0.25) {
    		// LOG - X esta paralizando, no se puede mover
    		return false;
    	}
    	if(p.getEstadoActual() == Estado.ENAMORADO && Math.random() < 0.50) {
    		//LOG - X esta enamorado no puede atacar
    		return false;
    	}
    	if(p.getEstadoActual() == Estado.SOMNOLIENTO && Math.random() < 0.33) {
    		//LOG - X esta somnoliento no puede moverse
    		return false;
    	}
    	if(p.getEstadoActual() == Estado.AMEDRENTADO) {
    		// Amedrentado significa que te han asustado en este turno y retrocedes sin pegar.
    		// LOG - X retrocedio
    		p.setEstadoActual(Estado.SALUDABLE); // Se te pasa el susto al momento.
			return false;
    	}
    	if(p.getEstadoActual() == Estado.CONFUSO) {
    		// Estar confuso significa que te mareas y tienes un 50% de probabilidad de pegarte un golpe a ti mismo sin querer.
    		//LOG - X esta confuso
    		if (Math.random() < 0.50) {
				// Cálculos matemáticos del daño que te haces a ti mismo.
				int dañoConfusion = (40 + p.ataqueObjetoAplicado()) - p.defensaObjetoAplicado();
				if (dañoConfusion < 1) dañoConfusion = 1;
				p.setVitalidad(p.getVitalidad() - dañoConfusion);
				// TODO: LOG - confuso que se hiere a sí mismo
				return false;
    		}
    	}
    	return true; // Si está sano (o sobrevive a todas las comprobaciones), puede atacar.
    }
    
    // ENFERMEDADES DE FIN DE TURNO
    // Algunas enfermedades no te quitan el turno pero van restando salud como el veneno. Se aplican aqui.
    private void aplicarEfectosFinDeTurno(Pokemon p, Pokemon enemigo) {
		if (p == null || p.getVitalidad() <= 0) return;

		int danioResidual = 0;

		switch(p.getEstadoActual()) {
			case ENVENENADO:
			case QUEMADO:
			case HELADO:
				//Estar envenenado te quita un 6% (1/16) de tu vida máxima cada turno.
				danioResidual = p.getVitalidad() / 16;
				if (danioResidual < 1) danioResidual = 1; // Para que como mínimo te quite 1 punto.
				p.setVitalidad(p.getVitalidad() - danioResidual);
				// TODO: LOG - X sufre daño por su estado (veneno/quemadura/helado).
				break;
				
			case GRAVEMENTE_ENVENENADO:
				// A diferencia del veneno normal, este veneno es cada vez peor (1/16, luego 2/16, luego 3/16...)
				p.setTurnosEstado(p.getTurnosEstado() + 1); 
				danioResidual = (p.getVitalidad() / 16) * p.getTurnosEstado();
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual);
				// TODO: LOG - X sufre mucho daño por el veneno grave
				break;
				
			case MALDITO:
				// Te roba una cuarta parte de la vida (25%) de golpe
				danioResidual = p.getVitalidad() / 4; 
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual);
				// TODO: LOG - X es afligido por la maldición
				break;
				
			case ATRAPADO:
				danioResidual = p.getVitalidad() / 16;
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual);
				// TODO: LOG - X sufre daño por estar atrapado
				break;
				
			case DRENADORAS:
				danioResidual = p.getVitalidad() / 16;
				if (danioResidual < 1) danioResidual = 1;
				p.setVitalidad(p.getVitalidad() - danioResidual); // Pierdes vida
				
				// A diferencia del veneno, las drenadoras roban tu vida y se la curan al enemigo.
				if (enemigo != null && enemigo.getVitalidad() > 0) {
					enemigo.setVitalidad(enemigo.getVitalidad() + danioResidual);
				}
				// TODO: LOG - Las drenadoras roban salud a X
				break;
				
			case CANTO_MORTAL:
				// Es una cuenta atrás. Mueres al tercer turno pase lo que pase.
				p.setTurnosEstado(p.getTurnosEstado() + 1);
				if (p.getTurnosEstado() >= 3) {
					p.setVitalidad(0); 
					// TODO: LOG - "El contador de Canto Mortal de X llegó a 0."
				} else {
					// TODO: LOG - "El contador de Canto Mortal de X baja."
				}
				break;
				
			default:
				break;
		}
    }
    
    // ---- CUARTA SECCIÓN ----
    // Sistema de muertes y victoria
    
    private void comprobarDebilitados(Pokemon pJ, Pokemon pR) {
    	if (pJ != null && pJ.getVitalidad() <= 0 && pJ.getEstadoActual() != Estado.DEBILITADO) {
			pJ.setEstadoActual(Estado.DEBILITADO);
			koJugador++; // Sumamos 1 baja a nuestro marcador en contra.
			// TODO: LOG - Tu Pokémon se ha debilitado
    }
    	if (pR != null && pR.getVitalidad() <= 0 && pR.getEstadoActual() != Estado.DEBILITADO) {
			pR.setEstadoActual(Estado.DEBILITADO);
			koRival++; // Sumamos 1 baja al marcador del enemigo.
			// TODO: LOG - El Pokémon rival se ha debilitado
    }
  }
    
    private void comprobarVictoria() {
		// El combate finaliza si alguien consigue matar a los 6 del rival.
		if (koRival >= 6) {
			this.ganador = jugador;
			this.perdedor = rival;
		} else if (koJugador >= 6) {
			this.ganador = rival;
			this.perdedor = jugador;
		} 
    }
    
    // ---- QUINTA SECCIÓN ----
    // Recompensas, experiencia y pokedollars
    
    private void repartirRecompensas(Pokemon pJugador, Pokemon pRival) {
		
		// 1. ROBO DE POKÉDOLLARS: El que pierde le tiene que pagar al ganador (Un tercio de su dinero).
		int dineroRobado = perdedor.getPokedollar() / 3;
		perdedor.setPokedollar(perdedor.getPokedollar() - dineroRobado); // Se lo restamos al perdedor
		ganador.setPokedollar(ganador.getPokedollar() + dineroRobado);   // Se lo sumamos al ganador
		
		// LOG - ganador.getNombreEntrenador() + " gana " + dineroRobado + " pokédollars
		
		// 2. EXPERIENCIA: Si ganas, tu luchador se hace más fuerte.
		if (ganador == jugador && pJugador != null && pJugador.getVitalidad() > 0 && pRival != null) {
			int expGanada = (pJugador.getNivel() + (pRival.getNivel() * 10)) / 4;
			pJugador.setExp(pJugador.getExp() + expGanada);
			
			//LOG - pJugador.getMote() + " obtiene " + expGanada + " puntos de EXP
			
			// Si ha ganado mucha experiencia, puede subir de nivel (hacerse nivel 2, nivel 3...)
			if (pJugador.subirNivel()) {
				// LOG - "¡" + pJugador.getMote() + " ha subido al nivel " + pJugador.getNivel() + "!"
			}
		} 
		else if (ganador == rival && pRival != null && pRival.getVitalidad() > 0 && pJugador != null) {
			int expGanada = (pRival.getNivel() + (pJugador.getNivel() * 10)) / 4;
			pRival.setExp(pRival.getExp() + expGanada);
			
			if (pRival.subirNivel()) {
				// LOG - "¡El " + pRival.getMote() + " rival ha subido al nivel " + pRival.getNivel() + "!"
			}
		} 
	}

	// GETTERS PARA EL JAVA FX 
	public Entrenador getGanador() { return ganador; }
	public Entrenador getPerdedor() { return perdedor; }
	public int getContadorTurnos() { return contadorTurnos; }
	public LinkedList<Turno> getHistorialTurnos() { return historialTurnos; }
	public int getKoJugador() { return koJugador; }
	public int getKoRival() { return koRival; }
}