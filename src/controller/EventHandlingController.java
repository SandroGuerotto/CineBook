package controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Film;
import model.FilmList;
import model.Room;
import model.RoomList;
import model.Show;
import model.ShowList;
import view.EditRoomDialog;
import view.Message;
import view.ShowItem;

/**
 * 
 * @author Tim Meier & Sandro Gueretto
 * @date 21.06.2016
 * @function Listener für GUI Elemente werden implementiert
 *
 */

public class EventHandlingController {
	private static final String TIMEREGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]"; // regex
																				// für
																				// Zeit
	private static final String NUMBERREGEX = "[0-9]{4}"; // regex für Zahlen
	private static final Pattern TIME24HOURS_PATTERN = Pattern.compile(TIMEREGEX);
	private static final Pattern NUMBERPATTERN = Pattern.compile(NUMBERREGEX);
	private static final String PIC_DIR = "../";
	private boolean firstrun = true;
	private FileChooser mediaChooser;
	private ExtensionFilter extFilter;
	private Stage stage;
	private File cover;
	private String defaultpath = "File:images/standard-cover.png";
	private String coverpath = "";
	private Controller controller;
	private Film film = null;
	private Room room = null;
	private String returncode;
	private Message message;

	@FXML
	private MenuItem btn_createfilm, btn_exitprogramm, btn_editfilm, btn_deletefilm;
	@FXML
	private MenuItem btn_createshow, btn_editshow, btn_deleteshow;
	@FXML
	private MenuItem btn_createroom, btn_editroom, btn_deleteroom;

	@FXML
	private GridPane pane_film, pane_main, pane_show;

	@FXML
	private VBox pane_overview;
	@FXML
	private Button btn_filmsave, btn_showsave;

	@FXML
	private Hyperlink btn_cancel, btn_cancelshow;

	@FXML
	private TextField tf_filmtitle, tf_filmduration, tf_starttime;

	@FXML
	private Label lbl_message, lbl_film, lbl_show;
	@FXML
	private Label lbl_filmtitle, lbl_filmduration;
	@FXML
	private TextArea ta_filmdesc;

	@FXML
	private ImageView iv_filmcover, iv_filmcovershow;
	@FXML
	private ListView<String> lv_room;

	@FXML
	private ListView<String> lv_film;
	@FXML
	private DatePicker dp_startdate;

	@FXML
	private ImageView iv_test;
	@FXML
	private ScrollPane sp_show;
	@FXML
	private HBox vb_wrapper_show;

	public EventHandlingController() {
		mediaChooser = new FileChooser();
		extFilter = new ExtensionFilter("ImageFormat", "*.png", "*.jpg", "*.jpeg");
		controller = new Controller();

	}

	@FXML
	private void initialize() {

		if (firstrun) {
			message = new Message(lbl_message);
			lv_film.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			lv_room.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			backToMenu(true);
			firstrun = false;
			
		}
		// Film controlling start-----
		btn_createfilm.setOnAction((event) -> {
			backToMenu(false);
			pane_film.setVisible(true);
			pane_film.setDisable(false);
			// Init all inputfields
			iv_filmcover.setImage(new Image(defaultpath));
			tf_filmduration.setText("");
			tf_filmtitle.setText("");
			ta_filmdesc.setText("");
			btn_cancel.setUnderline(false);
			lbl_film.setText("Create new film");
		});
		btn_exitprogramm.setOnAction((event) -> {
			System.exit(0);
		});
		btn_cancel.setOnAction((event) -> {
			backToMenu(true);
		});
		btn_cancelshow.setOnAction((event) -> {
			lv_film.getSelectionModel().clearSelection();
			lv_room.getSelectionModel().clearSelection();
			backToMenu(true);
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

		btn_filmsave.setOnAction((event) -> {
			// check the label in what function is needed
			if (!tf_filmduration.getText().isEmpty() && !tf_filmtitle.getText().isEmpty() && !coverpath.isEmpty()
					&& !ta_filmdesc.getText().isEmpty()) {
				if (lbl_film.getText().equals("Create new film")) {

					returncode = controller.createNewFilm(tf_filmduration.getText(), tf_filmtitle.getText(),
							ta_filmdesc.getText(), coverpath);

				} else if (lbl_film.getText().equals("Edit film")) {
					Film editedFilm = new Film();
					editedFilm.setDescription(ta_filmdesc.getText());
					// Überprüft ob Filmlänge eine Zahl ist
					try {
						editedFilm.setId(film.getId());
						editedFilm.setDurationInMinutes(Integer.parseInt(tf_filmduration.getText()));
						editedFilm.setTitle(tf_filmtitle.getText());
						editedFilm.setImagePath(coverpath);
						returncode = controller.editFilm(editedFilm);

					} catch (Exception e) {
						returncode = "e5";
					}

				}
				if (message.showMsg(returncode)) {
					backToMenu(true);
				}
			} else {
				message.showMsg("i9");
			}

		});

		btn_editfilm.setOnAction((event) -> {
			if (loadFilmList() != null) {
				film = choosePopupFilm(loadFilmList(), "Edit");
				if (film != null) {
					backToMenu(false);
					pane_film.setVisible(true);
					pane_film.setDisable(false);
					tf_filmduration.setText(Integer.toString(film.getDurationInMinutes()));
					tf_filmtitle.setText(film.getTitle());
					ta_filmdesc.setText(film.getDescription());
					Image pic = new Image("File:" + film.getImagePath());
					iv_filmcover.setImage(pic);
					coverpath = film.getImagePath();
					lbl_film.setText("Edit film");
				}
			} else {
				message.showMsg("i17");
				backToMenu(true);
			}

		});
		btn_deletefilm.setOnAction((event) -> {
			if (loadFilmList() != null) {
				film = choosePopupFilm(loadFilmList(), "Delete");
				if (film != null) {
					returncode = controller.deleteFilm(film);
					message.showMsg(returncode);
				}
			} else {
				message.showMsg("i17");
				backToMenu(true);
			}

		});

		// Film controlling end------------

		// Room controlling start--------------------

		btn_createroom.setOnAction((event) -> {
			backToMenu(false);
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Create new Room");
			dialog.setHeaderText("Create new Room");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String name = result.get();
				if (controller.createNewRoom(name)) {
					message.showMsg("s10");
					backToMenu(true);
				} else {
					message.showMsg("e11");
				}
			}
		});
		btn_editroom.setOnAction((event) -> {
			if (loadRoomList() != null) {
				EditRoomDialog dialog = new EditRoomDialog();
				Pair<String, String> eingabe = dialog.show(loadRoomList());
				if (eingabe != null) {
					Room editedRoom = new Room(eingabe.getValue().toString());
					controller.editRoom(eingabe.getKey().toString(), editedRoom);
					message.showMsg("s12");
				}

			} else {
				message.showMsg("i16");
			}
			backToMenu(true);

		});
		btn_deleteroom.setOnAction((event) -> {
			if (loadRoomList() != null) {
				Room delroom = deleteRoom(loadRoomList());
				if (delroom != null) {
					returncode = controller.deleteRoom(delroom);
					message.showMsg(returncode);
				}
			} else {
				message.showMsg("i16");
				backToMenu(true);
			}
		});
		// Room controlling end ------
		// Show controlling start -------

		btn_createshow.setOnAction((event) -> {
			backToMenu(false);
			pane_show.setVisible(true);
			pane_show.setDisable(false);
			// Init all inputfields
			tf_starttime.setText("");
			lbl_filmduration.setText("");
			lbl_filmtitle.setText("");
			iv_filmcovershow.setImage(null);
			lv_film.setItems(loadLVFilm());
			lv_room.setItems(loadLVRoom());
			lbl_show.setText("Create new Show");
			btn_cancelshow.setUnderline(false);
			
			Thread checkinput = new Thread(){
				
			};
			
		});

		btn_showsave.setOnAction((event) -> {
			String filmname = lv_film.getSelectionModel().getSelectedItem();
			String roomname = lv_room.getSelectionModel().getSelectedItem();
			LocalDate startDate = dp_startdate.getValue();
			String startTime = tf_starttime.getText();
			if (filmname != null && roomname != null && startDate != null && !startTime.isEmpty()) {
				returncode = controller.createNewShow(controller.getRoomByName(roomname),
						controller.getFilmByName(filmname), startDate, startTime);
				System.out.println(returncode);
				if (message.showMsg(returncode))
					backToMenu(true);
			}else{
				message.showMsg("i9");
			}
			// controller.edit
		});

		// Eventhandling for editing shows
		btn_editshow.setOnAction((event) -> {
			// popup choose show -> same as film
			// load data to screen
		});

		
		
		btn_deleteshow.setOnAction((event) ->{
			if (loadShowList() != null) {
				Show delshow = deleteShow(loadShowList());
				if (delshow != null) {
					returncode = controller.deleteShowAndReservations(delshow);
					message.showMsg(returncode);
					backToMenu(true);
				}
			} else {
				message.showMsg("i16");
				backToMenu(true);
			}
		});
		
		
		// check if entered value is valid to time 24 hours. otherwise reset
		// field
		tf_starttime.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					Matcher timeMatcher = TIME24HOURS_PATTERN.matcher(tf_starttime.getText());
					Matcher numMatcher = NUMBERPATTERN.matcher(tf_starttime.getText());
					if (numMatcher.matches()) {
						tf_starttime.setText(
								tf_starttime.getText().substring(0, 2) + ":" + tf_starttime.getText().substring(2, 4));
						System.out.println(tf_starttime.getText());
					} else {
						if (!timeMatcher.matches()) {
							tf_starttime.setText("");
						}
					}
					System.out.println(tf_starttime.getText());

				}
			}
		});

		// Show controlling end -------------

		// ListView controlling start ----------------
		lv_film.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					film = controller.getFilmByName(newValue);
					lbl_filmduration.setText(Integer.toString(film.getDurationInMinutes()) + " min");
					lbl_filmtitle.setText(film.getTitle());
					iv_filmcovershow.setImage(new Image("File:" + film.getImagePath()));

				}
			}
		});
		lv_room.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					room = controller.getRoomByName(newValue);
				}
			}
		});

	}

	private void loadShowsToOverview() {
		

		if (loadShowList() != null) {
			vb_wrapper_show.getChildren().clear();
			ShowList showlist = loadShowList();
			for (Show show : showlist) {
				ShowItem showitem = new ShowItem();
				// showitem.createShowItem(film);
				Pane pane = showitem.createShowItem(show);
				vb_wrapper_show.getChildren().add(pane);
				vb_wrapper_show.setMargin(pane, new Insets(0, 0, 0, 20));

			}
			sp_show.setContent(vb_wrapper_show);
		}

	}

	private ObservableList<String> loadLVFilm() {
		FilmList filmlist = controller.getAllFilms();
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Film film : filmlist) {
			content.add(film.getTitle());
		}

		return content;
	}
	//TMP ----------------------------------------------------------------------------------------------------------
	private ObservableList<String> loadLVRoom() {
		RoomList roomlist = controller.getAllRooms();
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Room room : roomlist) {
			content.add(room.getName());
		}

		return content;
	}
	//--------------------------------------------------------------------------------------------------------------
	
	// Mehtode to load lv_room with all available rooms.
	private ObservableList<String> loadLVRoom(LocalDate startDate, String startTime, Film film) {
		RoomList roomlist = controller.getAllAvailableRooms(startDate, startTime, film);
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Room room : roomlist) {
			content.add(room.getName());
		}

		return content;
	}

	// Listen holen und zurückgeben
	private FilmList loadFilmList() {
		FilmList filmlist = new FilmList();
		filmlist = controller.getAllFilms();
		if (filmlist.size() <= 0) {
			return null;
		}
		return filmlist;
	}

	private RoomList loadRoomList() {
		RoomList roomlist = new RoomList();
		roomlist = controller.getAllRooms();
		if (roomlist.size() <= 0) {
			return null;
		}
		return roomlist;
	}

	private ShowList loadShowList() {
		ShowList showlist = new ShowList();
		showlist = controller.getAllShows();
		if (showlist.size() <= 0) {
			return null;
		}
		return showlist;
	}

	// Popoup zur Auswahl von Film und Raum
	private Film choosePopupFilm(FilmList filmlist, String modus) {
		ArrayList<String> choices = new ArrayList<String>();
		for (Film current : filmlist) {
			choices.add(current.getTitle());
		}
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

	private Room deleteRoom(RoomList roomlist) {
		ArrayList<String> choices = new ArrayList<String>();
		for (Room current : roomlist) {
			choices.add(current.getName());
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle("Delete an existing room");
		dialog.setContentText("Choose a room:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (Room current : roomlist) {
				if (result.get().equals(current.getName())) {
					return current;
				}
			}
		}
		return null;
	}
	private Show deleteShow(ShowList showlist) {
		ArrayList<String> choices = new ArrayList<String>();
		for (Show current : showlist) {
			choices.add(current.getFilm().getTitle() + " - " + current.getStartDateTime());
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle("Delete an existing show");
		dialog.setContentText("Choose a show:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (Show current : showlist) {
				if (result.get().equals(current.getFilm().getTitle() + " - " + current.getStartDateTime())) {
					return current;
				}
			}
		}
		return null;
	}

	// general methods
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void backToMenu(Boolean hide) {
		
		pane_main.setVisible(true);
		pane_main.setDisable(false);
		pane_film.setVisible(false);
		pane_film.setDisable(true);
		pane_show.setVisible(false);
		pane_show.setDisable(true);
		pane_overview.setVisible(false);
		pane_overview.setDisable(true);

		if (hide) {
			pane_overview.setVisible(true);
			pane_overview.setDisable(false);
			loadShowsToOverview();
		}

	}
}
