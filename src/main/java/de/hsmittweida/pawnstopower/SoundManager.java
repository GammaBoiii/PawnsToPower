package de.hsmittweida.pawnstopower;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundManager {
    SoundManager(String name) {
        String path = this.getClass().getResource("sound/MainMenuTheme.mp3").toExternalForm();
        File f = new File("/home/johann/IdeaProjects/PawnsToPower/target/classes/de/hsmittweida/pawnstopower/sound/MainMenuTheme.mp3");
        System.out.println(f);
        System.out.println("-----------");

//        Platform.runLater(() -> {
            try {
                System.out.print("playsound");
                Media media = new Media(f.toURI().toString());
//            System.out.print(media.getSource());
                MediaPlayer mp = new MediaPlayer(media);
                mp.play();

            } catch (Exception e) {
                System.out.println("EXCEPTION");
                e.printStackTrace();
            }

//        });


    }
}
