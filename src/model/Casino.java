package model;

public class Casino {

	// !!!!!! IMPORTANTE: Para que no de error el proyecto todo el rato, al no tenerla la vista hecha, los returns son temporales 
	// importante luego los borramos o cambiamos segun haga falta !!!!!!!!!!

	// JUEGOS :
	// CARA O CRUZ - HECHO
	// RULETA - HECHO
	// ADIVINAR NUMERO - HECHO

	// 1. JUEGO CARA/CRUZ
	public String jugarCaraCruz(Entrenador e, String eleccion, int apuesta) {
		if (e.getPokedollar() < apuesta) {
			// NO TIENE SUFICIENTE DINERO, MOSTRAR EN PANTALLA EL ERROR
			return "";
		}

		// restamos la apuesta
		e.setPokedollar(e.getPokedollar() - apuesta);

		String resultado = (Math.random() < 0.5) ? "cara" : "cruz";
		// animacion de moneda? tipo dandose la vuelta varias veces hasta que aparezca el lado que ha salido.

		if (eleccion.equalsIgnoreCase(resultado)) {
			int premio = apuesta * 2; // Gana el doble si acierta
			e.setPokedollar(e.getPokedollar() + premio); // Sumamos
			// mostramos lo que ha ganado y el dinero total + animacion de subir el dinero poco a poco (si se puede)
			return "";
		} else {
			// pierde todo lo apostado, ya se resta arriba mostramos lo que ha ganado y el dinero total + animacion de subir el dinero poco a poco (si se puede)
			return "";
		}
	}

	// 2. JUEGO DE LA RULETA --
	public String jugarRuleta(Entrenador e, int numApostado, String colorApostado, int apuesta) {
		if (e.getPokedollar() < apuesta) {
			// NO TIENE SUFICIENTE DINERO, MOSTRAR EN PANTALLA EL ERROR
			return "";
		}

		// restamos la apuesta al entrar al juego
		e.setPokedollar(e.getPokedollar() - apuesta);

		int numResultado = (int) (Math.random() * 37) + 1; // numero del 1 al 37
		String colorResultado = (Math.random() < 0.5) ? "rojo" : "negro"; // color
		// animacion de la ruleta girando y la bolita cayendo en el número y color que salen (si se puede hacer, sino pos sale la bola del color y ya)

		if (numApostado == numResultado) {
			// acierto de numero: Gana x10
			e.setPokedollar(e.getPokedollar() + (apuesta * 10));
			// mostramos el pleno (x10) y el dinero total + animacion de subir el dinero
			return "";
		} else if (colorApostado.equalsIgnoreCase(colorResultado)) {
			// acierto de color: Gana x2
			e.setPokedollar(e.getPokedollar() + apuesta * 2);
			// mostramos premio por color y el dinero total + animacion de subir el dinero
			return "";
		} else {
			// No acierta nada: Pierde lo apostado, ya se resta al entrar al juego,  mostramos el resultado de la ruleta y que ha perdido + animacion de bajar el  dinero
			return "";
		}
	}
	
	

	// 3. JUEGO ADIVINAR NUMERO 
    public String comprobarAdivinar(Entrenador e, int numSecreto, int intentoUsuario, int numIntentoActual) {
        // el documento no dice que haya apuesta inicial, pdemos cambiarlo si es muy facil
    	// podemos generar el numero aleatorio fuera de la clase o aqui, asi
        // animación de numero aleatorio cambiando o algo nose

        if (intentoUsuario == numSecreto) {
            int premio = 0;
            // Premios
            if (numIntentoActual == 1) premio = 1000;
            else if (numIntentoActual == 2) premio = 750;
            else if (numIntentoActual == 3) premio = 500;
            else if (numIntentoActual == 4) premio = 250;

            e.setPokedollar(e.getPokedollar() + premio);
            // mensaje de acierto con el premio y el dinero total
            return "";
        } else {
            if (numIntentoActual >= 5) {
                // A partir del quinto intento no gana nada
                // mostramos mensaje de fin de juego y el número aleatorio.
                return "";
            }
            // "Sigue intentándolo, turnos restantes: X"
            return "";
        }
    }
    
    // OTROS METODOS
    
	// METODO PARA EL JUEGO 3, se llama antes y se introduce el valor para jugar.
	public int generarNumeroSecreto() {
		// Se genera un numero entre 1 y 20 
		return (int) (Math.random() * 20) + 1;
	}
}