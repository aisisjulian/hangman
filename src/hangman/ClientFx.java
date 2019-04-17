package hangman;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
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

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    class StartScene{
        Scene scene;
        BorderPane startPane;
        Image startBackgroundImage;
        Background startBackground;
        Button singlePlayerButton, multiPlayerButton, twoPlayerButton, threePlayerButton, fourPlayerButton;
        Button connect, start;
        TextArea ipInput, portInput;
        Label header, ipLabel, portLabel, numPlayersLabel;

        public StartScene(){
            startPane = new BorderPane();
            startBackgroundImage  = new Image("startScene.jpg");
            startBackground = new Background(new BackgroundFill(new ImagePattern(startBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            startPane.setBackground(startBackground);

            singlePlayerButton = new Button(" Single-Player");
            multiPlayerButton = new Button("Multi-Player");
            twoPlayerButton = new Button("2-Player");
            threePlayerButton = new Button( "3-Player");
            fourPlayerButton = new Button("4-Player");

            header = new Label("Welcome :-) Connect to Server....");




        }
    }

    class GameScene{

    }

    class EndScene{

    }
}



