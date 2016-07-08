package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.FilmList;
import model.ReservationList;
import model.RoomList;
import model.ShowList;

public class FileStream {

	
	// ArrayList wird in File geschrieben
	public void serializeFilmList(FilmList list) {

		try {
			FileOutputStream fos = new FileOutputStream("data/films.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();
			System.out.println("Film has saved");
			
		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			
		} catch (IOException ioe) {
			System.err.println("\nEs ist ein Fehler aufgetreten!\n");
			ioe.printStackTrace();
		}
	}
	
	
	public void serializeReservationList(ReservationList list) {

		try {
			FileOutputStream fos = new FileOutputStream("data/reservations.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();
			System.out.println("Reservation has saved");
			
		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			
		} catch (IOException ioe) {
			System.err.println("\nEs ist ein Fehler aufgetreten!\n");
			ioe.printStackTrace();
		}
	}
	
	
	public void serializeRoomList(RoomList list) {

		try {
			FileOutputStream fos = new FileOutputStream("data/rooms.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();
			System.out.println("Room has saved");
			
		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			
		} catch (IOException ioe) {
			System.err.println("\nEs ist ein Fehler aufgetreten!\n");
			ioe.printStackTrace();
		}
	}
	
	
	public void serializeShowList(ShowList list) {

		try {
			FileOutputStream fos = new FileOutputStream("data/shows.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();
			System.out.println("Show has saved");
			
		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			
		} catch (IOException ioe) {
			System.err.println("\nEs ist ein Fehler aufgetreten!\n");
			ioe.printStackTrace();
		}
	}
	

	
	
	// ArrayList wird aus File gelesen, wenn keine Liste vorhanden dann wird neue erstellt
	public FilmList deserializeFilmList() {

		try {
			FileInputStream fis = new FileInputStream("data/films.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			FilmList list = (FilmList) ois.readObject();
			ois.close();
			fis.close();
			return list;

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound");
			return new FilmList();

		} catch (IOException | ClassNotFoundException w) {
			return new FilmList();
		}
	}

	
	public ReservationList deserializeReservationList() {

		try {
			FileInputStream fis = new FileInputStream("data/reservations.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			ReservationList list = (ReservationList) ois.readObject();
			ois.close();
			fis.close();
			return list;

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound");
			return new ReservationList();

		} catch (IOException | ClassNotFoundException w) {
			return new ReservationList();
		}
	}
		
	
	public ShowList deserializeShowList() {

		try {
			FileInputStream fis = new FileInputStream("data/shows.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			ShowList list = (ShowList) ois.readObject();
			ois.close();
			fis.close();
			return list;

		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			return new ShowList();
			
		} catch (IOException | ClassNotFoundException w) {
			return new ShowList();
		}
	}
		

	public RoomList deserializeRoomList() {

		try {
			FileInputStream fis = new FileInputStream("data/rooms.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			RoomList list = (RoomList) ois.readObject();
			ois.close();
			fis.close();
			return list;

		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			return new RoomList();
			
		} catch (IOException | ClassNotFoundException w) {
			return new RoomList();
		}
	}
	
}
