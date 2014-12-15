package by.bsuir.client.nazarchuk;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseClient extends Application {
    
    @Override
    public void start(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("view/fxml/entry.fxml"));
        } catch (IOException ex) {
            System.err.println("IOException : " + ex);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
