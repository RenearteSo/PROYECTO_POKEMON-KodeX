package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Pokemon;
import model.Pokedex;

public class EquipoDao {
    
    //Base de Datos
    private final String URL = "jdbc:mysql://127.0.0.1:3306/proyecto_pokemon?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public EquipoDao() {
    }

    // Obtiene los Pokémon de un entrenador filtrando por su ubicación 
    public List<Pokemon> obtenerPokemonPorUbicacion(int idEntrenador, String ubicacion) {
        List<Pokemon> lista = new ArrayList<>();
        // Seleccionamos todos los campos del pokemon y la imagen de la pokedex
        String sql = "SELECT p.*, px.IMG_FRONT " +
                     "FROM pokemon p JOIN pokedex px ON p.NUM_POKEDEX = px.NUM_POKEDEX " +
                     "WHERE p.ID_ENTRENADOR = ? AND p.UBICACION = ?";
                     
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {
             
            ps.setInt(1, idEntrenador);
            ps.setString(2, ubicacion);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Cargamos datos de la Pokedex
                Pokedex dex = new Pokedex();
                dex.setNumPokedex(rs.getInt("NUM_POKEDEX"));
                dex.setRutaImgFront(rs.getString("IMG_FRONT"));
                
                // Cargamos datos del Pokemon
                Pokemon p = new Pokemon();
                p.setIdPokemon(rs.getInt("ID_POKEMON"));
                p.setMote(rs.getString("MOTE"));
                p.setNivel(rs.getInt("NIVEL"));
                p.setVitalidad(rs.getInt("VITALIDAD"));
                p.setAtaque(rs.getInt("ATAQUE"));
                p.setDefensa(rs.getInt("DEFENSA"));
                p.setAtaqueEspecial(rs.getInt("ATAQUE_ESPECIAL"));
                p.setDefensaEspecial(rs.getInt("DEFENSA_ESPECIAL"));
                p.setVelocidad(rs.getInt("VELOCIDAD"));
                p.setEstamina(rs.getInt("ESTAMINA"));
                p.setExp(rs.getInt("EXPERIENCIA"));
                p.setUbicacion(rs.getString("UBICACION"));
                
                // Cargamos el sexo para que no de error nulo en el controlador
                p.setSexo(rs.getString("SEXO"));
                
                p.setNumPokedex(dex);
                
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener Pokémon por ubicación: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Actualiza la ubicación de un Pokémon (de Caja a Equipo o al revés)
    public boolean moverPokemon(int idPokemon, String nuevaUbicacion) {
        String sql = "UPDATE pokemon SET UBICACION = ? WHERE ID_POKEMON = ?";
        
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {
             
            ps.setString(1, nuevaUbicacion);
            ps.setInt(2, idPokemon);
            
            // Si se ha actualizado al menos una fila, devuelve true
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al mover Pokémon: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}