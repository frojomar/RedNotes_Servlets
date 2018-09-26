package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
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
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class DeleteFriendServlet
 */
@WebServlet("/DeleteFriendsServlet")
public class DeleteFriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFriendsServlet() {
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
		
		if((boolean)session.getAttribute("login")==false) {
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request,response);	
		}
		else {
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
			db.setConnection(conn);
			
			User user= (User) session.getAttribute("userlogin");

						
			Integer id= Integer.parseInt(request.getParameter("id"));
			
			logger.info("ID del amigo a eliminar:"+id);
			
			User u= db.getFriend(user.getIdu(),id);

			request.setAttribute("friend", u);
			
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/DeleteFriend.jsp");
			view.forward(request,response);	
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
				
		request.setCharacterEncoding("UTF-8");
		
		logger.info("The request was made using POST");
		
		UserLogin user= (UserLogin) session.getAttribute("userlogin");

		
		String confirmation=request.getParameter("confirm");

		logger.info("Parameter:: "+confirmation);
		
		if(confirmation.equals("Yes, I am sure")) {
			Integer id= Integer.parseInt(request.getParameter("id"));
			db.deleteSharedNotesWith(user.getIdu(), id);
			db.deleteFriendship(user.getIdu(),id);
		}
			response.sendRedirect("FriendsServlet");
		
	}

}
