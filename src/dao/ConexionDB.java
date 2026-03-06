package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	
	public Connection dataBaseLink;

	public Connection getConnection() {
		String dataBaseName="proyecto-pokemon";
		String dataBaseUser = "root";
		String dataBasePassword = "";
		String url = "jdbc:mysql://localhost:3306/" +dataBaseName;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			dataBaseLink = DriverManager.getConnection(url, dataBaseUser, dataBasePassword);
			System.out.println("Conexión establecida.");
			
		} catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dataBaseLink;
	}
}
