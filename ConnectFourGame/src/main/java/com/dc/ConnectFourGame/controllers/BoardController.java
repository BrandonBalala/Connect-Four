package com.dc.ConnectFourGame.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.dc.ConnectFourGame.client.C4Client;

import javafx.scene.Node;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
/**
 * This is the Board Controller, which allows us to manipulate elements on our
 * GUI while also interacting with the C4Client
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class BoardController implements Initializable {

	@FXML
	private GridPane FirstColumn;
	@FXML
	private GridPane SecondColumn;
	@FXML
	private GridPane ThirdColumn;
	@FXML
	private GridPane FourthColumn;
	@FXML
	private GridPane FifthColumn;
	@FXML
	private GridPane SixthColumn;
	@FXML
	private GridPane SeventhColumn;

	@FXML
	private Label label00;
	@FXML
	private Label label10;
	@FXML
	private Label label20;
	@FXML
	private Label label30;
	@FXML
	private Label label40;
	@FXML
	private Label label50;

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
	private Label label03;
	@FXML
	private Label label13;
	@FXML
	private Label label23;
	@FXML
	private Label label33;
	@FXML
	private Label label43;
	@FXML
	private Label label53;

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

	// Label used to display messages
	@FXML
	private Label labelStatus;

	// Button used for resetting the game
	@FXML
	private Button replayBtn;

	// Button used to connect to server
	@FXML
	private Button doneBtn;

	// TextField in which user writes the IP address
	@FXML
	private TextField serverIpField;

	// The C4Client it interacts with
	private C4Client client;

	// Variable used to know if client is connected to the server or not
	private boolean isConnected;

	// Variable used to know if client is waiting for a packet from the server
	private boolean notWaiting;

	// Array containing all the labels in the correct position
	private Label[][] arrayLabels;

	private final String CLIENT_TOKEN = "X";
	private final String SERVER_TOKEN = "O";
	private final int NO_MOVE = -4;

	/**
	 * Constructor
	 */
	public BoardController() {
		this.client = new C4Client();
		isConnected = false;
		notWaiting = true;
	}

	/**
	 * Returns the server ip adddress inside of the serverIpField TextField
	 * 
	 * @return server ip address
	 */
	public String getConnectingIP() {
		return serverIpField.getText();
	}

	/**
	 * Returns the C4Client instance
	 * 
	 * @return client
	 */
	public C4Client getC4Client() {
		return client;
	}

	/**
	 * Returns whether client is connected to the server or not
	 * 
	 * @return isConnected
	 */
	public boolean getIsConnected() {
		return isConnected;
	}

	/**
	 * Set whether client is connected to the server or not
	 * 
	 * @param isConnected
	 */
	public void setIsConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	/**
	 * Set whether client is waiting for a package from the server
	 * 
	 * @param notWaiting
	 */
	public void setNotWaiting(boolean notWaiting) {
		this.notWaiting = notWaiting;
	}

	/**
	 * Sets a message on to the statusMessage label on the GUI
	 * 
	 * @param message
	 */
	public void setStatusMessage(String message) {
		labelStatus.setText(message);
	}

	/**
	 * Handles button click for connect button and tries establishing a
	 * connection to the server
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void IPClick(ActionEvent event) throws IOException {
		client.readIp(this);
	}

	/**
	 * Handles button click for replay button and sends a reset packet to the
	 * server
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ReplayGame(ActionEvent event) {
		if (isConnected)
			client.sendResetPacket();
	}

	/**
	 * Handles button click for exit button and sends a disconnect packet to the
	 * server
	 * 
	 * @param event
	 */
	@FXML
	void quitGame(ActionEvent event) {
		if (isConnected) {
			client.sendDisconnectPacket();
			Platform.exit();
		}
	}

	/**
	 * This method handles button click action for all 7 gridPanes containing an
	 * fxid ranging from first to seventh. Once client clicks on one of the
	 * gridpanes, it finds thanks to the id the column that was chosen and sends
	 * the col to the C4Client for it to deal with verificaition and sending of
	 * packets
	 * 
	 * @param event
	 */
	@FXML
	void userClick(MouseEvent event) {
		if (isConnected && notWaiting) {
			String id = ((Node) event.getTarget()).getId();
			int colChosen = -1;
			try {
				// Get the integer equivalent of the column that was chosen
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
				if (colChosen != -1) {
					notWaiting = false;
					client.makeMove(colChosen);
					notWaiting = true;
				}
			} catch (Exception e) {
				// ERROR CAUSED BY Clicking on edge of columns
			}
		}
	}

	/**
	 * Set the client's and server's move on the game board
	 * 
	 * @param packet
	 */
	public void setMovesOnBoard(byte[] packet) {
		int rowClient = (int) packet[1];
		int colClient = (int) packet[2];
		int rowServer = (int) packet[3];
		int colServer = (int) packet[4];

		// Checks whether a move was put in the packet for the client and for
		// the server
		if (rowClient != NO_MOVE && colClient != NO_MOVE)
			arrayLabels[rowClient][colClient].setText(CLIENT_TOKEN);
		if (rowServer != NO_MOVE && colServer != NO_MOVE)
			arrayLabels[rowServer][colServer].setText(SERVER_TOKEN);
	}

	/**
	 * This resets the whole game, by basically making all elements in the
	 * labels array blank and displays a new message in the label status
	 */
	public void resetBoard() {
		for (int row = 0; row < arrayLabels.length; row++) {
			for (int col = 0; col < arrayLabels[0].length; col++) {
				arrayLabels[row][col].setText("");
			}
		}
	}

	/**
	 * Initialize method is called after all fxml elements have been loaded This
	 * method initializes the arrayLabels
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		arrayLabels = new Label[][] { { label00, label01, label02, label03, label04, label05, label06 },
				{ label10, label11, label12, label13, label14, label15, label16 },
				{ label20, label21, label22, label23, label24, label25, label26 },
				{ label30, label31, label32, label33, label34, label35, label36 },
				{ label40, label41, label42, label43, label44, label45, label46 },
				{ label50, label51, label52, label53, label54, label55, label56 } };
	}
}