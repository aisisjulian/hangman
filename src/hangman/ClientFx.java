package hangman;

import javafx.application.Application;
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


    //private Scene startScene, gameScene, endScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        StartScene ss = new StartScene();
        primaryStage.setScene(ss.scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    // ******************************************************************* //
    //                          GUI Nested Classes                         //
    // ******************************************************************* //
    class StartScene{
        Scene scene;
        BorderPane startPane;
        Image startBackgroundImage;
        Background startBackground;
        Button singlePlayerButton, multiPlayerButton, twoPlayerButton, threePlayerButton, fourPlayerButton;
        VBox connectionBox;
        VBox optionsBox;
        Button connect, start;
        TextArea ipInput, portInput;
        Label header, ipLabel, portLabel, numPlayersLabel;

        public StartScene(){
            startPane = new BorderPane();
            startBackgroundImage  = new Image("startSceneOpt.png");
            startBackground = new Background(new BackgroundFill(new ImagePattern(startBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            Image buttonBackgroundImage  = new Image("buttonBackground.png");
            Background buttonBackground = new Background(new BackgroundFill(new ImagePattern(buttonBackgroundImage), CornerRadii.EMPTY, Insets.EMPTY));
            startPane.setBackground(startBackground);
            singlePlayerButton = new Button(" Single-Player");
            multiPlayerButton = new Button("Multi-Player");
            twoPlayerButton = new Button("2-Player");
            threePlayerButton = new Button( "3-Player");
            fourPlayerButton = new Button("4-Player");

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

            connect = new Button("CONNECT");
            connect.setPrefSize(250, 80);
            connect.setBackground(buttonBackground);
            connect.setTextFill(Color.NAVY);
            connect.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 22));
            connect.setTextAlignment(TextAlignment.CENTER);
            optionsBox = new VBox(10, header, ipBox, portBox, connect);
            optionsBox.setAlignment(Pos.CENTER);
            startPane.setCenter(optionsBox);

            scene = new Scene(startPane, 500, 500);
        }
    }

    class GameScene{

    }

    class EndScene{

    }

    // ********************  End Nested GUI Classes ********************** //
    // ******************************************************************* //
}



