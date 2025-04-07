module murray.csc325sprint1 {

    requires com.google.cloud.firestore;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.cloud.core;
    requires com.google.api.apicommon;
    requires io.grpc.api;
    requires com.google.common;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens murray.csc325sprint1 to javafx.fxml;
    exports murray.csc325sprint1;
}