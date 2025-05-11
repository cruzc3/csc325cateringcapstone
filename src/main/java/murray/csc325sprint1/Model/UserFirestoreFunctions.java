package murray.csc325sprint1.Model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.scene.control.Alert;
import murray.csc325sprint1.FirestoreContext;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserFirestoreFunctions {
    private static volatile UserFirestoreFunctions instanceOfUserFirestore;
    private final Firestore db;
    static final String USER_COLLECTION = "Users";
    private static User currentUser;

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

    public static synchronized User getCurrentUser() {
        return currentUser;
    }

    private static synchronized void setCurrentUser(User u) {
        currentUser = u;
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
            userItem.put("is admin", u.isAdmin());
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

    public void updateUserPassword(User u) {
        try {
            String documentId = u.getEmail().toLowerCase();
            Map<String, Object> updates = new HashMap<>();

            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                updates.put("password", hashPassword(u.getPassword()));
            }

            db.collection(USER_COLLECTION).document(documentId).update(updates).get();
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void promoteToEmployee(User u) {
        try {
            String documentId = u.getEmail().toLowerCase();
            Map<String, Object> updates = new HashMap<>();
            u.setEmployee(true);
            updates.put("is employee", u.isEmployee());
            db.collection(USER_COLLECTION).document(documentId).update(updates).get();
        } catch (Exception e) {
            System.err.println("Error updating user employee status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void demoteToCustomer(User u) {
        try {
            String documentId = u.getEmail().toLowerCase();
            Map<String, Object> updates = new HashMap<>();
            u.setEmployee(false);
            updates.put("is employee", u.isEmployee());
            db.collection(USER_COLLECTION).document(documentId).update(updates).get();
        } catch (Exception e) {
            System.err.println("Error updating user employee status: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public User findUser(String email) {
        try {
            DocumentSnapshot snapshot = db.collection(USER_COLLECTION).document(email).get().get();
            if (snapshot.exists()) {
                String firstName = snapshot.getString("first name");
                String lastName = snapshot.getString("last name");
                String userEmail = snapshot.getString("email");
                String secQuestion = snapshot.getString("security question");
                String secAnswer = snapshot.getString("security answer");
                Boolean isEmployee = snapshot.getBoolean("is employee");
                Boolean isAdmin = snapshot.getBoolean("is admin");

                return new User(
                        firstName,
                        lastName,
                        userEmail,
                        secQuestion,
                        secAnswer,
                        Boolean.TRUE.equals(isEmployee)
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
                if (verifyPassword(plainPassword, storedHashedPassword)) {
                    setCurrentUser(findUser(email));
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Login verification error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllEmployees() {
        List<User> employeeList = new LinkedList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(USER_COLLECTION).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                Boolean isEmployee = doc.getBoolean("is employee");
                if (Boolean.TRUE.equals(isEmployee)) {
                    String firstName = doc.getString("first name");
                    String lastName = doc.getString("last name");
                    String email = doc.getString("email");
                    Boolean isAdmin = doc.getBoolean("is admin");

                    User user = new User(
                            firstName,
                            lastName,
                            email,
                            true,
                            isAdmin
                    );
                    employeeList.add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }
        return employeeList;
    }
}
