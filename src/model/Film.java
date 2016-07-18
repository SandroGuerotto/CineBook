package model;

import java.io.Serializable;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Film Vorlage
 */

public class Film implements Serializable {

	int id;
	int durationInMinutes;
	String title;
	String description;
	String imagePath;

	public Film(){
		
	}
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

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public String getDescription() {
		return this.description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
