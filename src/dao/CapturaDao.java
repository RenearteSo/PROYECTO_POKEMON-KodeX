package dao;

import java.sql.*;

import model.Movimiento;
import model.Pokedex;
import model.Pokemon;

// CLASE DAO de Captura
// Se conecta con la Base de Datos para sacar los Pókemon al azar y guardar los que el entrenador logre capturar.
public class CapturaDao {

	// Instanciamos la clase de conexión
	private ConexionDB conexion = new ConexionDB();

	// Método 1: Generar un Pókemon
	// Va a la tabla pokedex, elige uno al azar y crea las estadísticas de nivel 1,
	// devuelve un Array de Objetos que contiene 0 que es el Pókemon creado y 1 su
	// Tipo principal (Fuego, Agua...)
	public Object[] generarPokemonSalvaje() {
		Pokemon p = null;
		String tipo = "NORMAL"; // Tipo por defecto por si llega a fallar algo

		// Abrimos la conexión llamando a tu clase ConexionDB
		try (Connection c = conexion.getConnection()) {

			// Coge el número, nombre, imagen y tipo de la Pokedex y el ORDER BY RAND()
			// LIMIT 1 devuelve solo un Pókemon.
			String q = "SELECT p.NUM_POKEDEX, p.NOM_POKEMON, p.IMG_FRONT, t.NOM_TIPO " + "FROM pokedex p "
					+ "LEFT JOIN pokedex_tipo pt ON p.NUM_POKEDEX = pt.NUM_POKEDEX "
					+ "LEFT JOIN tipos_pokemon t ON pt.ID_TIPO = t.ID_TIPO " + "ORDER BY RAND() LIMIT 1";

			ResultSet rs = c.createStatement().executeQuery(q);

			// Si la consulta encuentra un resultado
			if (rs.next()) {
				// Preparamos los datos básicos de la Pokedex
				Pokedex dex = new Pokedex();
				dex.setNumPokedex(rs.getInt("NUM_POKEDEX"));
				dex.setRutaImgFront(rs.getString("IMG_FRONT"));

				// Creamos el objeto Pókemon usando el constructor
				p = new Pokemon(dex, rs.getString("NOM_POKEMON"));

				// Generamos sus estadísticas de base aleatoria (Del 1 al 10)
				p.setVitalidad((int) (Math.random() * 10) + 1);
				p.setAtaque((int) (Math.random() * 10) + 1);
				p.setDefensa((int) (Math.random() * 10) + 1);
				p.setAtaqueEspecial((int) (Math.random() * 10) + 1);
				p.setDefensaEspecial((int) (Math.random() * 10) + 1);
				p.setVelocidad((int) (Math.random() * 10) + 1);

				// Ponemos la estamina en 100 para que no de error al dividir entre 0
				p.setEstamina(100);

				// Todos los Pókemons aparecen con Nivel 1
				p.setNivel(1);

				// Guardamos el tipo para poder cambiar el fondo de pantalla en el Controller
				tipo = (rs.getString("NOM_TIPO") != null) ? rs.getString("NOM_TIPO") : "NORMAL";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Devolvemos el Pokémon y su Tipo juntos
		return new Object[] { p, tipo };
	}

	// Método para comprobar Motes repetidos
	// Si el Entrenador actual ya tiene un Pókemon guardado cone el mismo mote evita
	// poner el mismo nombre,
	// pero permite que otro Entrenador en otra cuenta pueda usar ese mote.
	public boolean existeMoteParaEntrenador(int idEntrenador, String mote) {

		// Cuenta cuantos Pokemons tienen ese Entrenador y mote
		String sql = "SELECT COUNT(*) FROM pokemon WHERE ID_ENTRENADOR = ? AND MOTE = ?";

		// Usamos conexion.getConnection()
		try (Connection c = conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, idEntrenador);
			ps.setString(2, mote);
			ResultSet rs = ps.executeQuery();

			// Si la cuenta es mayor que 0, ya lo tiene
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Método contar el Equipo
	// Cuenta cuantos Pókemons tiene el jugador actualmente en su EQUIUPO, si son
	// mas de 6 los manda directo a CAJA.
	public int contarPokemonEquipo(int idEntrenador) {
		String sql = "SELECT COUNT(*) FROM pokemon WHERE ID_ENTRENADOR = ? AND UBICACION = 'EQUIPO'";

		// Usamos conexion.getConnection()
		try (Connection c = conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, idEntrenador);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Método Guardado final de Base de Datos
	// Coge todos los datos del Pókemon, lo inserta y LE ASIGNA SUS MOVIMIENTOS.
	public boolean guardarCapturaEnBD(int idEnt, Pokemon p, String ub) {
		String sql = "INSERT INTO pokemon (ID_ENTRENADOR, NUM_POKEDEX, ID_OBJETO, MOTE, VITALIDAD, ATAQUE, "
				+ "ATAQUE_ESPECIAL, DEFENSA, DEFENSA_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, ESTAMINA, "
				+ "EXPERIENCIA, SEXO, ESTADO, UBICACION) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// Añadimos Statement.RETURN_GENERATED_KEYS para poder
		// recuperar el ID del Pokemon recién creado
		try (Connection c = conexion.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			// Rellenamos las interrogaciones igual que antes
			ps.setInt(1, idEnt);
			ps.setInt(2, p.getNumPokedex().getNumPokedex());
			ps.setNull(3, java.sql.Types.INTEGER);
			ps.setString(4, p.getMote());
			ps.setInt(5, p.getVitalidad());
			ps.setInt(6, p.getAtaque());
			ps.setInt(7, p.getAtaqueEspecial());
			ps.setInt(8, p.getDefensa());
			ps.setInt(9, p.getDefensaEspecial());
			ps.setInt(10, p.getVelocidad());
			ps.setInt(11, p.getNivel());
			ps.setInt(12, p.getFertilidad());
			ps.setInt(13, p.getEstamina());
			ps.setInt(14, p.getExp());

			String sexoBD = (p.getSexo() != null && p.getSexo().equalsIgnoreCase("MACHO")) ? "M" : "H";
			ps.setString(15, sexoBD);
			ps.setString(16, p.getEstadoActual().toString());
			ps.setString(17, ub);

			// Ejecutamos la inserción del Pokémon
			int filasAfectadas = ps.executeUpdate();

			// Si el Pokemon se guardó bien, le damos sus ataques
			if (filasAfectadas > 0) {

				// Recuperamos el ID autogenerado de la base de datos
				try (ResultSet rsBotones = ps.getGeneratedKeys()) {
					if (rsBotones.next()) {
						int nuevoIdPokemon = rsBotones.getInt(1); // Este es su nuevo ID en la tabla POKEMON

						// Traemos a DAO de movimientos para calcular qué ataques debe saber
						MovimientoDao movDao = new MovimientoDao();
						Movimiento[] ataques = movDao.generarMovimientosIniciales(p.getNumPokedex().getNumPokedex(),
								p.getNivel());

						// Guardamos esos ataques en la tabla pokemon_movimiento
						String sqlMov = "INSERT INTO pokemon_movimiento (ID_POKEMON, ID_MOVIMIENTOS, ACTIVO) VALUES (?, ?, 'SI')";
						try (PreparedStatement psMov = c.prepareStatement(sqlMov)) {
							for (Movimiento m : ataques) {
								if (m != null) {
									psMov.setInt(1, nuevoIdPokemon);
									psMov.setInt(2, m.getIdMovimiento());
									psMov.executeUpdate();
								}
							}
						}
					}
				}
				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}