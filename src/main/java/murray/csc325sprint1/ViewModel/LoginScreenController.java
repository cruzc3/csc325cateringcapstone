package murray.csc325sprint1.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;


public class LoginScreenController {

    private static InitScreenController instance = InitScreenController.getInstance();
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private GridPane signInGridPane;

    @FXML
    private TextField usernameTextField;

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

    }


}
