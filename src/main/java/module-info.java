module sample.bettingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens sample.bettingproject to javafx.fxml;
    exports sample.bettingproject;
}