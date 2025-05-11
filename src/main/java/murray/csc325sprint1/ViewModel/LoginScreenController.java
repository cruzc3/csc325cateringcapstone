package murray.csc325sprint1.ViewModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.UserFirestoreFunctions;
import murray.csc325sprint1.Model.Util;

import java.io.IOException;
import java.util.Objects;

import static murray.csc325sprint1.Model.ViewPaths.CUST_MAIN;
import static murray.csc325sprint1.Model.ViewPaths.EMP_MAIN;


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
        instanceOfUserFirestore = UserFirestoreFunctions.getInstance();
        passwordTF.textProperty().bindBidirectional(passwordPF.textProperty());
        Platform.runLater(() -> {
            Stage stage = (Stage) signInGridPane.getScene().getWindow();

            signInGridPane.applyCss();  // Ensure styles are applied
            signInGridPane.layout();    // Force layout pass

            double prefWidth = signInGridPane.prefWidth(-1);
            double prefHeight = signInGridPane.prefHeight(-1);

            stage.setWidth(prefWidth);
            stage.setHeight(prefHeight + 20);
            stage.centerOnScreen();
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
        try {
            String email = emailTF.getText().trim();
            String password = passwordPF.isVisible() ? passwordPF.getText().trim() : passwordTF.getText().trim();
            if (email.isEmpty() || password.isEmpty()) {
                loginLbl.setText("Please enter both email and password.");
                return;
            }
            boolean isValid = instanceOfUserFirestore.verifyLogin(email, password);
            if (isValid) {
                loginLbl.setText("Login successful!");
                try {
                    Parent nextPane;
                    User currentUser = instanceOfUserFirestore.getCurrentUser();
                    currentUser.toString();
                    System.out.println(currentUser.isEmployee());

                    // Check if user is an employee
                    if (currentUser.isEmployee()) {
                        nextPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(EMP_MAIN)));
                        System.out.println("User: " + currentUser);
                        System.out.print("Welcome valued employee");
                    } else {
                        nextPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(CUST_MAIN)));
                    }
                    instance.clearContent();
                    instance.initScreenBorderPane.setCenter(nextPane);
                } catch (Exception e) {
                    // Handle loading pane failure
                    e.printStackTrace();
                    loginLbl.setText("Error loading user menu: " + e.getMessage());
                }
            } else {
                loginLbl.setText("Invalid email or password.");
            }
        } catch (Exception e) {
            // Handle general exceptions
            e.printStackTrace();
            loginLbl.setText("Error during login: " + e.getMessage());
        }
    }
}