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

		// �berpr�fen ob Show null ist
		if (show == null)
			return false;

		for (String seatNr : seatNumbers) {

			// �berpr�ft ob Reservation bereits existiert
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

		// �berpr�ft ob Datum valid ist
		try {
			date = format.parse(startDateTime);
		} catch (ParseException e) {
			return false;
		}

		// �berpr�ft ob room & film null sind
		if (room == null || film == null)
			return false;

		// �berpr�ft ob zu dieser Zeit kein anderer Film l�uft
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

		// �berpr�ft ob Filml�nge eine Zahl ist
		try {
			duration = Integer.parseInt(durationInMinutes);
		} catch (Exception e) {
			return 1; //Fehlerhafte eingabe
		}

		// �berpr�ft ob Pfad existiert
		File file = new File(imagePath);
		if (file.exists() && !file.isDirectory()) {
			return 2; //ung�ltiger Pfad
		}
		// �berpr�ft ob Film bereits existiert
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

	// Gibt alle nicht besetzten R�ume zur�ck (geplante Startzeit & geplanter
	// Film muss �bergeben werden) -----------------
	public RoomList getAllAvailableRooms(Date startDateTime, Film film) {

		RoomList tmpRoomList = this.showList.getAvailableRooms(startDateTime, film, this.roomList);
		this.tmpRoomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		return tmpRoomList;
	}

	// Gibt alle Shows zur�ck die an einem bestimmten Tag stattfinden
	// -----------------------------------------------------
	public ShowList getShowsByDate(String date) {

		return showList.getShowsByDate(date);
	}

	// Gibt alle dazugeh�rigen Reservationen zur�ck
	// -----------------------------------------------------------------------
	public ReservationList getAttendantReservations(Reservation reservation) {
		return reservationList.getAttendantReservations(reservation);
	}

	// Gibt alle Reservationen die zur gleichen Show geh�ren zur�ck
	// -------------------------------------------------------
	public ReservationList getReservationsByShow(Show show) {
		return reservationList.getReservationsByShow(show);
	}

	// Gibt alle Reservationen zur�ck
	// -------------------------------------------------------------------------------------
	public ReservationList getAllReservations() {

		this.reservationList.sort((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));
		return this.reservationList;
	}

	// Gibt alle Shows zur�ck
	// ---------------------------------------------------------------------------------------------
	public ShowList getAllShows() {

		this.showList.sort((o1, o2) -> o1.getStartDateTime().compareTo(o2.getStartDateTime()));
		return this.showList;
	}

	// Gibt alle Filme zur�ck
	// ---------------------------------------------------------------------------------------------
	public FilmList getAllFilms() {

		this.filmList.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
		return this.filmList;
	}

	// Gibt alle Rooms zur�ck
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
	// // �berpr�ft ob Show null ist
	// if(show == null)
	// return false;
	//
	// // �berpr�ft ob Datum valid ist
	// try{ reservationDateTime = format.parse(dateTime); } catch(ParseException
	// e){ return false; }
	//
	// // �berpr�ft ob Reservation bereits existiert
	// if(reservationList.doReservationExist(show, seatNumber) == false){
	// reservationList.addReservation(reservationList.getNewId(), show,
	// seatNumber, phoneNumber, reservationDateTime);
	// return true;
	// }
	// return false;
	// }
}
