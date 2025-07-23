package de.hsmittweida.pawnstopower;

import java.io.Serializable;
import java.util.Random;

public class Item implements Serializable {
    private String name;
    private Pawn owner;
    private boolean equipped;

    protected Item(Pawn owner) {
        this.owner = owner;
    }

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
        return 0;
    }

    protected void equip(Pawn p) {
        if (p == null) {
            this.equipped = false;
            this.owner = null;
            return;
        }
        this.equipped = true;
        this.owner = p;
    }

    protected void unequip() {
        this.equipped = false;
        this.owner = null;
    }

    protected boolean isEquipped() {
        return equipped;
    }

    protected Pawn getOwner() {
        return owner;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String s) {
        this.name = s;
    }

    protected void setName(){
        this.name = randomName(this);
    }

    protected String randomName(Item item) {
        if (item.getItemType().equals("Weapon")) {
            String[] mysticalWeaponNames = {"Excalibur", "Durandal", "Anduril", "Joyeuse", "Mjolnir", "Hrunting", "Kusanagi", "Zulfiqar", "Arondight", "Caliburn", "Obsidian", "Eternity", "Infinity", "Celestia", "Elysium", "Oracle", "Destiny", "Legacy", "Revelation", "Ascendant", "Radiance", "Nebula", "Nova", "Zenith", "Apex", "Vanguard", "Sovereign", "Empyrean", "Genesis", "Redemption", "Eclipse", "Requiem", "Mirage", "Mythos", "Empyreal", "Tempest", "Vortex", "Serenity", "Phantom", "Specter", "Arcadia", "Odyssey", "Inferno", "Chimera", "Mythica", "Mystic", "Eon", "Quantum", "Radiant", "Revere", "Solstice", "Equinox", "Aether", "Vesper", "Omen", "Nebulous", "Elysian", "Aurora", "Twilight", "Celestial", "Dominion", "Eminence", "Arcanum", "Enigma", "Singularity", "Nexus", "Reverence", "Ascension", "Solara", "Evanescence", "Majesty", "Celestium", "Oblivion", "Fate", "Reverie", "Paradigm", "Sovereignty", "Eternum", "Abyss", "Spectral", "Vigil", "Horizon", "Arcane", "Rapture", "Lumina", "Seraphim", "Valor", "Phantasm", "Nocturne", "Starlight", "Wraith", "Hallowed", "Sacred", "Mysteria", "Empyre", "Requital", "Veneration", "Ascendancy", "Transcendence", "Illumina"};
            Random rnd = new Random();
            return mysticalWeaponNames[rnd.nextInt(mysticalWeaponNames.length)];
        } else if (item.getItemType().equals("Armor")) {
            String name;
            Armor armor = (Armor) item;
            name = switch (armor.getSlotType()) {
                case 0 -> " - Helm";
                case 1 -> " - Brustplatte";
                case 2 -> " - Handschuhe";
                case 3 -> " - Beinschutz";
                default -> "";
            };
            if (armor.getArmorClass() == null) { //Wird benötigt, da die Super-Klasse den Slot beim Initialisiern des Rüstungsstückes noch nicht kennt! (Siehe Armor Klasse)
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

    protected String getItemType() {
        return "Item";
    }
}
