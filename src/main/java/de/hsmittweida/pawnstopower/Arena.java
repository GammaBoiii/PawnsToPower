package de.hsmittweida.pawnstopower;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Die Arena Klasse beinhaltet die Oberflächen und die Logik für die Arena.
 * In einem Arenakampf kämpft immer ein Kämpfer des Spielers gegen einen zufällig generierten Gegener.
 * Gewinnt der Spieler, so gewinnt er Gold und Erfahrungspunkte für den teilnehmenden Kämpfer. Außerdem
 * kann der gegnerische, besiegte Kämpfe mit einer gewissen Chance in das Team des Spielers übergehen.
 */
public class Arena {

    private static Pawn choosenFighter;
    private static Pawn enemy;
    private static boolean fightFinished = false;
    private static TextFlow log;
    private static ScrollPane textField; /* Wird hier definiert, damit es den anderen Methoden sichtbar ist. */
    private static DoubleProperty currentHealth_enemy, currentHealth_fighter;
    private static Pawn nextTurn;
    private static Button attack, defense;
    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>(); /* Wird benötigt, um Events zwischen Arena.java und Turn.java zu erwarten (versch. Threads)" */
    private static Turn turn;
    private static Pawn winner;
    private static int[] price;

    /**
     * Erstellt die Oberfläche, bei der der Spieler sich einen seiner Kämpfer aussucht, mit dem
     * er in die Arena geht.
     * @return Pane, welches dann in {@code Game.drawSpace()} angezeigt wird.
     */
    public static Pane chooseFighter() {
        AnchorPane pane = new AnchorPane();
        Tools.addStylesheet(pane, "style_arena.css");
        Label label = new Label("Wähle deinen Pawn aus!");
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
            Region spacerR = new Region();
            HBox.setHgrow(spacerR, Priority.ALWAYS);
            spacerR.setStyle("""
                        -fx-border-color: gray transparent gray transparent;
                        -fx-border-width: 0 0 1 0;
                        -fx-translate-y: 0;
                    """);
            Button choose = new Button("Kampf!");
            if(p.hashFoughtToday()) {
                choose.setDisable(true);
            }
            choose.setOnAction(e -> {
                choose.setDisable(true);
                choosenFighter = p;
                Game.drawSpace(Arena.arenaFight());
                p.setFoughtToday(true);
                Game.setNextDayButtonDisabled(true);
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

    /**
     * Dient zum Anzeigen der Arena und zum Initiieren des Kampfes.
     * Beinhaltet hauptsächlich UI handling.
     * @return FX-Element (Pane), welches dann im Spiel mittels {@code Game.drawSpace} angezeigt wird.
     */
    public static Pane arenaFight() {
        AnchorPane pane = new AnchorPane();
        BorderPane field = new BorderPane();
        Tools.addStylesheet(pane, "style_arena.css");
        Button mainMenu = new Button("Hauptmenu");
        mainMenu.setOnAction(e -> {
            String res = Tools.confirmPopup("Kampf abbrechen", "Möchtest du den Kampf beenden?", "Wenn du diesen Kampf abbrichst, verlierst du 50% deiner Reputation und erhälst keine Belohnung.");
            if(res.equals("yes")) {
                if(turn != null) turn.kill();
                Game.drawSpace();
                Inventory.addReputation((int) (Inventory.getReputation().get() * 0.5 * -1));
            }
        });
        AnchorPane.setTopAnchor(mainMenu, 2.0);
        AnchorPane.setLeftAnchor(mainMenu, 2.0);

        winner = null;

        /* Textbereich, der als Kampf-Log dient */
        textField = new ScrollPane();
        VBox textBox = new VBox();
        textField.setFitToHeight(true);
        textField.setFitToWidth(true);
        textField.setContent(textBox);
        log = new TextFlow();
        log.setId("log");
        textBox.getChildren().add(log);
        AnchorPane.setLeftAnchor(field, 35.0);
        AnchorPane.setRightAnchor(field, 35.0);
        AnchorPane.setTopAnchor(field, 50.0);
        AnchorPane.setBottomAnchor(field, 20.0);
        textField.setMinHeight(250.0);
        textField.setMaxHeight(250.0);
        textField.vvalueProperty().bind(textBox.heightProperty());
        textField.hbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.NEVER);
        pane.getChildren().addAll(mainMenu,field);
        field.setBottom(textField);

        /* Gegner wird generiert */
        enemy = generateEnemy();

        /* Schlacthfeld */
        AnchorPane arena = new AnchorPane();
        field.setCenter(arena);
        arena.setMaxWidth(Double.MAX_VALUE);
        arena.setMaxHeight(Double.MAX_VALUE);
        arena.setId("arenaBG");
        attack = new Button("Angreifen");
        defense = new Button("Verteidigen");
        attack.setOnAction(e -> {
            inputQueue.offer("attack");
            Arena.disableActionButtons(true);
        });
        defense.setOnAction(e -> {
            inputQueue.offer("defense");
            Arena.disableActionButtons(true);
        });
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

        /* Tot */
        currentHealth_fighter.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 0) {
                currentHealth_fighter.set(0);
            } else if (newValue.intValue() == 0) {
                if (winner == null) {
                    winner = enemy;
                    fightOver(false);
                }
                char1.setRotate(90);
            }
        });
        currentHealth_enemy.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 0) {
                currentHealth_enemy.set(0);
            } else if (newValue.intValue() == 0) {
                if (winner == null) {
                    winner = choosenFighter;
                    fightOver(true);
                }
                char2.setRotate(270);

            }
        });

        /* Die folgenden DoubleBindings dienen dazu, die ProgressBar korrekt anzuzeigen. Da die ProgressBar nur Werte von 0 bis 1 akzeptiert,
         * müssen die aktuellen Leben (currentHealth..), welche ja bereis vom Typ Property sind, erneut in ein Wert zwischen 0 und 1 normalisiert
         * werden. Dazu dienen die DoubleBindings mit ihren jeweiligen computeValue Methoden. */
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

        /* Name der Kämpfer */
        Label fighterlabel = new Label(choosenFighter.getName());
        Label enemylabel = new Label(enemy.getName());
        AnchorPane.setLeftAnchor(fighterlabel, 150.0);
        AnchorPane.setTopAnchor(fighterlabel, 550.0);
        AnchorPane.setRightAnchor(enemylabel, 150.0);
        AnchorPane.setTopAnchor(enemylabel, 550.0);
        arena.getChildren().addAll(fighterlabel, enemylabel);

        // Debug
        Label healthlabel_fighter = new Label("");
        Label healthlabel_enemy = new Label("");
        healthlabel_fighter.textProperty().bind(currentHealth_fighter.asString("%.0f"));
        healthlabel_enemy.textProperty().bind(currentHealth_enemy.asString("%.0f"));
        arena.getChildren().addAll(healthlabel_enemy, healthlabel_fighter);
        AnchorPane.setLeftAnchor(healthlabel_fighter, 150.0);
        AnchorPane.setTopAnchor(healthlabel_fighter, 430.0);
        AnchorPane.setRightAnchor(healthlabel_enemy, 150.0);
        AnchorPane.setTopAnchor(healthlabel_enemy, 430.0);


        /* Kampf Logik */
        /*Wer beginnt */
        nextTurn = Math.random() < 0.5 ? enemy : choosenFighter;
        if(!nextTurn.getName().equals(choosenFighter.getName())) {
            Arena.log("Der Gegner beginnt", "-fx-fill: red;");
        } else {
            Arena.log("Du beginnst", "-fx-fill: green;");
        }

        turn = new Turn(nextTurn);


        // Debuggin Boxes:
        textField.setStyle("-fx-border-color: green; -fx-border-width: 3");
        //field.setStyle("-fx-border-color: red; -fx-border-width: 1");
        //arena.setStyle("-fx-border-color: blue; -fx-border-width: 1");
        Button add10 = new Button("+"), add50 = new Button("-"), add150 = new Button("150");
        add10.setOnAction(e-> {
            currentHealth_fighter.set(currentHealth_fighter.get() + 10.0);
            currentHealth_enemy.set(currentHealth_enemy.get() + 10.0);
            //System.out.println(currentHealth_fighter.get() / choosenFighter.getSkills().get(0).getSkillValue());
        });
        add50.setOnAction(e-> {
            currentHealth_fighter.set(currentHealth_fighter.get() - 10.0);
            currentHealth_enemy.set(currentHealth_enemy.get() - 10.0);
            //System.out.println(currentHealth_fighter.get() / choosenFighter.getSkills().get(0).getSkillValue());

        });
        add150.setOnAction(e-> {
            //System.out.println(choosenFighter.addXp(150).get());
        });
        AnchorPane.setTopAnchor(add10, 20.0);
        AnchorPane.setTopAnchor(add50, 50.0);
        AnchorPane.setTopAnchor(add150, 70.0);
        arena.getChildren().addAll(add10,add50,add150);

        return pane;
    }

    /**
     * Generiert basierend auf dem eigenen Kämpfer einen Gegner, der per Zufall in einem gewissen Bereiches ungefähr so stark ist.
     */
    private static Pawn generateEnemy() {
        Pawn p = new Pawn();

        /* Level generieren. Abweichung von max +- 2 Leveln */
        Random rnd = new Random();
        byte diff = (byte) rnd.nextInt(3);
        //System.out.println("diff" + diff);
        byte level = (byte) (Math.random() < 0.5 ? diff * -1 : diff);
        //System.out.println("level" + level);
        if(choosenFighter.getLvl() + level < 1) level = 0;
        p.setLvl(choosenFighter.getLvl() + level);

        /* Skills zuweisen. 1 Level = 1 SkillPunkt. Diese müssen noch zufällig auf
         * alle Skills verteilt werden. */
        while(p.getSkillPoints() > 0) {
            Random rand = new Random();
            p.getSkills().get(rand.nextInt(p.getSkills().size())).addSkillLevel();
            p.addSkillPoints(-1);
        }

        //Debug
        for(Skill s : p.getSkills()) {
           // System.out.println(s.getName() + ": " + s.getSkillLevel() + " -> " + s.getSkillValue());
        }
        //System.out.println("---------------");
        for(Skill s : choosenFighter.getSkills()) {
            // System.out.println(s.getName() + ": " + s.getSkillLevel() + " -> " + s.getSkillValue());
        }

        /* Damit das ganze im Kampf-Log auch eine Bedeutung bekommt:
        * (Das doppelte "leicht" und "schwer" ist nur ein Workaround für die Zuweisung von "difficult", da sonst evtl ein ArrayOutOfBounds Exception geworfen wird.
        * Das Level kann nämlich 2 Level unter oder über dem Level des eigenen Kämpfers sein.*/
        String[] difficult_arr = {"leicht", "leicht", "moderat", "schwer", "schwer"};
        String dif_mod = "";
        String difficult = "";
        difficult = difficult_arr[p.getLvl() - choosenFighter.getLvl() + 2];
        if(Math.abs(p.getLvl()-choosenFighter.getLvl()) >= 2) dif_mod = "sehr ";
        Arena.log("Dieser Kampf wird " + dif_mod + difficult, "-fx-font-style: italic; -fx-font-weight: bold");

        /* Belohung anpassen */
        int baseXPreward = 25;
        int baseMoneyreward = 65;
        double baseMult = 0.0;
        if(dif_mod.equals("sehr ")) {
            baseMult = 0.25;
        } else baseMult = 0.1;
        if(difficult.equals("leicht")) {
            baseMult *= -1;
        } else if(difficult.equals("moderat")) {
            baseMult = 0.0;
        }
        baseMult += (0.5 - Math.random()) * 0.1; /* Noch etwas RNG mit reinbringen.. */
        //System.out.println("BaseMult: " + (1+baseMult));
        baseXPreward = (int)(baseXPreward *  (1+baseMult));
        baseMoneyreward = (int)(baseMoneyreward * (1+baseMult));
        price = new int[]{baseMoneyreward, baseXPreward};

        /* Der Gegner braucht natürlich noch Equippment..
        * Dies wird komplett zufällig generiert*/
        Weapon w = new Weapon(p);
        p.giveWeapon(w, (byte) 0);
        Armor a1 = new Armor(p, (byte) 0);
        Armor a2 = new Armor(p, (byte) 1);
        Armor a3 = new Armor(p, (byte) 2);
        Armor a4 = new Armor(p, (byte) 3);
        p.giveArmor(a1, (byte) 0);
        p.giveArmor(a2, (byte) 1);
        p.giveArmor(a3, (byte) 2);
        p.giveArmor(a4, (byte) 3);
        //System.out.println("Gegner Equippment: " +  p.getWeapon((byte) 0).getName() + " - " + p.getWeapon((byte) 0).getWeaponClass() + "| " + p.getArmor((byte) 0).getName() + " | " + p.getArmor((byte) 1).getName() +" | " + p.getArmor((byte) 2).getName() +" | " + p.getArmor((byte) 3).getName() );

        return p;
    }

    /**
     * Logt einen Text ohne Stil in den Log der Arena.
     * Das Platform.runLater() wird hierbei benötigt, damit auch von anderen Threads (außerhalb des FX-Threads) eine Nachricht
     * im Log angezeigt werden kann. JavaFX erlaubt es andernweitig nicht, dass ein Thread UI-Aktualisierungen in dem FX-Thread
     * vornimmt, und wirft daher standarmäßig einen Fehler. Verwendet wird dies zum Beispiel in der Turn-Klasse, welche einen
     * eigenen Thread erstellt. Die dort aufgefürhten "sleep" Befehle lassen sich aber auch nicht in dem FX-Thread verwenden,
     * da sonst das ganze UI eingefroren wird, und sind daher nicht für diese Anwendung geeignet.
     * @param text Der Text, der im Arena Log ausgegeben werden soll.
     */
    public static void log(String text) {
        /* Da zu diesem Zeitpunkt die Grafik Elemente noch nicht gerendert sind, müssen Anpassungen
         * erst später vorgenommen werden. Dazu wird einfach der Property der Breite vom Text an die der VBox gehangen..
         * Ist daher auch später dynamisch, wenn das Fenster vergrößert/verkleinert wird.
         */
        Platform.runLater(() -> {
            Text newText = new Text("\n"+text);
            newText.wrappingWidthProperty().bind(textField.widthProperty());
            log.getChildren().add(newText);
        });
    }

    /**
     * Logt einen Text mit einem CSS Style in den Log der Arena.
     * Weitere Erklärung zur Methode sind in {@code Arena.log()} zu finden
     * @param text Der Text, der im Arena Log ausgegeben werden soll.
     * @param style Der CSS Style, der auf den {@code text} angewendet werden soll.
     */
    public static void log(String text, String style) {
        Platform.runLater(() -> {
            Text newText = new Text("\n"+text);
            newText.wrappingWidthProperty().bind(textField.widthProperty()); // ?
            newText.setStyle(style);
            log.getChildren().add(newText);
        });
    }

    /**
     * Fügt dem Pawn schaden hinzu. Diese Methode wurde vor allem für die Spielführung entworfen, damit man von der Turn-Klasse, welche in einem
     * verschieden Thread läugt, Schaden hinzufügen kann. Auch dafür wird {@code Platform.runlater()} verwendet.
     * @param p Pawn, der Schaden erhalten soll.
     * @param damage Wie viel Schaden verursacht werden soll.
     */
    public static void damage(Pawn p, double damage) {
        Platform.runLater(() -> {
            if(p.equals(choosenFighter)) {
                currentHealth_fighter.set(Math.round(currentHealth_fighter.get() - damage));
            } else if(p.equals(enemy)) {
                currentHealth_enemy.set(Math.round(currentHealth_enemy.get() - damage));
            }
        });
    }

    /**
     * @param p Pawn, von dem die Lebenspunkte zurückgegeben werden sollen.
     * @return Die Lebenspunkte des angegebenen Pawns
     */
    public static double getLife(Pawn p) {
            if(p.equals(choosenFighter)) return Math.round(currentHealth_fighter.get());
            else if(p.equals(enemy)) return Math.round(currentHealth_enemy.get());
            return 0;
    }

    /**
     * Einfache Methode, die den Pawn zurückgibt, der gerade nicht am Zug ist. Da nur 2 gegeneinander Kämpfen, kann es entweder der Kämpfer sein, der
     * dem Spieler gehört oder nicht.
     * @param pawn Pawn, der gerade am Zug ist und dessen Gegner returned werden soll.
     * @return Der Pawn, der gerade nicht am Zug ist.
     */
    public static Pawn getOther(Pawn pawn) {
        if(pawn.ownedByPlayer()) {
            return enemy;
        } else {
            return choosenFighter;
        }
    }

    /**
     * Deaktiviert die Action Buttons (Angreifen/Verteidigen), wenn der Spieler nicht am Zug ist
     * Nutzt ebenfalls runLater(), da die Methode aus einem anderen (nicht FX-) Thread aufgerufen wird (Turn-Klasse)
     * @param disabled Boolean, der den Aktiv-Status der Buttons festlegt.
     */
    public static void disableActionButtons(boolean disabled) {
            Platform.runLater(() -> {
                attack.setDisable(disabled);
                defense.setDisable(disabled);
            });
    }

    /**
     * @return inputQeue, an die der "Trigger" gesendet werden soll.
     */
    public static BlockingQueue<String> getBlockingQeue() {
        return inputQueue;
    }

    /**
     * Eine einfache Methode, die bei jedem Zug des Gegners eine automatische Nachricht generiert, damit
     * das ganze Kampfgeschehen im Arena Log etwas mehr Story hat.
     * @return Angriffsnachricht
     */
    public static String[] getEnemyAttackMessage() {
        String[] gegnerInitial = {
                "Der Gegner beobachtet die Umgebung.",
                "Der Gegner knackt mit den Fingern und grinst finster.",
                "Der Gegner hebt seine Waffe langsam an.",
                "Der Gegner umrundet dich vorsichtig.",
                "Der Gegner blickt dir tief in die Augen und wartet auf deine Reaktion.",
                "Der Gegner richtet seinen Stand aus und atmet tief durch.",
                "Der Gegner schleift seine Waffe am Boden entlang.",
                "Ein leises Knurren kommt aus seiner Kehle.",
                "Der Gegner duckt sich leicht – bereit zum Angriff.",
                "Er streckt den Rücken durch und spannt die Muskeln an.",
                "Seine Augen verengen sich, als würde er deine nächste Bewegung erraten.",
                "Der Gegner klopft sich den Staub von den Schultern.",
                "Ein finsteres Lächeln spielt auf seinen Lippen.",
                "Er hebt eine Augenbraue, als wolle er dich herausfordern.",
                "Der Gegner wirbelt seine Waffe einmal durch die Luft.",
                "Du hörst, wie er die Zähne zusammenbeißt.",
                "Der Gegner holt tief Luft – als wäre dies sein Moment.",
                "Der Gegner setzt einen Fuß nach vorne – langsam, aber bestimmt.",
                "Ein Flackern von Entschlossenheit liegt in seinem Blick.",
                "Der Gegner schlägt sich leicht mit der Faust gegen die Brust – ein Kampfritual."
        };
        String[] gegnerAktion = {
                "Der Gegner prescht nach vorne.",
                "Der Gegner springt mit einem Kampfschrei auf dich zu.",
                "Der Gegner schleudert sich mit voller Wucht in deine Richtung.",
                "Der Gegner führt einen Seitwärtsschlag aus.",
                "Der Gegner setzt zu einem wuchtigen Angriff an.",
                "Mit überraschender Geschwindigkeit schnellt der Gegner vor.",
                "Der Gegner schleudert seine Waffe mit einem gezielten Schwung.",
                "Ein schneller Ausfallschritt bringt ihn direkt vor dich.",
                "Er lässt seine Faust mit voller Wucht auf dich zuschießen.",
                "Der Gegner schwingt seine Waffe in weitem Bogen.",
                "Ein harter Tritt zielt auf deinen Oberkörper.",
                "Der Gegner holt zu einem wuchtigen Schlag aus.",
                "Ein Schrei begleitet seinen blitzschnellen Angriff.",
                "Er dreht sich zur Seite und attackiert aus der Drehung heraus.",
                "Der Gegner springt hoch und zielt auf deinen Kopf.",
                "Er greift mit beiden Händen gleichzeitig an.",
                "Der Gegner rollt sich ab und kommt direkt unter dir hervor.",
                "Ein schneller Stoß zielt auf deinen Bauch.",
                "Er tritt einen Schritt zur Seite und greift sofort an.",
                "Der Gegner schlägt von oben herab wie ein Sturm."
        };
        String[] gegnerFolge = {
                "Die Waffe des Gegners saust auf dich herab.",
                "Ein dumpfer Schlag trifft dich an der Schulter.",
                "Ein Schnitt durchzieht die Luft, gefolgt von einem schmerzhaften Aufprall.",
                "Der Boden unter dir erzittert leicht, als der Angriff aufprallt.",
                "Ein harter Treffer bringt dich kurz aus dem Gleichgewicht.",
                "Du spürst den Luftzug des Schlags an deinem Gesicht vorbeirauschen.",
                "Ein klirrender Aufprall lässt deine Waffe erzittern.",
                "Der Angriff trifft dein Bein – du knickst leicht ein.",
                "Der Schlag streift deine Seite – ein brennender Schmerz bleibt zurück.",
                "Ein knurrender Laut entweicht dem Gegner, als der Angriff sitzt.",
                "Du stolperst rückwärts, während Funken durch die Luft fliegen.",
                "Ein Stück deiner Kleidung wird zerrissen.",
                "Du landest schwer auf dem Rücken.",
                "Der Gegner nutzt den Schwung und wirbelt erneut herum.",
                "Ein pochender Schmerz zieht durch deinen Arm.",
                "Du taumelst einige Schritte zurück.",
                "Der Boden splittert unter dem Einschlag.",
                "Eine Druckwelle schleudert dich zur Seite.",
                "Dein Atem stockt kurz – der Treffer war härter als gedacht."
        };
        Random rnd = new Random();
        return new String[]{gegnerInitial[rnd.nextInt(gegnerInitial.length)], gegnerAktion[rnd.nextInt(gegnerAktion.length)], gegnerFolge[rnd.nextInt(gegnerFolge.length)]};
    }

    /**
     * Ähnlich wie {@code getEnemyAttackMessage()}, nur wenn der Gegner in die Verteidigung geht, anstatt in den Angriff.
     * @return Verteidigungsnachricht
     */
    public static String getEnemyDefenseMessage() {
        String[] gegnerVerteidigung = {
                "Der Gegner nimmt eine defensive Haltung ein.",
                "Mit erhobenen Armen geht der Gegner langsam in Deckung.",
                "Der Gegner stemmt die Beine fest in den Boden – bereit für deinen Angriff.",
                "Ein kurzer Atemzug, dann bringt er sich in Verteidigungsposition.",
                "Der Gegner senkt seinen Schwerpunkt und beobachtet dich wachsam.",
                "Er hebt die Schultern und zieht die Waffe eng an den Körper.",
                "Der Gegner zieht sich leicht zurück, ohne den Blick von dir abzuwenden.",
                "Ein stiller Moment – der Gegner fixiert dich und spannt die Muskeln an.",
                "Seine Bewegungen verlangsamen sich – voll konzentriert auf die Abwehr.",
                "Mit einem Schritt zur Seite bringt er sich in Position.",
                "Der Gegner hebt sein linkes Bein leicht, um den Stand anzupassen.",
                "Er richtet seinen Schild aus und stellt sich breit auf.",
                "Ein leichtes Nicken – als hätte er deinen Angriff schon kommen sehen.",
                "Der Gegner atmet ruhig aus und fokussiert sich ganz auf deine Haltung.",
                "Mit ruhiger Entschlossenheit bereitet sich der Gegner auf deinen nächsten Zug vor."
        };
        Random rnd = new Random();
        return gegnerVerteidigung[rnd.nextInt(gegnerVerteidigung.length)] + "\n\t- Der Gegner geht in die Verteidigung und erhöht seine Konzentration";
    }

    /**
     * Wenn der Kampf vorrüber ist, wird nach einer bestimmten Zeit der Winscreen mit entsprechender Nachricht,
     * ob der Spieler gewonnen oder verloren hat, angezeigt.
     * @param playerWins
     */
    private static void fightOver(boolean playerWins) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                if(!turn.getThread().isInterrupted()) {
                    Platform.runLater(() -> {
                        Game.drawSpace(ArenaWinScreen.winscreen(playerWins));
                    });
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
        /*
        if(playerWins) {
            Tools.popup("Die Spiele sind vorbei!", "Du hast den Gegner geschlagen", """
                    Deine Belohnung ist:
                        Güldmünzen
                        Erfahrungspunkte
                    """ );
        } else {
            Tools.popup("Die Spiele sind vorbei!", "Du hast den Kampf verloren", """
                    Dein Kämpfer verliert:
                        Goldmünzen
                        Erfahrungspunkte
                    """);
        }
         */
    }

    /**
     * @return den Preis, der in der Arena vefügbar ist, wenn der Spieler gewinnt.
     */
    public static int[] getPrice() {
        return price;
    }

    /**
     * @return den Kämpfer des Spielers und den generierten Feind; in einem Array.
     */
    public static Pawn[] getCombatans( ){
        return new Pawn[] {choosenFighter, enemy};
    }
}
