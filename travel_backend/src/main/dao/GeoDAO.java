package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeoDAO {
	private Connection connection;
	private PreparedStatement preparedStatement;

	public GeoDAO() {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		this.connection = databaseConnection.getConnection();
	}

	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String cityCodeToName(String code) {
		String name = null;
		String sql = "SELECT Name FROM city WHERE CityCode = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, code);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				name = result.getString(1);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return name;
	}

	public String cityNameToCode(String name) {
		String code = null;
		String sql = "SELECT CityCode FROM city WHERE Name = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, name);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				code = result.getString(1);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return code;
	}

	public String countryCodeToName(String code) {
		String name = null;
		String sql = "SELECT Name FROM country WHERE CountryCode = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, code);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				name = result.getString(1);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return name;
	}

	public String countryNameToCode(String name) {
		String code = null;
		String sql = "SELECT CountryCode FROM country WHERE Name = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, name);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				code = result.getString(1);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return code;
	}

	public ArrayList<String> findAllCountry() {
		ArrayList<String> countries = new ArrayList<String>();

		String sql = "SELECT Name FROM country ORDER BY Name";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				countries.add(result.getString(1));
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return countries;
	}

	public ArrayList<String> findCitiesOfCountry(String country) {
		String code = null;
		String sql = "SELECT CountryCode FROM country WHERE Name = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, country);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				code = result.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (code == null)
			return null;

		ArrayList<String> cities = new ArrayList<String>();

		sql = "SELECT Name FROM city WHERE CountryCode = ? ORDER BY Name";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, code);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				String city = result.getString(1);
				if (!cities.contains(city)) {
					cities.add(city);
				}
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cities;
	}

}
