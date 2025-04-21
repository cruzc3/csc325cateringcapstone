package murray.csc325sprint1.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import murray.csc325sprint1.MainApp;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password");
            return;
        }

        if (authService.login(email, password)) {
            MainApp.setRoot("OrderView.fxml");
        } else {
            showAlert("Login Failed", "Invalid credentials");
        }
    }

    @FXML
    private void handleRegisterRedirect() {
        MainApp.setRoot("register.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}