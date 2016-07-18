package model;

import java.util.ArrayList;

import controller.FileStream;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Beinhaltet und Verwaltet Filme
 */

public class FilmList extends ArrayList<Film> {


	transient Film film;
	transient FileStream fileStream;
	
	public FilmList() {
	}

	// Neuen Film hinzuf�gen
	public void addFilm(Film film){
		this.add(film);
		
		save();
	}
	
	// Neuen Film erstellen und hinzuf�gen
	public void addFilm(int id, int durationInMinutes, String title, String description, String imagePath){
		film = new Film(id, durationInMinutes, title, description, imagePath);
		this.add(film);
		
		save();
	}
	
	// Film editieren (id muss nat�rlich die des alten Filmes sein)
	public void editFilm(Film editedFilm){
		film = getFilmById(editedFilm.id);
		
		film.durationInMinutes = editedFilm.durationInMinutes;
		film.title = editedFilm.title;
		film.description = editedFilm.description;
		film.imagePath = editedFilm.imagePath;

		save();
	}
	
	
	// Film l�schen mit Objekt (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteFilm(Film film){
		film = getFilmById(film.id);
		
		boolean hasRemoved = this.remove(film);
		save();
		
		return hasRemoved;
	}
	

	
	// Pr�fen ob ein Film schon existiert mit Variablen
	public boolean doFilmExist(String title){
		
		if(this.size() != 0){
			for(Film film : this){
				if(title.equals(film.title)){
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
			System.out.println("Liste ist leer");
			return 1;
		}
		int id = 0;
			
		for(Film film : this){
			if(film.id > id){
				id = film.id;
			}
		}
		return id+1;
	}
	
	// �berpr�ft ob Cover sonst noch gebraucht wird, damit man Unn�tige l�schen kann
	public boolean isCoverUsedByOtherFilm(Film film){
		for(Film tmpFilm : this){
			if(film.imagePath.equals(tmpFilm.imagePath)){
				return true;
			}
		}
		
		
		return false;
	}
	
	
	// Liste via FileStream in File schreiben
	public void save() {
		fileStream.serializeFilmList(this);
	}
	
	
	public void setFileStream(FileStream fileStream) {
		this.fileStream = fileStream;
	}

}
