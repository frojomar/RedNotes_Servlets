package es.unex.giiis.pi.rednotes.model;

import java.util.Date;

public class Reminder {

	private Date date;
	private Integer idn;
	private Integer idu;
	private String description;
	private NoteComplete n;
	private String dateString;
	
	
	public Reminder() {
		
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Integer getIdn() {
		return idn;
	}


	public void setIdn(Integer idn) {
		this.idn = idn;
	}


	public NoteComplete getN() {
		return n;
	}


	public void setN(NoteComplete n) {
		this.n = n;
	}


	public Integer getIdu() {
		return idu;
	}


	public void setIdu(Integer idu) {
		this.idu = idu;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDateString() {
		return dateString;
	}


	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	
}
