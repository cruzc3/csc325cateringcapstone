package murray.csc325sprint1.ViewModel;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.SecurityQuestion;
import murray.csc325sprint1.Model.ViewPaths;

import java.io.IOException;

public class CreateUserController {

    private static InitScreenController instance = InitScreenController.getInstance();
    @FXML
    private CheckBox agreeCB;

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
    private TextField securityAnswerTF;

    @FXML
    private ComboBox<String> securityQuestionCB;

    @FXML
    private TextField userNameTF;

    @FXML
    private Button registerBtn;

    private CreateUserController createUserController;

    @FXML
    public void initialize() {
        securityQuestionCB.setItems(
                FXCollections.observableArrayList(SecurityQuestion.getAllQuestions())
        );
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void registerBtnClicked(ActionEvent event) {
        try {
            GridPane signInGridPane = FXMLLoader.load(getClass().getResource(ViewPaths.CREATE_USER_SCREEN));
            instance.clearContent();
            instance.initScreenBorderPane.setCenter(signInGridPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void termsAndConditionHyperlinkClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.TERMS_SCREEN));
            BorderPane termsPane = loader.load();
            TermsAndConditionsController controller = loader.getController();
            controller.setCreateUserController(this);
            controller.agreeBtn.setDisable(true);
            controller.conditionsSP.vvalueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() >= 1.0) {
                    controller.agreeBtn.setDisable(false);
                }
            });
            instance.clearContent();
            instance.initScreenBorderPane.setCenter(termsPane);
            instance.initScreenBorderPane.setPrefWidth(instance.initScreenBorderPane.getWidth() + termsPane.getWidth());
            instance.initScreenBorderPane.setPrefHeight(Math.max(instance.initScreenBorderPane.getHeight(), termsPane.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }


}
