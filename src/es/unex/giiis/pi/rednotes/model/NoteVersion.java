package es.unex.giiis.pi.rednotes.model;

import java.util.Date;

public class NoteVersion {

		private Integer idn;
		private Date modificationDate;
		private Integer idu;
		private String title;
		private String content;
		public Integer getIdn() {
			return idn;
		}
		public void setIdn(Integer idn) {
			this.idn = idn;
		}
		public Date getModificationDate() {
			return modificationDate;
		}
		public void setModificationDate(Date modificationDate) {
			this.modificationDate = modificationDate;
		}
		public Integer getIdu() {
			return idu;
		}
		public void setIdu(Integer idu) {
			this.idu = idu;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
		
		
}
