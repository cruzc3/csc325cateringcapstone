package murray.csc325sprint1.Model;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
}