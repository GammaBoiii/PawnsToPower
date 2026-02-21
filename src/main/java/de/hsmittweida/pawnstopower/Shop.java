package de.hsmittweida.pawnstopower;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

/**
 * Der Shop erstellt jeden Tag ein Angebot mit 5 zufälligen Rüstungsteilen und Waffen.
 * Diese können jeweils nur einmalig gekauft werden.
 */
public class Shop {
    /**
     * Angebote für Waffen
     */
    private static ArrayList<Weapon> weapon_offer;
    /**
     * Angebote für Rüstungen
     */
    private static ArrayList<Armor> armor_offer;
    /**
     * Zu zahlender Betrag
     */
    private static IntegerProperty due_amount;
    /**
     * Warenkorb
     */
    private static ArrayList<Item> shopping_cart;

    /**
     * Das Pane, welches das Shopfenster darstellt.
     * Gibt das Pane zurück, welches dann in {@link Game#drawSpace()} angezeigt wird.
     * @return {@code Pane}
     */
    public static Pane Shop_view() {
        initVars();

        // refreshShop(); //DEBUG

        AnchorPane anchorPane = new AnchorPane();

        Tools.addStylesheet(anchorPane, "style_shop.css");
        anchorPane.setId("ap");
        BorderPane pane = new BorderPane();
        pane.setId("pane");
        AnchorPane.setTopAnchor(pane, 50.0);
        AnchorPane.setBottomAnchor(pane, 150.0);
        AnchorPane.setLeftAnchor(pane, 55.0);
        AnchorPane.setRightAnchor(pane, 55.0);
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.setPadding(new Insets(90,0.0,0.0,0.0));

        Button mainMenu = new Button("Hauptmenu");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });

        anchorPane.getChildren().addAll(mainMenu, pane);
        AnchorPane.setTopAnchor(mainMenu, 2.0);
        AnchorPane.setLeftAnchor(mainMenu, 2.0);
        AnchorPane.setTopAnchor(pane, 30.0);

        ScrollPane weapons = new ScrollPane();
        ScrollPane armor = new ScrollPane();

        /* Wird verwendet, um bereits gekaufte Gegenstände später im Shop
        * zu sperren (nur einmaliger Kauf möglich).
        * */
        ArrayList<CheckBox> purchased = new ArrayList<CheckBox>();

        weapons.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.NEVER);
        armor.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.NEVER);

        VBox weapon_list = new VBox();
        VBox armor_list = new VBox();
        weapon_list.setId("list");
        armor_list.setId("list");
        weapons.setContent(weapon_list);
        armor.setContent(armor_list);
        weapons.setFitToHeight(true);
        weapons.setFitToWidth(true);
        armor.setFitToHeight(true);
        armor.setFitToWidth(true);

        for (Weapon w : weapon_offer) {
            HBox hbox = new HBox();
            CheckBox cb = new CheckBox();

            /* Angekreuzte Items sind im Warenkorb */
            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    purchased.add(cb);
                    due_amount.set(due_amount.get() + w.getWeaponClass().basePrice);
                    shopping_cart.add(w);
                } else {
                    purchased.remove(cb);
                    due_amount.set(due_amount.get() - w.getWeaponClass().basePrice);
                    shopping_cart.remove(w);
                }
            });

            Label name = new Label(w.getName() + " (" + w.getWeaponClass().toString() + ")");
            Label price = new Label(w.getWeaponClass().basePrice + "$");

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);
            price.setPadding(new Insets(0, 15, 0, 0));
            cb.setPadding(new Insets(9, 15, 11, 15));
            hbox.setPadding(new Insets(15,0,0,0));
            cb.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().addAll(cb, name, price);
            weapon_list.getChildren().add(hbox);
        }
        for (Armor a : armor_offer) {
            HBox hbox = new HBox();
            CheckBox cb = new CheckBox();

            /* angekreuzte Items sind im Warenkorb */
            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    purchased.add(cb);
                    due_amount.set(due_amount.get() + a.getBasePrice());
                    shopping_cart.add(a);
                } else {
                    purchased.remove(cb);
                    due_amount.set(due_amount.get() - a.getBasePrice());
                    shopping_cart.remove(a);
                }
            });

            Label name = new Label(a.getName());
            Label price = new Label(a.getBasePrice() + "$");

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);
            price.setPadding(new Insets(0, 15, 0, 0));
            cb.setPadding(new Insets(9, 15, 11, 15));
            hbox.setPadding(new Insets(15,0,0,0));
            cb.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().addAll(cb, name,  price);
            armor_list.getChildren().add(hbox);
        }

        HBox center = new HBox();
        center.setMaxWidth(Double.MAX_VALUE);
        center.setMaxHeight(Double.MAX_VALUE);
        center.getChildren().addAll(weapons, armor);
        pane.setCenter(center);
        HBox.setHgrow(weapons, Priority.ALWAYS);
        HBox.setHgrow(armor, Priority.ALWAYS);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(15, 7, 15, 7));
        bottom.setSpacing(10);
        Button purchase = new Button("Kauf bestätigen");
        purchase.setOnAction(e -> {
            if (Inventory.getMoney() >= due_amount.get()) {
                for (Item x : shopping_cart) {
                    Inventory.addItem(x);
                }
                for(CheckBox cb : purchased) {
                    cb.setDisable(true);
                    cb.lookup(".box").setStyle("-fx-background-color: darkred");
                    cb.setSelected(false);
                }
                Inventory.addMoney(-1 * due_amount.get());

                /* Rücksetzen der Einkaufsvaraiblen */
                shopping_cart.clear();
                due_amount.set(0);
            } else {
                /* Nicht genug Geld */
                Tools.popup("Fehler.", "Nicht genug Geld!", ":(");
            }
        });

        Label price = new Label();
        price.setMaxHeight(Double.MAX_VALUE);
        price.textProperty().bind(Bindings.concat(due_amount.asString(), "$"));
        bottom.getChildren().addAll(purchase, price);
        pane.setBottom(bottom);

        weapons.setMaxWidth(Double.MAX_VALUE);
        weapons.setMaxHeight(Double.MAX_VALUE);
        armor.setMaxHeight(Double.MAX_VALUE);
        armor.setMaxWidth(Double.MAX_VALUE);
        return anchorPane;
    }

    /**
     * Erneuert das Angebot im Shop.
     */
    public static void refreshShop() {
        initVars();

        weapon_offer.clear();
        armor_offer.clear();
        for(int i = 0; i<12; i++) {
            weapon_offer.add(new Weapon());
            armor_offer.add(new Armor());
        }
    }

    /**
     * Prüft, ob ein Item bereits gekauft wurde.
     * @param list liste, aus der geprüft werden soll.
     * @param cb Checkbox, an der das zu prüfende Item "hängt".
     * @return {@code true}, wenn bereits gekauft, sonst {@code false}.
     * @deprecated
     */
    private static boolean isPurchased(ArrayList<CheckBox> list, CheckBox cb) {
        System.out.println("Checking " + cb + " against " + list);
        for(CheckBox x : list) {
            System.out.println(x + " | " + cb);
            if(x.equals(cb)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initialisiert die Shopvariablen.
     */
    private static void initVars() {
        if (due_amount == null) due_amount = new SimpleIntegerProperty(0);
        if (weapon_offer == null) weapon_offer = new ArrayList<Weapon>();
        if (armor_offer == null) armor_offer = new ArrayList<Armor>();
        if (shopping_cart == null) shopping_cart = new ArrayList<Item>();
    }
}
