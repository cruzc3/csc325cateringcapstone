package murray.csc325sprint1.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.UserFirestoreFunctions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static murray.csc325sprint1.Model.ViewPaths.EMP_MAIN;
import static murray.csc325sprint1.Model.ViewPaths.PROFILE_MAIN;

public class AdminPageController {

    private static UserFirestoreFunctions instanceOfUserFirestore = UserFirestoreFunctions.getInstance();
    @FXML
    private AnchorPane adminPane;

    @FXML
    private TextField emailTextArea;

    @FXML
    private ScrollPane userScrollPane;

    @FXML
    private Label selectedUserLbl;

    @FXML
    private VBox usersVBox;
    private AnchorPane selectedCard;
    private User selectedUser;

    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        showAlert("Window Closed", "The window has been closed.");
    }

    @FXML
    void deleteUserBtnClicked(ActionEvent event) {
        if (selectedUser != null) {
            instanceOfUserFirestore.deleteUser(selectedUser);
            usersVBox.getChildren().remove(selectedCard);
            showAlert("User Deleted",  selectedUser.getEmail() + " has been successfully deleted." );
            selectedUser = null;
            selectedCard = null;
        } else {
            showAlert("No User Selected", "Please select a user to delete.");
        }
    }

    @FXML
    void demoteUserBtnClicked(ActionEvent event) {
        if (selectedUser != null) {
            instanceOfUserFirestore.demoteToCustomer(selectedUser);
            showAlert("User Demoted",  selectedUser.getEmail() + " has been successfully demoted." );
        } else {
            showAlert("No User Selected", "Please select a user to demote.");
        }
    }

    @FXML
    void findAllEmployeesBtnClicked(ActionEvent event) {
        usersVBox.getChildren().clear();
        List<User> employees = UserFirestoreFunctions.getInstance().getAllEmployees();
        displayUsers(employees, usersVBox);
        if (employees.isEmpty()) {
            showAlert("No Employees Found", "No employees were found in the system.");
        } else {
            displayUsers(employees, usersVBox);
//            showAlert("Employees Found", employees.size() + " employees were found.");
        }
    }

    @FXML
    void findUserBtnClicked(ActionEvent event) {
        usersVBox.getChildren().clear(); // Clear previous results
        String email = emailTextArea.getText().trim().toLowerCase();
        if (!email.isEmpty()) {
            User user = UserFirestoreFunctions.getInstance().findUser(email);
            if (user != null) {
                displayUsers(List.of(user), usersVBox);
            } else {
                showAlert("User Not Found", "No user with that email exists in the system.");
            }
        } else {
            showAlert("Invalid Input", "Please enter a valid email address.");
        }
    }

    @FXML
    void promoteUserBtnClicked(ActionEvent event) {
        if (selectedUser != null) {
            instanceOfUserFirestore.promoteToEmployee(selectedUser);
            showAlert("User promoted",  selectedUser.getEmail() + " has been successfully promoted." );
        } else {
            showAlert("No User Selected", "Please select a user to promote.");
        }
    }

    public void displayUsers(List<User> users, VBox container) {
        for (User user : users) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(PROFILE_MAIN));
                AnchorPane card = loader.load();
                ProfileCardController controller = loader.getController();
                controller.setUserData(user);

                card.setOnMouseClicked(event -> {
                    selectedUser = user;
                    highlightCard(card);
                });
                container.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void highlightCard(AnchorPane card) {
        if (selectedCard != null) {
            selectedCard.setStyle("");
        }
        selectedUserLbl.setText(selectedUser.getEmail());
        card.setStyle("-fx-background-color: lightblue;");
        selectedCard = card;
    }

}
