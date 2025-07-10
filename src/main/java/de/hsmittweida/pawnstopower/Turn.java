package de.hsmittweida.pawnstopower;

import javafx.application.Platform;

/**
 * Diese Klasse dient der Ausführung eines Zuges in der Arena
 */
public class Turn extends Thread {
    private Pawn pawn;
    private final Turn thread;

    public Turn(Pawn p) {
        this.pawn = p;
        thread = this;
        thread.start();
    }

    /**
     * Jeder Turn wird in run() in maximal 3 Phasen aufgeteilt:
     * - Zug begonnen
     * - Angriff oder Verteidigung; Bei Angriff wird dem Gegner schaden hinzugefügt, bei Verteidigung erhöht sich die
     * Verteidigung des Pawns der gerade am Zug ist für die nächste Runde.
     * - Zug abschließen
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            /* Zug für den Gegner */
            if (!pawn.ownedByPlayer()) {
                /* Zug begonnen */
                Arena.disableActionButtons(true);
                Arena.log("Der Gegner ist nun am Zug.");
                waitFor(2500);

                /* Angriff/Verteidigung */
                /* Der Gegner geht zu 70% in Angriff und zu 30% in Verteidigung über */
                double rnd = Math.random();
                System.out.println("Random angriff oder defense: " + rnd);
                if(rnd > 0.3) {
                    /* Angriff */
                    pawn.goInDefenseMode(false);
                    String[] msg = Arena.getEnemyAttackMessage();
                    Arena.log(msg[0], "-fx-font-style: italic;");
                    waitFor(1500);
                    Arena.log(msg[1], "-fx-font-style: italic;");
                    waitFor(1500);
                    Arena.log(msg[2], "-fx-font-style: italic; -fx-fill: red;");
                    Arena.log("");
                    waitFor(2000);
                    int damage = pawn.calcDamage(Arena.getOther(pawn));

                    /* Durch das runLater() in Arena.damage werden in dem Arena-Log immer zuerst die Leben ausgegeben, und dann erst aktualisiert.
                     * Daher werden in der Ausgabe in der folgenden Zeile die Leben direkt mit abgezogen, und der eigentliche Damage
                     * (backend) kann erst danach ausgeführt werden. */
                    Arena.log("Du verlierst " + damage + " Lebenspunkte. Dir verbleiben noch " + (int) (Arena.getLife(Arena.getOther(pawn)) -damage));
                    Arena.damage(Arena.getOther(pawn), damage);
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
                /* Zug begonnen */
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
                        int damage = pawn.calcDamage(Arena.getOther(pawn));
                        Arena.damage(Arena.getOther(pawn), damage);
                        Arena.log("Du hast " + damage + " Schaden beim Gegner verursacht.");
                        waitFor(1500);
                        break;
                    case "defense":

                        break;

                    default:
                        break;
                }
            }

            /*
            waitFor(250);
            String in;
            try {
                 in = Arena.getBlockingQeue().take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("aaaa");
            System.out.println(in);
*/

            pawn = Arena.getOther(pawn);
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " wurde beendet.");
    }

    public void kill() {
        thread.interrupt();
    }

    /**
     * Dient zum schnellen "warten" im Thread, da ansonsten jedesmal in run() die Exception mit berücksichtigt werden müsste,
     * oder der try-catch-Block die ganze Methode ausfüllen würde, was nicht gut aussieht.
     *
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
}
