package main.service.image;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.ImageDAO;
import main.domian.Image;
import main.service.MyUtils;

@WebServlet("/GetImgByImageID")
public class GetImgByImageID extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetImgByImageID() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		ImageDAO imageDAO = new ImageDAO();
		Image image = imageDAO.findImageById(Integer.valueOf(request.getParameter("id")));
		imageDAO.close();
		MyUtils.sendObject(request, response, image);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("error: please use GET method");
	}

}
