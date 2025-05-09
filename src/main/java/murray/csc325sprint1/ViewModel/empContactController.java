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
                firestore.getAllTickets() // Already sorted by timestamp (newest first)
        );

        // Sort open tickets to the top (while keeping timestamp order)
        tickets.sort((t1, t2) -> {
            if (t1.isClosed() == t2.isClosed()) {
                return 0; // Keep Firestore's order (newest first)
            }
            return t1.isClosed() ? 1 : -1; // Open tickets come first
        });

        requestsTable.setItems(tickets);
    }

    @FXML
    private void handleEmpGoBackBtnClicked() {
        try {
            Stage currentStage = (Stage) empGoBackBtn.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to go back to the main screen.");
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