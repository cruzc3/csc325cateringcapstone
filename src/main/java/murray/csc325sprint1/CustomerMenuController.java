package murray.csc325sprint1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}