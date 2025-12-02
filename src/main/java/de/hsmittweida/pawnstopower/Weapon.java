package de.hsmittweida.pawnstopower;

import java.util.ArrayList;
import java.util.Random;
/**
 * Weapon Klasse, die die Logik für Waffen bereit stellt.
 * @see Item#getItemType()
 */
public class Weapon extends Item {
    private final WeaponClass wc;
    private final ArrayList<Double> damageModifier;
    private boolean twoHanded;

    /**
     * Erstellt ein neues Weapon Objekt mit vorgegebener Weapon-Class
     * @param cls Die vorgegebene Weapon-Class
     * @param p Der Besitzer der Waffe. Häufig {@code null}
     */
    Weapon(WeaponClass cls, Pawn p) {
        super(p);
        damageModifier = new ArrayList<Double>();
        wc = cls;
        super.setName();
    }

    /**
     * Erstellt ein neues, komplett zufälliges Weapon Objekt.
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
     * @return {@code true}, wenn Waffe ein Zweihänder ist und somit zwei Waffenplätze belegt.
     */
    public boolean isTwoHanded() {
        return wc.isTwoHanded();
    }

    /**
     * @return {@code WeaponClass}, also die Art der Waffe.
     */
    public WeaponClass getWeaponClass() {
        return this.wc;
    }

    /**
     * @return Liste der Damage-Modifiers.
     */
    private ArrayList<Double> getDamageModifier() {
        return this.damageModifier;
    }

    /**
     * Fügt der Waffe einen beliebigen Damage-Modifier hinzu. Dieser kann die Waffe stärken
     * oder schwächen. Alle Modifier werden in einer Liste gespeichert und zusammengezählt.
     * @param modifier Der Modifier, der hinzugefügt werden soll.
     */
    public void addDamageModifier(double modifier) {
        this.damageModifier.add(modifier);
    }

    /**
     * Entfernt einen gegebenen Modifier. Sollte es zufälligerweise mehrmals denselben Modifier geben,
     * wird der erste in der Liste entfernt.
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
     * Berechnet den gesamten Schaden einer Waffe, inkl. der Modifier.
     * @return Totalen Schaden der Waffe.
     */
    public double getTotalDamage() {
        double damage = wc.baseDamage;
        for (Double d : getDamageModifier()) {
            damage *= d;
        }
        return damage;
    }

    /**
     * Beinhaltet die Klasse der Waffe.
     * <small><br><br>Wenn hier etwas geänderd wird, dann auch in {@code Inspector.setImage()} nachtragen!
     */
    public enum WeaponClass {
        SWT(80, 8.0), //Schwert
        KTN(110, 11.0), //Katana
        LNS(120, 12.0), //Langschwert
        SBL(90, 9.0), //Säbel
        DOL(35, 3.5), //Dolch
        SPR(95, 9.5), //Speer
        HMR(130, 13.0), //Hammer
        AXT(100, 10.0);//Axt

        public int basePrice;
        public double baseDamage;
        public boolean isTwoHanded() {
            return this == LNS || this == KTN;
        }

        /**
         * Initiiert die Klasse.
         * @param basePrice Standardpreis der Waffe, für Shop und Verkauf.
         * @param baseDamage Standardschaden der Waffe.
         */
        WeaponClass(int basePrice, double baseDamage) {
            this.basePrice = basePrice;
            this.baseDamage = baseDamage;
        }
    }

    /**
     * @return Eine zufällige Waffenklasse.
     */
    private WeaponClass getRandomWeaponClass() {
        Random rnd = new Random();
        return WeaponClass.values()[rnd.nextInt(WeaponClass.values().length)];
    }

    /**
     * @return Den Item-Typ <strong><em>Weapon.
     */
    public String getItemType() {
        return "Weapon";
    }
}
