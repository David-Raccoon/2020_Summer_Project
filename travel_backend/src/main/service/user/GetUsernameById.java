package main.service.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.UserDAO;

@WebServlet("/GetUsernameById")
public class GetUsernameById extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetUsernameById() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		int uid = Integer.valueOf(request.getParameter("id"));
		UserDAO userDAO = new UserDAO();
		String username = userDAO.findUserByUid(uid).getUsername();
		userDAO.close();
		response.getWriter().print(username);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("error: please use GET method");
	}

}
