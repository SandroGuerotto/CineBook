package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import model.FilmList;
import model.ReservationList;
import model.RoomList;
import model.ShowList;

public class FileStream {

	// ArrayList wird in File geschrieben
	public void serializeList(List<?> list, String fileName) {

		try {
			FileOutputStream fos = new FileOutputStream("@../../data/" + fileName + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();
			
		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			
		} catch (IOException ioe) {
			System.err.println("\nEs ist ein Fehler aufgetreten!\n");
			ioe.printStackTrace();
		}
	}

	// ArrayList wird aus File gelesen, wenn keine Liste vorhanden dann wird neue erstellt
	public List<?> deserializeList(String fileName) {

		try {
			FileInputStream fis = new FileInputStream("@../../data/" + fileName + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			List<?> list = (List<?>) ois.readObject();
			ois.close();
			fis.close();
			return list;

		}catch(FileNotFoundException e){
			System.out.println("FileNotFound");
			return null;
			
		} catch (IOException | ClassNotFoundException w) {
			switch(fileName){
			case "reservations":
				return new ReservationList();
				
			case "shows":
				return new ShowList();
				
			case "films":
				return new FilmList();
				
			case "rooms":
				return new RoomList();
				
			default:
				return null;
			}
		}
	}
}
