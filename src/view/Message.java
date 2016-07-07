package view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Message {

	private Label lbl_message;
	private boolean backToMenu = true;
	private String type;

	public Message(Label obj) {
		this.lbl_message = obj;
	}

	public boolean showMsg(String msgCode) {
		char msgType = msgCode.charAt(0);
		msgCode = msgCode.substring(1);
		switch (msgType) {
		case 's':
			type = "msg_success";
			backToMenu = true;
			break;
		case 'i':
			type = "msg_info";
			break;
		case 'w':
			type = "msg_warning";
			break;
		case 'e':
			type = "msg_error";
			break;
		}

		switch (msgCode) {
		case "0":
			setProperties(type, "Film successfully deleted");
			break;
		case "1":
			setProperties(type, "Can't delete film. Film is in use!");
			break;
		case "2":
			setProperties(type, "Can't delete filmcover. Cover is in use!");
			break;
		case "3":
			setProperties(type, "An error occurred while deleting the film!");
			break;
		case "4":
			setProperties(type, "Film successfully saved");
			break;
		case "5":
			setProperties(type, "Duration is not a number!");
			break;
		case "6":
			setProperties(type, "Could not load image. Invalid path or file!");
			break;
		case "7":
			setProperties(type, "Film already exists!");
			break;
		case "8":
			setProperties(type, "An error occurred while deleting the filmcover!");
			break;
		case "9":
			setProperties(type, "Please fill everything out!");
			break;
		case "10":
			setProperties(type, "Room successfully created");
			break;
		case "11":
			setProperties(type, "Room already exists!");
			break;
		case "12":
			setProperties(type, "Room successfully saved");
			break;
		case "13":
			setProperties(type, "Room successfully deleted");
			break;
		case "14":
			setProperties(type, "Room successfully created");
			break;
		case "15":
			setProperties(type, "Can't delete Room. Room is in use!");
			break;
		case "16":
			setProperties(type, "No Rooms are existing! Please create one!");
			break;
		case "17":
			setProperties(type, "No films are existing! Please create one!");
			break;
		case "18":
			setProperties(type, "Film successfully created!");
			break;
		}
		removeMsg();
		return backToMenu;
	}

	private void setProperties(String cssClass, String msg) {
		lbl_message.getStyleClass().add(cssClass);
		lbl_message.setText(msg);
		lbl_message.setVisible(true);
		lbl_message.setDisable(false);
	}

	private void deleteStyleMsg(String name) {
		ObservableList<String> list = lbl_message.getStyleClass();
		try {
			for (String cssclass : list) {
				if (cssclass.matches(name)) {
					list.remove(cssclass);
					lbl_message.setText("");
					lbl_message.setVisible(false);
					lbl_message.setDisable(true);
					return;
				}
			}
		} catch (Exception e) {
			return;
		}

	}

	private void removeMsg() {
		// Label & Button nach bestimmter zeit not Visible
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(() -> {
			Platform.runLater(() -> {
				deleteStyleMsg("msg_.*");
			});

		}, 5, TimeUnit.SECONDS);

	}
}