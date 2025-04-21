package murray.csc325sprint1.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import murray.csc325sprint1.MainApp;

public class RegisterController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField fullNameField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String fullName = fullNameField.getText();

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords don't match");
            return;
        }

        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            showAlert("Error", "All fields are required");
            return;
        }

        try {
            if (authService.register(email, password, fullName)) {
                showAlert("Success", "Account created successfully!");
                MainApp.setRoot("login.fxml");
            }
        } catch (Exception e) {
            showAlert("Registration Failed", e.getMessage());
        }
    }

    @FXML
    private void handleLoginRedirect() {
        MainApp.setRoot("login.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}