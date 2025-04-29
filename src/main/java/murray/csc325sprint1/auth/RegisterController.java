package murray.csc325sprint1.auth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import murray.csc325sprint1.MainApp;

import java.util.concurrent.ExecutionException;

public class RegisterController {
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField fullNameField;
    @FXML private ChoiceBox<String> userTypeChoiceBox;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        userTypeChoiceBox.getItems().addAll("Customer", "Employee");
        userTypeChoiceBox.setValue("Customer");
    }

    @FXML
    private void handleRegister() {
        if (!validateInputs()) return;

        try {
            UserRecord userRecord = authService.register(
                    emailField.getText().trim(),
                    passwordField.getText().trim(),
                    usernameField.getText().trim(),
                    fullNameField.getText().trim(),
                    userTypeChoiceBox.getValue()
            );

            showAlert("Success", "Registration successful!");
            MainApp.setRoot("views/login");

        } catch (FirebaseAuthException | ExecutionException | InterruptedException e) {
            showAlert("Error", "Registration failed: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (emailField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty() ||
                fullNameField.getText().isEmpty()) {
            showAlert("Error", "All fields are required");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "Passwords don't match");
            return false;
        }

        if (passwordField.getText().length() < 6) {
            showAlert("Error", "Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    @FXML
    private void handleLoginRedirect() {
        MainApp.setRoot("views/login");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}