package view;

import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.Room;
import model.RoomList;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Der Popup Dialog für das editieren eines Room
 */

public class EditRoomDialog {
	private String select = "";
	public Pair<String, String> show(RoomList roomlist) {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Edit an existing room");
		dialog.setHeaderText("Edit an existing room");

		// Set the icon (must be included in the project).
//		dialog.setGraphic(new ImageView(this.getClass().getResource("File:images/edit.png").toString()));

		// Set the button types.
		ButtonType save = new ButtonType("Save", ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(save, cancel);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ArrayList<String> choices = new ArrayList<String>();
		for (Room current : roomlist) {
			choices.add(current.getName());
		}
		ChoiceBox<String> rooms = new ChoiceBox<>(FXCollections.observableArrayList(choices));
		TextField newName = new TextField();
		newName.setPromptText("New room name");

		grid.add(new Label("Choose a room:"), 0, 0);
		grid.add(rooms, 1, 0);
		grid.add(new Label("Enter new name:"), 0, 1);
		grid.add(newName, 1, 1);
		
		// Do some validation (using the Java 8 lambda syntax).
		rooms.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				select = newValue;
			}
	
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> rooms.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == save) {
				return new Pair<>(select, newName.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		if(result.isPresent()){
			return result.get();
		}
//		result.ifPresent(usernamePassword -> {
//			return result;
//			System.out.println("Raum=" + select + ", Neu=" + newName.getText());
//		});
		return null;
	}
}
