package murray.csc325sprint1.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.*;
import com.google.firebase.cloud.FirestoreClient;
import murray.csc325sprint1.FirestoreContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AuthService {
    private final FirebaseAuth auth;
    private final Firestore db;

    public AuthService() {
        this.auth = FirebaseAuth.getInstance();
        this.db = new FirestoreContext().firebase();
    }

    public boolean register(String email, String password, String fullName) {
        try {
            // 1. Create Firebase authentication user
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);
            UserRecord userRecord = auth.createUser(request);

            // 2. Store additional user data in Firestore
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("fullName", fullName);
            userData.put("createdAt", System.currentTimeMillis());
            userData.put("role", "customer"); // Default role

            // 3. Save to "users" collection with UID as document ID
            db.collection("users").document(userRecord.getUid())
                    .set(userData)
                    .get(); // Wait for completion

            return true;
        } catch (FirebaseAuthException | InterruptedException | ExecutionException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public boolean login(String email, String password) {
        try {
            // In a real implementation, you would use signInWithEmailAndPassword
            // This is simplified for demonstration
            UserRecord user = auth.getUserByEmail(email);
            return user != null;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }
}