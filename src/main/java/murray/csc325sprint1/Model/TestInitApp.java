package murray.csc325sprint1.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import murray.csc325sprint1.MainApp;

import static murray.csc325sprint1.Model.ViewPaths.INIT_SCREEN;

public class TestInitApp extends Application {
    private static final UserFirestoreFunctions instanceOfUserFirestore = UserFirestoreFunctions.getInstance();
    @Override
    public void start(Stage stage) throws Exception {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(INIT_SCREEN));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Jack's Catering Service");
                stage.setScene(scene);
                stage.show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    public static void main(String[] args) {
        launch(args);
    }
}
