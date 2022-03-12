package hangman;

import javafx.scene.layout.StackPane;
import org.json.JSONException;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public  class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            //GameController.Start();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("GameFile.fxml"));
            //Scene scene = new Scene(root, 1280, 780); width - height
            Scene scene = new Scene(root, 1080, 580);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Medialab Hangman");
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, JSONException, InvalidCountException, InvalidRangeException, UnbalancedException, UndersizeException {
        launch(args);
        //GameController.Start();
    }
}