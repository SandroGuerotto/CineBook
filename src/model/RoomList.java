package model;

import java.util.ArrayList;

import controller.FileStream;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Beinhaltet und Verwaltet Rooms
 */

public class RoomList extends ArrayList<Room> {
	transient Room room;
	transient FileStream fileStream;

	public RoomList(){

	}
	
	// Neuen Room hinzuf�gen
	public void addRoom(Room room) {
		this.add(room);

		save();
	}

	// Neuen Room erstellen und hinzuf�gen
	public void addRoom(String name) {
		room = new Room(name);
		this.add(room);

		save();
	}

	// Room editieren (name muss nat�rlich die des alten Filmes sein)
	public void editRoom(String oldName, Room editedRoom) {
		room = getRoomByName(oldName);
		room.name = editedRoom.name;

		save();
	}


	// Room l�schen mit Objekt (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteRoom(Room room) {
		
		room = getRoomByName(room.name);
		
		boolean hasRemoved = this.remove(room);
		save();
		
		return hasRemoved;
	}



	// Sucht Room per Name, wenn keine gefunden dann wird null zur�ckgegeben
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

	// �berpr�fen ob ein Room mit gleichem Namen bereits existiert
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
		fileStream.serializeRoomList(this);

	}

	public void setFileStream(FileStream fileStream) {
		this.fileStream = fileStream;
	}
}
