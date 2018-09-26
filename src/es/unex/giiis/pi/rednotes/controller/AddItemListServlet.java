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

import es.unex.giiis.pi.rednotes.helper.IntegerStringPair;
import es.unex.giiis.pi.rednotes.model.DataBaseManager;
import es.unex.giiis.pi.rednotes.model.NoteComplete;
import es.unex.giiis.pi.rednotes.model.User;

/**
 * Servlet implementation class AddItemListServlet
 */
@WebServlet("/AddItemListServlet")
public class AddItemListServlet extends HttpServlet {
	public static int NEW_NOTE_ID=9999; /*if it changes, change also in Notes.jsp, to Add buttons*/

	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddItemListServlet() {
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
			
			if(id!=null) {
				logger.info("ID de la nota solicitada:"+id);
				
				if(id==NEW_NOTE_ID) { /*if the id is the id define to identificate a new note...*/
					NoteComplete n= new NoteComplete();
					n.setType(1);
					
					List<NoteComplete> versionslist= new ArrayList<NoteComplete>();
					List<IntegerStringPair> elements= new ArrayList<IntegerStringPair>();
					
					session.setAttribute("listversions", versionslist);
					request.setAttribute("note", n);
					request.setAttribute("listelements", elements);
					
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/AddItemList.jsp");
					view.forward(request,response);	
				}
				else {/*we found the note that have the "idn" equals to id*/
					NoteComplete n= db.getNote(id, user.getIdu());
					
					if(n!=null) {
						if(n.getType()==0) { //if the note is not really a list
							response.sendRedirect("NoteServlet?id="+id);
						}
						else {
							
							List<IntegerStringPair> elements=IntegerStringPair.parseToListOfElements(n.getContent());
						
							List<NoteComplete> versionslist= new ArrayList<NoteComplete>();
							versionslist= db.getVersions(id);
							
							session.setAttribute("listversions", versionslist);
							request.setAttribute("note", n);
							request.setAttribute("listelements", elements);
							
							RequestDispatcher view = request.getRequestDispatcher("WEB-INF/AddItemList.jsp");
							view.forward(request,response);	
						}
					}
					else {
						messages.add(new String("There is no such note in your list of notes"));
						RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
						view.forward(request,response);						
					}
				}

			}
			else {
				messages.add(new String("You need to specify the id of the note you want"));
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
				view.forward(request,response);	
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
		
		
		/*we get the user information*/
		User user = (User) session.getAttribute("userlogin");
		
		/*we get the id of note list*/
		Integer idn= Integer.parseInt(request.getParameter("id"));
		
		logger.info("Solicited the note:"+idn);

		NoteComplete n= db.getNote(idn, user.getIdu());

		if(idn!=null) {
			
			
			if(idn==NEW_NOTE_ID) {
				
				logger.info("Creando nueva lista");
				n= new NoteComplete();
				
				n.setCreationDate(new Date());
				n.setModificationDate(new Date());
				n.setOwnerID(user.getIdu());
				n.setOwner(user.getUsername());
				n.setImageOwner(user.getImage());
				n.setAnchored(false);
				n.setArchived(false);
				n.setColor("yellow");
				n.setType(1);
			}
		
			String newname= request.getParameter("name");
			String oldname= n.getTitle();
			String item= request.getParameter("item");

			if(item!="") {			
				logger.info("Se ha solicitado añadir el elemento '"+item+"' a la lista");
				
				String newcontent=n.getContent();
				
				newcontent= newcontent.concat("0/;/"+item+"/;/");
				
				logger.info("New content of list:"+n.getContent());
				
				n.setTitle(newname);
				n.setContent(newcontent);

				n.setModificationDate(new Date());

			}
			if(!newname.equals(n.getTitle())) {
				n.setTitle(newname);
				n.setModificationDate(new Date());
			}
			
			if(idn!=NEW_NOTE_ID && (!newname.equals(oldname) || item!="")) {
				db.updateNote(n);
			}
			
			if(idn==NEW_NOTE_ID) {
				
				logger.info("USer login and owner: "+user.getUsername());
				n=db.addNote(n); //whe use this to get the note whit new id put
			}
			
		}
		
		/*we show the note updated*/
		response.sendRedirect("ListServlet?id="+n.getId());
	}

}
