package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.glass.events.MouseEvent;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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
import model.Reservation;
import model.Room;
import model.RoomList;
import model.Show;
import model.ShowList;
import view.EditRoomDialog;
import view.Message;
import view.Seat;
import view.ShowItem;

/**
 * 
 * @author Tim Meier & Sandro Gueretto
 * @date 21.06.2016
 * @function Listener für GUI Elemente werden implementiert
 *
 */

public class EventHandlingController {
	private static final String PHONE = "[0][0-9]{2} [0-9]{3} [0-9]{2}( [0-9]{2})";
	private static final String TIMEREGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	private static final String NUMBERREGEX = "[0-9]{4}"; // regex für Zahlen
	private static final Pattern TIME24HOURS_PATTERN = Pattern.compile(TIMEREGEX);
	private static final Pattern NUMBERPATTERN = Pattern.compile(NUMBERREGEX);
	private static final Pattern PHONEPATTERN = Pattern.compile(PHONE);
	private static final String PIC_DIR = "../";
	// private final ScheduledExecutorService schedulerLoader =
	// Executors.newScheduledThreadPool(1);

	// private ScheduledFuture<?> showLoader;
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
	private Show show = null;
	private String returncode;
	private Message message;

	@FXML
	private MenuItem btn_createfilm, btn_exitprogramm, btn_editfilm, btn_deletefilm;
	@FXML
	private MenuItem btn_createshow, btn_editshow, btn_deleteshow;
	@FXML
	private MenuItem btn_createroom, btn_editroom, btn_deleteroom;
	@FXML
	private MenuItem btn_helpme;

	@FXML
	private GridPane pane_film, pane_main, pane_show, pane_seats;
	@FXML
	private ScrollPane sp_show;
	@FXML
	private HBox vb_wrapper_show;
	@FXML
	private VBox pane_overview;
	@FXML
	private BorderPane pane_seatsarr;
	@FXML
	private TabPane pane_help;

	@FXML
	private Button btn_filmsave, btn_showsave, btn_reservationsave;
	@FXML
	private Hyperlink btn_cancel, btn_cancelshow, btn_cancelNewRes;
	@FXML
	private DatePicker dp_startdate;

	@FXML
	private TextField tf_filmtitle, tf_filmduration, tf_starttime, tf_phonenumber;
	@FXML
	private TextArea ta_filmdesc;

	@FXML
	private Label lbl_message, lbl_film, lbl_show;
	@FXML
	private Label lbl_filmtitle, lbl_filmduration;

	@FXML
	private ImageView iv_filmcover, iv_filmcovershow;
	@FXML
	private ListView<String> lv_room;
	@FXML
	private ListView<String> lv_film;
	@FXML
	private ListView<Pane> lv_shows;
	@FXML
	private ListView<String> lv_reservation;

	private int showclicked;
	int oldclick = -1, newclick = -1;

	public EventHandlingController() {
		mediaChooser = new FileChooser();
		extFilter = new ExtensionFilter("ImageFormat", "*.png", "*.jpg", "*.jpeg");
		controller = new Controller();
		// new Thread(new Runnable() {
		// public void run() {
		// while (true) {
		// Platform.runLater(() -> {
		// loadShowsToOverview();
		//
		// });
		// }
		// }
		// }).start();
	}

	@FXML
	private void initialize() {

		if (firstrun) {
			message = new Message(lbl_message);
			lv_film.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			lv_room.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			lv_shows.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			
			backToMenu(true);
			// createShowLoader();
			firstrun = false;

			loadShowToOverview();
			loadSeatPane();
//			  Runnable r = new Runnable() {
//			         public void run() {
//			             loadSeatPane();
//			         }
//			     };
//
//			     ExecutorService executor = Executors.newCachedThreadPool();
//			     executor.submit(r);
		}
		btn_helpme.setOnAction((event) -> {
			backToMenu(false);
			pane_help.setVisible(true);
			pane_help.setDisable(false);
		});
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
			// showLoader.cancel(true);
			System.exit(0);
		});
		btn_cancel.setOnAction((event) -> {
			backToMenu(true);
		});
		btn_cancelNewRes.setOnAction((event) -> {
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
			backToMenu(true);
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
				if(eingabe != null){
					if (!eingabe.getValue().isEmpty() && !eingabe.getKey().isEmpty() ) {
						Room editedRoom = new Room(eingabe.getValue().toString());
						controller.editRoom(eingabe.getKey().toString(), editedRoom);
						message.showMsg("s12");
					}
				}
			} else {
				message.showMsg("i16");
			}
			backToMenu(true);

		});
		btn_deleteroom.setOnAction((event) -> {
			if (loadRoomList() != null) {
				Room delroom = choosePopupRoom(loadRoomList());
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
			dp_startdate.setValue(null);
			lbl_filmduration.setText("");
			lbl_filmtitle.setText("");
			// set Cover to null
			iv_filmcovershow.setImage(null);
			// film list handling
			lv_film.setItems(loadLVFilm());
			lv_film.getSelectionModel().clearSelection();
			// room list handling -> default empty and locked
			lv_room.setItems(null);
			lv_room.setDisable(true);
			lv_room.getSelectionModel().clearSelection();

			lbl_show.setText("Create new show");
			btn_cancelshow.setUnderline(false);
		});

		btn_showsave.setOnAction((event) -> {
		      String filmname = lv_film.getSelectionModel().getSelectedItem();
		      String roomname = lv_room.getSelectionModel().getSelectedItem();
		      LocalDate startDate = dp_startdate.getValue();
		      String startTime = tf_starttime.getText();

		      if (filmname != null && roomname != null && startDate != null && !startTime.isEmpty() &&  checkStartTime() && checkStartDate()) {
		        if (lbl_show.getText().equals("Create new show")) {
		          returncode = controller.createNewShow(controller.getRoomByName(roomname),
		              controller.getFilmByName(filmname), startDate, startTime);
		          if (message.showMsg(returncode))
		            backToMenu(true);
		          
		        } else if (lbl_show.getText().equals("Edit show")) {
		          film = controller.getFilmByName(lv_film.getSelectionModel().getSelectedItem());
		          Show editedShow = new Show();
		          Date date = null;
		          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		          String startDateTime = dp_startdate.getValue() + " " + tf_starttime.getText();
		          try { date = format.parse(startDateTime); } catch (ParseException e) {}
		          
		          try{
		            editedShow.setId(show.getId());
		            editedShow.setDurationInMinutes(film.getDurationInMinutes());
		            editedShow.setFilm(film);
		            editedShow.setRoom(controller.getRoomByName(lv_room.getSelectionModel().getSelectedItem()));
		            editedShow.setStartDateTime(date);
		            editedShow.setEndDateTime(controller.showList.getEndTime(date, film));
		            returncode = controller.editShow(editedShow);
		          }catch(Exception e){
		            returncode = "e30";
		          }


		          if (message.showMsg(returncode))
		            backToMenu(true);
		        }

		      } else {
		        if (!checkStartDate()) {
		          message.showMsg("e28");
		          return;
		        }else if (!checkStartTime()) {
		          message.showMsg("e29");
		          return;
		        }
		        message.showMsg("i9");
		      }

		    });

		// Eventhandling for editing shows
	    btn_editshow.setOnAction((event) -> {
	      if (loadShowList() != null) {
	        show = choosePopupShow(loadShowList(), "Edit");
	        if (show != null) {
	          loadEditShow(show);
	        }
	      } else {
	        message.showMsg("i27");
	        backToMenu(true);
	      }
	    });

		btn_deleteshow.setOnAction((event) -> {
			if (loadShowList() != null) {
				Show delshow = choosePopupShow(loadShowList(), "Delete");
				if (delshow != null) {
					returncode = controller.deleteShowAndReservations(delshow);
					message.showMsg(returncode);
					backToMenu(true);
				}
			} else {
				message.showMsg("i27");
				backToMenu(true);
			}
		});

		// check if entered value is valid to time 24 hours. otherwise reset
		// field
		tf_starttime.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					checkTimeFormat();
				}
			}
		});

		dp_startdate.setOnAction((event) -> {
			checkStartDate();
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

		lv_shows.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				ShowItem item = null;
				if(lv_shows.getSelectionModel().getSelectedItem() == null){
					backToMenu(true);
				}
				// alte selektion löschen -> verstecken
				if (oldValue != null) {
					item = (ShowItem) oldValue;
					item.hide();
					item.setClicked(false);
				}
				item = (ShowItem) newValue;
				if (item != null) {
					item.setClicked(true);
					item.show();
					loadReservationToPane(item.getShowId());
					//loadSeatPane(item.getShowId());
					loadReservation(item.getShowId());
				}
			}
		});

		// reservation controlling start -----------------

		tf_phonenumber.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					checkPhoneFormat(tf_phonenumber.getText());
				}
			}
		});

		// reservation controlling end ---------

	}

	@FXML
	private void deselectShow() {
		ShowItem item = (ShowItem) lv_shows.getSelectionModel().getSelectedItem();
		if (!item.isClicked()) {
			backToMenu(true);
		}

	}

	private void loadShowToOverview() {
		if (loadShowList() != null) {
			ShowList showlist = loadShowList();
			ObservableList<Pane> content = FXCollections.observableArrayList();
			for (Show show : showlist) {
				if (!show.getStartDateTime().before(new Date())) {
					ShowItem showitem = new ShowItem();
					Pane pane = showitem.createShowItem(show);
					content.add(pane);
				}
			}
			lv_shows.setItems(content);
		}
	}

	
	@FXML
	private void refreshShows() {
		loadShowToOverview();
		backToMenu(true);
	}
	
	private void loadEditShow(Show editshow) {
		backToMenu(false);
		pane_show.setVisible(true);
		pane_show.setDisable(false);
		// Init all inputfields
		Film film = editshow.getFilm();

		// date formater
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String starttime = formatter.format(editshow.getStartDateTime());

		// convert date to LocalDate for datePicker
		LocalDate startdate = editshow.getStartDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		tf_starttime.setText(starttime);
		dp_startdate.setValue(startdate);
		lbl_filmduration.setText(Integer.toString(film.getDurationInMinutes()));
		lbl_filmtitle.setText(film.getTitle());

		// set Cover to null
		iv_filmcovershow.setImage(new Image("File:" + film.getImagePath()));

		// film list handling
		lv_film.setItems(loadLVFilm());
		lv_film.getSelectionModel().select(film.getTitle());

		// room list handling -> load as default and add show room -> sort
		lv_room.setItems(loadLVRoom(startdate, starttime, film));
		lv_room.getItems().add(editshow.getRoom().getName());
		lv_room.getItems().sort(((o1, o2) -> editshow.getRoom().getName().compareTo(editshow.getRoom().getName())));
		lv_room.getSelectionModel().select(editshow.getRoom().getName());
		lv_room.setDisable(false);

		lbl_show.setText("Edit show");
		btn_cancelshow.setUnderline(false);
	}
	
	
	private void loadReservation(int shownr){
		ArrayList<Reservation> reservationList= controller.getReservationsByShow(controller.getShowById(shownr));
		ObservableList<String> content = FXCollections.observableArrayList();
		for(Reservation reservation : reservationList){
			content.add(reservation.getSeatNumber() + "\t\t" + reservation.getPhoneNumber());
		}
		lv_reservation.setItems(content);
		
	}
	private void loadSeatPane() {

		Seat seatobj;
		int seatnr = 0;
		for (int row = 1; row < 16; row++) {
			seatnr = 0;
			for (int seat = 0; seat < 31; seat++) {
				if (seat == 0) {
					pane_seats.add(new Label(Integer.toString(row)), seat, row);
					continue;
				}
				seatnr++;
				seatobj = new Seat(row, seatnr);

				// walkway
				if (seat == 5 || seat == 26) {
					seatnr--;
					continue;
				}
				pane_seats.add(seatobj, seat + 1, row);
			}
		}
	}

	private void loadReservationToPane(int shownr){
		btn_cancelshow.setUnderline(false);
		pane_seatsarr.setVisible(true);
		pane_seatsarr.setDisable(false);
		tf_phonenumber.setEditable(false);
		tf_phonenumber.setDisable(true);
		tf_phonenumber.setVisible(false);
		tf_phonenumber.setText("");
		ObservableList<Node> children = pane_seats.getChildren();
		ArrayList<String> reservedSeats = controller.getReservedSeats(controller.getShowById(shownr));
		for(int i = 0 ; i < children.size(); i++){
			try {
				Seat seat = (Seat) children.get(i);
				seat.enable();
				seat.setCursor(Cursor.HAND);
				// alle sitze mit einer reservierung ausschalten
				for (String reservation : reservedSeats) {
					String[] part = reservation.split("-");
					if (part[0].equals(Integer.toString(seat.getRow())) && part[1].equals(Integer.toString(seat.getSeat()))) {
						seat.setCursor(Cursor.DEFAULT);
						seat.disable();
						break;
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	// laden der Raumliste
	@FXML
	private void checkAndLoadRooms() {
		Matcher timeMatcher = TIME24HOURS_PATTERN.matcher(tf_starttime.getText());
		if (dp_startdate.getValue() != null && timeMatcher.matches()
				&& lv_film.getSelectionModel().getSelectedItem() != null) {
			lv_room.setItems(loadLVRoom(dp_startdate.getValue(), tf_starttime.getText(),
					controller.getFilmByName(lv_film.getSelectionModel().getSelectedItem())));
			if (lv_room.getItems().size() == 0) {
				lv_room.setDisable(true);
				message.showMsg("i16");
			} else {
				lv_room.setDisable(false);
			}

		} else {
			lv_room.setItems(null);
			lv_room.setDisable(true);
			lv_room.getSelectionModel().clearSelection();
		}

	}


	// Mehtode to load lv_room with all available rooms.
	private ObservableList<String> loadLVRoom(LocalDate startDate, String startTime, Film film) {
		RoomList roomlist = controller.getAllAvailableRooms(startDate, startTime, film);
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Room room : roomlist) {
			content.add(room.getName());
		}		
		return content;
	}
	private ObservableList<String> loadLVFilm() {
		FilmList filmlist = controller.getAllFilms();
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Film film : filmlist) {
			content.add(film.getTitle());
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

	private Room choosePopupRoom(RoomList roomlist) {
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

	private Show choosePopupShow(ShowList showlist, String modus) {
		ArrayList<String> choices = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		
		for (Show current : showlist) {
			String starttime = format.format(current.getStartDateTime());
			choices.add(current.getFilm().getTitle() + " - " + starttime);
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle(modus + " an existing show");
		dialog.setContentText("Choose a show:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (Show current : showlist) {
				if (result.get().equals(current.getFilm().getTitle() + " - " + format.format(current.getStartDateTime()))) {
					return current;
				}
			}
		}
		return null;
	}
// All check methode for validating input
	private boolean checkStartDate() {
		if (dp_startdate.getValue() != null) {
			if (dp_startdate.getValue().isBefore(LocalDate.now())) {
				message.showMsg("e28");
				return false;
			}
			checkAndLoadRooms();
			return true;
		}
		return false;
	}

	private boolean checkStartTime() {
	     if (!tf_starttime.getText().isEmpty()) {
	      SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	      Date tmpDate = null;
	      String now = "";
	      if(dp_startdate.getValue() == null){
	        now = LocalDateToString(new Date()) + " " + tf_starttime.getText();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	        LocalDate date = LocalDate.parse(now, formatter);
	        dp_startdate.setValue(date);
	        return true;

	      }else if(dp_startdate.getValue() != null){
	        try {
	          tmpDate = format.parse(dp_startdate.getValue().toString() + " " + tf_starttime.getText());
	        } catch (ParseException e) {
	          return true;
	        }
	      }
	        
	      if (tmpDate.before(new Date())) {
	        message.showMsg("e29");
	        return false;
	      }
	      return true;
	    }
	    return false;
	  }
	private void checkPhoneFormat(String number) {
		String formatedphone = "";
		Pattern sevennum = Pattern.compile("[0-9]{10}");
		Matcher numMatcher = sevennum.matcher(number);
		if (numMatcher.matches()) {
			formatedphone = number.substring(0, 3) + " " + number.substring(3, 6) + " " + number.substring(6, 8) + " "
					+ number.substring(8, 10);
		}
		Matcher phonematcher = PHONEPATTERN.matcher(formatedphone);
		if (phonematcher.matches()) {
			tf_phonenumber.setText(formatedphone);
		} else {
			tf_phonenumber.setText("");
		}
	}

	public void checkTimeFormat() {
		String time = "";
		Matcher numMatcher = NUMBERPATTERN.matcher(tf_starttime.getText());
		if (numMatcher.matches()) {
			time = tf_starttime.getText().substring(0, 2) + ":" + tf_starttime.getText().substring(2, 4);
			tf_starttime.setText(time);
		}
		Matcher timeMatcher = TIME24HOURS_PATTERN.matcher(tf_starttime.getText());
		if (timeMatcher.matches()) {
			if (checkStartTime()) {
				checkAndLoadRooms();
				return;
			}
		} else {
			tf_starttime.setText("");
		}
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
		pane_seatsarr.setVisible(false);
		pane_seatsarr.setDisable(true);
		pane_help.setVisible(false);
		pane_help.setDisable(true);

		if (hide) {
			pane_overview.setVisible(true);
			pane_overview.setDisable(false);
			loadShowToOverview();
		}

	}

	private String LocalDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(date);
	}
	// private void createShowLoader() {
	// Runnable run_showLoader = new Runnable() {
	// public void run() {
	// loadShowsToOverview();
	// // System.out.println("hier");
	// }
	// };
	// showLoader = schedulerLoader.scheduleAtFixedRate(run_showLoader, 1, 1,
	// TimeUnit.SECONDS);
	// }




	@FXML
	private void saveReservation() {
		ObservableList<Node> seatList = pane_seats.getChildren();
		ArrayList<String> reservationList = new ArrayList<String>();
		reservationList.clear();
		ShowItem item = (ShowItem) lv_shows.getSelectionModel().getSelectedItem();
		Seat seat;
		Show show = controller.getShowById(item.getShowId());
		for (int i = 0; i < seatList.size(); i++) {
			try {
				seat = (Seat) seatList.get(i);
			} catch (Exception e) {
				continue;
			}
			if (seat.isSelected()) {
				reservationList.add(seat.getRow() + "-" + seat.getSeat());
			}
		}
		if(reservationList.size() == 0){
			message.showMsg("i32");
			return;
		}
		if (reservationList.size() != 0 && tf_phonenumber.getText().isEmpty()) {
			message.showMsg("i30");
			tf_phonenumber.setEditable(true);
			tf_phonenumber.setDisable(false);
			tf_phonenumber.setVisible(true);
			tf_phonenumber.requestFocus();
			return;
		} else if (reservationList.size() != 0 && !tf_phonenumber.getText().isEmpty()) {
			if (controller.createNewReservations(show, reservationList, tf_phonenumber.getText())) {
				message.showMsg("s31");
				loadReservation(item.getShowId());
				loadReservationToPane(item.getShowId());
			}
		}

	}
}
