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

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class CheckLoginServlet
 */
@WebServlet("/NotesServlet")
public class NotesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotesServlet() {
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
			
			/*I use this Attribute of flag for the first time i load the page*/
			String orderby= (String) session.getAttribute("orderby");
			
			if(orderby==null) {
				/*Attributes for post options of notes*/
				session.setAttribute("orderby", "creation");
				session.setAttribute("show1", "notesandlists");
				session.setAttribute("show2", "all");	
				session.setAttribute("colorfilter", "allcolors");	
			}
			
			/*i get the options saved previously for the show options*/
			orderby= (String) session.getAttribute("orderby");
			String show1= (String) session.getAttribute("show1");
			String show2= (String) session.getAttribute("show2");
			String colorfilter= (String) session.getAttribute("colorfilter");


			ArrayList<NoteComplete> AnchoredNotes= new ArrayList<NoteComplete>();
			ArrayList<NoteComplete> notAnchoredNotes= new ArrayList<NoteComplete>();
			ArrayList<NoteComplete> ArchivedNotes= new ArrayList<NoteComplete>();
						
			/*we save each note of the database (of the user) in a list, to use this in Notes.jsp*/
			db.getNotes(user.getIdu(), AnchoredNotes, notAnchoredNotes, ArchivedNotes, orderby,
					show1,show2, colorfilter);

			
			session.setAttribute("anchorednotes", AnchoredNotes);
			session.setAttribute("notanchorednotes", notAnchoredNotes);
			session.setAttribute("archivednotes", ArchivedNotes);
			
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
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
		
		ArrayList<NoteComplete> AnchoredNotes= new ArrayList<NoteComplete>();
		ArrayList<NoteComplete> notAnchoredNotes= new ArrayList<NoteComplete>();
		ArrayList<NoteComplete> ArchivedNotes= new ArrayList<NoteComplete>();
		
		User user= (User)session.getAttribute("userlogin");
		
		request.setCharacterEncoding("UTF-8");
		
		logger.info("The request was made using POST");
		
		
		String orderby= request.getParameter("orderby");
		String show1= request.getParameter("show1");
		String show2= request.getParameter("show2");
		String colorfilter= request.getParameter("colorfilter");
		
		logger.info("Notes options changed:");
		logger.info("-->>Order by: "+orderby);
		logger.info("-->>Show 1: "+show1);
		logger.info("-->>Show 2: "+show2);
		logger.info("-->>Color filter: "+colorfilter);
		
		/*i save the new show options*/
		session.setAttribute("orderby", orderby);
		session.setAttribute("show1", show1);
		session.setAttribute("show2", show2);
		session.setAttribute("colorfilter", colorfilter);
		
		response.sendRedirect("NotesServlet");
	}

}
