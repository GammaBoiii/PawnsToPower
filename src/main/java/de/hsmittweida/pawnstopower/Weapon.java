package de.hsmittweida.pawnstopower;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Random;

/**
 * Weapon Klasse, die die Logik für Waffen bereitstellt.
 *
 * @see Item#getItemType()
 */
public class Weapon extends Item {
    /**
     * Waffenklasse
     */
    private final WeaponClass wc;
    /**
     * Modifier für die Waffe
     */
    private final ArrayList<Double> damageModifier;
    /**
     * Ist Waffe Zweihänder?
     * {@code true}, wenn Waffe Zweihänder ist.
     */
    private boolean twoHanded;

    /**
     * Erstellt ein neues Weapon Objekt mit vorgegebener Weapon-Class
     *
     * @param cls Die vorgegebene Weapon-Class
     * @param p   Der Besitzer der Waffe. Häufig {@code null}
     */
    Weapon(WeaponClass cls, Pawn p) {
        super(p);
        damageModifier = new ArrayList<Double>();
        wc = cls;
        super.setName();
    }

    /**
     * Erstellt ein neues, komplett zufälliges Weapon Objekt.
     *
     * @param p Pawn, der direkt als Besitzer gesetzt werden soll.
     */
    Weapon(Pawn p) {
        super(p);
        damageModifier = new ArrayList<Double>();
        wc = getRandomWeaponClass();
        super.setName();
    }

    /**
     * Erstellt ein neues, komplett zufälliges Weapon Objekt. Ohne Besitzer.
     */
    Weapon() {
        super(null);
        damageModifier = new ArrayList<Double>();
        wc = getRandomWeaponClass();
        super.setName();
    }

    /**
     * Prüft, ob Waff eine Zweihänder ist, und entsprechend zwei Waffenplätze belegt
     *
     * @return {@code true}, wenn Waffe ein Zweihänder ist.
     */
    public boolean isTwoHanded() {
        return wc.isTwoHanded();
    }

    /**
     * Gibt die Klasse/Art der Waffe zurück.
     *
     * @return {@code WeaponClass}
     */
    public WeaponClass getWeaponClass() {
        return this.wc;
    }

    /**
     * Gibt die Damage-Modifier der Waffe zurück.
     *
     * @return {@code ArrayList<Double>}
     */
    private ArrayList<Double> getDamageModifier() {
        return this.damageModifier;
    }

    /**
     * Fügt der Waffe einen beliebigen Damage-Modifier hinzu. Dieser kann die Waffe stärken
     * oder schwächen. Alle Modifier werden in einer Liste gespeichert und zusammengezählt.
     *
     * @param modifier Der Modifier, der hinzugefügt werden soll.
     */
    public void addDamageModifier(double modifier) {
        this.damageModifier.add(modifier);
    }

    /**
     * Entfernt einen gegebenen Modifier. Sollte es zufälligerweise mehrmals denselben Modifier geben,
     * wird der erste in der Liste entfernt.
     *
     * @param modifier Der Modifier, der entfernt werden soll.
     */
    public void removeDamageModifier(double modifier) {
        int index = 0;
        for (double d : damageModifier) {
            if (d == modifier) {
                damageModifier.remove(index);
            }
            index++;
        }
    }

    /**
     * Berechnet den gesamten Schaden einer Waffe, inkl. der Modifier, und gibt diesen zurück.
     *
     * @return {@code double}
     */
    public double getTotalDamage() {
        double damage = wc.baseDamage;
        for (Double d : getDamageModifier()) {
            damage *= d;
        }
        return damage;
    }

    /**
     * Gibt eine zufällige Waffenklase zurück
     *
     * @return {@code WeaponClass}
     */
    private WeaponClass getRandomWeaponClass() {
        Random rnd = new Random();
        return WeaponClass.values()[rnd.nextInt(WeaponClass.values().length)];
    }

    /**
     * Gibt den Item Typ zurück.
     *
     * @return {@code String} ~ "Weapon"
     */
    public String getItemType() {
        return "Weapon";
    }

    /**
     * Beinhaltet die Klasse der Waffe.
     * <small><br><br>Wenn hier etwas geänderd wird, dann auch in {@link Inspector#setImage(StackPane, WeaponClass)} nachtragen!</small>
     */
    public enum WeaponClass {
        /**
         * Schwert-Klasse
         */
        SWT(80, 8.0),
        /**
         * Katana-Klasse
         */
        KTN(110, 11.0),
        /**
         * Langschwert-Klasse
         */
        LNS(120, 12.0),
        /**
         * Säbel-Klasse
         */
        SBL(90, 9.0),
        /**
         * Dolch-Klasse
         */
        DOL(35, 3.5),
        /**
         * Speer-Klasse
         */
        SPR(95, 9.5),
        /**
         * Hammer-Klasse
         */
        HMR(130, 13.0),
        /**
         * Axt-Klasse
         */
        AXT(100, 10.0);

        /**
         * Basispreis der Waffe
         */
        public int basePrice;
        /**
         * Basisschaden der Waffe
         */
        public double baseDamage;

        /**
         * Initiiert die Klasse.
         *
         * @param basePrice  Standardpreis der Waffe, für Shop und Verkauf.
         * @param baseDamage Standardschaden der Waffe.
         */
        WeaponClass(int basePrice, double baseDamage) {
            this.basePrice = basePrice;
            this.baseDamage = baseDamage;
        }

        /**
         * Prüft. ob die Waffe ein Zweihänder ist.
         *
         * @return {@code true}, wenn die Waffe ein Zweihänder ist.
         */
        public boolean isTwoHanded() {
            return this == LNS || this == KTN;
        }
    }
}
