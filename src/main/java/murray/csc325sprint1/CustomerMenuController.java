package murray.csc325sprint1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.Util;
import murray.csc325sprint1.Model.ViewPaths;

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

    @FXML
    private Label welcomeLabel; // Add this if you have a welcome label

    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get the current user from Util
        currentUser = Util.getCurrentUser();

        // Update welcome message if we have a welcome label and a user
        if (welcomeLabel != null && currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getfName() + "!");
        }

        // Initialize the Quote button click event
        cusQuote.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.QUOTE_VIEW_SCREEN));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.ORDER_VIEW_SCREEN));
                Parent root = loader.load();

                // Pass user email to the order controller if needed
                if (loader.getController() instanceof OrderController && currentUser != null) {
                    OrderController controller = loader.getController();
                    controller.setUserEmail(currentUser.getEmail());
                }

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
                // Clear current user
                Util.setCurrentUser(null);

                // Return to login screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.INIT_SCREEN));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.CUSTOMER_CONTACT_SCREEN));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.CATERING_MENU_VIEW_SCREEN));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.ORDER_LIST_VIEW_SCREEN));
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