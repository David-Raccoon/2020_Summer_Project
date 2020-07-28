package main.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
	private Connection connection;

	public DatabaseConnection() {
		Properties properties = new Properties();
		InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("config/jdbc.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Class.forName(properties.getProperty("JDBC_DRIVER"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			this.connection = DriverManager.getConnection(properties.getProperty("DB_URL"),
					properties.getProperty("USER"), properties.getProperty("PASS"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void close() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
