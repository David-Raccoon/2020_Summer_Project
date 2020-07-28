package test;

import static org.junit.Assert.*;

import org.junit.Test;
import main.dao.DatabaseConnection;

public class TestConnection {
	@Test
	public void testConnection() throws Exception {
		DatabaseConnection mysql = new DatabaseConnection();
		assertNotNull(mysql.getConnection());
	}
}
