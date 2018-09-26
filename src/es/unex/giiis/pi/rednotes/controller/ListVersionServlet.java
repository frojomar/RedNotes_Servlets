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

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.helper.IntegerStringPair;
import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;

/**
 * Servlet implementation class ListVersionServlet
 */
@WebServlet("/ListVersionServlet")
public class ListVersionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
			
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListVersionServlet() {
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

			Integer id= Integer.parseInt(request.getParameter("id"));
			String date=request.getParameter("date");
			
			if(id!=null && date!=null) {
				logger.info("ID de la nota solicitada:"+id);
				logger.info("Version de la nota solicitada:"+date);
				
				if(date=="") {
					response.sendRedirect("ListServlet?id="+id);
				}
				else {
					Date versionDate= DateTimeHelper.StringToDate(date);
					
					NoteComplete n= db.getVersionNote(id, user.getIdu(),versionDate);
					
					if(n!=null) {
						if(n.getType()==0) { //if the note is really a note
							response.sendRedirect("VersionServlet?id="+id+"&date="+date);
						}
						else {
						
							List<IntegerStringPair> elements=IntegerStringPair.parseToListOfElements(n.getContent());
						
							List<NoteComplete> versionslist= new ArrayList<NoteComplete>();
							versionslist= db.getVersions(id);
							
							session.setAttribute("listversions", versionslist);
							request.setAttribute("note", n);
							request.setAttribute("listelements", elements);
							
							RequestDispatcher view = request.getRequestDispatcher("WEB-INF/List.jsp");
							view.forward(request,response);	
							}
						}
					else {
						messages.add(new String("There is no such note version in your list of notes"));
						RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
						view.forward(request,response);						
					}
				}
			}
			else {
				messages.add(new String("You need to specify the id and date of the note you want"));
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
