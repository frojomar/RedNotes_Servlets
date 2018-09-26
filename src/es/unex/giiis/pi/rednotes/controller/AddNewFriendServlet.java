package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
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
import es.unex.giiis.pi.rednotes.model.User;

/**
 * Servlet implementation class AddNewFriendServlet
 */
@WebServlet("/AddNewFriendServlet")
public class AddNewFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());  
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> messages= new ArrayList<String>();
		
		HttpSession session = request.getSession();
		
		if(session.isNew()) {
			session.setAttribute("login", false);
		}
		
		if((boolean)session.getAttribute("login")==false) {
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request,response);	
		}
		else {
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
			db.setConnection(conn);
			
			User user = (User) session.getAttribute("userlogin");
	
			String username=request.getParameter("username");
		

			if(username!=null) { 
				logger.info("selected a user to add to friend list");

				User u=  db.getUser(username);
				session.setAttribute("user", u);
			}
			
			List<User> users= db.getNotFriendsUsers(user.getIdu());	
			session.setAttribute("friendslist", users);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/SearchFriends.jsp");
			view.forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> messages= new ArrayList<String>();
		
		HttpSession session = request.getSession();
		
		if(session.isNew()) {
			session.setAttribute("login", false);
		}
		
		if((boolean)session.getAttribute("login")==false) {
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request,response);	
		}
		else {
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
			db.setConnection(conn);
			
			User user = (User) session.getAttribute("userlogin");
			
			logger.info("search WITH filtered");
			String name=request.getParameter("name");
			String username=request.getParameter("username");
			String city=request.getParameter("city");
			String country=request.getParameter("country");

			List<User> users= db.getNotFriendsFiltered(user.getIdu(),name, username, city, country);
			
			session.setAttribute("friendslist", users);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/SearchFriends.jsp");
			view.forward(request,response);	
		}

	}

}
