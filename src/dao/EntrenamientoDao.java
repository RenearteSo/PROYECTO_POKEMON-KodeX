package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import model.Entrenador;
import model.Pokedex;
import model.Pokemon;
import model.TipoPokemon;

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
	
	// Método para obtener la lista de tipos de una especie concreta
		public LinkedList<TipoPokemon> obtenerTiposDePokedex(int numPokedex) {
			LinkedList<TipoPokemon> listaTipos = new LinkedList<>();
			
			// Unimos la tabla pokedex_tipo con la tabla de nombres tipos_pokemon
			String sql = "SELECT t.NOM_TIPO FROM tipos_pokemon t "
					   + "INNER JOIN pokedex_tipo pt ON t.ID_TIPO = pt.ID_TIPO "
					   + "WHERE pt.NUM_POKEDEX = ?";

			try (Connection con = conexionDB.getConnection(); 
				 PreparedStatement ps = con.prepareStatement(sql)) {
				
				ps.setInt(1, numPokedex);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					// Leemos el nombre del tipo en la base de datos 
					String nombreTipoBD = rs.getString("NOM_TIPO");
					
					// Lo convertimos al Enum TipoPokemon de Java y lo añadimos a la lista
					listaTipos.add(TipoPokemon.valueOf(nombreTipoBD)); 
				}
			} catch (SQLException e) {
				System.out.println("Error al obtener los tipos: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				System.out.println("El tipo de la BD no coincide con el Enum de Java: " + e.getMessage());
			}
			
			return listaTipos;
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
					+ "NIVEL = ?, EXPERIENCIA = ?, NUM_POKEDEX = ? WHERE ID_POKEMON = ?";

			try (PreparedStatement psP = c.prepareStatement(sqlPokemon)) {
				psP.setInt(1, p.getVitalidad());
				psP.setInt(2, p.getAtaque());
				psP.setInt(3, p.getDefensa());
				psP.setInt(4, p.getAtaqueEspecial());
				psP.setInt(5, p.getDefensaEspecial());
				psP.setInt(6, p.getVelocidad());
				psP.setInt(7, p.getNivel());
				psP.setInt(8, p.getExp());
				psP.setInt(9, p.getNumPokedex().getNumPokedex());
				psP.setInt(10, p.getIdPokemon());
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

	// Método para obtener los datos de una especie concreta de la Pokédex
	public Pokedex obtenerPokedexPorId(int idPokedex) {
		Pokedex nuevaEspecie = null;
		String sql = "SELECT * FROM pokedex WHERE NUM_POKEDEX = ?"; 

		try (Connection con = conexionDB.getConnection(); 
			 PreparedStatement ps = con.prepareStatement(sql)) {
			
			ps.setInt(1, idPokedex);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				nuevaEspecie = new Pokedex();
				
				// Rellenamos los datos básicos según las columnas de tu SQL
				nuevaEspecie.setNumPokedex(rs.getInt("NUM_POKEDEX"));
				nuevaEspecie.setNomPokemon(rs.getString("NOM_POKEMON")); 
				nuevaEspecie.setRutaImgFront(rs.getString("IMG_FRONT"));
				nuevaEspecie.setRutaImgBack(rs.getString("IMG_BACK"));
				nuevaEspecie.setSonido(rs.getString("SONIDO"));
				
				// Llamamos al método
				nuevaEspecie.setTipos(obtenerTiposDePokedex(idPokedex));
				
			}
		} catch (SQLException e) {
			System.out.println("Error al obtener la Pokedex por ID: " + e.getMessage());
		}
		
		return nuevaEspecie;
	}

	
}
