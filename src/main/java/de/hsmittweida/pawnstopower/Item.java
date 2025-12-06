package de.hsmittweida.pawnstopower;

import java.io.Serializable;
import java.util.Random;

/**
 * Elternklasse der Items (Rüstung und Waffen).
 * <br>
 * Handhabt die Grundeigenschaften eines Items, wie Besitzer, Name, Ausgerüstet-Status und Item-Typ.
 */
public class Item implements Serializable {
    private String name;
    private Pawn owner;
    private boolean equipped;

    /**
     * Legt den Besitzer fest.
     * <br>
     * Kann null sein.
     * @param owner
     */
    protected Item(Pawn owner) {
        this.owner = owner;
    }

    /**
     * Findet den Slot, an dem das Item ausgerüstet ist, falls es einen Besitzer hat.
     * @param item Item, welches gesucht werden soll.
     * @return Slot von dem Item; -1, wenn das Item nicht ausgerüstet ist.
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
     * Sollte nur mit der {@code Pawn.removeArmor() / Pawn.removeWeapon} verwendet werden,
     * da hier die Itemreferenz beim Pawn nicht entfernt wird.
     */
    protected void unequip() {
        this.equipped = false;
        this.owner = null;
    }

    /**
     * @return {@code true}, wenn das Item ausgerüstet ist.
     */
    protected boolean isEquipped() {
        return equipped;
    }

    /**
     * @return Pawn, der das Item besitzt, bzw. ausgerüstet hat.
     */
    protected Pawn getOwner() {
        return owner;
    }

    /**
     * @return Name des Items.
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
     * @return Den generierten Name.
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
     * @return Item-Typ
     */
    protected String getItemType() {
        return "Item";
    }
}
