package de.hsmittweida.pawnstopower;

public class Armor extends Item{
    private final ArmorClass ac;
    private final String name;
    private int baseProtection;
    private boolean equipped;
    private Pawn owner;

    Armor(ArmorClass cls, String name) {
        super(null);
        this.ac = cls;
        this.name = name;
        equipped = false;
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

}
