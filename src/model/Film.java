package model;

import java.io.Serializable;


public class Film implements Serializable {

	int id;
	int durationInMinutes;
	String title;
	String description;
	String imagePath;

	// Gibt eine neuen Film zurück
	public Film(int id, int durationInMinutes, String title, String description, String imagePath) {
		this.id = id;
		this.durationInMinutes = durationInMinutes;
		this.title = title;
		this.description = description;
		this.imagePath = imagePath;
	}

	
	
	public int getId() {
		return id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getTitle() {
		return title;
	}
	
	public int getDurationInMinutes(){
		return durationInMinutes;
	}

	public String getDescription() {
		return this.description;
	}
}
