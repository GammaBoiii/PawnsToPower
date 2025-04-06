package de.hsmittweida.pawnstopower;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundManager {
    SoundManager(String name) {
        String path = this.getClass().getResource("sound/MainMenuTheme.mp3").toExternalForm();
        File f = new File("/home/johann/IdeaProjects/PawnsToPower/target/classes/de/hsmittweida/pawnstopower/sound/MainMenuTheme.mp3");

            try {
                Media media = new Media(f.toURI().toString());
                MediaPlayer mp = new MediaPlayer(media);
                mp.play();

            } catch (Exception e) {
                e.printStackTrace();
            }



    }
}
