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
                InputStream serviceAccount =
                        getClass().getResourceAsStream("src/main/resources/murray/csc325/sprint1/key.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return FirestoreClient.getFirestore();
    }
}