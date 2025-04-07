package murray.csc325sprint1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main application class for CAK Catering
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Initialize Firebase service
            FirebaseService.getInstance().initializeFirebase();

            // Set up menu items in Firebase if they don't exist
            FirebaseService.getInstance().setupMenuItems();

            // Attempt both resource paths to find the correct one
            URL fxmlLocation = getClass().getResource("/fxml/OrderView.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/murray/csc325sprint1/OrderView.fxml");
            }

            if (fxmlLocation == null) {
                System.err.println("Could not find OrderView.fxml. Make sure it's in the right location!");
                throw new IOException("FXML file not found");
            }

            Parent root = FXMLLoader.load(fxmlLocation);

            // Try to find the stylesheet
            String stylesheet = "/styles/Styles.css";
            URL styleUrl = getClass().getResource(stylesheet);
            if (styleUrl == null) {
                stylesheet = "/murray/csc325sprint1/Styles.css";
                styleUrl = getClass().getResource(stylesheet);
            }

            Scene scene = new Scene(root);
            if (styleUrl != null) {
                scene.getStylesheets().add(styleUrl.toExternalForm());
            } else {
                System.err.println("Could not find stylesheet. The app will run but may not look as expected.");
            }

            primaryStage.setTitle("CAK Catering");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();

            System.out.println("Application started successfully!");
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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