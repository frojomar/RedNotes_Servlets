package es.unex.giiis.pi.rednotes.model;

import java.util.Date;
import java.util.List;

import es.unex.giiis.pi.rednotes.controller.EditNoteServlet;

public class NoteComplete {
	
	private Integer id;
	private String title;
	private String content;
	private Integer ownerID;
	private String owner;
	private String imageOwner;
	private boolean anchored;
	private boolean archived;
	private String color;
	private Date creationDate;
	private Date modificationDate;
	private Integer type; //0 is a note, 1 is a list
	private String dateVersion; //only used in some method of Control Version
	
	public NoteComplete(Integer id, String name, String content, Integer ownerID,String owner, String imageOwner, 
			boolean archived ,boolean anchored,String color, Date creationDate, Date lastModification, Integer type){
		this.id=id;
		this.title=name;
		this.content=content;
		this.ownerID=ownerID;
		this.owner=owner;
		this.imageOwner=imageOwner;
		this.archived=archived; 
		this.anchored=anchored;
		this.color=color;
		this.creationDate= creationDate;
		this.modificationDate= lastModification;
		this.type=type;
		this.dateVersion="";
	}
	
	public NoteComplete() {
		this.id=EditNoteServlet.NEW_NOTE_ID;
		this.title="";
		this.content="";
		this.owner="";
		this.archived=false; 
		this.anchored=false;
		this.creationDate= null;
		this.modificationDate= null;
		this.dateVersion="";
	}

	public boolean validate(List<String> validationMessages) {
		if (title == null || title.trim().isEmpty() || title.length() < 4) {
			validationMessages.add("The title must be higher than 3 characters.");
		} else if (title.length() > 50) {
			validationMessages.add("The title cannot be higher than 50 characters.");
		}

		
		if (content == null || content.trim().isEmpty() || content.length() < 11) {
			validationMessages.add("The content must be higher than 10 characters.");
		} else if (content.length() > 1000) {
			validationMessages.add("The content cannot be higher than 1000 characters.");
		}

		if (validationMessages.isEmpty())
			return true;
		else
			return false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
		this.modificationDate= new Date();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.modificationDate= new Date();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String username_owner) {
		this.owner = username_owner;
	}
	
	public boolean getAnchored() {
		return anchored;
	}

	public void setAnchored(boolean anchored) {
		this.anchored = anchored;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public boolean getArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Integer getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Integer ownerID) {
		this.ownerID = ownerID;
	}

	public String getImageOwner() {
		return imageOwner;
	}

	public void setImageOwner(String imageOwner) {
		this.imageOwner = imageOwner;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDateVersion() {
		return dateVersion;
	}

	public void setDateVersion(String dateVersion) {
		this.dateVersion = dateVersion;
	}
	
}
