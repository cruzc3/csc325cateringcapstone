package murray.csc325sprint1.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import murray.csc325sprint1.Model.User;

    public class ResetScreenController {

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
        void initialize(){

        }

        @FXML
        void cancelBtnClicked(ActionEvent event) {

        }

        @FXML
        void confirmBtnClicked(ActionEvent event) {

        }

        @FXML
        void findBtnClicked(ActionEvent event) {
            String emailString = emailTF.getText().trim();
            // User u = some kind of finding method that will search the database for a user

        }

    }
