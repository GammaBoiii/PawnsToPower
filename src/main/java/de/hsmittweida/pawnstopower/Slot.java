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

/**
 * Diese Klasse wird aufgerufen, wenn der Spieler ein neues Item ausrüsten möchte
 */
public class Slot {
    /**
     * Diese Klasse erstellt ein neues Fenster zum Ausrüsten der Items.
     *
     * @param i         - Inspector-Klasse, die mit zur Referenz übergeben wird.
     * @param reference - Knopf, der mit zur Referenz übergeben wird. Hierbei handelt es sich um das "Körperteil" das der Spieler anklickt, um an der Stelle etwas auszurüsten
     *                  Siehe dazu auch die Slotverteilung in Pawn.clothingSlotUsed bzw. Pawn.weaponSlotUsed.
     * @param p         - Der Pawn, der aktuell ausgerüstet wird.
     * @param type      - Waffe oder Kleidungsstück (Rüstung).
     * @param id        - Die ID des Slots, an dem die Ausrüstung ausgerüstet werden soll. Unterschieden wird bei den Slots gemäß {@code @param type}.
     */
    Slot(Inspector i, Button reference, Pawn p, String type, int id) {
        /* Das Fenster wird ganz normal aufgesetzt und bestückt. */
        Stage stage = new Stage();
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        VBox box = new VBox();
        Scene s = new Scene(sp, 350, 450);
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
                    Button equip = new Button("Equip");

                    /* Falls das Item bereits ausgerüstet ist, wird ein Hinweis ausgegeben, dass der Pawn, der das Item ausgerüstet hat, nicht mehr besitzen wird.
                     * Der Hinweis erfolgt nur, wenn es sich bei dem aktuellen Pawn nicht um den handelt, der das Item ausgerüstet hat! */
                    if (w.isEquipped()) {
                        if (p != w.getOwner()) {
                            equip.setOnAction(e -> {
                                String res = Tools.confirmPopup("Item bereits ausgerüstet.", "Dieses Item ist bereits bei" + w.getOwner().getName() + " ausgerüstet.", "Möchtest du es bei dem aktuellen Pawn ausrüsten?");
                                if (res.equals("yes")) {


                                    for (Weapon wep : w.getOwner().weapons) {
                                        if (wep != null) System.out.println(wep.getName());
                                    }

                                    for (Weapon wep : p.weapons) {
                                        if (wep != null) System.out.println(wep.getName());
                                    }

                                    /* Dem alten Pawn entrüsten */
                                    w.getOwner().removeWeapon(w);
                                    p.giveWeapon(i, reference, w, (byte) id);
                                    stage.close();
                                } else if (res.equals("no")) {

                                }
                            });

                        } else {
                            equip.setText("Unequip");
                            equip.setOnAction(e -> {

                            });

                        }
                    } else {
                        equip.setOnAction(e -> {
                            p.giveWeapon(i, reference, w, (byte) id);
                            stage.close();
                        });
                    }

                    details.setOnAction(e -> {
                        new ItemStats(w);
                    });

                    /* Etwas Gestaltung für die FX-Elemente. Weitere Design-Anpassung sind jeweils in der entsprechenden .css zu finden. */
                    box.setPadding(new Insets(0, 0, 15, 0));
                    HBox.setHgrow(name, Priority.ALWAYS);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    HBox.setHgrow(equip, Priority.ALWAYS);
                    name.setMaxWidth(Double.MAX_VALUE);
                    hbox.getChildren().addAll(name, details, equip);
                    box.getChildren().add(hbox);
                }
                break;
            case "clothing":

                if (Inventory.getArmor().isEmpty()) {

                    Tools.popup("Inventar", "Es wurden keine Kleidungsstücke im Inventar gefunden.", "Besuche den Shop, um Items zu kaufen!");
                    return;
                }

                for (Armor a : Inventory.getArmor()) {

                    HBox hbox = new HBox();
                    Label name = new Label(a.getName());
                    Button details = new Button("Details");
                    Button equip = new Button("Equip");

                    /* Falls das Item bereits ausgerüstet ist, wird ein Hinweis ausgegeben, dass der Pawn, der das Item ausgerüstet hat, nicht mehr besitzen wird.
                     * Der Hinweis erfolgt nur, wenn es sich bei dem aktuellen Pawn nicht um den handelt, der das Item ausgerüstet hat! */
                    if (a.isEquipped()) {
                        if (p != a.getOwner()) {
                            equip.setOnAction(e -> {
                                String res = Tools.confirmPopup("Item bereits ausgerüstet.", "Dieses Item ist bereits bei" + a.getOwner().getName() + " ausgerüstet.", "Möchtest du es bei dem aktuellen Pawn ausrüsten?");
                                if (res.equals("yes")) {


                                    for (Armor arm : a.getOwner().armors) {
                                        if (arm != null) System.out.println(arm.getName());
                                    }

                                    for (Armor arm : p.armors) {
                                        if (arm != null) System.out.println(arm.getName());
                                    }

                                    /* Dem alten Pawn entrüsten */
                                    a.getOwner().removeArmor(a);
                                    p.giveArmor(i, reference, a, (byte) id);
                                    stage.close();
                                } else if (res.equals("no")) {

                                }
                            });

                        } else {
                            equip.setText("Unequip");
                            equip.setOnAction(e -> {

                            });

                        }
                    } else {
                        equip.setOnAction(e -> {
                            p.giveArmor(i, reference, a, (byte) id);

                            stage.close();
                        });
                    }

                    details.setOnAction(e -> {
                        new ItemStats(a);
                    });

                    /* Etwas Gestaltung für die FX-Elemente. Weitere Design-Anpassung sind jeweils in der entsprechenden .css zu finden. */
                    box.setPadding(new Insets(0, 0, 15, 0));
                    HBox.setHgrow(name, Priority.ALWAYS);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    HBox.setHgrow(equip, Priority.ALWAYS);
                    name.setMaxWidth(Double.MAX_VALUE);
                    hbox.getChildren().addAll(name, details, equip);
                    box.getChildren().add(hbox);
                }

                break;
            default:
                break;
        }
        Tools.addStylesheet(s, "style_slot.css");
        Tools.defaultClose(stage, "slot");
        stage.setScene(s);
        stage.show();
    }
}
