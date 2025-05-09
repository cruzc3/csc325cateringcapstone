package murray.csc325sprint1.ViewModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.UserFirestoreFunctions;
import murray.csc325sprint1.Model.ViewPaths;

import java.io.IOException;
import java.util.Objects;

public class ResetScreenController {

    private User u;
    private final UserFirestoreFunctions uFF = UserFirestoreFunctions.getInstance();
    InitScreenController instance = InitScreenController.getInstance();
    @FXML
    private TextField answerTF;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private TextField emailTF;

    @FXML
    private HBox emailUserTF;

    @FXML
    private TextField nPasswordTF;

    @FXML
    private TextField securityQuestionTF;

    @FXML
    private AnchorPane forgotPasswordAnchorPane;

    @FXML
    void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) forgotPasswordAnchorPane.getScene().getWindow();

            forgotPasswordAnchorPane.applyCss();  // Ensure styles are applied
            forgotPasswordAnchorPane.layout();    // Force layout pass

            double prefWidth = forgotPasswordAnchorPane.prefWidth(-1);
            double prefHeight = forgotPasswordAnchorPane.prefHeight(-1);

            stage.setWidth(prefWidth);
            stage.setHeight(prefHeight + 20);
            stage.centerOnScreen();     // Center the stage
        });
    }
    private void clearTF(){
        confirmBtn.setDisable(true);
        emailTF.clear();
        securityQuestionTF.clear();
        nPasswordTF.clear();
    }

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        try {
            clearTF();
            instance.clearContent();
            GridPane signInGridPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(ViewPaths.LOGIN_SCREEN)));
            instance.initScreenBorderPane.setCenter(signInGridPane);

            Platform.runLater(() -> {
                Stage stage = (Stage) instance.initScreenBorderPane.getScene().getWindow();

                signInGridPane.applyCss();   // Ensure styles are applied
                signInGridPane.layout();     // Force layout pass

                double prefWidth = signInGridPane.prefWidth(-1);
                double prefHeight = signInGridPane.prefHeight(-1);

                stage.setWidth(prefWidth);
                stage.setHeight(prefHeight + 40);
                stage.centerOnScreen();
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void confirmBtnClicked(ActionEvent event) {
        boolean success = false;
        if (u != null && u.getSecAnswer().equalsIgnoreCase(answerTF.getText().trim())) {
            u.setPassword(nPasswordTF.getText().trim());
            uFF.updateUser(u);
            success = true;
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            if (success) {
                a.setHeaderText("Success");
                a.setContentText("Password updated successfully.");
            } else {
                answerTF.clear();
                a.setHeaderText("Update Failed");
                a.setContentText("Could not update password. Try again.");
            }
            a.show();
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Incorrect answer or user not found");
            a.setContentText("Please check your input and try again.");
            a.show();
        }
    }

    @FXML
    void findBtnClicked(ActionEvent event) {
        String emailString = emailTF.getText().trim();
        this.u = uFF.findUser(emailString);
        if (u != null && u.getEmail().equalsIgnoreCase(emailString)) {
            securityQuestionTF.setText(u.getSecQuestion());
            confirmBtn.setDisable(false);
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Cannot locate user in database");
            a.setContentText("User not found.");
            a.show();
        }

    }
}
