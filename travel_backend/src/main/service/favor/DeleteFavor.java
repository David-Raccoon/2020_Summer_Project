package main.service.favor;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FavorDAO;
import main.dao.ImageDAO;
import main.domian.Image;

/**
 * Servlet implementation class DeleteFavor
 */
@WebServlet("/DeleteFavor")
public class DeleteFavor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteFavor() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		int uid = Integer.valueOf(request.getParameter("uid"));
		int imageID = Integer.valueOf(request.getParameter("imageID"));
		FavorDAO favorDAO = new FavorDAO();
		if (favorDAO.deleteFavor(imageID, uid)) {
			ImageDAO imageDAO = new ImageDAO();
			Image image = imageDAO.findImageById(imageID);
			image.setFavorNumber(image.getFavorNumber() - 1);
			imageDAO.updateImage(image);
			imageDAO.close();
			response.getWriter().print("success");
		} else {
			response.getWriter().print("error: fail to delete the favor");
		}
		favorDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
