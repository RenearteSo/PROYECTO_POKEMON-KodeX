package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import model.Pokemon;
import model.Entrenador;
import model.Pokedex;

public class CrianzaDao {

	// Usamos una instancia de nuestra clase conexión.
	private ConexionDB conexionDB = new ConexionDB();

	public LinkedList<Pokemon> ObtenerEquipoEntrenador(int idEntrenador) throws SQLException {
		LinkedList<Pokemon> equipo = new LinkedList<>();

		String sql = "SELECT P.*, PX.NOM_POKEMON, PX.NUM_POKEDEX, PX.IMG_FRONT " + "FROM POKEMON P "
				+ "INNER JOIN POKEDEX PX ON P.NUM_POKEDEX = PX.NUM_POKEDEX " + "WHERE p.ID_ENTRENADOR = ?";

		// Usamos el método reutilizable
		try (Connection con = conexionDB.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, idEntrenador);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Pokedex info = new Pokedex();
				info.setNumPokedex(rs.getInt("NUM_POKEDEX"));
				info.setNomPokemon(rs.getString("NOM_POKEMON"));
				info.setRutaImgFront(rs.getString("IMG_FRONT"));

				Pokemon p = new Pokemon();
				p.setIdPokemon(rs.getInt("ID_POKEMON"));
				p.setMote(rs.getString("MOTE"));
				p.setSexo(rs.getString("SEXO"));
				p.setVitalidad(rs.getInt("VITALIDAD"));
				p.setAtaque(rs.getInt("ATAQUE"));
				p.setDefensa(rs.getInt("DEFENSA"));
				p.setAtaqueEspecial(rs.getInt("ATAQUE_ESPECIAL"));
				p.setDefensaEspecial(rs.getInt("DEFENSA_ESPECIAL"));
				p.setVelocidad(rs.getInt("VELOCIDAD"));
				p.setFertilidad(rs.getInt("FERTILIDAD"));
				p.setNivel(rs.getInt("NIVEL"));
				p.setNumPokedex(info);

				equipo.add(p);
			}
		}
		return equipo;
	}

	public void guardarNacimiento(Entrenador e, Pokemon p1, Pokemon p2, Pokemon hijo) throws SQLException {
		// Obtenemos la conexión de nuestra clase DAO
		Connection con = conexionDB.getConnection();

		if (con == null)
			throw new SQLException("No se pudo establecer conexión con la base de datos.");

		try {
			con.setAutoCommit(false); // Empezamos la transacción

			// Insertamos al hijo
			String sqlHijo = "INSERT INTO POKEMON (ID_ENTRENADOR, NUM_POKEDEX, MOTE, NIVEL, VITALIDAD, ATAQUE, DEFENSA, VELOCIDAD, ATAQUE_ESPECIAL, DEFENSA_ESPECIAL, FERTILIDAD, SEXO, ESTADO, EXPERIENCIA, ESTAMINA, UBICACION) "
					+ "VALUES (?, ?, ?, 1, ?, ?, ?, ?, ?, ?, 5, ?, 'SALUDABLE', 0, 100, 'CAJA')";

			try (PreparedStatement psH = con.prepareStatement(sqlHijo)) {
				psH.setInt(1, e.getIdEntrenador());
				psH.setInt(2, hijo.getNumPokedex().getNumPokedex());
				psH.setString(3, hijo.getMote());
				psH.setInt(4, hijo.getVitalidad());
				psH.setInt(5, hijo.getAtaque());
				psH.setInt(6, hijo.getDefensa());
				psH.setInt(7, hijo.getVelocidad());
				psH.setInt(8, hijo.getAtaqueEspecial());
				psH.setInt(9, hijo.getDefensaEspecial());
				psH.setString(10, hijo.getSexo());
				psH.executeUpdate();
			}

			// Restamos fertilidad a los padres
			String sqlUpdatePadre = "UPDATE POKEMON SET FERTILIDAD = FERTILIDAD - 1 WHERE ID_POKEMON = ?";
			try (PreparedStatement psP = con.prepareStatement(sqlUpdatePadre)) {
				psP.setInt(1, p1.getIdPokemon());
				psP.executeUpdate();
				psP.setInt(1, p2.getIdPokemon());
				psP.executeUpdate();
			}

			// Actualizamos el dinero del entrenador
			String sqlDinero = "UPDATE ENTRENADOR SET POKEDOLLAR = ? WHERE ID_ENTRENADOR = ?";
			try (PreparedStatement psD = con.prepareStatement(sqlDinero)) {
				psD.setInt(1, e.getPokedollar());
				psD.setInt(2, e.getIdEntrenador());
				psD.executeUpdate();
			}

			con.commit(); // Si todo ha ido bien, guardamos los cambios definitivamente

		} catch (SQLException ex) {
			con.rollback(); // Si algo falla, deshacemos todo para no perder dinero
			throw ex;
		} finally {
			con.close(); // Cerramos la conexión manualmente
		}
	}

	// Método que sirve para buscar cómo se llama y qué foto tiene el bebé recién
	// nacido
	public Pokedex obtenerInfoPokedex(int idEspecie) throws SQLException {
		Pokedex info = new Pokedex();
		String sql = "SELECT NOM_POKEMON, IMG_FRONT FROM POKEDEX WHERE NUM_POKEDEX = ?";

		try (Connection con = conexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idEspecie); // Le pasamos el ID del bebé
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				info.setNumPokedex(idEspecie);
				info.setNomPokemon(rs.getString("NOM_POKEMON"));
				info.setRutaImgFront(rs.getString("IMG_FRONT"));
			}
		}
		return info;
	}
}