package murray.csc325sprint1;

import java.time.LocalDate;
import javafx.scene.control.DateCell;

/**
 * This class is no longer needed since we've integrated the functionality
 * directly into OrderDetailsController using an anonymous DateCell implementation.
 *
 * However, we're keeping this class to maintain project structure, and it can
 * be removed in a future refactoring if desired.
 */
public class CustomDateCell extends DateCell {

    @Override
    public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);

        // Disable dates before tomorrow
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        setDisable(empty || date.compareTo(tomorrow) < 0);
    }

}