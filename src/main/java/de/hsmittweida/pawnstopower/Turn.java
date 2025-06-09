package de.hsmittweida.pawnstopower;

import javafx.application.Platform;

/**
 * Diese Klasse dient der Ausführung eines Zuges in der Arena
 */
public class Turn extends Thread {
    private final Pawn pawn;
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
        /* Zug begonnen */
        if (!pawn.ownedByPlayer()) {
            Arena.log("Der Gegner ist nun am Zug.");
            waitFor(1500);
            Arena.log("\b a");
            waitFor(150);
            Arena.log("\b a");
            waitFor(150);
            Arena.log("\b a");
            waitFor(150);
            Arena.log("\b a");
        } else {
            waitFor(150);
            Arena.log("\b x");
            waitFor(150);
            Arena.log("\b x");
            waitFor(150);
            Arena.log("\b x");
            waitFor(150);
            Arena.log("\b x");

        }
    }

    public void kill() {
        thread.kill();
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
            throw new RuntimeException(e);
        }
    }
}
