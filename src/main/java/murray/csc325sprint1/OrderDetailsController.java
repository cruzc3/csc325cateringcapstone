package murray.csc325sprint1;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class OrderDetailsController implements Initializable {

    @FXML private Label totalLabel;
    @FXML private VBox orderedItemsContainer;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private Button placeOrderButton;
    @FXML private Button closeButton;
    @FXML private Label availabilityLabel; // New label to show availability info

    private Order currentOrder;
    private boolean orderPlaced = false;
    private OrderService orderService;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get the order service from MainApp
        orderService = MainApp.getOrderService();

        // Set up date picker with min date as tomorrow
        datePicker.setValue(LocalDate.now().plusDays(1));

        // Set the day cell factory to disable dates before tomorrow
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        // Disable dates before tomorrow
                        LocalDate tomorrow = LocalDate.now().plusDays(1);
                        setDisable(empty || date.compareTo(tomorrow) < 0);
                    }
                };
            }
        });

        // Set up time combo box with available times (9 AM - 7 PM in 30-min intervals)
        setupTimeComboBox();

        // Add listeners to update availability info when date or time changes
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailabilityInfo());
        timeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailabilityInfo());
    }

    /**
     * Set up the time combo box with available times
     */
    private void setupTimeComboBox() {
        ObservableList<String> availableTimes = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(19, 0);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        LocalTime currentTime = startTime;
        while (!currentTime.isAfter(endTime)) {
            availableTimes.add(currentTime.format(timeFormatter));
            currentTime = currentTime.plusMinutes(30);
        }

        timeComboBox.setItems(availableTimes);
        if (!availableTimes.isEmpty()) {
            timeComboBox.setValue(availableTimes.get(0)); // Default to first available time
        }
    }

    /**
     * Update the availability information displayed to the user
     */
    private void updateAvailabilityInfo() {
        if (datePicker.getValue() == null || timeComboBox.getValue() == null) {
            return;
        }

        String date = datePicker.getValue().toString();
        String time = timeComboBox.getValue();

        try {
            int currentCount = orderService.countOrdersInTimeSlot(date, time);
            int maxOrders = orderService.getMaxOrdersPerTimeSlot();
            int remainingSlots = maxOrders - currentCount;

            if (availabilityLabel != null) {
                if (remainingSlots > 0) {
                    availabilityLabel.setText("Available slots: " + remainingSlots + " of " + maxOrders);
                    availabilityLabel.setStyle("-fx-text-fill: green;");
                } else {
                    availabilityLabel.setText("This time slot is fully booked. Please select another time.");
                    availabilityLabel.setStyle("-fx-text-fill: red;");
                }
            }

            // Enable or disable the place order button based on availability
            if (placeOrderButton != null) {
                placeOrderButton.setDisable(remainingSlots <= 0);
            }

        } catch (Exception e) {
            System.err.println("Error updating availability info: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the order to display in this dialog
     *
     * @param order The order to display
     */
    public void setOrder(Order order) {
        this.currentOrder = order;

        // Update the UI with order details
        updateOrderDetails();

        // Update availability info for the default date/time
        updateAvailabilityInfo();
    }

    /**
     * Update the UI with the current order details
     */
    private void updateOrderDetails() {
        if (currentOrder == null) {
            return;
        }

        // Set the total
        totalLabel.setText("Total: " + currentOrder.getFormattedTotal());

        // Clear existing items
        orderedItemsContainer.getChildren().clear();

        // Add each ordered item
        for (Map.Entry<String, Integer> entry : currentOrder.getOrderItems().entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            // Create a label for this item
            Label itemLabel = new Label(itemName + " (x" + quantity + ")");
            itemLabel.setStyle("-fx-font-size: 14;");

            // Add to container
            orderedItemsContainer.getChildren().add(itemLabel);
        }
    }

    /**
     * Handle the place order button click
     */
    @FXML
    private void placeOrder(ActionEvent event) {
        // Validate inputs
        if (datePicker.getValue() == null) {
            showError("Please select a date for pickup.");
            return;
        }

        if (timeComboBox.getValue() == null) {
            showError("Please select a time for pickup.");
            return;
        }

        // Set the pickup date and time on the order
        currentOrder.setPickupDate(datePicker.getValue().toString());
        currentOrder.setPickupTime(timeComboBox.getValue());

        // Check if the time slot is available
        try {
            if (!orderService.isTimeSlotAvailable(currentOrder.getPickupDate(), currentOrder.getPickupTime())) {
                showError("This time slot is fully booked. Please select another time.");
                updateAvailabilityInfo(); // Refresh the availability display
                return;
            }

            // Save the order
            boolean success = orderService.saveOrder(currentOrder);

            if (success) {
                // Mark the order as placed
                orderPlaced = true;

                // Close the dialog
                closeDialog(null);
            } else {
                showError("Failed to place order. The time slot may have been filled by another customer. Please try again.");
                updateAvailabilityInfo(); // Refresh the availability display
            }
        } catch (Exception e) {
            showError("Error placing order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the close button click
     */
    @FXML
    private void closeDialog(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Show an error message
     *
     * @param message The error message to show
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Check if the order was successfully placed
     *
     * @return true if the order was placed, false otherwise
     */
    public boolean isOrderPlaced() {
        return orderPlaced;
    }
}