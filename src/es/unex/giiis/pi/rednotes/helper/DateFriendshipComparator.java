package es.unex.giiis.pi.rednotes.helper;

import java.util.Comparator;

import es.unex.giiis.pi.rednotes.model.User;

public class DateFriendshipComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
        return user1.getDate().
                compareTo(user2.getDate());
	}
}