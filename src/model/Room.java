package model;

import java.io.Serializable;

public class Room implements Serializable {
	
	String name;
	String[][] seatArrangement; // Example: [A1][0], [A2][1], [A3][1]
	
	public Room(){

	}
	
	// Gibt einen neuen Room zurück
	public Room newRoom(String name){
		this.name = name;
		this.seatArrangement = new String[10][15];
		return this;
	}
	
	// Room editieren, bei Room ist auch der Name(Id) editierbar.
	public void editRoom(String name, String[][] seatArrangement){
		this.name = name;
		this.seatArrangement = seatArrangement;
	}

	public String getName() {
		return name;
	}
	
}
