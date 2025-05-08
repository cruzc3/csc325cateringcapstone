package murray.csc325sprint1;

import javafx.event.ActionEvent;
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
}

