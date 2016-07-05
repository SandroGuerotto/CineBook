package model;

import java.util.ArrayList;

import controller.FileStream;


public class FilmList extends ArrayList<Film> {

	Film film;
	FileStream fileStream;
	
	
	
	public FilmList() {
		fileStream = new FileStream();
		film = new Film();
	}

	// Neuen Film hinzuf�gen
	public void addFilm(Film film){
		this.add(film);
		
		save();
	}
	
	// Neuen Film erstellen und hinzuf�gen
	public void addFilm(int id, int durationInMinutes, String title, String description, String imagePath){
		film = film.newFilm(id, durationInMinutes, title, description, imagePath);
		this.add(film);
		
		save();
	}
	
	// Film editieren (id muss nat�rlich die des alten Filmes sein)
	public void editFilm(Film editedFilm){
		film = getFilmById(editedFilm.id);
		film.editFilm(editedFilm.durationInMinutes, editedFilm.title, editedFilm.description, editedFilm.imagePath);

		save();
	}
	
	// Film bearbeiten mit einzelnen Parameter (id muss nat�rlich die des alten Filmes sein)
	public void editFilm(int id, int newDurationInMinutes, String newTitle, String newDescription, String newImagePath){
		film = getFilmById(id);
		film.editFilm(newDurationInMinutes, newTitle, newDescription, newImagePath);
			
		save();
	}
	
	// Film l�schen mit Objekt (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteReservation(Film film){
		film = getFilmById(film.id);
		return this.remove(film);
	}
	
	// Film l�schen mit id (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteFilm(int id){
		film = getFilmById(id);
		return this.remove(film);
	}
	
	// Pr�fen ob ein Film schon existiert mit Variablen
	public boolean doFilmExist(int duration, String title){
		
		if(this.size() != 0){
			for(Film film : this){
				if(duration == film.durationInMinutes && title.equals(film.title)){
					return true;
				}			
			}
		}
		return false;
	}
	
	// Sucht Film per Id, wenn keine gefunden dann wird null zur�ckgegeben
	public Film getFilmById(int id){
		
		if(this.size() != 0){
			for(Film film : this){
				
				if(film.id == id){
					return film;
				}
			}
		}
		return null;
	}
	
	// Gibt gr�sste bis jetzt vorhandene ID zur�ck +1
	public int getNewId(){
		if(this.size() == 0){
			return 1;
		}
		
		int id = 0;
			
		for(Film film : this){
			if(film.id > id){
				id = film.id;
			}
		}
			
		return id++;
	}
	
	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeList(this, "films");
	}
	

}
