package main.service.friend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FriendDAO;
import main.domian.Friend;

@WebServlet("/FriendExecute")
public class FriendExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FriendExecute() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");

		String action = request.getParameter("action");
		int uid1 = Integer.valueOf(request.getParameter("uid1"));
		int uid2 = Integer.valueOf(request.getParameter("uid2"));

		FriendDAO friendDAO = new FriendDAO();
		Friend currentFriend = friendDAO.findFriendByUid(uid1, uid2);
		String result = "error: unknown action";

		// 限制好友访问
		if (action.equals("limit")) {
			if (currentFriend != null && currentFriend.getState().equals("unlimited")) {
				friendDAO.updateFriendState(currentFriend.getFriendId(), "limited");
				result = "success";
			} else {
				result = "illegal action: no such friend or already limited";
			}
		}

		// 拒绝请求
		if (action.equals("unlimit")) {
			if (currentFriend != null && currentFriend.getState().equals("limited")) {
				friendDAO.updateFriendState(currentFriend.getFriendId(), "unlimited");
				result = "success";
			} else {
				result = "illegal action: no such friend or already unlimited";
			}
		}

		// 删除好友
		if (action.equals("delete")) {
			if (currentFriend != null
					&& (currentFriend.getState().equals("limited") || currentFriend.getState().equals("unlimited"))) {
				friendDAO.deleteFriend(currentFriend.getFriendId());
				int anotherFriendID = friendDAO.findFriendByUid(currentFriend.getUid2(), currentFriend.getUid1())
						.getFriendId();
				friendDAO.deleteFriend(anotherFriendID);
				result = "success";
			} else {
				result = "illegal action: no such friend";
			}
		}

		response.getWriter().print(result);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
