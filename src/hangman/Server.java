package hangman;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

// ******************************************************************* //
//                                                                     //
//                              Sever Class                            //
//                                                                     //
// ******************************************************************* //

public class Server {

    private int port;
    private Consumer<Serializable> callback;

    private ArrayList<ClientThread> clientThreadList = new ArrayList<>();
    private HashMap<String, ClientThread> clientThreadMap = new HashMap<>();
    private ArrayList<Game> gamesList = new ArrayList<>();

    private ArrayList<String> dictionary = new ArrayList<>();

    static int numClients = 0;
    static int numGames = 0;

    public Server(int port, Consumer<Serializable> callback){
        this.callback = callback;
        this.port = port;

        //***************************************//
        //  CREATE & INITIALIZE DICTIONARY HERE  //

        /*String token = "";
        try {
            //need to add you own path
            Scanner inFile = new Scanner(new File ("C:/Users/wsiew/IdeaProjects/hangman/src/dictionary-small.txt"));
            List<String> temps = new ArrayList<String>();

            while (inFile.hasNext()){
                token = inFile.next();
                dictionary.add(token);
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //prints the library
        for (int i = 0; i < dictionary.size(); i++){
            System.out.println(dictionary.get(i));
        }
        */

    }

    public int getPort(){ return this.port; }

    public void startConn(ServerSocket ss){
        try{
            while(true){
                ClientThread t = new ClientThread(ss.accept());
            }
        }
        catch(IOException e){
            System.out.println("IOException -> startConn()");
        }
    }

    public void send(Serializable data, int clientIndex){
        try{
            clientThreadList.get(clientIndex).out.writeObject(data);
        }
        catch(Exception e){
            System.out.println("Exception -> send()");
        }

    }

    public void closeConn() throws Exception{
        for(int i = 0; i < clientThreadList.size(); i++){
            if(clientThreadList.get(i).isConnected){
                send("NO-CONNECTION", i);
                clientThreadList.get(i).socket.close();
            }
        }
        numClients = 0;
    }

    // ******************************************************************* //
    //                                                                     //
    //                              Client Thread Class                    //
    //                                                                     //
    // ******************************************************************* //

    class ClientThread extends Thread{
        private int clientIndex;

        // data members for game //
        private Game game;
        private boolean isInGame;
        private ArrayList<ClientThread> team;
        boolean hasPlayed;
        boolean isPlayingAgain;

        // data members for io //
        ObjectOutputStream out;
        private Socket socket;
        boolean isConnected = false;

        ClientThread(Socket s){
            this.socket = s;
            this.clientIndex = numClients;
            setDaemon(true);
            this.isConnected = true;

            this.isPlayingAgain = false;
            this.hasPlayed = false;
            this.isInGame = false;

        }

        public void setClientIndex( int index ){ this.clientIndex = index; }
        public int getClientIndex(){ return this.clientIndex; }

        public void setConnected( boolean c ){ this.isConnected = c; }
        public boolean isConnected(){ return this.isConnected; }

        public void setHasPlayed( boolean p ){ this.hasPlayed = p; }
        public boolean HasPlayed(){ return this.hasPlayed; }

        public void setIsPlayingAgain( boolean p ){ this.isPlayingAgain = p; }
        public boolean isPlayingAgain(){ return this.isPlayingAgain; }

        public void setIsInGame( boolean g ){ this.isInGame = g; }
        public boolean isInGame(){ return this.isInGame; }

        public void setGame(Game g){ this.game = g; }
        public Game getGame(){ return this.game; }


        public void run() {
           try(
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){

               this.out = out;
               socket.setTcpNoDelay(true);
                send("CONNECTION", this.clientIndex);
               while(this.isConnected){
                   Serializable data = (Serializable) in.readObject();
                   System.out.println(data.toString());
               }


           }
           catch(Exception e){
               callback.accept("NO-CONNECTION");

           }


        }

    }
    // ********************  End Client Thread Class ********************* //
    // ******************************************************************* //


    // ******************************************************************* //
    //                                                                     //
    //                              Game Class                             //
    //                                                                     //
    // ******************************************************************* //
    class Game{
        private int numPlayers = 0;
        private ArrayList<ClientThread> players;
        private boolean isActive;
        private int currentlyGuessing; //index of player of whose turn it is
        private ArrayList<Boolean> lettersGuessed; //can change to array of characters?
        private String word;
        private int wordLength;
        private ArrayList<Boolean> lettersGuessedInWord; //each index represents a character of the string
        private int lives = 5; //subject to change

        Game(ClientThread player){
            isActive = false;
            players = new ArrayList<>();
            players.add(player);
            numPlayers++;
        }

        void addPlayer(ClientThread player){
            players.add(player);
            numPlayers++;
        }

        void startGame(){
            if(numPlayers == 4){ isActive = true; } //can change total num players in game HERE
            if(isActive){
                for(int i = 0; i < players.size(); i++){
                    send("START", players.get(i).clientIndex);
                }


                //*****************************************//
                lettersGuessed = new ArrayList<>(); //index represents letter
                for(int i = 0; i < 26; i++){ lettersGuessed.add(false); }

                //    INSERT CODE THAT PICKS RANDOM WORD   //

                lettersGuessedInWord = new ArrayList<>(); //index represents letter
                for(int i = 0; i < wordLength; i++){ lettersGuessedInWord.add(false); }
                //*****************************************//

                currentlyGuessing = 0;
                send("WAITING-FOR-GUESS", players.get(currentlyGuessing).clientIndex);
            }
        }



        void resetGame(){
            players.clear();
            numPlayers = 0;
        }

        void evaluateGuess(){
            //update lettersGuessed

            //if wrong guess, update lives, check for loss

            //if right guess, update lettersGuessInWord, check for win

            //move onto next player
        }

        boolean checkForWin(){

            return false;
        }

        boolean checkForLoss(){
            return false;
        }

    }
    // ***************************** End Game Class ********************** //
    // ******************************************************************* //



}
// ************************** End Server Class *********************** //
// ******************************************************************* //



