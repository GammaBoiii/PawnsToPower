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