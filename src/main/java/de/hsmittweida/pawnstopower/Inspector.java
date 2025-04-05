package de.hsmittweida.pawnstopower;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Inspector {
    Button btnHead;
    Button btnTorso;
    Button btnArms;
    Button btnLegs;
    Button btnLeft;
    Button btnRight;

    Pawn pawn;

    Inspector(Pawn p) {
        pawn = p;
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        pane.setId("panel");
        Scene s = new Scene(pane, 584, 618);
        Tools.addStylesheet(s, "style_inspector.css");

        btnHead = new Button("Head");
        btnTorso = new Button("Torso");
        btnArms = new Button("Arms");
        btnLegs = new Button("Legs");
        btnLeft = new Button("Left");
        btnRight = new Button("Right");

        //Slot 0 - siehe Pawn.clothingSlotUsed
        if(p.getArmor((byte)0) != null) {

        }
        AnchorPane.setLeftAnchor(btnHead, 248.0);
        AnchorPane.setRightAnchor(btnHead, 248.0);
        AnchorPane.setTopAnchor(btnHead, 6.0);
        btnHead.setPrefWidth(70);
        btnHead.setPrefHeight(85);
        btnHead.setOnAction(e -> new Slot(this, btnHead, p, "clothing", 0));

        //Slot 1
        if(p.getArmor((byte)1) != null) {

        }
        AnchorPane.setLeftAnchor(btnTorso, 240.0);
        AnchorPane.setRightAnchor(btnTorso, 240.0);
        AnchorPane.setTopAnchor(btnTorso, 115.0);
        btnTorso.setPrefHeight(250);
        btnTorso.setPrefWidth(50);
        btnTorso.setOnAction(e -> new Slot(this, btnTorso, p, "clothing", 1));

        //Slot 2
        if(p.getArmor((byte)2) != null) {

        }
        AnchorPane.setLeftAnchor(btnArms, 180.0);
        AnchorPane.setRightAnchor(btnArms, 180.0);
        AnchorPane.setTopAnchor(btnArms, 115.0);
        btnArms.setPrefHeight(250);
        btnArms.setPrefWidth(150);
        btnArms.setOnAction(e -> new Slot(this, btnArms, p, "clothing", 2));

        //Slot 3
        if(p.getArmor((byte)3) != null) {

        }
        AnchorPane.setRightAnchor(btnLegs, 240.0);
        AnchorPane.setLeftAnchor(btnLegs, 240.0);
        AnchorPane.setTopAnchor(btnLegs, 350.0);
        btnLegs.setPrefHeight(250);
        btnLegs.setPrefWidth(50);
        btnLegs.setOnAction(e -> new Slot(this, btnLegs, p, "clothing", 3));


        //Slot 0 - siehe Pawn.weaponSlotUsed
        if(p.getWeapon((byte)0) != null) {
            setImage(btnRight, p.getWeapon((byte)0).getWClass());
        }
        AnchorPane.setRightAnchor(btnRight, 7.0);
        AnchorPane.setTopAnchor(btnRight, 195.0);
        btnRight.setPrefWidth(65);
        btnRight.setPrefHeight(200);
        btnRight.setOnAction(e -> new Slot(this, btnRight, p, "weapon", 0));

        //Slot 1
        if(p.getWeapon((byte)1) != null) {
            setImage(btnLeft, p.getWeapon((byte)1).getWClass());
        }
        AnchorPane.setLeftAnchor(btnLeft, 7.0);
        AnchorPane.setTopAnchor(btnLeft, 195.0);
        btnLeft.setPrefWidth(65);
        btnLeft.setPrefHeight(200);
        btnLeft.setOnAction(e -> new Slot(this, btnLeft, p, "weapon", 1));


        pane.getChildren().addAll(btnHead, btnArms, btnTorso, btnLegs, btnLeft, btnRight);

        stage.setScene(s);
        stage.show();
        Tools.defaultClose(stage, "inspector");
    }

    public void setImage(Button ref, Weapon.WeaponClass wc) {
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
        String location = getClass().getResource("image/weapons/"+picName).toExternalForm();
        ref.setStyle("-fx-background-image: url('" + location + "');");
    }
    public void setImage(Button ref, Armor.ArmorClass ac, byte slot) {
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
    public void clearImage(Button ref) {
        ref.setStyle(("-fx-background-image: none"));
    }
}
