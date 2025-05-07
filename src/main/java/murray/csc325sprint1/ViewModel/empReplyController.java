package murray.csc325sprint1.ViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class empReplyController {

    @FXML
    private TableColumn<?, ?> TicketIDColumn;

    @FXML
    private Button empGoBackBtn;

    @FXML
    private TableView<?> requestsTable;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> subjectColumn;

    @FXML
    private TableColumn<?, ?> userColumn;

}

