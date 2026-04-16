package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

	public Connection getConnection() {
		String dataBaseName = "proyecto_pokemon";
		String dataBaseUser = "root";
		String dataBasePassword = "";
		String url = "jdbc:mysql://localhost:3306/" + dataBaseName;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Devolvemos la conexión directamente
			return DriverManager.getConnection(url, dataBaseUser, dataBasePassword);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
