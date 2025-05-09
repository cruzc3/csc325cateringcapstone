package murray.csc325sprint1.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import murray.csc325sprint1.Model.EmployeeSupport;
import murray.csc325sprint1.Model.SupportFirestoreFunctions;

import java.io.IOException;

public class empContactController {
    @FXML private TableView<EmployeeSupport> requestsTable;
    @FXML private TableColumn<EmployeeSupport, String> userColumn;
    @FXML private TableColumn<EmployeeSupport, String> subjectColumn;
    @FXML private TableColumn<EmployeeSupport, Integer> TicketIDColumn;
    @FXML private TableColumn<EmployeeSupport, String> statusColumn;
    @FXML private TableColumn<EmployeeSupport, Void> actionColumn;
    @FXML private Button empGoBackBtn;

    private final SupportFirestoreFunctions firestore = SupportFirestoreFunctions.getInstance();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadTickets();
    }

    private void setupTableColumns() {
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        TicketIDColumn.setCellValueFactory(new PropertyValueFactory<>("ticketID"));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isClosed() ? "Closed" : "Open"));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button openButton = new Button("Open");

            {
                openButton.setOnAction(event -> {
                    EmployeeSupport ticket = getTableView().getItems().get(getIndex());
                    openReplyWindow(ticket);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(openButton);
                    EmployeeSupport ticket = getTableView().getItems().get(getIndex());
                    openButton.setDisable(ticket.isClosed());
                }
            }
        });
    }

    private void openReplyWindow(EmployeeSupport ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/emp-reply.fxml"));
            Parent root = loader.load();

            empReplyController controller = loader.getController();
            controller.initData(ticket);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open reply window");
        }
    }

    private void loadTickets() {
        ObservableList<EmployeeSupport> tickets = FXCollections.observableArrayList(
                firestore.getAllTickets()
        );
        requestsTable.setItems(tickets);
    }

    @FXML
    private void handleEmpGoBackBtnClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/emp-main.fxml"));
            Parent root = loader.load();
            Stage currStage = (Stage) empGoBackBtn.getScene().getWindow();
            currStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load employee main screen");
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