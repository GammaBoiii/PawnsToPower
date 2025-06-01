package de.hsmittweida.pawnstopower;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;

public class Game {
    private static ArrayList<Pawn> pawns;
    private static Stage stage;
    private static int reputation;
    private static IntegerProperty day;

    private static Pane gameSpace;
    private static BorderPane panel;
    private static HBox helpBar;
    private static VBox sideBar;


    Game() {


    }

    public static void Game_view() {
        //        pawns = new ArrayList<Pawn>();
        day = new SimpleIntegerProperty(0);
        Inventory.setup();
        CreateWindow();
        Debug();

        /* Musik im Spiel */
        SoundManager mainmenutheme = new SoundManager("GameDefaultMusic_1.mp3", "GameDefaultMusic_2.mp3", "GameDefaultMusic_3.mp3", "GameDefaultMusic_4.mp3");
        stage.setOnHiding(e -> {
            mainmenutheme.getMediaPlayer().stop();
        });
    }

/*    public static String requestInput(String title, String header, String context) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(context);
        dialog.getDialogPane().getStylesheets().add(Game.class.getResource("style_dialog.css").toExternalForm());
        String res = "";
        try {
            res = dialog.showAndWait().get();
        } catch (NoSuchElementException e) {
        }
        return res;
//		return dialog.getSelectedItem();
    }*/

    public static Stage getGameStage() {
        return stage;
    }

    public static void CreateWindow() {
        stage = new Stage();
        panel = new BorderPane();
        Scene s = new Scene(panel, Tools.getScreenSize().get('w'), Tools.getScreenSize().get('h'));
        //s.getStylesheets().add(getClass().getResource("style_game.css").toExternalForm());
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

    private static VBox createGameView() {
        VBox box = new VBox();
//        box.setStyle("-fx-border-color: red; -fx-border-style: solid; -fx-border-width: 2;"); //debug
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
//        field.setPadding(new Insets(15, 15, 15, 15));

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

    private static VBox createSideBar() {
        VBox box = new VBox();
        box.setPrefWidth(Tools.getScreenSize().get('w') * 0.2);
        //VBox.setMargin(box, new Insets(0,0,0,25));
        box.setId("side-bar");

        Label header = new Label("Tagebuch");
        header.setId("diary-header");

        ScrollPane content = new ScrollPane();
        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setId("diary");
        content.setFitToWidth(true);
        content.setFitToHeight(true);
        content.setMaxHeight(Double.MAX_VALUE);

        /*
        Label contentText = new Label();
        contentText.setWrapText(true);
        contentText.setText("Hallo Welt, das hier \nist nich schön. Es passieren zu viele schlimme Dinge. Man kann sich nicht gutes tun. \n Aber, dass ist die Reise ins ungewisse, \nnirvana, whatever. hier unten blühen blumen so schön gelb und rot. wohin damit? das fragt man sich im Nachhinaein immer wohin damit\n");
         */
        Text text = new Text();
        text.setText("Hallo Welt, das hier ist nich schön. Es passieren zu viele schlimme Dinge. Man kann sich nicht gutes tun. Aber, dass ist die Reise ins ungewisse, nirvana, whatever. hier unten blühen blumen so schön gelb und rot. wohin damit? das fragt man sich im Nachhinaein immer wohin damit");
        text.wrappingWidthProperty().bind(content.widthProperty());
        content.setContent(text);


        box.getChildren().addAll(header, content);

        return box;
    }

    private static HBox createHelpBar() {
        HBox box = new HBox();
        box.setPrefWidth(Double.MAX_VALUE);
        box.setId("help-bar");
        /*
        ComboBox<String> help = new ComboBox<String>();
        help.setId("help-bar-box");
        help.setValue("Menu");
        help.getItems().addAll("Schließen");
        help.setOnAction(e -> {
            if (help.getValue().equals("Schließen")) {
                System.exit(0);
            }
        });
        box.getChildren().add(help);
        */
        Label label_pawns = new Label("Bauern:\n" + Inventory.getPawns().size());
        Label label_money = new Label("Goldstücke:\n" + Inventory.getMoney());
        Label label_rep = new Label("Reputation:\n" + reputation);
        Label label_day = new Label("Tag:\n" + day);
        label_day.textProperty().bind(Bindings.concat("Tag:\n", day.asString()));
        label_money.textProperty().bind((Bindings.concat("Goldstücke:\n", Inventory.getMoneyAsSimpleInt())));
        /*
        label_pawns.getStyleClass().add("small-label");
        label_money.getStyleClass().add("small-label");
        label_rep.getStyleClass().add("small-label");
         */
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
        nextDay.setOnAction(e -> {
            newDay();
        });
        box.getChildren().add(nextDay);

        return box;
    }

    private static  void barracks() {
        Game.drawSpace(Barracks.Barrack_view());
    }

    private static void shop() {
        Game.drawSpace(Shop.Shop_view());
        // new Shop();
    }

    private static  void arena() {
        Game.drawSpace(Arena.chooseFighter());
    }

    private static void other() {

    }

    private static void newDay() {
        day.set(day.get() + 1);
    }

    private static void Debug() {
        for (int i = 0; i < 15; i++) {
            Inventory.addPawn(new Pawn());
        }
        // Inventory.getPawns().get(0).setLevel((byte) 10);
        Inventory.getPawns().get(0).setLvl(10);
        Inventory.addMoney(3874);
        Inventory.addItem(new Weapon(Weapon.WeaponClass.AXT, null));
        Inventory.addItem(new Weapon(Weapon.WeaponClass.KTN, null));
        Inventory.addItem(new Armor());
        Inventory.addItem(new Armor());
    }

    public static void drawSpace(Pane pane) {
        Game.gameSpace = pane;
        panel.setCenter(gameSpace);

    }

    public static void drawSpace() {
        Game.gameSpace = createGameView();
        panel.setCenter(gameSpace);
        System.out.println("ayooooooo");
    }


}
