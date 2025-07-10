package de.hsmittweida.pawnstopower;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.geom.Area;

public class ArenaWinScreen {
    public static Pane winscreen(boolean playerWins) {
        BorderPane pane = new BorderPane();
        Label header = new Label("Die Spiele sind vorbei!");
        pane.setTop(header);

        VBox stats = new VBox();
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
                    Arena.getPrice()[1]
            );

            msg = new Label(s);
        } else {
            String s = String.format("""
                    Du verlierst gegen %s
                    und damit %d Goldmünzen.
                    Dein Kämpfer erhält %d Erfahrungspunkte.
                    """,
                    Arena.getCombatans()[1].getName(), Math.round(Arena.getPrice()[0] * 0.25), Math.round(Arena.getPrice()[1] * 0.4));

            msg = new Label(s);
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
