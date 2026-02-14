package model;

import java.util.LinkedList;

public class Combate {
	
	// TAREAS POR HACER EN LA CLASE COMBATE:
	// comprobar si turno es tipo turno o un numero (deberia ser int creo pero comprobar cuando se haga la clase)
	// todos los metodos del combate
	// LOG (registro del combate, puede ir aqui o en clase Turno)

	
    private Entrenador jugador;
    private Entrenador rival;
    private LinkedList<Turno>turno; // LinkedList
    private int koJugador; // pokemons debilitados NUESTROS
    private int koRival;   // pokemons debilitados del ENEMIGO
    private Entrenador ganador;
    
    public Combate(Entrenador jugador, Entrenador rival) { // al empezar combate añadimos el jugador y el rival
        this.jugador = jugador;
        this.rival = rival;
        this.turno = new LinkedList<Turno>(); // Creo que debería ser un linkedList para ir guardando los turnos
        this.koJugador = 0;
        this.koRival = 0;
        // LOG AQUI O EN TURNO, hay que comprobarlo
    }

    // el combate finaliza si alguno de los dos ha tumbado 6 pokemon, hacer metodo
}