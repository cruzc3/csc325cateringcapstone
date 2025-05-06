package murray.csc325sprint1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerContactController {
    @FXML private TextField EnterSubject;
    @FXML private TextArea EnterMessage;

    @FXML
    private void handleSendButton() {
        try {
            SupportRequest request = new SupportRequest(
                    EnterSubject.getText().trim(),
                    EnterMessage.getText().trim()
            );

            supportmain.fstore.collection("SupportRequest")
                    .document(request.getDocId())
                    .set(request)
                    .get(); // Block until complete

            showAlert("Success", "Request sent! ID: " + request.getUserID());
            EnterSubject.clear();
            EnterMessage.clear();
        } catch (Exception e) {
            showAlert("Error", "Failed to send request");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}