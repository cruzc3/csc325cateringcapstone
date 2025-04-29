package murray.csc325sprint1.auth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import murray.csc325sprint1.MainApp;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final AccessDataView accessDataView = new AccessDataView();
    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        // Bind view properties to model
        emailField.textProperty().bindBidirectional(accessDataView.emailProperty());
        passwordField.textProperty().bindBidirectional(accessDataView.passwordProperty());
    }

    @FXML
    private void handleLogin() {
        try {
            UserRecord user = authService.login(
                    accessDataView.emailProperty().get(),
                    accessDataView.passwordProperty().get()
            );

            showAlert("Success", "Logged in successfully!");
            MainApp.setRoot("views/LoggedInView");

        } catch (FirebaseAuthException e) {
            showAlert("Login Failed", e.getMessage());
        }
    }

    @FXML
    private void handleRegisterRedirect() {
        MainApp.setRoot("views/register");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}