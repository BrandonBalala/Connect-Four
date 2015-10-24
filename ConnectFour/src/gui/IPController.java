package gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class IPController {

    @FXML
    private TextField serverIPField;

    @FXML
    private TextField connectingIPField;

    @FXML
    private Button IPBtn;

    @FXML
    void IPClick(ActionEvent event) {
    	System.out.println("clicked");
    }
    
}
