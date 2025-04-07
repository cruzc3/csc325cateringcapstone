package murray.csc325sprint1;

import java.time.LocalDate;
import javafx.scene.control.DateCell;

public class CustomDateCell extends DateCell {

    @Override
    public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);

        // Disable dates before tomorrow
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        setDisable(empty || date.compareTo(tomorrow) < 0);
    }

}
