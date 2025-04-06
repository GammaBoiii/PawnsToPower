package de.hsmittweida.pawnstopower;

import java.util.ArrayList;
import java.util.Random;

public class Weapon extends Item {
    /**
     * Schwert, Zweihander, Katana, Kurzklinge, Langschwert, Säbel, Dolch, Speer, Hammer, Axt, Groß-Axt
     */
//    String weaponClass;
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
    }

    /**
     * Erstellt ein neues, komplett zufälliges Weapon Objekt
     * @param p
     */
    Weapon(Pawn p) {
        super(p);
        damageModifier = new ArrayList<Double>();
        wc = getRandomWeaponClass();
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public WeaponClass getWClass() {
        return this.wc;
    }

    private ArrayList<Double> getDamageModifier() {
        return this.damageModifier;
    }

    public void addDamageModifier(double modifier) {
        this.damageModifier.add(modifier);
    }

    public void removeDamageModifier(double modifier) {
        int index = 0;
        for (double d : damageModifier) {
            if (d == modifier) {
                damageModifier.remove(index);
            }
            index++;
        }
    }

    public double getTotalDamage() {
        double damage = wc.baseDamage;
        for (Double d : getDamageModifier()) {
            damage *= d;
        }
        return damage;
    }

    /**
     * Beinhaltet die Klasse der Waffe. Wenn hier etwas geänderd wird, dann auch in {@code Inspector.setImage} nachtragen!
     */
    public enum WeaponClass {
        SWT(80, 8.0), //Schwert
        ZWH(120, 12.0), //Zweihänder
        KTN(110, 11.0), //Katana
        KZG(50, 5.0), //Kurzklinge
        LNS(120, 12.0), //Langschwert
        SBL(90, 9.0), //Säbel
        DOL(35, 3.5), //Dolch
        SPR(95, 9.5), //Speer
        HMR(130, 13.0), //Hammer
        AXT(100, 10.0),//Axt
        GXT(125, 12.5);//Groß-Axt

        public int basePrice;
        public double baseDamage;

        WeaponClass(int basePrice, double baseDamage) {
            this.basePrice = basePrice;
            this.baseDamage = baseDamage;
        }
    }

    private WeaponClass getRandomWeaponClass() {
        Random rnd = new Random();
        return WeaponClass.values()[rnd.nextInt(WeaponClass.values().length)];
    }

    public String getItemType() {
        return "Weapon";
    }
}
