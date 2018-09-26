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
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class EditPerfilServlet
 */
@WebServlet("/EditPerfilServlet")
public class EditPerfilServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditPerfilServlet() {
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
		else {		

			if(request.getParameter("selectImage")!=null) {
				
				Connection conn = (Connection) getServletContext().getAttribute("dbConn");
				DataBaseManager db = (DataBaseManager) getServletContext().getAttribute("dbmanager");
				db.setConnection(conn);
				
				logger.info("Change of image of perfil solicited");
				
				List<String> images=db.getImagesAvaliables();
			
				request.setAttribute("images", images);
				
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/SelectImagePerfil.jsp");
				view.forward(request,response);	
			}
			else {
				User user= (User)session.getAttribute("userlogin");
				
				
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/EditPerfil.jsp");
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
		
		/*edit of image of perfil*/
		if(request.getParameter("selectImage")!=null) {
			logger.info("Change of value of User");
			
			UserLogin user = (UserLogin) session.getAttribute("userlogin");
			
			user.setImage(request.getParameter("image"));
			
			db.updateUser(user);
			
			session.setAttribute("userlogin", user);
			
			/*redirect to perfil*/
			response.sendRedirect("PerfilServlet");
		}
		
		/*edit of data of perfil*/
		else {
			boolean passwordProblems=false;
		
			UserLogin user = (UserLogin) session.getAttribute("userlogin");
			
			UserLogin newuser= new UserLogin();
			
			List<String> messages = new ArrayList<String>();
			
	
			/*we put the not modificable fields...*/
			newuser.setIdu(user.getIdu());
			newuser.setUsername(user.getUsername());
			newuser.setDate(user.getDate());
			newuser.setPassword(user.getPassword());
			newuser.setImage(user.getImage());
			/*and update the other fields*/
			newuser.setEmail(request.getParameter("email"));
			newuser.setName(request.getParameter("name"));
			newuser.setCountry(request.getParameter("country"));
			newuser.setCity(request.getParameter("city"));
			if(request.getParameter("age")!="") {
				newuser.setAge(Integer.parseInt(request.getParameter("age")));
			}
			newuser.setTelephone(request.getParameter("telephone"));
			
			if (newuser.validatePerfilDates(messages)) {
				logger.info(">>DAta validate");

				session.setAttribute("userlogin", newuser);
				
				/*we check that, if you want to change the password, everything is correct*/
				if(request.getParameter("actualpas")!="") {
					logger.info(">>Change of password solicited");
					if(request.getParameter("actualpas").equals(newuser.getPassword())) {
						if(request.getParameter("newpas")!="" && request.getParameter("newpas2")!="") {
							if(request.getParameter("newpas").equals(request.getParameter("newpas2"))) {
								newuser.setPassword(request.getParameter("newpas"));
								if(!newuser.validatePassword(messages)) {/*if new password don't validate, we recover the old*/
									newuser.setPassword(request.getParameter("actualpas"));
									passwordProblems=true;
								}
								else {
									db.updateUser(newuser);
								}
							}
							else {
								messages.add("ERROR:The two fields of the new password do not match");
								passwordProblems=true;
							}
						}					
						else {
							messages.add("ERROR:You must complete the two field of new passsword");
							passwordProblems=true;
						}
					}
					else {
						messages.add("ERROR: Actual password not is correct");
						passwordProblems=true;
					}
				}
				else {
					db.updateUser(newuser);
				}
				
				if(passwordProblems) {
					request.setAttribute("messages",messages);
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/EditPerfil.jsp");
					view.forward(request,response);
				}
				else {
					/*redirect to perfil*/
					response.sendRedirect("PerfilServlet");
				}
	
			} 
			else {
				request.setAttribute("messages",messages);
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/EditPerfil.jsp");
				view.forward(request,response);
			}
		
		}
	}

}
