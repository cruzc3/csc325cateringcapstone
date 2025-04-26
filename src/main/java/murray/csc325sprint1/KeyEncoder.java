package murray.csc325sprint1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

/**
 * Utility class to encode key.json to base64 and save to .env file
 */
public class KeyEncoder {

    public static void main(String[] args) {
        try {
            // Find the key.json file
            File keyFile = new File("src/main/resources/murray/csc325sprint1/key.json");

            if (!keyFile.exists()) {
                System.err.println("key.json not found at: " + keyFile.getAbsolutePath());
                System.exit(1);
            }

            // Read the key file
            String keyContent = readFile(keyFile);

            // Encode to base64
            String encodedKey = Base64.getEncoder().encodeToString(keyContent.getBytes(StandardCharsets.UTF_8));

            // Create config directory if it doesn't exist
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdir();
            }

            // Save to .env file
            File envFile = new File("config/.env");
            try (FileOutputStream fos = new FileOutputStream(envFile)) {
                String content = "# Firebase Configuration\n" +
                        "FIREBASE_KEY=" + encodedKey + "\n";
                fos.write(content.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Successfully encoded key.json and saved to config/.env");
            System.out.println("You can now safely remove the key.json file from your repository if needed.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        }
        return content.toString();
    }
}
