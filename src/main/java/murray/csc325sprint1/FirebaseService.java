package murray.csc325sprint1;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for interacting with Firebase Firestore database
 */
public class FirebaseService {
    private static FirebaseService instance;
    private Firestore db;

    private static final String MENU_COLLECTION = "menu_items";
    private static final String ORDERS_COLLECTION = "orders";

    private FirebaseService() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get the singleton instance of FirebaseService
     *
     * @return FirebaseService instance
     */
    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }

    /**
     * Initialize Firebase with the credentials from key.json
     */
    public void initializeFirebase() {
        try {
            // Load Firebase configuration from the key.json file
            InputStream serviceAccount = getClass().getResourceAsStream("/key.json");
            if (serviceAccount == null) {
                System.err.println("Could not find key.json file");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://csc325capstone-1054a.firebaseio.com")
                    .build();

            // Initialize Firebase if not already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            // Get Firestore instance
            db = FirestoreClient.getFirestore();

            System.out.println("Firebase initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the Firestore database instance
     *
     * @return Firestore instance
     */
    public Firestore getFirestore() {
        return db;
    }

    /**
     * Set up menu items in Firebase if they don't exist
     */
    public void setupMenuItems() {
        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return;
            }

            // Check if menu collection already has items
            QuerySnapshot menuQuery = db.collection(MENU_COLLECTION).limit(1).get().get();

            if (!menuQuery.isEmpty()) {
                System.out.println("Menu items already exist in Firebase.");
                return;
            }

            System.out.println("Initializing menu items in Firebase...");

            // Create appetizers
            addMenuItem("Mini Crab Cakes", 48.0, "Bite-sized crab cakes made with lump crab meat, herbs, and spices", "appetizer", "dozen");
            addMenuItem("Arancini", 36.0, "Crispy fried risotto balls stuffed with mozzarella cheese and served with marinara dipping sauce", "appetizer", "dozen");
            addMenuItem("Shrimp Cocktail", 54.0, "Fresh jumbo shrimp, chilled and served with zesty cocktail sauce", "appetizer", "dozen");
            addMenuItem("Pulled Pork Sliders", 60.0, "Smoky BBQ pulled pork topped with coleslaw, served in soft brioche buns", "appetizer", "dozen");
            addMenuItem("Caprese Skewers", 36.0, "Fresh mozzarella, cherry tomatoes, and basil leaves drizzled with a tangy balsamic glaze", "appetizer", "dozen");
            addMenuItem("Vegan Spring Rolls", 42.0, "Fresh vegetable-filled rice paper rolls served with peanut or sweet chili sauce", "appetizer", "dozen");

            // Add entrees
            addMenuItem("Rosemary-Garlic Prime Rib", 225.0, "Slow-roasted prime rib crusted with fresh rosemary, garlic, and cracked black pepper, served with au jus, horseradish cream, and Yorkshire pudding.", "entree", "order");
            addMenuItem("Herb-Crusted Salmon", 190.0, "Fresh Atlantic salmon fillets topped with a lemon-herb crust, roasted to perfection and served with dill cream sauce and charred lemon halves.", "entree", "order");
            addMenuItem("Mediterranean Stuffed Chicken", 175.0, "Boneless chicken breasts stuffed with spinach, sun-dried tomatoes, and feta cheese, wrapped in prosciutto and served with a white wine and herb sauce.", "entree", "order");

            // Add desserts
            addMenuItem("French Macaron Tower", 95.0, "A stunning display of delicate French macarons in assorted flavors including raspberry, pistachio, chocolate, lemon, salted caramel, and lavender.", "dessert", "tower");
            addMenuItem("Tiramisu Cups", 75.0, "Individual servings of classic tiramisu featuring espresso-soaked ladyfingers layered with mascarpone cream and dusted with cocoa powder.", "dessert", "order");
            addMenuItem("Chocolate Ganache Tartlets", 65.0, "Buttery shortbread tart shells filled with rich dark chocolate ganache, topped with sea salt, gold leaf, and seasonal berries.", "dessert", "order");

            System.out.println("Menu items initialized successfully");

        } catch (Exception e) {
            System.err.println("Error setting up menu items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a menu item to the Firebase collection
     *
     * @param name Item name
     * @param price Item price
     * @param description Item description
     * @param category Item category (appetizer, entree, dessert)
     * @param unit Item unit (dozen, platter, order)
     */
    private void addMenuItem(String name, double price, String description, String category, String unit) {
        try {
            Map<String, Object> menuItem = new HashMap<>();
            menuItem.put("name", name);
            menuItem.put("price", price);
            menuItem.put("description", description);
            menuItem.put("category", category);
            menuItem.put("unit", unit);

            String documentId = name.toLowerCase().replace(" ", "_");
            db.collection(MENU_COLLECTION).document(documentId).set(menuItem).get();

        } catch (Exception e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get all menu items from Firebase
     *
     * @return List of MenuItem objects
     */
    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return menuItems;
            }

            QuerySnapshot querySnapshot = db.collection(MENU_COLLECTION).get().get();

            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                Double price = document.getDouble("price");
                String description = document.getString("description");
                String category = document.getString("category");
                String unit = document.getString("unit");

                if (name != null && price != null && description != null && category != null && unit != null) {
                    MenuItem item = new MenuItem(name, price, description, category, unit);
                    menuItems.add(item);
                }
            }

        } catch (Exception e) {
            System.err.println("Error getting menu items: " + e.getMessage());
            e.printStackTrace();
        }

        return menuItems;
    }

    /**
     * Get menu items by category
     *
     * @param category Category to filter by (appetizer, entree, dessert)
     * @return List of MenuItem objects in the specified category
     */
    public List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> menuItems = new ArrayList<>();

        try {
            if (db == null) {
                System.err.println("Firestore not initialized");
                return menuItems;
            }

            QuerySnapshot querySnapshot = db.collection(MENU_COLLECTION)
                    .whereEqualTo("category", category)
                    .get()
                    .get();

            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                Double price = document.getDouble("price");
                String description = document.getString("description");
                String unit = document.getString("unit");

                if (name != null && price != null && description != null && unit != null) {
                    MenuItem item = new MenuItem(name, price, description, category, unit);
                    menuItems.add(item);
                }
            }

        } catch (Exception e) {
            System.err.println("Error getting menu items by category: " + e.getMessage());
            e.printStackTrace();
        }

        return menuItems;
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
