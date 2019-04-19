package hangman;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.ServerSocket;

public class ServerFx extends Application {

    private Server server;
    private ServerSocket ss;

    private serverDisplay serverScene;



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Space-Man Server");
        serverScene = new serverDisplay();
        primaryStage.setScene(serverScene.scene);
        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
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
        private TextArea portInput;
        private Button serverOn, serverOff;

        serverDisplay(){
            this.serverPane = new BorderPane();
            this.serverPane.setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            this.serverPane.setPrefSize(500, 400);

            header = new Label("WELCOME TO SPACEMAN");
            header.setTextFill(Color.WHITE);
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

            portInput = new TextArea();
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

