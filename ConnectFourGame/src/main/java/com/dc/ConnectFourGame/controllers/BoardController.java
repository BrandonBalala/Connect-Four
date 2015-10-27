package com.dc.ConnectFourGame.controllers;

import java.io.IOException;

import com.dc.ConnectFourGame.client.C4Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BoardController {

    @FXML
    private VBox FirstColumn;

    @FXML
    private Label label00;

    @FXML
    private Label label01;

    @FXML
    private Label label03;

    @FXML
    private Label label02;

    @FXML
    private Label label04;

    @FXML
    private Label label05;
    
    @FXML
    private VBox SecondColumn;

    @FXML
    private Label label10;

    @FXML
    private Label label11;

    @FXML
    private Label label12;

    @FXML
    private Label label13;

    @FXML
    private Label label14;

    @FXML
    private Label label15;

    @FXML
    private VBox ThirdColumn;

    @FXML
    private Label label20;

    @FXML
    private Label label21;

    @FXML
    private Label label22;

    @FXML
    private Label label23;

    @FXML
    private Label label24;

    @FXML
    private Label label25;

    @FXML
    private VBox FourthColumn;

    @FXML
    private Label label30;

    @FXML
    private Label label33;

    @FXML
    private Label label31;

    @FXML
    private Label label32;

    @FXML
    private Label label34;

    @FXML
    private Label label35;

    @FXML
    private VBox FifthColumn;

    @FXML
    private Label label40;

    @FXML
    private Label label41;

    @FXML
    private Label label42;

    @FXML
    private Label label43;

    @FXML
    private Label label44;

    @FXML
    private Label label45;

    @FXML
    private VBox SixthColumn;

    @FXML
    private Label label50;

    @FXML
    private Label label51;

    @FXML
    private Label label52;

    @FXML
    private Label label53;

    @FXML
    private Label label54;

    @FXML
    private Label label55;

    @FXML
    private VBox SeventhColumn;

    @FXML
    private Label label60;

    @FXML
    private Label label61;

    @FXML
    private Label label62;

    @FXML
    private Label label63;

    @FXML
    private Label label64;

    @FXML
    private Label label65;
    
    private Label[] arrayLabels = {label00,label01,label02,label03,label04,label05,
    							   label10,label11,label12,label13,label14,label15,
    							   label20,label21,label22,label23,label24,label25,
    							   label30,label31,label32,label33,label34,label35,
    							   label40,label41,label42,label43,label44,label45,
    							   label50,label51,label52,label53,label54,label55,
    							   label60,label61,label62,label63,label64,label65};
    
    private C4Client client = new C4Client();
	
    @FXML
    private TextField serverIpField;
    
    @FXML
    private Button replayBtn;

    @FXML
    private Button doneBtn;
    
    @FXML
    private GridPane boardPane;

    @FXML
    void quitGame(ActionEvent event) {
    	Platform.exit();
    }

    @FXML
    void IPClick(ActionEvent event) throws IOException {
    	client.readIp();
    }
    
    public String getConnectingIP(){
    	return serverIpField.getText();
    }

    @FXML
    void ReplayGame(ActionEvent event) {

    }

    @FXML
    void userClick(ActionEvent event) {
    	

    }

}