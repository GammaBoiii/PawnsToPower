package de.hsmittweida.pawnstopower;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class SoundManager {

    private MediaPlayer mp;
    private int index = 0;
    ArrayList<String> playlist;
    SoundManager(String name) {
        //File f = new File(this.getClass().getResource("sound/" + name).getFile());
        String f = this.getClass().getResource("sound/" + name).toExternalForm();

        try {
            mp = createSound(f);
            mp.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    SoundManager(String... name) {
        playlist = new ArrayList<String>();
        for (String s : name) {
            playlist.add(this.getClass().getResource("sound/" + s).toExternalForm());
        }
        //System.out.println(playlist.size());
        playCurrentSong();

    }

    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    /**
     * Hilft beim Abspielen eines Songs aus einer Playlist. Nach jedem Ende eines Tracks wird automatisch der nächste ausgewählt.
     * Ist die Liste einmal durchgespielt, fangen die Tracks wieder von vorne an.
     * Danke hierbei an den Java Garbage-Collector.
     */
    private void playCurrentSong() {
        String currentSong = playlist.get(index);
        //System.out.println("Playing song: " + currentSong.getName());
        mp = createSound(currentSong);
        mp.play();
        mp.setOnEndOfMedia(() -> {
            if(index + 1 >= playlist.size()) {
                index = 0;
                playCurrentSong();
            } else {
                index++;
                playCurrentSong();
            }
        });
    }

    private MediaPlayer createSound(String f) {
        return new MediaPlayer(new Media(f));
    }

}
