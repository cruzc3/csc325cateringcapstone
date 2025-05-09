package murray.csc325sprint1.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.EmployeeSupport;
import murray.csc325sprint1.Model.SupportFirestoreFunctions;

import java.io.IOException;
import java.util.Random;

public class customerContactController {
    @FXML private TextArea EnterMessage;
    @FXML private TextField EnterSubject;
    @FXML private TextField EnterUsernameTF;
    @FXML private Button sendButton;
    @FXML private Button viewRequest;
    @FXML private Label yourUsernameLabel;

    private final SupportFirestoreFunctions firestore = SupportFirestoreFunctions.getInstance();
    private int currentTicketID = -1;

    @FXML
    void handleSendButton(ActionEvent event) {
        String username = EnterUsernameTF.getText().trim();
        String subject = EnterSubject.getText().trim();
        String message = EnterMessage.getText().trim();

        if (username.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        currentTicketID = generateTicketID();
        EmployeeSupport ticket = new EmployeeSupport(
                currentTicketID,
                username,
                subject,
                message,
                false,
                ""
        );

        firestore.insertTicket(ticket);
        showAlert("Success", "Ticket #" + currentTicketID + " created successfully!");
        clearFields();
    }

    @FXML
    void handleViewRequest(ActionEvent event) {
        if (currentTicketID == -1) {
            showAlert("Error", "No ticket has been created yet!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/customer-view-request.fxml"));
            Parent root = loader.load();

            customerViewRequestController controller = loader.getController();
            controller.initData(firestore.getTicket(currentTicketID));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open view request window");
        }
    }

    private int generateTicketID() {
        Random rand = new Random();
        return 100000 + rand.nextInt(900000);
    }

    private void clearFields() {
        EnterSubject.clear();
        EnterMessage.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}