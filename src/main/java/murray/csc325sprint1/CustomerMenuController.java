package murray.csc325sprint1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    private void initialize() {
        // Set up event handler for contact button
        cusContact.setOnAction(event -> handleCusContact());
    }

    private void handleCusContact() {
        try {
            // Load the customer contact FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/customer-contact.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) cusContact.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Support");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading customer contact screen: " + e.getMessage());
        }
    }

    // You can add similar methods for other buttons as needed
    // private void handleCusOrder() { ... }
    // private void handleCusLogOut() { ... }
}