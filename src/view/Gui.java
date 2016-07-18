package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import controller.EventHandlingController;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Die Startklasse für das GUI
 */

public class Gui extends Application {

	@Override
	public void start(Stage stage) {
		// Init stage position and size
		final double ypos = Screen.getPrimary().getVisualBounds().getMinY();
		final double xpos = Screen.getPrimary().getVisualBounds().getMinX();
		final double width = Screen.getPrimary().getVisualBounds().getWidth();
		final double height = Screen.getPrimary().getVisualBounds().getHeight();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Gui.fxml"));
			Parent root = (Parent) loader.load();

			// used for dialog popup
			EventHandlingController eventHandlingController = (EventHandlingController) loader.getController();
			eventHandlingController.setStage(stage);  
			
			Scene scene = new Scene(root);
			// import css file
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			stage.setTitle("Cinema Booking System"); // Titel
			stage.setScene(scene);
			stage.getIcons().add(new Image("File:" + "images/tab-icon.png"));
			
			// Set position and size
			stage.setX(xpos);
			stage.setY(ypos);
			stage.setWidth(width);
			stage.setHeight(height);
			stage.setMaximized(true);;
			stage.setResizable(true);
			
			//exit with "X" from windows ! 
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {
			        System.exit(0);
			    }
			});
			
			stage.show();
			root.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showScreen() {
		launch();
	}
}
