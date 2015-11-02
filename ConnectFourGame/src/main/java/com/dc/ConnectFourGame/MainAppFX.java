package com.dc.ConnectFourGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.ConnectFourGame.controllers.BoardController;

import java.io.IOException;

/**
 * Basic class for starting a JavaFX application
 *
 * #KFCStandard and JavaFX8
 *
 * @author Ken Fogel
 */
public class MainAppFX extends Application {

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	// The primary window or frame of this application
	private Stage primaryStage;

	/**
	 * Constructor
	 */
	public MainAppFX() {
		super();
	}

	/**
	 * The application starts here
	 *
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		log.info("Program Begins");

		// The Stage comes from the framework so make a copy to use elsewhere
		this.primaryStage = primaryStage;
		// Create the Scene and put it on the Stage
		configureMainStage();

		// Set the window title
		primaryStage.setTitle("Board");

		// Raise the curtain on the Stage
		primaryStage.show();

	}

	/**
	 * Load the FXML and bundle, create a Scene and put the Scene on Stage.
	 *
	 * Using this approach allows you to use loader.getController() to get a
	 * reference to the fxml's controller should you need to pass data to it.
	 * Not used in this archetype.
	 */
	  private void configureMainStage() {
			try {
				// Instantiate the FXMLLoader
				FXMLLoader loader = new FXMLLoader();

				// Set the location of the fxml file in the FXMLLoader
				loader.setLocation(MainAppFX.class.getResource("/fxml/board.fxml"));

				// Parent is the base class for all nodes that have children in the
				// scene graph such as AnchorPane and most other containers
				Parent parent = (BorderPane) loader.load();

				// Load the parent into a Scene
				Scene scene = new Scene(parent);

				// Put the Scene on Stage
				primaryStage.setScene(scene);
				
		        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		            public void handle(WindowEvent we) {
		            	BoardController controller = loader.getController();
		            	
		            	if(controller.getIsConnected())
		            		controller.getC4Client().sendDisconnectPacket();
		            	
		                Platform.exit();  
		            }
		        });

			} catch (IOException ex) { // getting resources or files could fail
				log.error(null, ex);
				System.exit(1);
			}
		}
	  
	  
	/**
	 * Where it all begins
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
		Platform.exit();
	}
}
