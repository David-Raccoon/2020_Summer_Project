package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import main.dao.ImageDAO;
import main.domian.Image;

public class TestImageDAO {
	private static ImageDAO imageDAO = new ImageDAO();

	@Test
	public void testFindImageById() {
		Image testImage = imageDAO.findImageById(1);
		assertEquals("Edworthy Park", testImage.getTitle());
		assertEquals("Calgary Children's Hospital from Edworthy Park", testImage.getDescription());
		assertEquals("6592902825.jpg", testImage.getPath());
		assertEquals(1, testImage.getUid());
		assertEquals(1, testImage.getImageID());
		assertEquals("scenery", testImage.getContent());
		assertEquals("Canada", testImage.getCountry());
		assertEquals("Calgary", testImage.getCity());
		assertEquals("2020-07-19 15:50:04", testImage.getDate());
		assertEquals(4, testImage.getFavorNumber());

	}

//	@Test
//	public void testFindImageByTitle() {
//		Image testImage = imageDAO.findImageByTitle("Park").get(0);
//		assertEquals("Edworthy Park", testImage.getTitle());
//		assertEquals("Calgary Children's Hospital from Edworthy Park", testImage.getDescription());
//		assertEquals("6592902825.jpg", testImage.getPath());
//		assertEquals(1, testImage.getUid());
//		assertEquals(1, testImage.getImageID());
//		assertEquals("scenery", testImage.getContent());
//		assertEquals("Canada", testImage.getCountry());
//		assertEquals("Calgary", testImage.getCity());
//		assertEquals("2020-07-19 15:50:04", testImage.getDate());
//		assertEquals(4, testImage.getFavorNumber());
//	}
//
//	@Test
//	public void testFindImageByDescription() {
//		Image testImage = imageDAO.findImageByDescription("Hospital").get(0);
//		assertEquals("Edworthy Park", testImage.getTitle());
//		assertEquals("Calgary Children's Hospital from Edworthy Park", testImage.getDescription());
//		assertEquals("6592902825.jpg", testImage.getPath());
//		assertEquals(1, testImage.getUid());
//		assertEquals(1, testImage.getImageID());
//		assertEquals("scenery", testImage.getContent());
//		assertEquals("Canada", testImage.getCountry());
//		assertEquals("Calgary", testImage.getCity());
//		assertEquals("2020-07-19 15:50:04", testImage.getDate());
//		assertEquals(4, testImage.getFavorNumber());
//	}

	@Test
	public void testCreate_Delete_Update_Image() {
		Image image = imageDAO.findImageById(1);
		assertTrue(imageDAO.deleteImageById(1));
		assertTrue(imageDAO.createImage(image));
		assertNotNull(image = imageDAO.findImageById(1));
		assertEquals("Edworthy Park", image.getTitle());
		assertEquals("Calgary Children's Hospital from Edworthy Park", image.getDescription());
		assertEquals("6592902825.jpg", image.getPath());
		assertEquals(1, image.getUid());
		assertEquals(1, image.getImageID());
		assertEquals("scenery", image.getContent());
		assertEquals("Canada", image.getCountry());
		assertEquals("Calgary", image.getCity());
		assertEquals("2020-07-19 15:50:04", image.getDate());
		assertEquals(4, image.getFavorNumber());
		image.setTitle("Park");
		assertTrue(imageDAO.updateImage(image));
		assertEquals("Park", imageDAO.findImageById(1).getTitle());
		image.setTitle("Edworthy Park");
		assertTrue(imageDAO.updateImage(image));
	}

	@Test
	public void testFindFreeImageID() {
		Image image = imageDAO.findImageById(2);
		assertNotNull(image);
		assertTrue(imageDAO.deleteImageById(2));
		assertEquals(2, imageDAO.findFreeImageId());
		assertTrue(imageDAO.createImage(image));
	}

	@Test
	public void testHomePageImage() {
		ArrayList<Image> result = imageDAO.getHottestImage(5);
		assertEquals(5, result.size());
		result = imageDAO.getLatestImage(5);
		assertEquals(5, result.size());
	}

}
