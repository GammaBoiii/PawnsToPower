package de.hsmittweida.pawnstopower;

import java.util.Random;

public class Item {
    private String name;
    private Pawn owner;
    private boolean equipped;

    protected Item(Pawn owner) {
        this.owner = owner;
        setName();
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

    protected void setName() {
        name = randomName(this);
    }

    protected String randomName(Item item) {
        if (item.getItemType().equals("Weapon")) {
            String[] mysticalWeaponNames = {"Excalibur", "Durandal", "Anduril", "Joyeuse", "Mjolnir", "Hrunting", "Kusanagi", "Zulfiqar", "Arondight", "Caliburn", "Obsidian", "Eternity", "Infinity", "Celestia", "Elysium", "Oracle", "Destiny", "Legacy", "Revelation", "Ascendant", "Radiance", "Nebula", "Nova", "Zenith", "Apex", "Vanguard", "Sovereign", "Empyrean", "Genesis", "Redemption", "Eclipse", "Requiem", "Mirage", "Mythos", "Empyreal", "Tempest", "Vortex", "Serenity", "Phantom", "Specter", "Arcadia", "Odyssey", "Inferno", "Chimera", "Mythica", "Mystic", "Eon", "Quantum", "Radiant", "Revere", "Solstice", "Equinox", "Aether", "Vesper", "Omen", "Nebulous", "Elysian", "Aurora", "Twilight", "Celestial", "Dominion", "Eminence", "Arcanum", "Enigma", "Singularity", "Nexus", "Reverence", "Ascension", "Solara", "Evanescence", "Majesty", "Celestium", "Oblivion", "Fate", "Reverie", "Paradigm", "Sovereignty", "Eternum", "Abyss", "Spectral", "Vigil", "Horizon", "Arcane", "Rapture", "Lumina", "Seraphim", "Valor", "Phantasm", "Nocturne", "Starlight", "Wraith", "Hallowed", "Sacred", "Mysteria", "Empyre", "Requital", "Veneration", "Ascendancy", "Transcendence", "Illumina"};
            Random rnd = new Random();
            return mysticalWeaponNames[rnd.nextInt(mysticalWeaponNames.length)];
        } else if (item.getItemType().equals("Armor")) {
            return "";
        }
        return "";
    }

    protected String getItemType() {
        return "Item";
    }
}
