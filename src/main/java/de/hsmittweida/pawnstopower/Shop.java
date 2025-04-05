package de.hsmittweida.pawnstopower;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Der Shop erstellt jeden Tag ein Angebot mit 5 zufälligen Rüstungsteilen und Waffen
 */
public class Shop {
    private static ArrayList<Weapon> weapon_offer;
    private static ArrayList<Armor> armor_offer;

    private final IntegerProperty ptp; //price to pay
    private ArrayList<Item>  shopping_cart;

    Shop() {
        ptp = new SimpleIntegerProperty(0);
        weapon_offer = new ArrayList<Weapon>();
        armor_offer = new ArrayList<Armor>();
        shopping_cart = new ArrayList<Item>();

        refreshShop(); //DEBUG

        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        Scene s = new Scene(pane, Tools.getScreenSize().get('w') * 0.5, Tools.getScreenSize().get('h') * 0.5);

        ScrollPane weapons = new ScrollPane();
        ScrollPane armor = new ScrollPane();
        VBox weapon_list = new VBox();
        VBox armor_list = new VBox();
        weapons.setContent(weapon_list);
        armor.setContent(armor_list);
        weapons.setFitToHeight(true);
        weapons.setFitToWidth(true);
        armor.setFitToHeight(true);
        armor.setFitToWidth(true);

        for (Weapon w : weapon_offer) {
            System.out.println("aaa");
            HBox hbox = new HBox();
            CheckBox cb = new CheckBox();

            cb.setOnAction(e -> {
                if(cb.isSelected()) {
                    ptp.set(ptp.get() + w.getWClass().basePrice);
                    stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() - ptp.get()) + "$");
                    shopping_cart.add(w);
                } else {
                    ptp.set(ptp.get() - w.getWClass().basePrice);
                    stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() + ptp.get()) + "$");
                    shopping_cart.remove(w);
                }
            });

            Label name = new Label(w.getName() + " (" + w.getWClass().toString() + ")");
            Label price = new Label(w.getWClass().basePrice + "$");
            //Label wclass = new Label(w.getWClass().toString());

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);
            price.setPadding(new Insets(0,15,0,0));
            cb.setPadding(new Insets(0,10,0,0));
            hbox.getChildren().addAll(cb, name, price);
            weapon_list.getChildren().add(hbox);
        }
        for (Armor a : armor_offer) {
            HBox hbox = new HBox();
            CheckBox cb = new CheckBox();
            Label name = new Label(a.getName());
            Label aclass = new Label(a.getArmorClass().toString());

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);
            hbox.getChildren().addAll(cb, name, aclass);
            armor_list.getChildren().add(hbox);
        }

        HBox center = new HBox();
        center.getChildren().addAll(weapons, armor);
        pane.setCenter(center);
        HBox.setHgrow(weapons, Priority.ALWAYS);
        HBox.setHgrow(armor, Priority.ALWAYS);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(15, 7, 15, 7));
        bottom.setSpacing(10);
        Button purchase = new Button("Kauf bestätigen");
        purchase.setOnAction(e -> {
            if(Inventory.getMoney() >= ptp.get()) {
                for(Item x : shopping_cart) {
                    Inventory.addItem(x);
                    stage.close();
                }
                Inventory.addMoney(-1 * ptp.get());
            }
        });

        Label price = new Label();
        price.setMaxHeight(Double.MAX_VALUE);
        price.textProperty().bind(Bindings.concat(ptp.asString(), "$"));
        bottom.getChildren().addAll(purchase, price);
        pane.setBottom(bottom);

        weapons.setMaxWidth(Double.MAX_VALUE);
        weapons.setMaxHeight(Double.MAX_VALUE);
        armor.setMaxHeight(Double.MAX_VALUE);
        armor.setMaxWidth(Double.MAX_VALUE);

        Tools.defaultClose(stage, "shop");
        stage.setScene(s);
        stage.setTitle("Kontostand nach Kauf: " + Inventory.getMoney() + "$");
        stage.show();
    }

    public static void refreshShop() {
        weapon_offer.add(new Weapon(null));
        weapon_offer.add(new Weapon(null));
        weapon_offer.add(new Weapon(null));
        weapon_offer.add(new Weapon(null));
        weapon_offer.add(new Weapon(null));


        armor_offer.add(new Armor(Armor.ArmorClass.IRN, "skibi"));
        armor_offer.add(new Armor(Armor.ArmorClass.FAB, "huchu"));
        armor_offer.add(new Armor(Armor.ArmorClass.STL, "ioesen"));
        armor_offer.add(new Armor(Armor.ArmorClass.LTH, "maugazu"));
        armor_offer.add(new Armor(Armor.ArmorClass.STL, "nazgul"));

    }
}
