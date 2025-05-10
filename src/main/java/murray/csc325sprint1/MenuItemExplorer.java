package murray.csc325sprint1;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A utility application to explore menu items and their images
 */
public class MenuItemExplorer extends Application {

    private final Menu menuService = new Menu();
    private final Firestore db = FirestoreContext.getInstance().getFirestore();

    private ListView<String> menuItemsListView;
    private Label nameLabel;
    private Label categoryLabel;
    private Label priceLabel;
    private Label imagePathLabel;
    private ImageView imageView;
    private TextArea descriptionTextArea;
    private TextArea resourcePathTextArea;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Left side - List of menu items
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10));

        Label menuLabel = new Label("Menu Items");
        menuLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        menuItemsListView = new ListView<>();
        menuItemsListView.setPrefWidth(200);

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> loadMenuItems());

        leftPane.getChildren().addAll(menuLabel, menuItemsListView, refreshButton);

        // Center - Item details
        VBox centerPane = new VBox(10);
        centerPane.setPadding(new Insets(10));

        Label detailsLabel = new Label("Item Details");
        detailsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        nameLabel = new Label("Name: ");
        categoryLabel = new Label("Category: ");
        priceLabel = new Label("Price: ");
        imagePathLabel = new Label("Image Path: ");

        descriptionTextArea = new TextArea();
        descriptionTextArea.setPrefHeight(80);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setPromptText("Description");

        resourcePathTextArea = new TextArea();
        resourcePathTextArea.setPrefHeight(80);
        resourcePathTextArea.setEditable(false);
        resourcePathTextArea.setWrapText(true);
        resourcePathTextArea.setPromptText("Resource Path Info");

        HBox imageBox = new HBox(10);
        imageBox.setPadding(new Insets(10));

        imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #CCCCCC;");

        imageBox.getChildren().add(imageView);

        Button testImageButton = new Button("Test Image Loading");
        testImageButton.setOnAction(e -> testImageLoading());

        centerPane.getChildren().addAll(
                detailsLabel,
                nameLabel,
                categoryLabel,
                priceLabel,
                imagePathLabel,
                new Label("Description:"),
                descriptionTextArea,
                new Label("Resource Path Info:"),
                resourcePathTextArea,
                imageBox,
                testImageButton
        );

        // Set up the layout
        root.setLeft(leftPane);
        root.setCenter(centerPane);

        // Set up the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Menu Item Explorer");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load menu items
        loadMenuItems();

        // Add selection listener
        menuItemsListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayMenuItemDetails(newValue);
                    }
                });
    }

    /**
     * Load menu items from the database
     */
    private void loadMenuItems() {
        try {
            // Clear the list
            menuItemsListView.getItems().clear();

            // Query all menu items
            QuerySnapshot querySnapshot = db.collection("menu_items").get().get();

            // Add each item to the list
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                if (name != null) {
                    menuItemsListView.getItems().add(name);
                }
            }

            // Sort the list
            menuItemsListView.getItems().sort(String::compareTo);

        } catch (InterruptedException | ExecutionException e) {
            showAlert("Error", "Failed to load menu items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Display details for a selected menu item
     */
    private void displayMenuItemDetails(String itemName) {
        try {
            // Query for the specific item
            QuerySnapshot querySnapshot = db.collection("menu_items")
                    .whereEqualTo("name", itemName)
                    .get()
                    .get();

            if (querySnapshot.isEmpty()) {
                showAlert("Error", "Menu item not found: " + itemName);
                return;
            }

            // Get the document
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

            // Update labels
            nameLabel.setText("Name: " + document.getString("name"));
            categoryLabel.setText("Category: " + document.getString("category"));
            priceLabel.setText("Price: $" + document.getDouble("price"));

            String imagePath = document.getString("imagePath");
            imagePathLabel.setText("Image Path: " + imagePath);

            // Description
            descriptionTextArea.setText(document.getString("description"));

            // Clear image and image path info
            imageView.setImage(null);
            resourcePathTextArea.setText("");

            // Try to load the image
            if (imagePath != null && !imagePath.isEmpty()) {
                // Get resource URL
                URL imageUrl = getClass().getResource(imagePath);

                StringBuilder info = new StringBuilder();
                info.append("Attempting to load image from: ").append(imagePath).append("\n");

                if (imageUrl != null) {
                    info.append("Resource URL: ").append(imageUrl.toExternalForm()).append("\n");

                    try {
                        Image image = new Image(imageUrl.toExternalForm());
                        if (!image.isError()) {
                            imageView.setImage(image);
                            info.append("Image loaded successfully!");
                        } else {
                            info.append("Error loading image: Image error property is true\n");
                            info.append("Exception: ").append(image.getException());
                        }
                    } catch (Exception e) {
                        info.append("Exception loading image: ").append(e.getMessage());
                    }
                } else {
                    info.append("Resource not found. Check path and resource directory structure.");
                }

                resourcePathTextArea.setText(info.toString());
            }

        } catch (InterruptedException | ExecutionException e) {
            showAlert("Error", "Failed to load menu item details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test different image loading approaches
     */
    private void testImageLoading() {
        String selectedItem = menuItemsListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select a menu item first");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("Image Loading Test Results:\n\n");

        try {
            // Query for the specific item
            QuerySnapshot querySnapshot = db.collection("menu_items")
                    .whereEqualTo("name", selectedItem)
                    .get()
                    .get();

            if (querySnapshot.isEmpty()) {
                info.append("Menu item not found: ").append(selectedItem);
                resourcePathTextArea.setText(info.toString());
                return;
            }

            // Get the document and image path
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            String imagePath = document.getString("imagePath");

            if (imagePath == null || imagePath.isEmpty()) {
                info.append("No image path specified for this item");
                resourcePathTextArea.setText(info.toString());
                return;
            }

            info.append("Image path from database: ").append(imagePath).append("\n\n");

            // Test 1: Using Class.getResource
            info.append("Method 1 - Class.getResource:\n");
            try {
                URL resourceUrl = getClass().getResource(imagePath);
                if (resourceUrl != null) {
                    info.append("Resource found: ").append(resourceUrl.toExternalForm()).append("\n");

                    Image image = new Image(resourceUrl.toExternalForm());
                    if (!image.isError()) {
                        info.append("Image loaded successfully!\n");
                        imageView.setImage(image);
                    } else {
                        info.append("Error loading image: ").append(image.getException()).append("\n");
                    }
                } else {
                    info.append("Resource not found using getClass().getResource()\n");
                }
            } catch (Exception e) {
                info.append("Exception: ").append(e.getMessage()).append("\n");
            }

            // Test 2: Using getResourceAsStream
            info.append("\nMethod 2 - getResourceAsStream:\n");
            try {
                var stream = getClass().getResourceAsStream(imagePath);
                if (stream != null) {
                    info.append("Resource stream found\n");

                    Image image = new Image(stream);
                    if (!image.isError()) {
                        info.append("Image loaded successfully!\n");
                        imageView.setImage(image);
                    } else {
                        info.append("Error loading image: ").append(image.getException()).append("\n");
                    }
                } else {
                    info.append("Resource stream not found\n");
                }
            } catch (Exception e) {
                info.append("Exception: ").append(e.getMessage()).append("\n");
            }

            // Test 3: Using ClassLoader.getResource
            info.append("\nMethod 3 - ClassLoader.getResource:\n");
            try {
                String path = imagePath;
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }

                URL resourceUrl = ClassLoader.getSystemResource(path);
                if (resourceUrl != null) {
                    info.append("Resource found: ").append(resourceUrl.toExternalForm()).append("\n");

                    Image image = new Image(resourceUrl.toExternalForm());
                    if (!image.isError()) {
                        info.append("Image loaded successfully!\n");
                        imageView.setImage(image);
                    } else {
                        info.append("Error loading image: ").append(image.getException()).append("\n");
                    }
                } else {
                    info.append("Resource not found using ClassLoader.getSystemResource()\n");
                }
            } catch (Exception e) {
                info.append("Exception: ").append(e.getMessage()).append("\n");
            }

            // Test 4: Using absolute file URL
            info.append("\nMethod 4 - File URL check:\n");
            try {
                String path = imagePath;
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }

                String absPath = System.getProperty("user.dir") + "/src/main/resources/" + path;
                info.append("Checking file at: ").append(absPath).append("\n");

                java.io.File file = new java.io.File(absPath);
                if (file.exists()) {
                    info.append("File exists on disk at absolute path\n");
                } else {
                    info.append("File does not exist at absolute path\n");
                }
            } catch (Exception e) {
                info.append("Exception: ").append(e.getMessage()).append("\n");
            }

            // Update text area
            resourcePathTextArea.setText(info.toString());

        } catch (Exception e) {
            info.append("Error in test: ").append(e.getMessage());
            resourcePathTextArea.setText(info.toString());
            e.printStackTrace();
        }
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
