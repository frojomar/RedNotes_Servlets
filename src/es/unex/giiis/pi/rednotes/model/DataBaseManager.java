package es.unex.giiis.pi.rednotes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.catalina.tribes.group.interceptors.FragmentationInterceptor;

import es.unex.giiis.pi.rednotes.dao.*;
import es.unex.giiis.pi.rednotes.helper.CreationComparator;
import es.unex.giiis.pi.rednotes.helper.DateFriendshipComparator;
import es.unex.giiis.pi.rednotes.helper.DateReminderComparator;
import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.helper.ModificationComparator;
import es.unex.giiis.pi.rednotes.helper.NameComparator;
import es.unex.giiis.pi.rednotes.helper.UsernameComparator;
import es.unex.giiis.pi.rednotes.helper.VersionComparatorDESC;

import java.sql.Connection;

public class DataBaseManager {
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	private ArrayList<NoteComplete> notesSimulation;
	private ArrayList<UserLogin> userSimulation;
	
	
	private Connection dbConnection=null;

	public DataBaseManager() {
		
	}
	
	
	public void setConnection(Connection dbConnection) {
		this.dbConnection=dbConnection;
	}
	
	public Connection getConnection() {
		return dbConnection;
	}
	
	public List<String> getImagesAvaliables(){
		List<String> images= new ArrayList<String>();
		
		/*consultar la tabla image de la base de datos y traernos todas las urls de imagenes*/
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);		
		
		images=userDAO.getAllImages();

    	return images;
	}
	
	private boolean debeMostrarse(Note n, Integer idu, String show1, String show2, String colorfilter, String color) {
		boolean debe=true;
		
		switch(show1) {
			case "notesandlists":
				break;
			case "notes":
				if(n.getType()==1){
					debe=false;
				}
				break;
			case "lists":
				if(n.getType()==0) {
					debe=false;
				}
				break;
		}
		
		switch (show2) {
			case "all":
				break;

			case "my":
				if(n.getOwnerID()!=idu) {
					debe=false;
				}
				break;
				
			case "friends":
				if(n.getOwnerID()==idu) {
					debe=false;
				}
				break;
		}
		
		if(!colorfilter.equals("allcolors")) {
			if(!color.equals(colorfilter)) {
				debe=false;
			}
		}
		
		return debe;
	}
	
	public void getNotes(Integer idu, ArrayList<NoteComplete> anchoredNotes, ArrayList<NoteComplete> notAnchoredNotes, ArrayList<NoteComplete> archivedNotes, String ordened, String show1, String show2, String colorfilter){
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<UsersNotes> notes=usersnotesDAO.getAllByUser(idu);
		
		Note n;
		UsersNotes un;
		boolean archived, anchored;
		String username, image;
		
		for(int index=0; index<notes.size(); index++) {
			un=notes.get(index);
			n=noteDAO.get(un.getIdn());
			
			if(debeMostrarse(n, idu, show1, show2, colorfilter, un.getColor())) {
				/*convert int of archived and anchored to boolean*/
				if(un.getArchived()==1) {archived=true;}else {archived=false;}
				if(un.getPinned()==1) {anchored=true;}else {anchored=false;}
				
				/*search username and image of owner*/
				username=userDAO.searchUsername(n.getOwnerID());
				image=userDAO.searchImage(n.getOwnerID());
				
				if(archived) {
					archivedNotes.add(new NoteComplete(n.getIdn(),n.getTitle(),n.getContent(),n.getOwnerID(),username, image,archived,anchored,un.getColor(), n.getCreationDate(),n.getModificationDate(),n.getType()));
				}
				else {
					if(anchored) {
						anchoredNotes.add(new NoteComplete(n.getIdn(),n.getTitle(),n.getContent(),n.getOwnerID(),username, image,archived,anchored,un.getColor(), n.getCreationDate(),n.getModificationDate(),n.getType()));
					}
					else {
						notAnchoredNotes.add(new NoteComplete(n.getIdn(),n.getTitle(),n.getContent(),n.getOwnerID(),username, image,archived,anchored,un.getColor(), n.getCreationDate(),n.getModificationDate(),n.getType()));
					}
				}
			}
		}

		
		switch (ordened) {
		case "creation":
			Collections.sort(archivedNotes, new CreationComparator());
			Collections.sort(anchoredNotes, new CreationComparator());
			Collections.sort(notAnchoredNotes, new CreationComparator());
			break;
			
		case "modification":
			Collections.sort(archivedNotes, new ModificationComparator());
			Collections.sort(anchoredNotes, new ModificationComparator());
			Collections.sort(notAnchoredNotes, new ModificationComparator());
			break;

		case "name":
			Collections.sort(archivedNotes, new NameComparator());
			Collections.sort(anchoredNotes, new NameComparator());
			Collections.sort(notAnchoredNotes, new NameComparator());
			break;
			
		}	
	
	}
	

	public List<NoteComplete> getNotesSharedBy(Integer idu, Integer ownerIdu) {
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<NoteComplete> notesUser=new ArrayList<NoteComplete>();
		
		List<UsersNotes> notes=usersnotesDAO.getAllByUser(idu);
		
		Note n;
		UsersNotes un;
		boolean archived, anchored;
		String username, image;
		
		for(int index=0; index<notes.size(); index++) {
			un=notes.get(index);
			n=noteDAO.get(un.getIdn());
			
			if(n.getOwnerID()==ownerIdu) {
				
				/*convert int of archived and anchored to boolean*/
				if(un.getArchived()==1) {archived=true;}else {archived=false;}
				if(un.getPinned()==1) {anchored=true;}else {anchored=false;}
				
				/*search username and image of owner*/
				username=userDAO.searchUsername(n.getOwnerID());
				image=userDAO.searchImage(n.getOwnerID());
				
				
				notesUser.add(new NoteComplete(n.getIdn(),n.getTitle(),n.getContent(),n.getOwnerID(),username, image,archived,anchored,un.getColor(), n.getCreationDate(),n.getModificationDate(),n.getType()));

			}

		}

		return notesUser;
	}	


	public NoteComplete getNote(Integer idn, Integer idu) {
		NoteComplete note=null;
		boolean archived, anchored;
		String username, image;
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		UsersNotes un= usersnotesDAO.get(idu, idn);
		
		if(un!=null) {
			Note n= noteDAO.get(idn);
			
			/*convert int of archived and anchored to boolean*/
			if(un.getArchived()==1) {archived=true;}else {archived=false;}
			if(un.getPinned()==1) {anchored=true;}else {anchored=false;}
			
			/*search username and image of owner*/
			username=userDAO.searchUsername(n.getOwnerID());
			image=userDAO.searchImage(n.getOwnerID());
			
			note= new NoteComplete(n.getIdn(),n.getTitle(),n.getContent(),n.getOwnerID(),username, image,archived,anchored,un.getColor(), n.getCreationDate(),n.getModificationDate(),n.getType());
			
		}

				
		return note;
	}

	/**This method, find the user in the database. If found, return true and complete the information of user
	 * 
	 * @param user
	 * @param messages
	 * @return
	 */
	public boolean existsUser(UserLogin user,List<String> messages) {
		boolean emailExists=false;
		boolean exists=false;
		boolean passwordCorrect=false;
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);		
		
		UserLogin userOfDB=userDAO.getforLogin(user.getEmail());
		
		if(userOfDB!=null) {
			emailExists=true;
			if(userOfDB.getPassword().equals(user.getPassword())) {
				passwordCorrect=true;
				exists=true;
			}
		}
		
		/*Load the rest of data for the user instance*/
		if(exists) {
			/*Password and email is load yet*/
			user.setIdu(userOfDB.getIdu());
			user.setName(userOfDB.getName());
			user.setCountry(userOfDB.getCountry());
			user.setCity(userOfDB.getCity());
			user.setAge(userOfDB.getAge());
			user.setTelephone(userOfDB.getTelephone());
			user.setUsername(userOfDB.getUsername());
			user.setDate(userOfDB.getDate());
			user.setImage(userOfDB.getImage());
		}
		else {
			if(!emailExists) {
				exists=false;
				messages.add("This email is not register!!");
			}
			else {
				if(!passwordCorrect) {
					exists=false;
					messages.add("Password is not correct!!");
				}
			}

		}
		
		return exists;
	}

	public boolean existsUserByUsername(UserLogin user, List<String> messages) {
		boolean usernameExists=false;
		boolean exists=false;
		boolean passwordCorrect=false;
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);		
		
		UserLogin userOfDB=userDAO.getforLoginByUsername(user.getUsername());
		
		if(userOfDB!=null) {
			usernameExists=true;
			if(userOfDB.getPassword().equals(user.getPassword())) {
				passwordCorrect=true;
				exists=true;
			}
		}
		
		/*Load the rest of data for the user instance*/
		if(exists) {
			/*Password and email is load yet*/
			user.setIdu(userOfDB.getIdu());
			user.setName(userOfDB.getName());
			user.setCountry(userOfDB.getCountry());
			user.setCity(userOfDB.getCity());
			user.setAge(userOfDB.getAge());
			user.setTelephone(userOfDB.getTelephone());
			user.setEmail(userOfDB.getEmail());
			user.setDate(userOfDB.getDate());
			user.setImage(userOfDB.getImage());
		}
		else {
			if(!usernameExists) {
				exists=false;
				messages.add("This username is not register!!");
			}
			else {
				if(!passwordCorrect) {
					exists=false;
					messages.add("Password is not correct!!");
				}
			}

		}
		
		return exists;
	}


	/**Comprobation of username, email and password are not yet in the database*/
	public boolean dataRegisterValid(UserLogin user, List<String> messages) {
		boolean valid=true;
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		if(userDAO.existsEmail(user.getEmail())) {
			messages.add("Email already register");
			valid=false;
		}
		
		if(userDAO.existsPassword(user.getPassword())) {
			messages.add("Password is already being used");
			valid=false;
		}
		
		if(userDAO.existsUsername(user.getUsername())) {
			messages.add("Username already register");
			valid=false;
		}
		
		return valid;
	}


	/**Method that add a new register user to the database*/
	public void addNewUser(UserLogin user) {
		
		logger.info("DBManager: Adding user to db");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		userDAO.add(user);
	}


	public void updateNote(NoteComplete n) {
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		Note note= new Note();
		note.setTitle(n.getTitle());
		note.setContent(n.getContent());
		note.setOwnerID(n.getOwnerID());
		note.setCreationDate(n.getCreationDate());
		note.setModificationDate(n.getModificationDate());
		note.setIdn(n.getId());
		note.setType(n.getType());
		
		noteDAO.save(note);
			
		
		int archived, anchored;
		if(n.getArchived()==true) {archived=1;}else {archived=0;}
		if(n.getAnchored()==true) {anchored=1;}else {anchored=0;}
		
		UsersNotes un= new UsersNotes();
		un.setArchived(archived);
		un.setPinned(anchored);
		un.setColor(n.getColor());
		un.setIdu(n.getOwnerID());
		un.setIdn(n.getId());
		
		usersnotesDAO.save(un);
		
		NoteVersion nv= new NoteVersion();
		nv.setIdn(n.getId());
		nv.setIdu(n.getOwnerID());
		nv.setModificationDate(n.getModificationDate());
		nv.setTitle(n.getTitle());
		nv.setContent(n.getContent());
		
		versionDAO.add(nv);
		
		
		logger.info("Nota modificada");
	}

	public void updateNoteforAUser(NoteComplete n, Integer idu) {
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		Note note= new Note();
		note.setTitle(n.getTitle());
		note.setContent(n.getContent());
		note.setOwnerID(n.getOwnerID());
		note.setCreationDate(n.getCreationDate());
		note.setModificationDate(n.getModificationDate());
		note.setIdn(n.getId());
		note.setType(n.getType());
		
		noteDAO.save(note);
			
		
		int archived, anchored;
		if(n.getArchived()==true) {archived=1;}else {archived=0;}
		if(n.getAnchored()==true) {anchored=1;}else {anchored=0;}
		
		UsersNotes un= new UsersNotes();
		un.setArchived(archived);
		un.setPinned(anchored);
		un.setColor(n.getColor());
		un.setIdu(idu);
		un.setIdn(n.getId());

		
		usersnotesDAO.save(un);
		
		
		logger.info("Nota modificada");
	}
	
	public NoteComplete addNote(NoteComplete note) {
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);

		Note n= new Note();
		n.setTitle(note.getTitle());
		n.setContent(note.getContent());
		n.setOwnerID(note.getOwnerID());
		n.setCreationDate(note.getCreationDate());
		n.setModificationDate(note.getModificationDate());
		n.setType(note.getType());
		
		note.setId(noteDAO.add(n));
		
		UsersNotes un= new UsersNotes();
		un.setArchived(0);
		un.setPinned(0);
		un.setColor(note.getColor());
		un.setIdu(note.getOwnerID());
		un.setIdn(note.getId());
		
		usersnotesDAO.add(un);
		
		NoteVersion nv= new NoteVersion();
		nv.setIdn(note.getId());
		nv.setIdu(note.getOwnerID());
		nv.setModificationDate(note.getModificationDate());
		nv.setTitle(note.getTitle());
		nv.setContent(note.getContent());
		
		versionDAO.add(nv);
		
		return note;
	}


	public void dropNote(Integer idn, Integer idu) {
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		usersnotesDAO.delete(idu, idn);
		remindersDAO.deleteAllOfUserAndNote(idu, idn);
		
		Note n=noteDAO.get(idn);
		
		if(n.getOwnerID()==idu) {
			remindersDAO.deleteAllOfNote(idn);
			usersnotesDAO.deleteAllIdN(idn);
			versionDAO.deleteAllOf(idn);
			noteDAO.delete(idn);
			logger.info("Eliminando nota "+idn+" definitivamente");
		}
		
		logger.info("Nota "+idn+" eliminada");
	}


	public void updateUser(UserLogin user) {
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		userDAO.save(user);
	}


	public void deleteAccount(UserLogin user) {
		boolean wellDone;
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		/*borrar todos los recordatorios del usuario*/
		wellDone=remindersDAO.deleteAllOfUser(user.getIdu());
		
		if(wellDone)
			logger.info("ALL REMINDERS DELETED");
		
		
		/*borrar todas las notas de UserNotes asociadas al idu del user*/
		wellDone=usersnotesDAO.deleteAllIdU(user.getIdu());
		
		if(wellDone)
			logger.info("ALL NOTES SHARED DELETED");

		/*para cada nota de Notes donde el ownerID es idu..*/
		List<Note> notes= noteDAO.getAll();
		
		for(int i=0; i<notes.size(); i++) {
			Note n= notes.get(i);
			if(n.getOwnerID()==user.getIdu()) {
				/*eliminamos las apariciones de esa nota en UsersNotes*/
				usersnotesDAO.deleteAllIdN(n.getIdn());
				/*elimiamos los recordatorios de notas cuyo propietario era idu*/
				remindersDAO.deleteAllOfNote(n.getIdn());
				/*eliminamos todas las versiones de la nota*/
				versionDAO.deleteAllOf(n.getIdn());
			}
		}
		
		/*borrar todas las notas de Notes donde el owner sea el idu del user*/
		wellDone=noteDAO.deleteAllIdU(user.getIdu());
		
		if(wellDone)
			logger.info("ALL NOTES OF USER DELETED");

		/*borrar todas las amistades (confirmadas y sin confirmar) en las que aparezca el usuario*/
		wellDone=friendDAO.deleteAllOf(user.getIdu());
		
		if(wellDone)
			logger.info("ALL FRIENDSHIP RELATIONS DELETED");
		
		/*borrar el user de la tabla Users*/
		userDAO.delete(user.getIdu());
		
		logger.info("USER ACCOUNT DELETED");
	}


	public List<NoteComplete> searchNotes(String search, Integer idu) {
		List<NoteComplete> notesCompletes= new ArrayList<NoteComplete>();
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<Note> notes=noteDAO.getAllBySearchAll(search);
		
		Note n;
		UsersNotes un;
		boolean archived, anchored;
		String username, image;
		
		for(int index=0; index<notes.size(); index++) {
			n=notes.get(index);
			un=usersnotesDAO.get(idu, n.getIdn());
			
			if(un!=null) {
				/*convert int of archived and anchored to boolean*/
				if(un.getArchived()==1) {archived=true;}else {archived=false;}
				if(un.getPinned()==1) {anchored=true;}else {anchored=false;}
				
				/*search username and image of owner*/
				username=userDAO.searchUsername(n.getOwnerID());
				image=userDAO.searchImage(n.getOwnerID());

				notesCompletes.add(new NoteComplete(n.getIdn(),n.getTitle(),n.getContent(),n.getOwnerID(),username, image,archived,anchored,un.getColor(), n.getCreationDate(),n.getModificationDate(),n.getType()));
				
			}
			
		}
		
		return notesCompletes;
	}


	public User getFriend(Integer idu, String username) {
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		User u= userDAO.get(username);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		Friend f= friendDAO.get(idu, u.getIdu());
		
		u.setDate(f.getDateFriendship());
		
		return u;
	}
	
	public User getFriend(Integer idu, Integer idu2) {
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		User u= userDAO.get(idu2);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		Friend f= friendDAO.get(idu, idu2);
		
		u.setDate(f.getDateFriendship());
		
		return u;
	}


	public List<User> getFriends(Integer idu, String orderby) {

		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<Friend> friends= friendDAO.getAllBy(idu);
		List<User> users= new ArrayList<User>();
		
		Friend friend;
		User u;
		for(int i=0; i<friends.size(); i++) {
			friend= friends.get(i);
			if(friend.getConfirmed()==1) {
				
				if(friend.getIdA()==idu) {
					logger.info("Ejecutado el buscar por idB");
					u=userDAO.get(friend.getIdB());
				}
				else {
					logger.info("Ejecutado el buscar por idA");
					u=userDAO.get(friend.getIdA());
				}
				
				logger.info(" Amistad: "+friend.getIdA()+"-"+friend.getIdB()+"-"+friend.getDateFriendship());
				logger.info("User amigo: "+u.getUsername()+" "+u.getIdu());
				u.setDate(friend.getDateFriendship());
				users.add(u);
			}

		}
		
		switch (orderby) {
		case "name":
			Collections.sort(users, new UsernameComparator());
			break;
			
		case "date":
			Collections.sort(users, new DateFriendshipComparator());
			break;
		}	
		
		return users;

	}

	/**Return a list of friends of the user, including the not confirmed friends
	 * 
	 * @param idu
	 * @param orderby
	 * @return
	 */
	public List<User> getFriendsAndPetitions(Integer idu, String orderby) {

		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<Friend> friends= friendDAO.getAllBy(idu);
		List<User> users= new ArrayList<User>();
		
		Friend friend;
		User u;
		for(int i=0; i<friends.size(); i++) {
			friend= friends.get(i);
			
			if(friend.getIdA()==idu) {
				logger.info("Ejecutado el buscar por idB");
				u=userDAO.get(friend.getIdB());
			}
			else {
				logger.info("Ejecutado el buscar por idA");
				u=userDAO.get(friend.getIdA());
			}
			
			logger.info(" Amistad: "+friend.getIdA()+"-"+friend.getIdB()+"-"+friend.getDateFriendship());
			logger.info("User amigo: "+u.getUsername()+" "+u.getIdu());
			u.setDate(friend.getDateFriendship());
			users.add(u);

		}
		
		switch (orderby) {
		case "name":
			Collections.sort(users, new UsernameComparator());
			break;
			
		case "date":
			Collections.sort(users, new DateFriendshipComparator());
			break;
		}	
		
		return users;

	}

	
	
	public List<User> getAllUsers() {
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<User> users= userDAO.getAll();
		
		return users;
	}

	private List<User> getUsersFiltered(String name, String username, String city, String country){
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		logger.info(">>Buscando usuarios con filtro");
		String where= "WHERE";
		
		if(name!="") {
			where= where.concat(" name LIKE '%"+name+"%'");
		}
		if( name!="" &&(username!="" || city!="" || country!="")) {
			where= where.concat(" AND ");

		}
		if(username!="") {
			where= where.concat(" username LIKE '%"+username+"%'");
		}
		if(username!="" &&(city!="" || country!="")) {
			where= where.concat(" AND ");

		}
		if(city!="") {
			where= where.concat(" city LIKE '%"+city+"%'");
		}
		if(city!="" && country!="") {
			where= where.concat(" AND ");

		}
		if(country!="") {
			where= where.concat(" country LIKE '%"+country+"%'");
		}
		
		
		logger.info("WHERE para buscar: "+where);
		List<User> users= userDAO.getAllFiltered(where);
		return users;
	}

	public List<User> getNotFriendsFiltered(Integer idu, String name, String username, String city, String country) {
		
		logger.info("Buscando amigos con filtro");
		List<User> users=this.getUsersFiltered(name, username, city, country);
		List<User> friends= this.getFriendsAndPetitions(idu, "name");
		
		List<User> usersValid =new ArrayList<User>();
		
		boolean yaPertenece=false;
		for(int i=0; i<users.size(); i++) {
			yaPertenece=false;
			if(users.get(i).getIdu()==idu) {
				yaPertenece=true;
			}
			else {
				for(int j=0; j<friends.size();j++) {
					if(users.get(i).getIdu()==friends.get(j).getIdu()) {
						yaPertenece=true;
					} 
				}
			}
			if(!yaPertenece) {
				usersValid.add(users.get(i));
			}
		}
		
		return usersValid;
	}


	public List<User> searchFriend(Integer idu, String search, String orderby) {
		List<User> friendsValid= new ArrayList<User>();
		List<User> friends= this.getFriends(idu, orderby);
		
		User u;
		for(int i=0; i<friends.size(); i++) {
			u=friends.get(i);
			if(u.getUsername().contains(search)) {
				friendsValid.add(u);
			}
			else {
				if(u.getName()!=null) {
					if(u.getName().contains(search)) {
						friendsValid.add(u);
					}
				}
			}
			
		}
		
		return friendsValid;
	}


	public User getUser(String username) {
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		User user= userDAO.get(username);
		
		return user;
	}


	public List<User> getNotFriendsUsers(Integer idu) {

		List<User> users=this.getAllUsers();
		List<User> friends= this.getFriendsAndPetitions(idu, "name");
		
		List<User> usersValid =new ArrayList<User>();
		
		boolean yaPertenece=false;
		for(int i=0; i<users.size(); i++) {
			yaPertenece=false;
			if(users.get(i).getIdu()==idu) {
				yaPertenece=true;
			}
			else {
				for(int j=0; j<friends.size();j++) {
					if(users.get(i).getIdu()==friends.get(j).getIdu()) {
						yaPertenece=true;
					}
				}
			}
			if(!yaPertenece) {
				usersValid.add(users.get(i));
			}
		}
		
		return usersValid;
	}


	public List<User> getUserPetitionsSent(Integer idu) {
		List<User> users= new ArrayList<User>();
		
		logger.info("Solicitando las peticiones de amistad enviadas por: "+idu);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		
		List<Friend> friends=friendDAO.getAllNotConfirmedBy(idu);
		
		User u;
		for(int i=0; i<friends.size(); i++) {
			u=userDAO.get(friends.get(i).getIdB());
			users.add(u);
		}
		
		return users;
	}


	public List<User> getUserPetitionsReceived(Integer idu) {
		List<User> users= new ArrayList<User>();
		
		logger.info("Solicitando las peticiones de amistad recibidas por: "+idu);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		
		List<Friend> friends=friendDAO.getAllNotConfirmedTo(idu);
		
		User u;
		for(int i=0; i<friends.size(); i++) {
			u=userDAO.get(friends.get(i).getIdA());
			users.add(u);
		}
		
		return users;
	}


	public boolean createNewFriendship(Integer iduS, Integer iduR) {
		boolean ok=true;
		logger.info("Creando una peticion de amistad por:"+iduS+" para:"+iduR);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		Friend friend= new Friend();
		friend.setIdA(iduS);
		friend.setIdB(iduR);
		friend.setDateFriendship(new Date());
		friend.setConfirmed(0);
		
		ok=friendDAO.add(friend);
		return ok;
	}


	public boolean deleteFriendship(Integer iduS, Integer iduR) {
		boolean ok=true;
		logger.info("Eliminando la peticion de amistad creada por:"+iduS+" para:"+iduR);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		ok=friendDAO.delete(iduS, iduR);
		
		return ok;
	}


	public boolean confirmFriendship(Integer iduS, Integer iduR) {
		boolean ok=true;
		logger.info("Confirmando la  peticion de amistad creada por:"+iduS+" para:"+iduR);
	
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		ok=friendDAO.confirm(iduS, iduR, new Date());
		
		return ok;
	}


	/**Method that delete the notes that "id" shared with "idu" of "idu" notes collection. Also, delete the notes
	 * that "idu" shared with "id" of "id" notes collection.
	 * 
	 * @param idu
	 * @param id
	 */
	public void deleteSharedNotesWith(Integer idu, Integer id) {
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		/*cogemos la coleccion de notas de idu*/
		List<UsersNotes> notes= usersnotesDAO.getAllByUser(idu);
		
		Note n;
		/*para cada nota de la lista...*/
		for(int i=0; i<notes.size(); i++) {
			n=noteDAO.get(notes.get(i).getIdn());
			/*si esta nota es de idu, la eliminamos en id (si es que existe)*/
			if(n.getOwnerID()==idu) {
				usersnotesDAO.delete(id, n.getIdn());
				this.dropNote(n.getIdn(), id);
			}
			/*si esta nota es de id, la eliminamos en idu*/
			if(n.getOwnerID()==id) {
				usersnotesDAO.delete(idu, n.getIdn());
				this.dropNote(n.getIdn(), idu);
			}
		}

		logger.info("ELIMINADAS TODAS LAS NOTAS COMPARTIDAS ENTRE "+idu+" Y "+id);
	}


	/**Share the note "idn" of "idu" with "idu2"
	 * 
	 * @param idu
	 * @param idn
	 * @param idu2
	 * @return
	 */
	public boolean shareNote(Integer idu, Integer idn, Integer idu2) {
		boolean ok;
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		UsersNotes un= new UsersNotes();
		
		un.setIdn(idn);
		un.setIdu(idu2);
		un.setPinned(0);
		un.setArchived(0);
		un.setColor("yellow");
		
		ok=usersnotesDAO.add(un);
		
		return ok;
	}

	
	/**Method that return a list with the friends of idu, that don't have the note "idn" shared yet.
	 * 
	 * @param idu
	 * @param string
	 * @return
	 */
	public List<User> getPossibleShareFriends(Integer idu, Integer idn, String orderby) {
		
		List<User> friends= this.getFriends(idu, orderby);
		List<User> users= new ArrayList<User>();
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		for(int i=0; i<friends.size(); i++) {
			if(usersnotesDAO.get(friends.get(i).getIdu(), idn)==null) {
				users.add(friends.get(i));
			}
		}
		
		return users;
	}


	public void deleteReminder(Integer idu, Integer idn, Date date) {
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		Reminder rem= new Reminder();
		
		rem.setIdn(idn);
		rem.setIdu(idu);
		rem.setDate(date);
		
		remindersDAO.delete(rem);
		
		logger.info("Eliminado el recordatorio");
		
	}


	public List<Reminder> getReminders(Integer idu) {
		logger.info("Getting los recordatorios del usuario"+idu );
		
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		List<Reminder> reminders= remindersDAO.getAllBy(idu);
		List<Reminder> remindersFinal= null;
		
		if(reminders!=null) {
			Reminder r;
			for(int i=0; i<reminders.size(); i++){
				r=reminders.get(i);
				r.setN(this.getNote(r.getIdn(),r.getIdu()));
				r.setDateString(DateTimeHelper.DateToString(r.getDate()));
			}
			
			remindersFinal= new ArrayList<Reminder>();
			for(int i=0; i<reminders.size(); i++) {
				if(reminders.get(i).getDate().compareTo(new Date())>=0) {
					remindersFinal.add(reminders.get(i));
				}
			}
			
			Collections.sort(remindersFinal, new DateReminderComparator());
			
		}
		
		return remindersFinal;
	}


	public void addReminder(Integer idn, Integer idu, Date date, String description) {
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		Reminder rem= new Reminder();
		
		rem.setIdn(idn);
		rem.setIdu(idu);
		rem.setDate(date);
		rem.setDescription(description);
		
		remindersDAO.add(rem);
		
		logger.info("Añadido el recordatorio");
	}


	public List<NoteComplete> getVersions(Integer idn) {
		
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		List<NoteVersion> versions= versionDAO.getAllBy(idn);
		
		Note n=noteDAO.get(idn);
		
		List<NoteComplete> notes= new ArrayList<NoteComplete>();
		
		if(versions!=null) {
			NoteComplete n_version;
		
			Collections.sort(versions, new VersionComparatorDESC());

			for(int i=0; i<versions.size(); i++) {
				
				n_version= new NoteComplete();
				
				User user= userDAO.get(versions.get(i).getIdu());
				
				n_version.setId(n.getIdn());
				n_version.setCreationDate(n.getCreationDate());
				n_version.setType(n.getType());
			
				n_version.setAnchored(false);
				n_version.setArchived(false);
				
				n_version.setImageOwner(userDAO.searchImage(user.getIdu()));
				n_version.setOwner(user.getUsername());
				n_version.setOwnerID(user.getIdu());
				n_version.setTitle(versions.get(i).getTitle());
				n_version.setContent(versions.get(i).getContent());
				n_version.setModificationDate(versions.get(i).getModificationDate());
				
				n_version.setDateVersion(DateTimeHelper.DateToString(n_version.getModificationDate()));
	
				
				notes.add(n_version);
			}
		}
		
		
		return notes;
	}


	public NoteComplete getVersionNote(Integer idn, Integer idu, Date versionDate) {
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		NoteComplete note;
		
		note=this.getNote(idn, idu);
		
		NoteVersion noteVersion= versionDAO.get(idn, versionDate);
		
		note.setTitle(noteVersion.getTitle());
		note.setContent(noteVersion.getContent());
		note.setModificationDate(noteVersion.getModificationDate());
		note.setDateVersion(DateTimeHelper.DateToString(noteVersion.getModificationDate()));
		
		return note;
	}


	public void dropVersionNote(Integer idn, Date versionDate) {

		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		List<NoteVersion> versions= versionDAO.getAllBy(idn);
		
		NoteVersion nv;
		
		List<NoteVersion> newVersions= new ArrayList<NoteVersion>();
		
		if(versions!=null) {
			if(versions.size()>1) {
				for(int i=0; i<versions.size(); i++) {
					nv=versions.get(i);
					if(nv.getModificationDate().compareTo(versionDate)==0) { /*if this version is equals to versionDate*/
						versionDAO.delete(idn, nv.getModificationDate());
					}
					else {
						newVersions.add(nv);
					}
				}
				
				NoteDAO noteDAO = new JDBCNoteDAOImpl();
				noteDAO.setConnection(this.dbConnection);
							
				Note n= noteDAO.get(idn);
				
				
				if(n.getModificationDate().compareTo(versionDate)==0) {
					Collections.sort(newVersions, new VersionComparatorDESC());
					
					nv=newVersions.get(newVersions.size()-1);
					
		
					Note note= new Note();
					note.setTitle(nv.getTitle());
					note.setContent(nv.getContent());
					note.setOwnerID(n.getOwnerID());
					note.setCreationDate(n.getCreationDate());
					note.setModificationDate(nv.getModificationDate());
					note.setIdn(n.getIdn());
					note.setType(n.getType());
					
					noteDAO.save(note);
				}
			}
		}
	}
		
		public void dropForwardVersionNote(Integer idn, Date versionDate) {

			NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
			versionDAO.setConnection(this.dbConnection);
			
			List<NoteVersion> versions= versionDAO.getAllBy(idn);
			
			NoteVersion nv;
			
			List<NoteVersion> newVersions= new ArrayList<NoteVersion>();
					
			if(versions!=null) {
				for(int i=0; i<versions.size(); i++) {
					nv=versions.get(i);
					if(nv.getModificationDate().compareTo(versionDate)>0) { /*if this version is later of versionDate*/
						versionDAO.delete(idn, nv.getModificationDate());
					}
					else {
						newVersions.add(nv);
					}
				}
				Collections.sort(newVersions, new VersionComparatorDESC());
				
				nv=newVersions.get(newVersions.size()-1);
				
				NoteDAO noteDAO = new JDBCNoteDAOImpl();
				noteDAO.setConnection(this.dbConnection);
				
				Note n= noteDAO.get(idn);
				
				Note note= new Note();
				note.setTitle(nv.getTitle());
				note.setContent(nv.getContent());
				note.setOwnerID(n.getOwnerID());
				note.setCreationDate(n.getCreationDate());
				note.setModificationDate(nv.getModificationDate());
				note.setIdn(n.getIdn());
				note.setType(n.getType());
				
				noteDAO.save(note);
					
				
			}
		
		

	}
}
