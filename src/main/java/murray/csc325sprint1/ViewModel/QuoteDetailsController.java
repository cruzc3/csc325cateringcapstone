package murray.csc325sprint1.ViewModel;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import murray.csc325sprint1.Order;

public class QuoteDetailsController implements Initializable {

    @FXML private Label totalLabel;
    @FXML private VBox quotedItemsContainer;
    @FXML private Button closeButton;
    // No print button anymore
    @FXML private Label quoteExplanationLabel;

    private Order currentQuote;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No initialization needed for now
    }

    /**
     * Set the quote to display in this dialog
     *
     * @param quote The quote to display
     */
    public void setQuote(Order quote) {
        this.currentQuote = quote;

        // Update the UI with quote details
        updateQuoteDetails();
    }

    /**
     * Update the UI with the current quote details
     */
    private void updateQuoteDetails() {
        if (currentQuote == null) {
            return;
        }

        // Set the total
        totalLabel.setText("Total: " + currentQuote.getFormattedTotal());

        // Clear existing items
        quotedItemsContainer.getChildren().clear();

        // Add each ordered item
        for (Map.Entry<String, Integer> entry : currentQuote.getOrderItems().entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            // Create a label for this item
            Label itemLabel = new Label(itemName + " (x" + quantity + ")");
            itemLabel.setStyle("-fx-font-size: 14;");

            // Add to container
            quotedItemsContainer.getChildren().add(itemLabel);
        }

        // Set explanation text
        if (quoteExplanationLabel != null) {
            quoteExplanationLabel.setText("This is a price quote only. To place an actual order, " +
                    "please go to the order section from the main menu.");
        }
    }

    /**
     * Handle the close button click
     */
    @FXML
    private void closeDialog(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}