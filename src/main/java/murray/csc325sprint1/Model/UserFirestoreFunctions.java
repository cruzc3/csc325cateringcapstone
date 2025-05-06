package murray.csc325sprint1.Model;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class UserFirestoreFunctions {
    private static volatile UserFirestoreFunctions instanceOfUserFirestore;
    private final Firestore db;
    private static final String USER_COLLECTION = "Users";

    // Private constructor to prevent instantiation
    private UserFirestoreFunctions() {
        db = FirestoreContext.getInstance().getFirestore();
    }

    // Public method to provide access to the singleton instance
    public static UserFirestoreFunctions getInstance() {
        if (instanceOfUserFirestore == null) {
            synchronized (UserFirestoreFunctions.class) {
                if (instanceOfUserFirestore == null) {
                    instanceOfUserFirestore = new UserFirestoreFunctions();
                }
            }
        }
        return instanceOfUserFirestore;
    }

    public void insertUser(User u) {
        try {
            Map<String, Object> userItem = new HashMap<>();
            userItem.put("first name", u.getfName());
            userItem.put("last name", u.getlName());
            userItem.put("email", u.getEmail());
            userItem.put("security question", u.getSecQuestion());
            userItem.put("security answer", u.getSecAnswer());
            userItem.put("is employee", u.isEmployee());

            String hashedPassword = hashPassword(u.getPassword());
            userItem.put("password", hashedPassword);

            String documentId = u.getEmail().toLowerCase();
            db.collection(USER_COLLECTION).document(documentId).set(userItem).get();
        } catch (Exception e) {
            System.err.println("Error inserting user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUser(User u) {
        try {
            String documentId = u.getEmail().toLowerCase();
            DocumentReference docRef = db.collection(USER_COLLECTION).document(documentId);
            DocumentSnapshot snapshot = docRef.get().get();

            if (snapshot.exists()) {
                docRef.delete().get();
            } else {
                showAlert("User not found", "User cannot be found in the database. Ensure you're searching for the correct user.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(User u) {
        try {
            String documentId = u.getEmail().toLowerCase();
            Map<String, Object> updates = new HashMap<>();
            updates.put("first name", u.getfName());
            updates.put("last name", u.getlName());
            updates.put("security question", u.getSecQuestion());
            updates.put("security answer", u.getSecAnswer());
            updates.put("is employee", u.isEmployee());

            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                updates.put("password", hashPassword(u.getPassword()));
            }

            db.collection(USER_COLLECTION).document(documentId).update(updates).get();
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public User findUser(String email) {
        try {
            DocumentSnapshot snapshot = db.collection(USER_COLLECTION).document(email).get().get();
            if (snapshot.exists()) {
                return new User(
                        snapshot.getString("first name"),
                        snapshot.getString("last name"),
                        snapshot.getString("email"),
                        snapshot.getString("security question"),
                        snapshot.getString("security answer"),
                        snapshot.getString("password")
                );
            }
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public boolean verifyLogin(String email, String plainPassword) {
        try {
            String documentId = email.toLowerCase();
            DocumentSnapshot snapshot = db.collection(USER_COLLECTION).document(documentId).get().get();

            if (snapshot.exists()) {
                String storedHashedPassword = snapshot.getString("password");
                return verifyPassword(plainPassword, storedHashedPassword);
            }
        } catch (Exception e) {
            System.err.println("Login verification error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
