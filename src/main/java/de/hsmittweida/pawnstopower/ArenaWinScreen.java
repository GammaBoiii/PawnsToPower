package de.hsmittweida.pawnstopower;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Random;

/**
 * Die ArenaWinScreen Klasse beinhaltet die Oberfläche, die angezeigt wird,
 * wenn der Kampf in der Arena vorrüber ist.
 */
public class ArenaWinScreen {
    /**
     * Gibt ein Pane zurück, welches dann in {@link Game#drawSpace()} angezeigt wird.
     *
     * @param playerWins {@code true}, wenn der Spieler gewonnen hat. Sonst {@code false}.
     * @return {@code Pane}
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
            if (Math.random() < 0.1) { //Debug 100%
                Pawn p = Arena.getCombatans()[1];
                for (Armor a : p.getArmors()) {
                    p.removeArmor(a);
                }
                for (Weapon w : p.getWeapons()) {
                    p.removeWeapon(w);
                }
                Inventory.addPawn(p);
                String congrats = String.format("""
                        Der Pawn %s schließt sich außerdem deinem Trupp an!
                        Damit erhälst du einen neuen Level %d Kämpfer.
                        """, p.getName(), p.getLvl());
                s += congrats;
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
            if (Inventory.getMoney() < 0) {
                Inventory.addMoney(Inventory.getMoney() * -1);
            }

            /* Spieler bekommt trotzdem ein Trostpreis: */
            Arena.getCombatans()[0].addXp((int) Math.round(Arena.getPrice()[1] * 0.4));
        }
        stats.getChildren().add(msg);
        pane.setCenter(stats);

        Diary.writeDiaryEntry(generateArenaDiaryMsg(playerWins));

        Button backtomenu = new Button("Rückkehr");
        backtomenu.setOnAction(e -> {
            Game.drawSpace();
        });
        pane.setBottom(backtomenu);
        return pane;
    }

    /**
     * Generiert ein Eintrag ins Tagebuch nach einem Arena-Kampf.
     *
     * @param won {@code true}, wenn der Spieler gewonnen hat.
     * @return {@code String} mit der Nachricht.
     */
    public static String generateArenaDiaryMsg(boolean won) {
        String[] msg = new String[3];
        if (won) {
            msg[0] = Arena.getCombatans()[0].getName() + " besiegt den Gegner.\n";
            msg[1] = Arena.getCombatans()[1].getName() + " wurde von uns geschlagen.\n";
            msg[2] = "Wir haben den Kampf mit " + Arena.getCombatans()[0].getName() + " an unserer Seite gewonnen.\n";
        } else {
            msg[0] = Arena.getCombatans()[0].getName() + " wurde vom Gegner besiegt.\n";
            msg[1] = Arena.getCombatans()[1].getName() + " hat uns in der Arena geschlagen.\n";
            msg[2] = "Wir haben den Kampf mit " + Arena.getCombatans()[0].getName() + " an unserer Seite nicht für uns entscheiden können. \n";
        }
        return msg[new Random().nextInt(msg.length)];
    }
}
