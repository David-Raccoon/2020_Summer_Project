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

@WebServlet("/GetHottestImg")
public class GetHottestImg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetHottestImg() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		int count = Integer.valueOf(request.getParameter("count"));
		ImageDAO imageDAO = new ImageDAO();
		ArrayList<Image> hotImage = imageDAO.getHottestImage(count);
		imageDAO.close();
		MyUtils.sendArray(request, response, hotImage);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
