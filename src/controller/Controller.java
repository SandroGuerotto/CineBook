package controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Film;
import model.FilmList;
import model.Reservation;
import model.ReservationList;
import model.Room;
import model.RoomList;
import model.Show;
import model.ShowList;

public class Controller {
	FileStream fileStream;
	ReservationList reservationList;
	ShowList showList;
	FilmList filmList;
	RoomList roomList;
	private RoomList tmpRoomList;

	public Controller() {
		fileStream = new FileStream();

		reservationList = (ReservationList) fileStream.deserializeList("reservations");
		showList = (ShowList) fileStream.deserializeList("shows");
		filmList = (FilmList) fileStream.deserializeList("films");
		roomList = (RoomList) fileStream.deserializeList("rooms");
	}

	// Meherer Reservations erstellen
	// -------------------------------------------------------------------------------------
	public boolean createNewReservations(Show show, String[] seatNumbers, String phoneNumber) {

		Date dateTime;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String timeStamp = format.format(Calendar.getInstance().getTime());

		// Akuteller Zeitstempfel erzeugen
		try {
			dateTime = format.parse(timeStamp);
		} catch (ParseException e) {
			return false;
		}

		// Überprüfen ob Show null ist
		if (show == null)
			return false;

		for (String seatNr : seatNumbers) {

			// Überprüft ob Reservation bereits existiert
			if (reservationList.doReservationExist(show, seatNr) == false) {
				reservationList.addReservation(reservationList.getNewId(), show, seatNr, phoneNumber, dateTime);
			} else {
				return false;
			}
		}

		return true;
	}

	// Eine neue Show erstellen
	// -------------------------------------------------------------------------------------------
	public boolean createNewShow(Room room, Film film, String startDate, String startTime) {

		Date date;
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String startDateTime = startDate + " " + startTime;

		// Überprüft ob Datum valid ist
		try {
			date = format.parse(startDateTime);
		} catch (ParseException e) {
			return false;
		}

		// Überprüft ob room & film null sind
		if (room == null || film == null)
			return false;

		// Überprüft ob zu dieser Zeit kein anderer Film läuft
		if (showList.isAvailable(date, film) == true) {
			showList.addShow(showList.getNewId(), room, film, date, showList.getEndTime(date, film),
					film.getDurationInMinutes());
			return true;
		}
		return false;
	}

	// Einen neuen Film erstellen
	// -----------------------------------------------------------------------------------------
	public int createNewFilm(String durationInMinutes, String title, String description, String imagePath) {

		int duration;

		// Überprüft ob Filmlänge eine Zahl ist
		try {
			duration = Integer.parseInt(durationInMinutes);
		} catch (Exception e) {
			return 1; //Fehlerhafte eingabe
		}

		// Überprüft ob Pfad existiert
		File file = new File(imagePath);
		if (file.exists() && !file.isDirectory()) {
			return 2; //ungültiger Pfad
		}
		// Überprüft ob Film bereits existiert
		if (filmList.doFilmExist(duration, title) == false) {
			filmList.addFilm(filmList.getNewId(), duration, title, description, imagePath);
			return 0; //alles ok
		}else{
			return 3; // Film existiert schon
		}
		
	}

	// Einen neuen Room erstellen
	// -----------------------------------------------------------------------------------------
	public boolean createNewRoom(String name) {

		if (roomList.doRoomExist(name) == false) {
			roomList.addRoom(name);
			return true;
		}
		return false;
	}

	// Gibt alle nicht besetzten Räume zurück (geplante Startzeit & geplanter
	// Film muss übergeben werden) -----------------
	public RoomList getAllAvailableRooms(Date startDateTime, Film film) {

		RoomList tmpRoomList = this.showList.getAvailableRooms(startDateTime, film, this.roomList);
		this.tmpRoomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		return tmpRoomList;
	}

	// Gibt alle Shows zurück die an einem bestimmten Tag stattfinden
	// -----------------------------------------------------
	public ShowList getShowsByDate(String date) {

		return showList.getShowsByDate(date);
	}

	// Gibt alle dazugehörigen Reservationen zurück
	// -----------------------------------------------------------------------
	public ReservationList getAttendantReservations(Reservation reservation) {
		return reservationList.getAttendantReservations(reservation);
	}

	// Gibt alle Reservationen die zur gleichen Show gehören zurück
	// -------------------------------------------------------
	public ReservationList getReservationsByShow(Show show) {
		return reservationList.getReservationsByShow(show);
	}

	// Gibt alle Reservationen zurück
	// -------------------------------------------------------------------------------------
	public ReservationList getAllReservations() {

		this.reservationList.sort((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));
		return this.reservationList;
	}

	// Gibt alle Shows zurück
	// ---------------------------------------------------------------------------------------------
	public ShowList getAllShows() {

		this.showList.sort((o1, o2) -> o1.getStartDateTime().compareTo(o2.getStartDateTime()));
		return this.showList;
	}

	// Gibt alle Filme zurück
	// ---------------------------------------------------------------------------------------------
	public FilmList getAllFilms() {

		this.filmList.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
		return this.filmList;
	}

	// Gibt alle Rooms zurück
	// ---------------------------------------------------------------------------------------------
	public RoomList getAllRooms() {

		this.roomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		return this.roomList;
	}

	// Eine neue Reservation erstellen
	// ------------------------------------------------------------------------------------
	// public boolean createNewReservation(Show show, String seatNumber, String
	// phoneNumber, String date, String time){
	//
	// Date reservationDateTime;
	// DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	// String dateTime = date + " " + time;
	//
	// // Überprüft ob Show null ist
	// if(show == null)
	// return false;
	//
	// // Überprüft ob Datum valid ist
	// try{ reservationDateTime = format.parse(dateTime); } catch(ParseException
	// e){ return false; }
	//
	// // Überprüft ob Reservation bereits existiert
	// if(reservationList.doReservationExist(show, seatNumber) == false){
	// reservationList.addReservation(reservationList.getNewId(), show,
	// seatNumber, phoneNumber, reservationDateTime);
	// return true;
	// }
	// return false;
	// }
}
