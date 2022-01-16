module it.farrlor.zsi.zsi_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens it.farrlor.zsi.zsi_project to javafx.fxml;
    exports it.farrlor.zsi.zsi_project;
}