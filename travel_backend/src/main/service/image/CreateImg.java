package main.service.image;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.dao.ImageDAO;
import main.domian.Image;
import main.service.MyUtils;

@WebServlet("/CreateImg")
@MultipartConfig
public class CreateImg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CreateImg() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use POST method");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		request.setCharacterEncoding("UTF-8");
		Image image = new Image();
		ImageDAO imageDAO = new ImageDAO();
		image.setImageID(imageDAO.findFreeImageId());
		image.setDate(MyUtils.getImageDate());
		image.setFavorNumber(0);
		image.setUid(Integer.valueOf(request.getParameter("uid")));
		image.setTitle(request.getParameter("title"));
		image.setDescription(request.getParameter("description"));
		image.setCountry(request.getParameter("country"));
		image.setCity(request.getParameter("city"));
		image.setContent(request.getParameter("content"));

		// 在一个新的path下创建文件
		Part part = request.getPart("img");
		String path = UUID.randomUUID().toString() + ".jpg";

		String originPath = getServletContext().getRealPath("/image/origin/") + "\\" + path;
		String squarePath = getServletContext().getRealPath("/image/square") + "\\" + path;

		part.write(getServletContext().getRealPath("/image/origin/") + "\\" + path);
		MyUtils.createSquareImg(originPath, squarePath);
		image.setPath(path);

		if (imageDAO.createImage(image)) {
			response.getWriter().print("success");
		} else {
			response.getWriter().print("error: fail to insert the image");
		}
		imageDAO.close();
	}

}
