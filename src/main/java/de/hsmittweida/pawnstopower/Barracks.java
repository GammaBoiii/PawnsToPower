package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Barracks {
    Barracks() {
        Stage stage = new Stage();
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        Scene s = new Scene(sp, 350, 450);
        Tools.addStylesheet(s, "style_barracks.css");
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
            box.getChildren().addAll(name, inspect);
            name.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(name, Priority.ALWAYS);
            HBox.setHgrow(inspect, Priority.ALWAYS);

            inspect.setOnAction(e -> {
                new Inspector(p);
            });

            list.getChildren().add(box);
        }


        stage.setTitle("Kämpfer");
        stage.setScene(s);
        stage.show();
        Tools.defaultClose(stage, "barracks");
    }
}
