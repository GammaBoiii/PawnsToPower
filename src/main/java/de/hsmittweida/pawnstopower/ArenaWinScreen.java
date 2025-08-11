package de.hsmittweida.pawnstopower;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Die ArenaWinScreen Klasse beinhaltet die Oberfläche, die angezeigt wird,
 * wenn der Kampf in der Arena vorrüber ist.
 */
public class ArenaWinScreen {
    /**
     * @param playerWins true, wenn der Spieler gewonnen hat. Sonst false.
     * @return Pane, welches dann in {@code Game.drawSpace()} angezeigt wird.
     */
    public static Pane winscreen(boolean playerWins) {
        BorderPane pane = new BorderPane();
        Tools.addStylesheet(pane, "style_winscreen.css");
        pane.setId("pane");
        Label header = new Label("Die Spiele sind vorbei!");
        pane.setTop(header);

        VBox stats = new VBox();
        stats.setId("stats");
        Label msg;
        if (playerWins) {
            String s = String.format("""
                            Du besiegst den Gegner %s
                            und erhältst deine Belohnung von %d Goldmünzen.
                            Dein Kämpfer %s erhält außerdem %d Erfahrungspunkte.
                            """,
                    Arena.getCombatans()[1].getName(),
                    Arena.getPrice()[0],
                    Arena.getCombatans()[0].getName(),
                    Arena.getPrice()[1]);

            /* Wird der Spieler den besiegten Pawn gewinnen? Mit 10% Chance */
            if(Math.random() < 1.0) { //Debug 100%
                Pawn p = Arena.getCombatans()[1];
                for(Armor a : p.armors) {
                    p.removeArmor(a);
                }
                for(Weapon w : p.weapons) {
                    p.removeWeapon(w);
                }
                Inventory.addPawn(p);
                String congrats = String.format("""
                        Der Pawn %s schließt sich außerdem deinem Trupp an!
                        Damit erhälst du einen neuen Level %d Kämpfer.
                        """, p.getName(), p.getLvl());
                s+=congrats;
            }
            msg = new Label(s);
            Inventory.addMoney(Arena.getPrice()[0]);
            Arena.getCombatans()[0].addXp(Arena.getPrice()[1]);
        } else {
            String s = String.format("""
                            Du verlierst gegen %s
                            und damit -%d Goldmünzen.
                            Dein Kämpfer erhält %d Erfahrungspunkte.
                            """,
                    Arena.getCombatans()[1].getName(), Math.round(Arena.getPrice()[0] * 0.25), Math.round(Arena.getPrice()[1] * 0.4));
            msg = new Label(s);
            Inventory.addMoney(-1 * (int) Math.round(Arena.getPrice()[0] * 0.25));
            Arena.getCombatans()[0].addXp((int) Math.round(Arena.getPrice()[1] * 0.4));
        }
        stats.getChildren().add(msg);
        pane.setCenter(stats);

        Button backtomenu = new Button("Rückkehr");
        backtomenu.setOnAction(e -> {
            Game.drawSpace();
        });
        pane.setBottom(backtomenu);


        return pane;
    }
}
