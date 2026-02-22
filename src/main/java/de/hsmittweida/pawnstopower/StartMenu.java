package de.hsmittweida.pawnstopower;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Das Startmenu ist das erste, was der Spieler nach Programmstart sieht.
 * Dabei kann zwischen dem Start eines neuen Spielstands und dem Laden eines
 * bestehenden gewählt werden.
 * Alternativ hat der Spieler die Möglichkeit, das Programm an der Stelle
 * zu beenden.
 */
public class StartMenu {

	/**
	 * Konstruktor der StartMenu Klasse.
	 * @param stage primaryStage
	 */
	StartMenu(Stage stage) {
			VBox root = new VBox(15);
			Scene scene = new Scene(root,400,500);
			Tools.addStylesheet(scene, "style_mainmenu.css");
			stage.setScene(scene);

			/* Musik im Startmeu */
			SoundManager startmenusound = new SoundManager("StartMenuTheme.wav");
			stage.setOnHiding(e -> {
				startmenusound.getMediaPlayer().stop();
			});

			Label title = new Label("Pawn to Power");
			Button newGame = new Button("Neues Spiel");
			Button loadGame = new Button("Spiel laden");
			Button setup = new Button("Setup"); //WIP
            setup.setDisable(true);
			Button exit = new Button ("Exit");

			Tools.addButtonSfx(newGame,loadGame,exit);

			newGame.setOnAction(e -> {
                Game.Game_view();
				stage.close();
			});

			/* Hier kann der Spieler einen Pfad angeben, um ein bestehenden Spielstand
			* zu laden. Dabei wird nur nach dem Pfad in den Ordner, in dem der
			* Spielstand liegt, gefragt!
			* */
			loadGame.setOnAction(e-> {
				String res = Tools.inputPopup("Spiel laden", "Gib den Pfad an!", "Es sollte eine \"savegame.p2p\" Datei enthalten sein.");
                System.out.println(res);
                String str = "";
                Game.Game_view();
                if(res.endsWith("/")) {
                    str = "savegame.p2p";
                    if(Save.loadAll(res + str)) {
                        Tools.popup("Laden", "Datei wird geladen!", "Der Pfad wurde festgelegt auf: " + res + str);
                        stage.close();
                    } else {
                        Game.getStage().close();
                    }
                } else {
                    str = "/savegame.p2p";
                    if(Save.loadAll(res + str)) {
                        Tools.popup("Laden", "Datei wird geladen!", "Der Pfad wurde festgelegt auf: " + res + str);
                        stage.close();
                    } else {
                        Game.getStage().close();
                    }
                }
            });

			exit.setOnAction(e -> {
				stage.close();
			});

			setup.setOnAction(e -> {
				new SetupMenu();
			});

			root.setId("main-menu");
			root.getChildren().addAll(title, newGame, loadGame, setup, exit);
			root.setAlignment(Pos.CENTER);

			stage.show();
	}
}
