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

@WebServlet("/GetImageByUid")
public class GetImageByUid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetImageByUid() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		ImageDAO imageDAO = new ImageDAO();
		ArrayList<Image> image = imageDAO.findImageByUid(Integer.valueOf(request.getParameter("uid")));
		imageDAO.close();
		MyUtils.sendArray(request, response, image);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
