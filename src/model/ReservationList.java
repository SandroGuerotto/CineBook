package model;

import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import controller.FileStream;

public class ReservationList extends ArrayList<Reservation> {

	transient Reservation reservation;
	transient FileStream fileStream;

	public ReservationList() {

	}

	// Neue Reservation hinzuf�gen
	public void addReservation(Reservation reservation) {
		this.add(reservation);

		save();
	}
	
	// Neue Reservation erstellen und hinzuf�gen
	public void addReservation(int id, Show show, String seatNumber, String phoneNumber, Date dateTime) {
		reservation = new Reservation(id, show, seatNumber, phoneNumber, dateTime);
		this.add(reservation);

		save();
	}

	// Reservation editieren (id muss nat�rlich die der alten Reservierung sein)
	public void editReservation(Reservation editedReservation) {
		reservation = getReservationById(editedReservation.id);
		reservation.show = editedReservation.show;
		reservation.seatNumber = editedReservation.seatNumber;
		reservation.phoneNumber = editedReservation.phoneNumber;
		reservation.dateTime = editedReservation.dateTime;

		save();
	}


	// Reservation l�schen mit Objekt (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteReservation(Reservation reservation) {
		reservation = getReservationById(reservation.id);
		
		boolean hasRemoved = this.remove(reservation);
		save();
		
		return hasRemoved;

	}


	
	// Gibt eine Reservation anhand eines Sitzplatzes zur�ck
	public Reservation getReservationBySeatNumber(Show show, String seatNumber){
		
		for(Reservation reservation : this){
			if(reservation.show == show && reservation.seatNumber.equals(seatNumber)){
				return reservation;
			}
		}
		
		return null;
	}

	// Sucht Reservation per Id, wenn keine gefunden dann wird null zur�ckgegeben
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

	// Pr�fen ob eine Reservation schon existiert mit Objekt
	public boolean doReservationExist(Reservation reservation) {
		
		if(this.size() != 0){
			for (Reservation item : this) {
				if (reservation.getShow() == item.getShow() && reservation.seatNumber == item.seatNumber) {
					return true;
				}
			}
		}
		return false;
	}

	// Pr�fen ob eine Reservation schon existiert mit Variablen
	public boolean doReservationExist(Show show, String seatNumber) {
		
		if(this.size() != 0){
			for (Reservation reservation : this) {
				if (show == reservation.getShow() && seatNumber == reservation.seatNumber) {
					return true;
				}
			}
		}
		return false;
	}

// 	Gibt alle dazugeh�rigen Reservationen zur�ck
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
	
	
	
	// Gibt eine Liste mit den bereits reservierten Pl�tzen zur�ck
	public ArrayList<String> getReservedSeats(Show show){
		ArrayList<String> tmpList = new ArrayList<>();
		
		for(Reservation reservation : this){
			if(reservation.getShow().getId() == show.getId()){
				tmpList.add(reservation.seatNumber);
			}
		}
		
		return tmpList;
	}
	
	// Gibt alle Reservationen die zur gleichen Show geh�ren zur�ck
	public ReservationList getReservationsByShow(Show show){
		ReservationList showReservationList = new ReservationList();
		
		if(this.size() != 0){
			for(Reservation reservation : this){
				if(reservation.getShow() == show){
					showReservationList.add(reservation);
				}
			}
		}
		return showReservationList;
	}
	
	
	// Gibt gr�sste bis jetzt vorhandene ID zur�ck +1
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

		return id+1;
	}

	
	
	
	
	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeReservationList(this);

	}

	public void setFileStream(FileStream fileStream) {
		this.fileStream = fileStream;
	}

	
	
	
}

