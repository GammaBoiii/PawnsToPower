package de.hsmittweida.pawnstopower;

import java.util.Random;

public class Armor extends Item {
    private final ArmorClass ac;
    private int baseProtection;
    private boolean equipped;
    private byte slot;// = -1; //Erste Setzung ist hier notwendig, da die super() sonst standardmäßig 0 erkennt.
    private Pawn owner;

    Armor(ArmorClass cls, byte Slot, Pawn p) {
        super(p);
        setSlotType(slot);
        this.ac = cls;
        super.setName();
    }

    Armor(Pawn p, byte slot) {
        super(p);
        ac = getRandomArmorClass();
        setSlotType(slot);
        super.setName();
    }

    Armor(Pawn p) {
        super(p);
        ac = getRandomArmorClass();
        setSlotType(this.setRandomSlotType());
        super.setName(); // muss erneut aufgerufen werden, da super() beim Initialisieren den Slot noch nicht kennt!
    }

    Armor() {
        super(null);
        ac = getRandomArmorClass();
        setSlotType(this.setRandomSlotType());
        super.setName(); // muss erneut aufgerufen werden, da super() beim Initialisieren den Slot noch nicht kennt!
    }

    public ArmorClass getArmorClass() {
        return ac;
    }

    public int getBaseProtection() {
        return baseProtection;
    }

    /**
     * @return Slot des Rüstungsstückes
     */
    public byte getSlotType() {
        if(this.slot == -1) {
            this.slot = setRandomSlotType();
        }
        return this.slot;
    }

    /**
     * Gibt an, an welchem Körperteil das Rüstungsstück angebracht wird.
     *
     * <p> 0 - Kopf </p>
     * <p> 1 - Torso </p>
     * <p> 2 - Arme </p>
     * <p> 3 - Beine </p>
     *
     * @param slot
     */
    public void setSlotType(byte slot) {
        this.slot = slot;
    }

    private byte setRandomSlotType() {
        Random rnd = new Random();
        return (byte) rnd.nextInt(4);
    }

    public String getItemType() {
        return "Armor";
    }

    public int getBasePrice() {
        int price = switch (this.getSlotType()) {
            case 0 -> 40;
            case 1 -> 80;
            case 2 -> 45;
            case 3 -> 60;
            default -> 65;
        };

        price = switch (this.getArmorClass()) {
            case FAB -> (int) (price * 0.6);
            case LTH -> (int) (price * 0.8);
            case IRN -> (int) (price * 1.0);
            case STL -> (int) (price * 1.25);
        };
        return price;
    }

    private ArmorClass getRandomArmorClass() {
        Random rnd = new Random();
        return ArmorClass.values()[rnd.nextInt(ArmorClass.values().length)];
    }

    public double getTotalProtection() {
        int baseProtection = switch(this.getSlotType()) {
            case 0 -> 15;
            case 1 -> 35;
            default -> 20; // Zählt für Arm- und Beinschutz
        };
        return baseProtection * ac.modifier;
    }

    public enum ArmorClass {
        FAB(0.6), //Stoff
        LTH(0.8), //Leder
        IRN(1.0), //Eisen
        STL(1.25); //Stahl

        public double modifier;

        ArmorClass(double modifier) {
            this.modifier =  modifier;
        }
    }
}
