package murray.csc325sprint1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerMenuController {

    @FXML
    private Button cusContact;

    @FXML
    private Button cusLogOut;

    @FXML
    private Button cusMenu;

    @FXML
    private Button cusOrder;

    @FXML
    private Button cusOrderHistory;

    @FXML
    private Button cusQuote;

    @FXML
    public void initialize() {
        // Set up event handlers for buttons
        setupEventHandlers();
    }

    /**
     * Set up event handlers for all buttons
     */
    private void setupEventHandlers() {
        cusOrder.setOnAction(event -> handleOrderButtonClick());
        cusOrderHistory.setOnAction(event -> handleOrderHistoryButtonClick());
        cusMenu.setOnAction(event -> handleMenuButtonClick());
        cusQuote.setOnAction(event -> handleQuoteButtonClick());
        cusContact.setOnAction(event -> handleContactButtonClick());
        cusLogOut.setOnAction(event -> handleLogOutButtonClick());
    }

    /**
     * Handle click on the Order button
     */
    private void handleOrderButtonClick() {
        try {
            // Load the order view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/OrderView.fxml"));
            Parent root = loader.load();

            // Create and show the scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) cusOrder.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load order view: " + e.getMessage());
        }
    }

    /**
     * Handle click on the Order History button
     */
    private void handleOrderHistoryButtonClick() {
        try {
            // Load the order list view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/OrderListView.fxml"));
            Parent root = loader.load();

            // Create and show the scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) cusOrderHistory.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load order history: " + e.getMessage());
        }
    }

    /**
     * Handle click on the Menu button
     */
    private void handleMenuButtonClick() {
        // Placeholder for menu view navigation
        showInfoAlert("Menu view not yet implemented");
    }

    /**
     * Handle click on the Quote button
     */
    private void handleQuoteButtonClick() {
        // Placeholder for quote view navigation
        showInfoAlert("Quote view not yet implemented");
    }

    /**
     * Handle click on the Contact button
     */
    private void handleContactButtonClick() {
        // Placeholder for contact view navigation
        showInfoAlert("Contact view not yet implemented");
    }

    /**
     * Handle click on the Log Out button
     */
    private void handleLogOutButtonClick() {
        try {
            // Navigate back to login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/initial-screen.fxml"));
            Parent root = loader.load();

            // Create and show the scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) cusLogOut.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to logout: " + e.getMessage());
        }
    }

    /**
     * Show an error alert dialog
     */
    private void showErrorAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show an information alert dialog
     */
    private void showInfoAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

