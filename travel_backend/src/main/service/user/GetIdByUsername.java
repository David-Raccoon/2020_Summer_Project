package main.service.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.UserDAO;
import main.domian.User;

@WebServlet("/GetIdByUsername")
public class GetIdByUsername extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetIdByUsername() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String username = new String(request.getParameter("username").getBytes("iso-8859-1"), "UTF-8");
		UserDAO userDAO = new UserDAO();
		User user = userDAO.findUserByUsername(username);
		if (user == null) {
			response.getWriter().print("User does not exist!");
		} else {
			response.getWriter().print(user.getUid());
		}
		userDAO.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
