package de.hsmittweida.pawnstopower;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class Inspector {
    private static Button btnHead;
    private static Button btnTorso;
    private static Button btnArms;
    private static Button btnLegs;
    private static Button btnLeft;
    private static Button btnRight;

    private static Pawn pawn;

    public static Pane Inspector_view(Pawn p) {
        pawn = p;
        AnchorPane background = new AnchorPane();
        background.setId("pane");
        Tools.addStylesheet(background, "style_inspector.css");
        Button mainMenu = new Button("Hauptmenu");
        Button barracks = new Button("Barracken");
        mainMenu.setOnAction(e -> {
            Game.drawSpace();
        });
        barracks.setOnAction(e -> {
            Game.drawSpace(Barracks.Barrack_view());
        });
        AnchorPane.setTopAnchor(mainMenu, 2.0);
        AnchorPane.setLeftAnchor(mainMenu, 2.0);
        AnchorPane.setTopAnchor(barracks, 2.0);
        AnchorPane.setLeftAnchor(barracks, 100.0);


        AnchorPane anchorPane = new AnchorPane();
        background.getChildren().addAll(mainMenu, barracks, anchorPane);
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

        VBox stats = createStats();
        AnchorPane.setLeftAnchor(stats, 7.0);
        AnchorPane.setTopAnchor(stats, 6.0);

        //Slot 0 - siehe Pawn.clothingSlotUsed
        if(p.getArmor((byte)0) != null) {

        }
        AnchorPane.setLeftAnchor(btnHead, 248.0);
        AnchorPane.setRightAnchor(btnHead, 248.0);
        AnchorPane.setTopAnchor(btnHead, 6.0);
        btnHead.setPrefWidth(70);
        btnHead.setPrefHeight(85);
        btnHead.setOnAction(e -> new Slot( btnHead, p, "clothing", 0));

        //Slot 1
        if(p.getArmor((byte)1) != null) {

        }
        AnchorPane.setLeftAnchor(btnTorso, 240.0);
        AnchorPane.setRightAnchor(btnTorso, 240.0);
        AnchorPane.setTopAnchor(btnTorso, 115.0);
        btnTorso.setPrefHeight(250);
        btnTorso.setPrefWidth(50);
        btnTorso.setOnAction(e -> new Slot( btnTorso, p, "clothing", 1));

        //Slot 2
        if(p.getArmor((byte)2) != null) {

        }
        AnchorPane.setLeftAnchor(btnArms, 180.0);
        AnchorPane.setRightAnchor(btnArms, 180.0);
        AnchorPane.setTopAnchor(btnArms, 115.0);
        btnArms.setPrefHeight(250);
        btnArms.setPrefWidth(150);
        btnArms.setOnAction(e -> new Slot(btnArms, p, "clothing", 2));

        //Slot 3
        if(p.getArmor((byte)3) != null) {

        }
        AnchorPane.setRightAnchor(btnLegs, 240.0);
        AnchorPane.setLeftAnchor(btnLegs, 240.0);
        AnchorPane.setTopAnchor(btnLegs, 350.0);
        btnLegs.setPrefHeight(250);
        btnLegs.setPrefWidth(50);
        btnLegs.setOnAction(e -> new Slot(btnLegs, p, "clothing", 3));


        //Slot 0 - siehe Pawn.weaponSlotUsed
        if(p.getWeapon((byte)0) != null) {
            setImage(btnRight, p.getWeapon((byte)0).getWeaponClass());
        }
        AnchorPane.setRightAnchor(btnRight, 7.0);
        AnchorPane.setTopAnchor(btnRight, 195.0);
        btnRight.setPrefWidth(65);
        btnRight.setPrefHeight(200);
        btnRight.setOnAction(e -> new Slot(btnRight, p, "weapon", 0));

        //Slot 1
        if(p.getWeapon((byte)1) != null) {
            setImage(btnLeft, p.getWeapon((byte)1).getWeaponClass());
        }
        AnchorPane.setLeftAnchor(btnLeft, 7.0);
        AnchorPane.setTopAnchor(btnLeft, 195.0);
        btnLeft.setPrefWidth(65);
        btnLeft.setPrefHeight(200);
        btnLeft.setOnAction(e -> new Slot(btnLeft, p, "weapon", 1));


        anchorPane.getChildren().addAll(btnHead, btnArms, btnTorso, btnLegs, btnLeft, btnRight, stats);
        return background;
    }

    public static void setImage(Button ref, Weapon.WeaponClass wc) {
        String picName = switch (wc) {
            case AXT -> "AXT.png";
            case DOL -> "DOL.png";
            case GXT -> "GXT.png";
            case HMR -> "HMR.png";
            case KTN -> "KTN.png";
            case KZG -> "KZG.png";
            case LNS -> "LNS.png";
            case SBL -> "SBL.png";
            case SPR -> "SPR.png";
            case SWT -> "SWT.png";
            case ZWH -> "ZWH.png";
        };
        String location = Inspector.class.getResource("image/weapons/"+picName).toExternalForm();
        ref.setStyle("-fx-background-image: url('" + location + "');");
    }
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
    public static void clearImage(Button ref) {
        ref.setStyle(("-fx-background-image: none"));
    }

    private static VBox createStats() {
        VBox box = new VBox();
        box.setPrefWidth(200.0);

//        box.setStyle("-fx-border-width: 1; -fx-border-color: rgba(15,15,15,75); -fx-border-radius: 4");

        // Name
        HBox name = new HBox();
        Label nameLabel = new Label("Name:");
        Label pawnName = new Label(pawn.getName());
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        name.getChildren().addAll(nameLabel,pawnName);

        // Level
        HBox level = new HBox();
        Label levelLabel = new Label("Level:");
        Label pawnLevel = new Label(pawn.getLevel() + "");
        HBox.setHgrow(levelLabel, Priority.ALWAYS);
        levelLabel.setMaxWidth(Double.MAX_VALUE);
        level.getChildren().addAll(levelLabel, pawnLevel);

        // Erfahrungspunkte
        HBox xp = new HBox();
        ProgressBar pb = new ProgressBar(pawn.xpForLevelup((byte) pawn.getLevel()));
        pb.setProgress( ((double) pawn.getExperience()) / (double) (pawn.xpForLevelup(pawn.getLevel()) ));
        HBox.setHgrow(pb, Priority.ALWAYS);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setPadding(new Insets(10,22,10,0));
        Button openSkill = new Button("+");
        openSkill.setId("skill-button");
        xp.setAlignment(Pos.CENTER);

        openSkill.setOnAction(e -> {
            new SkillInspector(pawn);
        });
        xp.getChildren().addAll(pb, openSkill);

        Button debug = new Button("ayoo");

        //Debug
        /*debug.setOnAction(e -> {
            pawn.addExperience(3);
            System.out.println(pawn.getExperience());
        });*/

        box.getChildren().addAll(name, level, xp/*, debug*/);
        return box;
    }

}
