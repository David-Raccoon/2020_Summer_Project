package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.domian.Friend;

public class FriendDAO {
	private Connection connection;
	private PreparedStatement preparedStatement;

	public FriendDAO() {
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

	private static Friend setFriend(ResultSet result) {
		Friend friend = new Friend();
		try {
			friend.setFriendId(result.getInt(1));
			friend.setUid1(result.getInt(2));
			friend.setUid2(result.getInt(3));
			friend.setState(result.getString(4));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friend;
	}

	public int findFreeFriendId() {
		int r = 1;
		ResultSet result = null;
		String sql = "SELECT FriendID FROM friend ORDER BY FriendID";
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

	public Friend findFriendById(int friendID) {
		Friend friend = null;
		String sql = "SELECT * FROM friend WHERE FriendID = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, friendID);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				friend = setFriend(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friend;
	}

	public Friend findFriendByUid(int uid1, int uid2) {
		Friend friend = null;
		String sql = "SELECT * FROM friend WHERE UID1 = ? AND UID2 = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid1);
			this.preparedStatement.setInt(2, uid2);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				friend = setFriend(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friend;
	}

	public ArrayList<Friend> findFriendByUid1(int uid1) {

		ArrayList<Friend> friends = new ArrayList<Friend>();
		String sql = "SELECT * FROM friend WHERE UID1 = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid1);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				friends.add(setFriend(result));
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

		return friends;
	}

	public boolean createFriend(Friend friend) {
		String sql = "INSERT INTO friend (FriendID,UID1,UID2,State) values (?,?,?,?)";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, friend.getFriendId());
			this.preparedStatement.setInt(2, friend.getUid1());
			this.preparedStatement.setInt(3, friend.getUid2());
			this.preparedStatement.setString(4, friend.getState());
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

	public boolean deleteFriend(int friendID) {
		String sql = "DELETE FROM friend WHERE friendID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, friendID);
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

	public boolean updateFriendState(int friendID, String state) {
		String sql = "UPDATE friend SET State=? WHERE FriendID=?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, state);
			this.preparedStatement.setInt(2, friendID);
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
