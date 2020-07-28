package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import main.dao.GeoDAO;

public class TestGeoDAO {
	private static GeoDAO geoDao = new GeoDAO();

	@Test
	public void testCityCodeToName() {
		assertEquals("Canada", geoDao.cityCodeToName("2520397"));
	}

	@Test
	public void testCityNameToCode() {
		assertEquals("2520397", geoDao.cityNameToCode("Canada"));
	}

	@Test
	public void testCountryCodeToName() {
		assertEquals("Andorra", geoDao.countryCodeToName("AD"));
	}

	@Test
	public void testCountryNameToCode() {
		assertEquals("AD", geoDao.countryNameToCode("Andorra"));
	}

	@Test
	public void testFindCitiesOfCountry() {
		ArrayList<String> cities = geoDao.findCitiesOfCountry("United States");
		Boolean flag = false;
		for (String city : cities) {
			if (city.equals("Washington")) {
				flag = true;
			}
		}
		assertTrue(flag);
	}

}
