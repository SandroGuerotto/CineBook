package model;

import java.io.Serializable;

public class Room implements Serializable {
	
	String name;
	
	public Room(){

	}
	
	// Gibt einen neuen Room zurück
	public Room(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
