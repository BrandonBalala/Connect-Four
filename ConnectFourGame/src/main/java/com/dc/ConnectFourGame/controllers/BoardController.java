package com.dc.ConnectFourGame.controllers;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.dc.ConnectFourGame.client.C4Client;
import com.dc.ConnectFourGame.shared.Identifier;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

import javafx.scene.Node;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

public class BoardController implements Initializable{
	@FXML
	private GridPane FirstColumn;
	@FXML
	private VBox VBox1;
	@FXML
	private VBox VBox2;
	@FXML
	private VBox VBox3;
	@FXML
	private VBox VBox4;
	@FXML
	private VBox VBox5;
	@FXML
	private VBox VBox6;
	@FXML
	private VBox VBox7;
	@FXML
	private Label label00;

	@FXML
	private Label label10;

	@FXML
	private Label label30;

	@FXML
	private Label label20;

	@FXML
	private Label label40;

	@FXML
	private Label label50;

	@FXML
	private Label labelStatus;

	@FXML
	private Button replayBtn;

	@FXML
	private TextField serverIpField;

	@FXML
	private Button doneBtn;

	@FXML
	private GridPane boardPane;

	@FXML
	private GridPane SecondColumn;

	@FXML
	private Label label01;

	@FXML
	private Label label11;

	@FXML
	private Label label21;

	@FXML
	private Label label31;

	@FXML
	private Label label41;

	@FXML
	private Label label51;

	@FXML
	private GridPane ThirdColumn;

	@FXML
	private Label label02;

	@FXML
	private Label label12;

	@FXML
	private Label label22;

	@FXML
	private Label label32;

	@FXML
	private Label label42;

	@FXML
	private Label label52;

	@FXML
	private GridPane FourthColumn;

	@FXML
	private Label label03;

	@FXML
	private Label label33;

	@FXML
	private Label label13;

	@FXML
	private Label label23;

	@FXML
	private Label label43;

	@FXML
	private Label label53;

	@FXML
	private GridPane FifthColumn;

	@FXML
	private Label label04;

	@FXML
	private Label label14;

	@FXML
	private Label label24;

	@FXML
	private Label label34;

	@FXML
	private Label label44;

	@FXML
	private Label label54;

	@FXML
	private GridPane SixthColumn;

	@FXML
	private Label label05;

	@FXML
	private Label label15;

	@FXML
	private Label label25;

	@FXML
	private Label label35;

	@FXML
	private Label label45;

	@FXML
	private Label label55;

	@FXML
	private GridPane SeventhColumn;

	@FXML
	private Label label06;

	@FXML
	private Label label16;

	@FXML
	private Label label26;

	@FXML
	private Label label36;

	@FXML
	private Label label46;

	@FXML
	private Label label56;

	private Label[][] arrayLabels;

	private C4Client client;
	private boolean isConnected;
	private boolean notWaiting;
	
	public BoardController() {
		this.client = new C4Client();
		isConnected = false;
		notWaiting = true;
	}

	public void setIsConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public void setNotWaiting(boolean notWaiting) {
		this.notWaiting = notWaiting;
	}

	@FXML
	void IPClick(ActionEvent event) throws IOException {
		client.readIp(this);
	}

	public String getConnectingIP() {
		return serverIpField.getText();
	}

	@FXML
	void ReplayGame(ActionEvent event) {
		if (isConnected)
			client.sendResetPacket();
	}

	@FXML
	void quitGame(ActionEvent event) {
		if (isConnected) {
			client.sendDisconnectPacket();
			Platform.exit();
		}
	}

	@FXML
	void userClick(MouseEvent event) {
		if (isConnected && notWaiting) {
			String id = ((Node) event.getTarget()).getId();
			int colChosen = -1;
			switch (id) {
			case "FirstColumn":
				colChosen = 0;
				break;
			case "SecondColumn":
				colChosen = 1;
				break;
			case "ThirdColumn":
				colChosen = 2;
				break;
			case "FourthColumn":
				colChosen = 3;
				break;
			case "FifthColumn":
				colChosen = 4;
				break;
			case "SixthColumn":
				colChosen = 5;
				break;
			case "SeventhColumn":
				colChosen = 6;
				break;
			}
			if(colChosen != -1)
			//HANDLE this exception here
			try {
				notWaiting=false;
				client.makeMove(colChosen);
				notWaiting=true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setStatusMessage(String message) {
		labelStatus.setText(message);
	}

	public void setMovesOnBoard(byte[] packet) {
		int rowClient = (int) packet[1];
		int colClient = (int) packet[2];
		int rowServer = (int) packet[3];
		int colServer = (int) packet[4];

		
		System.out.println("Move attempting to chance board: "
				+ "\nROW CLIENT: " + rowClient
				+ "\nCOL CLIENT: " + colClient
				+ "\nROW SERVER: " + rowServer
				+ "\nCOL SERVER: " + colServer);
		if(rowClient != -4 && colClient != -4)
		arrayLabels[rowClient][colClient].setText("X");
		if(rowServer != -4 && colServer != -4)
		arrayLabels[rowServer][colServer].setText("O");
	}

	public void resetBoard() {
		for (int row = 0; row < arrayLabels.length; row++) {
			for (int col = 0; col < arrayLabels[0].length; col++) {
				arrayLabels[row][col].setText("");
			}
		}

		setStatusMessage("");
	}
	
	public C4Client getC4Client(){
		return client;
	}
	
	public boolean getIsConnected(){
		return isConnected;
	}
  
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		arrayLabels = new Label[][]{ { label00, label01, label02, label03, label04, label05, label06 },
			{ label10, label11, label12, label13, label14, label15, label16 },
			{ label20, label21, label22, label23, label24, label25, label26 },
			{ label30, label31, label32, label33, label34, label35, label36 },
			{ label40, label41, label42, label43, label44, label45, label46 },
			{ label50, label51, label52, label53, label54, label55, label56 } };
	}
}