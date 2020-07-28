package main.dao;

import main.domian.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImageDAO {
	private Connection connection;
	private PreparedStatement preparedStatement;

	public ImageDAO() {
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

	private static Image setImage(ResultSet result) {
		GeoDAO geoDAO = new GeoDAO();
		Image image = new Image();
		try {
			image.setImageID(result.getInt(1));
			image.setTitle(result.getString(2));
			image.setDescription(result.getString(3));
			image.setCity(geoDAO.cityCodeToName(result.getString(4)));
			image.setCountry(geoDAO.countryCodeToName(result.getString(5)));
			image.setUid(result.getInt(6));
			image.setPath(result.getString(7));
			image.setContent(result.getString(8));
			image.setDate(result.getString(9));
			image.setFavorNumber(result.getInt(10));
			geoDAO.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return image;
	}

	public int findFreeImageId() {
		int r = 1;
		String sql = "SELECT ImageID FROM image ORDER BY ImageID";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				if (result.getInt(1) != r) {
					break;
				}
				r++;
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

		return r;
	}

	public Image findImageById(int id) {
		Image image = null;
		String sql = "SELECT * FROM image WHERE ImageID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, id);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				image = setImage(result);
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

		return image;
	}

	public ArrayList<Image> findImageByTitle(String keyword, String sort) {
		ArrayList<Image> images = new ArrayList<Image>();
		String sql = "SELECT * FROM image WHERE Title like ? ORDER BY Date";
		if (sort.equals("byFavor")) {
			sql = "SELECT * FROM image WHERE Title like ? ORDER BY FavorNumber";
		}

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, "%" + keyword + "%");
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				images.add(setImage(result));
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

		return images;
	}

	public ArrayList<Image> findImageByDescription(String keyword, String sort) {
		ArrayList<Image> images = new ArrayList<Image>();
		String sql = "SELECT * FROM image WHERE Description like ? ORDER BY Date";
		if (sort.equals("byFavor")) {
			sql = "SELECT * FROM image WHERE Description like ? ORDER BY FavorNumber";
		}

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, "%" + keyword + "%");
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				images.add(setImage(result));
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

		return images;
	}

	public ArrayList<Image> findImageByUid(int uid) {
		ArrayList<Image> images = new ArrayList<Image>();
		String sql = "SELECT * FROM image WHERE UID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, uid);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				images.add(setImage(result));
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

		return images;
	}

	public ArrayList<Image> getLatestImage(int count) {
		ArrayList<Image> images = new ArrayList<Image>();
		String sql = "SELECT * FROM image ORDER BY Date DESC LIMIT ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, count);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				images.add(setImage(result));
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

		return images;
	}

	public ArrayList<Image> getHottestImage(int count) {
		ArrayList<Image> images = new ArrayList<Image>();
		String sql = "SELECT * FROM image ORDER BY FavorNumber DESC LIMIT ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, count);
			ResultSet result = this.preparedStatement.executeQuery();
			while (result.next()) {
				images.add(setImage(result));
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

		return images;
	}

	public boolean deleteImageById(int id) {
		String sql = "DELETE FROM image WHERE ImageID = ?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, id);
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

	public boolean createImage(Image image) {
		GeoDAO geoDAO = new GeoDAO();
		String sql = "INSERT INTO image (ImageID,Title,Description,CityCode,CountryCode,UID,PATH,Content,Date,FavorNumber) values (?,?,?,?,?,?,?,?,?,?)";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(1, image.getImageID());
			this.preparedStatement.setString(2, image.getTitle());
			this.preparedStatement.setString(3, image.getDescription());
			this.preparedStatement.setString(4, geoDAO.cityNameToCode(image.getCity()));
			this.preparedStatement.setString(5, geoDAO.countryNameToCode(image.getCountry()));
			this.preparedStatement.setInt(6, image.getUid());
			this.preparedStatement.setString(7, image.getPath());
			this.preparedStatement.setString(8, image.getContent());
			this.preparedStatement.setString(9, image.getDate());
			this.preparedStatement.setInt(10, image.getFavorNumber());
			geoDAO.close();
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

	public boolean updateImage(Image image) {

		GeoDAO geoDAO = new GeoDAO();
		String sql = "UPDATE image SET Title=?,Description=?,CityCode=?,CountryCode=?,UID=?,PATH=?,Content=?,Date=?,FavorNumber=? WHERE ImageID=?";

		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setInt(10, image.getImageID());
			this.preparedStatement.setString(1, image.getTitle());
			this.preparedStatement.setString(2, image.getDescription());
			this.preparedStatement.setString(3, geoDAO.cityNameToCode(image.getCity()));
			this.preparedStatement.setString(4, geoDAO.countryNameToCode(image.getCountry()));
			this.preparedStatement.setInt(5, image.getUid());
			this.preparedStatement.setString(6, image.getPath());
			this.preparedStatement.setString(7, image.getContent());
			this.preparedStatement.setString(8, image.getDate());
			this.preparedStatement.setInt(9, image.getFavorNumber());
			int result = this.preparedStatement.executeUpdate();
			geoDAO.close();
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
