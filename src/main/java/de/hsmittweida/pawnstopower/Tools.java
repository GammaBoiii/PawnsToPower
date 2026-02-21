package de.hsmittweida.pawnstopower;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Die Tool-Klasse bietet eine große Sammlung an Methoden, die in dem Projekt
 * mehrfach von verschiedensten Objekten verwendet werden. Daher eignete es
 * sich, diese Methode in dieser Zentralen klasse, statisch bereitzustellen.
 */
public class Tools {

    /**
     * Gibt die Auflösung des Bildschirms zurück.
     * <br>
     * {@code HashMap<Character, Integer>} enthält:
     * <ul>
     *     <li> 'w' ~ width </li>
     *     <li> 'h' ~ height </li>
     * </ul>
     * @return {@code HashMap<Character, Integer>}
     */
    public static HashMap<Character, Integer> getScreenSize() {
        HashMap<Character, Integer> dimensions = new HashMap<Character, Integer>();

        /* Hole die Bildschirmgröße */
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = toolkit.getScreenSize().width;
        int screenHeight = toolkit.getScreenSize().height;
        dimensions.put('h', screenHeight);
        dimensions.put('w', screenWidth);
        return dimensions;
    }

    /**
     * Setzt ein Hintergrund auf einen Button
     * @param value Buttonelement, auf das das Hintergrundbild angewendet werden soll.
     * @param radius radius des Bildes.
     * @param path Pfad zum Bild.
     * @deprecated
     */
    public static void setBackground(Button value, int radius, String path){
        Image i = new Image(path);
        BackgroundFill f = new BackgroundFill(new ImagePattern(i), new CornerRadii(radius), new Insets(0));
        value.setBackground(new Background(f));
    }

    /**
     * Schließt Nebenfenster automatisch, sobald das Hauptfenster im Vordergrund ist.
     * Verhindert somit, dass viele Fenster offen sind, und somit auch, dass 2 Fenster
     * vom selben Objekt gleichzeitig offen sind.
     * @param stage stage des Fensters, das geprüft und ggf. geschlossen werden soll.
     * @param ownerClass Eine einzigartige Bezeichnung für das Fenster, um das Öffnen
     *                   von mehreren Fenstern des selben Objekts zu verhindern.
     */
    public static void defaultClose(Stage stage, String ownerClass) {
        stage.setResizable(false);

        /* Sorgt dafür, dass überflüssige Fenster geschlossen werden */
        stage.getProperties().put("owner", ownerClass);
        stage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                ArrayList<Window> tempWindows = new ArrayList<Window>();
                for (Window window : Window.getWindows()) {
                    if (window.getScene() != stage.getScene() && window.getProperties().get("owner") == stage.getProperties().get("owner")) {
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
     * Erstelle ein PopUp Fenster.
     * Dient nur zur Informationsanzeige. Erwartet kein User-Input.
     * @param title Titel des Popups
     * @param header Kopfzeile
     * @param context Kontext
     */
    public static void popup(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.getDialogPane().getStylesheets().add(Tools.class.getResource("style_dialog.css").toExternalForm());
        alert.show();
    }

    /**
     * Erstellt ein PopUp-Fenster, das nach einem einfach "Ja/Nein"
     * als Input fragt, und das entsprechende Ergebnis zurückgibt.
     * @param title Titel des Popups
     * @param header Kopfzeile
     * @param context Kontext
     * @return {@code String} mit der gewählten Option.
     */
    public static String confirmPopup(String title, String header, String context) {
        ButtonType yes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "test", yes, no);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.getDialogPane().getStylesheets().add(Tools.class.getResource("style_dialog.css").toExternalForm());

        Optional<ButtonType> res = alert.showAndWait();
        if (res.get() == yes) {
            return "yes";
        } else if (res.get() == no) {
            return "no";
        }
        return "no";
    }

    /**
     * Erstellt ein PopUp-Fenster, das den User nach einem TextInput fragt,
     * und diesen anschließend zurückgibt.
     * @param title Titel des Popups
     * @param header Kopfzeile
     * @param context Kontext
     * @return {@code String} mit den eingegebenen Text, oder <i>"empty"</i>, falls
     * kein Input erfolgte.
     */
    public static String inputPopup(String title, String header, String context) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(context);
        dialog.getDialogPane().getStylesheets().add(Tools.class.getResource("style_dialog.css").toExternalForm());

        Optional<String> r = dialog.showAndWait();
        System.out.println(r.toString());
        if(r.isEmpty()) return "empty";
        else return r.get();


    }

    /**
     * Fügt einer Scene eine .css an
     * @param s Scene, die das Stylesheet erhalten soll
     * @param name Name der .css
     */
    public static void addStylesheet(Scene s, String name) {
        s.getStylesheets().add(Tools.class.getResource(name).toExternalForm());
    }

    /**
     * Fügt einem Pane eine .css an.
     * @param pane Pane, das das Stylesheet erhalten soll.
     * @param name Name der .css.
     */
    public static void addStylesheet(Pane pane, String name) {
        pane.getStylesheets().add(Tools.class.getResource(name).toExternalForm());
    }

    /**
     * Ersetzt das -fx-transition aus .css
     * @param b Der Button, der animiert werden soll.
     * @param millis Transitionszeit in Millisek.
     * @param fac Der Faktor um wie viel vergrößert wird.
     */
    public static void addHoverEffect(Button b, int millis, double fac, boolean glowEffect) {
        /* Definiert zunächst zwei ScaleTransitions, jeweils zur Vergrößerung
        * und zur Verkleinerung des Buttons.
        * */
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(millis), b);
        scaleIn.setToX(fac);
        scaleIn.setToY(fac);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(millis), b);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        /* Glow-Effekt mittel Dropshadow für Button */
        DropShadow shadow = new DropShadow(0, new Color(0.871, 0.714, 0.718, 1.0));
        b.setEffect(shadow);

        /* TimeLine, um da Glow-Animation des Buttons zu steuern.
        * Zwei TimeLines für das Erscheinen und Verschiwnden der Glow-Animation des Buttons.
        * */
        Timeline glowIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 0)),
                new KeyFrame(Duration.millis(millis), new KeyValue(shadow.radiusProperty(), 20))
        );

        Timeline glowOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 20)),
                new KeyFrame(Duration.millis(millis), new KeyValue(shadow.radiusProperty(), 0))
        );

        /* Spielt die entsprechende Animation jeweils beim Eintritt
        * und Austritt der Maus ab.
        * */
        b.setOnMouseEntered(e -> {
            glowIn.playFromStart();
            if (glowEffect) scaleIn.playFromStart();
        });
        b.setOnMouseExited(e -> {
            glowOut.playFromStart();
            if (glowEffect) scaleOut.playFromStart();
        });
    }

    /**
     * Fügt einer Liste von Buttons den SoundEffekt hinzu.
     * @param buttons Buttons, welche den Soundeffekt bekommen sollen.
     */
    public static void addButtonSfx(Button... buttons) {
        for(Button b : buttons) {

            /* Hier addEventHandler, da setOnAction() bereits bei den Buttons benutzt wird,
             * und es daher zu Überschreibungen kommen würde */
            b.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
//                System.out.println("drag entered");
                SoundManager hoversound = new SoundManager("sfx/hover.wav");
            });

            b.addEventHandler(ActionEvent.ACTION, event -> {
//                System.out.println("action event");
                SoundManager clicksound = new SoundManager("sfx/click.wav");
            });
        }
    }

    /**
     * Pausiert den Thread des Spiels.
     * @param time in ms, die der Haupt-Thread des Spiels pausiert werden soll.
     * @deprecated, aus offensichtlichen Gründen... (blockiert das ganze Spiel,
     * bleibt hier nur als Museums-Stück stehen)
     */
    public static void pauseGame(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while break");
        }
    }

    /**
     * Diente zum Debuggen und zum Finden bestimmter Koordinaten auf einem Node.
     * Vereinfachte die Positionierung von UI-Elementen mit Platzierung durch Koordinaten.
     * @param node Node, von dem die Koordinaten ausgelesen werden sollen.
     * @deprecated, da das Programm soweit abgabefertig ist.
     */
    public static void getMouseClickedPosOnNode(Node node) {
        node.setOnMouseClicked(event -> {
            System.out.println(event.getX() + " " + event.getY());
        });
    }
}
