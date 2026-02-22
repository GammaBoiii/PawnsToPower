package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Quickmenu ist ein kleines Popup, verschieden Einstellungen anzeigt, die Möglichkeit
 * gibt, das Spiel zu speichern oder zu laden und einen Cheat zu aktivieren.
 */
public class QuickMenu {

    /**
     * Konstruktor, der das Pop-Up Fenster erstellt.
     */
    public QuickMenu() {
        Stage stage = new Stage();
        VBox box = new VBox();
        box.setId("quickmenubox");
        Scene s = new Scene(box, 500,550);
        Tools.addStylesheet(s, "style_default.css");

        /* Vollbild toggle Button */
        VBox fullscreen = new VBox();
        fullscreen.setSpacing(10);
        fullscreen.setPadding(new Insets(0,25,15,25));
        Label fullscreen_label = new Label("Vollbild umschalten:");
        ToggleButton fullscreen_button = new ToggleButton("Vollbild");
        fullscreen_button.setSelected(Game.getStage().isFullScreen());
        fullscreen_button.setOnAction(e -> {
            Game.setFullscreen(fullscreen_button.isSelected());
        });
        fullscreen.getChildren().addAll(fullscreen_label, fullscreen_button);

        /* Speichern des Spiels */
        VBox save = new VBox();
        save.setSpacing(10);
        save.setPadding(new Insets(0,25,15,25));
        Label save_label = new Label("Spiel speichern (Nur Pfad):");
        TextField save_pathinput = new TextField();
        Button save_button = new Button("Speichern");
        save_pathinput.setPromptText("Bsp.: /home/myname/Documents/savegames/");
        save_button.setOnAction(e -> {
            //Save.saveAll("/home/johann/Documents/p2p/newsavetypelol.p2p");
            String str = "";
            if(save_pathinput.getText().endsWith("/")) {
                //save_pathinput.setText(save_pathinput.getText() + "savegame.p2p");
                str = "savegame.p2p";
                if(Save.saveAll(save_pathinput.getText() + str)) {
                    Tools.popup("Speichern", "Datei wird gespeichert!", "Der Pfad wurde festgelegt auf: " + save_pathinput.getText() + str);
                }
            } else {
                //save_pathinput.setText(save_pathinput.getText() + "/savegame.p2p");
                str = "/savegame.p2p";
                if(Save.saveAll(save_pathinput.getText() + str)) {
                    Tools.popup("Speichern", "Datei wird gespeichert!", "Der Pfad wurde festgelegt auf: " + save_pathinput.getText() + str);
                }
            }
            //Save.saveAll(save_pathinput.getText());
        });
        save.getChildren().addAll(save_label, save_pathinput, save_button);

        /* Laden eines Spielstandes */
        VBox load = new VBox();
        load.setSpacing(10);
        load.setPadding(new Insets(0,25,15,25));
        Label load_label = new Label("Spiel laden (Nur Pfad):");
        TextField load_pathinput = new TextField();
        Button load_button = new Button("Laden");
        load_pathinput.setPromptText("Bsp.: /home/myname/Documents/p2p/");
        load_button.setOnAction(e -> {
            //Save.loadAll("/home/johann/Documents/p2p/newsavetypelol.p2p");
            String str = "";
            if(load_pathinput.getText().endsWith("/")) {
                str = "savegame.p2p";
                if(Save.loadAll(load_pathinput.getText() + str)) {
                    Tools.popup("Laden", "Datei wird geladen!", "Der Pfad wurde festgelegt auf: " + load_pathinput.getText() + str);
                    stage.close();
                }
            } else {
                str = "/savegame.p2p";
                if(Save.loadAll(load_pathinput.getText() + str)) {
                    Tools.popup("Laden", "Datei wird geladen!", "Der Pfad wurde festgelegt auf: " + load_pathinput.getText() + str);
                    stage.close();
                }
            }

        });
        load.getChildren().addAll(load_label, load_pathinput, load_button);

        /* Cheat Aktivierung */
        Button cheat = new Button("Cheat");
        cheat.setOnAction(e -> {
            String in = Tools.inputPopup("Cheat-Aktivierung!", "Top SECRET Passwort erforderlich!", "Passwort eingeben:");
            if(in != null && in.equals("helloworld")) {
                Tools.popup("Erfolgreich.", "Passwort erraten!", "Cheat aktiviert.");
                Inventory.addMoney(424);
                Inventory.addPawn(new Pawn(true));
                Inventory.addItem(new Armor());
                Inventory.addItem(new Armor());
                Inventory.addItem(new Armor());
                Inventory.addItem(new Armor());
                Inventory.addItem(new Armor());
                Inventory.getPawns().get(0).addXp(65);
                Inventory.addReputation(15);
            } else {
                Tools.popup("Fehler.", "Passwort falsch!", ":(");
            }
        });

        /* Spiel beenden Button */
        Button quit = new Button("Spiel Beenden");
        quit.setOnAction(e -> {
            Game.close();
            stage.close();
        });

        box.getChildren().addAll(new VBox(),fullscreen, save, load, cheat, quit, new VBox());

        /* Auf alle Elemente die growth-Prio setzen */
        for(Node n: box.getChildren()) {
            VBox.setVgrow(n, Priority.ALWAYS);
        }

        box.setAlignment(Pos.CENTER);
        box.setSpacing(15);

        Tools.defaultClose(stage, "quickmenu" );
        stage.setScene(s);
        stage.show();
    }
}
