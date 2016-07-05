package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Seat extends ToggleButton {

	Image seatIconEmpty = new Image(getClass().getResourceAsStream("seat2.png"), 25, 25, false, false);
	Image seatIconDisabled = new Image(getClass().getResourceAsStream("seat3.png"), 25, 25, false, false);
	Image seatIconClicked = new Image(getClass().getResourceAsStream("seat4.png"), 25, 25, false, false);
	
	public Seat(int row, int seat){	

        setSelected(false);
    	setGraphic(new ImageView(seatIconEmpty));
    	setPadding(Insets.EMPTY);
    	setStyle("-fx-background-color: transparent;");
	
    	// Bild wechseln wenn Sitz angeklickt wird
    	setOnAction(e -> {
    		if(isSelected() == false){
    			Platform.runLater(() -> { setGraphic(new ImageView(seatIconEmpty)); });
    		}else{
    			Platform.runLater(() -> { setGraphic(new ImageView(seatIconClicked)); });
    		}
    	});
    	
    	// Cursor wechseln
    	setOnMouseEntered(o-> {
    	        setCursor(Cursor.HAND);
		setTooltip(new Tooltip(getId()));
    	});
    	
	}
	
	public void disable(){
		Platform.runLater(() -> {
			setDisable(true);
			setGraphic(new ImageView(seatIconDisabled));
			setStyle("-fx-opacity: 1.0;");
			setSelected(false);
			});
	}
	
	public void enable(){
		Platform.runLater(() -> {
			setDisable(false);
			setGraphic(new ImageView(seatIconEmpty)); 
			});
	}


	
	
	
}
