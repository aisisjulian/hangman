package hangman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerFx extends Application {

    private Server server;
    private ServerSocket serverSocket;

    private serverDisplay ss;
    private boolean isServerOn = false;



    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Space-Man Server");
        ss = new serverDisplay();


        this.ss.portInput.setOnAction(ActionEvent -> {
            try {
                server = createServer(Integer.valueOf(ss.portInput.getText()));
                ss.portInput.clear();
                ss.portInput.setVisible(false);
                ss.portInputLabel.setVisible(false);
                ss.serverOn.setDisable(false);
                isServerOn = true;
            }
            catch(Exception e){
                System.out.println("exception in start()");
            }
        });

        ss.serverOn.setOnAction(event -> {
            ss.serverOn.setDisable(true);
            try {
                if(isServerOn) {
                    this.serverSocket = new ServerSocket(server.getPort());
                    startServer();
                    Server.numClients = 0;
                    ss.serverOff.setDisable(false);
                    ss.message.setText("~ server on ~");

                }
            }
            catch (IOException e) {
                ss.portInput.setVisible(true);
                ss.portInputLabel.setVisible(true);
                ss.serverOff.setDisable(true);
            }
        });

        ss.serverOff.setOnAction(event -> {
            try {
                isServerOn = false;
                server.closeConn();
                this.serverSocket.close();
                ss.portInput.setVisible(true);
                ss.portInputLabel.setVisible(true);
                ss.message.setText("~ server off ~");
                ss.serverOff.setDisable(true);
            }
            catch (Exception e){
                //status.setText("FAILED TO TURN SERVER OFF");
            }
            ss.serverOff.setDisable(true);
            ss.serverOn.setDisable(true);
        });

        primaryStage.setScene(ss.scene);
        primaryStage.show();
    }



    public static void main(String[] args) { launch(args); }

    public void startServer() {
        Runnable task = () -> server.startConn(this.serverSocket);
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();

    }

    private Server createServer(int port){
        return new Server(port, data->{
            Platform.runLater(()->{
                ss.numconnectedLabel.setText(" " + Server.numClients);
                System.out.println(Server.numClients);
                if(data.toString().equals("NO-CONNECTION")){
                    System.out.println("NO CONNECTION");
                }
            });
        });
    }

    // ******************************************************************* //
    //                          GUI Nested Classes                         //
    // ******************************************************************* //
    class serverDisplay{
        private Scene scene;
        private BorderPane serverPane;
        private VBox headerBox, centerBox;
        private HBox portBox;
        private Label header, message;
        private Label portInputLabel, numconnectedLabel;
        private TextField portInput;
        private Button serverOn, serverOff;

        serverDisplay(){
            this.serverPane = new BorderPane();
            this.serverPane.setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            this.serverPane.setPrefSize(500, 400);

            header = new Label("WELCOME TO SPACEMAN");
            header.setTextFill(Color.GOLD);
            header.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 32));
            header.setAlignment(Pos.CENTER);

            message = new Label("~ server off ~");
            message.setTextFill(Color.LEMONCHIFFON);
            message.setFont(Font.font("sans-serif", FontWeight.NORMAL, 18));
            message.setAlignment(Pos.CENTER);

            headerBox = new VBox(10, header, message);
            headerBox.setAlignment(Pos.CENTER);

            portInputLabel = new Label("PORT # : ");
            portInputLabel.setTextFill(Color.WHITE);
            portInputLabel.setFont(Font.font("sans-serif", FontWeight.BOLD, 18));

            portInput = new TextField();
            portInput.setBackground(new Background(new BackgroundFill(Color.LEMONCHIFFON, CornerRadii.EMPTY, Insets.EMPTY)));
            portInput.setPrefSize(100, 15);

            portBox = new HBox(5, portInputLabel, portInput);
            portBox.setAlignment(Pos.CENTER);

            numconnectedLabel = new Label("[     Clients Connected:   0   ]");
            numconnectedLabel.setTextFill(Color.WHITE);
            numconnectedLabel.setFont(Font.font("sans-serif", FontWeight.BOLD, 15));
            numconnectedLabel.setAlignment(Pos.CENTER);

            serverOn = new Button("ON");
            serverOn.setBackground(new Background(new BackgroundFill(Color.LEMONCHIFFON, new CornerRadii(10), Insets.EMPTY)));
            serverOn.setPrefSize(60, 30);
            serverOn.setTextFill(Color.MIDNIGHTBLUE);

            serverOff = new Button("OFF");
            serverOff.setBackground(new Background(new BackgroundFill(Color.LEMONCHIFFON, new CornerRadii(10), Insets.EMPTY)));
            serverOff.setPrefSize(60, 30);
            serverOff.setTextFill(Color.MIDNIGHTBLUE);

            HBox serverOptions = new HBox(10, serverOn, serverOff);
            serverOptions.setAlignment(Pos.CENTER);

            centerBox = new VBox(15, portBox, serverOptions, numconnectedLabel);
            centerBox.setPadding(new Insets(50));
            centerBox.setAlignment(Pos.TOP_CENTER);

            serverPane.setCenter(centerBox);
            serverPane.setTop(headerBox);

            scene = new Scene(serverPane, 500, 400);

            //to test library
            Server testLibrary = createServer(5555);
        }



    }

    class clientTableDisplay{

    }

    class clientDisplay{

        private HBox clientStats;
        private String connected, status, clientIndex;
        private Label screenNameLabel, connectedLabel, statusLabel;

        clientDisplay(String clientIndex){
            this.clientIndex = clientIndex;
            this.connected = " ~ ";
            this.status = " ~ "; //letters guessed
            screenNameLabel = new Label(this.clientIndex);
            screenNameLabel.setPrefSize(100, 30);
            screenNameLabel.setAlignment(Pos.CENTER);
            connectedLabel = new Label(connected);
            connectedLabel.setPrefSize(100, 30);
            connectedLabel.setAlignment(Pos.CENTER);
            statusLabel = new Label(status);
            statusLabel.setPrefSize(100, 30);
            statusLabel.setAlignment(Pos.CENTER);
            clientStats = new HBox(3, this.screenNameLabel, this.connectedLabel, this.statusLabel);
            clientStats.setAlignment(Pos.CENTER);
        }
    }
    // ********************  End Nested GUI Classes ********************** //
    // ******************************************************************* //

}

