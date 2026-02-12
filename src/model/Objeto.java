package model;

public class Objeto {

	private int idObjeto; // id unico
	private String nomObjeto;
	private int precioObjeto; // precio en pokedollars
	private double modAtaque, modDefensa, modVelocidad;
	private double modDefEsp, modEstamina, modRecupEstamina;
	
	// Constructor por defecto
	public Objeto() {
		super();
		this.idObjeto = 0;
		this.nomObjeto = "";
		this.precioObjeto = 0;
		this.modAtaque = 1.0;
		this.modDefensa = 1.0;
		this.modVelocidad = 1.0;
		this.modDefEsp = 1.0;
		this.modEstamina = 1.0;
		this.modRecupEstamina = 1.0;
	}
	
	// Constructor parametrizado
	public Objeto(int idObjeto, String nomObjeto, int precioObjeto, double modAtaque, double modDefensa,
			double modVelocidad, double modDefEsp, double modEstamina, double modRecupEstamina) {
		super();
		this.idObjeto = idObjeto;
		this.nomObjeto = nomObjeto;
		this.precioObjeto = precioObjeto;
		this.modAtaque = modAtaque;
		this.modDefensa = modDefensa;
		this.modVelocidad = modVelocidad;
		this.modDefEsp = modDefEsp;
		this.modEstamina = modEstamina;
		this.modRecupEstamina = modRecupEstamina;
	}
	
	// Constructor copia
		public Objeto(Objeto o) {
			super();
			this.idObjeto = o.idObjeto;
			this.nomObjeto = o.nomObjeto;
			this.precioObjeto = o.precioObjeto;
			this.modAtaque = o.modAtaque;
			this.modDefensa = o.modDefensa;
			this.modVelocidad = o.modVelocidad;
			this.modDefEsp = o.modDefEsp;
			this.modEstamina = o.modEstamina;
			this.modRecupEstamina = o.modRecupEstamina;
		}

	// Getters para obtener informacion del objeto (los setters no son necesarios al no modificar los valores)
	
	public int getIdObjeto() {
		return idObjeto;
	}

	public String getNomObjeto() {
		return nomObjeto;
	}

	public int getPrecioObjeto() {
		return precioObjeto;
	}

	public double getModAtaque() {
		return modAtaque;
	}

	public double getModDefensa() {
		return modDefensa;
	}

	public double getModVelocidad() {
		return modVelocidad;
	}

	public double getModDefEsp() {
		return modDefEsp;
	}

	public double getModEstamina() {
		return modEstamina;
	}

	public double getModRecupEstamina() {
		return modRecupEstamina;
	}

	// Metodo toString que muestra solo el nombre del objeto.
	
	@Override
	public String toString() {
	    return "--- DETALLES DEL OBJETO " + idObjeto + "---\n" +
	           "Nombre: " + nomObjeto + "\n" +
	           "Precio: " + precioObjeto + " pokedollars\n" +
	           "----------------------------\n" +
	           "MODIFICADORES DE ESTADÍSTICAS:\n" +
	           "Ataque: " + modAtaque + " | Defensa: " + modDefensa + " | Velocidad: " + modVelocidad + "\n" +
	           "Def. Esp: " + modDefEsp + " | Estamina: " + modEstamina + " | Recup. Est: " + modRecupEstamina + "\n" +
	           "----------------------------";
	}

	

	
	
	
}
