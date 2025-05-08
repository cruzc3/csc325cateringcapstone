package murray.csc325sprint1.ViewModel;

import com.google.cloud.firestore.DocumentSnapshot;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import murray.csc325sprint1.FirestoreContext;
import murray.csc325sprint1.MainApp;
import murray.csc325sprint1.Model.OrderListItem;
import murray.csc325sprint1.OrderService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.scene.Parent;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    // This would be set from login screen. For now using a dummy email
    private String userEmail = "customer@example.com";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get instances of services
        orderService = MainApp.getOrderService();
        db = FirestoreContext.getInstance().getFirestore();

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
            QuerySnapshot querySnapshot = db.collection("orders")
                    //.whereEqualTo("userEmail", userEmail)
                    .get()
                    .get();

            List<OrderListItem> orders = new ArrayList<>();

            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                String orderId = document.getString("orderId");
                String pickupDate = document.getString("pickupDate");
                String pickupTime = document.getString("pickupTime");
                Double orderTotal = document.getDouble("orderTotal");
                String status = document.getString("orderStatus");

                OrderListItem orderItem = new OrderListItem(
                        orderId,
                        pickupDate,
                        pickupTime,
                        String.format("$%.2f", orderTotal),
                        status
                );

                orders.add(orderItem);
            }

            // Update the table with orders
            ordersList = FXCollections.observableArrayList(orders);
            ordersTableView.setItems(ordersList);

            // Update visibility of "No orders" message
            noOrdersLabel.setVisible(orders.isEmpty());
            ordersTableView.setVisible(!orders.isEmpty());

            // Update status label
            orderStatusLabel.setText("Found " + orders.size() + " orders.");

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load orders: " + e.getMessage());
        }
    }


    /**
     * Open the edit order dialog
     */
    private void openEditOrderDialog(OrderListItem orderItem) {
        try {
            // Create a new OrderEditController
            OrderEditController controller = new OrderEditController();

            // Create the dialog programmatically
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Edit Order #" + orderItem.getOrderId());

            // Create a root container
            VBox root = new VBox(10);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: white;");

            // Create the header
            Label titleLabel = new Label("Edit Order #" + orderItem.getOrderId());
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            root.getChildren().add(titleLabel);

            // Order ID and Total
            HBox infoBox = new HBox(20);
            Label idLabel = new Label("Order ID: " + orderItem.getOrderId());
            Label totalLabel = new Label("Total: " + orderItem.getTotal());
            infoBox.getChildren().addAll(idLabel, totalLabel);
            root.getChildren().add(infoBox);

            // Separator
            root.getChildren().add(new Separator());

            // Order date and time section
            Label dateTimeLabel = new Label("Change pickup date and time:");
            dateTimeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            root.getChildren().add(dateTimeLabel);

            // Date and time selectors
            HBox dateTimeBox = new HBox(20);

            // Date picker
            VBox dateBox = new VBox(5);
            Label dateLabel = new Label("Date:");
            DatePicker datePicker = new DatePicker();
            dateBox.getChildren().addAll(dateLabel, datePicker);

            // Time combo box
            VBox timeBox = new VBox(5);
            Label timeLabel = new Label("Time:");
            ComboBox<String> timeComboBox = new ComboBox<>();

            // Add times from 9 AM to 7 PM
            ObservableList<String> times = FXCollections.observableArrayList();
            LocalTime startTime = LocalTime.of(9, 0);
            LocalTime endTime = LocalTime.of(19, 0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

            LocalTime current = startTime;
            while (!current.isAfter(endTime)) {
                times.add(current.format(formatter));
                current = current.plusMinutes(30);
            }

            timeComboBox.setItems(times);
            timeBox.getChildren().addAll(timeLabel, timeComboBox);

            dateTimeBox.getChildren().addAll(dateBox, timeBox);
            root.getChildren().add(dateTimeBox);

            // Availability label
            Label availabilityLabel = new Label("Select date and time to check availability");
            availabilityLabel.setStyle("-fx-text-fill: blue;");
            root.getChildren().add(availabilityLabel);

            // Buttons
            HBox buttonsBox = new HBox(20);
            buttonsBox.setAlignment(Pos.CENTER);

            Button cancelOrderButton = new Button("Cancel Order");
            cancelOrderButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white;");

            Button saveButton = new Button("Save Changes");
            saveButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");

            buttonsBox.getChildren().addAll(cancelOrderButton, saveButton);
            root.getChildren().add(buttonsBox);

            // Create the scene and set it on the stage
            Scene scene = new Scene(root, 500, 400);
            dialogStage.setScene(scene);

            // Initialize the order data and setup event handlers
            try {
                // Assume we're working with a date in format yyyy-MM-dd
                datePicker.setValue(LocalDate.parse(orderItem.getPickupDate()));
                timeComboBox.setValue(orderItem.getPickupTime());

                // Check availability when date or time changes
                datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
                    updateAvailability(datePicker.getValue(), timeComboBox.getValue(), availabilityLabel, saveButton, orderItem.getOrderId());
                });

                timeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                    updateAvailability(datePicker.getValue(), timeComboBox.getValue(), availabilityLabel, saveButton, orderItem.getOrderId());
                });

                // Save button action
                saveButton.setOnAction(event -> {
                    saveChanges(orderItem, datePicker.getValue().toString(), timeComboBox.getValue(), dialogStage);
                });

                // Cancel order button action
                cancelOrderButton.setOnAction(event -> {
                    cancelOrder(orderItem.getOrderId(), dialogStage);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Show the dialog
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    /**
     * Update the availability information
     */
    private void updateAvailability(LocalDate date, String time, Label availabilityLabel, Button saveButton, String orderId) {
        if (date == null || time == null) {
            return;
        }

        try {
            OrderService orderService = new OrderService();

            // Get current count for this time slot
            int currentCount = orderService.countOrdersInTimeSlot(date.toString(), time);
            int maxOrders = orderService.getMaxOrdersPerTimeSlot();
            int remainingSlots = maxOrders - currentCount;

            if (remainingSlots > 0) {
                availabilityLabel.setText("Available slots: " + remainingSlots + " of " + maxOrders);
                availabilityLabel.setStyle("-fx-text-fill: green;");
                saveButton.setDisable(false);
            } else {
                availabilityLabel.setText("This time slot is fully booked. Please select another time.");
                availabilityLabel.setStyle("-fx-text-fill: red;");
                saveButton.setDisable(true);
            }
        } catch (Exception e) {
            availabilityLabel.setText("Error checking availability");
            availabilityLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Save changes to the order
     */
    private void saveChanges(OrderListItem orderItem, String newDate, String newTime, Stage dialogStage) {
        try {
            // Check if we can modify this order (24-hour rule)
            if (!canModifyOrder(orderItem)) {
                showAlert(Alert.AlertType.ERROR, "Cannot Modify Order",
                        "Orders can only be modified if it's more than 24 hours before the scheduled pickup time.");
                return;
            }

            // Update the order in Firestore
            Firestore db = FirestoreContext.getInstance().getFirestore();
            Map<String, Object> updates = new HashMap<>();
            updates.put("pickupDate", newDate);
            updates.put("pickupTime", newTime);

            db.collection("orders")
                    .document(orderItem.getOrderId())
                    .update(updates)
                    .get();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully.");

            // Refresh the orders list
            refreshOrders();

            // Close the dialog
            dialogStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order: " + e.getMessage());
        }
    }

    /**
     * Cancel an order
     */
    private void cancelOrder(String orderId, Stage dialogStage) {
        try {
            // Check if we can cancel this order (24-hour rule)
            OrderListItem orderItem = findOrderById(orderId);
            if (orderItem != null && !canModifyOrder(orderItem)) {
                showAlert(Alert.AlertType.ERROR, "Cannot Cancel Order",
                        "Orders can only be cancelled if it's more than 24 hours before the scheduled pickup time.");
                return;
            }

            // Confirm cancellation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Cancel Order");
            confirmation.setHeaderText("Cancel Order #" + orderId);
            confirmation.setContentText("Are you sure you want to cancel this order? This action cannot be undone.");

            if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Update order status to Cancelled
                Firestore db = FirestoreContext.getInstance().getFirestore();
                db.collection("orders")
                        .document(orderId)
                        .update("orderStatus", "Cancelled")
                        .get();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Order cancelled successfully.");

                // Refresh the orders list
                refreshOrders();

                // Close the dialog
                dialogStage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel order: " + e.getMessage());
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
            db.collection("orders")
                    .document(orderId)
                    .update("orderStatus", "Cancelled")
                    .get();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Order cancelled successfully.");

            // Refresh the orders list
            loadOrders();

        } catch (InterruptedException | ExecutionException e) {
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
            // Navigate to customer menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/customer-main.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/OrderView.fxml"));
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
