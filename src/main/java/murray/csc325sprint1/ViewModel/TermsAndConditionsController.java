package murray.csc325sprint1.ViewModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static murray.csc325sprint1.Model.ViewPaths.LOAD_TEXT_TERMS_AND_CONDITIONS;

public class TermsAndConditionsController {

    private static InitScreenController instance = InitScreenController.getInstance();

    @FXML
    private BorderPane tCBP;

    @FXML
    Button agreeBtn;

    @FXML
    private HBox answerBtnContainer;

    @FXML
    ScrollPane conditionsSP;

    @FXML
    private Button disagreeBtn;

    @FXML
    private Text textConditions;

    private Runnable onAgreeCallback;

    private Stage theStage;

    public void setOnAgreeCallback(Runnable callback) {
        this.onAgreeCallback = callback;
    }

    private CreateUserController createUserController;

    double prefWidth;

    double prefHeight;

    public void setCreateUserController(CreateUserController controller) {
        this.createUserController = controller;
    }

    @FXML
    public void initialize() {
        loadTermsText();
        Platform.runLater(() -> {
            Stage stage = (Stage) tCBP.getScene().getWindow();
            if (stage != null) {
        prefWidth = tCBP.prefWidth(-1);
        prefHeight = tCBP.prefHeight(-1);
        stage.setWidth(instance.initScreenBorderPane.getPrefWidth() + prefWidth);
        stage.setHeight(prefHeight + 30);
        stage.centerOnScreen();
                stageIsSet(stage);
            }
        });
        conditionsSP.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > .9) {
                answerBtnContainer.setDisable(false);
                agreeBtn.setDisable(false);
                disagreeBtn.setDisable(false);
            }
        });
    }

    private void stageIsSet(Stage stage){
        this.theStage = stage;
    }

    private void loadTermsText() {
        StringBuilder termsText = new StringBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream(LOAD_TEXT_TERMS_AND_CONDITIONS);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                termsText.append(line).append("\n");
            }
            textConditions.setText(termsText.toString());
        } catch (IOException | NullPointerException e) {
            textConditions.setText("Unable to load Terms and Conditions.");
            e.printStackTrace();
        }
    }

    @FXML
    void agreenTermsAndCondiitonBtnClicked(ActionEvent event) {
        if (createUserController != null) {
            createUserController.userAgreedToTerms();
            instance.initScreenBorderPane.setRight(null);
            theStage.setWidth(instance.initScreenBorderPane.getPrefWidth());
            theStage.setHeight(instance.initScreenBorderPane.getPrefHeight()+20);
            theStage.centerOnScreen();
        }

    }

    @FXML
    void disagreenTermsAndCondiitonBtnClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
