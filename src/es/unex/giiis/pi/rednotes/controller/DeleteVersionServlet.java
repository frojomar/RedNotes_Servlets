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
import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class DeleteVersionServlet
 */
@WebServlet("/DeleteVersionServlet")
public class DeleteVersionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteVersionServlet() {
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
					response.sendRedirect("NoteServlet");
				}
				else {
					Date versionDate= DateTimeHelper.StringToDate(date);
					
					NoteComplete n= db.getVersionNote(id, user.getIdu(),versionDate);
					
					if(n!=null) {
						session.setAttribute("note", n);
						
						RequestDispatcher view = request.getRequestDispatcher("WEB-INF/DeleteVersion.jsp");
						view.forward(request,response);	
					}
					else {
						messages.add(new String("There is no such note version in your list of notes"));
						response.sendRedirect("NotesServlet");					
					}
				}
				
			}
			else {
				messages.add(new String("You need to specify the id and date of the note version you want to delete"));
				response.sendRedirect("NotesServlet");
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
				
		request.setCharacterEncoding("UTF-8");
		
		logger.info("The request was made using POST");
		
		UserLogin user= (UserLogin) session.getAttribute("userlogin");

		
		String confirmation=request.getParameter("confirm");

		logger.info("Parameter:: "+confirmation);
		Integer id= Integer.parseInt(request.getParameter("id"));

		if(confirmation.equals("Yes, I am sure")) {
			Date versionDate= DateTimeHelper.StringToDate(request.getParameter("date"));
			db.dropVersionNote(id, versionDate);
		}
			response.sendRedirect("NoteServlet?id="+id);
		
	}

}
