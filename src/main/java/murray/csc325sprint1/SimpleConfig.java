package murray.csc325sprint1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

/**
 * A simple configuration class to manage environment variables
 * without external dependencies or module requirements
 */
public class SimpleConfig {
    private static SimpleConfig instance;
    private Properties properties = new Properties();

    private SimpleConfig() {
        loadEnvFile();
    }

    public static SimpleConfig getInstance() {
        if (instance == null) {
            instance = new SimpleConfig();
        }
        return instance;
    }

    /**
     * Load environment variables from .env file
     */
    private void loadEnvFile() {
        // Try to load from config/.env
        File envFile = new File("config/.env");

        if (!envFile.exists()) {
            // Try alternate locations
            envFile = new File(".env");
            if (!envFile.exists()) {
                System.out.println("No .env file found. Using system environment variables.");
                return;
            }
        }

        try (FileInputStream fis = new FileInputStream(envFile)) {
            properties.load(fis);
            System.out.println("Loaded environment variables from " + envFile.getPath());
        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }

    /**
     * Get environment variable value
     *
     * @param key The environment variable key
     * @return The value or null if not found
     */
    public String get(String key) {
        // Check properties first
        String value = properties.getProperty(key);

        // If not found in properties, check system environment
        if (value == null) {
            value = System.getenv(key);
        }

        return value;
    }

    /**
     * Get environment variable value with default fallback
     *
     * @param key The environment variable key
     * @param defaultValue Default value if key not found
     * @return The value or defaultValue if not found
     */
    public String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null) ? value : defaultValue;
    }

    /**
     * Convert base64 encoded Firebase key JSON to InputStream
     *
     * @return InputStream containing the Firebase service account key
     * @throws IOException If the key cannot be decoded
     */
    public InputStream getFirebaseKeyAsStream() throws IOException {
        String base64Key = get("FIREBASE_KEY");

        if (base64Key == null || base64Key.isEmpty()) {
            // Try to read directly from a key file as fallback
            File keyFile = new File("src/main/resources/murray/csc325sprint1/key.json");
            if (keyFile.exists()) {
                System.out.println("Using key.json file as fallback");
                return new FileInputStream(keyFile);
            }

            throw new IOException("FIREBASE_KEY environment variable is not set and key.json not found");
        }

        try {
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
            return new ByteArrayInputStream(decodedKey);
        } catch (IllegalArgumentException e) {
            throw new IOException("Failed to decode FIREBASE_KEY: " + e.getMessage());
        }
    }
}
