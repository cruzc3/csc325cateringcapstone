// src/main/java/murray/csc325sprint1/auth/AccessDataView.java
package murray.csc325sprint1.auth;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccessDataView {
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ReadOnlyBooleanWrapper writePossible = new ReadOnlyBooleanWrapper();

    public AccessDataView() {
        writePossible.bind(email.isNotEmpty().and(password.isNotEmpty()));
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public ReadOnlyBooleanProperty isWritePossibleProperty() {
        return writePossible.getReadOnlyProperty();
    }
}