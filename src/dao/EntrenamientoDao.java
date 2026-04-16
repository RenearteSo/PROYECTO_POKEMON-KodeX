package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import model.Entrenador;
import model.Pokedex;
import model.Pokemon;

public class EntrenamientoDao {

	// Clase entrenamiendo DAO.
	// Creamos una instancia de nuestra clase ConexionDB
	private ConexionDB conexionDB = new ConexionDB();

	// Método para obtener TODOS los pokemons del entrenador
	public LinkedList<Pokemon> obtenerTodosLosPokemon(int idEntrenador) {
		LinkedList<Pokemon> lista = new LinkedList<>();
		String sql = "SELECT P.*, PX.NOM_POKEMON, PX.NUM_POKEDEX, PX.IMG_FRONT " + "FROM POKEMON P "
				+ "INNER JOIN POKEDEX PX ON P.NUM_POKEDEX = PX.NUM_POKEDEX " + "WHERE P.ID_ENTRENADOR = ?";

		try (Connection c = conexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, idEntrenador);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// Reconstruimos la pokédex base
				Pokedex info = new Pokedex();
				info.setNumPokedex(rs.getInt("NUM_POKEDEX"));
				info.setNomPokemon(rs.getString("NOM_POKEMON"));
				info.setRutaImgFront(rs.getString("IMG_FRONT"));

				// Reconstruimos el Pokémon individual
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
				p.setNivel(rs.getInt("NIVEL"));
				p.setExp(rs.getInt("EXPERIENCIA"));
				p.setUbicacion(rs.getString("UBICACION")); // Para saber si está en el equipo o caja
				p.setNumPokedex(info);

				lista.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	// Creamos un método para actualizar los entrenamientos realizados y su
	// modificación de los stats y cobrar
	public boolean guardarEntrenamiento(Entrenador entrenador, Pokemon p) {
		Connection c = conexionDB.getConnection();
		if (c == null)
			return false;

		try {
			// Bloqueamos para que no haya cobros sin entrenamientos
			c.setAutoCommit(false);

			// Actualizamos los Stats, la Experiencia y el Nivel del Pokémon
			String sqlPokemon = "UPDATE pokemon SET VITALIDAD = ?, ATAQUE = ?, DEFENSA = ?, "
					+ "ATAQUE_ESPECIAL = ?, DEFENSA_ESPECIAL = ?, VELOCIDAD = ?, "
					+ "NIVEL = ?, EXPERIENCIA = ? WHERE ID_POKEMON = ?";

			try (PreparedStatement psP = c.prepareStatement(sqlPokemon)) {
				psP.setInt(1, p.getVitalidad());
				psP.setInt(2, p.getAtaque());
				psP.setInt(3, p.getDefensa());
				psP.setInt(4, p.getAtaqueEspecial());
				psP.setInt(5, p.getDefensaEspecial());
				psP.setInt(6, p.getVelocidad());
				psP.setInt(7, p.getNivel());
				psP.setInt(8, p.getExp());
				psP.setInt(9, p.getIdPokemon());
				psP.executeUpdate();
			}

			// Actualizamos los pokedollares del entrenador
			String sqlEntrenador = "UPDATE entrenador SET POKEDOLLAR = ? WHERE ID_ENTRENADOR = ?";
			try (PreparedStatement psE = c.prepareStatement(sqlEntrenador)) {
				psE.setInt(1, entrenador.getPokedollar());
				psE.setInt(2, entrenador.getIdEntrenador());
				psE.executeUpdate();
			}

			// Si llegamos aquí sin errores guardamos definitivamente
			c.commit();
			return true;

		} catch (SQLException e) {
			try {
				c.rollback(); // Si algo falla deshacemos todo
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
