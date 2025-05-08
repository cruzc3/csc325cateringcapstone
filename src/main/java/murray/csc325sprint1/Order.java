package murray.csc325sprint1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private String orderId;
    private Map<String, Integer> orderItems; // Map of item name to quantity
    private double orderTotal;
    private String pickupDate;
    private String pickupTime;
    private String orderStatus; // Pending, Confirmed, Ready, Picked Up, Cancelled

    // New field for user email
    private String userEmail;

    /**
     * Default constructor
     */
    public Order() {
        this.orderItems = new HashMap<>();
        this.orderTotal = 0.0;
        this.orderStatus = "Pending";
    }

    /**
     * Add an item to the order
     *
     * @param item The menu item to add
     * @param quantity The quantity to add
     */
    public void addItem(MenuItem item, int quantity) {
        String itemName = item.getName();

        // If the item is already in the order, increase the quantity
        if (orderItems.containsKey(itemName)) {
            int currentQuantity = orderItems.get(itemName);
            orderItems.put(itemName, currentQuantity + quantity);
        } else {
            orderItems.put(itemName, quantity);
        }

        // Update the order total
        orderTotal += item.getPrice() * quantity;
    }

    /**
     * Remove an item from the order
     *
     * @param item The menu item to remove
     * @param quantity The quantity to remove
     */
    public void removeItem(MenuItem item, int quantity) {
        String itemName = item.getName();

        if (orderItems.containsKey(itemName)) {
            int currentQuantity = orderItems.get(itemName);

            if (currentQuantity <= quantity) {
                // Remove the item completely
                orderItems.remove(itemName);
                orderTotal -= item.getPrice() * currentQuantity;
            } else {
                // Reduce the quantity
                orderItems.put(itemName, currentQuantity - quantity);
                orderTotal -= item.getPrice() * quantity;
            }
        }
    }

    /**
     * Clear all items from the order
     */
    public void clearItems() {
        orderItems.clear();
        orderTotal = 0.0;
    }

    /**
     * Get the formatted total with dollar sign
     *
     * @return Formatted total (e.g., "$54.00")
     */
    public String getFormattedTotal() {
        return String.format("$%.2f", orderTotal);
    }

    /**
     * Get the user email
     *
     * @return The user email
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Set the user email
     *
     * @param userEmail The user email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Check if the order can be modified (more than 24 hours before pickup)
     *
     * @return true if the order can be modified, false otherwise
     */
    public boolean canBeModified() {
        // If order is already cancelled or completed, it cannot be modified
        if (orderStatus.equals("Cancelled") || orderStatus.equals("Completed")) {
            return false;
        }

        try {
            // Parse date and time
            LocalDate date = LocalDate.parse(pickupDate);

            // Parse time using pattern like "10:30 AM"
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
            LocalTime time = LocalTime.parse(pickupTime, timeFormatter);

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

    // Getters and setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<String, Integer> orderItems) {
        this.orderItems = orderItems;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}