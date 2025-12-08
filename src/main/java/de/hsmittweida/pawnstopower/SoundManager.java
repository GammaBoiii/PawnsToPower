package de.hsmittweida.pawnstopower;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

/**
 * Diese Klasse ist das Herzstück der Soundverwaltung.
 * Hier werden die Sounds registriert und verwaltet.
 */
public class SoundManager {

    private MediaPlayer mp;
    private int index = 0;
    ArrayList<String> playlist;

    /**
     * Registriert einen Sound aus einem Namen und spielt diesen ab.
     * Gut nutzbar für einzelne, kurze Sounds.
     * @param name Name der Sounddatei.
     */
    SoundManager(String name) {
        String f = this.getClass().getResource("sound/" + name).toExternalForm();

        try {
            mp = createSound(f);
            mp.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Registriert mehrere Sounds aus einem Array und spielt diese ab.
     * Gut nutzbar für Hintergrundmusik, da hier wie aus einer Playlist alles
     * abgespielt wird.
     * @param name Varaible Menge an Namen der Sounddateien.
     */
    SoundManager(String... name) {
        playlist = new ArrayList<String>();
        for (String s : name) {
            playlist.add(this.getClass().getResource("sound/" + s).toExternalForm());
        }
        playCurrentSong();

    }

    /**
     * @return {@coode MediaPlayer} MediaPlayer, der von einem Sound verwendet wird.
     */
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

    /**
     * Erstellt den eigentlichen MediaPlayer für eine Sounddatei.
     * @param f Pfad zur Datei.
     * @return {@code MediaPlayer} MediaPlayer, der für eine Datei erstellt wurde.
     */
    private MediaPlayer createSound(String f) {
        return new MediaPlayer(new Media(f));
    }

}
