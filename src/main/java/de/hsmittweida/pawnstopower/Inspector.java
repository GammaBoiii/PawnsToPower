package de.hsmittweida.pawnstopower;

import javafx.application.Platform;
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
 * Der Spieler erhält eine Übersicht über Ausrüstung, Name, Level, Erfahrungspunkte und Skills.
 */
public class Inspector {
    /**
     * Referenzknopf für Kopfausrüstung
     */
    private static Button btnHead;
    /**
     * Referenzknopf für Torsoausrüstung
     */
    private static Button btnTorso;
    /**
     * Referenzknopf für Armausrüstung
     */
    private static Button btnArms;
    /**
     * Referenzknopf für Beinausrüstung
     */
    private static Button btnLegs;
    /**
     * Referenzknopf für den Linken Arm (Waffen)
     */
    private static Button btnLeft;
    /**
     * Referenzknopf für den Rechten Arm (Waffen)
     */
    private static Button btnRight;
    /**
     * Pane für den Kopf
     */
    private static StackPane headPane;
    /**
     * Pane für den Torso
     */
    private static StackPane torsoPane;
    /**
     * Pane für die Arme
     */
    private static StackPane armPane;
    /**
     * Pane für die Beine
     */
    private static StackPane legPane;
    /**
     * Pane für den rechten Arm
     */
    private static StackPane paneRight;
    /**
     * Pane für den linken Arm
     */
    private static StackPane paneLeft;
    /**
     * Referenzvariable für den {@code Pawn}, der gerade inspiziert wird.
     */
    private static Pawn pawn;

    /**
     * Die Anzeige, die einen Kämpfer detailliert anzeigt.
     * Gibt das Pane zurück, welches dann in {@code Game.drawSpace()} angezeigt wird.
     * @param p Pawn, der inspiziert werden soll.
     * @return {@code Pane}
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
        Tools.getMouseClickedPosOnNode(anchorPane);
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


        /* Slot 0 - siehe Pawn.clothingSlotUsed */
        headPane = new StackPane();
        AnchorPane.setLeftAnchor(headPane, 710.0);
        AnchorPane.setRightAnchor(headPane, 710.0);
        AnchorPane.setTopAnchor(headPane, 190.0);
        AnchorPane.setLeftAnchor(btnHead, 710.0);
        AnchorPane.setRightAnchor(btnHead, 710.0);
        AnchorPane.setTopAnchor(btnHead, 190.0);
        headPane.setPrefWidth(90);
        headPane.setPrefHeight(90);
        headPane.setMaxWidth(90);
        headPane.setMaxHeight(90);
        headPane.setMinWidth(90);
        headPane.setMinHeight(90);
        btnHead.setPrefWidth(90);
        btnHead.setPrefHeight(90);
        btnHead.setMaxWidth(90);
        btnHead.setMaxHeight(90);
        btnHead.setMinWidth(90);
        btnHead.setMinHeight(90);
        btnHead.setOnAction(e -> new Slot(headPane, p, "clothing", 0, "Kopfschutz"));


        /* Slot 1 */
        torsoPane = new StackPane();
        AnchorPane.setLeftAnchor(torsoPane, 690.0);
        AnchorPane.setRightAnchor(torsoPane, 690.0);
        AnchorPane.setTopAnchor(torsoPane, 300.0);
        AnchorPane.setLeftAnchor(btnTorso, 690.0);
        AnchorPane.setRightAnchor(btnTorso, 690.0);
        AnchorPane.setTopAnchor(btnTorso, 300.0);
        torsoPane.setPrefHeight(235);
        torsoPane.setPrefWidth(50);
        torsoPane.setMaxHeight(235);
        torsoPane.setMaxWidth(50);
        torsoPane.setMinHeight(235);
        torsoPane.setMinWidth(50);
        btnTorso.setPrefHeight(235);
        btnTorso.setPrefWidth(50);
        btnTorso.setMaxHeight(235);
        btnTorso.setMaxWidth(50);
        btnTorso.setMinHeight(235);
        btnTorso.setMinWidth(50);
        btnTorso.setOnAction(e -> new Slot(torsoPane, p, "clothing", 1, "Torsoschutz"));
        torsoPane.setId("torso-pane");


        /* Slot 2 */
        armPane = new StackPane();
        AnchorPane.setLeftAnchor(armPane, 655.0);
        AnchorPane.setRightAnchor(armPane, 655.0);
        AnchorPane.setTopAnchor(armPane, 310.0);
        AnchorPane.setLeftAnchor(btnArms, 655.0);
        AnchorPane.setRightAnchor(btnArms, 655.0);
        AnchorPane.setTopAnchor(btnArms, 310.0);
        armPane.setPrefHeight(250);
        armPane.setPrefWidth(150);
        armPane.setMaxHeight(250);
        armPane.setMaxWidth(150);
        armPane.setMinHeight(250);
        armPane.setMinWidth(150);
        btnArms.setPrefHeight(250);
        btnArms.setPrefWidth(150);
        btnArms.setMaxHeight(250);
        btnArms.setMaxWidth(150);
        btnArms.setMinHeight(250);
        btnArms.setMinWidth(150);
        btnArms.setOnAction(e -> new Slot(armPane, p, "clothing", 2, "Armschutz"));


        /* Slot 3 */
        legPane = new StackPane();
        AnchorPane.setRightAnchor(legPane, 710.0);
        AnchorPane.setLeftAnchor(legPane, 710.0);
        AnchorPane.setTopAnchor(legPane, 535.0);
        AnchorPane.setRightAnchor(btnLegs, 710.0);
        AnchorPane.setLeftAnchor(btnLegs, 710.0);
        AnchorPane.setTopAnchor(btnLegs, 535.0);
        legPane.setPrefHeight(260);
        legPane.setPrefWidth(50);
        legPane.setMaxHeight(260);
        legPane.setMaxWidth(50);
        legPane.setMinHeight(260);
        legPane.setMinWidth(50);
        btnLegs.setPrefHeight(260);
        btnLegs.setPrefWidth(50);
        btnLegs.setMaxHeight(260);
        btnLegs.setMaxWidth(50);
        btnLegs.setMinHeight(260);
        btnLegs.setMinWidth(50);
        btnLegs.setOnAction(e -> new Slot(legPane, p, "clothing", 3, "Beinschutz"));


        /* Slot 0 - siehe Pawn.weaponSlotUsed */
        paneRight = new StackPane();
        AnchorPane.setRightAnchor(paneRight, 456.0);
        AnchorPane.setTopAnchor(paneRight, 370.0);
        paneRight.setPrefWidth(175);
        paneRight.setPrefHeight(175);
        paneRight.setMaxWidth(175);
        paneRight.setMaxHeight(175);
        paneRight.setMinWidth(175);
        paneRight.setMinHeight(175);
        if(p.getWeapon((byte)0) != null) {
            System.out.println("weapon in rigght hand");
            setImage(paneRight, p.getWeapon((byte)0).getWeaponClass());
        }
        AnchorPane.setRightAnchor(btnRight, 456.0);
        AnchorPane.setTopAnchor(btnRight, 370.0);
        btnRight.setPrefWidth(175);
        btnRight.setPrefHeight(175);
        btnRight.setOnAction(e -> new Slot(paneRight, p, "weapon", 0, "Rechte Handwaffe"));

        /* Slot 1 */
        paneLeft = new StackPane();
        AnchorPane.setLeftAnchor(paneLeft, 456.0);
        AnchorPane.setTopAnchor(paneLeft, 370.0);
        paneLeft.setPrefWidth(175);
        paneLeft.setPrefHeight(175);
        paneLeft.setMaxWidth(175);
        paneLeft.setMaxHeight(175);
        paneLeft.setMinWidth(175);
        paneLeft.setMinHeight(175);
        if(p.getWeapon((byte)1) != null) {
            System.out.println("weapon in left thand");
            setImage(paneLeft, p.getWeapon((byte)1).getWeaponClass());
        }
        AnchorPane.setLeftAnchor(btnLeft, 456.0);
        AnchorPane.setTopAnchor(btnLeft, 370.0);
        btnLeft.setPrefWidth(175);
        btnLeft.setPrefHeight(175);
        btnLeft.setOnAction(e -> new Slot(paneLeft, p, "weapon", 1, "Linke Handwaffe"));

        Button debug = new Button("Fix img");
        AnchorPane.setBottomAnchor(debug, 10.0);
        debug.setOnAction(e -> {refreshImages();});
        anchorPane.getChildren().addAll(headPane, armPane, legPane, torsoPane, paneRight, paneLeft, btnHead, btnArms, btnTorso, btnLegs, btnLeft, btnRight, stats, debug);
        Inspector.refreshImages();
        Platform.runLater(() -> {
            refreshImages();
        });
        return background;
    }

    /**
     * Setzt ein Bild auf ein Stackpane. Die StackPanes sind hier die Körperregionen des Kämpfers.
     * Dadurch wird mehr Interaktivität mit dem Kämpfer geboten.
     * @param ref StackPane, auf den das Bild gelegt werden soll.
     * @param wc Waffenklasse, die als Referenz für das richtige Bild dient.
     */
    public static void setImage(StackPane ref, Weapon.WeaponClass wc) {
        String picName = switch (wc) {
            case AXT -> "AXT.png";
            case DOL -> "DOL.png";
            case HMR -> "HMR.png";
            case KTN -> "KTN.png";
            case LNS -> "LNS.png";
            case SBL -> "SBL.png";
            case SPR -> "SPR.png";
            case SWT -> "SWT.png";
        };
        String location = Inspector.class.getResource("image/weapons/"+picName).toExternalForm();
        Image img = new Image(location);
        ImageView iv = new ImageView(img);

        iv.fitWidthProperty().bind(ref.widthProperty());
        iv.fitHeightProperty().bind(ref.heightProperty());

        ref.getChildren().add(iv);
    }

    /**
     * Setzt ein Bild auf einen StackPane. Die StackPanes sind hier die Körperregionen des Kämpfers.
     * Dadurch wird mehr Interaktivität mit dem Kämpfer geboten.
     * @param ref StackPane, auf den das Bild gelegt werden soll.
     * @param ac Rüstungsklasse, die zur Auswahl des richtigen Bildes dient.
     * @param slot Körperregion, an der das Rüstungsstück ausgerüstet werden soll.
     */
    public static void setImage(StackPane ref, Armor.ArmorClass ac, byte slot) {
        String path = "";
        path = switch (slot) {
            case 0 -> "helm.png";
            case 1 -> "brustplatte.png";
            case 2 -> "armschutz.png";
            case 3 -> "beinschutz.png";
            default -> "";
        };
        String picName = switch(ac) {
            case FAB -> "stoff_" + path;
            case LTH -> "leder_" + path;
            case IRN -> "eisen_" + path;
            case STL -> "stahl_" + path;
        };

        String location = Inspector.class.getResource("image/clothing/"+picName).toExternalForm();
        Image img = new Image(location);
        ImageView iv = new ImageView(img);

        iv.fitWidthProperty().bind(ref.widthProperty());
        iv.fitHeightProperty().bind(ref.heightProperty());

        /* Je nach Rüstungsslot müssen die Bilder entsprechend skaliert werden: */
        if (path.equals("helm.png")) {
            iv.setScaleY(1.125);
        } else if (path.equals("beinschutz.png")) {
            iv.setScaleX(1.3);
            iv.setScaleY(1.166);
        } else if (path.equals("armschutz.png")) {
            iv.setScaleX(1.15);
            iv.setScaleY(1.15);
        } else if (path.equals("brustplatte.png")) {
            iv.setScaleX(1.325);
            iv.setScaleY(1.05);
        }

        ref.getChildren().add(iv);
    }

    /**
     * Entfernt ein Bild aus einem StackPane, wenn ein Item abgelegt wird.
     * @param ref Button, von dem das Bild entfernt werden soll.
     */
    public static void clearImage(StackPane ref) {
        ref.setStyle(("-fx-background-image: none"));
        ref.getChildren().clear();
    }

    /**
     * Entfernt alle Bilder von jedem Slot
     */
    public static void clearAllImages() {
        headPane.getChildren().clear();
        torsoPane.getChildren().clear();
        legPane.getChildren().clear();
        armPane.getChildren().clear();
        paneRight.getChildren().clear();
        paneLeft.getChildren().clear();
    }

    /**
     * Erstellt in dem Inspector ein kleines Übersichtsfenster zu dem ausgewählten Kämpfer.
     * Abzulesen sind Level, Name und ein weitere Button für das SkillMenu.
     * Gibt eine VBox zurück, welche oben Links im Inspector dargestellt wird.
     * @return {@code VBox} mit den Stats
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

        /* ID */
        HBox id = new HBox();
        Label idLabel = new Label("ID:" + pawn.getId());
        id.getChildren().add(idLabel);
        id.setAlignment(Pos.CENTER);

        box.getChildren().addAll(name, level, xpbar, xp, id);
        box.setSpacing(15);
        return box;
    }

    /**
     * Aktualisiert die Bilder bei den Ausrüstungsknöpfen, sodass die ausgerüsteten Items
     * richtig dargestellt werden.
     */
    public static void refreshImages() {
        clearAllImages();
        if (pawn != null) {
            if(pawn.getWeapon((byte)0) == null) {
                clearImage(paneRight);
            } else {
                setImage(paneRight, pawn.getWeapon((byte) 0).getWeaponClass());
            }
            if(pawn.getWeapon((byte)1) == null) {
                clearImage(paneLeft);
            } else {
                setImage(paneLeft, pawn.getWeapon((byte) 1).getWeaponClass());
            }
            if(pawn.getArmor((byte)0) == null) {
                clearImage(headPane);
            } else {
                setImage(headPane, pawn.getArmor((byte) 0).getArmorClass(), (byte) 0);
            }
            if(pawn.getArmor((byte)1) == null) {
                clearImage(torsoPane);
            } else {
                setImage(torsoPane, pawn.getArmor((byte) 1).getArmorClass(), (byte) 1);
            }
            if(pawn.getArmor((byte)2) == null) {
                clearImage(armPane);
            } else {
                setImage(armPane, pawn.getArmor((byte) 2).getArmorClass(), (byte) 2);
            }
            if(pawn.getArmor((byte)3) == null) {
                clearImage(legPane);
            } else {
                setImage(legPane, pawn.getArmor((byte) 3).getArmorClass(), (byte) 3);
            }
        }
    }
}
