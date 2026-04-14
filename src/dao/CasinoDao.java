package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Entrenador;

public class CasinoDao {

	/**
	 * Actualiza la cantidad de PokéDólares de un entrenador en la base de datos.
	 * Se debe llamar a este método cada vez que termine una partida en el casino.
	 */
	public static void actualizarSaldo(Connection con, Entrenador e) throws SQLException {
		
		// Preparamos la consulta SQL para actualizar SOLO el dinero de ese entrenador
		String sql = "UPDATE ENTRENADOR SET POKEDOLLAR = ? WHERE ID_ENTRENADOR = ?";

		// Usamos try-with-resources para asegurarnos de que el PreparedStatement se cierra solo
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			// Le asignamos los parámetros (primero el dinero nuevo, luego el ID del jugador)
			pstmt.setInt(1, e.getPokedollar());
			pstmt.setInt(2, e.getIdEntrenador());

			// Ejecutamos la actualización en la base de datos
			pstmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.err.println("Error al actualizar el saldo en la base de datos: " + ex.getMessage());
			throw ex; // Relanzamos la excepción por si el controlador quiere mostrar un pop-up de error
		}
	}
}