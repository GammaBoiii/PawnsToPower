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
    static ArrayList<Pawn> pawns;
    static Stage stage;
    int reputation;
    IntegerProperty day;

    Game() {
//        pawns = new ArrayList<Pawn>();
        day = new SimpleIntegerProperty(0);
        Inventory.setup();
        CreateWindow();
        Debug();
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

    public void CreateWindow() {
        System.out.println("NEW GAME BABAY");
        stage = new Stage();
        BorderPane panel = new BorderPane();
        Scene s = new Scene(panel, Tools.getScreenSize().get('w'), Tools.getScreenSize().get('h'));
        //s.getStylesheets().add(getClass().getResource("style_game.css").toExternalForm());
        Tools.addStylesheet(s, "style_game.css");
        stage.setScene(s);

        HBox helpBar = createHelpBar();

        VBox gameSpace = createGameView();

        VBox sideBar = createSideBar();

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

    private VBox createGameView() {
        VBox box = new VBox();
        box.setId("game");

        GridPane field = new GridPane();
        field.setId("grid-game");


        Button house1, house2, house3, house4;
        house1 = new Button("Kämpfer");
        house2 = new Button("Shop");
        house3 = new Button("Arena");
        house4 = new Button("Sonstiges");

//        Image i1 = new Image(this.getClass().getResource("image/barracks.png").toExternalForm());
//        Image i2 = new Image(this.getClass().getResource("image/shop.png").toExternalForm());
//        Image i3 = new Image(this.getClass().getResource("image/arena.png").toExternalForm());
//        Image i4 = new Image(this.getClass().getResource("image/other.png").toExternalForm());
//        house1.setBackground(new Background(new BackgroundImage(i1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
//
////        house1.setBackground(new Background(new BackgroundFill(new ImagePattern(i1,20,20,1,1,true), new CornerRadii(1.0), new Insets(0,0,0,0))));
//        house2.setBackground(new Background(new BackgroundFill(new ImagePattern(i2,0,0,1,1,true), new CornerRadii(1.0), new Insets(0,0,0,0))));
//        house3.setBackground(new Background(new BackgroundFill(new ImagePattern(i3,0,0,1,1,true), new CornerRadii(1.0), new Insets(0,0,0,0))));
//        house4.setBackground(new Background(new BackgroundFill(new ImagePattern(i4,0,0,1,1,true), new CornerRadii(1.0), new Insets(0,0,0,0))));
        /*Tools.setBackground(house1, 15, this.getClass().getResource("image/barracks.png").toExternalForm());
        Tools.setBackground(house2, 15, this.getClass().getResource("image/barracks.png").toExternalForm());
        Tools.setBackground(house3, 15, this.getClass().getResource("image/barracks.png").toExternalForm());
        Tools.setBackground(house4, 15, this.getClass().getResource("image/barracks.png").toExternalForm());*/

        Tools.addHoverEffect(house1, 100, 1.02, true);
        Tools.addHoverEffect(house2, 100, 1.02, true);
        Tools.addHoverEffect(house3, 100, 1.02, true);
        Tools.addHoverEffect(house4, 100, 1.02, true);

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
        house1.setMaxWidth(Double.MAX_VALUE);
        house1.setMaxHeight(Double.MAX_VALUE);
        house2.setMaxWidth(Double.MAX_VALUE);
        house2.setMaxHeight(Double.MAX_VALUE);
        house3.setMaxWidth(Double.MAX_VALUE);
        house3.setMaxHeight(Double.MAX_VALUE);
        house4.setMaxWidth(Double.MAX_VALUE);
        house4.setMaxHeight(Double.MAX_VALUE);

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

    private VBox createSideBar() {
        VBox box = new VBox();
        box.setPrefWidth(400);
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
        text.setWrappingWidth(400);
        content.setContent(text);


        box.getChildren().addAll(header, content);

        return box;
    }

    private HBox createHelpBar() {
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

    private void barracks() {
        System.out.println("Laaager");
        new Barracks();
    }

    private void shop() {
        System.out.println("GEschifft");
        new Shop();
    }

    private void arena() {
        System.out.println("Aräna");
    }

    private void other() {
        System.out.println("Sonstigs");
    }

    private void newDay() {
        day.set(day.get() + 1);
    }

    private void Debug() {
        for (int i = 0; i < 2; i++) {
            Inventory.addPawn(new Pawn());
        }
        Inventory.addMoney(3874);
        Inventory.addItem(new Weapon(Weapon.WeaponClass.AXT, null));
        Inventory.addItem(new Weapon(Weapon.WeaponClass.KTN, null));
        Inventory.addItem(new Armor(Armor.ArmorClass.FAB, "Helmut"));
        Inventory.addItem(new Armor(Armor.ArmorClass.IRN, "gunt"));
    }

}
