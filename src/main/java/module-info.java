module Attendance.with.NFC {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;
    requires java.smartcardio;
    requires java.desktop;
    requires telegrambots;
    requires telegrambots.meta;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    opens AwNFC;
}