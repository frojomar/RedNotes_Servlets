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
 * Servlet implementation class SearchNoteServle
 */
@WebServlet("/SearchNoteServlet")
public class SearchNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("The request was made using GET");
		
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

			String search=request.getParameter("search");
			
			if(search!=null) {
				logger.info(">>Datos de nota a buscar: "+search);
				
				List<NoteComplete> notes= db.searchNotes(search, user.getIdu());
				
				if(notes.isEmpty()) {
					messages.add(new String("We have not found anything like what we wanted"));
					request.setAttribute("messages", messages);
					
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
					view.forward(request,response);	
				}
				else {
					request.setAttribute("notes", notes);
					
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/SearchNote.jsp");
					view.forward(request,response);	
				}
			}
			else {
				messages.add(new String("We don't know what want to search"));
				request.setAttribute("messages", messages);

				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
				view.forward(request,response);	
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
