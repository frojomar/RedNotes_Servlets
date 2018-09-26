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
 * Servlet implementation class ListServlet
 */
@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListServlet() {
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
						
						RequestDispatcher view = request.getRequestDispatcher("WEB-INF/List.jsp");
						view.forward(request,response);	
					}
				}
				else {
					messages.add(new String("There is no such note in your list of notes"));
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Notes.jsp");
					view.forward(request,response);						
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
			
			String[] values;
			List<IntegerStringPair> lastcontent=IntegerStringPair.parseToListOfElements(n.getContent());
			String newcontent= new String("");			
			
			/*we show in logger the last data of the note*/
			logger.info("Last content:"+n.getContent());
			
			/*we get the new values of the note*/
			values=request.getParameterValues("elementBox");
			
			IntegerStringPair element;
			boolean isChecked;
			
			if(lastcontent!=null) {
				for(int i=0; i<lastcontent.size();i++) {
					logger.info("Tratando elemento "+i);
					element=lastcontent.get(i);
									
					isChecked=false;
					if(values!=null) {	
						for(int j=0; j<values.length; j++) {
							if(values[j].equals(element.getText())) {
								isChecked=true;
							}
						}
					}
					if(isChecked) {
						newcontent= newcontent.concat("1");
					}
					else {
						newcontent= newcontent.concat("0");
					}
					
					newcontent= newcontent.concat("/;/");
					newcontent= newcontent.concat(element.getText());
					newcontent= newcontent.concat("/;/");
					
					logger.info("Cadena momentanea:"+newcontent);
					
				}
				
				n.setContent(newcontent);
							
				logger.info("New content of list:"+n.getContent());
				
				n.setModificationDate(new Date());
				db.updateNote(n);
			}
		}
		
		/*we show the note updated*/
		response.sendRedirect("ListServlet?id="+n.getId());
	}

}
