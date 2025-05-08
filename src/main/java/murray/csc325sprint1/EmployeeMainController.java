package murray.csc325sprint1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class EmployeeMainController  {

    @FXML
    private Button EmpContact;

    @FXML
    private Button EmpInventory;

    @FXML
    private Button EmpLogOut;

    @FXML
    private Button EmpOrders;

    @FXML
    private Button EmpSchedule;

    @FXML
    private void initialize() {
        EmpContact.setOnAction(event -> ContactBtnClicked());
    }

    private void ContactBtnClicked() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/murray/csc325sprint1/emp-contact.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error loading contact screen");
        }
    }


}

