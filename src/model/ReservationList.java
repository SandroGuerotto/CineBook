package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import controller.FileStream;

public class ReservationList extends ArrayList<Reservation> implements Serializable{

	Reservation reservation;
	FileStream fileStream;

	public ReservationList() {
		fileStream = new FileStream();
		reservation = new Reservation();
//		addReservation(1, null, "A1", "0765679867", new Date(300000));

	}

	// Neue Reservation hinzufügen
	public void addReservation(Reservation reservation) {
		this.add(reservation);

		save();
	}
	
	// Neue Reservation erstellen und hinzufügen
	public void addReservation(int id, Show show, String seatNumber, String phoneNumber, Date dateTime) {
		reservation = reservation.newReservation(id, show, seatNumber, phoneNumber, dateTime);
		this.add(reservation);

		save();
	}

	// Reservation editieren (id muss natürlich die der alten Reservierung sein)
	public void editReservation(Reservation editedReservation) {
		reservation = getReservationById(editedReservation.id);
		reservation.editReservation(editedReservation.show, editedReservation.seatNumber,
				editedReservation.phoneNumber, editedReservation.dateTime);

		save();
	}

	// Reservation bearbeiten mit einzelnen Parameter (id muss natürlich die der alten Reservierung sein)
	public void editReservation(int id, Show newShow, String newSeatNumber, String newPhoneNumber, Date newDateTime) {
		reservation = getReservationById(id);
		reservation.editReservation(newShow, newSeatNumber, newPhoneNumber, newDateTime);

		save();
	}

	// Reservation löschen mit Objekt (boolean gibt Wert zurück ob wirklich gelöscht wurde)
	public boolean deleteReservation(Reservation reservation) {
		reservation = getReservationById(reservation.id);
		return this.remove(reservation);
	}

	// Reservation löschen mit id (boolean gibt Wert zurück ob wirklich gelöscht wurde)
	public boolean deleteReservation(int id) {
		reservation = getReservationById(id);
		return this.remove(reservation);
	}

	// Sucht Reservation per Id, wenn keine gefunden dann wird null zurückgegeben
	public Reservation getReservationById(int id) {

		if(this.size() != 0){
			for (Reservation reservation : this) {
	
				if (reservation.id == id) {
					return reservation;
				}
			}
		}
		return null;
	}

	// Prüfen ob eine Reservation schon existiert mit Objekt
	public boolean doReservationExist(Reservation reservation) {
		
		if(this.size() != 0){
			for (Reservation item : this) {
				if (reservation.show == item.show && reservation.seatNumber == item.seatNumber) {
					return true;
				}
			}
		}
		return false;
	}

	// Prüfen ob eine Reservation schon existiert mit Variablen
	public boolean doReservationExist(Show show, String seatNumber) {
		
		if(this.size() != 0){
			for (Reservation reservation : this) {
				if (show == reservation.show && seatNumber == reservation.seatNumber) {
					return true;
				}
			}
		}
		return false;
	}

// 	Gibt alle dazugehörigen Reservationen zurück
	public ReservationList getAttendantReservations(Reservation reservation){
		ReservationList attendantReservationList = new ReservationList();
		
		if(this.size() != 0){
			for(Reservation tmpReservation : this){
				
				// Wenn Reservierung gleiche Telefonnummer, gleicher Zeitstempfel aber nicht gleiche Id hat
				if(reservation.dateTime == tmpReservation.dateTime && reservation.phoneNumber.equals(tmpReservation.phoneNumber)){
					if(reservation.id != tmpReservation.id){
						attendantReservationList.add(tmpReservation);
					}
				}
			}
		}
		
		return attendantReservationList;
	}
	
	
	// Gibt alle Reservationen die zur gleichen Show gehören zurück
	public ReservationList getReservationsByShow(Show show){
		ReservationList showReservationList = new ReservationList();
		
		if(this.size() != 0){
			for(Reservation reservation : this){
				if(reservation.show == show){
					showReservationList.add(reservation);
				}
			}
		}
		return showReservationList;
	}
	
	
	// Gibt grösste bis jetzt vorhandene ID zurück +1
	public int getNewId() {
		if (this.size() == 0) {
			return 1;
		}
		int id = 0;

		for (Reservation reservation : this) {
			if (reservation.id > id) {
				id = reservation.id;
			}
		}

		return id++;
	}

	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeList(this, "reservations");

	}

}
