package murray.csc325sprint1.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import murray.csc325sprint1.FirestoreContext;
import murray.csc325sprint1.Model.OrderListItem;
import murray.csc325sprint1.OrderService;

/**
 * A minimal controller for the order edit dialog
 */
public class OrderEditController {

    @FXML
    private Label orderIdLabel;

    @FXML
    private Button closeButton;

    private OrderService orderService;
    private OrderListController parentController;
    private String orderId;

    /**
     * Initialize the dialog with order data
     */
    public void initData(OrderListItem orderItem, OrderListController controller) {
        this.parentController = controller;
        this.orderService = new OrderService();

        // Get and display the order ID
        this.orderId = orderItem.getOrderId();
        orderIdLabel.setText("Order ID: " + orderId);
    }

    /**
     * Close the dialog
     */
    @FXML
    private void closeDialog(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}