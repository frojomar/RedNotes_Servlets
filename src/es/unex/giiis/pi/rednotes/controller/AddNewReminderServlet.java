package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.helper.NameComparator;
import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class AddNewReminderServlet
 */
@WebServlet("/AddNewReminderServlet")
public class AddNewReminderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewReminderServlet() {
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
			String idnString =request.getParameter("idn");
	
			if(idnString!=null) { //search in our friends
				
				Integer idn= Integer.parseInt(idnString);
				
				logger.info(">>Datos de nota a mostrar: "+idn);
	
				NoteComplete note = db.getNote(idn, user.getIdu());
				
				if(note!=null) {
					request.setAttribute("note", note);
				}
				else {
					messages.add(new String("The Note solicited don't exists in your list"));
					request.setAttribute("messages", messages);
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Reminders.jsp");
					view.forward(request,response);	
				}
			}
			else {
				request.setAttribute("note", "null");
			}
			
			ArrayList<NoteComplete> anchoredNotes= new ArrayList<NoteComplete>();
			ArrayList<NoteComplete> notAnchoredNotes= new ArrayList<NoteComplete>();
			ArrayList<NoteComplete> archivedNotes= new ArrayList<NoteComplete>();
			
			db.getNotes(user.getIdu(), anchoredNotes, notAnchoredNotes, archivedNotes, "name", "notesandlists", "all", "allcolors");
			
			List<NoteComplete> noteslist= new ArrayList<NoteComplete>();
			
			for(int i=0; i<anchoredNotes.size(); i++) {
				noteslist.add(anchoredNotes.get(i));
			}
			for(int i=0; i<notAnchoredNotes.size(); i++) {
				noteslist.add(notAnchoredNotes.get(i));
			}
			for(int i=0; i<archivedNotes.size(); i++) {
				noteslist.add(archivedNotes.get(i));
			}
			
			Collections.sort(noteslist, new NameComparator());
			
			session.setAttribute("noteslist", noteslist);
			
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/AddReminder.jsp");
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

		
		logger.info("IDN:"+request.getParameter("idn"));
		logger.info("IDU:"+request.getParameter("idu"));
		logger.info("Date:"+request.getParameter("date"));
		logger.info("DESCRIPTION:"+request.getParameter("description"));
		
		Integer idn= Integer.parseInt(request.getParameter("idn"));
		Integer idu= Integer.parseInt(request.getParameter("idu"));
		Date date= DateTimeHelper.StringToDate2(request.getParameter("date"));
		String description= request.getParameter("description");
		String confirmation=request.getParameter("confirm");
		
		logger.info("Date parsed:"+date);

		if(!confirmation.equals("")) {
			db.addReminder(idn, idu, date, description);
		}
		response.sendRedirect("RemindersServlet");
	}

}
