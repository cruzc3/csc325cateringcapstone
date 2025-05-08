package murray.csc325sprint1.ViewModel;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.EmployeeSupport;
import murray.csc325sprint1.Model.SupportFirestoreFunctions;

import java.io.IOException;

public class empContactController {

    @FXML
    private Button SuppReplyBtn;

    @FXML
    private TableColumn<EmployeeSupport, String> TicketIDColumn;

    @FXML
    private Button empGoBackBtn;

    @FXML
    private TableView<EmployeeSupport> requestsTable;

    @FXML
    private TableColumn<EmployeeSupport, String> subjectColumn;

    @FXML
    private TableColumn<EmployeeSupport, String> userColumn;

    private SupportFirestoreFunctions firestoreFunctions;

    @FXML
    private void initialize() {
        firestoreFunctions = SupportFirestoreFunctions.getInstance();
        TicketIDColumn.setCellValueFactory(new PropertyValueFactory<>("ticketID"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        loadTickets();
    }
    private void loadTickets() {
        requestsTable.getItems().clear();
        requestsTable.getItems().addAll(firestoreFunctions.getAllTickets());
    }

    @FXML
    private void handleSuppReplyBtnClicked() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/emp-reply.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEmpGoBackBtnClicked() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/emp-main.fxml"));
            Parent root = loader.load();
            Stage currStage = (Stage) empGoBackBtn.getScene().getWindow();
            currStage.close();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Failed to load emp main screen");
        }

    }

}


