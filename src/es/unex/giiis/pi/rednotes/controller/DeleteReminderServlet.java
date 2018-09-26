package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.logging.Logger;

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
 * Servlet implementation class DeleteReminderServlet
 */
@WebServlet("/DeleteReminderServlet")
public class DeleteReminderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteReminderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		
		
		/*we get the user information*/
		User user = (User) session.getAttribute("userlogin");
		
		/*we get the id of note*/
		Integer idn= Integer.parseInt(request.getParameter("idn"));

		/*we get the id of user*/
		Integer idu= Integer.parseInt(request.getParameter("idu"));
		
		/*we get the date of reminder*/
		Date date= DateTimeHelper.StringToDate(request.getParameter("date"));
		
		logger.info("date to delete in string "+request.getParameter("date"));
		logger.info("date to delete "+date);
		
		logger.info("Solicited to delete the reminder:"+idn);

		db.deleteReminder(idu, idn, date);	
		
		/*we show the note updated*/
		response.sendRedirect("RemindersServlet");
		
	}

}
