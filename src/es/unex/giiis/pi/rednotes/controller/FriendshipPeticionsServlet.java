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
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;

/**
 * Servlet implementation class FriendshipPeticionsServlet
 */
@WebServlet("/FriendshipPeticionsServlet")
public class FriendshipPeticionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendshipPeticionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		logger.info("The request was made using GET");
		
		HttpSession session = request.getSession();
		
		if(session.isNew()) {
			session.setAttribute("login", false);
		}
		
		if((boolean)session.getAttribute("login")==false || session.getAttribute("login")==null) {
			logger.info("Sesion nueva. Mandando a login");
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request,response);	
		}
		else {
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
			db.setConnection(conn);		
			
			User user= (User) session.getAttribute("userlogin");

			List<User> petitionsSent= db.getUserPetitionsSent(user.getIdu());
			List<User> petitionsReceived= db.getUserPetitionsReceived(user.getIdu());
			
			session.setAttribute("userspetitionssent", petitionsSent);
			session.setAttribute("userspetitionsreceived", petitionsReceived);
			
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Petitions.jsp");
			view.forward(request,response);	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("The request was made using POST");

		HttpSession session = request.getSession();

		List<String> messages= new ArrayList<String> ();
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
		db.setConnection(conn);
		
		request.setCharacterEncoding("UTF-8");
		
		User user= (User) session.getAttribute("userlogin");
		
		/*first, get the parameters*/
		String action= request.getParameter("action");
		Integer idu= Integer.parseInt(request.getParameter("idu"));
		String actbutton= request.getParameter("actbutton");
		
		/*second, select the action with Parameter "action"*/

		boolean ok=true;
		switch(action) {
			case "newPetition": //create a new petition to another user
				ok=db.createNewFriendship(user.getIdu(),idu);
				if(!ok) {
					messages.add(new String("We can't create the petition of friendship. Try again"));
				}
				break;
				
			case "revision": //delete a petition sent previously
				ok=db.deleteFriendship(user.getIdu(), idu);
				if(!ok) {
					messages.add(new String("We can't delete the petition of friendship. Try again"));
				}
				break;
				
			case "confirmation": //confirmate or delete a petition received (depend of value of "actbutton")
				switch(actbutton) {
					case "Confirm":
						ok=db.confirmFriendship(idu, user.getIdu());
						if(!ok) {
							messages.add(new String("We can't confirm the petition of friendship. Try again"));
						}
						break;
					case "Delete":
						ok=db.deleteFriendship(idu, user.getIdu());
						if(!ok) {
							messages.add(new String("We can't delete the petition of friendship. Try again"));
						}
						break;
				}
				break;
		}
		
		if(!ok) {
			request.setAttribute("messages", messages);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Petitions.jsp");
			view.forward(request,response);	
		}
		/*Redirect to the GET method of the this same Servlet*/
		response.sendRedirect("FriendshipPeticionsServlet");
	}

}
