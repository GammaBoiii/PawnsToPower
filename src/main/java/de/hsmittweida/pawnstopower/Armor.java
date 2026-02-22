package de.hsmittweida.pawnstopower;

import java.util.Random;

/**
 * Armor Klasse, die die Logik für die Rüstung bereit stellt.
 *
 * @see Item#getItemType()
 */
public class Armor extends Item {
    /**
     * {@code ArmorClass} des Armor-Objektes.
     */
    private final ArmorClass ac;
    /**
     * Basis Wert des Schutzes.
     */
    private int baseProtection;
    /**
     * {@code true}, wenn das Teil ausgerüstet ist, sonst {@code false}..
     */
    private boolean equipped;
    /**
     * Slot ID, an dem das Teil ausgerüstet ist.
     */
    private byte slot;
    /**
     * {@code Pawn} Besitzer des Teils.
     */
    private Pawn owner;

    /**
     * Erstellt ein neues Armor Objekt mit vorgegebener Armor Klasse und Slot.
     *
     * @param cls  Rüstungsklasse, also welches Material (siehe {@link ArmorClass})
     * @param Slot Für welches Körperteil die Rüstung ist.
     * @param p    Pawn, der direkt als Besitzer gesetzt werden soll.
     */
    Armor(ArmorClass cls, byte Slot, Pawn p) {
        super(p);
        setSlotType(slot);
        this.ac = cls;
        super.setName();
    }

    /**
     * Generiert eine Rüstung mit zufälliger Rüstungsklasse.
     *
     * @param p    Pawn, der direkt als Besitzer gesetzt werden soll.
     * @param slot Für welches Körperteil die Rüstung ist.
     */
    Armor(Pawn p, byte slot) {
        super(p);
        ac = getRandomArmorClass();
        setSlotType(slot);
        super.setName(); // muss erneut aufgerufen werden, da super() beim Initialisieren den Slot noch nicht kennt!
    }

    /**
     * Generiert eine Rüstung mit zufälliger Rüstungsklasse und zufälligem Slot.
     *
     * @param p Pawn, der direkt als Besitzer gesetzt werden soll.
     */
    Armor(Pawn p) {
        super(p);
        ac = getRandomArmorClass();
        setSlotType(this.setRandomSlotType());
        super.setName(); // muss erneut aufgerufen werden, da super() beim Initialisieren den Slot noch nicht kennt!
    }

    /**
     * Generiert eine Rüstung mit angegebener Rüstungsklasse ohne Besitzer und zufälliger Rüstungsklasse.
     *
     * @param slot Für welches Körperteil die Rüstung ist.
     */
    Armor(byte slot) {
        super(null);
        ac = getRandomArmorClass();
        setSlotType(slot);
        super.setName(); // muss erneut aufgerufen werden, da super() beim Initialisieren den Slot noch nicht kennt!
    }

    /**
     * Generiert eine Rüstung mit zufälliger Rüstungsklasse und zufälligem Slot. Ohne Besitzer.
     */
    Armor() {
        super(null);
        ac = getRandomArmorClass();
        setSlotType(this.setRandomSlotType());
        super.setName(); // muss erneut aufgerufen werden, da super() beim Initialisieren den Slot noch nicht kennt!
    }

    /**
     * Gibt die Rüstungsklasse der Rüstung zurück.
     *
     * @return {@code ArmorClass}
     */
    public ArmorClass getArmorClass() {
        return ac;
    }

    /**
     * Gibt den Standardwert des Schutzes der Ausrüstung zurück
     *
     * @return {@code int}
     * @deprecated
     */
    public int getBaseProtection() {
        return baseProtection;
    }

    /**
     * Gibt den Slot zurück, an dem das Rüstungstück angebracht werden kann.
     *
     * @return {@code byte} Slot ID
     */
    public byte getSlotType() {
        if (this.slot == -1) {
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
     * @param slot SlotType, der der Rüstung gegeben werden soll
     */
    public void setSlotType(byte slot) {
        this.slot = slot;
    }

    /**
     * Returned einen zufälligen Slot als byte-Wert, entsprechend der 4 Körperregionen.
     *
     * @return {@code byte}
     */
    private byte setRandomSlotType() {
        Random rnd = new Random();
        return (byte) rnd.nextInt(4);
    }

    /**
     * Returend den Item-Typ <strong>Armor</strong>.
     *
     * @return {@code String} ~ "Amor"
     */
    public String getItemType() {
        return "Armor";
    }

    /**
     * Errechnet den Basispreis der Rüstung, basierend auf dem
     * Ausrüstungsslot und dem Material und gibt diesen zurück.
     *
     * @return {@code int}
     */
    public int getBasePrice() {
        int price = switch (this.getSlotType()) {
            case 0 -> 40;
            case 1 -> 80;
            case 2 -> 45;
            case 3 -> 60;
            default -> 65;
        };

        price = switch (this.getArmorClass()) {
            case FAB -> (int) (price * 0.85);
            case LTH -> (int) (price * 1.0);
            case IRN -> (int) (price * 1.625);
            case STL -> (int) (price * 3.25);
        };
        return price;
    }

    /**
     * Returned eine zufällige Rüstungsklasse.
     *
     * @return {@code ArmorClass}
     */
    private ArmorClass getRandomArmorClass() {
        Random rnd = new Random();
        return ArmorClass.values()[rnd.nextInt(ArmorClass.values().length)];
    }

    /**
     * Returned den gesamten Schutz, den die Rüstung entsprechend ihrere Ausüstungsregion und
     * Rüstungsklasse liefert.
     *
     * @return {@code double}
     */
    public double getTotalProtection() {
        int baseProtection = switch (this.getSlotType()) {
            case 0 -> 15;
            case 1 -> 35;
            default -> 20; // Zählt für Arm- und Beinschutz
        };
        return baseProtection * ac.modifier;
    }

    /**
     * Der Enumerator für die Rüstungsklasse:
     *
     * <ul>
     * <li>FAB = Fabric / Stoff</li>
     * <li>LTH = Leather / Leder</li>
     * <li>IRN = Iron / Eisen</li>
     * <li>STL = Steel / Stahl</li>
     * </ul>
     */
    public enum ArmorClass {
        /**
         * Fabric - Stoff
         */
        FAB(0.6, "Stoff"),
        /**
         * Leather - Leder
         */
        LTH(0.8, "Leder"),
        /**
         * Iron - Eisen
         */
        IRN(1.0, "Eisen"),
        /**
         * Steel - Stahl
         */
        STL(1.25, "Stahl");

        /**
         * Modifier Wert, der auf die Rüstung angewendet wird.
         */
        public double modifier;
        /**
         * Name der Rüstung
         */
        public String name;

        /**
         * Konstruktur für den Enumerator.
         *
         * @param modifier der Modifier für die Rüstung.
         * @param name     der Name der Rüstung.
         */
        ArmorClass(double modifier, String name) {
            this.modifier = modifier;
            this.name = name;
        }
    }
}
