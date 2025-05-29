package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class Arena {

    private static Pawn choosenFighter;
    private static Pawn Enemy;

    public static Pane chooseFighter() {
        AnchorPane pane = new AnchorPane();
        Label label = new Label("Wähle deinen Kämpfer aus!");
        label.setStyle("-fx-border-color: red; -fx-border-width: 2"); //Debug
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        ScrollPane sp = new ScrollPane();
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        VBox fighters = new VBox();
        fighters.setMaxWidth(Double.MAX_VALUE);
        for (Pawn p : Inventory.getPawns()) {


            Label name = new Label(p.getName());
            Label spacer = new Label("\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-");

            Region spacerR = new Region();
            HBox.setHgrow(spacerR, Priority.ALWAYS);
            spacerR.setStyle("""
                        -fx-border-color: gray transparent gray transparent;
                        -fx-border-width: 0 0 1 0;
                        -fx-translate-y: 0;
                    """);
            Button choose = new Button("Kampf!");

            choose.setOnAction(e -> {
                choosenFighter = p;
                Game.drawSpace(Arena.arenaFight());
            });

            HBox fighter = new HBox(10, name, spacerR, choose);
            fighter.setPadding(new Insets(20,0,0,0));

            fighters.getChildren().add(fighter);
        }
        sp.setContent(fighters);
        AnchorPane.setLeftAnchor(sp, 20.0);
        AnchorPane.setRightAnchor(sp, 20.0);
        AnchorPane.setTopAnchor(sp, 80.0);
        AnchorPane.setBottomAnchor(sp, 25.0);
        AnchorPane.setTopAnchor(label, 50.0);
        AnchorPane.setLeftAnchor(label, 25.0);
        AnchorPane.setRightAnchor(label, 25.0);
        pane.getChildren().addAll(label, sp);
        return pane;
    }

    public static Pane arenaFight() {
        AnchorPane pane = new AnchorPane();

        return pane;
    }

    /**
     * Generiert basierend auf dem eigenen Kämpfer einen Gegener, der innerhlabt eines gewissen Bereiches ungefähr so stark ist.
     */
    private static Pawn generateEnemy() {
        Pawn p = new Pawn();


        return p;
    }
}
