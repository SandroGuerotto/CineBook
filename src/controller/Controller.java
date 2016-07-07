package controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
	
	
	public Controller(){
		fileStream = new FileStream();
		
		reservationList = fileStream.deserializeReservationList();
		showList = fileStream.deserializeShowList();
		filmList = fileStream.deserializeFilmList();
		roomList = fileStream.deserializeRoomList();
		
		reservationList.setFileStream(fileStream);
		showList.setFileStream(fileStream);
		filmList.setFileStream(fileStream);
		roomList.setFileStream(fileStream);
		
		for(Film film : filmList){
			System.out.print(film.getId() + ": ");
			System.out.print(film.getTitle() + ", ");
			System.out.print(film.getImagePath()+"\n");
		}
	}
	
// 	Meherer Reservations erstellen -------------------------------------------------------------------------------------
	public boolean createNewReservations(Show show, ArrayList<String> seatNumbers, String phoneNumber){
		
		Date dateTime;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String timeStamp = format.format(Calendar.getInstance().getTime());
		
		// Akuteller Zeitstempfel erzeugen
		try { dateTime = format.parse(timeStamp); } catch (ParseException e) { return false; }
		
		// Überprüfen ob Show null ist
		if(show == null)
			return false;
		
		for(String seatNr : seatNumbers){
			
			// Überprüft ob Reservation bereits existiert
			if(reservationList.doReservationExist(show, seatNr) == false){
				reservationList.addReservation(reservationList.getNewId(), show, seatNr, phoneNumber, dateTime);
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	
// 	Eine neue Show erstellen -------------------------------------------------------------------------------------------
	public boolean createNewShow(Room room, Film film, String startDate, String startTime){
		
		Date date;
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String startDateTime = startDate + " " + startTime;
		
		// Überprüft ob Datum valid ist
		try{ date = format.parse(startDateTime); } catch(ParseException e){ return false; }
		
		// Überprüft ob room & film null sind
		if(room == null || film == null)
			return false;
		
		// Überprüft ob zu dieser Zeit kein anderer Film läuft
		if(showList.isAvailable(date, film) == true){
			showList.addShow(showList.getNewId(), room, film, date, showList.getEndTime(date, film), film.getDurationInMinutes());
			return true;
		}
		return false;
	}
	
// 	Einen neuen Film erstellen -----------------------------------------------------------------------------------------
	public int createNewFilm(String durationInMinutes, String title, String description, String imagePath){
		
		int duration;
		
		// Überprüft ob Filmlänge eine Zahl ist
		try{ duration = Integer.parseInt(durationInMinutes); } catch(Exception e){ return 0; }
		
//		// Überprüft ob Pfad existiert
//		File file = new File(imagePath);
//		if (!file.isDirectory())
//		   return 0;
		
		// Image in Covers Ordner kopieren
		String subPath = imagePath.split("file:/")[1];
		Path copy_from_1 = Paths.get(subPath);
		
		String name = copy_from_1.getName(copy_from_1.getNameCount()-1).toString();
		
	    Path copy_to_1 = Paths.get("@../../covers/"+name);
	    try {
	      Files.copy(copy_from_1, copy_to_1, REPLACE_EXISTING, COPY_ATTRIBUTES,
	          NOFOLLOW_LINKS);
	    } catch (IOException e) {
	    	System.err.println(e);
	    }
		
		// Überprüft ob Film bereits existiert
		if(filmList.doFilmExist(title) == false){
			filmList.addFilm(filmList.getNewId(), duration, title, description, "@../../covers/"+name);
			return 0;
			
		}
		return 0;
	}
	
// 	Einen neuen Room erstellen -----------------------------------------------------------------------------------------
	public boolean createNewRoom(String name){
		
		if(roomList.doRoomExist(name) == false){
			roomList.addRoom(name);
			return true;
		}
		return false;
	}
	
// 	Gibt alle nicht besetzten Räume zurück (geplante Startzeit & geplanter Film muss übergeben werden) -----------------
	public RoomList getAllAvailableRooms(Date startDateTime, Film film){
		
		RoomList tmpRoomList = this.showList.getAvailableRooms(startDateTime, film, this.roomList);
		this.tmpRoomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		
		return tmpRoomList;
	}
	
// 	Gibt alle Shows zurück die an einem bestimmten Tag stattfinden -----------------------------------------------------
	public ShowList getShowsByDate(String date){
		
		return showList.getShowsByDate(date);
	}
	
//	Löscht eine Show mit all ihren Reservierungen ----------------------------------------------------------------------
	public void deleteShowAndReservations(Show show){
		
		showList.deleteShow(show);
		
		for(Reservation reservation : getAllReservations()){
			if(reservation.getShow() == show){
				reservationList.deleteReservation(reservation);
			}
		}
	}
	
// 	Gibt eine Liste mit den bereits reservierten Plätzen zurück --------------------------------------------------------
	public ArrayList<String> getReservedSeats(Show show){
		
		return reservationList.getReservedSeats(show);
	}
	
// 	Gibt alle dazugehörigen Reservationen zurück -----------------------------------------------------------------------
	public ReservationList getAttendantReservations(Reservation reservation){
		
		return reservationList.getAttendantReservations(reservation);
	}
	
//	Löscht alle zusammengehörigen Reservationen (Date & Phonenumber) ---------------------------------------------------
	public void deleteAllAttendantReservations(Reservation reservation){
		
		for(Reservation tmpReservation : reservationList.getAttendantReservations(reservation)){
			reservationList.deleteReservation(tmpReservation);
		}
	}	
	
//	Einzelne Reservation löschen ---------------------------------------------------------------------------------------
	public void deleteSingleReservation(Reservation reservation){
		
		reservationList.deleteReservation(reservation);
	}
	
//	Eine Reservation bearbeiten ----------------------------------------------------------------------------------------
	public void editReservation(Reservation reservation){
		
		reservationList.editReservation(reservation);
	}
	
//	Gibt eine Reservation anhand eines Sitzplatzes zurück --------------------------------------------------------------
	public Reservation getReservationBySeatNumber(Show show, String seatNumber){
		
		return reservationList.getReservationBySeatNumber(show, seatNumber);
	}
	
//	Gibt alle Reservationen die zur gleichen Show gehören zurück -------------------------------------------------------
	public ReservationList getReservationsByShow(Show show){
		
		return reservationList.getReservationsByShow(show);
	}
	
//	Film löschen -------------------------------------------------------------------------------------------------------
	public boolean deleteFilm(Film film){
		
		// Überprüfen ob Film in einer zukünftigen Show gebraucht wird
		if(showList.isShowDepending(film) == false){
			
			// Cover wird ebenfalls gelöscht wenn es nicht von anderen Filmen gebraucht wird
			if(filmList.isCoverUsedByOtherFilm(film) == false){
				Path deleteImage = Paths.get("File:"+film.getImagePath());
				try {
					Files.delete(deleteImage);
				} catch (IOException e) { e.printStackTrace(); }
			}
			filmList.deleteFilm(film);
			return true;
		}
		
		return false;
	}
	
//	Film bearbeiten ----------------------------------------------------------------------------------------------------
	public void editFilm(Film film){
		if(filmList.doFilmExist(film.getTitle())){
			filmList.editFilm(film);
		}
	}
	
//	Room löschen -------------------------------------------------------------------------------------------------------
	public boolean deleteRoom(Room room){
		
		// Überprüfen ob Room in zukünftiger Show gebraucht wird
		if(showList.isShowDepending(room) == false){
			roomList.deleteRoom(room);
			return true;
		}

		
		return false;
	}
	
//	Room bearbeiten ----------------------------------------------------------------------------------------------------
	public void editRoom(String oldName, Room room){
		
		roomList.editRoom(oldName, room);
	}
	
// 	Gibt alle Reservationen zurück -------------------------------------------------------------------------------------
	public ReservationList getAllReservations(){
		
		if(reservationList.size() > 1){
			this.reservationList.sort((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));
		}
		return this.reservationList;
	}
	
// 	Gibt alle Shows zurück ---------------------------------------------------------------------------------------------
	public ShowList getAllShows(){
		
		if(showList.size() > 1){
			this.showList.sort((o1, o2) -> o1.getStartDateTime().compareTo(o2.getStartDateTime()));
		}
		return this.showList;
	}
	
// 	Gibt alle Filme zurück ---------------------------------------------------------------------------------------------
	public FilmList getAllFilms(){
		
		if(filmList.size() > 1){
			this.filmList.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
		}
		return this.filmList;
	}
	
// 	Gibt alle Rooms zurück ---------------------------------------------------------------------------------------------
	public RoomList getAllRooms(){
		
		if(roomList.size() > 1){
			this.roomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		}
		return this.roomList;
	}

}
