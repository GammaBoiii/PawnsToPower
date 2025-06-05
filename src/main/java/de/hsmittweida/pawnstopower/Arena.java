package de.hsmittweida.pawnstopower;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import javax.sound.midi.SysexMessage;

import java.util.Random;

public class Arena {

    private static Pawn choosenFighter;
    private static Pawn enemy;
    private static boolean fightFinished = false;
    private static TextFlow log;
    private static ScrollPane textField; /* Wird hier definiert, damit es den anderen Methoden sichtbar ist. */
    private static DoubleProperty currentHealth_enemy, currentHealth_fighter;
    private static Pawn nextTurn;

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
        textField = new ScrollPane();
        VBox textBox = new VBox();

        textField.setFitToHeight(true);
        textField.setFitToWidth(true);
        textField.setContent(textBox);

        log = new TextFlow();
        //log2.setText("\nDas hier ist ein Test, nice. Hier sollen später mal gaaaanz viele Kämpfer kämpfen. Mal gucken, wie dieses sich so schlagen wreden. Bin gespannt, hole popcorn!");
        //log2.setStyle("-fx-font-style: italic; -fx-font-weight: bold;");
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


        enemy = generateEnemy();

        /* Schlacthfeld */
        AnchorPane arena = new AnchorPane();
        field.setCenter(arena);
        arena.setMaxWidth(Double.MAX_VALUE);
        arena.setMaxHeight(Double.MAX_VALUE);
        arena.setId("arenaBG");
        Button attack = new Button("Angreifen"), defense = new Button("Verteidigen");
        AnchorPane.setTopAnchor(attack, 650.0);
        AnchorPane.setTopAnchor(defense, 650.0);
        AnchorPane.setLeftAnchor(attack, 500.0);
        AnchorPane.setRightAnchor(defense, 500.0);
        String quickStyle = "-fx-font-size: 25";
        attack.setStyle(quickStyle);
        defense.setStyle(quickStyle);
        arena.getChildren().addAll(attack, defense);

        /* Charactere darstellen */
        ImageView char2 = new ImageView(new Image(Arena.class.getResource("image/stickman.png").toExternalForm()));
        ImageView char1 = new ImageView(new Image(Arena.class.getResource("image/stickman.png").toExternalForm()));
        char1.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        char1.setFitHeight(250);
        char1.setFitWidth(250);
        char2.setFitHeight(250);
        char2.setFitWidth(250);
        arena.getChildren().addAll(char2,char1);
        AnchorPane.setLeftAnchor(char1, 150.0);
        AnchorPane.setTopAnchor(char1, 350.0);
        AnchorPane.setRightAnchor(char2, 150.0);
        AnchorPane.setTopAnchor(char2, 350.0);

        /* Lebensanzeigen */
        currentHealth_enemy = new SimpleDoubleProperty(enemy.getSkills().get(0).getSkillValue());
        currentHealth_fighter = new SimpleDoubleProperty(choosenFighter.getSkills().get(0).getSkillValue());

        /*
         * Die folgenden DoubleBindings dienen dazu, die ProgressBar korrekt anzuzeigen. Da die ProgressBar nur Werte von 0 bis 1 akzeptiert,
         * muessen die aktuellen Leben (currentHealth..), welche ja bereis vom Typ Property sind, erneut in ein Wert zwischen 0 und 1 normalisiert
         * werden. Dazu dienen die DoubleBindings mit ihren jeweiligen computeValue Methoden.
         */
        DoubleBinding healthProgress_enemy = new DoubleBinding() {
            {super.bind(currentHealth_enemy);}
            @Override
            protected double computeValue() {
                return currentHealth_enemy.get() / enemy.getSkills().get(0).getSkillValue();
            }
        };
        DoubleBinding healthProgress_fighter = new DoubleBinding() {
            {super.bind(currentHealth_fighter);}
            @Override
            protected double computeValue() {
                return currentHealth_fighter.get()  / choosenFighter.getSkills().get(0).getSkillValue();
            }
        };

        ProgressBar hb_enemy = new ProgressBar(currentHealth_enemy.get() / enemy.getSkills().get(0).getSkillValue());
        ProgressBar hb_fighter = new ProgressBar(currentHealth_fighter.get() / choosenFighter.getSkills().get(0).getSkillValue());
        hb_enemy.progressProperty().bind(healthProgress_enemy);
        hb_fighter.progressProperty().bind(healthProgress_fighter);
        arena.getChildren().addAll(hb_fighter, hb_enemy);
        AnchorPane.setLeftAnchor(hb_fighter, 150.0);
        AnchorPane.setTopAnchor(hb_fighter, 450.0);
        AnchorPane.setRightAnchor(hb_enemy, 150.0);
        AnchorPane.setTopAnchor(hb_enemy, 450.0);


        /* Kampf Logik */

        /*Wer beginnt */
        nextTurn = Math.random() < 0.5 ? enemy : choosenFighter;
        if(!nextTurn.getName().equals(choosenFighter.getName())) {
            Arena.log("Der Gegner beginnt", "-fx-fill: red;");
        } else {
            Arena.log("Du beginnst", "-fx-fill: green;");
        }



        // Debuggin Boxes:
        textField.setStyle("-fx-border-color: green; -fx-border-width: 1");
        field.setStyle("-fx-border-color: red; -fx-border-width: 1");
        arena.setStyle("-fx-border-color: blue; -fx-border-width: 1");
        Button add10 = new Button("+"), add50 = new Button("-"), add150 = new Button("150");
        add10.setOnAction(e-> {
            currentHealth_fighter.set(currentHealth_fighter.get() + 10.0);
            System.out.println(currentHealth_fighter.get());
        });
        add50.setOnAction(e-> {
            currentHealth_fighter.set(currentHealth_fighter.get() - 10.0);
            System.out.println(currentHealth_fighter.get());

        });
        add150.setOnAction(e-> {
            System.out.println(choosenFighter.addXp(150).get());
        });
        AnchorPane.setTopAnchor(add10, 20.0);
        AnchorPane.setTopAnchor(add50, 50.0);
        AnchorPane.setTopAnchor(add150, 70.0);
        arena.getChildren().addAll(add10,add50,add150);

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
        System.out.println("diff" + diff);
        byte level = (byte) (Math.random() < 0.5 ? diff * -1 : diff);
        System.out.println("level" + level);
        if(choosenFighter.getLvl() + level < 1) level = 0;
        p.setLvl(choosenFighter.getLvl() + level);

        System.out.println("Level Gegner: " + p.getLvl() + " >----< " + "Level Kaempfer: " + choosenFighter.getLvl());

        /* Skills zuweisen. 1 Level = 1 SkillPunkt. Diese müssen noch zufällig auf
         * alle Skills verteilt werden.
         */
        while(p.getSkillPoints() > 0) {
            Random rand = new Random();
            p.getSkills().get(rand.nextInt(p.getSkills().size())).addSkillLevel();
            p.addSkillPoints(-1);
        }
        for(Skill s : p.getSkills()) {
           // System.out.println(s.getName() + ": " + s.getSkillLevel() + " -> " + s.getSkillValue());
        }
        System.out.println("---------------");
        for(Skill s : choosenFighter.getSkills()) {
            // System.out.println(s.getName() + ": " + s.getSkillLevel() + " -> " + s.getSkillValue());
        }

        /* Damit das ganze im Kampf-Log auch eine Bedeutung bekommt:
        * (Das doppelte "leicht" und "schwer" ist nur ein Workaround für die Zuweisung von "difficult", da sonst evtl ein ArrayOutOfBounds Exception geworfen wird.
        * Das Level kann nämlich 2 Level unter oder über dem Level des eigenen Kämpfers sein.
        * */
        String[] difficult_arr = {"leicht", "leicht", "moderat", "schwer", "schwer"};
        String dif_mod = "";
        String difficult = "";
        difficult = difficult_arr[p.getLvl() - choosenFighter.getLvl() + 2];
        if(Math.abs(p.getLvl()-choosenFighter.getLvl()) >= 2) dif_mod = "sehr ";
        Arena.log("Dieser Kampf wird " + dif_mod + difficult, "-fx-font-style: italic; -fx-font-weight: bold");

        return p;
    }

    private static void log(String text) {
        Text newText = new Text("\n"+text);

        /* Da zu diesem Zeitpunkt die Grafik Elemente noch nicht gerendert sind, müssen Anpassungen
         * erst später vorgenommen werden. Dazu wird einfach der Property der Breite vom Text an die der VBox gehangen..
         * Ist daher auch später dynamisch, wenn das Fenster vergrößert/verkleinert wird.
         */
        newText.wrappingWidthProperty().bind(textField.widthProperty());

        log.getChildren().add(newText);
    }
    private static void log(String text, String style) {
        Text newText = new Text(text+"\n");
        newText.setStyle(style);
        log.getChildren().add(newText);
    }
}
