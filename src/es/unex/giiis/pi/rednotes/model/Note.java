package es.unex.giiis.pi.rednotes.model;

import java.util.Date;
import java.util.List;

public class Note {
	private int idn;
	private String title;
	private String content;
	private int ownerID; 
	private Date creationDate;
	private Date modificationDate;
	private int type;
	
	
	
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
	
	public int getIdn() {
		return idn;
	}
	public void setIdn(int idn) {
		this.idn = idn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String newContent) {
		this.content = newContent;
	}
	public int getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(int propietary) {
		this.ownerID = propietary;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	


}
