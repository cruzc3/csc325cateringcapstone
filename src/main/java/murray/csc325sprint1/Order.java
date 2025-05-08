package murray.csc325sprint1;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private String orderId;
    private Map<String, Integer> orderItems; // Map of item name to quantity
    private double orderTotal;
    private String pickupDate;
    private String pickupTime;
    private String orderStatus; // Pending, Confirmed, Ready, Picked Up, Cancelled

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
        if (quantity <= 0) return; // Don't add negative or zero quantities

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
     * Update an item's quantity directly
     *
     * @param item The menu item to update
     * @param newQuantity The new quantity to set
     * @return true if updated, false if not found
     */
    public boolean updateItemQuantity(MenuItem item, int newQuantity) {
        String itemName = item.getName();

        if (!orderItems.containsKey(itemName)) {
            return false;
        }

        int currentQuantity = orderItems.get(itemName);

        if (newQuantity <= 0) {
            // Remove the item completely
            orderItems.remove(itemName);
            orderTotal -= item.getPrice() * currentQuantity;
        } else {
            // Calculate the difference and update total
            int difference = newQuantity - currentQuantity;
            orderTotal += item.getPrice() * difference;

            // Update the quantity
            orderItems.put(itemName, newQuantity);
        }

        return true;
    }

    /**
     * Remove an item from the order
     *
     * @param item The menu item to remove
     * @param quantity The quantity to remove
     */
    public void removeItem(MenuItem item, int quantity) {
        if (quantity <= 0) return; // Don't remove negative or zero quantities

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