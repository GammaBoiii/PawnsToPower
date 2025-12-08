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

/**
 * Der Skillinspector dient dem Anzeigen der verfügbaren Skills von einem Pawn.
 * Der Spieler hat hier die Möglichkeit, die durch aufleveln verdienten Skillpunkte,
 * in die verschiedenen Skills zu investieren, um den Kämpfer in der Arena
 * stärler zu machen.
 */
public class SkillInspector {

    /**
     * Konstruktor der SkillInspector Klasse.
     *
     * @param pawn Pawn, dessen Skills angezeigt werden sollen.
     */
    public SkillInspector(Pawn pawn) {
        Stage stage = new Stage();
        VBox box = new VBox();
        box.setId("skillinspectorbox");
        Scene s = new Scene(box, 400.0, 250.0);
        Tools.addStylesheet(s, "style_default.css");

        //Label skillPointLabel = new Label("Skillpunkte verfügbar: " + pawn.getSkillPoints());

        for (Skill skill : pawn.getSkills()) {
            System.out.println(skill.getName() + " " + skill.getSkillValue());
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            Label label = new Label(skill.getName());
            Label level = new Label("Level: " + skill.getSkillLevel());
            HBox.setHgrow(label, Priority.ALWAYS);
            label.setMaxWidth(Double.MAX_VALUE);

            Button increase = new Button("+");
            HBox.setMargin(increase, new Insets(10, 0, 0, 7));
            increase.setOnAction(e -> {
                if (pawn.getSkillPoints() > 0) {
                    skill.addSkillLevel();
                    pawn.addSkillPoints(-1);
                    stage.setTitle("Skillunkte: " + pawn.getSkillPoints());
                    level.setText("Level: " + skill.getSkillLevel());
                }
                System.out.println(skill.getId());
            });
            hbox.getChildren().addAll(label, level, increase);
            box.getChildren().add(hbox);
        }

        Tools.defaultClose(stage, "skillinspector");
        stage.setScene(s);
        stage.setTitle("Skillunkte: " + pawn.getSkillPoints());
        stage.show();
    }
}