module Attendance.with.NFC {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;
    requires java.smartcardio;
    requires java.desktop;
    opens AwNFC;
}