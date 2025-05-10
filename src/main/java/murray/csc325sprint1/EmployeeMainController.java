package murray.csc325sprint1;

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
import java.net.URL;
import java.util.ResourceBundle;


public class EmployeeMainController implements Initializable {

    @FXML
    private Button EmpContact;

    @FXML
    private Button EmpInventory;

    @FXML
    private Button EmpLogOut;

    @FXML
    private Button EmpOrders;

    @FXML
    private Button EmpSchedule;

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

        // Initialize contact button functionality
        EmpContact.setOnAction(event -> ContactBtnClicked());

        // Initialize logout button functionality
        EmpLogOut.setOnAction(event -> {
            try {
                // Clear current user
                Util.setCurrentUser(null);

                // Return to login screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.INIT_SCREEN));
                Parent root = loader.load();
                Stage stage = (Stage) EmpLogOut.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error logging out: " + e.getMessage());
            }
        });

        // Set up order management button
        EmpOrders.setOnAction(event -> {
            try {
                // Load the order list view (can be the same as customer view but with more controls)
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.ORDER_LIST_VIEW_SCREEN));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage stage = (Stage) EmpOrders.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading orders view: " + e.getMessage());
            }
        });

        // Add additional button handlers as needed
        EmpInventory.setOnAction(event -> {
            showNotImplementedMessage();
        });

        EmpSchedule.setOnAction(event -> {
            showNotImplementedMessage();
        });
    }

    private void ContactBtnClicked() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewPaths.EMPLOYEE_CONTACT_SCREEN));
            Parent root = fxmlLoader.load();

            // Use existing stage instead of creating a new one
            Stage stage = (Stage) EmpContact.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
            showError("Error loading contact screen: " + e.getMessage());
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

    /**
     * Show a message for features not yet implemented
     */
    private void showNotImplementedMessage() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Not Implemented");
        alert.setHeaderText(null);
        alert.setContentText("This feature is not yet implemented in the current version.");
        alert.showAndWait();
    }
}