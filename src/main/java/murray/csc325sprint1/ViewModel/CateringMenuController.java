package murray.csc325sprint1.ViewModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import murray.csc325sprint1.MainApp;
import murray.csc325sprint1.Menu;
import murray.csc325sprint1.MenuItem;

public class CateringMenuController implements Initializable {

    @FXML private VBox menuContainer;
    @FXML private Button backBtn;

    private Menu menuService;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get the menu from MainApp
        menuService = MainApp.getMenu();

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

        // Add everything to the item container
        itemContainer.getChildren().addAll(detailsContainer, imageView);

        // Add item container to the menu
        menuContainer.getChildren().add(itemContainer);
    }

    /**
     * Go back to the customer main menu
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Navigate back to customer main menu
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();

            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/murray/csc325sprint1/customer-main.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}