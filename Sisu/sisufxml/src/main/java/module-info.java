/**
 * the module of package of Programming 3 project work
 */
module fi.tuni.prog3.sisufxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires org.jsoup;
    requires javafx.web;
    requires java.desktop;


    opens fi.tuni.prog3.sisufxml to javafx.fxml;
    exports fi.tuni.prog3.sisufxml;
}
