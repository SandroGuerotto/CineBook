package model;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable{
	
	int id;
	Show show;
	String seatNumber;
	String phoneNumber;
	Date dateTime;
	
	// Gibt eine neue Reservation zurück
	public Reservation(int id, Show show, String seatNumber, String phoneNumber, Date dateTime){
		this.id = id;
		this.show = show;
		this.seatNumber = seatNumber;
		this.phoneNumber = phoneNumber;
		this.dateTime = dateTime;
	}


	public Date getDateTime() {
		return dateTime;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public Show getShow() {
		return show;
	}

	
	
}
