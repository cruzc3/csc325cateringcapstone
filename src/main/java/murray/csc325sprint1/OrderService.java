package murray.csc325sprint1;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

/**
 * Service for managing orders in the application
 */
public class OrderService {
    private Firestore db;
    private static final String ORDERS_COLLECTION = "orders";
    private static final int MAX_ORDERS_PER_TIMESLOT = 5;

    /**
     * Constructor - initializes Firestore
     */
    public OrderService() {
        // Get Firestore instance using singleton
        db = FirestoreContext.getInstance().getFirestore();
    }

    /**
     * Save an order to Firebase
     *
     * @param order Order object to save
     * @return true if the order was saved successfully, false otherwise
     */
    public boolean saveOrder(Order order) {
        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return false;
            }

            // Check if the selected time slot is available
            if (!isTimeSlotAvailable(order.getPickupDate(), order.getPickupTime())) {
                System.err.println("Time slot is full for " + order.getPickupDate() + " at " + order.getPickupTime());
                return false;
            }

            // Generate a unique order ID if not provided
            if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                order.setOrderId(UUID.randomUUID().toString());
            }

            // Convert order to a map for Firestore
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", order.getOrderId());
            orderMap.put("orderItems", order.getOrderItems());
            orderMap.put("orderTotal", order.getOrderTotal());
            orderMap.put("pickupDate", order.getPickupDate());
            orderMap.put("pickupTime", order.getPickupTime());
            orderMap.put("orderStatus", order.getOrderStatus());
            orderMap.put("orderTimestamp", System.currentTimeMillis());

            // Add user email
            orderMap.put("userEmail", order.getUserEmail());

            // Save to Firestore
            db.collection(ORDERS_COLLECTION).document(order.getOrderId()).set(orderMap).get();

            System.out.println("Order saved successfully with ID: " + order.getOrderId());
            return true;

        } catch (Exception e) {
            System.err.println("Error saving order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a time slot is available (less than MAX_ORDERS_PER_TIMESLOT orders)
     *
     * @param date The pickup date
     * @param time The pickup time
     * @return true if the time slot is available, false otherwise
     */
    public boolean isTimeSlotAvailable(String date, String time) {
        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return false;
            }

            int count = countOrdersInTimeSlot(date, time);
            return count < MAX_ORDERS_PER_TIMESLOT;

        } catch (Exception e) {
            System.err.println("Error checking time slot availability: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Count the number of orders in a specific time slot
     *
     * @param date The pickup date
     * @param time The pickup time
     * @return The number of orders in the specified time slot
     */
    public int countOrdersInTimeSlot(String date, String time) throws InterruptedException, ExecutionException {
        if (db == null) {
            System.err.println("Firestore not initialized");
            return 0;
        }

        QuerySnapshot querySnapshot = db.collection(ORDERS_COLLECTION)
                .whereEqualTo("pickupDate", date)
                .whereEqualTo("pickupTime", time)
                .get()
                .get();

        return querySnapshot.size();
    }

    /**
     * Get the maximum number of orders allowed per time slot
     *
     * @return The maximum number of orders per time slot
     */
    public int getMaxOrdersPerTimeSlot() {
        return MAX_ORDERS_PER_TIMESLOT;
    }

    /**
     * Get all orders for a specific user
     *
     * @param userEmail The user's email
     * @return List of orders
     */
    public List<Order> getUserOrders(String userEmail) {
        List<Order> orders = new ArrayList<>();

        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return orders;
            }

            QuerySnapshot querySnapshot = db.collection(ORDERS_COLLECTION)
                    .whereEqualTo("userEmail", userEmail)
                    .get()
                    .get();

            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                Order order = new Order();
                order.setOrderId(document.getString("orderId"));
                order.setPickupDate(document.getString("pickupDate"));
                order.setPickupTime(document.getString("pickupTime"));
                order.setOrderTotal(document.getDouble("orderTotal"));
                order.setOrderStatus(document.getString("orderStatus"));

                // Get order items
                Map<String, Object> orderItems = (Map<String, Object>) document.get("orderItems");
                if (orderItems != null) {
                    for (Map.Entry<String, Object> entry : orderItems.entrySet()) {
                        String itemName = entry.getKey();
                        Long quantity = (Long) entry.getValue();
                        // Note: We don't have the price info here, but it's not needed for display purposes
                        order.getOrderItems().put(itemName, quantity.intValue());
                    }
                }

                orders.add(order);
            }

        } catch (Exception e) {
            System.err.println("Error getting user orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Update an order in Firebase
     *
     * @param order Order object to update
     * @return true if the order was updated successfully, false otherwise
     */
    public boolean updateOrder(Order order) {
        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return false;
            }

            // Check if the selected time slot is available (only if the date/time changed)
            DocumentSnapshot originalOrder = db.collection(ORDERS_COLLECTION)
                    .document(order.getOrderId())
                    .get()
                    .get();

            String originalDate = originalOrder.getString("pickupDate");
            String originalTime = originalOrder.getString("pickupTime");

            // If date/time changed, check availability
            if (!originalDate.equals(order.getPickupDate()) || !originalTime.equals(order.getPickupTime())) {
                if (!isTimeSlotAvailable(order.getPickupDate(), order.getPickupTime())) {
                    System.err.println("Time slot is full for " + order.getPickupDate() + " at " + order.getPickupTime());
                    return false;
                }
            }

            // Convert order to a map for Firestore
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("pickupDate", order.getPickupDate());
            orderMap.put("pickupTime", order.getPickupTime());
            orderMap.put("orderItems", order.getOrderItems());
            orderMap.put("orderTotal", order.getOrderTotal());
            orderMap.put("orderStatus", order.getOrderStatus());

            // Update in Firestore
            db.collection(ORDERS_COLLECTION).document(order.getOrderId()).update(orderMap).get();

            System.out.println("Order updated successfully with ID: " + order.getOrderId());
            return true;

        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cancel an order
     *
     * @param orderId The order ID to cancel
     * @return true if cancelled successfully, false otherwise
     */
    public boolean cancelOrder(String orderId) {
        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return false;
            }

            // Update the status to Cancelled
            db.collection(ORDERS_COLLECTION)
                    .document(orderId)
                    .update("orderStatus", "Cancelled")
                    .get();

            System.out.println("Order cancelled successfully: " + orderId);
            return true;

        } catch (Exception e) {
            System.err.println("Error cancelling order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}