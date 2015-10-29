package com.dc.ConnectFourGame.shared;

public enum PACKET_TYPE {
	/**
	 * RE WRITE COMMENTSSSSSSSSSSSSSSSSSSS
	 */
    CONNECT(0), //client sends connect, server sends it back to confirm connection
    DISCONNECT(1), //client sends to server server and just quits
    RESET_GAME(2), //reset_game is sent by client then server responds by sending this back to confirm everything was reset correctly. used when user wants to play again or simply reset board in the middle of a game
	WIN(3), //only client can send this
	LOSE(4), //only client can send this
	TIE(5), //only client can send this
	MOVE(6), //both client and server send this
	BAD_MOVE(7); //only client can send this

    private int value;

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
