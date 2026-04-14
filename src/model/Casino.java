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
			return "sin_dinero";
		}
		e.setPokedollar(e.getPokedollar() - apuesta);

		String resultado = (Math.random() < 0.5) ? "cara" : "cruz";

		if (eleccion.equalsIgnoreCase(resultado)) {
			int premio = apuesta * 2; 
			e.setPokedollar(e.getPokedollar() + premio); 
		} 
        // Devolvemos lo que ha salido en la moneda para que la vista lo sepa
        return resultado;
	}

	// 2. JUEGO DE LA RULETA --
		public String jugarRuleta(Entrenador e, int numApostado, String colorApostado, int apuesta) {
		if (e.getPokedollar() < apuesta) {
			return "sin_dinero";
		}

		// restamos la apuesta al entrar al juego
		e.setPokedollar(e.getPokedollar() - apuesta);

		int numResultado = (int) (Math.random() * 37); // Del 0 al 36 (Ruleta real)
		String colorResultado = (Math.random() < 0.5) ? "rojo" : "negro"; 
        
        // Excepción de la ruleta: el 0 suele ser verde y hace perder a todos
        if (numResultado == 0) colorResultado = "verde";

		if (numApostado == numResultado) {
			// Acierto de numero: Gana x10
			e.setPokedollar(e.getPokedollar() + (apuesta * 10));
            return "numero_" + numResultado + "_" + colorResultado;
		} else if (colorApostado.equalsIgnoreCase(colorResultado)) {
			// Acierto de color: Gana x2
			e.setPokedollar(e.getPokedollar() + apuesta * 2);
			return "color_" + numResultado + "_" + colorResultado;
		} else {
			// Pierde
			return "pierde_" + numResultado + "_" + colorResultado;
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