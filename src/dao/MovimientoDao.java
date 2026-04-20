package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Estado;
import model.Movimiento;
import model.TipoPokemon;

public class MovimientoDao {

	// Creamos una instancia de nuestra clase ConexionDB
	private ConexionDB conexionDB = new ConexionDB();

	// Método para cargar los ataques que tiene equipados un pokemon en la BD
	public Movimiento[] obtenerMovimientosEquipados(int idPokemon) {
		Movimiento[] ataques = new Movimiento[4]; // El kit de 4 huecos

		// Buscamos en pokemon_movimiento los ataques activos de este Pokémon concreto
		String sql = "SELECT m.*, t.NOM_TIPO FROM movimientos m "
				+ "INNER JOIN tipos_pokemon t ON m.ID_TIPO = t.ID_TIPO "
				+ "INNER JOIN pokemon_movimiento pm ON m.ID_MOVIMIENTOS = pm.ID_MOVIMIENTOS "
				+ "WHERE pm.ID_POKEMON = ? AND pm.ACTIVO = 'SI' LIMIT 4";

		try (Connection con = conexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idPokemon);
			ResultSet rs = ps.executeQuery();

			int indice = 0; // Para ir rellenando el array
			while (rs.next() && indice < 4) {
				Movimiento mov = new Movimiento();
				mov.setIdMovimiento(rs.getInt("ID_MOVIMIENTOS"));
				mov.setNomMovimiento(rs.getString("NOM_MOVIMIENTO"));
				mov.setPotencia(rs.getInt("POTENCIA"));
				mov.setPp(rs.getInt("PP"));
				mov.setNumTurnos(rs.getInt("NUM_TURNOS"));
				mov.setNivelAprendizaje(rs.getInt("NIVEL_APRENDIZAJE"));
				mov.setDescripMovimiento(rs.getString("DECRIP_MOVIMIENTO"));
				mov.setClaseMovimiento(rs.getString("CLASE_MOVIMIENTO"));

				// Tipo
				String nombreTipo = rs.getString("NOM_TIPO");
				mov.setTipoPokemon(TipoPokemon.valueOf(nombreTipo.toUpperCase()));

				mov.calcularCosteEstamina();

				// Lo metemos en el hueco correspondiente del array
				ataques[indice] = mov;
				indice++;
			}
		} catch (SQLException e) {
			System.out.println("Error al cargar movimientos equipados: " + e.getMessage());
		}

		return ataques; // Devolvemos el array con 1, 2, 3 o 4 ataques
	}

	// Método para darle a un Pokémon salvaje recién capturado sus ataques por
	// defecto
	public Movimiento[] generarMovimientosIniciales(int numPokedex, int nivelCaptura) {
		Movimiento[] ataquesIniciales = new Movimiento[4];

		// Buscamos ataques que se aprendan a un nivel MENOR O IGUAL al nivel actual.
		// Los ordenamos de mayor a menor nivel para coger los 4 más fuertes/recientes.
		String sql = "SELECT m.*, t.NOM_TIPO FROM movimientos m "
				+ "INNER JOIN tipos_pokemon t ON m.ID_TIPO = t.ID_TIPO "
				+ "INNER JOIN pokedex_movimiento pm ON m.ID_MOVIMIENTOS = pm.ID_MOVIMIENTOS "
				+ "WHERE pm.NUM_POKEDEX = ? AND m.NIVEL_APRENDIZAJE <= ? "
				+ "ORDER BY m.NIVEL_APRENDIZAJE DESC LIMIT 4";

		try (Connection con = conexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, numPokedex);
			ps.setInt(2, nivelCaptura);
			ResultSet rs = ps.executeQuery();

			int indice = 0;
			while (rs.next() && indice < 4) {
				Movimiento mov = new Movimiento();
				mov.setIdMovimiento(rs.getInt("ID_MOVIMIENTOS"));
				mov.setNomMovimiento(rs.getString("NOM_MOVIMIENTO"));
				mov.setPotencia(rs.getInt("POTENCIA"));
				mov.setPp(rs.getInt("PP"));
				mov.setNumTurnos(rs.getInt("NUM_TURNOS"));
				mov.setNivelAprendizaje(rs.getInt("NIVEL_APRENDIZAJE"));
				mov.setDescripMovimiento(rs.getString("DECRIP_MOVIMIENTO"));
				mov.setClaseMovimiento(rs.getString("CLASE_MOVIMIENTO"));

				// Tipo
				String nombreTipo = rs.getString("NOM_TIPO");
				mov.setTipoPokemon(TipoPokemon.valueOf(nombreTipo.toUpperCase()));

				mov.calcularCosteEstamina();

				ataquesIniciales[indice] = mov;
				indice++;
			}
		} catch (SQLException e) {
			System.out.println("Error al generar movimientos iniciales: " + e.getMessage());
		}

		return ataquesIniciales;
	}

	// Método para buscar en la base de datos si existe un ataque para este nivel
	public Movimiento comprobarNuevoMovimiento(int numPokedex, int nivelActual) {
		Movimiento nuevoMov = null;
		String sql = "SELECT m.*, t.NOM_TIPO FROM movimientos m "
				+ "INNER JOIN tipos_pokemon t ON m.ID_TIPO = t.ID_TIPO "
				+ "INNER JOIN pokedex_movimiento pm ON m.ID_MOVIMIENTOS = pm.ID_MOVIMIENTOS "
				+ "WHERE pm.NUM_POKEDEX = ? AND m.NIVEL_APRENDIZAJE = ?";

		try (Connection con = conexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, numPokedex);
			ps.setInt(2, nivelActual);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				nuevoMov = new Movimiento();

				// Datos básicos
				nuevoMov.setIdMovimiento(rs.getInt("ID_MOVIMIENTOS"));
				nuevoMov.setNomMovimiento(rs.getString("NOM_MOVIMIENTO"));
				nuevoMov.setPotencia(rs.getInt("POTENCIA"));
				nuevoMov.setPp(rs.getInt("PP"));
				nuevoMov.setNumTurnos(rs.getInt("NUM_TURNOS"));
				nuevoMov.setNivelAprendizaje(rs.getInt("NIVEL_APRENDIZAJE"));
				nuevoMov.setDescripMovimiento(rs.getString("DECRIP_MOVIMIENTO"));
				nuevoMov.setClaseMovimiento(rs.getString("CLASE_MOVIMIENTO"));
				// Conversión de Tipo (De String de la BD a Enum de Java)
				String nombreTipo = rs.getString("NOM_TIPO");
				nuevoMov.setTipoPokemon(TipoPokemon.valueOf(nombreTipo.toUpperCase()));

				// Llamamos al método para calcular la estamina
				// basándonos en los datos que acabamos de cargar.
				nuevoMov.calcularCosteEstamina();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nuevoMov;
	}

}
