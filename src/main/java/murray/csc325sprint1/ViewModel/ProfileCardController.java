package murray.csc325sprint1.ViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import murray.csc325sprint1.Model.User;

public class ProfileCardController {

    @FXML
    private Label adminLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label employeeLbl;

    @FXML
    private Label fNameLbl;

    @FXML
    private Label lNameLbl;

    @FXML
    private AnchorPane profileCard;

    public void setUserData(User u) {
        fNameLbl.setText(u.getfName());
        lNameLbl.setText(u.getlName());
        emailLbl.setText(u.getEmail());
        employeeLbl.setText(u.isEmployee() ? "Yes" : "No");
        adminLbl.setText(u.isAdmin() ? "Yes" : "No");
    }
}
