package gui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Basic class for starting a JavaFX application
 *
 * #KFCStandard and JavaFX8
 *
 * @author Ken Fogel
 */
public class MainAppFX extends Application {

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
        
     // The Stage comes from the framework so make a copy to use elsewhere
    	this.primaryStage = primaryStage;
        
        configureIPForm();
        
        primaryStage.setTitle("Connecting");
        
        // Raise the curtain on the Stage
        primaryStage.show();
    }
    
    private void configureIPForm() {
        try {
            // Instantiate the FXMLLoader
            FXMLLoader loader = new FXMLLoader();

            // Set the location of the fxml file in the FXMLLoader
            loader.setLocation(MainAppFX.class.getResource("resources/fxml/IP.fxml"));

            // Parent is the base class for all nodes that have children in the
            // scene graph such as AnchorPane and most other containers
            Parent parent = (GridPane) loader.load();

            // Load the parent into a Scene
            Scene scene = new Scene(parent);

            // Put the Scene on Stage
            primaryStage.setScene(scene);

        } catch (IOException ex) { // getting resources or files could fail
            System.out.println(ex);
            System.exit(1);
        }
    }


    /**
     * Where it all begins
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
