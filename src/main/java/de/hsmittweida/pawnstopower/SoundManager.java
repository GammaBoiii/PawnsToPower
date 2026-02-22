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
    /**
     * Playlist, die mehrere Lieder beinhaltet, die alle hintereinander automatisch abgespielt werden
     * sollen.
     *
     * @see SoundManager#playCurrentSong()
     */
    ArrayList<String> playlist;
    /**
     * Der MediaPlayer, der den Sound wiedergibt.
     */
    private MediaPlayer mp;
    /**
     * Index eines Songs aus einer Playlist, der gerade abgespielt wird
     *
     * @see SoundManager#playCurrentSong()
     */
    private int index = 0;

    /**
     * Registriert einen Sound aus einem Namen und spielt diesen ab.
     * Gut nutzbar für einzelne, kurze Sounds/Soundeffekte.
     *
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
     *
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
     * Gibt den MediaPlayer zurück, der von einem Sound verwendet wird.
     *
     * @return {@code MediaPlayer}
     */
    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    /**
     * Hilft beim Abspielen eines Songs aus einer Playlist. Nach jedem Ende eines Tracks wird automatisch der nächste ausgewählt.
     * Ist die Liste einmal durchgespielt, fangen die Tracks wieder von vorne an.
     * Danke hierbei auch an den Java Garbage-Collector, der nach jedem Song automatisch aufräumt.
     */
    private void playCurrentSong() {
        String currentSong = playlist.get(index);
        //System.out.println("Playing song: " + currentSong.getName());
        mp = createSound(currentSong);
        mp.play();
        mp.setOnEndOfMedia(() -> {
            if (index + 1 >= playlist.size()) {
                index = 0;
                playCurrentSong();
            } else {
                index++;
                playCurrentSong();
            }
        });
    }

    /**
     * Erstellt den eigentlichen MediaPlayer für eine Sounddatei. <br>
     * Gibt den MediaPlayer für einen Sound zurück.
     *
     * @param f Pfad zur Datei.
     * @return {@code MediaPlayer}
     */
    private MediaPlayer createSound(String f) {
        return new MediaPlayer(new Media(f));
    }

}
