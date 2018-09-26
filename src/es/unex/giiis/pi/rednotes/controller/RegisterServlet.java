package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("The request was made using GET");
		HttpSession session = request.getSession();
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
		db.setConnection(conn);
		
		if(session.isNew()) {
			session.setAttribute("login", false);
		}
		
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Register.jsp");
		view.forward(request,response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
		db.setConnection(conn);
		
		List<String> messages = new ArrayList<String>();

		request.setCharacterEncoding("UTF-8");
		
		logger.info("The request was made using POST");
		
		UserLogin user = new UserLogin();
		
		if(!((String)request.getParameter("password")).equals((String)request.getParameter("password2"))){
			messages.add("The password is not equals in 'Repeat Password' field");
			
			request.setAttribute("messages",messages);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Register.jsp");
			view.forward(request,response);
		}
		else {
			user.setUsername(request.getParameter("username"));
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			user.setDate(new Date());
			
			
			/*Validate of data format and data don't exist in the database yet*/
			if (user.validateEmail(messages) && user.validatePassword(messages) &&
					user.validateUsername(messages) && db.dataRegisterValid(user,messages)) {
				logger.info(">>Registrando al nuevo usuario: "+user.getUsername());
				db.addNewUser(user);
				request.setAttribute("username", user.getUsername());
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/RegisterComplete.jsp");
				view.forward(request,response);	
			} 
			else {
				request.setAttribute("messages",messages);
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Register.jsp");
				view.forward(request,response);
			}	
		}

	}

}
