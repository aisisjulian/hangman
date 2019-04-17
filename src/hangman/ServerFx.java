package hangman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.ServerSocket;

public class ServerFx extends Application {

    private Server server;
    private ServerSocket ss;

    private Scene serverScene;



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
