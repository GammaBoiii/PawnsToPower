package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

/**
 * Die Barracks-Klasse dient dem Anzeigen der Kämpfer im Besitz des Spielers.
 * Eine schnelle Übersicht gibt Hinweis auf Name und Level eines jeden Kämpfers
 * im Inventar des Spielers.
 */
public class Barracks {

    /**
     * Die Anzeige, die alle Kämpfer im Besitz anzeigt.
     * @return Pane, welches dann in {@code Game.drawSpace()} angezeigt wird.
     */
    public static Pane Barrack_view() {
        AnchorPane background = new AnchorPane();
        background.setId("pane");
        Tools.addStylesheet(background, "style_barracks.css");
        Button mainMenu = new Button("Hauptmenu");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });

        ScrollPane sp = new ScrollPane();
        sp.setId("background");
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        sp.setPadding(new Insets(15, 25, 15, 25));
        AnchorPane.setTopAnchor(sp, 37.0);
        AnchorPane.setLeftAnchor(sp, 0.0);
        AnchorPane.setRightAnchor(sp, 0.0);
        AnchorPane.setBottomAnchor(sp, 0.0);
        background.getChildren().addAll(mainMenu, sp);

        VBox list = new VBox();
        list.setMaxWidth(Double.MAX_VALUE);
        list.setId("pawns-list");

        sp.setContent(list);
        sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        for (Pawn p : Inventory.getPawns()) {
            HBox box = new HBox();
            Label name = new Label(p.getName() + " - Level " + p.getLvl()); //p.getLevel());
            Button inspect = new Button("Details");

            box.setPadding(new Insets(0, 0, 15, 0));
            name.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(name, Priority.ALWAYS);
            HBox.setHgrow(inspect, Priority.ALWAYS);
            box.getChildren().addAll(name,  inspect);

            inspect.setOnAction(e -> {
                Game.drawSpace(Inspector.Inspector_view(p));
                Inspector.refreshImages();
            });

            list.getChildren().add(box);
        }
        Label info = new Label("Neue Pawns kannst du durch erfolgreiche Arenakämpfe gewinnen!");
        info.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: rgb(175,175,175)");
        info.setTextAlignment(TextAlignment.CENTER);
        info.setAlignment(Pos.CENTER);
        info.setMaxWidth(Double.MAX_VALUE);
        list.getChildren().add(info);

        return background;
    }
}
