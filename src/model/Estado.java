package model;

public enum Estado {
	
	// ----ESTADO NORMAL----
	
	// Saludable: El pokemon esta sano.
	SALUDABLE,
	
	// ----ESTADOS PERSISTENTES----
	
	// PARALIZADO: 25% de probabilidad de no atacar.
	// Reduce la velocidad del Pokémon en un 75%.
	PARALIZADO,
	
	// QUEMADO: Reduce el ataque físico a la mitad.
	// Pierde 1/16 de su vitalidad maxima cada turno.
	// INMUNIDAD: Los Pokémon de tipo FUEGO no pueden quemarse.
	QUEMADO,
	
	// ENVENENADO: Pierde vitalidad cada turno en combate.
	// Fuera de combate: Pierde 1 punto de vitalidad cada 4 pasos hasta debilitarse.
	ENVENENADO,
	
	// El daño a la vitalidad aumenta cada turno: pierde 1/16 el primero, 2/16 el segundo, 3/16 el tercero...
	// Si es cambiado por otro Pokémon, la cuenta vuelve a empezar en 1/16.
	GRAVEMENTE_ENVENENADO,
	
	// DORMIDO: No puede usar movimientos.
	// El tiempo de sueño varía entre 1 y 5 turnos.
	DORMIDO,
	
	// CONGELADO: El Pokémon no puede realizar absolutamente nada en combate.
	// INMUNIDAD: Los Pokémon de tipo HIELO no pueden congelarse.
	CONGELADO,
	
	// HELADO: Pierde el 6,25% de su vitalidad máxima cada turno y reduce su ataque especial a la mitad.
	// INMUNIDAD: Los Pokémon de tipo HIELO no pueden helarse.
	HELADO,
	
	// SOMNOLIENTO: El Pokémon puede no moverse y aumenta el daño recibido por los ataques del oponente.
	SOMNOLIENTO,
	
	// POKÉRUS: Estado beneficioso provocado por un virus.
	// Duplica la cantidad de puntos de esfuerzo que recibe un Pokémon al derrotar a un Pokémon salvaje.
	POKERUS, 
	
	// ----ESTADOS TEMPORALES----
	
	// CONFUSO: El Pokémon afectado se puede autolesionar en lugar de atacar.
	// El daño provocado a su propia vitalidad es equivalente a un ataque de potencia 40.
	CONFUSO,
	
	// ENAMORADO: Un Pokémon enamorado tendrá un 50% de posibilidades de no atacar al oponente.
	ENAMORADO,
	
	// ATRAPADO: El Pokémon afectado no puede ser cambiado por el entrenador.
	// Recibirá daño en su vitalidad al final de cada turno.
	ATRAPADO,
	
	// MALDITO: Pierde 1/4 del total de su vitalidad al final de cada turno.
	// INMUNIDAD: No existen Pokémon inmunes a maldición.
	MALDITO,
	
	// DRENADORAS: El Pokémon pierde 1/16 de su vitalidad máxima al final de cada turno.
	// INMUNIDAD: Los Pokémon de tipo PLANTA son inmunes a este estado.
	DRENADORAS,
	
	// CANTO MORTAL: El Pokémon se debilitará tras 3 turnos en combate.
	CANTO_MORTAL,
	
	// CCENTRO DE ATENCIÓN: Los movimientos de sus oponentes se dirigirán a él, independientemente del objetivo que se hubiera elegido.
	CENTRO_ATENCION,
	
	// AAMEDRENTADO: Impide a un Pokémon atacar durante un turno.
	AMEDRENTADO,
	
	// ----ESTADO FINAL----
	
	// DEBILITADO: La vitalidad llega a 0. El Pokémon no puede continuar.
	DEBILITADO,

}
