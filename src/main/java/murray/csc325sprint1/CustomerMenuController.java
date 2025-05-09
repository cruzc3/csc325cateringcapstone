package murray.csc325sprint1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMenuController implements Initializable {

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the Quote button click event
        cusQuote.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/QuoteView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) cusQuote.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading quote view: " + e.getMessage());
            }
        });

        // Initialize the Order button click event
        cusOrder.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/OrderView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) cusOrder.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading order view: " + e.getMessage());
            }
        });

        // Initialize log out functionality
        cusLogOut.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/initial-screen.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) cusLogOut.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error logging out: " + e.getMessage());
            }
        });
    }

    @FXML
    public void initialize() {

        // Set up button event handlers
        cusMenu.setOnAction(this::showCateringMenu);
        cusOrder.setOnAction(this::placeOrder);
        cusQuote.setOnAction(this::getQuote);
        cusContact.setOnAction(this::showContact);
        cusOrderHistory.setOnAction(this::showOrderHistory);
        cusLogOut.setOnAction(this::logOut);
    }

    /**
     * Show the catering menu (read-only version)
     */
    private void showCateringMenu(ActionEvent event) {
        try {
            // Get the stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Load the catering menu view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/catering-menu-view.fxml"));
            Parent root = loader.load();

            // Create the scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load catering menu view");
            e.printStackTrace();
        }
    }

    /**
     * Navigate to order placement screen
     */
    private void placeOrder(ActionEvent event) {
        try {
            // Get the stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Load the order view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/OrderView.fxml"));
            Parent root = loader.load();

            // Create the scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load order view");
            e.printStackTrace();
        }
    }

    // Placeholder methods for other buttons
    private void getQuote(ActionEvent event) {
        // This would be implemented to show the quote form
        System.out.println("Get Quote button clicked");
    }

    private void showContact(ActionEvent event) {
        // This would be implemented to show contact information
        System.out.println("Contact button clicked");
    }

    private void showOrderHistory(ActionEvent event) {
        // This would be implemented to show order history
        System.out.println("Order History button clicked");
    }

    private void logOut(ActionEvent event) {
        try {
            // Get the stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource(murray.csc325sprint1.Model.ViewPaths.INIT_SCREEN));
            Parent root = loader.load();

            // Create the scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load login screen");
            e.printStackTrace();
        }
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


    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}