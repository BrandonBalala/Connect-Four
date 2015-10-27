package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
 
import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
 
public class C4ServerSession extends C4Logic {
       
        private boolean playAgain;
        private boolean gameOver;
        Socket connection;
        OutputStream send;
        InputStream  receive;
        C4Packet converser;
        C4Logic c4logic;
       
       
        public C4ServerSession(Socket connection) throws IOException{
               
                playAgain = true;
                gameOver = false;
            this.connection = connection;
                receive = connection.getInputStream();
                send = connection.getOutputStream();
            converser = new C4Packet();
            c4logic = new C4Logic();
            start();
               
        }
        private void start() throws IOException {
                byte[] packet = converser.receivePacket(receive);
                if(packet[0] == 2)
                        playAgain = true;
                else if(packet[0] == 3)
                {
                        playAgain = false;
                        gameOver = true;
                }
            startGameSession();
        }
 
        public void startGameSession() throws IOException{
       
                byte[] b = {0,0,0};
                sendPacket(b);
                /*             
               
                while (false)
                {
                       
                        //a session can be 0 or more games. check if the user wants to play
                }*/
               
                while(playAgain)
                {
                        while(!gameOver)
                        {
                                //AI logic
                        }
       
                }
               
                connection.close();
        }
       
        public void sendPacket(byte[] packet) throws IOException
        {
                send = connection.getOutputStream();
                converser.sendPacket(packet, send);
        }
       
        /**
         * The AI
         *
         * @return
         */
        public boolean decideMove(int serverToken, int clientToken){
                //TODO
                int row = getWinningMove(serverToken);
                //CHECK IF ROWS -1
                row = getBlockingMove(clientToken);
                //CHECK IF ROWS -1
                row = getRandomMove(serverToken);
                return false;  
        }
       
        private int getBlockingMove(int player) {
                // TODO Auto-generated method stub
                return -1;
        }
 
        private int getWinningMove(int player) {
                // TODO Auto-generated method stub
                int[][]gameBoard = c4logic.getGameBoard();
                int rowLength = gameBoard.length;
                int colLength = gameBoard[0].length;
               
                //Horizontal check
                int colChoice = -1;
                outerLoop:
                for(int row = 0; row < rowLength; row++){
                        for(int col = 0; col <= colLength -4; col++){
                                int token1 = gameBoard[row][col];
                                int token2 = gameBoard[row][col+1];
                                int token3 = gameBoard[row][col+2];
                                int token4 = gameBoard[row][col+3];
                               
                                if(token1 == 0 && token2 == player && token3 == player && token4 == player){
                                        colChoice = col;
                                        break outerLoop;
                                }
                                if(token2 == 0 && token1 == player && token3 == player && token4 == player){
                                        colChoice = col+1;
                                        break outerLoop;
                                }
                                if(token3 == 0 && token1 == player && token2 == player && token4 == player){
                                        colChoice = col+2;
                                        break outerLoop;
                                }
                                if(token4 == 0 && token1 == player && token2 == player && token3 == player){
                                        colChoice = col+1;
                                        break outerLoop;
                                }
                        }
                }
                if(colChoice != -1){
                        return colChoice;
                }
               
                //Vertical check
                colChoice = -1;
                outerLoop:
                for(int col = 0; col < colLength; col++){
                        for(int row = 0; col <= rowLength -4; col++){
                                int token1 = gameBoard[row][col];
                                int token2 = gameBoard[row+1][col];
                                int token3 = gameBoard[row+2][col];
                                int token4 = gameBoard[row+3][col];
                               
                                if(token1 == 0 && token2 == player && token3 == player && token4 == player){
                                        colChoice = col;
                                        break outerLoop;
                                }
                                if(token2 == 0 && token1 == player && token3 == player && token4 == player){
                                        colChoice = col+1;
                                        break outerLoop;
                                }
                                if(token3 == 0 && token1 == player && token2 == player && token4 == player){
                                        colChoice = col+2;
                                        break outerLoop;
                                }
                                if(token4 == 0 && token1 == player && token2 == player && token3 == player){
                                        colChoice = col+1;
                                        break outerLoop;
                                }
                        }
                }
                if(colChoice != -1){
                        return colChoice;
                }
               
                //Diagonal-Upwards check
                colChoice = -1;
                outerLoop:
                for(int col = 0; col < colLength; col++){
                        for(int row = 0; col <= rowLength -4; col++){
                                int token1 = gameBoard[row][col];
                                int token2 = gameBoard[row+1][col];
                                int token3 = gameBoard[row+2][col];
                                int token4 = gameBoard[row+3][col];
                               
                                if(token1 == 0 && token2 == player && token3 == player && token4 == player){
                                        colChoice = col;
                                        break outerLoop;
                                }
                                if(token2 == 0 && token1 == player && token3 == player && token4 == player){
                                        colChoice = col+1;
                                        break outerLoop;
                                }
                                if(token3 == 0 && token1 == player && token2 == player && token4 == player){
                                        colChoice = col+2;
                                        break outerLoop;
                                }
                                if(token4 == 0 && token1 == player && token2 == player && token3 == player){
                                        colChoice = col+1;
                                        break outerLoop;
                                }
                        }
                }
                if(colChoice != -1){
                        return colChoice;
                }
               
               
                return -1;
        }
       
        public int getRandomMove(int player){
                int col = -1;
               
                while(col != -1){
                        Random rand = new Random();
                        int max = c4logic.getGameBoard()[0].length - 1;
                        int min = 0;
                        int randomCol = rand.nextInt((max - min) + 1) + min;
                       
                        col  = setChoice(randomCol, player);
                }
               
                return col;
        }
}