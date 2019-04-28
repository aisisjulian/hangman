package hangman;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

// ******************************************************************* //
//                                                                     //
//                              Server Class                            //
//                                                                     //
// ******************************************************************* //

public class Server {

    private int port;
    private Consumer<Serializable> callback;
    Serializable data;

    private ArrayList<ClientThread> clientThreadList = new ArrayList<>();
    private HashMap<String, ClientThread> clientThreadMap = new HashMap<>();
    private ArrayList<Game> gamesList = new ArrayList<>();

    private ArrayList<String> dictionary = new ArrayList<>();
    private ArrayList<String> easyDictionary = new ArrayList<>();
    private ArrayList<String> mediumDictionary = new ArrayList<>();
    private ArrayList<String> hardDictionary = new ArrayList<>();

    static int numClients = 0;
    static int numGames = 0;

    public Server(int port){
        this.callback = callback;
        this.port = port;
        initDictionary();
    }

    public void initDictionary(){
        String word = "";
        try {
            Scanner inFile = new Scanner(new File ("src/dictionary-small.txt"));
            List<String> temp = new ArrayList<String>();

            while (inFile.hasNext()){
                word = inFile.next();
                word = word.toUpperCase();
                dictionary.add(word);

                if(word.length() <= 4 && word.length() > 2){
                    easyDictionary.add(word);
                }
                else if(word.length() > 4 && word.length() <= 7){
                    mediumDictionary.add(word);
                }
                else if(word.length() > 7){
                    hardDictionary.add(word);
                }
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        prints the library
//        for (int i = 0; i < dictionary.size(); i++){
//            System.out.println(dictionary.get(i));
//        }

    }


    public int getPort(){ return this.port; }

    public void startConn(ServerSocket ss){
        try{
            System.out.println("Waiting for clients on server socket: " + ss);
            while(true){
                ClientThread t = new ClientThread(ss.accept());
                clientThreadList.add(t);
                t.clientIndex = clientThreadList.size()-1;
                t.start();
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

    public void broadcast(Serializable data){
        try{
            for (int i = 0; i <clientThreadList.size(); i++){
                clientThreadList.get(i).out.writeObject(data);
            }
        }
        catch(Exception e){
            System.out.println("Exception -> broadcast()");
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
        private int numPlayersChosen;
        private String difficultyChosen;

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
            this.numPlayersChosen = 0;
            this.difficultyChosen = "";

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

        public Serializable getMessage(){ return data; }


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

                   if(data.toString().split(" ")[0].equals("NUM-PLAYERS:")){
                       this.numPlayersChosen = Integer.valueOf(data.toString().split(" ")[1]);
                   }
                   if(data.toString().split(" ")[0].equals("DIFFICULTY:")){
                       this.difficultyChosen = data.toString().split(" ")[1];
                       if(this.numPlayersChosen == 1 && !difficultyChosen.equals("")){
                           this.game = new Game(this);
                           gamesList.add(this.game);
                       }
                       else if(this.numPlayersChosen != 0 && !difficultyChosen.equals("")){
                           this.game = findGame();
                       }
                   }
                   if(data.toString().split(" ")[0].equals("LETTER:")){
                       String letter = data.toString().split(" ")[1];
                       game.evaluateGuess(letter);
                   }

               }
           }
           catch(Exception e){
               callback.accept("NO-CONNECTION");

           }

        }

        public Game findGame(){
            Game g;
            for(int i = 0; i < gamesList.size(); i++){
                g = gamesList.get(i);
                if(!g.isActive && g.numPlayers == this.numPlayersChosen && g.difficulty.equals(this.difficultyChosen)){
                    g.addPlayer(this);
                    return g;
                }
            }
            send("WAIT-FOR-PLAYERS", clientIndex);
            g = new Game(this);
            gamesList.add(g);
            return g;
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
        private int numPlayers = 1;
        private int numPlayersConnected = 0;
        private String difficulty = "easy";
        private ArrayList<ClientThread> players;
        private boolean isActive;
        private int currentlyGuessing; //index of player of whose turn it is
        private ArrayList<Boolean> lettersGuessed; //can change to array of characters?
        private String word;
        private ArrayList<Character> lettersInWord;
        private ArrayList<Boolean> lettersGuessedInWord; //each index represents a character of the string
        private int lives = 5; //subject to change

        Game(ClientThread player){
            isActive = false;
            players = new ArrayList<>();
            players.add(player);
            numPlayersConnected++;
            numPlayers = player.numPlayersChosen;
            difficulty = player.difficultyChosen;
            if(numPlayersConnected == numPlayers){ isActive = true; startGame();} //can change total num players in game HERE
            else{
                send("WAIT-FOR-PLAYERS", player.clientIndex);
            }
        }

        void addPlayer(ClientThread player){
            players.add(player);
            numPlayersConnected++;
            if(numPlayersConnected == numPlayers && !isActive){ isActive = true; startGame();} //can change total num players in game HERE
            else{
                send("WAIT-FOR-PLAYERS", player.clientIndex);
            }
        }

        void startGame(){
            if(isActive){
                //*****************************************//
                lettersGuessed = new ArrayList<>(); //index represents letter
                for(int i = 0; i < 26; i++){ this.lettersGuessed.add(false); }

                //    INSERT CODE THAT PICKS RANDOM WORD   //

                Random r = new Random();
                int w;
                if(difficulty.equals("easy")){
                    w = r.nextInt(easyDictionary.size());
                    word = easyDictionary.get(w);
                }
                else if(difficulty.equals("medium")){
                    w = r.nextInt(mediumDictionary.size());
                    word = mediumDictionary.get(w);
                }
                else{
                    w = r.nextInt(hardDictionary.size());
                    word = hardDictionary.get(w);
                }

                removeDup();

                for(int i = 0; i < players.size(); i++){
                    send("WORD: " + word, players.get(i).clientIndex);
                    send("START", players.get(i).clientIndex);
                }
              //  lettersGuessedInWord = new ArrayList<>(); //index represents letter
              //  for(int i = 0; i < wordLength; i++){ lettersGuessedInWord.add(false); }
                //*****************************************//

                currentlyGuessing = 0;
                send("WAITING-FOR-GUESS", players.get(currentlyGuessing).clientIndex);
            }
        }


        void resetGame(){
            players.clear();
            numPlayersConnected = 0;
        }


        void evaluateGuess(String letter){
            //update lettersGuessed

            switch (letter) {
                case "A":
                    lettersGuessed.set(0, true);
                    break;
                case "B":
                    lettersGuessed.set(1, true);
                    break;
                case "C":
                    lettersGuessed.set(2, true);
                    break;
                case "D":
                    lettersGuessed.set(3, true);
                    break;
                case "E":
                    lettersGuessed.set(4, true);
                    break;
                case "F":
                    lettersGuessed.set(5, true);
                    break;
                case "G":
                    lettersGuessed.set(6, true);
                    break;
                case "H":
                    lettersGuessed.set(7, true);
                    break;
                case "I":
                    lettersGuessed.set(8, true);
                    break;
                case "J":
                    lettersGuessed.set(9, true);
                    break;
                case "K":
                    lettersGuessed.set(10, true);
                    break;
                case "L":
                    lettersGuessed.set(11, true);
                    break;
                case "M":
                    lettersGuessed.set(12, true);
                    break;
                case "N":
                    lettersGuessed.set(13, true);
                    break;
                case "O":
                    lettersGuessed.set(14, true);
                    break;
                case "P":
                    lettersGuessed.set(15, true);
                    break;
                case "Q":
                    lettersGuessed.set(16, true);
                    break;
                case "R":
                    lettersGuessed.set(17, true);
                    break;
                case "S":
                    lettersGuessed.set(18, true);
                    break;
                case "T":
                    lettersGuessed.set(19, true);
                    break;
                case "U":
                    lettersGuessed.set(20, true);
                    break;
                case "V":
                    lettersGuessed.set(21, true);
                    break;
                case "W":
                    lettersGuessed.set(22, true);
                    break;
                case "X":
                    lettersGuessed.set(23, true);
                    break;
                case "Y":
                    lettersGuessed.set(24, true);
                    break;
                case "Z":
                    lettersGuessed.set(25, true);

            }

            for(int i = 0; i < players.size(); i++){
                if(i != currentlyGuessing) {
                    send("LETTER: " + letter, players.get(i).clientIndex);
                }
            }
            if(currentlyGuessing < numPlayers-1){
                currentlyGuessing++;
            }
            else {
                currentlyGuessing = 0;
            }

            send("WAITING-FOR-GUESS", players.get(currentlyGuessing).clientIndex);

            //if wrong guess, update lives, check for loss
            //if right guess, update lettersGuessInWord, check for win

            if (lettersInWord.contains(letter)){
                this.lettersInWord.remove(letter);
                checkForWin();
            }
            else{
                lives--;
                checkForLoss();
            }

            //move onto next player
        }

        boolean checkForWin(){
           /* for (int i = 0; i < word.length(); i++){
                int index = word.charAt(i) - 65;
                if (lettersGuessed.get(index) == false){
                    return false;
                }
            }*/
           if (lettersInWord.size() == 0 ){
               System.out.println("Win");
               return true;
           }
            return false;
        }

        void removeDup(){
            this.lettersInWord = new ArrayList<>();
            int len = word.length();
            for (int i = 0; i < len; i++) {
                char c = word.charAt(i);
                if (!lettersInWord.contains(c)){
                    this.lettersInWord.add(c);
                }
            }
        }


        boolean checkForLoss(){
            if (lives < 0){
                System.out.println("Lost");
                return true;
            }
            return false;
        }

    }
    // ***************************** End Game Class ********************** //
    // ******************************************************************* //



}
// ************************** End Server Class *********************** //
// ******************************************************************* //