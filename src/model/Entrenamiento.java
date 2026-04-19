package model;

public enum Entrenamiento {
	PESADO(20){
		// ENTRENAMIENTO PESADO (Coste x20)
	    // Sube: Defensa, Def. Especial y Vitalidad (+5)
		@Override
		public void aplicarMejora(Pokemon p) {
			p.setDefensa((p.getDefensa()) + 5);
			p.setDefensaEspecial((p.getDefensaEspecial()) + 5);
			p.setVitalidad((p.getVitalidad()) + 5);
			System.out.println("¡Entrenamiento Pesado completado! (+5 Def, +5 DefEsp, +5 Vit)");
		}
	},
	FURIOSO(30){
		// ENTRENAMIENTO FURIOSO (Coste x30)
	    // Sube: Ataque, Atq. Especial y Velocidad (+5)
		@Override
		public void aplicarMejora(Pokemon p) {
			p.setDefensa((p.getDefensa()) + 5);
			p.setDefensaEspecial((p.getDefensaEspecial()) + 5);
			p.setVitalidad((p.getVitalidad()) + 5);
			System.out.println("¡Entrenamiento Furioso completado! (+5 Atk, +5 AtkEsp, +5 Vel)");
		}
	},
	FUNCIONAL(40){
		// ENTRENAMIENTO FURIOSO (Coste x30)
	    // Sube: Velocidad, Ataque, Defensa y Velocidad (+5)
		@Override
		public void aplicarMejora(Pokemon p) {
			p.setVelocidad((p.getVelocidad()) + 5);
			p.setAtaque((p.getAtaque()) + 5);
			p.setDefensa((p.getDefensa()) + 5);
			p.setVitalidad(p.getVitalidad() + 5);
			System.out.println("¡Entrenamiento Pesado completado! (+5 Vel, +5 Atk, +5 Def, +5 Vit)");
		}
	},
	// ENTRENAMIENTO ONÍRICO (Coste x40)
    // Sube: Velocidad, Atq. Especial, Def. Especial y Vitalidad (+5)
    ONIRICO(40) {
        @Override
        public void aplicarMejora(Pokemon p) {
            p.setVelocidad(p.getVelocidad() + 5);
            p.setAtaqueEspecial(p.getAtaqueEspecial() + 5);
            p.setDefensaEspecial(p.getDefensaEspecial() + 5);
            p.setVitalidad(p.getVitalidad() + 5);
            System.out.println("¡Entrenamiento Onírico completado! (+5 Vel, +5 AtkEsp, +5 DefEsp, +5 Vit)");
        }
    };
	
	private int factorCoste;
	//Constructores
	private Entrenamiento(int factorCoste) {
		this.factorCoste = factorCoste;
	}
	private Entrenamiento() {
		this.factorCoste = 0;
	}
	// Método abstracto que cada constante implementa arriba a su manera
    public abstract void aplicarMejora(Pokemon p);
    
	public int calcularCoste(Pokemon p) {
		return this.factorCoste * p.getNivel();
	}
	
}
