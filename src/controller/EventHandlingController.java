package controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Film;
import model.FilmList;
import model.RoomList;

/**
 * 
 * @author Tim Meier & Sandro Gueretto
 * @date 21.06.2016
 * @function Listener für GUI Elemente werden implementiert
 *
 */

public class EventHandlingController {

	private boolean firstrun = true;
	private static final String PIC_DIR = "../";
	private FileChooser mediaChooser;
	private ExtensionFilter extFilter;
	private Stage stage;
	private File cover;
	private String defaultpath = "File:images/standard-cover.png";
	private String coverpath = "";
	private Controller controller;
	private Film film = null;

	@FXML
	private MenuItem btn_createfilm, btn_exitprogramm, btn_editfilm, btn_deletefilm;
	@FXML
	private MenuItem btn_createshow, btn_editshow, btn_deleteshow;
	@FXML
	private MenuItem btn_createroom, btn_editroom, btn_deleteroom;

	@FXML
	private GridPane pane_film, pane_main, pane_show;

	@FXML
	private Button btn_filmcreatesave;

	@FXML
	private Hyperlink btn_cancel;

	@FXML
	private TextField tf_filmtitle, tf_filmduration;

	@FXML
	private Label lbl_message, lbl_film;
	@FXML
	private TextArea ta_filmdesc;

	@FXML
	private ImageView iv_filmcover;



	public EventHandlingController() {
		mediaChooser = new FileChooser();
		extFilter = new ExtensionFilter("ImageFormat", "*.png", "*.jpg", "*.jpeg");
		controller = new Controller();
	}

	@FXML
	private void initialize() {

		if (firstrun) {
			pane_main.setVisible(true);
			pane_main.setDisable(false);
			pane_film.setVisible(false);
			pane_film.setDisable(true);
			// pane_show.setVisible(false);
			// pane_show.setDisable(true);
			firstrun = false;
		}
		// Film controlling start-----
		btn_createfilm.setOnAction((event) -> {
			pane_film.setVisible(true);
			pane_film.setDisable(false);
			// Init all inputfields
			iv_filmcover.setImage(new Image(defaultpath));
			tf_filmduration.setText("");
			tf_filmtitle.setText("");
			ta_filmdesc.setText("");
			lbl_film.setText("Create new film");
		});
		btn_exitprogramm.setOnAction((event) -> {
			System.exit(0);
		});
		btn_cancel.setOnAction((event) -> {
			backToMenu();
		});

		iv_filmcover.setOnMouseClicked((event) -> {
			mediaChooser.setTitle("choose Cover");
			mediaChooser.setInitialDirectory(new File(PIC_DIR));
			mediaChooser.getExtensionFilters().add(extFilter);
			// Liste mit ausgewählten Songs erstellen
			cover = mediaChooser.showOpenDialog(stage);
			if (cover != null) {
				coverpath = cover.toURI().toString();
				Image pic = new Image(coverpath);
				iv_filmcover.setImage(pic);

			}
		});

		btn_filmcreatesave.setOnAction((event) -> {
			if (lbl_film.getText().equals("Create new film")) {
				if (!tf_filmduration.getText().isEmpty() && !tf_filmtitle.getText().isEmpty() && !coverpath.isEmpty()
						&& !ta_filmdesc.getText().isEmpty()) {
					int returncode = controller.createNewFilm(tf_filmduration.getText(), tf_filmtitle.getText(),
							ta_filmdesc.getText(), coverpath);
					switch (returncode) {
					// Errorhandling
					case 0:
//						tf_filmduration.setText("");
//						tf_filmtitle.setText("");
//						ta_filmdesc.setText("");
						lbl_message.getStyleClass().add("msg_success");
						lbl_message.setText("Film successfully saved");
						removeMsg();
						backToMenu();
						break;
					case 1:
						lbl_message.getStyleClass().add("msg_error");
						lbl_message.setText("Duration is not a number!");
						removeMsg();
						break;
					case 2:
						lbl_message.getStyleClass().add("msg_error");
						lbl_message.setText("Could not load image. Invalid path or file!");
						removeMsg();
						break;
					case 3:
						lbl_message.getStyleClass().add("msg_error");
						lbl_message.setText("Film already exists!");
						removeMsg();
						break;
					}
				}
			} else if (lbl_film.getText().equals("Edit film")) {
				
				film.setDescription(ta_filmdesc.getText());
				film.setDurationInMinutes(Integer.parseInt(tf_filmduration.getText()));
				film.setTitle(tf_filmtitle.getText());
				film.setImagePath(coverpath);
				controller.editFilm(film);
				lbl_message.getStyleClass().add("msg_success");
				lbl_message.setText("Film successfully saved");
				removeMsg();
				backToMenu();
			}

		});

		btn_editfilm.setOnAction((event) -> {
			if (loadFilmList() != null) {
				film = choosePopupFilm(loadFilmList(), "Edit");
				pane_film.setVisible(true);
				pane_film.setDisable(false);
				tf_filmduration.setText(Integer.toString(film.getDurationInMinutes()));
				tf_filmtitle.setText(film.getTitle());
				ta_filmdesc.setText(film.getDescription());
				Image pic = new Image("File:" + film.getImagePath());
				iv_filmcover.setImage(pic);
				coverpath = film.getImagePath();
				lbl_film.setText("Edit film");
			} else {
				backToMenu();
			}

		});
		btn_deletefilm.setOnAction((event) -> {
			if (loadFilmList() != null) {
				choosePopupFilm(loadFilmList(), "Delete");
			} else {
				backToMenu();
			}

		});

		// Film controlling end------------

		// Room controlling start-------

		btn_createroom.setOnAction((event) -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Create new Room");
			dialog.setHeaderText("Create new Room");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String name = result.get();
				if (controller.createNewRoom(name)) {
					lbl_message.getStyleClass().add("msg_success");
					lbl_message.setText("Room successfully created");
					removeMsg();
					backToMenu();
				} else {
					lbl_message.getStyleClass().add("msg_error");
					lbl_message.setText("Room already exists!");
					removeMsg();
				}
			}
		});
		btn_editroom.setOnAction((event) -> {
			if (loadRoomList() != null) {

			} else {
				backToMenu();
			}
		});
		btn_deleteroom.setOnAction((event) -> {
			if (loadRoomList() != null) {
				deleteRoom(loadRoomList());
			} else {
				backToMenu();
			}
		});

	}

	// Room controlling end ------
	private FilmList loadFilmList() {
		FilmList filmlist = new FilmList();
		filmlist = controller.getAllFilms();
		if (filmlist.size() <= 0) {
			lbl_message.getStyleClass().add("msg_info");
			lbl_message.setText("No films are existing! Please create one!");
			removeMsg();
			return null;
		}
		return filmlist;
	}

	private RoomList loadRoomList() {
		RoomList roomlist = new RoomList();
		roomlist = controller.getAllRooms();
		if (roomlist.size() <= 0) {
			lbl_message.getStyleClass().add("msg_info");
			lbl_message.setText("No Rooms are existing! Please create one!");
			removeMsg();
			return null;
		}
		return roomlist;
	}

	// to-do 1 ## ArrayList richtig befüllen
	private Film choosePopupFilm(FilmList filmlist, String modus) {
		ArrayList<String> choices = new ArrayList<String>();
		for (Film current : filmlist) {
			choices.add(current.getTitle());
		}
		// choices.add("please choose");
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle(modus + " an existing film");
		dialog.setContentText("Choose a film:");
		dialog.setHeaderText(modus + " an existing film");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// get film by name
			for (Film current : filmlist) {
				if (result.get().equals(current.getTitle())) {
					return current;
				}
			}
		}
		return null;
	}

	// to-do 2 ## Arraylist richtig abfüllen (Name, id)
	private void deleteRoom(RoomList roomlist) {
		ArrayList choices = new ArrayList();
		// choices.add("please choose");
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle("Delete an existing room");
		dialog.setContentText("Choose a room:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// controller.deleteRoom();
			System.out.println("Your choice: " + result.get());

		}
	}

	// general methods
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub
		this.stage = stage;
	}

	private void deleteStyle(Label object, String name) {
		ObservableList<String> list = object.getStyleClass();
		try {
			for (String cssclass : list) {
				if (cssclass.matches(name)) {
					list.remove(cssclass);
				}
			}
		} catch (Exception e) {
			return;
		}

	}

	private void removeMsg() {
		// Label & Button nach bestimmter zeit not Visible
		ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
		s.schedule(() -> {
			Platform.runLater(() -> {
				lbl_message.setText("");
				deleteStyle(lbl_message, "msg_.*");
			});

		}, 5, TimeUnit.SECONDS);
	}

	private void backToMenu() {

		pane_main.setVisible(true);
		pane_main.setDisable(false);
		pane_film.setVisible(false);
		pane_film.setDisable(true);
		// pane_show.setVisible(false);
		// pane_show.setDisable(true);

	}

}
