package murray.csc325sprint1.Model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import murray.csc325sprint1.CustomerMenuController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for common functionality across the application
 */
public class Util {

    // Current logged-in user
    private static User currentUser;

    /**
     * Navigate to the appropriate menu screen based on user type
     *
     * @param stage The current stage
     * @param user The logged-in user
     * @throws IOException If the FXML file cannot be loaded
     */
    public static void navigateToAppropriateMenu(Stage stage, User user) throws IOException {
        currentUser = user;

        String fxmlPath;
        if (user.isEmployee()) {
            fxmlPath = "/murray/csc325sprint1/emp-main.fxml";
        } else {
            fxmlPath = "/murray/csc325sprint1/customer-main.fxml";
        }

        FXMLLoader loader = new FXMLLoader(Util.class.getResource(fxmlPath));
        Parent root = loader.load();

        // Pass user details to controller if needed
        if (!user.isEmployee() && loader.getController() instanceof CustomerMenuController) {
            // If we need to pass user info to the customer controller
            CustomerMenuController controller = loader.getController();
            // You could add methods to the controller to set user information if needed
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("CAK Catering");
        stage.show();
    }

    /**
     * Get the currently logged-in user
     *
     * @return The current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the current user
     *
     * @param user The user to set as current
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Read data from a CSV file
     *
     * @param fileName The file to read
     * @return A map of parsed data
     */
    public static Map<String, Object> readFile(String fileName) {
        Map<String, Object> data = new HashMap<>();

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                System.err.println("File not found: " + fileName);
                return data;
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();

            String content = new String(buffer);
            String[] lines = content.split("\\r?\\n");

            // Parse header
            if (lines.length > 0) {
                String[] headers = lines[0].split(",");

                // Parse data rows
                for (int i = 1; i < lines.length; i++) {
                    String[] values = lines[i].split(",");
                    Map<String, String> row = new HashMap<>();

                    for (int j = 0; j < Math.min(headers.length, values.length); j++) {
                        row.put(headers[j].trim(), values[j].trim());
                    }

                    data.put("row_" + i, row);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Write data to a backup file
     *
     * @param fileName The file to write
     * @param content The content to write
     */
    public static void writeFile(String fileName) {
        try {
            // Create output directory if it doesn't exist
            File outputDir = new File("backup");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            // Get the original file
            File inputFile = new File(fileName);
            if (!inputFile.exists()) {
                System.err.println("File not found: " + fileName);
                return;
            }

            // Create backup file name
            String baseName = inputFile.getName();
            int dotIndex = baseName.lastIndexOf('.');
            String nameWithoutExt = (dotIndex == -1) ? baseName : baseName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? ".dat" : ".dat";
            String backupFileName = "backup/" + nameWithoutExt + extension;

            // Copy file content
            FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(backupFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fis.close();
            fos.close();

            System.out.println("Backup saved to: " + backupFileName);

        } catch (IOException e) {
            System.err.println("Error writing backup file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
