package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Diese Klasse beinhaltet Methoden, um die Stats für ein Item anzuzeigen.
 */
public class ItemStats {

    /**
     * Öffnet ein Popup für eine Waffe zur Inspektion.
     * <br>
     * Zeigt Name, Klasse und Schaden der Waffe an, ebenso, ob es sich um ein Zweihänder handelt oder nicht.
     *
     * @param w Die Waffe, die isnpiziert werden soll.
     */
    ItemStats(Weapon w) {
        Stage stage = new Stage();
        VBox box = new VBox();
        Scene s = new Scene(box, 400, 150);
        Tools.addStylesheet(s, "style_default.css");
        box.setId("vbox");

        Label nameLabel = new Label("Name:");
        Label nameVal = new Label(w.getName());
        HBox name = new HBox();
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        name.getChildren().addAll(nameLabel, nameVal);
        name.setPadding(new Insets(0, 0, 15.0, 0));

        Label wClassLabel = new Label("Klasse:");
        Label wClassVal = new Label(w.getWeaponClass().toString());
        wClassVal.setOnMousePressed(e -> {
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

    /**
     * Öffnet ein Popup für ein Rüstungsteil zur Inspektion.
     * <br>
     * Zeigt Name, Ausrüstungsslot, Klasse und Rüstungswert an.
     *
     * @param a Das Rüstungsteil, das inspiziert werden soll.
     */
    ItemStats(Armor a) {
        Stage stage = new Stage();
        VBox box = new VBox();
        Scene s = new Scene(box, 400, 150);
        Tools.addStylesheet(s, "style_default.css");
        box.setId("vbox");

        Label nameLabel = new Label("Rüstung: ");
        Label nameVal = new Label(a.getName());
        HBox name = new HBox();
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        name.getChildren().addAll(nameLabel, nameVal);
        name.setPadding(new Insets(0, 0, 15.0, 0));

        Label regionLabel = new Label("Körperregion:");
        String bodyRegion = switch (a.getSlotType()) {
            case 0 -> "Kopf";
            case 1 -> "Torso";
            case 2 -> "Arme";
            case 3 -> "Beine";
            default -> throw new IllegalStateException("Unexpected value: " + a.getSlotType());
        };
        Label regionVal = new Label(bodyRegion);
        HBox region = new HBox();
        HBox.setHgrow(regionLabel, Priority.ALWAYS);
        regionLabel.setMaxWidth(Double.MAX_VALUE);
        region.getChildren().addAll(regionLabel, regionVal);
        region.setPadding(new Insets(0, 0, 15.0, 0));

        Label aClassLabel = new Label("Klasse:");
        Label aClassVal = new Label(a.getArmorClass().name);
        aClassVal.setOnMousePressed(e -> {
        });
        HBox aClass = new HBox();
        HBox.setHgrow(aClassLabel, Priority.ALWAYS);
        aClassLabel.setMaxWidth(Double.MAX_VALUE);
        aClass.getChildren().addAll(aClassLabel, aClassVal);
        aClass.setPadding(new Insets(0, 0, 15.0, 0));

        Label protectionLabel = new Label("Rüstungswert:");
        Label protectionVal = new Label(String.valueOf(a.getTotalProtection()));
        HBox protection = new HBox();
        HBox.setHgrow(protectionLabel, Priority.ALWAYS);
        protectionLabel.setMaxWidth(Double.MAX_VALUE);
        protection.getChildren().addAll(protectionLabel, protectionVal);
        protection.setPadding(new Insets(0.0, 0, 15.0, 0));

        box.getChildren().addAll(name, region, aClass, protection);

        Tools.addStylesheet(s, "style_itemstats.css");
        stage.setScene(s);
        stage.show();
    }
}
