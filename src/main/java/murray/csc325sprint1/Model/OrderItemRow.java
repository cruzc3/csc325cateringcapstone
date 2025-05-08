package murray.csc325sprint1;

/**
 * Class for representing an order item in the edit order dialog table
 */
public class OrderItemRow {
    private String itemName;
    private int quantity;
    private String price;

    /**
     * Constructor
     *
     * @param itemName Item name
     * @param quantity Quantity
     * @param price Formatted price
     */
    public OrderItemRow(String itemName, int quantity, String price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
