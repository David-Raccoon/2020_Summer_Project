package main.service.favor;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FavorDAO;
import main.dao.ImageDAO;
import main.domian.Favor;
import main.domian.Image;

/**
 * Servlet implementation class CreateFavor
 */
@WebServlet("/CreateFavor")
public class CreateFavor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateFavor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		int uid = Integer.valueOf(request.getParameter("uid"));
		int imageID = Integer.valueOf(request.getParameter("imageID"));
		Favor favor = new Favor();
		FavorDAO favorDAO = new FavorDAO();
		favor.setUid(uid);
		favor.setImageId(imageID);
		favor.setFavorId(favorDAO.findFreeFavorId());
		if (favorDAO.createFavor(favor)) {
			ImageDAO imageDAO = new ImageDAO();
			Image image = imageDAO.findImageById(imageID);
			image.setFavorNumber(image.getFavorNumber() + 1);
			imageDAO.updateImage(image);
			imageDAO.close();
			response.getWriter().print("success");
		} else {
			response.getWriter().print("error: fail to delete the favor");
		}
		favorDAO.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("error: please use GET method");
	}

}
