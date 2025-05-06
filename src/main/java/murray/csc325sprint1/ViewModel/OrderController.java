package murray.csc325sprint1.ViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import murray.csc325sprint1.MainApp;
import murray.csc325sprint1.Model.Menu;
import murray.csc325sprint1.Model.MenuItem;
import murray.csc325sprint1.Model.Order;

public class OrderController implements Initializable {

    @FXML private VBox menuContainer;
    @FXML private Button cartBtn;

    private Order currentOrder;
    private Menu menuService;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get the menu from MainApp
        menuService = MainApp.getMenu();

        // Initialize the current order
        currentOrder = new Order();

        // Load menu items
        loadMenuItems();
    }

    /**
     * Load menu items and display them
     */
    private void loadMenuItems() {
        // Clear existing menu items
        menuContainer.getChildren().clear();

        // Get appetizers
        List<MenuItem> appetizers = menuService.getMenuItemsByCategory("appetizer");
        addCategoryToMenu("Appetizers", appetizers);

        // Get entrees
        List<MenuItem> entrees = menuService.getMenuItemsByCategory("entree");
        addCategoryToMenu("Entrees", entrees);

        // Get desserts
        List<MenuItem> desserts = menuService.getMenuItemsByCategory("dessert");
        addCategoryToMenu("Desserts", desserts);
    }

    /**
     * Add a category of menu items to the UI
     *
     * @param categoryName The name of the category
     * @param items The list of items in this category
     */
    private void addCategoryToMenu(String categoryName, List<MenuItem> items) {
        // Create category header
        Label categoryLabel = new Label(categoryName);
        categoryLabel.getStyleClass().add("category-header");
        categoryLabel.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10; -fx-font-size: 24; -fx-text-fill: #888888;");
        menuContainer.getChildren().add(categoryLabel);

        // Add each item
        for (MenuItem item : items) {
            addMenuItemToUI(item);
        }

        // Add some spacing after each category
        Region spacing = new Region();
        spacing.setPrefHeight(20);
        menuContainer.getChildren().add(spacing);
    }

    /**
     * Add a single menu item to the UI
     *
     * @param item The menu item to add
     */
    private void addMenuItemToUI(MenuItem item) {
        // Create the container for this menu item
        HBox itemContainer = new HBox();
        itemContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        itemContainer.setSpacing(10);
        itemContainer.setPadding(new Insets(10));

        // Create the left side with item details
        VBox detailsContainer = new VBox();
        detailsContainer.setPrefWidth(500);

        // Item name
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");

        // Item price
        Label priceLabel = new Label(item.getFormattedPriceWithUnit());
        priceLabel.setStyle("-fx-font-size: 14;");

        // Item description
        Label descLabel = new Label(item.getDescription());
        descLabel.setStyle("-fx-font-size: 14; -fx-wrap-text: true;");

        // Add all to details container
        detailsContainer.getChildren().addAll(nameLabel, priceLabel, descLabel);

        // Create image view (placeholder for now)
        ImageView imageView = new ImageView();
        try {
            // Try to load an image for this item
            Image image = new Image(getClass().getResourceAsStream("/images/food_placeholder.png"));
            imageView.setImage(image);
        } catch (Exception e) {
            // Use a colored rectangle instead
            imageView.setStyle("-fx-background-color: #CCCCCC;");
        }
        imageView.setFitHeight(80);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        // Create add button
        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 15;");
        addButton.setPrefSize(30, 30);
        addButton.setMinSize(30, 30);
        addButton.setMaxSize(30, 30);

        // Set up add button action
        addButton.setOnAction(event -> {
            currentOrder.addItem(item, 1);
            showAddedToCartAlert(item);
        });

        // Add everything to the item container
        itemContainer.getChildren().addAll(detailsContainer, imageView, addButton);

        // Add item container to the menu
        menuContainer.getChildren().add(itemContainer);
    }

    /**
     * Show a small alert when an item is added to the cart
     *
     * @param item The item that was added
     */
    private void showAddedToCartAlert(MenuItem item) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Item Added");
        alert.setHeaderText(null);
        alert.setContentText(item.getName() + " added to cart.");
        alert.showAndWait();
    }

    /**
     * Show the order details dialog when the cart button is clicked
     */
    @FXML
    private void showOrderDetails(ActionEvent event) {
        try {
            // Check if there are items in the order
            if (currentOrder.getOrderItems().isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Empty Cart");
                alert.setHeaderText(null);
                alert.setContentText("Your cart is empty. Please add some items first.");
                alert.showAndWait();
                return;
            }

            // Try to load the dialog from different possible paths
            URL fxmlLocation = getClass().getResource("/fxml/OrderDetailsDialog.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/murray/csc325sprint1/OrderDetailsDialog.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Could not find OrderDetailsDialog.fxml");
            }

            // Load the order details dialog
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Get the controller and pass the current order
            OrderDetailsController controller = loader.getController();
            controller.setOrder(currentOrder);

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            // Check if order was placed successfully
            if (controller.isOrderPlaced()) {
                // Reset the current order
                currentOrder = new Order();

                // Show success message
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Order Placed");
                alert.setHeaderText(null);
                alert.setContentText("Your order has been placed successfully!");
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while opening the order details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}