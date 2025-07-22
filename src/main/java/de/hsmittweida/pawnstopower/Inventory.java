package de.hsmittweida.pawnstopower;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

/**
 * Diese Klasse enthält alles, was der Spieler besitzt.
 * Dazu zählen Kämpfer, Waffen, Kleidung und sonstige Gegenstände
 */
public class Inventory {
    private static ArrayList<Weapon> weapons;
    private static ArrayList<Armor> armor;
    private static ArrayList<Pawn> pawns;

    private static IntegerProperty money;
    private static IntegerProperty pawnsNum;

    public static void setup() {
        weapons = new ArrayList<Weapon>();
        armor = new ArrayList<Armor>();
        pawns = new ArrayList<Pawn>();
        money = new SimpleIntegerProperty();
        money.set(0);
        pawnsNum = new SimpleIntegerProperty();
        pawnsNum.set(0);

        Inventory.addPawn(new Pawn());
        Inventory.addMoney(250);
    }

    public static ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    private static void addWeapon(Weapon w) {
        weapons.add(w);
    }

    public static ArrayList<Armor> getArmor() {
        return armor;
    }

    private static void addArmor(Armor c) {
        armor.add(c);
    }

    public static void addItem(Item item) {
        if (item.getItemType().equals("Weapon")) {
            addWeapon((Weapon) item);
        } else if (item.getItemType().equals("Armor")) {
            addArmor((Armor) item);
        }
    }

    public static ArrayList<Pawn> getPawns() {
        return pawns;
    }

    public static void addPawn(Pawn p) {
        pawns.add(p);
        pawnsNum.set(pawnsNum.get() + 1);
    }

    public static IntegerProperty getPawnsNum() {
        return pawnsNum;
    }

    public static int getMoney() {
        return money.get();
    }

    public static IntegerProperty getMoneyAsSimpleInt() {
        return money;
    }

    public static void addMoney(int money) {
        Inventory.money.set(Inventory.money.get() + money);
    }
}
