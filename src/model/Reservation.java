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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}



	
	
}
