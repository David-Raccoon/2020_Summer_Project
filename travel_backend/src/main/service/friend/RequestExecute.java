package main.service.friend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.FriendDAO;
import main.domian.Friend;

@WebServlet("/RequestExecute")
public class RequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestExecute() {
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

		// 发送请求
		if (action.equals("send")) {
			if (currentFriend == null) {
				Friend friend = new Friend();

				int friendID = friendDAO.findFreeFriendId();
				friend.setFriendId(friendID);
				friend.setUid1(uid1);
				friend.setUid2(uid2);
				friend.setState("inviting");
				friendDAO.createFriend(friend);

				friendID = friendDAO.findFreeFriendId();
				friend.setFriendId(friendID);
				friend.setUid1(uid2);
				friend.setUid2(uid1);
				friend.setState("invited");
				friendDAO.createFriend(friend);
				
				result = "success";
			} else {
				result = "illegal action: you already sent the request or you're already friends";
			}
		}

		// 接受请求
		if (action.equals("accept")) {
			if (currentFriend != null && currentFriend.getState().equals("invited")) {
				friendDAO.updateFriendState(currentFriend.getFriendId(), "unlimited");
				int anotherFriendID = friendDAO.findFriendByUid(currentFriend.getUid2(), currentFriend.getUid1())
						.getFriendId();
				friendDAO.updateFriendState(anotherFriendID, "unlimited");
				
				result = "success";
			} else {
				result = "illegal action: no such request";
			}
		}

		// 拒绝请求
		if (action.equals("reject")) {
			if (currentFriend != null && currentFriend.getState().equals("invited")) {
				friendDAO.deleteFriend(currentFriend.getFriendId());
				int anotherFriendID = friendDAO.findFriendByUid(currentFriend.getUid2(), currentFriend.getUid1())
						.getFriendId();
				friendDAO.deleteFriend(anotherFriendID);

				result = "success";
			} else {
				result = "illegal action: no such request";
			}
		}

		response.getWriter().print(result);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
