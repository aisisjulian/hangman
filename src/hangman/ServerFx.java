package hangman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
