package test;

import static org.junit.Assert.*;

import main.dao.UserDAO;
import main.domian.User;

import org.junit.Test;

public class TestUserDAO {
	private static UserDAO userDAO = new UserDAO();

	@Test
	public void testFindUserByUsername() {
		User user = userDAO.findUserByUsername("luisg");
		assertEquals(1, user.getUid());
		assertEquals("luisg", user.getUsername());
		assertEquals("luisg@embraer.com.br", user.getEmail());
		assertEquals("abcd1234", user.getPassword());
		assertEquals("2020-07-20", user.getSignUpDate());
	}

	@Test
	public void testFindUserByUid() {
		User user = userDAO.findUserByUid(1);
		assertEquals(1, user.getUid());
		assertEquals("luisg", user.getUsername());
		assertEquals("luisg@embraer.com.br", user.getEmail());
		assertEquals("abcd1234", user.getPassword());
		assertEquals("2020-07-20", user.getSignUpDate());
	}

	@Test
	public void testCreateUser() {
		User user = new User();
		int uid = userDAO.findFreeUid();
		assertNotEquals(-1, uid);
		user.setUid(uid);
		user.setUsername("testuser");
		user.setPassword("abcd1234");
		user.setEmail("test@travel.com");
		user.setEmail("2020-07-20");
		assertTrue(userDAO.createUser(user));
		User newUser = userDAO.findUserByUid(user.getUid());
		assertEquals(user.getUid(), newUser.getUid());
		assertEquals(user.getEmail(), newUser.getEmail());
		assertEquals(user.getUsername(), newUser.getUsername());
		assertEquals(user.getPassword(), newUser.getPassword());
		assertEquals(user.getSignUpDate(), newUser.getSignUpDate());
		assertNotNull(userDAO.deleteUserById(user.getUid()));
	}

}
