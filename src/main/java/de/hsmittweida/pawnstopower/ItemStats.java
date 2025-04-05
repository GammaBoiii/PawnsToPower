package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ItemStats {
    ItemStats(Weapon w) {
        Stage stage = new Stage();
        VBox box = new VBox();
        Scene s = new Scene(box, 400, 150);

        Label nameLabel = new Label("Name:");
        Label nameVal = new Label(w.getName());
        HBox name = new HBox();
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        name.getChildren().addAll(nameLabel, nameVal);
        name.setPadding(new Insets(0, 0, 15.0, 0));

        Label wClassLabel = new Label("Klasse:");
        Label wClassVal = new Label(w.getWClass().toString());
        wClassVal.setOnMousePressed(e-> {
            System.out.print("Das ist eine Waffe. DEBUG!");
        });
        HBox wClass = new HBox();
        HBox.setHgrow(wClassLabel, Priority.ALWAYS);
        wClassLabel.setMaxWidth(Double.MAX_VALUE);
        wClass.getChildren().addAll(wClassLabel, wClassVal);
        wClass.setPadding(new Insets(0, 0, 15.0, 0));

        Label damageLabel = new Label("Schaden:");
        Label damageVal = new Label(String.valueOf(w.getTotalDamage()));
        HBox damage = new HBox();
        HBox.setHgrow(damageLabel, Priority.ALWAYS);
        damageLabel.setMaxWidth(Double.MAX_VALUE);
        damage.getChildren().addAll(damageLabel, damageVal);
        damage.setPadding(new Insets(0.0, 0, 15.0, 0));

        Label twoHandedVal = new Label(w.isTwoHanded() ? "Zweihänder" : "Einhänder");

        box.getChildren().addAll(name, wClass, damage, twoHandedVal);

        Tools.addStylesheet(s, "style_itemstats.css");
        stage.setScene(s);
        stage.show();

    }

    ItemStats(Armor c) {

    }
}
