package murray.csc325sprint1;

public class MenuItem {
    private String name;
    private double price;
    private String description;
    private String category;
    private String unit;
    private String imagePath;

    /**
     * Constructor for MenuItem
     *
     * @param name Item name
     * @param price Item price
     * @param description Item description
     * @param category Item category (appetizer, entree, dessert)
     * @param unit Item unit (dozen, platter, order)
     * @param imagePath Path to the item's image file
     */
    public MenuItem(String name, double price, String description, String category, String unit, String imagePath) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.imagePath = imagePath;
    }

    /**
     * Constructor for MenuItem without image path (backward compatibility)
     *
     * @param name Item name
     * @param price Item price
     * @param description Item description
     * @param category Item category (appetizer, entree, dessert)
     * @param unit Item unit (dozen, platter, order)
     */
    public MenuItem(String name, double price, String description, String category, String unit) {
        this(name, price, description, category, unit, "/images/food_placeholder.png");
    }

    /**
     * Get the formatted price with dollar sign
     *
     * @return Formatted price (e.g., "$48.00")
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    /**
     * Get the formatted price with unit
     *
     * @return Formatted price with unit (e.g., "$48.00/dozen")
     */
    public String getFormattedPriceWithUnit() {
        return String.format("$%.2f/%s", price, unit);
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", name, getFormattedPriceWithUnit());
    }
}