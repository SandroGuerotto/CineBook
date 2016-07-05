package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controller.FileStream;

public class ShowList extends ArrayList<Show> {
	Show show;
	FileStream fileStream;
	
	public ShowList(){
		fileStream = new FileStream();
		show = new Show();
	}

	// Neue Show hinzuf�gen
	public void addShow(Show show) {
		this.add(show);

		save();
	}

	// Neue Show erstellen und hinzuf�gen
	public void addShow(int id, Room room, Film film, Date startDateTime, Date endDateTime, int durationInMinutes) {
		show = show.newShow(id, room, film, startDateTime, endDateTime, durationInMinutes);
		this.add(show);

		save();
	}

	// Show editieren (id muss nat�rlich die der alten Show sein)
	public void editShow(Show editedShow) {
		show = getShowById(editedShow.id);
		show.editShow(editedShow.room, editedShow.film, editedShow.startDateTime, editedShow.endDateTime,
				editedShow.durationInMinutes);

		save();
	}

	// Show bearbeiten mit einzelnen Parameter (id muss nat�rlich die der alten
	// Show sein)
	public void editShow(int id, Room newRoom, Film newFilm, Date newStartDateTime, Date newEndDateTime,
			int newDurationInMinutes) {
		show = getShowById(id);
		show.editShow(newRoom, newFilm, newStartDateTime, newEndDateTime, newDurationInMinutes);

		save();
	}

	// Show l�schen mit Objekt (boolean gibt Wert zur�ck ob wirklich gel�scht
	// wurde)
	public boolean deleteShow(Show show) {
		show = getShowById(show.id);
		return this.remove(show);
	}

	// Show l�schen mit id (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteShow(int id) {
		show = getShowById(id);
		return this.remove(show);
	}

	// Sucht Show per Id, wenn keine gefunden dann wird null zur�ckgegeben
	public Show getShowById(int id) {
		
		if(this.size() != 0){
			for (Show show : this) {
				if (show.id == id) {
					return show;
				}
			}
		}
		return null;
	}
	
	// Gibt alle Shows zur�ck die an einem bestimmten Tag stattfinden
	public ShowList getShowsByDate(String mainDate){
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		ShowList tmpShowList = new ShowList();
		
		// �berpr�ft ob mainDate valid ist
		try { Date tmpDate = format.parse(mainDate); } catch (ParseException e) { return null; }
		
		if(this.size() != 0){
			for(Show show : this){
				if(mainDate.equals(format.format(show.startDateTime))){
					tmpShowList.add(show);
				}
			}
		}
		return tmpShowList;
	}
	

	// Man �bergibt beim Erstellen einer neuen Show die geplante Startzeit und den Film
	// Eine Liste mit nicht besetzen R�umen wird zur�ckgegeben
	public RoomList getAvailableRooms(Date startDateTime, Film film, RoomList roomList) {

		RoomList tmpRoomList = roomList;
		long filmStartMillisec = startDateTime.getTime();
		long filmDurationMillisec = film.durationInMinutes * 60000;
		long filmEndMillisec = filmStartMillisec + filmDurationMillisec;

		for (Show show : this) {
			long showStartMillisec = show.startDateTime.getTime() - 1800000;
			long showEndMillisec = show.endDateTime.getTime() + 1800000;

			if (filmStartMillisec >= showStartMillisec && filmEndMillisec <= showEndMillisec) {
				tmpRoomList.deleteRoom(show.room);
			}

			else if (filmEndMillisec >= showStartMillisec && filmEndMillisec <= showEndMillisec) {
				tmpRoomList.deleteRoom(show.room);
			}
		}

		return tmpRoomList;
	}

	// �berpr�fen ob neue Show in geplantem Saal erstellt werden kann
	public boolean isAvailable(Date startDateTime, Film film) {
		long filmStartMillisec = startDateTime.getTime();
		long filmDurationMillisec = film.durationInMinutes * 60000;
		long filmEndMillisec = filmStartMillisec + filmDurationMillisec;

		for (Show show : this) {
			long showStartMillisec = show.startDateTime.getTime() - 1800000;
			long showEndMillisec = show.endDateTime.getTime() + 1800000;

			if (filmStartMillisec >= showStartMillisec && filmEndMillisec <= showEndMillisec) {
				return false;
			}

			else if (filmEndMillisec >= showStartMillisec && filmEndMillisec <= showEndMillisec) {
				return false;

			}
		}
		return true;
	}

	// Rechnet anhand von Filmdauer das Spielende aus und gibt dieses im Date Format zur�ck
	public Date getEndTime(Date startDateTime, Film film) {
		long filmStartMillisec = startDateTime.getTime();
		long durationMillisec = film.durationInMinutes * 60000;
		long filmEndMillisec = filmStartMillisec + durationMillisec;
		return new Date(filmEndMillisec);
	}

	// Gibt gr�sste bis jetzt vorhandene ID zur�ck +1
	public int getNewId() {
		if (this.size() == 0) {
			return 1;
		}
		int id = 0;

		for (Show show : this) {
			if (show.id > id) {
				id = show.id;
			}
		}

		return id++;
	}

	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeList(this, "shows");

	}

}
