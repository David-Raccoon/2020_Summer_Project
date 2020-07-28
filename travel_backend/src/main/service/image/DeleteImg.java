package main.service.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FavorDAO;
import main.dao.ImageDAO;
import main.domian.Favor;

@WebServlet("/DeleteImg")
public class DeleteImg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteImg() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		ImageDAO imageDAO = new ImageDAO();
		int imageId = Integer.valueOf(request.getParameter("imageID"));
		String path = imageDAO.findImageById(imageId).getPath();

		String originPath = getServletContext().getRealPath("/image/origin/") + "\\" + path;
		String squarePath = getServletContext().getRealPath("/image/square") + "\\" + path;

		File file = new File(originPath);
		file.delete();
		file = new File(squarePath);
		file.delete();

		if (imageDAO.deleteImageById(imageId)) {
			// 处理收藏数据
			FavorDAO favorDAO = new FavorDAO();
			ArrayList<Favor> favors = favorDAO.findFavorByImageId(imageId);
			for (int i = 0; i < favors.size(); i++) {
				favorDAO.deleteFavor(favors.get(i).getImageId(), favors.get(i).getUid());
			}

			response.getWriter().print("success");
			favorDAO.close();
		}

		else {
			response.getWriter().print("error: fail to delete the image");
		}
		imageDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("error: please use GET method");
	}

}
