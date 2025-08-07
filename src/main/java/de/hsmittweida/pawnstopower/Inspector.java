package de.hsmittweida.pawnstopower;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

/**
 * Die Inspektor-Klasse dient dem detailierten Anzeigen eines Kämpfers.
 */
public class Inspector {
    private static Button btnHead;
    private static Button btnTorso;
    private static Button btnArms;
    private static Button btnLegs;
    private static Button btnLeft;
    private static Button btnRight;
    private static Pawn pawn;

    /**
     * Die Anzeige, die einen Kämpfer detailliert anzeigt.
     * @param p Pawn, der inspiziert werden soll.
     * @return Pane, welches dann in {@code Game.drawSpace()} angezeigt wird.
     */
    public static Pane Inspector_view(Pawn p) {
        pawn = p;
        AnchorPane background = new AnchorPane();
        background.setId("pane");
        Tools.addStylesheet(background, "style_inspector.css");

        HBox navButtons = new HBox();
        Button mainMenu = new Button("Hauptmenu");
        Button barracks = new Button("Barracken");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });
        barracks.setOnAction(e -> {
            Game.drawSpace(Barracks.Barrack_view());
        });
        navButtons.getChildren().addAll(mainMenu, barracks);
        navButtons.setSpacing(10.0);
        navButtons.prefWidthProperty().bind(background.widthProperty());
        navButtons.setId("nav-box");

        AnchorPane anchorPane = new AnchorPane();
        background.getChildren().addAll(navButtons, anchorPane);
        anchorPane.setId("anchorPane");
        AnchorPane.setTopAnchor(anchorPane, 30.0);
        AnchorPane.setLeftAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);
        AnchorPane.setBottomAnchor(anchorPane, 0.0);

        btnHead = new Button("Head");
        btnTorso = new Button("Torso");
        btnArms = new Button("Arms");
        btnLegs = new Button("Legs");
        btnLeft = new Button("Left");
        btnRight = new Button("Right");

        btnHead.setId("slot-button");
        btnTorso.setId("slot-button");
        btnArms.setId("slot-button");
        btnLegs.setId("slot-button");
        btnLeft.setId("slot-button");
        btnRight.setId("slot-button");

        VBox stats = createStats();
        AnchorPane.setTopAnchor(stats, 6.0);
        stats.setPadding(new Insets(10,15,10,15));


        //Slot 0 - siehe Pawn.clothingSlotUsed
        if(p.getArmor((byte)0) != null) {

        }
        AnchorPane.setLeftAnchor(btnHead, 710.0);
        AnchorPane.setRightAnchor(btnHead, 710.0);
        AnchorPane.setTopAnchor(btnHead, 190.0);
        btnHead.setPrefWidth(70);
        btnHead.setPrefHeight(90);
        btnHead.setOnAction(e -> new Slot( btnHead, p, "clothing", 0, "Kopfschutz"));

        //Slot 1
        if(p.getArmor((byte)1) != null) {

        }
        AnchorPane.setLeftAnchor(btnTorso, 710.0);
        AnchorPane.setRightAnchor(btnTorso, 710.0);
        AnchorPane.setTopAnchor(btnTorso, 300.0);
        btnTorso.setPrefHeight(235);
        btnTorso.setPrefWidth(50);
        btnTorso.setOnAction(e -> new Slot( btnTorso, p, "clothing", 1, "Torsoschutz"));

        //Slot 2
        if(p.getArmor((byte)2) != null) {

        }
        AnchorPane.setLeftAnchor(btnArms, 655.0);
        AnchorPane.setRightAnchor(btnArms, 655.0);
        AnchorPane.setTopAnchor(btnArms, 300.0);
        btnArms.setPrefHeight(250);
        btnArms.setPrefWidth(150);
        btnArms.setOnAction(e -> new Slot(btnArms, p, "clothing", 2, "Armschutz"));

        //Slot 3
        if(p.getArmor((byte)3) != null) {

        }
        AnchorPane.setRightAnchor(btnLegs, 710.0);
        AnchorPane.setLeftAnchor(btnLegs, 710.0);
        AnchorPane.setTopAnchor(btnLegs, 535.0);
        btnLegs.setPrefHeight(260);
        btnLegs.setPrefWidth(50);
        btnLegs.setOnAction(e -> new Slot(btnLegs, p, "clothing", 3, "Beinschutz"));


        //Slot 0 - siehe Pawn.weaponSlotUsed
        if(p.getWeapon((byte)0) != null) {
            setImage(btnRight, p.getWeapon((byte)0).getWeaponClass());
        }
        AnchorPane.setRightAnchor(btnRight, 7.0);
        AnchorPane.setTopAnchor(btnRight, 195.0);
        btnRight.setPrefWidth(65);
        btnRight.setPrefHeight(200);
        btnRight.setOnAction(e -> new Slot(btnRight, p, "weapon", 0, "Rechte Handwaffe"));

        //Slot 1
        if(p.getWeapon((byte)1) != null) {
            setImage(btnLeft, p.getWeapon((byte)1).getWeaponClass());
        }
        AnchorPane.setLeftAnchor(btnLeft, 7.0);
        AnchorPane.setTopAnchor(btnLeft, 195.0);
        btnLeft.setPrefWidth(65);
        btnLeft.setPrefHeight(200);
        btnLeft.setOnAction(e -> new Slot(btnLeft, p, "weapon", 1, "Linke Handwaffe"));


        anchorPane.getChildren().addAll(btnHead, btnArms, btnTorso, btnLegs, btnLeft, btnRight, stats);
        return background;
    }

    /**
     * Setzt ein Bild auf einen Knopf. Die Knöpfe sind hier die Körperregionen des Kämpfers.
     * Dadurch wird mehr Interaktivität mit dem Kämpfer geboten.
     * @param ref Button, auf den das Bild gelegt werden soll.
     * @param wc Waffenklasse, die als Referenz für das richtige Bild dient.
     */
    public static void setImage(Button ref, Weapon.WeaponClass wc) {
        String picName = switch (wc) {
            case AXT -> "AXT.png";
            case DOL -> "DOL.png";
            case GXT -> "GXT.png";
            case HMR -> "HMR.png";
            case KTN -> "KTN.png";
            case LNS -> "LNS.png";
            case SBL -> "SBL.png";
            case SPR -> "SPR.png";
            case SWT -> "SWT.png";
            case ZWH -> "ZWH.png";
        };
        String location = Inspector.class.getResource("image/weapons/"+picName).toExternalForm();
        ref.setStyle("-fx-background-image: url('" + location + "');");
    }

    /**
     * Setzt ein Bild auf einen Knopf. Die Knöpfe sind hier die Körperregionen des Kämpfers.
     * Dadurch wird mehr Interaktivität mit dem Kämpfer geboten.
     * @param ref Button, auf den das Bild gelegt werden soll.
     * @param ac Rüstungsklasse, die zur Auswahl des richtigen Bildes dient.
     * @param slot Körperregion, an der das Rüstungsstück ausgerüstet werden soll.
     */
    public static void setImage(Button ref, Armor.ArmorClass ac, byte slot) {
        String path = "";
        path = switch (slot) {
            case 0 -> "haube.png";
            case 1 -> "brustplatte.png";
            case 2 -> "beinschutz.png";
            case 3 -> "armschutz.png";
            default -> "";
        };
        String picName = switch(ac) {
            case FAB -> "stoff_" + path;
            case LTH -> "leder_" + path;
            case IRN -> "eisen_" + path;
            case STL -> "stahl_" + path;
        };
    }

    /**
     * Entfernt ein Bild aus einem Knopf, wenn ein Item abgelegt wird.
     * @param ref Button, von dem das Bild entfernt werden soll.
     */
    public static void clearImage(Button ref) {
        ref.setStyle(("-fx-background-image: none"));
    }

    /**
     * Erstellt in dem Inspector ein kleines Übersichtsfenster zu dem ausgewählten Kämpfer.
     * Abzulesen sind Level, Name und ein weitere Button für das SkillMenu.
     * @return VBox, welche oben links im Inspector dargestellt wird.
     */
    private static VBox createStats() {
        VBox box = new VBox();
        box.setId("stats-box");
        box.setPrefWidth(400);
        box.setMaxWidth(500);

        /* Name */
        HBox name = new HBox();
        Label nameLabel = new Label("Name: ");
        Label pawnName = new Label(pawn.getName());
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        name.getChildren().addAll(nameLabel,pawnName);

        /* Level */
        HBox level = new HBox();
        Label levelLabel = new Label("Level:");
        Label pawnLevel = new Label(pawn.getLvl() + "");
        HBox.setHgrow(levelLabel, Priority.ALWAYS);
        levelLabel.setMaxWidth(Double.MAX_VALUE);
        level.getChildren().addAll(levelLabel, pawnLevel);

        /* Efahrungspunkte Anzeige */
        HBox xpbar = new HBox();
        ProgressBar pb = new ProgressBar();
        pb.progressProperty().bind(pawn.getLevelProgress());
        xpbar.getChildren().addAll(pb);
        pb.prefWidthProperty().bind(box.widthProperty());

        /* Erfahrungspunkte */
        HBox xp = new HBox();
        Button openSkill = new Button("» Skills «");
        openSkill.setId("skill-button");
        xp.setAlignment(Pos.CENTER);
        openSkill.setOnAction(e -> {
            new SkillInspector(pawn);
        });
        xp.getChildren().addAll(openSkill);

        box.getChildren().addAll(name, level, xpbar, xp);
        box.setSpacing(15);
        return box;
    }
}
