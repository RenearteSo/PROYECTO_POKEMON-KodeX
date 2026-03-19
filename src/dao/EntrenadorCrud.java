package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Entrenador;

public class EntrenadorCrud {
	
	// registro de un nuevo entrenador en la base de datos
	public static void crearNuevoEntrenador(Connection con, Entrenador e) throws SQLException {
		
		int nuevoID = obtenerIdEntrenador(con);
		// pokedolares iniciales (800-1000)
		int dineroInicial = (int) (Math.random() * (1000 - 800 + 1) + 800);

		// preparamos la consulta SQL

		String sql = "INSERT INTO ENTRENADOR (ID_ENTRENADOR, NOM_ENTRENADOR, PASSWORD, TIPO_ENTRENADOR, POKEDOLLAR, NIVEL_ENTRENADOR) "
				+ "VALUES (?, ?, ?, 'JUGADOR', ?, 1)";

		PreparedStatement pstmt = con.prepareStatement(sql);

			// le asignamos los parametros al prepareStatement
			pstmt.setInt(1, e.getIdEntrenador());
			pstmt.setString(2, e.getNombreEntrenador()); // sustituye el primer '?' por el nombre.
			pstmt.setString(3, e.getPassword());
			pstmt.setInt(4, dineroInicial); // pokedollars entre 800 y 1000

			pstmt.executeUpdate(); // realizamos la ejecucion a la base de datos
			
			// Actualizamos el objeto en memoria para que el controlador tenga los datos reales
            e.setIdEntrenador(nuevoID);
            e.setPokedollar(dineroInicial);

		}

	private static int obtenerIdEntrenador(Connection con) throws SQLException{
		int idEntrenador = 0;
		String sql = "SELECT MAX(ID_ENTRENADOR) " // obtenemos el valor MAYOR de id_entrenador para ir asignando id's de forma continua
				+ "FROM ENTRENADOR";
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		while(rs.next()) {
			idEntrenador = rs.getInt(1);
		}
		
		return idEntrenador+1; // le devolvemos el identrenador + 1
	}

}