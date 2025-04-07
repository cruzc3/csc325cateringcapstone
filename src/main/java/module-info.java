module murray.csc325sprint1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires firebase.admin;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires google.cloud.core;
    requires com.google.api.apicommon;

    // Open package to FXML
    opens murray.csc325sprint1 to javafx.fxml;
    exports murray.csc325sprint1;
}