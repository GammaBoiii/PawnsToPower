package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Barracks {

    public static Pane Barrack_view() {
        AnchorPane background = new AnchorPane();
        background.setId("pane");
        background.setMaxWidth(Double.MAX_VALUE);
        background.setMaxHeight(Double.MAX_VALUE);
//        background.setAlignment(Pos.CENTER);
        Tools.addStylesheet(background, "style_barracks.css");
        Button mainMenu = new Button("Hauptmenu");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });
        AnchorPane.setTopAnchor(mainMenu, 2.0);
        AnchorPane.setLeftAnchor(mainMenu, 2.0);
        ScrollPane sp = new ScrollPane();
        sp.setId("background");
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        sp.setPadding(new Insets(15, 25, 15, 25));
        AnchorPane.setTopAnchor(sp, 30.0);
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
            Label name = new Label(p.getName() + " - Level " + p.getLevel());
            Button inspect = new Button("Details");

            box.setPadding(new Insets(0, 0, 15, 0));

            name.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(name, Priority.ALWAYS);
            HBox.setHgrow(inspect, Priority.ALWAYS);
            box.getChildren().addAll(name,  inspect);

            inspect.setOnAction(e -> {
                Game.drawSpace(Inspector.Inspector_view(p));
            });

            list.getChildren().add(box);
        }
        return background;
    }
}
