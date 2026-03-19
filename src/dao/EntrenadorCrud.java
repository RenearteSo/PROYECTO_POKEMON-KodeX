package dao;

import java.sql.Connection;

import model.Entrenador;

public class EntrenadorCrud {

	public static void crearEntrenador(Connection con, Entrenador e) {
		String sql = "INSERT INTO ENTRENADOR VALUES (?,?,?,?,?,?,?,?)";
	}
}
