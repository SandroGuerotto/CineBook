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
	
	// Gibt eine neue Show zur�ck (Wird in ShowList gebraucht & Controller)
	public Show(int id, Room room, Film film, Date startDateTime, Date endDateTime,int durationInMinutes){
		this.id = id;
		this.room = room;
		this.film = film;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.durationInMinutes = durationInMinutes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	


}
