package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.Pokemon;
import model.Entrenador;
import model.Pokedex;

public class CrianzaDao {

	// Clase Crianza para la conexión con la base de datos. Sacaremos de aquí los
	// pokemons que querramos poner a criar del equipo pokemon y guardaremos la cria
	// en un huevo hasta que eclosione

	private final String URL = "jdbc:mysql://127.0.0.1:3306/proyecto_pokemon?useSSL=false&serverTimezone=UTC";
	private final String USER = "root";
	private final String PASSWORD = "";

	// Creamos el método para obtener los pokemons a través del equipo pokemon de un
	// entrenador
	public LinkedList<Pokemon> ObtenerEquipoEntrenador(int idEntrenador) throws SQLException {
		LinkedList<Pokemon> equipo = new LinkedList<>();

		// Realizamos la consulta que une la tabla Pokemon con Pokedex para obtener los
		// nombre e imágenes.
		String sql = "SELECT P.*, PX.NOM_POKEMON, PX.NUM_POKEDEX, PX.IMG_FRONT " + "FROM POKEMON P "
				+ "INNER JOIN POKEDEX PX ON P.NUM_POKEDEX = PX.NUM_POKEDEX " + "WHERE p.ID_ENTRENADOR = ?";
		// Creamos la conexión
		try (Connection con = new ConexionDB().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, idEntrenador);
			ResultSet rs = pstmt.executeQuery();

			// Por cada fila que genere la base de datos:
			while (rs.next()) {
				// Construimos la información general de la Pokédex
				Pokedex info = new Pokedex();
				info.setNumPokedex(rs.getInt("NUM_POKEDEX"));
				info.setNomPokemon(rs.getString("NOM_POKEMON"));
				info.setRutaImgFront(rs.getString("IMG_FRONT"));

				// Creamos el pokemon con los datos de la base de datos para que los stats no
				// sean 0
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

				// Añadimos el pokemon
				equipo.add(p);
			}
		}

		return equipo;

	}

	// Creamos el método para guardar al hijo y actualizar a los padres
	public void guardarNacimiento(Entrenador e, Pokemon p1, Pokemon p2, Pokemon hijo) throws SQLException {
		Connection con = new ConexionDB().getConnection();
		try {
			con.setAutoCommit(false);
			// Insertamos el hijo creado
			String sqlHijo = "INSERT INTO POKEMON (ID_ENTRENADOR, NUM_POKEDEX, MOTE, NIVEL, VITALIDAD, ATAQUE, DEFENSA, VELOCIDAD, ATAQUE_ESPECIAL, DEFENSA_ESPECIAL, FERTILIDAD, SEXO, ESTADO, EXPERIENCIA, ESTAMINA) VALUES (?, ?, ?, 1, ?, ?, ?, ?, ?, ?, 5, ?, 'SALUDABLE', 0, 100)";
			PreparedStatement psH = con.prepareStatement(sqlHijo);
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

			// Restamos fertilidad a padres y dinero al entrenador
			String sqlUpdatePadre = "UPDATE POKEMON SET FERTILIDAD = FERTILIDAD - 1 WHERE ID_POKEMON = ?";
			PreparedStatement psP = con.prepareStatement(sqlUpdatePadre);
			psP.setInt(1, p1.getIdPokemon());
			psP.executeUpdate();
			psP.setInt(1, p2.getIdPokemon());
			psP.executeUpdate();

			String sqlDinero = "UPDATE ENTRENADOR SET POKEDOLLAR = ? WHERE ID_ENTRENADOR = ?";
			PreparedStatement psD = con.prepareStatement(sqlDinero);
			psD.setInt(1, e.getPokedollar());
			psD.setInt(2, e.getIdEntrenador());
			psD.executeUpdate();

			con.commit();

		} catch (SQLException ex) {
			con.rollback();
			throw ex;
		} finally {
			con.close();
		}
	}

}
