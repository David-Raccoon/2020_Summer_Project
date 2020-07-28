package main.service.image;

import java.io.File;
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

@WebServlet("/UpdateImg")
@MultipartConfig
public class UpdateImg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateImg() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use POST method");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		int imageID = Integer.valueOf(request.getParameter("imageID"));
		ImageDAO imageDAO = new ImageDAO();
		Image image = imageDAO.findImageById(imageID);
		image.setDate(MyUtils.getImageDate());
		image.setTitle(request.getParameter("title"));
		image.setDescription(request.getParameter("description"));
		image.setCountry(request.getParameter("country"));
		image.setCity(request.getParameter("city"));
		image.setContent(request.getParameter("content"));

		if (request.getParameter("imgChange").equals("true")) {
			// 删除原path下的对应文件
			String path = imageDAO.findImageById(imageID).getPath();
			String originPath = getServletContext().getRealPath("/image/origin/") + "\\" + path;
			String squarePath = getServletContext().getRealPath("/image/square") + "\\" + path;

			File file = new File(originPath);
			file.delete();
			file = new File(squarePath);
			file.delete();

			// 获取新的path并创建文件
			path = UUID.randomUUID().toString() + ".jpg";
			originPath = getServletContext().getRealPath("/image/origin/") + "\\" + path;
			squarePath = getServletContext().getRealPath("/image/square") + "\\" + path;

			Part part = request.getPart("img");
			part.write(originPath);
			MyUtils.createSquareImg(originPath, squarePath);
			image.setPath(path);
		}
		if (imageDAO.updateImage(image)) {
			response.getWriter().print("success");
		} else {
			response.getWriter().print("error: fail to update the image");
		}
	}

}
