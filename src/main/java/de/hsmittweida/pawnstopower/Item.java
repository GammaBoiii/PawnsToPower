package de.hsmittweida.pawnstopower;

import java.io.Serializable;
import java.util.Random;

/**
 * Elternklasse der Items (Rüstung und Waffen).
 * <br>
 * Handhabt die Grundeigenschaften eines Items, wie Besitzer, Name, Ausgerüstet-Status und Item-Typ.
 */
public class Item implements Serializable {
    /**
     * Name des Items
     */
    private String name;
    /**
     * Besitzer des Items. Kann null sein.
     */
    private Pawn owner;
    /**
     * Ist Item ausgerüstet?
     * {@code true}, wenn Item ausgerüstet ist
     */
    private boolean equipped;
    /**
     * Kombination aus Pawn Id und Ausrüstungsslot. <br> <br>
     * Wird dringend benötigt, um Items nach dem Laden eines gespeicherten Spielstandes korrekt anzulegen.
     * Sonst kann es zum Fehler kommen, dass die Items noch den "alten" Ownern (Java-Objekt-Referenz)
     * aus dem alten Spielstand zugewiesen sind und daher nachd dem Laden nicht bei dem "neuen" Pawn-Objekt
     * angelegt sind.
     */
    private int[] equipLocation;

    /**
     * Legt den Besitzer fest.
     * <br>
     * Kann null sein.
     * @param owner Owner von dem Item
     */
    protected Item(Pawn owner) {
        this.owner = owner;
    }

    /**
     * Findet den Slot, an dem das Item ausgerüstet ist, falls es einen Besitzer hat und gibt diesen Slot zurück.
     * @param item Item, welches gesucht werden soll.
     * @return {@code byte} SlotID
     */
    public static byte getSlotOfItem(Item item) {
        if (item.getItemType().equals("Weapon")) {
            for (byte i = 0; i < 2; i++) {
                if (item.getOwner().getWeapon(i) != null && item.getOwner().getWeapon(i).equals(item)) {
                    return i;
                }
            }
        } else if (item.getItemType().equals("Armor")) {
            for (byte i = 0; i < 4; i++) {
                if (item.getOwner().getArmor(i) != null && item.getOwner().getArmor(i).equals(item)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Legt einem Kämpfer ein Item an.
     * @param p Pawn, der das Item bekommen soll.
     */
    protected void equip(Pawn p) {
        if (p == null) {
            this.equipped = false;
            this.owner = null;
            return;
        }
        this.equipped = true;
        this.owner = p;
    }

    /**
     * Entfernt ein Item von einem Kämpfer.
     * <br>
     * Sollte nur mit der {@link Pawn#removeArmor(Armor)} / {@link Pawn#removeWeapon(Weapon)} verwendet werden,
     * da hier die Itemreferenz beim Pawn nicht entfernt wird.
     */
    protected void unequip() {
        this.equipped = false;
        this.owner = null;
    }

    /**
     * Gibt an, ob ein Item ausgerüstet ist.
     * @return {@code true}, wenn das Item ausgerüstet ist.
     */
    protected boolean isEquipped() {
        return equipped;
    }

    /**
     * Gibt den Pawn zurück, der das Item besitzt.
     * @return {@code Pawn}
     */
    protected Pawn getOwner() {
        return owner;
    }

    /**
     * Gibt den Namen des Items zurück.
     * @return {@code String}
     */
    protected String getName() {
        return name;
    }

    /**
     * Gibt dem Item einen Namen.
     * @param s Name, den das Item bekommen soll.
     */
    protected void setName(String s) {
        this.name = s;
    }

    /**
     * Gibt dem Item einen zufällig generierten Namen.
     */
    protected void setName(){
        this.name = randomName(this);
    }

    /**
     * Generiert einen Namen, basierend auf dem Item Typ.
     * @param item Das Item, für welches ein Namen generiert werden soll.
     * @return {@code String} Name
     */
    protected String randomName(Item item) {

        /* Namensgenerierung für Waffen: */
        if (item.getItemType().equals("Weapon")) {
            String[] mysticalWeaponNames = {"Excalibur", "Durandal", "Anduril", "Joyeuse", "Mjolnir", "Hrunting", "Kusanagi", "Zulfiqar", "Arondight", "Caliburn", "Obsidian", "Eternity", "Infinity", "Celestia", "Elysium", "Oracle", "Destiny", "Legacy", "Revelation", "Ascendant", "Radiance", "Nebula", "Nova", "Zenith", "Apex", "Vanguard", "Sovereign", "Empyrean", "Genesis", "Redemption", "Eclipse", "Requiem", "Mirage", "Mythos", "Empyreal", "Tempest", "Vortex", "Serenity", "Phantom", "Specter", "Arcadia", "Odyssey", "Inferno", "Chimera", "Mythica", "Mystic", "Eon", "Quantum", "Radiant", "Revere", "Solstice", "Equinox", "Aether", "Vesper", "Omen", "Nebulous", "Elysian", "Aurora", "Twilight", "Celestial", "Dominion", "Eminence", "Arcanum", "Enigma", "Singularity", "Nexus", "Reverence", "Ascension", "Solara", "Evanescence", "Majesty", "Celestium", "Oblivion", "Fate", "Reverie", "Paradigm", "Sovereignty", "Eternum", "Abyss", "Spectral", "Vigil", "Horizon", "Arcane", "Rapture", "Lumina", "Seraphim", "Valor", "Phantasm", "Nocturne", "Starlight", "Wraith", "Hallowed", "Sacred", "Mysteria", "Empyre", "Requital", "Veneration", "Ascendancy", "Transcendence", "Illumina"};
            Random rnd = new Random();
            return mysticalWeaponNames[rnd.nextInt(mysticalWeaponNames.length)];
        } /* Namensgenerierung für Rüstung. Diese basieren nur auf Material und Rüstungsslot. */
        else if (item.getItemType().equals("Armor")) {
            String name;
            Armor armor = (Armor) item;
            name = switch (armor.getSlotType()) {
                case 0 -> " - Helm";
                case 1 -> " - Brustplatte";
                case 2 -> " - Handschuhe";
                case 3 -> " - Beinschutz";
                default -> "";
            };
            /* Wird benötigt, da die Super-Klasse den Slot beim Initialisiern des Rüstungsstückes noch nicht kennt! (Siehe Armor Klasse) */
            if (armor.getArmorClass() == null) {
                return "Stoff" + name;
            } else
                name = switch (armor.getArmorClass()) {
                    case FAB -> "Stoff" + name;
                    case LTH -> "Leder" + name;
                    case IRN -> "Eisen" + name;
                    case STL -> "Stahl" + name;
                };
            return name;
        }
        return "";
    }

    /**
     * Platzhalter der Super Klasse. Wird von den Kindklassen entsprechend mit {@code Override} überschrieben.
     * @return {@code String} Item-Typ
     */
    protected String getItemType() {
        return "Item";
    }

    /**
     * Gibt die EquipLocation eines Pawns an
     * @return {@code int[]}
     */
    public int[] getEquipLocation() {
        return equipLocation;
    }

    /**
     * Generiert die Equip-Location.
     * @param pawnid Id des Pawns für den die Location generiert werden soll.
     * @param slotid Id des Slots.
     */
    public void generateEquipLocation(int pawnid, int slotid) {
        equipLocation = new int[] {pawnid, slotid};
    }

    /**
     * Setzt das Item an eine Location im Equipement.
     * Dient der korrekten Ausrüstung von Items nach dem Laden eines gespeicherten Spielstandes
     * @param item Item, welches gepusht werden soll.
     */
    public void pushEquipLocation(Item item) {
        if(item.getItemType().equals("Weapon")) {
            Inventory.getPawnById(equipLocation[0]).giveWeapon((Weapon) item, (byte) equipLocation[1]);
        } else if(item.getItemType().equals("Armor")) {
            Inventory.getPawnById(equipLocation[0]).giveArmor((Armor) item, (byte) equipLocation[1]);
        } else {
            System.out.println("Konnte Item nicht anlegen");
        }

    }
}
