package murray.csc325sprint1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for CAK Catering
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize Firebase service
        FirebaseService.getInstance().initializeFirebase();

        // Set up menu items in Firebase if they don't exist
        FirebaseService.getInstance().setupMenuItems();

        // Load the main view
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/OrderView.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        primaryStage.setTitle("CAK Catering");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}