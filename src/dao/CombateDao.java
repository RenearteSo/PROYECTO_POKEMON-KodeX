package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import model.Entrenador;
import model.Movimiento;
import model.Pokedex;
import model.Pokemon;
import model.TipoPokemon;

public class CombateDao {

   public void cargarEquipoJugador(Entrenador jugador) {
        
        // Preparamos una consulta bastante completa. Usamos JOIN para unir la tabla del pokemon con la Pokedex (para pillar la imagen trasera), 
        // y LEFT JOIN para traernos tambien el nombre de su tipo. 
        // GROUP BY al final para asegurarnos de que no nos devuelva filas duplicadas si el pokemon tiene más de un tipo.
        String sql = "SELECT p.*, px.IMG_BACK, t.NOM_TIPO " +
                     "FROM pokemon p " +
                     "JOIN pokedex px ON p.NUM_POKEDEX = px.NUM_POKEDEX " +
                     "LEFT JOIN pokedex_tipo pt ON px.NUM_POKEDEX = pt.NUM_POKEDEX " +
                     "LEFT JOIN tipos_pokemon t ON pt.ID_TIPO = t.ID_TIPO " +
                     "WHERE p.ID_ENTRENADOR = ? AND p.UBICACION = 'EQUIPO' " +
                     "GROUP BY p.ID_POKEMON";
                     
        ConexionDB connection = new ConexionDB();
        try (Connection con = connection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, jugador.getIdEntrenador());
            ResultSet rs = pstmt.executeQuery();
            
            Pokemon[] equipo = new Pokemon[6];
            int i = 0;
            
            // Vamos leyendo los resultados de la base de datos y cargamdo cada Pokemon
            while(rs.next() && i < 6) {
                Pokemon p = new Pokemon();
                p.setIdPokemon(rs.getInt("ID_POKEMON"));
                p.setMote(rs.getString("MOTE"));
                
                p.setVitalidad(rs.getInt("VITALIDAD")); 
                p.setAtaque(rs.getInt("ATAQUE"));
                p.setDefensa(rs.getInt("DEFENSA"));
                p.setVelocidad(rs.getInt("VELOCIDAD"));
                p.setNivel(rs.getInt("NIVEL"));
                p.setExp(rs.getInt("EXPERIENCIA"));
                p.setEstamina(100); 
                
                Pokedex px = new Pokedex();
                px.setNumPokedex(rs.getInt("NUM_POKEDEX"));
                px.setRutaImgBack(rs.getString("IMG_BACK"));
                px.setSonido("audio/Pokemon/cries_pokemon_legacy_" + px.getNumPokedex() + ".ogg");
                p.setNumPokedex(px);
                
                // sacamos el tipo. Si viene null o vacia, le plantamos el tipo Normal
                LinkedList<TipoPokemon> tipos = new LinkedList<>();
                String tipoStr = rs.getString("NOM_TIPO");
                if (tipoStr != null) {
                    try { tipos.add(TipoPokemon.valueOf(tipoStr.toUpperCase())); } catch (Exception e) {}
                }
                if (tipos.isEmpty()) tipos.add(TipoPokemon.NORMAL);
                p.setTipoPokemon(tipos);
                
                // ataques basicos
                MovimientoDao movDao = new MovimientoDao();
                Movimiento[] movsRecuperados = movDao.obtenerMovimientosEquipados(p.getIdPokemon());
                
                // PARCHE DE SEGURIDAD: Si no tiene ningún ataque equipado en la BD, le damos un básico por defecto
                if (movsRecuperados[0] == null) {
                    movsRecuperados[0] = new Movimiento(0, 40, 35, 0, 20, 1, "ataque", "Placaje", "Ataque básico por defecto.", null, "", TipoPokemon.NORMAL);
                }
                
                p.setMovimiento(movsRecuperados);
                
                equipo[i] = p;
                i++;
            }
            // metemos el array con los pokemon ya creados al entrenador
            jugador.setEquipoPrincipal(equipo);
            
        } catch (SQLException e) {}
    }

    public int[] obtenerRangoNivelesEquipo(int idEntrenador) {
        int min = 1, max = 1;
        
        // usamos min y max para obtener el rango de niveles del equipo
        // calculamos despues
        String sql = "SELECT MIN(NIVEL) as min_lvl, MAX(NIVEL) as max_lvl "
                + "FROM pokemon "
                + "WHERE ID_ENTRENADOR = ? "
                + "AND UBICACION = 'EQUIPO'";
        ConexionDB connection = new ConexionDB();
        
        try (Connection con = connection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idEntrenador);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()) {
                min = rs.getInt("min_lvl");
                max = rs.getInt("max_lvl");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // asi evitamos que nos devuelva niveles como 0 o negativos
        if (min <= 0) min = 1;
        if (max <= 0) max = 1;
        return new int[]{min, max};
    }

  public Entrenador generarRivalAleatorio(int nivelMin, int nivelMax, int cantidadPokemon) {
        String[] posiblesNombres = {"LuisRe", "Fernando", "Carrión"};
        String nombreElegido = posiblesNombres[(int)(Math.random() * posiblesNombres.length)];
        
        Entrenador rival = new Entrenador(nombreElegido, "");
        rival.setPokedollar(1500);
        Pokemon[] equipoRival = new Pokemon[6];
        
        // Esta consulta es super importante. Sacamos la info directamente de la pokedex y sus tipos.
        // "ORDER BY RAND() LIMIT ? es para pedir desordene la tabla aleatoriamente y 
        //nos devuelva solo la cantidad de pokemon que le pasemos
        String sql = "SELECT p.NUM_POKEDEX, p.NOM_POKEMON, p.IMG_FRONT, t.NOM_TIPO " +
                     "FROM pokedex p " +
                     "LEFT JOIN pokedex_tipo pt ON p.NUM_POKEDEX = pt.NUM_POKEDEX " +
                     "LEFT JOIN tipos_pokemon t ON pt.ID_TIPO = t.ID_TIPO " +
                     "GROUP BY p.NUM_POKEDEX " +
                     "ORDER BY RAND() LIMIT ?";
                     
        ConexionDB connection = new ConexionDB();
        try (Connection con = connection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
             
            pstmt.setInt(1, cantidadPokemon);
            ResultSet rs = pstmt.executeQuery();
             
            int i = 0;
            while (rs.next()) {
                Pokemon p = new Pokemon();
                
                // Generamos un nivel que tenga sentido respecto al nuestro
                int nivelEnemigo = (int) (Math.random() * (nivelMax - nivelMin + 1)) + nivelMin;
                p.setNivel(nivelEnemigo);
                
                // Generamos una estadística base aleatoria de 1 a 10 y la escalamos multiplicando su nivel.
                int statBase = (int) (Math.random() * 10) + 1;
                
                // CORRECCIÓN: En lugar de 100 fijos, simulamos la vitalidad base (15) más lo que ganaría por nivel.
                // Así un rival nv 1 tendrá ~20 HP, y un rival nv 30 tendrá ~110 HP, haciendo el combate justo.
                int hpSimulado = 15 + statBase + (nivelEnemigo * 3);
                p.setVitalidad(hpSimulado); 
                
                p.setAtaque(statBase + (nivelEnemigo * 2));
                p.setDefensa(statBase + (nivelEnemigo * 2));
                p.setVelocidad(statBase + (nivelEnemigo * 2));
                p.setEstamina(100);
                
                p.setMote(rs.getString("NOM_POKEMON"));
                
                Pokedex px = new Pokedex();
                px.setNumPokedex(rs.getInt("NUM_POKEDEX"));
                px.setRutaImgFront(rs.getString("IMG_FRONT"));
                px.setSonido("audio/Pokemon/cries_pokemon_legacy_" + px.getNumPokedex() + ".ogg");
                p.setNumPokedex(px);
                
                LinkedList<TipoPokemon> tipos = new LinkedList<>();
                String tipoStr = rs.getString("NOM_TIPO");
                if (tipoStr != null) {
                    try { tipos.add(TipoPokemon.valueOf(tipoStr.toUpperCase())); } catch (Exception e) {}
                }
                if (tipos.isEmpty()) tipos.add(TipoPokemon.NORMAL);
                p.setTipoPokemon(tipos);
                
                MovimientoDao movDao = new MovimientoDao();
                Movimiento[] movsRival = movDao.generarMovimientosIniciales(p.getNumPokedex().getNumPokedex(), nivelEnemigo);
                
                // PARCHE DE SEGURIDAD: Evita que el rival se quede sin ataques si la base de datos no le da ninguno
                if (movsRival[0] == null) {
                    movsRival[0] = new Movimiento(0, 40, 35, 0, 20, 1, "ataque", "Placaje", "Ataque básico por defecto.", null, "", TipoPokemon.NORMAL);
                }
                
                p.setMovimiento(movsRival);
                
                equipoRival[i] = p;
                i++;
            }
        } catch (Exception e) {}
        
        rival.setEquipoPrincipal(equipoRival);
        return rival;
    }

    public void actualizarRecompensasBD(Entrenador jugador) {
        
        // dos sentencias UPDATE separadas: una para actualizar la cartera del jugador
        // y otra para ir guardando la experiencia y el nivel que han ganado sus pokémon en el combate.
        String sqlDinero = "UPDATE entrenador SET POKEDOLLAR = ? WHERE ID_ENTRENADOR = ?";
        String sqlExp = "UPDATE pokemon SET EXPERIENCIA = ?, NIVEL = ? WHERE ID_POKEMON = ?";
        
        ConexionDB connection = new ConexionDB();
        try (Connection con = connection.getConnection()) {
            
            // Primero actualizamos el dinero
            try (PreparedStatement pstmt = con.prepareStatement(sqlDinero)) {
                pstmt.setInt(1, jugador.getPokedollar());
                pstmt.setInt(2, jugador.getIdEntrenador());
                pstmt.executeUpdate();
            }
            
            // Ahora recorremos el equipo entero y lanzamos un update por cada bicho que tenga ID válido
            try (PreparedStatement pstmt = con.prepareStatement(sqlExp)) {
                for(Pokemon p : jugador.getEquipoPrincipal()) {
                    if (p != null && p.getIdPokemon() != 0) { 
                        pstmt.setInt(1, p.getExp());
                        pstmt.setInt(2, p.getNivel());
                        pstmt.setInt(3, p.getIdPokemon());
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}