module com.example.edytor_tekstu {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.edytor_tekstu to javafx.fxml;
    exports com.example.edytor_tekstu;
}