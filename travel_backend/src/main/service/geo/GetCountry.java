package main.service.geo;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.GeoDAO;
import main.service.MyUtils;

@WebServlet("/GetCountry")
public class GetCountry extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetCountry() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		GeoDAO geoDAO = new GeoDAO();
		MyUtils.sendArray(request, response, geoDAO.findAllCountry());
		geoDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
