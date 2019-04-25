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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    boolean isSinglePlayer = false;

    private int numLives = 5;
    private String word = "welcome";
    private Scene startScene, gameScene, endScene;
    private StartScene ss = new StartScene();
    private GameScene gs = new GameScene();
    private EndScene es = new EndScene();
    private BorderPane startPane, gamePane, endPane;
    private VBox connectionBox;
    private VBox optionsBox;
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: deeppink,  -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;";

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Welcome to Spaceman :-)");
        startScene = ss.scene;
        gameScene = gs.scene;
        primaryStage.setScene(this.startScene);


        // ******************************************************************* //
        //                       START - EVENT HANDLERS                        //
        // ******************************************************************* //

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

        ss.startButton.setOnAction(event->{
            primaryStage.setScene(gameScene);
            ss.startButton.setDisable(true);
        });

        // ******************************************************************* //
        //                       GAME - EVENT HANDLERS                         //
        // ******************************************************************* //




        // ******************************************************************* //
        //                       END - EVENT HANDLERS                          //
        // ******************************************************************* //





        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Client createClient(String IP, int portIn, Stage primaryStage) {
        return new Client(IP, portIn, data -> {
            Platform.runLater(() -> {

                if(data.toString().split(" ")[0].equals("WORD: ")){
                    word = data.toString().split(" ")[1];
                }
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
                        startPane.setCenter(optionsBox);
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
                        ss.connectButton.setText("CONNECT");
                        ss.header.setText("NO CONNECTION");
                        ss.header.setPrefSize(300, 40);
                        ss.header.setAlignment(Pos.CENTER);
                        started = false;
                        primaryStage.setScene(this.startScene);
                        break;
                    case "START":
                        numLives = 5;
                        primaryStage.setScene(this.gameScene);
                }
            });
        });


    }

    // ******************************************************************* //
    //                          GUI Nested Classes                         //
    // ******************************************************************* //
    class StartScene{
        private Scene scene;
        private Image startBackgroundImage;
        private Background startBackground;
        private Button singlePlayerButton, multiPlayerButton, twoPlayerButton, threePlayerButton, fourPlayerButton;
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
            header.setPrefSize(250, 40);
            header.setAlignment(Pos.CENTER);
            portLabel = new Label("PORT :");
            portLabel.setTextFill(Color.WHITE);
            portLabel.setFont(Font.font("Courier", FontWeight.BOLD, 17));
            portLabel.setPrefSize(75, 15);
            portInput = new TextArea();
            portInput.setPrefSize(150, 15);
            portInput.setPadding(new Insets(5, 0, 5, 0));


            ipLabel = new Label(" IP  :");
            ipLabel.setTextFill(Color.WHITE);
            ipLabel.setFont(Font.font("Courier", FontWeight.BOLD, 17));
            ipLabel.setPrefSize(75, 15);
            ipInput = new TextArea();
            ipInput.setPrefSize(150, 15);
            ipInput.setPadding(new Insets(5, 0, 5, 0));

            HBox ipBox = new HBox(ipLabel, ipInput);
            HBox portBox = new HBox(portLabel, portInput);
            ipBox.setAlignment(Pos.CENTER);
            portBox.setAlignment(Pos.CENTER);

            connectButton = new Button("CONNECT");
            connectButton.setPrefSize(175, 80);
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
            singlePlayerButton.setOnMouseClicked(e -> {
                singlePlayerButton.setBackground(new Background(new BackgroundFill(Color.DEEPPINK, new CornerRadii(7), Insets.EMPTY)));
                multiPlayerButton.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(7), Insets.EMPTY)));
            });
            singlePlayerButton.setOnAction(e->{
                twoPlayerButton.setDisable(true); threePlayerButton.setDisable(true); fourPlayerButton.setDisable(true);
            } );

            multiPlayerButton = new Button("Multi-Player");
            multiPlayerButton.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(7), Insets.EMPTY)));
            multiPlayerButton.setPrefSize(150, 40);
            multiPlayerButton.setTextFill(Color.INDIGO);
            multiPlayerButton.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 18));
            multiPlayerButton.setOnMouseClicked(e -> {
                multiPlayerButton.setBackground(new Background(new BackgroundFill(Color.DEEPPINK, new CornerRadii(7), Insets.EMPTY)));
                singlePlayerButton.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(7), Insets.EMPTY)));
            });
            multiPlayerButton.setOnAction(e->{
                isSinglePlayer=false;
                twoPlayerButton.setDisable(false); threePlayerButton.setDisable(false); fourPlayerButton.setDisable(false);
            });

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
            startPane.setCenter(connectionBox);
            scene = new Scene(startPane, 500, 500);
        }
    }

    class GameScene{
       // private Button A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
        private Scene scene;
        private Image gameBackgroundImage;
        private Background gameBackground;

        private ArrayList<Button> keyboard;
        private HBox bottomDisplay; //to display keyboard and letter entered
        private VBox keyboardBox;
        private HBox row1;
        private HBox row2;
        private HBox row3;

        private HBox wordDisplay;

        private VBox bottomBox;

        private VBox enterBox;
        private Label letterChosenLabel;
        private String letter;
        private Button pressed;
        private Button sumbitButton;

        private Label spaceship;
        private ArrayList<Image> ssImageList;

        private Label header;

        public GameScene(){
            gamePane = new BorderPane();
            gameBackgroundImage = new Image("gameScene2.png");
            gameBackground = new Background(new BackgroundFill(new ImagePattern(gameBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            gamePane.setBackground(gameBackground);
            initSpaceshipImages();
            initWordDisplay();
            initKeyboard();
            this.bottomBox = new VBox(10, this.wordDisplay, this.bottomDisplay);
            bottomBox.setAlignment(Pos.CENTER);
            gamePane.setBottom(bottomBox);
            scene = new Scene(gamePane, 800, 600);
        }

        public void initWordDisplay(){
            this.wordDisplay = new HBox(10);
            for(int i = 0; i < word.length(); i++){
                Label character = new Label(word.charAt(i)+ "");
                character.setTextFill(Color.MIDNIGHTBLUE);
                character.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 33));
                character.setAlignment(Pos.CENTER);
                character.setPrefSize(40, 40);
                character.setBackground(new Background(new BackgroundFill(Color.LEMONCHIFFON, new CornerRadii(5), Insets.EMPTY)));
                this.wordDisplay.getChildren().add(character);
            }
            this.wordDisplay.setAlignment(Pos.CENTER);
        }

        public void initSpaceshipImages(){
            this.spaceship = new Label();
            this.spaceship.setAlignment(Pos.BOTTOM_CENTER);
            this.ssImageList = new ArrayList<>();

            for(int i = 0; i < 7; i++){
                Image spaceShipImage = new Image("spaceship" + (i+1) + ".png");
                this.ssImageList.add(spaceShipImage);
            }
            ImageView iv = new ImageView(ssImageList.get(numLives-5));
            iv.setFitWidth(350);
            iv.setFitHeight(350);
            this.spaceship.setGraphic(iv);
            gamePane.setCenter(this.spaceship);
        }

        public void initKeyboard(){
            this.keyboard = new ArrayList<>();
            this.row1 = new HBox(10);
            this.row2 = new HBox(10);
            this.row3 = new HBox(10);

            for (int i = 0; i < 26; i++){
                char letter = (char)(65 + i);
                Button k = new Button("" + letter + "");
                k.setTextAlignment(TextAlignment.CENTER);
                this.keyboard.add(k);
                this.keyboard.get(i).setPrefSize(32, 32);
                k.setOnAction(displayLetter);
                if (i < 9){
                    row1.getChildren().add(k);
                }
                else if(i < 17){
                    row2.getChildren().add(k);
                }
                else {
                    row3.getChildren().add(k);
                }
            }

            row1.setAlignment(Pos.CENTER);
            row2.setAlignment(Pos.CENTER);
            row3.setAlignment(Pos.CENTER);
            keyboardBox = new VBox(10, row1, row2, row3);
            keyboardBox.setAlignment(Pos.CENTER);
            keyboardBox.setPrefSize(410, 150);
            keyboardBox.setPadding(new Insets(3,0,3,0));
            //keyboardBox.setBackground(new Background(new BackgroundFill(new Color(0x19/255.0, 0x19/255.0, 0x70/255.0, .7), new CornerRadii(0), Insets.EMPTY)));
//            gamePane.setBottom(keyboardBox);

            /*dispaly letter entered code*/
            bottomDisplay = new HBox();
            bottomDisplay.setBackground(new Background(new BackgroundFill(new Color(0x19/255.0, 0x19/255.0, 0x70/255.0, .7), new CornerRadii(0), Insets.EMPTY)));
            this.enterBox = new VBox(10);
            letterChosenLabel = new Label();
            letterChosenLabel.setTextFill(Color.GOLD);
            letterChosenLabel.setAlignment(Pos.BASELINE_LEFT);
            letterChosenLabel.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 42));

            sumbitButton = new Button("submit");
            sumbitButton.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 12));
            sumbitButton.setAlignment(Pos.CENTER);
            sumbitButton.setOnAction(sendLetter);
            enterBox.getChildren().addAll(letterChosenLabel, sumbitButton);
            enterBox.setPrefSize(120, 150);
            enterBox.setPadding(new Insets(0, 0,0, 0));

            bottomDisplay.setMaxSize(560, 150);
            this.bottomDisplay.setAlignment(Pos.CENTER);
            this.bottomDisplay.getChildren().addAll(this.enterBox, this.keyboardBox);
            this.enterBox.setAlignment(Pos.CENTER);

        }

        EventHandler<ActionEvent> sendLetter = event -> {
            pressed.setDisable(true);
            if (!(letter.isBlank())) {
                client.send(letter);
                sumbitButton.setDisable(true); /*disable until its that players turn*/
            }
        };

        EventHandler<ActionEvent> displayLetter = event -> {
            int i = this.keyboard.indexOf((Button) event.getSource());
            pressed = this.keyboard.get(i);
            letter = pressed.getText();
            letterChosenLabel.setText(letter);
        };
    }

    class EndScene{
        private Scene scene;
        private Image endBackgroundImage;
        private Background endBackground;

        public EndScene(){
            endPane = new BorderPane();
            endBackgroundImage = new Image("endScene.png");
            endBackground = new Background(new BackgroundFill(new ImagePattern(endBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            endPane.setBackground(endBackground);

            // display letters guessed
            // display the final word
            // display win or lose
            // ask to quit or new game

            scene = new Scene(endPane, 500, 500);
        }


    }

    // ********************  End Nested GUI Classes ********************** //
    // ******************************************************************* //
}