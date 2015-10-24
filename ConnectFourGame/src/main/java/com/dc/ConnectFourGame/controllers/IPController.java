package com.dc.ConnectFourGame.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.ConnectFourGame.client.C4Client;
import com.dc.ConnectFourGame.server.C4Server;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class IPController {

	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	private C4Client client = new C4Client();
	
    @FXML
    private TextField serverIPField;

    @FXML
    private Button IPBtn;
    
    @FXML
    private Label errorLabel;

    @FXML
    void IPClick(ActionEvent event) throws IOException {
    	client.readIp();
    	
    }
    
    public String getConnectingIP(){
    	return serverIPField.getText();
    }
    
    public void setErrorLabel(String msg){
    	errorLabel.setText(msg);
    }
}
