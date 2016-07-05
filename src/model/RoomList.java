package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.FileStream;

public class RoomList extends ArrayList<Room> implements Serializable{
	Room room;
	FileStream fileStream;

	public RoomList(){
		fileStream = new FileStream();
		room = new Room();
	}
	
	// Neuen Room hinzufügen
	public void addRoom(Room room) {
		this.add(room);

		save();
	}

	// Neuen Room erstellen und hinzufügen
	public void addRoom(String name) {
		room = room.newRoom(name);
		this.add(room);

		save();
	}

	// Room editieren (name muss natürlich die des alten Filmes sein)
	public void editRoom(String oldName, Room editedRoom) {
		room = getRoomByName(oldName);
		room.editRoom(editedRoom.name, editedRoom.seatArrangement);

		save();
	}

	// Room bearbeiten mit einzelnen Parameter (id muss natürlich die des alten Filmes sein)
	public void editRoom(String oldName, String newName, String[][] newSeatArrangement) {
		room = getRoomByName(oldName);
		room.editRoom(newName, newSeatArrangement);

		save();
	}

	// Room löschen mit Objekt (boolean gibt Wert zurück ob wirklich gelöscht wurde)
	public boolean deleteRoom(Room room) {
		room = getRoomByName(room.name);
		return this.remove(room);
	}

	// Room löschen mit id (boolean gibt Wert zurück ob wirklich gelöscht wurde)
	public boolean deleteRoom(String name) {
		room = getRoomByName(name);
		return this.remove(room);
	}

	public void addSeat() {

	}

	// Sucht Room per Name, wenn keine gefunden dann wird null zurückgegeben
	public Room getRoomByName(String name) {

		if(this.size() != 0){
			for (Room room : this) {
	
				if (room.name.equals(name)) {
					return room;
				}
			}
		}
		return null;
	}

	// Überprüfen ob ein Room mit gleichem Namen bereits existiert
	public boolean doRoomExist(String name) {
		if(this.size() != 0){
			for (Room room : this) {
				if (room.name.equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeList(this, "rooms");

	}

}
