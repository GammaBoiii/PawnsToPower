package de.hsmittweida.pawnstopower;

import java.util.Random;

public class Armor extends Item{
    private final ArmorClass ac;
    private int baseProtection;
    private boolean equipped;
    private Pawn owner;

    Armor(ArmorClass cls, Pawn p) {
        super(p);
        this.ac = cls;
    }

    Armor(Pawn p) {
        super(p);
        ac = getRandomArmorClass();
    }

    Armor() {
        super(null);
        ac = getRandomArmorClass();
    }

    public ArmorClass getArmorClass() {
        return ac;
    }

    public int getBaseProtection() {
        return baseProtection;
    }

    public enum ArmorClass {
        FAB, //Stoff
        LTH, //Leder
        IRN, //Eisen
        STL, //Stahl
    }

    public String getItemType() {
        return "Armor";
    }

    private ArmorClass getRandomArmorClass() {
        Random rnd = new Random();
        return ArmorClass.values()[rnd.nextInt(ArmorClass.values().length)];
    }
}
