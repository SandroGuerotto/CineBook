package model;

import java.io.Serializable;
import java.util.Date;


public class Show implements Serializable {

	int id;
	Room room;
	Film film;
	Date startDateTime;
	Date endDateTime;
	int durationInMinutes;
	
	// Gibt eine neue Show zurück (Wird in ShowList gebraucht & Controller)
	public Show(int id, Room room, Film film, Date startDateTime, Date endDateTime,int durationInMinutes){
		this.id = id;
		this.room = room;
		this.film = film;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.durationInMinutes = durationInMinutes;
	}
	

	public Date getStartDateTime() {
		return startDateTime;
	}	
	
}
