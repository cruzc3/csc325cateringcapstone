package murray.csc325sprint1;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;
import java.io.IOException;

public class FirestoreContext {

    public Firestore firebase() {
        try {
            // Check if Firebase is already initialized to avoid multiple instances
            if (FirebaseApp.getApps().isEmpty()) {
                // Get Firebase credentials from environment variable instead of file
                InputStream serviceAccount = SimpleConfig.getInstance().getFirebaseKeyAsStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Failed to initialize Firebase: " + ex.getMessage());
            System.exit(1);
        }
        return FirestoreClient.getFirestore();
    }
}