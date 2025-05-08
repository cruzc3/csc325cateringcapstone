package murray.csc325sprint1.Model;

/**
 * Class for representing an order in the orders list table view
 */
public class OrderListItem {
    private String orderId;
    private String pickupDate;
    private String pickupTime;
    private String total;
    private String status;

    /**
     * Constructor
     *
     * @param orderId Order ID
     * @param pickupDate Pickup date
     * @param pickupTime Pickup time
     * @param total Formatted total price
     * @param status Order status
     */
    public OrderListItem(String orderId, String pickupDate, String pickupTime, String total, String status) {
        this.orderId = orderId;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.total = total;
        this.status = status;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
