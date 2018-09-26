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
 * Servlet implementation class ShareNoteServlet
 */
@WebServlet("/ShareNoteServlet")
public class ShareNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShareNoteServlet() {
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
			Integer idn= Integer.parseInt(request.getParameter("id"));
			
			List<User> friends= db.getPossibleShareFriends(user.getIdu(), idn, "name");
			
			session.setAttribute("friendslist", friends);	
			session.setAttribute("idnote", idn);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/ShareNote.jsp");
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
		Integer idn= Integer.parseInt(request.getParameter("idn"));
		Integer idu= Integer.parseInt(request.getParameter("idu"));
		String actbutton= request.getParameter("actbutton");
		
		/*second, selectshare the note with "idu"*/

		boolean ok=true;
		
		NoteComplete n=db.getNote(idn, idu);
		NoteComplete n2=db.getNote(idn, user.getIdu());
		
		logger.info("Compartiendo note "+idn+" con usuario idu:"+idu);
		
		if(n==null && n2.getOwnerID()==user.getIdu()) {
			ok= db.shareNote(user.getIdu(), idn, idu);
		}
		else {
			messages.add(new String("You are not the owner of this note. You can not share it."));
			request.setAttribute("messages", messages);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
			view.forward(request,response);	
		}
		if(!ok) {
			messages.add(new String("We can't share the note. Try again after"));
			request.setAttribute("messages", messages);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
			view.forward(request,response);	
		}
		else {
			/*Redirect to the GET method of the this same Servlet*/
			response.sendRedirect("NotesServlet");
		}
	}

}
