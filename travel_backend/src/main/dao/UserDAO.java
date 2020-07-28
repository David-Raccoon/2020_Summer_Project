package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.domian.User;

public class UserDAO {
	private Connection connection;
	private PreparedStatement preparedStatement;

	public UserDAO() {
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

	private static User setUser(ResultSet result) {
		User user = new User();
		try {
			user.setUid(result.getInt(1));
			user.setEmail(result.getString(2));
			user.setUsername(result.getString(3));
			user.setPassword(result.getString(4));
			user.setSignUpDate(result.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public int findFreeUid() {
		int r = 1;
		ResultSet result = null;
		String sql = "SELECT UID FROM user ORDER BY UID";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			result = this.preparedStatement.executeQuery();
			while (result.next()) {
				if (result.getInt(1) != r) {
					break;
				}
				r++;
			}
		}

		catch (

		SQLException e) {
			e.printStackTrace();
			return -1;
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		}

		return r;
	}

	public User findUserByUsername(String username) {
		User user = null;
		String sql = "SELECT * FROM user WHERE UserName = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, username);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				user = setUser(result);
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

		return user;
	}

	public User findUserByUid(int uid) {
		User user = null;
		String sql = "SELECT * FROM user WHERE UID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				user = setUser(result);
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

		return user;
	}

	public boolean updateUser(User user) {
		// Not implemented yet
		return false;
	}

	public boolean deleteUserById(int uid) {
		String sql = "DELETE FROM user WHERE uid = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid);
			int result = this.preparedStatement.executeUpdate();
			if (result < 0) {
				return false;
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public boolean createUser(User user) {
		String sql = "INSERT INTO user (UID,Email,UserName,Pass,SignUpDate) values (?,?,?,?,?)";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, user.getUid());
			this.preparedStatement.setString(2, user.getEmail());
			this.preparedStatement.setString(3, user.getUsername());
			this.preparedStatement.setString(4, user.getPassword());
			this.preparedStatement.setString(5, user.getSignUpDate());
			int result = this.preparedStatement.executeUpdate();
			if (result < 0) {
				return false;
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		finally {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
}
