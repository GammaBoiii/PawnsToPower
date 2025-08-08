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

public class Tools {
    public static HashMap<Character, Integer> getScreenSize() {
        HashMap<Character, Integer> dimensions = new HashMap<Character, Integer>();
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        /* Hole die Bildschirmgröße */
        int screenWidth = toolkit.getScreenSize().width;
        int screenHeight = toolkit.getScreenSize().height;
        dimensions.put('h', screenHeight);
        dimensions.put('w', screenWidth);
        return dimensions;
    }

    public static void setBackground(Button value, int radius, String path){
        Image i = new Image(path);
        BackgroundFill f = new BackgroundFill(new ImagePattern(i), new CornerRadii(radius), new Insets(0));
        value.setBackground(new Background(f));
    }

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

      /*  stage.getScene().setOnDragDetected(e -> {
            System.out.println(e.toString());
        });
        stage.focusedProperty().addListener((a, b, c) -> {
            if (!c) { // c → boolean, ob das Fenster nun fokussiert ist oder nicht
				stage.close();
            }
        });*/
    }

    public static void popup(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.getDialogPane().getStylesheets().add(Tools.class.getResource("style_dialog.css").toExternalForm());
        alert.show();
    }

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

    public static String inputPopup(String title, String header, String context) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(context);
        dialog.getDialogPane().getStylesheets().add(Tools.class.getResource("style_dialog.css").toExternalForm());

        String res = "";
        try {
            res = dialog.showAndWait().get();
        } catch (NoSuchElementException nsex) {
        }
        return res;

    }

    /**
     * Fügt einer Scene eine .css an
     * @param s Scene, die das Stylesheet erhalten soll
     * @param name Name der .css
     */
    public static void addStylesheet(Scene s, String name) {
        s.getStylesheets().add(Tools.class.getResource(name).toExternalForm());
    }

    public static void addStylesheet(Pane pane, String name) {
        pane.getStylesheets().add(Barracks.class.getResource(name).toExternalForm());
    }

    /**
     * Ersetzt das -fx-transition aus .css
     *
     * @param b Der Button, der animiert werden soll.
     * @param millis Transitionszeit in Millisek.
     * @param fac Der Faktor um wie viel vergrößert wird.
     */
    public static void addHoverEffect(Button b, int millis, double fac, boolean glowEffect) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(millis), b);
        scaleIn.setToX(fac);
        scaleIn.setToY(fac);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(millis), b);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        /* Glow-Effekt für Button */
        DropShadow shadow = new DropShadow(0, new Color(0.871, 0.714, 0.718, 1.0));
        b.setEffect(shadow);

        Timeline glowIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 0)),
                new KeyFrame(Duration.millis(millis), new KeyValue(shadow.radiusProperty(), 20))
        );

        Timeline glowOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 20)),
                new KeyFrame(Duration.millis(millis), new KeyValue(shadow.radiusProperty(), 0))
        );

        /*b.setOnMouseEntered(e -> scaleIn.playFromStart());
        b.setOnMouseExited(e -> scaleOut.playFromStart());*/
        b.setOnMouseEntered(e -> {
            glowIn.playFromStart();
            if (glowEffect) scaleIn.playFromStart();
        });
        b.setOnMouseExited(e -> {
            glowOut.playFromStart();
            if (glowEffect) scaleOut.playFromStart();
        });
    }

    public static void addButtonSfx(Button... buttons) {
        for(Button b : buttons) {

            /* Hier addEventHandler, da setOnAction() bereits bei den Buttons benutzt wird,
             * und es daher zu überschreibungen kommen würde */

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

    public static void pauseGame(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while break");
        }
    }

    public static void getMouseClickedPosOnNode(Node node) {
        node.setOnMouseClicked(event -> {
            System.out.println(event.getX() + " " + event.getY());
        });
    }
}
