package murray.csc325sprint1.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoggedInViewController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("You are now logged in!");
    }
}
