package de.hsmittweida.pawnstopower;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class Arena {

    private static Pawn choosenFighter;
    private static Pawn Enemy;
    private static boolean fightFinished = false;

    public static Pane chooseFighter() {
        AnchorPane pane = new AnchorPane();
        Tools.addStylesheet(pane, "style_arena.css");
        Label label = new Label("Wähle deinen Kämpfer aus!");
        label.setStyle("-fx-border-color: red; -fx-border-width: 2"); //Debug
        Button mainMenu = new Button("Hauptmenu");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });
        AnchorPane.setTopAnchor(mainMenu, 2.0);
        AnchorPane.setLeftAnchor(mainMenu, 2.0);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        ScrollPane sp = new ScrollPane();
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        VBox fighters = new VBox();
        fighters.setMaxWidth(Double.MAX_VALUE);
        for (Pawn p : Inventory.getPawns()) {


            Label name = new Label(p.getName());
            Label spacer = new Label("\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-");

            Region spacerR = new Region();
            HBox.setHgrow(spacerR, Priority.ALWAYS);
            spacerR.setStyle("""
                        -fx-border-color: gray transparent gray transparent;
                        -fx-border-width: 0 0 1 0;
                        -fx-translate-y: 0;
                    """);
            Button choose = new Button("Kampf!");

            choose.setOnAction(e -> {
                choosenFighter = p;
                Game.drawSpace(Arena.arenaFight());
            });

            HBox fighter = new HBox(10, name, spacerR, choose);
            fighter.setPadding(new Insets(20,0,0,0));

            fighters.getChildren().add(fighter);
        }
        sp.setContent(fighters);
        AnchorPane.setLeftAnchor(sp, 20.0);
        AnchorPane.setRightAnchor(sp, 20.0);
        AnchorPane.setTopAnchor(sp, 80.0);
        AnchorPane.setBottomAnchor(sp, 25.0);
        AnchorPane.setTopAnchor(label, 50.0);
        AnchorPane.setLeftAnchor(label, 25.0);
        AnchorPane.setRightAnchor(label, 25.0);
        pane.getChildren().addAll(mainMenu, label, sp);
        return pane;
    }

    public static Pane arenaFight() {
        AnchorPane pane = new AnchorPane();
        BorderPane field = new BorderPane();
        Tools.addStylesheet(pane, "style_arena.css");
        Button mainMenu = new Button("Hauptmenu");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });
        AnchorPane.setTopAnchor(mainMenu, 2.0);
        AnchorPane.setLeftAnchor(mainMenu, 2.0);

        /* Textbereich, der als Kampf-Log dient */
        ScrollPane textField = new ScrollPane();
        VBox textBox = new VBox();

        textField.setFitToHeight(true);
        textField.setFitToWidth(true);
        textField.setContent(textBox);

        Text log = new Text();
        log.setText("Das hier ist ein Test, nice. Hier sollen später mal gaaaanz viele Kämpfer kämpfen. Mal gucken, wie dieses sich so schlagen wreden. Bin gespannt, hole popcorn!");
        log.setId("log");

        textBox.getChildren().add(log);

        AnchorPane.setLeftAnchor(field, 35.0);
        AnchorPane.setRightAnchor(field, 35.0);
        AnchorPane.setTopAnchor(field, 50.0);
        AnchorPane.setBottomAnchor(field, 20.0);
        textField.setMinHeight(150.0);
        textField.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.NEVER);
        textField.hbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.NEVER);
        pane.getChildren().addAll(mainMenu,field);
        field.setBottom(textField);

        /* Da zu diesem Zeitpunkt die Grafik Elemente noch nicht gerendert sind, müssen Anpassungen
         * erst später vorgenommen werden. Dazu wird einfach der Property der Breite vom Text an die der VBox gehangen..
         * Ist daher auch später dynamisch, wenn das Fenster vergrößert/verkleinert wird.
         */
        log.wrappingWidthProperty().bind(textField.widthProperty());

        /* Schlacthfeld */
        AnchorPane arena = new AnchorPane();
        field.setCenter(arena);
        arena.setMaxWidth(Double.MAX_VALUE);
        arena.setMaxHeight(Double.MAX_VALUE);
        arena.setId("arenaBG");

        // Debuggin Boxes:
        textField.setStyle("-fx-border-color: green; -fx-border-width: 1");
        field.setStyle("-fx-border-color: red; -fx-border-width: 1");
        arena.setStyle("-fx-border-color: blue; -fx-border-width: 1");

        log.setText(log.getText() + "\n" + "AYOOOOOOOOOOOOOOOOOOOYOYOYOOOOOOOOOOOOOOOO82822828828282828LOOOOOOOOOOOOOOOOOl");


        generateEnemy();

        return pane;
    }

    /**
     * Generiert basierend auf dem eigenen Kämpfer einen Gegner, der innerhlabt eines gewissen Bereiches ungefähr so stark ist.
     */
    private static Pawn generateEnemy() {
        Pawn p = new Pawn();


        /* Level generieren. Abweichung von max +- 2 Leveln */
        Random rnd = new Random();
        byte diff = (byte) rnd.nextInt(3);
        byte level = (byte) (Math.random() < 0.5 ? diff * -1 : diff);
        if(level < 1) level = 1;
        p.setLevel((byte) (choosenFighter.getLevel() + level));

        System.out.println("Level: " + p.getLevel());

        /* Skills zuweisen. 1 Level = 1 SkillPunkt. Diese müssen noch zufällig auf
         * alle Skills verteilt werden.
         */
        p.addSkillPoints((int) p.getLevel());
        while(p.getSkillPoints() > 0) {
            Random rand = new Random();
            p.getSkills().get(rand.nextInt(p.getSkills().size())).addSkillLevel();
            p.addSkillPoints(-1);
        }
        for(Skill s : p.getSkills()) {
            System.out.println(s.getName() + ": " + s.getSkillLevel() + " -> " + s.getSkillValue());
        }
        System.out.println("---------------");
        for(Skill s : Inventory.getPawns().get(0).getSkills()) {
            System.out.println(s.getName() + ": " + s.getSkillLevel() + " -> " + s.getSkillValue());
        }
        return p;
    }
}
