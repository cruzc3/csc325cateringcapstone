package murray.csc325sprint1.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import murray.csc325sprint1.MainApp;

import static murray.csc325sprint1.Model.ViewPaths.ADMIN_MAIN;
import static murray.csc325sprint1.Model.ViewPaths.INIT_SCREEN;

public class TestInitApp extends Application {
    private static final UserFirestoreFunctions instanceOfUserFirestore = UserFirestoreFunctions.getInstance();
    @Override
    public void start(Stage stage) throws Exception {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(INIT_SCREEN));
//                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(ADMIN_MAIN));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Jack's Catering Service");
                stage.setScene(scene);
                stage.show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    public static void main(String[] args) {
        //Account creation test
        User admin = new User("admin","admin", "admin@farmingdale.edu");
        User customer = new User("customer","customer", "customer@farmingdale.edu","customer","customer", "customer");
        User customerToEmployee = new User("customerToEmployee","customerToEmployee", "customerToEmployee@farmingdale.edu","customerToEmployee","customerToEmployee", "customerToEmployee");
        User employeeToCustomer = new User("employeeToCustomer","employeeToCustomer", "employeeToCustomer@farmingdale.edu","employeeToCustomer","employeeToCustomer", "employeeToCustomer");
        User deletedUser = new User("deletedUser","deletedUser", "deletedUser@farmingdale.edu","deletedUser","deletedUser", "deletedUser");
        User updatedUser = new User("updatedUser","updatedUser", "updatedUser@farmingdale.edu","updatedUser","updatedUser", "updatedUser");
        //update to fix test
        employeeToCustomer.setEmployee(true);

        //array of users to be inserted into the database
        User[] users = {admin,customer,customerToEmployee,employeeToCustomer,deletedUser,updatedUser};
        for(int i = 0; i < users.length; i++){
            instanceOfUserFirestore.insertUser(users[i]);
        }

        launch(args);
    }
}
