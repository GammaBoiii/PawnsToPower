package de.hsmittweida.pawnstopower;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuickMenu {

    public QuickMenu() {
        Stage stage = new Stage();
        VBox box = new VBox();
        Scene s = new Scene(box, 500,350);

        Button save = new Button("Save");
        save.setOnAction(e -> {
            Save.saveAll("/home/johann/Documents/p2p/newsavetypelol.p2p");
        });
        Button load = new Button("Load");
        load.setOnAction(e -> {
            Save.loadAll("/home/johann/Documents/p2p/newsavetypelol.p2p");
        });
        Button cheat = new Button("Cheat");
        cheat.setOnAction(e -> {
           Inventory.addMoney(424);
           Inventory.addPawn(new Pawn());
           Inventory.addItem(new Armor());
            Inventory.addItem(new Armor());
            Inventory.addItem(new Armor());
            Inventory.addItem(new Armor());
            Inventory.addItem(new Armor());
        });
        box.getChildren().addAll(save, load, cheat);


        Tools.defaultClose(stage, "quickmenu" );
        stage.setScene(s);
        stage.show();
    }
}
