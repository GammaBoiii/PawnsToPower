package de.hsmittweida.pawnstopower;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class SoundManager {

    private MediaPlayer mp;
    private int index = 0;
    ArrayList<File> playlist;
    SoundManager(String name) {
        File f = new File(this.getClass().getResource("sound/" + name).getFile());

        try {
            mp = createSound(f);
            mp.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    SoundManager(String... name) {
        playlist = new ArrayList<File>();
        for (String s : name) {
            playlist.add(new File(this.getClass().getResource("sound/" + s).getFile()));
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
        File currentSong = playlist.get(index);
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

    private MediaPlayer createSound(File f) {
        return new MediaPlayer(new Media(f.toURI().toString()));
    }

}
