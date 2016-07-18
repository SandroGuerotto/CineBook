package model;

import java.io.Serializable;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Room Vorlage
 */

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

	public void setName(String name) {
		this.name = name;
	}
	
	
}
