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
        boolean mayRun = true;
        while (mayRun) {
            /* Zug begonnen */
            if (!pawn.ownedByPlayer()) {
                Arena.log("Der Gegner ist nun am Zug.");
                Arena.disableActionButtons(true);
            } else {
                Arena.log("Du bist am Zug");
                Arena.disableActionButtons(false);
            }
            waitFor(1500);
            Arena.log("Der Gegner trägt..");
            waitFor(500);
            for(Armor a : pawn.getAllArmor()) {
                if(a != null) {
                    Arena.log("\t-" + a.getName());
                }
            }
            waitFor(250);
            Arena.log("Der Gegner führt die Waffe: " + pawn.getWeapon((byte) 0).getName() + " vom Typ " + pawn.getWeapon((byte) 0).getWeaponClass());

            /* Angriff/Verteidigung */

            /* Zug abschließen */
        }
    }

    public void kill() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
