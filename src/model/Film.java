package model;

import java.io.Serializable;


public class Film implements Serializable {

	int id;
	int durationInMinutes;
	String title;
	String description;
	String imagePath;

	// Gibt eine neuen Film zurück
	public Film newFilm(int id, int durationInMinutes, String title, String description, String imagePath) {
		this.id = id;
		this.durationInMinutes = durationInMinutes;
		this.title = title;
		this.description = description;
		this.imagePath = imagePath;
		return this;
	}

	// Editiert den Film
	public void editFilm(int durationInMinutes, String title, String description, String imagePath) {
		this.durationInMinutes = durationInMinutes;
		this.title = title;
		this.description = description;
		this.imagePath = imagePath;
	}

	public String getTitle() {
		return title;
	}
	
	public int getDurationInMinutes(){
		return durationInMinutes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
