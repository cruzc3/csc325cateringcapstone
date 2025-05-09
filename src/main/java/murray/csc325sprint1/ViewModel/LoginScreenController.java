package murray.csc325sprint1.ViewModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.UserFirestoreFunctions;

import java.io.IOException;


public class LoginScreenController {

    private static InitScreenController instance = InitScreenController.getInstance();
    private static UserFirestoreFunctions instanceOfUserFirestore = UserFirestoreFunctions.getInstance();
    @FXML
    private TextField emailTF;

    @FXML
    private ImageView eyeIV;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private StackPane passwordSP;

    @FXML
    private TextField passwordTF;

    @FXML
    private GridPane signInGridPane;

    @FXML
    private Label loginLbl;

    @FXML
    public void initialize() {
        passwordTF.textProperty().bindBidirectional(passwordPF.textProperty());
        Platform.runLater(() -> {
            Stage stage = (Stage) signInGridPane.getScene().getWindow();

            signInGridPane.applyCss();  // Ensure styles are applied
            signInGridPane.layout();    // Force layout pass

            double prefWidth = signInGridPane.prefWidth(-1);
            double prefHeight = signInGridPane.prefHeight(-1);

            stage.setWidth(prefWidth);
            stage.setHeight(prefHeight + 20);
            stage.centerOnScreen();     // Center the stage
        });
    }

    @FXML
    void eyeBtnClicked(ActionEvent event) {
        boolean isVisible = passwordTF.isVisible();
        if (isVisible) {
            // Switch to PasswordField (hide password)
            eyeIV.setImage(new Image(getClass().getResource("/images/closedEye.png").toExternalForm()));
            passwordTF.setVisible(false);
            passwordTF.setManaged(false);
            passwordPF.setVisible(true);
            passwordPF.setManaged(true);
            passwordPF.setText(passwordTF.getText());
        } else {
            // Switch to TextField (show password)
            eyeIV.setImage(new Image(getClass().getResource("/images/openEye.png").toExternalForm()));
            passwordPF.setVisible(false);
            passwordPF.setManaged(false);
            passwordTF.setVisible(true);
            passwordTF.setManaged(true);
            passwordTF.setText(passwordPF.getText());
        }
    }

    @FXML
    void ForgotPasswordHyperlink(ActionEvent event) {
        try {
            AnchorPane forgotPasswordPane = FXMLLoader.load(getClass().getResource(murray.csc325sprint1.Model.ViewPaths.FORGOT_PASSWORD_SCREEN));
            instance.clearContent();
            instance.initScreenBorderPane.setCenter(forgotPasswordPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void createAnAccountHyperlink(ActionEvent event) {
        try {
            GridPane signUpGridPane = FXMLLoader.load(getClass().getResource(murray.csc325sprint1.Model.ViewPaths.CREATE_USER_SCREEN));
            instance.clearContent();
            instance.initScreenBorderPane.setCenter(signUpGridPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loginBtnClicked(ActionEvent event) {
        try{
            String email = emailTF.getText().trim();
            String password = passwordPF.isVisible() ? passwordPF.getText().trim() : passwordTF.getText().trim();
            if (email.isEmpty() || password.isEmpty()) {
                loginLbl.setText("Please enter both email and password.");
                return;
            }
            boolean isValid = instanceOfUserFirestore.verifyLogin(email, password);
            if (isValid) {
                loginLbl.setText("Login successful!");
//                [Change this to whatever container holds the next screen] AfterLoginPane = FXMLLoader.load(getClass().getResource());
                instance.clearContent();
//                instance.initScreenBorderPane.setCenter(AfterLoginPane);
            } else {
                loginLbl.setText("Invalid email or password.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
