package view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Film;
import model.Room;
import model.Show;

public class ShowItem extends BorderPane{

	// Data
	private Film film;
	private Room room;
	private int showid;

	// Layer 1
	// Visible at start
	private Image cover;
	private ImageView iv_cover;
	private String imagePath;
	private VBox showinfo;
	private Label lbl_start;
	// Layer 2
	private BorderPane pane;
	private Boolean clicked = false;

	// Layer 3
	private VBox dataholder;
	private Label lbl_title, lbl_duration, lbl_desc;

	public Pane createShowItem(Show show) {
		film = show.getFilm();
		room = show.getRoom();
		showid = show.getId();

		// Bild laden und event hinzufügen
		imagePath = "File:" + film.getImagePath();
		iv_cover = new ImageView();
		cover = new Image(imagePath);
		iv_cover.setImage(cover);
		iv_cover.setFitHeight(330);
		iv_cover.setFitWidth(220);
		iv_cover.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (clicked) {
					hide();
				} else {
					System.out.println("ShowID " + showid);
					setPrefWidth(440);
					setRight(dataholder);
					clicked = true;
				}
			}
		});
		iv_cover.getStyleClass().add("handcursor");
		//show daten verarbeiten
		Date showstart =show.getStartDateTime();
		Date showend = show.getEndDateTime();
		String starttime = LocalTimeToString(showstart);
		String endtime = LocalTimeToString(showend);
		String startdate = LocalDateToString(showstart);
		String enddate = LocalDateToString(showend);
		String showlaunch = startdate + " " + starttime + " - " + endtime;

		// show daten laden
		showinfo = new VBox();
		lbl_start = new Label(showlaunch);
		lbl_start.getStyleClass().add("showinfo");
		lbl_start.setAlignment(Pos.CENTER);
		lbl_start.setFont(Font.font("System", 15));
		lbl_start.setPrefHeight(35);
		lbl_start.setPadding(new Insets(0, 0, 0, 5));

		showinfo.getChildren().addAll(iv_cover, lbl_start);
		// Vorschau laden und mit daten abfüllen
		dataholder = new VBox();
		dataholder.setPrefWidth(220);
		dataholder.setPadding(new Insets(0, 0, 5, 10));
		lbl_title = new Label(film.getTitle());
		lbl_title.getStyleClass().add("showinfo");
		lbl_title.setFont(new Font("System", 25));
		lbl_title.setPrefHeight(35);

		lbl_desc = new Label(film.getDescription());
		lbl_desc.setWrapText(true);
		lbl_desc.setFont(new Font("System", 15));
		lbl_desc.getStyleClass().add("showinfo");
		lbl_desc.setPrefHeight(270);
		lbl_desc.setMaxHeight(270);
		lbl_desc.setAlignment(Pos.TOP_LEFT);

		lbl_duration = new Label(Integer.toString(film.getDurationInMinutes()) + " Minutes");
		lbl_duration.getStyleClass().add("showinfo");
		lbl_duration.setFont(Font.font("System", FontWeight.BOLD, 15));
		lbl_duration.setPrefHeight(35);

		dataholder.getChildren().addAll(lbl_title, lbl_desc, lbl_duration);
		pane = new BorderPane();
		this.setPrefHeight(330);
		this.setPrefWidth(220);
		this.setStyle(" -fx-background-color: rgb(72, 72, 72);");
		this.setLeft(showinfo);

		pane.setAlignment(showinfo, Pos.CENTER_LEFT);
		return this;
	}
	
	public void hide(){
		this.setPrefWidth(220);
		this.setRight(null);
		this.clicked = false;
	}
	private String LocalDateToString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(date);
	}
	private String LocalTimeToString(Date time){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(time);
	}
	public boolean isClicked(){
		return clicked;
	}
	public void setClicked(boolean bool){
		this.clicked = bool;
	}
	public int getShowId(){
		return showid;
	}
}
