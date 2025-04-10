package murray.csc325sprint1;

import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service for managing orders in the application
 */
public class OrderService {
    private Firestore db;
    private static final String ORDERS_COLLECTION = "orders";

    /**
     * Constructor - initializes Firestore
     */
    public OrderService() {
        // Get Firestore instance
        db = new FirestoreContext().firebase();
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
}