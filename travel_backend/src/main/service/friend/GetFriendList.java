package main.service.friend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FriendDAO;
import main.dao.UserDAO;
import main.domian.Friend;
import main.domian.User;
import main.service.MyUtils;

@WebServlet("/GetFriendList")
public class GetFriendList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetFriendList() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		FriendDAO friendDAO = new FriendDAO();
		UserDAO userDAO = new UserDAO();
		int uid = Integer.valueOf(request.getParameter("uid"));
		String state = request.getParameter("state");
		ArrayList<Friend> friends = friendDAO.findFriendByUid1(uid);
		ArrayList<User> users = new ArrayList<User>();
		for (Friend friend : friends) {
			if (friend.getState().equals(state)) {
				User user = userDAO.findUserByUid(friend.getUid2());
				user.setPassword(null);
				users.add(user);
			}
		}
		MyUtils.sendArray(request, response, users);
		friendDAO.close();
		userDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
