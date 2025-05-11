package murray.csc325sprint1.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.UserFirestoreFunctions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static murray.csc325sprint1.Model.ViewPaths.EMP_MAIN;
import static murray.csc325sprint1.Model.ViewPaths.PROFILE_MAIN;

public class AdminPageController {
    private static InitScreenController instance = InitScreenController.getInstance();
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
        Parent nextPane;
        nextPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(EMP_MAIN)));
        System.out.println(UserFirestoreFunctions.getCurrentUser().isEmployee());
        instance.clearContent();
        instance.initScreenBorderPane.setCenter(nextPane);
    }

    @FXML
    void deleteUserBtnClicked(ActionEvent event) {
        if (selectedUser != null) {
            instanceOfUserFirestore.deleteUser(selectedUser);
            usersVBox.getChildren().remove(selectedCard);
            selectedUser = null;
            selectedCard = null;
        } else {
            showAlert("No User Selected", "Please select a user to delete.");
        }
    }

    @FXML
    void demoteUserBtnClicked(ActionEvent event) {
        if (selectedUser != null) {
            User currentAdmin = instanceOfUserFirestore.getCurrentUser();
            instanceOfUserFirestore.demoteToCustomer(selectedUser);
        } else {
            showAlert("No User Selected", "Please select a user to demote.");
        }
    }

    @FXML
    void findAllEmployeesBtnClicked(ActionEvent event) {
        usersVBox.getChildren().clear();
        List<User> employees = UserFirestoreFunctions.getInstance().getAllEmployees();
        displayUsers(employees, usersVBox);
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
