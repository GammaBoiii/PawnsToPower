package de.hsmittweida.pawnstopower;

import javafx.application.Platform;

/**
 * Diese Klasse dient der Ausführung eines Zuges in der Arena
 */
public class Turn extends Thread {
    private Pawn pawn;
    private final Turn thread;

    /**
     * Konstruktor der Turn Klasse.
     * Startet einen neuen thread.
     * @param p
     */
    public Turn(Pawn p) {
        this.pawn = p;
        thread = this;
        thread.start();
    }

    /**
     * Die Eigentliche Laufumgebung des Threads.
     * Beinhaltet die Logik für Angriff und Verteidigung.
     * Jeweilige Fallunterscheidung für Spieler- und Gegner-Züge
     */
    @Override
    public void run() {
        System.out.println("Thread1 " + Thread.currentThread().getName() + " wurde gestartet.");
        while (!Thread.currentThread().isInterrupted()) {
            /* Zug für den Gegner */
            if (!pawn.ownedByPlayer()) {
                Arena.disableActionButtons(true);
                Arena.log("Der Gegner ist nun am Zug.");
                waitFor(2500);

                /* Angriff/Verteidigung */
                /* Der Gegner geht zu 70% in Angriff und zu 30% in Verteidigung über */
                double rnd = Math.random();
                if(rnd > 0.3) {
                    /* Angriff */
                    int[] damageArr = pawn.calcDamage(Arena.getOther(pawn));
                    int damage = damageArr[0];
                    String[] msg = Arena.getEnemyAttackMessage(damageArr[1]);
                    Arena.log(msg[0], "-fx-font-style: italic;");
                    waitFor(1500);
                    Arena.log(msg[1], "-fx-font-style: italic;");
                    waitFor(2000);
                    Arena.log(msg[2], "-fx-font-style: italic; -fx-fill: red;");
                    Arena.log("");
                    new SoundManager(getRandomAtkSound());

                    /* Durch das runLater() in Arena.damage werden in dem Arena-Log immer zuerst die Leben ausgegeben, und dann erst aktualisiert.
                     * Daher werden in der Ausgabe in der folgenden Zeile die Leben direkt mit abgezogen, und der eigentliche Damage
                     * (backend) kann erst danach ausgeführt werden. */
                    Arena.log("Du verlierst " + damage + " Lebenspunkte. Dir verbleiben noch " + (int) (Arena.getLife(Arena.getOther(pawn)) -damage));
                    Arena.damage(Arena.getOther(pawn), damage);
                    pawn.goInDefenseMode(false);
                } else {
                    /* Verteidigung */
                    pawn.goInDefenseMode(true);
                    Arena.log(Arena.getEnemyDefenseMessage(), "-fx-font-style: italic;");
                    waitFor(1500);
                    Arena.log("");
                    waitFor(3500);
                }
            }
            /* Zug für Spieler selbst */
            else {
                Arena.log("Du bist am Zug");
                Arena.disableActionButtons(false);
                String in = "";
                try {
                    in = Arena.getBlockingQeue().take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                switch (in) {
                    case "attack":
                        int damage = pawn.calcDamage(Arena.getOther(pawn))[0];
                        Arena.damage(Arena.getOther(pawn), damage);
                        new SoundManager(getRandomAtkSound());
                        Arena.log("Du hast " + damage + " Schaden beim Gegner verursacht.\n");
                        waitFor(1500);
                        pawn.goInDefenseMode(false);
                        break;
                    case "defense":
                        pawn.goInDefenseMode(true);
                        Arena.log("Du stellst dich defensiv auf.\n");
                        break;

                    default:
                        break;
                }
            }
            pawn = Arena.getOther(pawn);
        }
        //System.out.println("Thread1 " + Thread.currentThread().getName() + " wurde beendet.");
    }

    /**
     * Beendet den Turn-Thread.
     */
    public void kill() {
        System.out.println("Thread2 " + thread.getName() + " wurde beendet.");
        thread.interrupt();
    }

    /**
     * Dient zum schnellen "warten" im Thread, da ansonsten jedesmal in run() die Exception mit berücksichtigt werden müsste,
     * oder der try-catch-Block die ganze Methode ausfüllen würde, was nicht gut aussieht.
     * @param ms Milisekunden, die gewartet werden
     * @throws InterruptedException InterruptedException
     */
    private void waitFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Gibt den Thread als Objekt zurück
     * @return {@code Thread}
     */
    public Thread getThread() {
        return this.thread;
    }

    /**
     * Spielt während dem Angreifen einen zurälligen Angriffsound ab.
     * Für ein mehr belebtes Gameplay.
     * @return {@code String} mit dem Namen der Sounddatei.
     */
    private String getRandomAtkSound() {
        String[] loc = {"sfx/atk1.wav","sfx/atk2.wav","sfx/atk3.wav","sfx/atk4.wav"};
        return loc[(int)(Math.random()*loc.length)];
    }
}
