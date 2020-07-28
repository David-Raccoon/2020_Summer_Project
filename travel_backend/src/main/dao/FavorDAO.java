package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.domian.Favor;

public class FavorDAO {
	private Connection connection;
	private PreparedStatement preparedStatement;

	public FavorDAO() {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		this.connection = databaseConnection.getConnection();
	}

	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Favor setFavor(ResultSet result) {
		Favor favor = new Favor();
		try {
			favor.setFavorId(result.getInt(1));
			favor.setUid(result.getInt(2));
			favor.setImageId(result.getInt(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return favor;
	}

	public int findFreeFavorId() {
		int r = 1;
		ResultSet result = null;
		String sql = "SELECT FavorID FROM favor ORDER BY FavorID";
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

	public Favor findFavorById(int favorID) {
		Favor favor = null;
		String sql = "SELECT * FROM favor WHERE FavorID = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, favorID);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				favor = setFavor(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return favor;
	}

	public int getFavorNumber(int imageId) {
		int r = 0;
		String sql = "SELECT count(*) as result FROM favor WHERE ImageID = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, imageId);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				r = result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

	public ArrayList<Favor> findFavorByImageId(int imageId) {
		ArrayList<Favor> favors = new ArrayList<Favor>();
		String sql = "SELECT * FROM favor WHERE ImageID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, imageId);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				favors.add(setFavor(result));
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

		return favors;
	}

	public ArrayList<Favor> findFavorByUid(int uid) {
		ArrayList<Favor> favors = new ArrayList<Favor>();
		String sql = "SELECT * FROM favor WHERE UID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				favors.add(setFavor(result));
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

		return favors;
	}

	public boolean checkFavor(int uid, int imageId) {
		String sql = "SELECT * FROM favor WHERE UID = ? AND ImageID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid);
			this.preparedStatement.setInt(2, imageId);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				return true;
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

		return false;
	}

	public boolean createFavor(Favor favor) {
		String sql = "INSERT INTO favor (FavorID,UID,ImageID) values (?,?,?)";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, favor.getFavorId());
			this.preparedStatement.setInt(2, favor.getUid());
			this.preparedStatement.setInt(3, favor.getImageId());
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

	public boolean deleteFavor(int imageID, int uid) {
		String sql = "DELETE FROM favor WHERE ImageID = ? AND UID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, imageID);
			this.preparedStatement.setInt(2, uid);
			int result = this.preparedStatement.executeUpdate();
			System.out.println(imageID);
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
