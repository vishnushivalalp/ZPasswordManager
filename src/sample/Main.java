package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNIFIED);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("titlelogo.png")));
        primaryStage.setTitle("Z - Password Manger");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        root.requestFocus();
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
