package murray.csc325sprint1.ViewModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.EmployeeSupport;

import java.net.URL;
import java.util.ResourceBundle;

public class customerViewRequestController implements Initializable {
    @FXML private TextArea SubjectTA;
    @FXML private TextArea TicketIDTA;
    @FXML private TextArea usernameTA;
    @FXML private TextArea statusTA;
    @FXML private TextArea customerConcernTA;
    @FXML private TextArea empResponseTA;
    @FXML private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization handled by initData
    }

    public void initData(EmployeeSupport ticket) {
        if (ticket != null) {
            SubjectTA.setText(ticket.getSubject());
            TicketIDTA.setText(String.valueOf(ticket.getTicketID()));
            usernameTA.setText(ticket.getUser());
            statusTA.setText(ticket.isClosed() ? "Closed" : "Open");
            customerConcernTA.setText(ticket.getCusmsg());
            empResponseTA.setText(ticket.getResponse() != null ? ticket.getResponse() : "No response yet");
        }
    }

    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}