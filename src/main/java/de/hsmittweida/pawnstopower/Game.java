package de.hsmittweida.pawnstopower;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Die Hauptklasse des Spiels.
 * Dient der Anzeige sämtlicher UI im Spielvordergrund.
 * Zählt Tage.
 * Lädt Mechaniken wie Fonts.
 */
public class Game {
    private static ArrayList<Pawn> pawns;
    private static Stage stage;
    private static IntegerProperty day;

    private static Pane gameSpace;
    private static BorderPane panel;
    private static HBox helpBar;
    private static VBox sideBar;

    private static TextFlow diary;
    private static int diaryIndex;
    private static Button prevDay, nextDay;

    private static HashMap<String, Font> fonts;

    /**
     * @deprecated
     */
    Game() {
    }

    /**
     * Diese Methode lädt die Standardoberfläche in die Szene.
     * Dient als Startpunkt.
     */
    public static void Game_view() {
        /* Fonts laden */
        if(fonts == null) {
            fonts = new HashMap<String, Font>();
        }
        loadFonts();

        /* Initiierungen */
        day = new SimpleIntegerProperty(0);
        diary = new TextFlow();
        Inventory.setup();
        CreateWindow();
        // Debug();

        /* Musik */
        SoundManager mainmenutheme = new SoundManager("GameDefaultMusic_1.wav", "GameDefaultMusic_2.wav", "GameDefaultMusic_3.wav", "GameDefaultMusic_4.wav");
        stage.setOnHiding(e -> {
            mainmenutheme.getMediaPlayer().stop();
        });
    }

    /**
     * Methode, die sämtliche Erstellungen der Oberflächen zusammenfasst.
     * Beinhaltet das {@code panel}, als zentrale Oberfläche, die die
     * weiteren Oberflächen zusammenfasst.
     */
    public static void CreateWindow() {
        stage = new Stage();
        panel = new BorderPane();
        Scene s = new Scene(panel, Tools.getScreenSize().get('w'), Tools.getScreenSize().get('h'));
        Tools.addStylesheet(s, "style_game.css");
        stage.setScene(s);

        helpBar = createHelpBar();

        gameSpace = createGameView();

        sideBar = createSideBar();

        panel.setCenter(gameSpace);
        panel.setTop(helpBar);
        panel.setRight(sideBar);
        stage.show();
        stage.setResizable(true);
        stage.setFullScreen(true);

        /* Da es bei zu vielen geöffneten Fenstern schnell unüberischltich oder eventuell zu ungewollten Fehlern kommt, sollen Fenster möglichs
         * effizient gesclossen werden. Der 1. Schritt dafür ist es, alle Fenster zu schließen, sobald das Hauptfenster geöffnet wird.
         * Ein weiteres Verfahren schließt alle doppelten geöffneten Fenster. Dies ist jedoch für jede Fenster-Klasse selbst definiert. */
        stage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {

                /* Da in der Schleife Window.getWindows() eine ObservableList returned, kann man die Fenster
                 * nicht direkt während dem Schleifendurchgang schließen, da es sonst zu einem ArrayOutofBounds Exception kommt.
                 * Die Fenster werden dann in der 2. Schleife entgültig geschlossen. */
                ArrayList<Window> tempWindows = new ArrayList<Window>();
                for (Window window : Window.getWindows()) {
                    if (window.getScene() != stage.getScene()) {
                        tempWindows.add(window);
                    }
                }
                for (Window w : tempWindows) {
                    w.hide();
                }
            }
        });
    }

    /**
     * Lädt das "Standard" Fenster in die Szene.
     * Szene ist hier das Hauptfenster, in dem das meiste Geschehen und die meisten
     * User-Interaktionen stattfinden.
     * @return Vbox, als zentrales Element.
     */
    private static VBox createGameView() {
        VBox box = new VBox();
        box.setId("game");

        GridPane field = new GridPane();
        field.setId("grid-game");

        Button house1, house2, house3, house4;
        house1 = new Button("Kämpfer");
        house2 = new Button("Shop");
        house3 = new Button("Arena");
        house4 = new Button("Sonstiges");

        Tools.addButtonSfx(house1,house2,house3,house4);

        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(house1);
        buttons.add(house2);
        buttons.add(house3);
        buttons.add(house4);

        for(Button button : buttons) {
            Tools.addHoverEffect(button, 100, 1.02, true);
        }

        house1.setId("barracks");
        house2.setId("shop");
        house3.setId("arena");
        house4.setId("other");

        field.add(house1, 0, 0);
        field.add(house2, 0, 1);
        field.add(house3, 1, 0);
        field.add(house4, 1, 1);

        field.setAlignment(Pos.CENTER);
        field.setHgap(200);
        field.setVgap(125);

        for(Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
        }

        house1.setOnAction(e -> {
            barracks();
        });
        house2.setOnAction(e -> {
            shop();
        });
        house3.setOnAction(e -> {
            arena();
        });
        house4.setOnAction(e -> {
            other();
        });

        box.getChildren().addAll(field);
        return box;
    }

    /**
     * Erstellt die Sidebar, die im Großen und Ganzen nur das Tagebuch beinhaltet.
     * @return VBox, als zentrales Element.
     */
    private static VBox createSideBar() {
        VBox box = new VBox();
        box.setPrefWidth(Tools.getScreenSize().get('w') * 0.2);
        box.setId("side-bar");

        Label header = new Label("Tagebuch");
        header.setPadding(new Insets(0,25,0,25));
        box.setAlignment(Pos.CENTER);
        header.setId("diary-header");

        ScrollPane content = new ScrollPane();

        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setId("diary");
        content.setFitToWidth(true);

        VBox textBox = new VBox();
        textBox.setId("diary2");
        System.out.println("Diary created");
        textBox.getChildren().add(diary);
        content.setContent(textBox);
        content.setMinHeight(Tools.getScreenSize().get('h') * 0.83);
        content.setMaxHeight(Tools.getScreenSize().get('h') * 0.83);

        HBox entryChooser = new HBox();
        prevDay = new Button("\t«\t");
        nextDay = new Button("\t»\t");
        prevDay.setId("day-button");
        nextDay.setId("day-button");
        prevDay.setOnAction(e -> {
            diaryIndex--;
            diary.getChildren().clear();
            System.out.println("Diary index: " + diaryIndex);
            for(Text t : Diary.getDiaryEntries().get(diaryIndex-1)) {
                diary.getChildren().add(t);
                t.setStyle("-fx-font-size: 36px;");
            }
            nextDay.setDisable(false);
            if(diaryIndex - 1 <= 0) {
                prevDay.setDisable(true);
            }
        });
        nextDay.setOnAction(e -> {
            diaryIndex++;
            diary.getChildren().clear();
            for(Text t : Diary.getDiaryEntries().get(diaryIndex-1)) {
                diary.getChildren().add(t);
                t.setStyle("-fx-font-size: 36px;");
            }
            prevDay.setDisable(false);
            if(diaryIndex + 1 > getDay()) {
                nextDay.setDisable(true);
            }
        });
        prevDay.setDisable(true);
        nextDay.setDisable(true);
        entryChooser.setAlignment(Pos.CENTER);
        entryChooser.getChildren().addAll(prevDay, nextDay);
        entryChooser.setSpacing(10);
        entryChooser.setPadding(new Insets(10,25,10,25));
        day.addListener((obs, oldDay, newDay) -> {
           prevDay.setDisable(false);
        });

        box.getChildren().addAll(header, content, entryChooser);
        return box;
    }

    /**
     * Enthält die HelpBar, die die Kernwerte wie Gold, Kämpferanzahl, Reputation und vergangene Tage anzeigt.
     * @return VBox, als zentrales Element.
     */
    private static HBox createHelpBar() {
        HBox box = new HBox();
        box.setPrefWidth(Double.MAX_VALUE);
        box.setId("help-bar");

        Label label_pawns = new Label("Bauern:\n" + Inventory.getPawns().size());
        Label label_money = new Label("Goldstücke:\n" + Inventory.getMoney());
        Label label_rep = new Label("Reputation:\n" + Inventory.getReputation());
        Label label_day = new Label("Tag:\n" + day);

        label_pawns.textProperty().bind(Bindings.concat("Pawns:\n", Inventory.getPawnsNum()));
        label_rep.textProperty().bind(Bindings.concat("Reputation:\n", Inventory.getReputation()));
        label_day.textProperty().bind(Bindings.concat("Tag:\n", day.asString()));
        label_money.textProperty().bind((Bindings.concat("Goldstücke:\n", Inventory.getMoneyAsSimpleInt())));

        label_pawns.setId("label-small");
        label_money.setId("label-small");
        label_rep.setId("label-small");
        label_day.setId("label-small");
        box.getChildren().addAll(label_pawns, label_money, label_rep, label_day);
        HBox.setHgrow(label_pawns, Priority.ALWAYS);
        HBox.setHgrow(label_money, Priority.ALWAYS);
        HBox.setHgrow(label_rep, Priority.ALWAYS);
        HBox.setHgrow(label_day, Priority.ALWAYS);
        label_pawns.setMaxWidth(Double.MAX_VALUE);
        label_money.setMaxWidth(Double.MAX_VALUE);
        label_rep.setMaxWidth(Double.MAX_VALUE);
        label_day.setMaxWidth((Double.MAX_VALUE));
        Button nextDay = new Button("Nächster Tag");
        nextDay.setId("next-day");
        nextDay.setAlignment(Pos.CENTER);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0,25,0,8));


        /* Beim Initiieren wird einmalig ein neuer Tag gesetzt (von 0 auf 1). Dies wird so gemacht, damit die
         * Grundwerte (Gold und Kämpfer..), mit denen man das Spiel startet, nicht im Tagebuch log als neue
         * Errungenschaft dargestellt werden. Weitere Absicherungen diesbezüglich sind ebenfalls in Diary.java
         * zu finden.*/
        newDay();
        nextDay.setOnAction(e -> {
            newDay();
        });
        box.getChildren().add(nextDay);

        return box;
    }

    /**
     * Extra Methode für die Action vom Barracks-Button.
     */
    private static  void barracks() {
        Game.drawSpace(Barracks.Barrack_view());
    }

    /**
     * Extra Methode für die Action vom Shop-Button.
     */
    private static void shop() {
        Game.drawSpace(Shop.Shop_view());
    }

    /**
     * Extra Methode für die Action vom Arena-Button.
     */
    private static  void arena() {
        Game.drawSpace(Arena.chooseFighter());
    }

    /**
     * Extra Methode für die Action vom Other-Button.
     */
    private static void other() {
        new QuickMenu();
    }

    /**
     * Lässt das Spiel einen Tag vorranschreiten.
     */
    private static void newDay() {
        /* Tagebuch einrichten */
        diary.getChildren().clear();
        day.set(day.get() + 1);
        Diary.newDay();
        diaryIndex = day.get();

        /* Pawn Kämpfe zurücksetzen */
        for(Pawn p : Inventory.getPawns()) {
            p.setFoughtToday(false);
        }

        /* UI aktualisieren */
        Game.drawSpace();
    }

    /**
     * @return Den aktuellen Tag als Integer.
     */
    public static int getDay() {
        return day.get();
    }

    public static void setDay(int d) {
        day.set(d);
    }

    /**
     * Eine Debug-Methode, die während der Entwicklung verwendet wurde.
     * @deprecated
     */
    private static void Debug() {
        // Inventory.getPawns().get(0).setLevel((byte) 10);
        Inventory.getPawns().get(0).setLvl(10);
        Inventory.getPawns().get(0).addSkillPoints(4);

        Inventory.addItem(new Weapon(Weapon.WeaponClass.AXT, null));
        Inventory.addItem(new Weapon(Weapon.WeaponClass.KTN, null));
        Inventory.addItem(new Armor());
        Inventory.addItem(new Armor());
    }

    /**
     * Zeichnet ein Pane in die zentrale Szene des Spiels.
     * @param pane Pane, welches geladen werden soll.
     */
    public static void drawSpace(Pane pane) {
        Game.gameSpace = pane;
        panel.setCenter(gameSpace);

    }

    /**
     * Lädt das zentrale "Standard"-Fenster mit der Spieleübersicht.
     */
    public static void drawSpace() {
        Game.gameSpace = createGameView();
        panel.setCenter(gameSpace);
        Game.setNextDayButtonDisabled(false);
    }

    /**
     * Wird verwendet, um Tagebucheinträge mittels {@¢ode Diary} zu speichern.
     * @return Den Tagebucheintrag des Tages.
     */
    public static TextFlow getDiary() {
        if(diary == null) {
            diary = new TextFlow();
        }
        return diary;
    }

    /**
     * Erlaubt das deaktivieren des "Nächster Tag"-Buttons, während bestimmten Zeitpunkten des
     * Spiels
     * @param disabled {@code true} = deaktiviert; {@code false} = aktiviert
     */
    public static void setNextDayButtonDisabled(boolean disabled) {
        if(helpBar != null)helpBar.getChildren().get(helpBar.getChildren().size()-1).setDisable(disabled);
    }

    /**
     * Lädt Custom Fonts in das Spiel, damit diese auch verwendet werden können, wenn das
     * Spiel einmal fertig verpackt wird.
     */
    private static void loadFonts() {
        Font f1 = Font.loadFont(Game.class.getResourceAsStream("fonts/DancingScript-Regular.ttf"), 21);
        Font f2 = Font.loadFont(Game.class.getResourceAsStream("fonts/MedievalSharp-Regular.ttf"), 21);
        Font f3 = Font.loadFont(Game.class.getResourceAsStream("fonts/MoonDance-Regular.ttf"), 21);
        Font f4 = Font.loadFont(Game.class.getResourceAsStream("fonts/CinzelDecorative-Regular.ttf"), 21);
        Font f5 = Font.loadFont(Game.class.getResourceAsStream("fonts/CinzelDecorative-Bold.ttf"), 21);

        fonts.put("DancingScript", f1);
        fonts.put("MedievalSharp", f2);
        fonts.put("MoonDance", f3);
        fonts.put("CinzelDecorative", f4);
        fonts.put("CinzelDecorativeBold", f5);

        System.out.println("DancingScript name: " + f1.getName());
        System.out.println("MedievalSharp name: " + f2.getName());
        System.out.println("MoonDance name: " + f3.getName());
        System.out.println("CinzelDecorative name: " + f4.getName());
        System.out.println("CinzelDecorativeBold name: " + f5.getName());


    }

    /**
     * Gibt eine Font zurück, die an einen UI-Text geheftet werden kann.
     */
    public static Font getFont(String name) {
        if(fonts.containsKey(name)) {
            return fonts.get(name);
        }
        return null;
    }

    public static void resetDay() {
        day.set(1);
    }

    public static void refreshDiaryIndex() {
        diaryIndex = day.get();
    }

    public static void close() {
        stage.close();
    }

}
