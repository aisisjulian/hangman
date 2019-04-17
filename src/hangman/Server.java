package hangman;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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

    static int numClients = 0;
    static int numGames = 0;

    public Server(int port, Consumer<Serializable> callback){
        this.callback = callback;
        this.port = port;
    }

    public int getPort(){ return this.port; }

    public void startConn(ServerSocket ss){
        try{
            while(true){
                ClientThread t = new ClientThread(ss.accept());
            }
        }
        catch(IOException e){
            System.out.println("IOException in startConn()");
        }
    }






    // ******************************************************************* //
    //                                                                     //
    //                              Client Thread Class                    //
    //                                                                     //
    // ******************************************************************* //

    class ClientThread extends Thread{
        int clientIndex;

        // data members for game //
        Game game;
        boolean isInGame;
        ArrayList<ClientThread> team;
        ArrayList<Boolean> lettersGuessed;
        boolean hasPlayed;
        boolean isPlayingAgain;

        // data members for io //
        ObjectOutputStream out;
        Socket socket;
        boolean isConnected;

        ClientThread(Socket s){
            this.socket = s;
            this.clientIndex = numClients;
            setDaemon(true);
            this.isConnected = true;

            this.isPlayingAgain = false;
            this.hasPlayed = false;
            this.isInGame = false;
            lettersGuessed = new ArrayList<>(); //index represents letter
            for(int i = 0; i < 26; i++){ lettersGuessed.add(false); }
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

               while(isConnected){
                  // Serializable data = (Serializable) in.readObject();
               }


           }
           catch(IOException e){

           }


        }








    }
    // ********************  End Client Thread Class ********************* //
    // ******************************************************************* //

}
// ************************** End Server Class *********************** //
// ******************************************************************* //



// ******************************************************************* //
//                                                                     //
//                              Game Class                             //
//                                                                     //
// ******************************************************************* //
class Game{




}
// ***************************** End Game Class ********************** //
// ******************************************************************* //
