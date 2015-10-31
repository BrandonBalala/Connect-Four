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
    private Button connectButton;

    @FXML
    void connectToServer(ActionEvent event) {
    	System.out.println("IPCONTROLLER Stuff");
    }
}
