package murray.csc325sprint1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.ViewPaths;

import java.io.IOException;

/**
 * Main application class for Jack's Catering
 */
public class MainApp extends Application {

    // These will be created as needed by controllers
    private static Menu menu;
    private static OrderService orderService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the initial screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewPaths.INIT_SCREEN));
            Parent root = fxmlLoader.load();

            // Set up the scene
            Scene scene = new Scene(root);

            // Configure the stage
            primaryStage.setTitle("Jack's Catering");
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
     * Get the menu instance
     *
     * @return The menu
     */
    public static Menu getMenu() {
        if (menu == null) {
            menu = new Menu();
        }
        return menu;
    }

    /**
     * Get the order service instance
     *
     * @return The order service
     */
    public static OrderService getOrderService() {
        if (orderService == null) {
            orderService = new OrderService();
        }
        return orderService;
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