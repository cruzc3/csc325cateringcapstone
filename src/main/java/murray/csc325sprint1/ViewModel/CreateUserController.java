package murray.csc325sprint1.ViewModel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.SecurityQuestion;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.UserFirestoreFunctions;

import java.io.IOException;

import static murray.csc325sprint1.Model.ViewPaths.LOGIN_SCREEN;
import static murray.csc325sprint1.Model.ViewPaths.TERMS_SCREEN;

public class CreateUserController {

    private static InitScreenController instance = InitScreenController.getInstance();
    private static UserFirestoreFunctions instanceOfUserFirestore = UserFirestoreFunctions.getInstance();

    @FXML
    private CheckBox agreeCB;

    @FXML
    private CheckBox isEmployeeCB; // New checkbox for employee status

    @FXML
    private TextField cPasswordTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField fNameTF;

    @FXML
    private TextField lNameTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField securityAnswerTF;

    @FXML
    private ComboBox<String> securityQuestionCB;

    @FXML
    private GridPane rootPane;

    private CreateUserController createUserController;

    @FXML
    public void initialize() {
        securityQuestionCB.setItems(
                FXCollections.observableArrayList(SecurityQuestion.getAllQuestions())
        );
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();

            // Ensure the stage resizes to fit the preferred size of the content
            rootPane.applyCss();  // Ensures styles are applied before layout pass
            rootPane.layout();    // Forces layout pass to calculate preferred size

            double prefWidth = rootPane.prefWidth(-1);
            double prefHeight = rootPane.prefHeight(-1);

            stage.setWidth(prefWidth);
            stage.setHeight(prefHeight);
            Platform.runLater(stage::centerOnScreen);
        });
    }

    public void setCreateUserController(CreateUserController controller) {
        this.createUserController = controller;
    }

    public void userAgreedToTerms() {
        agreeCB.setSelected(true);
        registerBtn.setDisable(false); // Optional
    }

    //This needs to be fixed
    @FXML
    void backBtnClciked(ActionEvent event) {
        try {
            GridPane signInGridPane = FXMLLoader.load(getClass().getResource(LOGIN_SCREEN));
            instance.clearContent();
            instance.initScreenBorderPane.setCenter(signInGridPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void registerBtnClicked(ActionEvent event) {
        if (fNameTF.getText().trim().isEmpty() ||
                lNameTF.getText().trim().isEmpty() ||
                emailTF.getText().trim().isEmpty() ||
                passwordTF.getText().trim().isEmpty() ||
                cPasswordTF.getText().trim().isEmpty() ||
                securityAnswerTF.getText().trim().isEmpty() ||
                securityQuestionCB.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields before continuing.");
            alert.showAndWait();
        } else {
            try {
                String password = passwordTF.getText().trim();
                String confirmPassword = cPasswordTF.getText().trim();
                if (!password.equals(confirmPassword)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Password Mismatch");
                    alert.setHeaderText(null);
                    alert.setContentText("Passwords do not match. Please try again.");
                    alert.showAndWait();
                    return;
                }
                if (!agreeCB.isSelected()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Terms Not Accepted");
                    alert.setHeaderText(null);
                    alert.setContentText("You must agree to the terms and conditions to register.");
                    alert.showAndWait();
                    return;
                }

                // Create new user with isEmployee checkbox value
                User u = new User(
                        fNameTF.getText().trim(),
                        lNameTF.getText().trim(),
                        emailTF.getText().trim(),
                        securityQuestionCB.getValue(),
                        securityAnswerTF.getText().trim(),
                        passwordTF.getText().trim()
                );

                // Set employee status based on checkbox
                if (isEmployeeCB != null && isEmployeeCB.isSelected()) {
                    u.setEmployee(true);
                }

                instanceOfUserFirestore.insertUser(u);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Account created successfully. You can now log in.");
                alert.showAndWait();

                GridPane signInGridPane = FXMLLoader.load(getClass().getResource(LOGIN_SCREEN));
                instance.clearContent();
                instance.initScreenBorderPane.setCenter(signInGridPane);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred during registration: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void termsAndConditionHyperlinkClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TERMS_SCREEN));
            BorderPane termsPane = loader.load();
            TermsAndConditionsController controller = loader.getController();
            controller.setCreateUserController(this);
            controller.agreeBtn.setDisable(true);
            controller.conditionsSP.vvalueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() >= 1.0) {
                    controller.agreeBtn.setDisable(false);
                }
            });
            instance.initScreenBorderPane.setRight(termsPane);
            instance.initScreenBorderPane.setPrefWidth(instance.initScreenBorderPane.getWidth() + termsPane.getWidth());
            instance.initScreenBorderPane.setPrefHeight(Math.max(instance.initScreenBorderPane.getHeight(), termsPane.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}