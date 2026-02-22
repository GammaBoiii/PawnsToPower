package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Diese Klasse wird aufgerufen, wenn der Spieler ein neues Item ausrüsten möchte
 */
public class Slot {
    /**
     * Dieser Konstruktor erstellt ein neues Fenster zum Ausrüsten der Items.
     *
     * @param reference Knopf, der mit zur Referenz übergeben wird. Hierbei handelt es sich um das "Körperteil" das der Spieler anklickt, um an der Stelle etwas auszurüsten
     *                  Siehe dazu auch die Slotverteilung in Pawn.clothingSlotUsed bzw. Pawn.weaponSlotUsed.
     * @param p         Der Pawn, der aktuell ausgerüstet wird.
     * @param type      Waffe oder Kleidungsstück (Rüstung).
     * @param id        Die ID des Slots, an dem die Ausrüstung ausgerüstet werden soll. Unterschieden wird bei den Slots gemäß {@code @param type}.
     */
    Slot(StackPane reference, Pawn p, String type, int id, String title) {
        Stage stage = new Stage();
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        VBox box = new VBox();
        Scene s = new Scene(sp, 450, 450);
        sp.setContent(box);
        sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        /* Entsprechend der Parameter des Konstruktors wird hier zwischen Rüstung und Waffen unterschieden.
         * Falls es keine Gegenstände zum Ausrüsten gibt, wird mithilfe der eigens erstellten Tool Klasse ein Popup-Hinweis erstellt, das nichts verfügbar ist. */
        switch (type) {
            case "weapon":
                if (Inventory.getWeapons().isEmpty()) {
                    Tools.popup("Inventar", "Es wurden keine Waffen im Inventar gefunden.", "Besuche den Shop, um Items zu kaufen!");
                    return;
                }

                /* Entsprechend für jede Waffe wird ein Eintrag in der Liste erstellt, die dem Spieler dann als Auswahl vorliegt, was er ausrüsten möchte.
                 * Des Weiteren existiert ein Knopf, der ein paar Informationen zu einem Item anzeigt. */
                for (Weapon w : Inventory.getWeapons()) {
                    HBox hbox = new HBox();
                    Label name = new Label(w.getName());
                    Button details = new Button("Details");
                    Button sell = new Button("Verkauf");
                    Button equip = new Button("Equip");

                    /* Falls das Item bereits ausgerüstet ist, wird ein Hinweis ausgegeben, dass der Pawn, der das Item ausgerüstet hat, nicht mehr besitzen wird.
                     * Der Hinweis erfolgt nur, wenn es sich bei dem aktuellen Pawn nicht um den handelt, der das Item ausgerüstet hat! */
                    if (w.isEquipped()) {
                        /* Pawn ist nicht Besitzer */
                        if (p != w.getOwner()) {
                            System.out.println("!!!" + w.getOwner().getName());
                            System.out.println(w.getOwner() + " .. " + p);
                            equip.setOnAction(e -> {
                                System.out.println(w.getOwner());
                                System.out.println(p);

                                String res = Tools.confirmPopup("Item bereits ausgerüstet.", "Dieses Item ist bereits bei " + w.getOwner().getName() + " ausgerüstet.", "Möchtest du es ablegen?");
                                if (res.equals("yes")) {
                                    for (Weapon wep : w.getOwner().getWeapons()) {
                                        if (wep != null) System.out.println(wep.getName());
                                    }

                                    for (Weapon wep : p.getWeapons()) {
                                        if (wep != null) System.out.println(wep.getName());
                                    }

                                    /* Dem alten Pawn entrüsten */
                                    w.getOwner().removeWeapon(w);
                                    stage.close();
                                } else if (res.equals("no")) {
                                    // Do nothing
                                }
                            });
                        /* Pawn ist Besitzer */
                        } else {
                            equip.setText("Unequip");
                            equip.setOnAction(e -> {
                                w.getOwner().removeWeapon(w);
                                Inspector.refreshImages();
                                stage.close();
                            });
                        }
                    /* Waffe noch nicht ausgerüstet */
                    } else {
                        equip.setOnAction(e -> {
                            /* Prüfen, ob bereits ein Zweihänder ausgerüstet ist. */
                            if (id == 0) {
                                if ((w.isTwoHanded() && p.getWeapon((byte) 1) != null)) {
                                    Tools.popup("Zweihänder", "Du willst ein Zweihänder ausrüsten.", "Lege dazu erst die Waffe aus der anderen Hand ab.");
                                    return;
                                } else if (p.getWeapon((byte) 1) != null && p.getWeapon((byte) 1).isTwoHanded()) {
                                    Tools.popup("Zweihänder", "Ein Zweihänder ist bereits ausgerüstet.", "Lege diesen zunächst ab, wenn du eine neue Waffe ausrüsten willst.");
                                    return;
                                }
                            } else if (id == 1) {
                                if ((w.isTwoHanded() && p.getWeapon((byte) 0) != null)) {
                                    Tools.popup("Zweihänder", "Du willst ein Zweihänder ausrüsten.", "Lege dazu erst die Waffe aus der anderen Hand ab.");
                                    return;
                                } else if (p.getWeapon((byte) 0) != null && p.getWeapon((byte) 0).isTwoHanded()) {
                                    Tools.popup("Zweihänder", "Ein Zweihänder ist bereits ausgerüstet.", "Lege diesen zunächst ab, wenn du eine neue Waffe ausrüsten willst.");
                                    return;
                                }
                            }

                            p.giveWeapon(reference, w, (byte) id);
                            stage.close();
                        });
                    }

                    sell.setOnAction(e -> {
                        if (w.getOwner() != null) {
                            String res = Tools.confirmPopup("Item bereits ausgerüstet.", "Dieses Item ist bei " + w.getOwner().getName() + " ausgerüstet.", "Möchtest du es verkaufen?");
                            if (res.equals("yes")) {
                                w.getOwner().removeWeapon(w);
                                Inventory.addMoney((int) (w.getWeaponClass().basePrice * 0.65));
                                Inventory.removeItem(w);
                                stage.close();
                            }
                        } else {
                            String res = Tools.confirmPopup("Verkauf", "Dieses Item ist zum Verkauf ausgewählt.", "Möchtest du es verkaufen?");
                            if (res.equals("yes")) {
                                Inventory.addMoney((int) (w.getWeaponClass().basePrice * 0.65));
                                Inventory.removeItem(w);
                                stage.close();
                            }
                        }
                    });

                    details.setOnAction(e -> {
                        new ItemStats(w);
                    });

                    /* Etwas Gestaltung für die FX-Elemente. Weitere Design-Anpassung sind jeweils in der entsprechenden .css zu finden. */
                    box.setPadding(new Insets(0, 0, 15, 0));
                    HBox.setHgrow(name, Priority.ALWAYS);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    HBox.setHgrow(sell, Priority.ALWAYS);
                    HBox.setHgrow(equip, Priority.ALWAYS);
                    name.setMaxWidth(Double.MAX_VALUE);
                    hbox.getChildren().addAll(name, details, sell, equip);
                    box.getChildren().add(hbox);
                }
                break;
            case "clothing":

                if (Inventory.getArmor().isEmpty()) {
                    Tools.popup("Inventar", "Es wurden keine Kleidungsstücke im Inventar gefunden.", "Besuche den Shop, um Items zu kaufen!");
                    return;
                }

                for (Armor a : Inventory.getArmor()) {
                    /* Kurzer Check, dass auch nur Kleidungsstücke angezeigt werden, die auch an dem bestimmten Slot ausgerüstet werden können. */
                    if (!(a.getSlotType() == id)) {
                        continue;
                    }

                    HBox hbox = new HBox();
                    Label name = new Label(a.getName());
                    Button details = new Button("Details");
                    Button sell = new Button("Verkauf");
                    Button equip = new Button("Equip");

                    /* Falls das Item bereits ausgerüstet ist, wird ein Hinweis ausgegeben, dass der Pawn, der das Item ausgerüstet hat, nicht mehr besitzen wird.
                     * Der Hinweis erfolgt nur, wenn es sich bei dem aktuellen Pawn nicht um den handelt, der das Item ausgerüstet hat! */
                    if (a.isEquipped()) {
                        /* Pawn ist nicht Besitzer */
                        if (p != a.getOwner()) {
                            equip.setOnAction(e -> {
                                String res = Tools.confirmPopup("Item bereits ausgerüstet.", "Dieses Item ist bereits bei " + a.getOwner().getName() + " ausgerüstet.", "Möchtest du es bei dem aktuellen Pawn ausrüsten?");
                                if (res.equals("yes")) {
                                    for (Armor arm : a.getOwner().getArmors()) {
                                        if (arm != null) System.out.println(arm.getName());
                                    }

                                    for (Armor arm : p.getArmors()) {
                                        if (arm != null) System.out.println(arm.getName());
                                    }

                                    /* Dem alten Pawn entrüsten */
                                    a.getOwner().removeArmor(a);
                                    p.giveArmor(reference, a, (byte) id);
                                    stage.close();
                                } else if (res.equals("no")) {
                                    // Do nothing
                                }
                            });
                        /* Pawn ist Besitzer */
                        } else {
                            equip.setText("Unequip");
                            equip.setOnAction(e -> {
                                a.getOwner().removeArmor(a);
                                Inspector.refreshImages();
                                stage.close();
                            });
                        }
                    /* Armor noch nicht ausgerüstet */
                    } else {
                        equip.setOnAction(e -> {
                            if (p.getArmor((byte) id) != null) {
                                p.removeArmor(p.getArmor((byte) id));
                            }

                            p.giveArmor(reference, a, (byte) id);

                            Inspector.refreshImages();
                            stage.close();
                        });
                    }

                    sell.setOnAction(e -> {
                        if (a.getOwner() != null) {
                            String res = Tools.confirmPopup("Item bereits ausgerüstet.", "Dieses Item ist bei " + a.getOwner().getName() + " ausgerüstet.", "Möchtest du es verkaufen?");
                            if (res.equals("yes")) {
                                a.getOwner().removeArmor(a);
                                Inventory.addMoney((int) (a.getBasePrice() * 0.65));
                                Inventory.removeItem(a);
                                stage.close();
                            }
                        } else {
                            String res = Tools.confirmPopup("Verkauf", "Dieses Item ist zum Verkauf ausgewählt.", "Möchtest du es verkaufen?");
                            if (res.equals("yes")) {
                                Inventory.addMoney((int) (a.getBasePrice() * 0.65));
                                Inventory.removeItem(a);
                                stage.close();
                            }
                        }
                    });

                    details.setOnAction(e -> {
                        new ItemStats(a);
                    });

                    /* Etwas Gestaltung für die FX-Elemente. Weitere Design-Anpassung sind jeweils in der entsprechenden .css zu finden. */
                    box.setPadding(new Insets(0, 0, 15, 0));
                    HBox.setHgrow(name, Priority.ALWAYS);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    HBox.setHgrow(sell, Priority.ALWAYS);
                    HBox.setHgrow(equip, Priority.ALWAYS);
                    name.setMaxWidth(Double.MAX_VALUE);
                    hbox.getChildren().addAll(name, details, sell, equip);
                    box.getChildren().add(hbox);
                }

                if (box.getChildren().size() == 0) {
                    Tools.popup("Inventar", "Es wurden keine Kleidungsstücke im Inventar gefunden.", "Besuche den Shop, um Items zu kaufen!");
                    return;
                }

                break;
            default:
                break;
        }
        Tools.addStylesheet(s, "style_default.css");
        Tools.defaultClose(stage, "slot");
        stage.setScene(s);
        stage.setTitle(title);
        stage.show();
    }
}
