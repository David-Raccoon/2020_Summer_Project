package main.service.image;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.ImageDAO;
import main.domian.Image;
import main.service.MyUtils;

@WebServlet("/SearchByDescription")
public class SearchByDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchByDescription() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		String keyword = new String(request.getParameter("keyword").getBytes("iso-8859-1"), "UTF-8");
		String sort = new String(request.getParameter("sort").getBytes("iso-8859-1"), "UTF-8");
		ImageDAO imageDAO = new ImageDAO();
		ArrayList<Image> hotImage = imageDAO.findImageByDescription(keyword, sort);
		MyUtils.sendArray(request, response, hotImage);
		imageDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
