package de.hsmittweida.pawnstopower;

import java.util.ArrayList;

/**
 * Diese Klasse enthält alles, was der Spieler besitzt.
 * Dazu zählen Kämpfer, Waffen, Kleidung und sonstige Gegenstände
 */
public class Inventory {
    private static ArrayList<Weapon> weapons;
    private static ArrayList<Armor> armor;
    private static ArrayList<Pawn> pawns;

    private static int money;

    public static void setup() {
        weapons = new ArrayList<Weapon>();
        armor = new ArrayList<Armor>();
        pawns = new ArrayList<Pawn>();
        money = 0;
    }

    public static ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public static void addWeapon(Weapon w) {
        weapons.add(w);
    }

    public static ArrayList<Armor> getArmor() {
        return armor;
    }

    public static void addArmor(Armor c) {
        armor.add(c);
    }

    public static ArrayList<Pawn> getPawns() {
        return pawns;
    }

    public static void addPawn(Pawn p) {
        pawns.add(p);
    }

    public static int getMoney() {
        return money;
    }

    public static void setMoney(int money) {
        Inventory.money = money;
    }
}
