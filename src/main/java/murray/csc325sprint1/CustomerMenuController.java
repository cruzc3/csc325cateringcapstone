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
        
        // Initialize contact button with UpdatedMain-employee-customer-Support functionality
        cusContact.setOnAction(event -> {
            try {
                // Load the customer contact FXML file from UpdatedMain branch
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/customer-contact.fxml"));
                Parent root = loader.load();
                
                // Get the current stage
                Stage stage = (Stage) cusContact.getScene().getWindow();
                
                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Customer Support");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading customer contact screen: " + e.getMessage());
            }
        });
        
        // Set up remaining buttons
        cusMenu.setOnAction(this::showCateringMenu);
        cusOrderHistory.setOnAction(this::showOrderHistory);
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
            showError("Failed to load catering menu view: " + e.getMessage());
        }
    }

    /**
     * Show order history
     */
    private void showOrderHistory(ActionEvent event) {
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
            showError("Failed to load order history: " + e.getMessage());
        }
    }

    /**
     * Show an error alert dialog
     */
    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
