package murray.csc325sprint1;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Utility to update image paths in Firestore menu items
 */
public class ImagePathUpdater {

    private static final String MENU_COLLECTION = "menu_items";
    private final Firestore db;

    public ImagePathUpdater() {
        // Get Firestore instance
        db = FirestoreContext.getInstance().getFirestore();
    }

    /**
     * Update all menu items with appropriate image paths
     */
    public void updateAllImagePaths() {
        try {
            System.out.println("Starting image path update process...");

            // Get all menu items
            QuerySnapshot querySnapshot = db.collection(MENU_COLLECTION).get().get();

            System.out.println("Found " + querySnapshot.size() + " menu items in Firestore");

            // Create mappings for each category
            Map<String, String> imagePathMap = createImagePathMap();

            int updatedCount = 0;

            // Update each document
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                String category = document.getString("category");

                if (name != null && category != null) {
                    // Get appropriate path based on name or category
                    String imagePath = imagePathMap.getOrDefault(name.toLowerCase().replace(" ", "_"),
                            "/images/menu/" + category + "_placeholder.png");

                    // Update the document with the image path
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("imagePath", imagePath);

                    // Log before update
                    System.out.println("Updating item: " + name);
                    System.out.println("  Setting image path to: " + imagePath);

                    // Perform update
                    WriteResult result = db.collection(MENU_COLLECTION)
                            .document(document.getId())
                            .update(updates)
                            .get();

                    System.out.println("  Update time: " + result.getUpdateTime());
                    updatedCount++;
                }
            }

            System.out.println("Successfully updated " + updatedCount + " menu items with image paths");

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating menu items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create a mapping of item names to image paths
     */
    private Map<String, String> createImagePathMap() {
        Map<String, String> map = new HashMap<>();

        // Appetizers
        map.put("mini_crab_cakes", "/images/menu/crab_cakes.png");
        map.put("arancini", "/images/menu/arancini.png");
        map.put("shrimp_cocktail", "/images/menu/shrimp_cocktail.png");
        map.put("pulled_pork_sliders", "/images/menu/pork_sliders.png");
        map.put("caprese_skewers", "/images/menu/caprese_skewers.png");
        map.put("vegan_spring_rolls", "/images/menu/spring_rolls.png");

        // Entrees
        map.put("rosemary-garlic_prime_rib", "/images/menu/prime_rib.png");
        map.put("herb-crusted_salmon", "/images/menu/salmon.png");
        map.put("mediterranean_stuffed_chicken", "/images/menu/stuffed_chicken.png");

        // Desserts
        map.put("french_macaron_tower", "/images/menu/macaron_tower.png");
        map.put("tiramisu_cups", "/images/menu/tiramisu.png");
        map.put("chocolate_ganache_tartlets", "/images/menu/chocolate_tart.png");

        return map;
    }

    /**
     * Check if a document with this name exists and print its image path
     */
    public void checkMenuItem(String name) {
        try {
            QuerySnapshot querySnapshot = db.collection(MENU_COLLECTION)
                    .whereEqualTo("name", name)
                    .get()
                    .get();

            if (querySnapshot.isEmpty()) {
                System.out.println("No menu item found with name: " + name);
                return;
            }

            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

            System.out.println("Found menu item: " + name);
            System.out.println("  Document ID: " + document.getId());
            System.out.println("  Category: " + document.getString("category"));
            System.out.println("  Image Path: " + document.getString("imagePath"));

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error checking menu item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method to run the updater
     */
    public static void main(String[] args) {
        ImagePathUpdater updater = new ImagePathUpdater();

        // Update all image paths
        updater.updateAllImagePaths();

        // Check a few items to verify
        System.out.println("\nVerifying updates:");
        updater.checkMenuItem("Mini Crab Cakes");
        updater.checkMenuItem("Herb-Crusted Salmon");
        updater.checkMenuItem("Tiramisu Cups");
    }
}
