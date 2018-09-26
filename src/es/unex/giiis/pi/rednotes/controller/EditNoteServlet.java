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

import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class EditNoteServlet
 */
@WebServlet("/EditNoteServlet")
public class EditNoteServlet extends HttpServlet {
	
	public static int NEW_NOTE_ID=9999; /*if it changes, change also in Notes.jsp, to Add buttons*/
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditNoteServlet() {
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
		else { /*if we are register*/
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
			db.setConnection(conn);
						
			Integer id= Integer.parseInt(request.getParameter("id"));
			
			logger.info("ID de la nota solicitada:"+id);
			
			NoteComplete n;
			User user= (User) session.getAttribute("userlogin");

			if(id==NEW_NOTE_ID) { /*if the id is the id define to identificate a new note...*/
				n= new NoteComplete();
			}
			else { /*if it's other id, we find the note of this id*/
				n= db.getNote(id, user.getIdu());
				if(n.getType()==1) { //if the note is really a list
					response.sendRedirect("ListServlet?id="+id);
				}
			}
			
			List<NoteComplete> versionslist= new ArrayList<NoteComplete>();
			versionslist= db.getVersions(id);
			
			session.setAttribute("listversions", versionslist);
			
			/*we save the note like parameter to jsp*/
			session.setAttribute("note", n);
			
			/*we show the jsp with the previous content (if it's a new note, nothing)*/
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/EditNote.jsp");
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
		
		/*we get the last note register in session (the note that we show in GET)*/
		NoteComplete n= (NoteComplete) session.getAttribute("note");
		
		/*we get the user information*/
		User user = (User) session.getAttribute("userlogin");
		
		/*we show in logger the last data of the note*/
		logger.info("Last name:"+n.getTitle());
		logger.info("Last content:"+n.getContent());
		
		boolean modify=false;
		if(!request.getParameter("name").equals(n.getTitle()) || !request.getParameter("content").equals(n.getContent())) {
			modify=true;
		}
		
		/*we get the new values of the note*/
		n.setTitle(request.getParameter("name"));
		n.setContent(request.getParameter("content"));

		/*we show the new values of the note*/
		logger.info("New name:"+n.getTitle());
		logger.info("New content:"+n.getContent());
		
		if(n.getId()==NEW_NOTE_ID) { /*if the note if new*/
			
			n.setCreationDate(new Date());
			n.setModificationDate(new Date());
			n.setOwnerID(user.getIdu());
			n.setOwner(user.getUsername());
			n.setImageOwner(user.getImage());
			n.setAnchored(false);
			n.setArchived(false);
			n.setColor("yellow");
			n.setType(0);
			
			logger.info("USer login and owner: "+user.getUsername());
			n=db.addNote(n); //whe use this to get the note whit new id put
		}
		else { /*if the note exists yet*/
			n.setModificationDate(new Date());
			if(modify) {
				db.updateNote(n);
			}
		}
		
		logger.info("New id of note:"+n.getId());
		
		/*we show the note updated*/
		response.sendRedirect("NoteServlet?id="+n.getId());
	}

}
