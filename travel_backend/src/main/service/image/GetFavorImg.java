package main.service.image;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FavorDAO;
import main.dao.FriendDAO;
import main.dao.ImageDAO;
import main.domian.Favor;
import main.domian.Friend;
import main.domian.Image;
import main.service.MyUtils;

@WebServlet("/GetFavorImg")
public class GetFavorImg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetFavorImg() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		ImageDAO imageDAO = new ImageDAO();
		FavorDAO favorDAO = new FavorDAO();
		int uid1 = Integer.valueOf(request.getParameter("uid1"));
		int uid2 = Integer.valueOf(request.getParameter("uid2"));
		// 访问自己的收藏
		if (uid1 == uid2) {
			ArrayList<Favor> favor = favorDAO.findFavorByUid(uid2);
			ArrayList<Image> image = new ArrayList<Image>();
			for (Favor f : favor) {
				image.add(imageDAO.findImageById(f.getImageId()));
			}
			MyUtils.sendArray(request, response, image);
		}

		// 访问别人的收藏
		else {
			FriendDAO friendDAO = new FriendDAO();
			Friend friend = friendDAO.findFriendByUid(uid1, uid2);
			if (friend != null && friend.getState().equals("unlimited")) {
				ArrayList<Favor> favor = favorDAO.findFavorByUid(uid2);
				ArrayList<Image> image = new ArrayList<Image>();
				for (Favor f : favor) {
					image.add(imageDAO.findImageById(f.getImageId()));
				}
				MyUtils.sendArray(request, response, image);
			}

			else {
				response.getWriter().print("denied");
			}

			friendDAO.close();
		}
		imageDAO.close();
		favorDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
