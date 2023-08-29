module com.example.jumpnow {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.jumpnow to javafx.fxml;
    exports com.example.jumpnow;
}