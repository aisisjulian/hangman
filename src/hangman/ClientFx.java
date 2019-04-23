package hangman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClientFx extends Application {

    private ClientConnection client;
    private boolean isConnected = false;
    private ArrayList<String> clientsConnected = new ArrayList<>();

    private Runnable task;
    private Thread t;
    boolean started = false;


    private Scene startScene, gameScene, endScene;
    private StartScene ss = new StartScene();
    private GameScene gs = new GameScene();
    private EndScene es = new EndScene();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Welcome to Spaceman :-)");
        startScene = ss.scene;
        primaryStage.setScene(this.startScene);

        ss.connectButton.setOnAction(event->{
            if(!ss.ipInput.getText().isEmpty() && !ss.portInput.getText().isEmpty()){
                try {
                    if(!started) {
                        started = true;
                        client = createClient(ss.ipInput.getText(), Integer.parseInt(ss.portInput.getText()), primaryStage);
                        task = () -> client.clientConnect();
                        t = new Thread(task);
                        t.setDaemon(true);
                        t.start();
                    }
                }
                catch(Exception e){
                    ss.connectButton.setDisable(false);
                    ss.ipInput.clear();
                    ss.portInput.clear();
                    System.out.println("exception with connect button");
                }
            }
        });




        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Client createClient(String IP, int portIn, Stage primaryStage) {
        return new Client(IP, portIn, data -> {
            Platform.runLater(() -> {
                switch (data.toString()) {
                    case "CONNECTION":
                        ss.header.setText("CONNECTED TO SERVER");
                        ss.header.setPrefSize(300, 40);
                        ss.header.setAlignment(Pos.CENTER);
                        isConnected = true;
                        ss.ipInput.clear();
                        ss.ipInput.setVisible(false);
                        ss.ipLabel.setVisible(false);
                        ss.portLabel.setVisible(false);
                        ss.portInput.clear();
                        ss.portInput.setVisible(false);
                        ss.connectButton.setDisable(true);
                        break;
                    case "NO-CONNECTION":
                        isConnected = false;
                        ss.ipInput.clear();
                        ss.ipInput.setVisible(true);
                        ss.ipLabel.setVisible(true);
                        ss.portLabel.setVisible(true);
                        ss.portInput.clear();
                        ss.portInput.setVisible(true);
                        ss.connectButton.setDisable(false);
                        ss.connectButton.setText("connect");
                        ss.header.setText("NO CONNECTION");
                        ss.header.setPrefSize(300, 40);
                        ss.header.setAlignment(Pos.CENTER);
                        started = false;
                        primaryStage.setScene(this.startScene);
                        break;
                }
            });
        });


    }

    // ******************************************************************* //
    //                          GUI Nested Classes                         //
    // ******************************************************************* //
    class StartScene{
        private Scene scene;
        private BorderPane startPane;
        private Image startBackgroundImage;
        private Background startBackground;
        private Button singlePlayerButton, multiPlayerButton, twoPlayerButton, threePlayerButton, fourPlayerButton;
        private VBox connectionBox;
        private VBox optionsBox;
        private Button connectButton, startButton;
        private TextArea ipInput, portInput;
        private Label header, ipLabel, portLabel, numPlayersLabel;

        public StartScene(){
            startPane = new BorderPane();
            startBackgroundImage  = new Image("startScene.png");
            startBackground = new Background(new BackgroundFill(new ImagePattern(startBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            Image buttonBackgroundImage  = new Image("buttonBackground.png");
            Background buttonBackground = new Background(new BackgroundFill(new ImagePattern(buttonBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            startPane.setBackground(startBackground);

            header = new Label("");
            header.setTextFill(Color.WHITE);
            header.setPrefSize(300, 40);
            header.setAlignment(Pos.CENTER);
            portLabel = new Label("Port : ");
            portLabel.setTextFill(Color.WHITE);
            portLabel.setPrefSize(50, 20);
            portLabel.setTextAlignment(TextAlignment.CENTER);
            portInput = new TextArea();
            portInput.setPrefSize(150, 20);

            ipLabel = new Label("IP: ");
            ipLabel.setTextFill(Color.WHITE);
            ipLabel.setPrefSize(50, 20);
            ipLabel.setTextAlignment(TextAlignment.CENTER);
            ipInput = new TextArea();
            ipInput.setPrefSize(150, 20);

            HBox ipBox = new HBox(ipLabel, ipInput);
            HBox portBox = new HBox(portLabel, portInput);
            ipBox.setAlignment(Pos.CENTER);
            portBox.setAlignment(Pos.CENTER);

            connectButton = new Button("CONNECT");
            connectButton.setPrefSize(250, 80);
            connectButton.setBackground(buttonBackground);
            connectButton.setTextFill(Color.NAVY);
            connectButton.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 22));
            connectButton.setTextAlignment(TextAlignment.CENTER);
            connectionBox = new VBox(10, header, ipBox, portBox, connectButton);
            connectionBox.setAlignment(Pos.CENTER);

            singlePlayerButton = new Button(" Single-Player");
            singlePlayerButton.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(7), Insets.EMPTY)));
            singlePlayerButton.setPrefSize(150, 40);
            singlePlayerButton.setTextFill(Color.INDIGO);
            singlePlayerButton.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 18));

            multiPlayerButton = new Button("Multi-Player");
            multiPlayerButton.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(7), Insets.EMPTY)));
            multiPlayerButton.setPrefSize(150, 40);
            multiPlayerButton.setTextFill(Color.INDIGO);
            multiPlayerButton.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 18));

            HBox playerMode = new HBox(10, singlePlayerButton, multiPlayerButton);
            playerMode.setAlignment(Pos.CENTER);

            twoPlayerButton = new Button("2-Players");
            twoPlayerButton.setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, new CornerRadii(20), Insets.EMPTY)));
            twoPlayerButton.setPrefSize(75, 25);
            twoPlayerButton.setTextFill(Color.INDIGO);
            twoPlayerButton.setFont(Font.font("verdana", FontWeight.BOLD, 11));

            threePlayerButton = new Button( "3-Players");
            threePlayerButton.setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, new CornerRadii(20), Insets.EMPTY)));
            threePlayerButton.setPrefSize(75, 25);
            threePlayerButton.setTextFill(Color.INDIGO);
            threePlayerButton.setFont(Font.font("verdana", FontWeight.BOLD, 11));

            fourPlayerButton = new Button("4-Players");
            fourPlayerButton.setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, new CornerRadii(20), Insets.EMPTY)));
            fourPlayerButton.setPrefSize(75, 25);
            fourPlayerButton.setTextFill(Color.INDIGO);
            fourPlayerButton.setFont(Font.font("verdana", FontWeight.BOLD, 11));

            startButton = new Button("START");
            startButton.setPrefSize(150, 80);
            startButton.setBackground(buttonBackground);
            startButton.setTextFill(Color.MIDNIGHTBLUE);
            startButton.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 19));
            startButton.setTextAlignment(TextAlignment.CENTER);

            HBox numPlayersBox = new HBox(8, twoPlayerButton, threePlayerButton, fourPlayerButton);
            numPlayersBox.setAlignment(Pos.CENTER);
            optionsBox = new VBox(15, header, playerMode, numPlayersBox, startButton);
            optionsBox.setAlignment(Pos.CENTER);
            startPane.setCenter(optionsBox);
            scene = new Scene(startPane, 500, 500);
        }
    }

    class GameScene{
       // private Button A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
        private ArrayList<Button> keyboard = new ArrayList<>();
        private HBox row1 = new HBox(10);
        private HBox row2 = new HBox(10);
        private HBox row3 = new HBox(10);

        public GameScene(){
            for (int i = 0; i < 26; i++){
                char letter = (char)(65 + i);
                Button k = new Button(letter + "");
                keyboard.add(k);
                k.setOnAction(sendLetter);
                if (i < 9){
                    row1.getChildren().add(k);
                }
                else if(i < 18){
                    row2.getChildren().add(k);
                }
                else {
                    row3.getChildren().add(k);
                }
            }
        }

        EventHandler<ActionEvent> sendLetter = event -> {
            Button b = (Button) event.getSource();
            String s = b.getText();
            b.setDisable(true);
            //  keyboard.getIndexOf();
            // send s;
        };
    }

    class EndScene{

    }

    // ********************  End Nested GUI Classes ********************** //
    // ******************************************************************* //
}