package murray.csc325sprint1.ViewModel;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import murray.csc325sprint1.FirestoreContext;
import murray.csc325sprint1.MainApp;
import murray.csc325sprint1.Model.OrderListItem;
import murray.csc325sprint1.Model.User;
import murray.csc325sprint1.Model.Util;
import murray.csc325sprint1.Model.ViewPaths;
import murray.csc325sprint1.Order;
import murray.csc325sprint1.OrderService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.scene.Parent;

public class OrderListController implements Initializable {

    @FXML
    private TableView<OrderListItem> ordersTableView;

    @FXML
    private TableColumn<OrderListItem, String> orderIdColumn;

    @FXML
    private TableColumn<OrderListItem, String> pickupDateColumn;

    @FXML
    private TableColumn<OrderListItem, String> pickupTimeColumn;

    @FXML
    private TableColumn<OrderListItem, String> totalColumn;

    @FXML
    private TableColumn<OrderListItem, String> statusColumn;

    @FXML
    private TableColumn<OrderListItem, String> actionsColumn;

    @FXML
    private Label orderStatusLabel;

    @FXML
    private Label noOrdersLabel;

    private OrderService orderService;
    private Firestore db;
    private ObservableList<OrderListItem> ordersList;
    private User currentUser;
    private boolean isEmployeeView = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get instances of services
        orderService = MainApp.getOrderService();
        db = FirestoreContext.getInstance().getFirestore();

        // Get current user
        currentUser = Util.getCurrentUser();

        // Determine if this is employee view
        if (currentUser != null) {
            isEmployeeView = currentUser.isEmployee();
        }

        // Initialize the table columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        pickupDateColumn.setCellValueFactory(new PropertyValueFactory<>("pickupDate"));
        pickupTimeColumn.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Setup the actions column
        setupActionsColumn();

        // Load orders on initialization
        loadOrders();
    }

    private void setupActionsColumn() {
        Callback<TableColumn<OrderListItem, String>, TableCell<OrderListItem, String>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<OrderListItem, String> call(final TableColumn<OrderListItem, String> param) {
                        return new TableCell<>() {
                            private final Button editButton = new Button("Edit");
                            private final Button cancelButton = new Button("Cancel");
                            private final HBox buttonsBox = new HBox(5, editButton, cancelButton);

                            {
                                editButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                                cancelButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white;");

                                editButton.setOnAction(event -> {
                                    OrderListItem order = getTableView().getItems().get(getIndex());
                                    openEditOrderDialog(order);
                                });

                                cancelButton.setOnAction(event -> {
                                    OrderListItem order = getTableView().getItems().get(getIndex());
                                    confirmCancelOrder(order);
                                });
                            }

                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty) {
                                    setGraphic(null);
                                    return;
                                }

                                OrderListItem order = getTableView().getItems().get(getIndex());
                                boolean canModify = canModifyOrder(order);

                                // Disable buttons if order cannot be modified
                                editButton.setDisable(!canModify);
                                cancelButton.setDisable(!canModify);

                                setGraphic(buttonsBox);
                            }
                        };
                    }
                };

        actionsColumn.setCellFactory(cellFactory);
    }

    /**
     * Load orders from Firestore
     */
    private void loadOrders() {
        try {
            List<OrderListItem> orders = new ArrayList<>();

            if (isEmployeeView) {
                // Employee view - get all orders
                List<Order> allOrders = orderService.getAllOrders();

                for (Order order : allOrders) {
                    OrderListItem orderItem = new OrderListItem(
                            order.getOrderId(),
                            order.getPickupDate(),
                            order.getPickupTime(),
                            String.format("$%.2f", order.getOrderTotal()),
                            order.getOrderStatus()
                    );
                    orders.add(orderItem);
                }
            } else {
                // Customer view - get only this user's orders
                String userEmail = currentUser != null ? currentUser.getEmail() : "";
                List<Order> userOrders = orderService.getUserOrders(userEmail);

                for (Order order : userOrders) {
                    OrderListItem orderItem = new OrderListItem(
                            order.getOrderId(),
                            order.getPickupDate(),
                            order.getPickupTime(),
                            String.format("$%.2f", order.getOrderTotal()),
                            order.getOrderStatus()
                    );
                    orders.add(orderItem);
                }
            }

            // Update the table with orders
            ordersList = FXCollections.observableArrayList(orders);
            ordersTableView.setItems(ordersList);

            // Update visibility of "No orders" message
            noOrdersLabel.setVisible(orders.isEmpty());
            ordersTableView.setVisible(!orders.isEmpty());

            // Update status label
            orderStatusLabel.setText("Found " + orders.size() + " orders.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load orders: " + e.getMessage());
        }
    }


    /**
     * Open the edit order dialog
     */
    private void openEditOrderDialog(OrderListItem orderItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.ORDER_EDIT_DIALOG_SCREEN));
            Parent root = loader.load();

            OrderEditController controller = loader.getController();
            controller.initData(orderItem, this);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Edit Order #" + orderItem.getOrderId());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    /**
     * Confirm and cancel an order
     */
    private void confirmCancelOrder(OrderListItem order) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cancel Order");
        confirmation.setHeaderText("Cancel Order #" + order.getOrderId());
        confirmation.setContentText("Are you sure you want to cancel this order? This action cannot be undone.");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            cancelOrderInFirestore(order.getOrderId());
        }
    }

    /**
     * Update the order status to Cancelled in Firestore
     */
    private void cancelOrderInFirestore(String orderId) {
        try {
            if (orderService.cancelOrder(orderId)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order cancelled successfully.");
                refreshOrders();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel order.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel order: " + e.getMessage());
        }
    }

    /**
     * Refresh the orders list
     */
    @FXML
    public void refreshOrders() {
        loadOrders();
    }

    /**
     * Go to the home screen
     */
    @FXML
    private void goToHome(ActionEvent event) {
        try {
            // Navigate to appropriate menu based on user type
            String fxmlPath = isEmployeeView ? ViewPaths.EMPLOYEE_MAIN_SCREEN : ViewPaths.CUSTOMER_MAIN_SCREEN;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ordersTableView.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to navigate to home: " + e.getMessage());
        }
    }

    /**
     * Create a new order
     */
    @FXML
    private void createNewOrder(ActionEvent event) {
        try {
            // Navigate to the order view
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.ORDER_VIEW_SCREEN));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ordersTableView.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to navigate to new order screen: " + e.getMessage());
        }
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Check if an order can be modified (more than 24 hours before pickup)
     */
    private boolean canModifyOrder(OrderListItem orderItem) {
        try {
            // If order is already cancelled or completed, it cannot be modified
            if (orderItem.getStatus().equals("Cancelled") || orderItem.getStatus().equals("Completed")) {
                return false;
            }

            // Parse date and time
            LocalDate date = LocalDate.parse(orderItem.getPickupDate());

            // Parse time using pattern like "10:30 AM"
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
            LocalTime time = LocalTime.parse(orderItem.getPickupTime(), timeFormatter);

            // Combine into LocalDateTime
            LocalDateTime pickupDateTime = LocalDateTime.of(date, time);

            // Check if current time is more than 24 hours before pickup
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime cutoffTime = pickupDateTime.minusHours(24);

            return currentTime.isBefore(cutoffTime);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find an order in the current list by ID
     */
    private OrderListItem findOrderById(String orderId) {
        if (ordersList == null) return null;

        for (OrderListItem item : ordersList) {
            if (item.getOrderId().equals(orderId)) {
                return item;
            }
        }

        return null;
    }
}