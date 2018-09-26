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
 * Servlet implementation class FriendsServlet
 */
@WebServlet("/FriendsServlet")
public class FriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("The request was made using GET");
		boolean toPerfil=false;
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

			String username= request.getParameter("username");
			
			if(username!=null) { //if you want to see a friend's info
				logger.info("Username del amigo solicitado:"+username);
				
				if(username.equals(user.getUsername())) { //if solicited friend is you
					toPerfil=true;
				}
				else {//if solicited friend is not you
					logger.info("Buscando amigo '"+username+"'");
					//buscar amigo y registrarlo en session.attribute
					
					User friend= db.getFriend(user.getIdu(),username);
					
					List<NoteComplete> notes= db.getNotesSharedBy(user.getIdu(),friend.getIdu());
					
					request.setAttribute("notes", notes);
					request.setAttribute("friend", friend);
				}
				
			}
			else {	//DON'T SHOW ANY FRIEND OPEN
				
				//flag to indicate that we don't need to show a friend's information
				session.setAttribute("friend", "null");
			
			}
			
			if(toPerfil) {
				logger.info("Redirigiendo al perfil");
				response.sendRedirect("PerfilServlet");
			}
			else {
				/*I use this Attribute of flag for the first time i load the page*/
				String friendsorderby= (String) session.getAttribute("friendsorderby");
				
				if(friendsorderby==null) {
					/*Attributes for post options of notes*/
					session.setAttribute("friendsorderby", "name");
				}
				
				/*i get the option saved previously for the orden options*/
				friendsorderby= (String) session.getAttribute("friendsorderby");
				
				/*get all friends of database*/
				List<User> friends= db.getFriends(user.getIdu(), friendsorderby);
				
				session.setAttribute("friendslist", friends);
				
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Friends.jsp");
				view.forward(request,response);	
			
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
		db.setConnection(conn);
		
		List<User> friends= new ArrayList<User>();
		
		User user= (User)session.getAttribute("userlogin");
		
		request.setCharacterEncoding("UTF-8");
		
		logger.info("The request was made using POST");
		
		
		String orderby= request.getParameter("friendsorderby");
		
		logger.info("Friends options changed:");
		logger.info("-->>Order by: "+orderby);

		/*i save the new show options*/
		session.setAttribute("friendsorderby", orderby);

		/*we save each note of the database (of the user) in a list, to use this in Notes.jsp*/
		friends= db.getFriends(user.getIdu(), orderby);
		
		session.setAttribute("friends", friends);
		
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Friends.jsp");
		view.forward(request,response);	
	}

}
