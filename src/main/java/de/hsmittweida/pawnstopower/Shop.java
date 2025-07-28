package de.hsmittweida.pawnstopower;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Der Shop erstellt jeden Tag ein Angebot mit 5 zufälligen Rüstungsteilen und Waffen
 */
public class Shop {
    private static ArrayList<Weapon> weapon_offer;
    private static ArrayList<Armor> armor_offer;

    private static IntegerProperty ptp; //price to pay
    private static ArrayList<Item> shopping_cart;

   /* Shop() {
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
            HBox hbox = new HBox();
            CheckBox cb = new CheckBox();

            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    ptp.set(ptp.get() + w.getWeaponClass().basePrice);
                    stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() - ptp.get()) + "$");
                    shopping_cart.add(w);
                } else {
                    ptp.set(ptp.get() - w.getWeaponClass().basePrice);
                    stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() + ptp.get()) + "$");
                    shopping_cart.remove(w);
                }
            });

            Label name = new Label(w.getName() + " (" + w.getWeaponClass().toString() + ")");
            Label price = new Label(w.getWeaponClass().basePrice + "$");
            //Label wclass = new Label(w.getWClass().toString());

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);
            price.setPadding(new Insets(0, 15, 0, 0));
            cb.setPadding(new Insets(0, 10, 0, 0));
            hbox.getChildren().addAll(cb, name, price);
            weapon_list.getChildren().add(hbox);
        }
        for (Armor a : armor_offer) {
            HBox hbox = new HBox();
            CheckBox cb = new CheckBox();

            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    ptp.set(ptp.get() + a.getBasePrice());
                    stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() - ptp.get()) + "$");
                    shopping_cart.add(a);
                } else {
                    ptp.set(ptp.get() - a.getBasePrice());
                    stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() + ptp.get()) + "$");
                    shopping_cart.remove(a);
                }
            });

            Label name = new Label(a.getName());
            Label price = new Label(a.getBasePrice() + "$");

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);
            price.setPadding(new Insets(0,15,0,0));
            cb.setPadding(new Insets(0,10,0,0));
            hbox.getChildren().addAll(cb, name,  price);
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
            if (Inventory.getMoney() >= ptp.get()) {
                for (Item x : shopping_cart) {
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
    */

    public static Pane Shop_view() {
        ptp = new SimpleIntegerProperty(0);
        weapon_offer = new ArrayList<Weapon>();
        armor_offer = new ArrayList<Armor>();
        shopping_cart = new ArrayList<Item>();

        refreshShop(); //DEBUG

        AnchorPane anchorPane = new AnchorPane();

        Tools.addStylesheet(anchorPane, "style_shop.css");
       // anchorPane.setStyle("-fx-border-color: blue; -fx-border-style: solid;");
        anchorPane.setId("ap");
        //Tools.addStylesheet(anchorPane, "style_shop.css");
        BorderPane pane = new BorderPane();
        pane.setId("pane");
        AnchorPane.setTopAnchor(pane, 50.0);
        AnchorPane.setBottomAnchor(pane, 150.0);
        AnchorPane.setLeftAnchor(pane, 55.0);
        AnchorPane.setRightAnchor(pane, 55.0);
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.setPadding(new Insets(90,0.0,0.0,0.0));
        //Scene s = new Scene(pane, Tools.getScreenSize().get('w') * 0.5, Tools.getScreenSize().get('h') * 0.5);

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

        /* Wird verwendet, um bereits gekaufte Gegenstände später im Shop zu sperren (nur einmaliger Kauf möglich). */
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

            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    purchased.add(cb);
                    ptp.set(ptp.get() + w.getWeaponClass().basePrice);
                    //stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() - ptp.get()) + "$");
                    shopping_cart.add(w);
                } else {
                    purchased.remove(cb);
                    ptp.set(ptp.get() - w.getWeaponClass().basePrice);
                    //stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() + ptp.get()) + "$");
                    shopping_cart.remove(w);
                }
            });

            Label name = new Label(w.getName() + " (" + w.getWeaponClass().toString() + ")");
            Label price = new Label(w.getWeaponClass().basePrice + "$");
            //Label wclass = new Label(w.getWClass().toString());

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

            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    purchased.add(cb);
                    ptp.set(ptp.get() + a.getBasePrice());
                    //stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() - ptp.get()) + "$");
                    shopping_cart.add(a);
                } else {
                    purchased.remove(cb);
                    ptp.set(ptp.get() - a.getBasePrice());
                    //stage.setTitle("Kontostand nach Kauf: " + (Inventory.getMoney() + ptp.get()) + "$");
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
            if (Inventory.getMoney() >= ptp.get()) {
                for (Item x : shopping_cart) {
                    Inventory.addItem(x);

                    //Game.drawSpace();

                }
                for(CheckBox cb : purchased) {
                    cb.setDisable(true);
                    cb.lookup(".box").setStyle("-fx-background-color: darkred");
                    cb.setSelected(false);
                }
                Inventory.addMoney(-1 * ptp.get());

                /* Rücksetzen der Einkaufsvaraiblen */
                shopping_cart.clear();
                ptp.set(0);
            } else {
                /* Nicht genug Geld */
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
        return anchorPane;
    }

    public static void refreshShop() {
        for(int i = 0; i<12; i++) {
            weapon_offer.add(new Weapon());
            armor_offer.add(new Armor());
        }
    }

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
}
