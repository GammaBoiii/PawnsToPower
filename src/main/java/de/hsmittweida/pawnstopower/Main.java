package de.hsmittweida.pawnstopower;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main.
 * Startpunkt des Programms.
 * StartetPunkt von JavaFX.
 * Lädt das Startmenu.
 */
public class Main extends Application {

    /**
     * main Methode.
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Startmethode für Startmenu.
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        new StartMenu(primaryStage);
        //Game.Game_view();
    }
}
