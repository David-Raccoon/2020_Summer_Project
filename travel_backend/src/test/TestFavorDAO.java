package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import main.dao.FavorDAO;
import main.dao.ImageDAO;
import main.domian.Favor;

public class TestFavorDAO {
	private static FavorDAO favorDAO = new FavorDAO();
	private static ImageDAO imageDAO = new ImageDAO();

	@Test
	public void testGetFavorNumber() {
		assertEquals(4, favorDAO.getFavorNumber(1));
	}

	@Test
	public void test_Create_Delete_Favor() {
		Favor favor = favorDAO.findFavorById(4);
		assertNotNull(favor);
		int num = imageDAO.findImageById(favor.getImageId()).getFavorNumber();
		assertTrue(favorDAO.deleteFavor(favor.getImageId(), favor.getUid()));
		assertEquals(num, imageDAO.findImageById(favor.getImageId()).getFavorNumber());
		assertEquals(4, favorDAO.findFreeFavorId());
		assertTrue(favorDAO.createFavor(favor));
		assertEquals(num, imageDAO.findImageById(favor.getImageId()).getFavorNumber());
	}

	@Test
	public void testFindFavor() {

		ArrayList<Favor> result = favorDAO.findFavorByUid(2);
		assertEquals(1, (int) result.get(0).getImageId());
		assertEquals(2, (int) result.get(1).getImageId());
		result = favorDAO.findFavorByImageId(1);
		assertEquals(1, (int) result.get(0).getUid());
		assertEquals(2, (int) result.get(1).getUid());
		assertEquals(10, (int) result.get(2).getUid());
		assertEquals(12, (int) result.get(3).getUid());

	}

	@Test
	public void testCheckFavor(){
		assertTrue(favorDAO.checkFavor(2, 1));
	}
}