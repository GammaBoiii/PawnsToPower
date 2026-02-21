/**
 * Module-Info für das Projekt
 */
module de.hsmittweida.pawnstopower {
    requires javafx.controls;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;


    opens de.hsmittweida.pawnstopower to javafx.fxml;
    exports de.hsmittweida.pawnstopower;
}