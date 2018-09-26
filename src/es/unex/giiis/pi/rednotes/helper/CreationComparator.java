package es.unex.giiis.pi.rednotes.helper;

import java.util.Comparator;

import es.unex.giiis.pi.rednotes.model.NoteComplete;

public class CreationComparator implements Comparator<NoteComplete> {

	@Override
	public int compare(NoteComplete nota1, NoteComplete nota2) {
        return nota1.getCreationDate().
                compareTo(nota2.getCreationDate());
	}
}