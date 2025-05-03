package de.hsmittweida.pawnstopower;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartMenu {
	StartMenu(Stage stage) {
			VBox root = new VBox(15);
			Scene scene = new Scene(root,400,500);
			//scene.getStylesheets().add(getClass().getResource("style_mainmenu.css").toExternalForm());
			Tools.addStylesheet(scene, "style_mainmenu.css");
			stage.setScene(scene);

			/* Musik im Startmeu */
			SoundManager startmenusound = new SoundManager("StartMenuTheme.mp3");
			stage.setOnHiding(e -> {
				startmenusound.getMediaPlayer().stop();
			});

			Label title = new Label("Pawn to Power");
			Button newGame = new Button("Neues Spiel");
			Button loadGame = new Button("Spiel laden");
			Button setup = new Button("Setup");
			Button exit = new Button ("Exit");

			Tools.addButtonSfx(newGame,loadGame,setup,exit);
			
			newGame.setOnAction(e -> {
                new Game();
				stage.close();
			});

			loadGame.setOnAction(e-> {
				/*TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Spiel laden");
				dialog.setHeaderText("Gib den Pfad an!");
				dialog.setContentText("Es sollte eine \\\".ptpfile\\\" sein.");
				dialog.getDialogPane().getStylesheets().add(Game.class.getResource("style_dialog.css").toExternalForm());
				String res = "";
				try {
					res = dialog.showAndWait().get();
				} catch (NoSuchElementException nsex) {}*/
				String res = Tools.inputPopup("Spiel laden", "Gib den Pfad an!", "Es sollte eine \".ptpfile\" sein.");
			});

			exit.setOnAction(e -> {
				stage.close();
			});
			
			setup.setOnAction(e -> {
				System.out.println("lol");
			});
			
			root.setId("main-menu");
			root.getChildren().addAll(title,newGame, loadGame,setup,exit);
			root.setAlignment(Pos.CENTER);
			
			stage.show();
	}
}
