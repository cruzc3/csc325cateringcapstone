package murray.csc325sprint1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthStarter extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize Firebase
        new FirestoreContext().firebase();

        Parent root = FXMLLoader.load(getClass().getResource("/murray/csc325sprint1/views/login.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Catering App - Authentication");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}