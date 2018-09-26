package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

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
 * Servlet implementation class PinnedArchivedServlet
 */
@WebServlet("/PinnedArchivedServlet")
public class PinnedArchivedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PinnedArchivedServlet() {
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
		
		User user= (User) session.getAttribute("userlogin");
		
		
		logger.info("ID:"+request.getParameter("id"));
		logger.info("PINNED:"+request.getParameter("anchored"));
		logger.info("ARCHIVED:"+request.getParameter("archived"));

		if(request.getParameter("id")!=null) {
			logger.info("Archived or pinned the note: "+Integer.parseInt(request.getParameter("id")));
			Integer id= Integer.parseInt(request.getParameter("id"));
			NoteComplete n=db.getNote(id, user.getIdu());
			if(request.getParameter("anchored")!=null) {
				logger.info("pinned the note whit value: "+Boolean.parseBoolean(request.getParameter("anchored")));
				n.setAnchored(Boolean.parseBoolean(request.getParameter("anchored")));
				logger.info(">>"+n.getAnchored());
			}
			if(request.getParameter("archived")!=null) {
				logger.info("Archived the note with value: "+Boolean.parseBoolean(request.getParameter("archived")));
				n.setArchived(Boolean.parseBoolean(request.getParameter("archived")));
				logger.info(">>"+n.getArchived());
			}
			db.updateNoteforAUser(n,user.getIdu());
		}
		
		response.sendRedirect("NotesServlet");
	}

}
