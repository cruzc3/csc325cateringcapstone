package murray.csc325sprint1.ViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.EmployeeSupport;
import murray.csc325sprint1.Model.SupportFirestoreFunctions;

public class empReplyController {

    @FXML
    private TextArea TicketIDTA;

    @FXML
    private Button backButton;

    @FXML
    private TextArea customerConcernTA;

    @FXML
    private TextField empResponseTF;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea statusTA;

    @FXML
    private TextArea subjectTA;

    @FXML
    private TextArea usernameTA;


    private final SupportFirestoreFunctions firestoreFunctions = SupportFirestoreFunctions.getInstance();

    @FXML
    private void initialize() {
        sendButton.setOnAction(e -> SendButtonClicked());
        backButton.setOnAction(e -> backButtonClicked());
    }

    private void SendButtonClicked() {
        try{
            int ticketID = Integer.parseInt(TicketIDTA.getText().trim());
            String user = usernameTA.getText().trim();
            String status = statusTA.getText().trim();
            String subject = subjectTA.getText().trim();
            String response = empResponseTF.getText().trim();
            String cusmsg = customerConcernTA.getText().trim();

            EmployeeSupport ticket = new EmployeeSupport(ticketID, user, subject, status, response, cusmsg);
            firestoreFunctions.updateResponse(ticketID, response, status);
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close();
        }catch(NumberFormatException e){
            System.err.println("Invalid Ticket");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void backButtonClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

}
