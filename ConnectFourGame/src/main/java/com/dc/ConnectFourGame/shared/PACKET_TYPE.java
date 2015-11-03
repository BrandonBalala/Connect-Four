package com.dc.ConnectFourGame.shared;

/**
 * The packet types that can be sent
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public enum PACKET_TYPE {
	/**
	 * Packet type used to establish connection
	 */
    CONNECT(0),
    /**
     * Packet type used to disconnect from server
     */
    DISCONNECT(1),
    /**
     * Packet type used to reset/restart a game
     */
    RESET_GAME(2),
    /**
     * Packet type used to notify win
     */
	WIN(3),
    /**
     * Packet type used to notify lose
     */
	LOSE(4),
    /**
     * Packet type used to notify tie
     */
	TIE(5),
    /**
     * Packet type used to notify a move
     */
	MOVE(6),
    /**
     * Packet type used to notify a bad move
     */
	BAD_MOVE(7);

	//Variable indication integer equivalent 
	//of each individual packet type
    private int value;

    /**
     * Sets the value fields
     * @param value
     */
    PACKET_TYPE(final int value) {
        this.value = value;
    }
    
    /**
     * Use this method to get integer equivalent of the type of packet
     * @return value
     */
    public int getValue(){
    	return value; 
    } 
}
