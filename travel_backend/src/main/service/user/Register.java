package main.service.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.dao.UserDAO;
import main.domian.User;
import main.service.MyUtils;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserDAO userDAO = new UserDAO();
		if (userDAO.findUserByUsername(request.getParameter("checkName")) == null) {
			response.getWriter().print(true);
		} else {
			response.getWriter().print(false);
		}
		userDAO.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserDAO userDAO = new UserDAO();
		User user = new User();
		user.setUid(userDAO.findFreeUid());
		user.setUsername(request.getParameter("username"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		user.setSignUpDate(MyUtils.getUserDate());
		boolean r = userDAO.createUser(user);
		if (r) {
			response.getWriter().print("success");
		} else {
			response.getWriter().print("error: fail to insert user data");
		}
		userDAO.close();
	}

}
