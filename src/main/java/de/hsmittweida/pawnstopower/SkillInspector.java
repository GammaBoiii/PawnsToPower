package de.hsmittweida.pawnstopower;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.print.attribute.standard.PDLOverrideSupported;

public class SkillInspector {
    public SkillInspector(Pawn pawn) {
        Stage stage = new Stage();
        VBox box = new VBox();
        Scene s = new Scene(box,400.0, 250.0);

        /*HBox health = new HBox();
        health.setPadding(new Insets(0,0,20,0));
        skillLabel = new Label("Leben");
        increase = new Button("+");
        increase.setOnAction(e -> {
            System.out.println("healthe");
        });
        health.getChildren().addAll(skillLabel, increase);

        HBox damage = new HBox();
        damage.setPadding(new Insets(0,0,20,0));
        skillLabel = new Label("Schaden");
        increase = new Button("+");
        increase.setOnAction(e -> {
            System.out.println("damage");
        });
        damage.getChildren().addAll(skillLabel,increase);

        HBox resistance = new HBox();
        resistance.setPadding(new Insets(0,0,20,0));
        skillLabel = new Label("Widerstand");
        increase = new Button("+");
        increase.setOnAction(e -> {
            System.out.println("resist");
        });
        resistance.getChildren().addAll(skillLabel,increase);

        HBox speed = new HBox();
        speed.setPadding(new Insets(0,0,20,0));
        skillLabel = new Label("Agilität");
        increase = new Button("+");
        increase.setOnAction(e -> {
            System.out.println("speed");
        });
        speed.getChildren().addAll(skillLabel,increase);

        box.getChildren().addAll(health, damage, resistance, speed);*/

        for(Skill skill : pawn.getSkills()) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            Label label = new Label(skill.getName());

            Label level = new Label("Level: " + skill.getSkillLevel());
            HBox.setHgrow(label, Priority.ALWAYS);
            label.setMaxWidth(Double.MAX_VALUE);

            Button increase = new Button("+");
            HBox.setMargin(increase, new Insets(0,0,0,7));
            increase.setOnAction(e -> {
                System.out.println(skill.getId());
            });
            hbox.getChildren().addAll(label, level, increase);
            box.getChildren().add(hbox);
        }

        Tools.defaultClose(stage, "skillinspector");
        stage.setScene(s);
        stage.setTitle("Skills");
        stage.show();
    }
}
