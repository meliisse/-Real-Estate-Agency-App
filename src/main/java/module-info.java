module com.app.app_rea {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.app.app_rea to javafx.fxml;
    exports com.app.app_rea;
}