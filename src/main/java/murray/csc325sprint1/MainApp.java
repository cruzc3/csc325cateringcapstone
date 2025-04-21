package murray.csc325sprint1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {
    private static Menu menu;
    private static OrderService orderService;
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Initialize Firebase
            new FirestoreContext().firebase();

            // Create menu and initialize menu items
            menu = new Menu();

            // Check if user is logged in (simplified - you'll enhance this)
            if (true) { // Replace with actual auth check
                showLoginScreen(primaryStage);
            } else {
                showMainApplication(primaryStage);
            }

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void showLoginScreen(Stage stage) throws IOException {
        Parent root = loadFXML("login.fxml");
        scene = new Scene(root);
        applyStyles(scene);
        stage.setScene(scene);
        stage.setTitle("CAK Catering - Login");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    private void showMainApplication(Stage stage) throws IOException {
        Parent root = loadFXML("OrderView.fxml");
        scene = new Scene(root);
        applyStyles(scene);
        stage.setTitle("CAK Catering");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    private void applyStyles(Scene scene) {
        URL styleUrl = getClass().getResource("/murray/csc325sprint1/Styles.css");
        if (styleUrl != null) {
            scene.getStylesheets().add(styleUrl.toExternalForm());
        } else {
            System.err.println("Could not find stylesheet");
        }
    }

    public static void setRoot(String fxmlPath) {
        try {
            Parent root = loadFXML(fxmlPath);
            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
            showErrorAlert("Navigation Error", "Failed to load view: " + fxmlPath);
        }
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        String fullPath = "/murray/csc325sprint1/" + fxmlPath;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fullPath));
        return fxmlLoader.load();
    }

    private static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Menu getMenu() {
        if (menu == null) {
            menu = new Menu();
        }
        return menu;
    }

    public static OrderService getOrderService() {
        if (orderService == null) {
            orderService = new OrderService();
        }
        return orderService;
    }

    public static void main(String[] args) {
        launch(args);
    }
}