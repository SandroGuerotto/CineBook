package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controller.FileStream;

public class ShowList extends ArrayList<Show> {
	transient Show show;
	transient FileStream fileStream;

	public ShowList() {
	}

	// Neue Show hinzufügen
	public void addShow(Show show) {
		this.add(show);

		save();
	}

	// Neue Show erstellen und hinzufügen
	public void addShow(int id, Room room, Film film, Date startDateTime, Date endDateTime, int durationInMinutes) {
		show = new Show(id, room, film, startDateTime, endDateTime, durationInMinutes);
		this.add(show);

		save();
	}

	// Show editieren (id muss natürlich die der alten Show sein)
	public void editShow(Show editedShow) {
		show = getShowById(editedShow.id);
		show.room = editedShow.room;
		show.film = editedShow.film;
		show.startDateTime = editedShow.startDateTime;
		show.endDateTime = editedShow.endDateTime;
		show.durationInMinutes = editedShow.durationInMinutes;

		save();
	}

	// Show löschen mit Objekt (boolean gibt Wert zurück ob wirklich gelöscht
	// wurde)
	public boolean deleteShow(Show show) {
		show = getShowById(show.id);

		boolean hasRemoved = this.remove(show);
		save();

		return hasRemoved;
	}

	// Sucht Show per Id, wenn keine gefunden dann wird null zurückgegeben
	public Show getShowById(int id) {

		if (this.size() != 0) {
			for (Show show : this) {
				if (show.id == id) {
					return show;
				}
			}
		}
		return null;
	}

	// Überprüfen ob es in Zukunft eine Show mit diesem Film gibt
	public boolean isShowDepending(Film film) {
		Date date = new Date();

		for (Show show : this) {
			if (show.film == film && show.startDateTime.after(date) && show.film == film
					&& show.startDateTime.before(date)) {
				return true;
			}
		}

		return false;
	}

	// Überprüfen ob es in Zukunft eine Show mit diesem Room gibt
	public boolean isShowDepending(Room room) {
		Date date = new Date();

		for (Show show : this) {
			if (show.room == room && show.startDateTime.after(date) && show.room == room
					&& show.startDateTime.before(date)) {
				return true;
			}
		}

		return false;
	}

	// Gibt alle Shows zurück die an einem bestimmten Tag stattfinden
	public ShowList getShowsByDate(String mainDate) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		ShowList tmpShowList = new ShowList();

		// Überprüft ob mainDate valid ist
		try {
			Date tmpDate = format.parse(mainDate);
		} catch (ParseException e) {
			return null;
		}

		if (this.size() != 0) {
			for (Show show : this) {
				if (mainDate.equals(format.format(show.startDateTime))) {
					tmpShowList.add(show);
				}
			}
		}
		return tmpShowList;
	}

	// Man übergibt beim Erstellen einer neuen Show die geplante Startzeit und
	// den Film
	// Eine Liste mit nicht besetzen Räumen wird zurückgegeben
	public RoomList getAvailableRooms(Date startDateTime, Film film, RoomList roomList) {
		// System.out.println("StartDateTime" + startDateTime.toString());
		// create new tmp Room list
		RoomList tmpRoomList = new RoomList();
		tmpRoomList = fileStream.deserializeRoomList();

		long newShowStartTime = startDateTime.getTime();
		long filmDurationMillisec = film.durationInMinutes * 60000;
		long newShowEndTime = newShowStartTime + filmDurationMillisec + 0;

		if (tmpRoomList.size() > 0) {
			for (Show show : this) {
				long existShowStart = show.startDateTime.getTime();
				long existShowEnd = show.endDateTime.getTime();
				if ((existShowStart <= newShowStartTime && newShowStartTime  <= existShowEnd)
						|| (existShowStart <= newShowEndTime && existShowEnd >= newShowEndTime)) {
					for( Room room : tmpRoomList){
						if(room.getName().equals(show.room.getName())){
							tmpRoomList.remove(room);
							break;
						}
					}
					
				}

				// if (filmStartMillisec >= showStartMillisec && filmEndMillisec
				// <= showEndMillisec) {
				// tmpRoomList.remove(show.room);
				// }
				//
				// else if (filmEndMillisec >= showStartMillisec &&
				// filmEndMillisec <= showEndMillisec) {
				// tmpRoomList.remove(show.room);
				// } else {
				//
				// }
			}
		}
		return tmpRoomList;
	}

	// Überprüfen ob neue Show in geplantem Saal erstellt werden kann
	public boolean isAvailable(Date startDateTime, Film film) {
		long filmStartMillisec = startDateTime.getTime();
		long filmDurationMillisec = film.durationInMinutes * 60000;
		long filmEndMillisec = filmStartMillisec + filmDurationMillisec;

		if (!this.isEmpty()) {
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
		}
		return true;
	}

	// Rechnet anhand von Filmdauer das Spielende aus und gibt dieses im Date
	// Format zurück
	public Date getEndTime(Date startDateTime, Film film) {
		long filmStartMillisec = startDateTime.getTime();
		long durationMillisec = film.durationInMinutes * 60000;
		long filmEndMillisec = filmStartMillisec + durationMillisec;
		return new Date(filmEndMillisec);
	}

	// Gibt grösste bis jetzt vorhandene ID zurück +1
	public int getNewId() {
		if (this.size() == 0) {
			return 1;
		}
		int id = 0;

		for (Show show : this) {
			if (show.id > id) {
				id = show.id;
				System.out.println(id);
			}
		}
		id++;
		System.out.println(id);

		return id;
	}

	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeShowList(this);

	}

	public void setFileStream(FileStream fileStream) {
		this.fileStream = fileStream;
	}
}
