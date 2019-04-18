package hangman;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.ServerSocket;

public class ServerFx extends Application {

    private Server server;
    private ServerSocket ss;

    private Scene serverScene;



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Space-Man Server");
        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }

    // ******************************************************************* //
    //                          GUI Nested Classes                         //
    // ******************************************************************* //
    class serverDisplay{

    }

    class clientTableDisplay{

    }

    class clientDisplay{

        HBox clientStats;
        String connected, status, clientIndex;
        Label screenNameLabel, connectedLabel, statusLabel;

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

