package de.hsmittweida.pawnstopower;

/**
 * Die RunMain-Klasse dient zum eigentlichen Starten des Programms.
 * [[ Leider kann ich nicht genau sagen warum, aber mit den von mir benutzten Umgebungen (Intellij als IDE + Shadow zum
 * Erstellen der .jar mit allen Ressourcen)
 * funktioniert es nicht, die Main selber direkt zu starten (vermutlich da dort JavaFX geladen wird). Daher
 * existiert diese seperate RunMain, um das Programm ohne Fehler zu starten. ]]
 */
public class RunMain {
    /**
     * Startet die eigentliche Main-Klasse, mit JavaFX.
     * @param args Arguments
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
