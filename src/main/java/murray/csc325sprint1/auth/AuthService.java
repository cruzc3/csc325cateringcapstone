package murray.csc325sprint1.auth;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.*;
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

    public boolean isUsernameAvailable(String username) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = db.collection("usernames")
                .whereEqualTo("username", username.toLowerCase())
                .get();
        return query.get().isEmpty();
    }

    public UserRecord register(String email, String password, String username,
                               String fullName, String userType)
            throws FirebaseAuthException, ExecutionException, InterruptedException {

        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Create auth user
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        UserRecord userRecord = auth.createUser(request);

        // Store additional info in Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);
        userData.put("fullName", fullName);
        userData.put("userType", userType);

        db.collection("users").document(userRecord.getUid()).set(userData).get();

        // Reserve username
        Map<String, Object> usernameData = new HashMap<>();
        usernameData.put("username", username.toLowerCase());
        usernameData.put("userId", userRecord.getUid());
        db.collection("usernames").document(username.toLowerCase()).set(usernameData).get();

        return userRecord;
    }

    public UserRecord login(String email, String password) throws FirebaseAuthException {
        // In real app, use signInWithEmailAndPassword
        return auth.getUserByEmail(email);
    }
}