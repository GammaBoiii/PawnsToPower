package de.hsmittweida.pawnstopower;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Diese Klasse enthält alles, was der Spieler besitzt.
 * <br>
 * Dazu zählen Kämpfer, Waffen, Kleidung und sonstige Gegenstände
 */
public class Inventory implements Serializable {
    private static ArrayList<Weapon> weapons;
    private static ArrayList<Armor> armor;
    private static ArrayList<Pawn> pawns;
    private static IntegerProperty money;
    private static IntegerProperty pawnsNum;
    private static IntegerProperty reputation;
    private static boolean initialized = false;

    /**
     * Die {@code setup()} Methode wird zu Beginn eines neuen Spieles aufgerufen und
     * initialisiert das Inventar.
     */
    public static void setup() {
        weapons = new ArrayList<Weapon>();
        armor = new ArrayList<Armor>();
        pawns = new ArrayList<Pawn>();
        money = new SimpleIntegerProperty();
        money.set(0);
        pawnsNum = new SimpleIntegerProperty();
        pawnsNum.set(0);

        Inventory.addPawn(new Pawn(true));
        Inventory.addMoney(250);
        Inventory.addReputation(15);

        initialized = true;
    }

    /**
     * @return {@code ArrayList<Weapons>}, mit den Waffen, die der Spieler besitzt.
     */
    public static ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Fügt dem Inventar eine neue Waffe hinzu.
     * @param w Waffe, die dem Inventar hinzugefügt werden soll.
     */
    private static void addWeapon(Weapon w) {
        weapons.add(w);
    }

    /**
     * Entfernt eine Waffe aus dem Inventar.
     * @param w Waffe, die entfernt werden soll.
     */
    private static void removeWeapon(Weapon w) {
        weapons.remove(w);
    }

    /**
     * @return {@code ArrayList<Armor>}, mit den Rüstungen, die der Spieler besitzt.
     */
    public static ArrayList<Armor> getArmor() {
        return armor;
    }

    /**
     * Fügt dem Inventar eine neue Waffe hinzu.
     * @param a Rüstungsstück, das dem Inventar hinzugefügt werden soll.
     */
    private static void addArmor(Armor a) {
        armor.add(a);
    }

    private static void removeArmor(Armor a) {
        armor.remove(a);
    }

    /**
     * Alternative Methode, um dem Spieler ein neues Item in das Inventar abzulegen.
     * @param item
     */
    public static void addItem(Item item) {
        if (item.getItemType().equals("Weapon")) {
            addWeapon((Weapon) item);
        } else if (item.getItemType().equals("Armor")) {
            addArmor((Armor) item);
        }
    }

    /**
     * Entfernt ein Item aus dem Inventar.
     * @param item Item, welches entfernt werden soll.
     */
    public static void removeItem(Item item) {
        if(item.getItemType().equals("Weapon")) {
            removeWeapon((Weapon) item);
        } else if(item.getItemType().equals("Armor")) {
            removeArmor((Armor) item);
        }
    }

    /**
     * @return {@code ArrayList<Pawn>}, mit den Kämpfern/Pawns, die der Spieler besitzt.
     */
    public static ArrayList<Pawn> getPawns() {
        return pawns;
    }

    /**
     * Fügt dem Inventar einen neuen Kämpfer hinzu.
     * @param p Kämpfer, der dem Inventar hinzugefügt werden soll.
     */
    public static void addPawn(Pawn p) {
        pawns.add(p);
        pawnsNum.set(pawnsNum.get() + 1);
    }

    /**
     * @return Anzahl der Pawns im Spielerinventar.
     */
    public static IntegerProperty getPawnsNum() {
        return pawnsNum;
    }

    /**
     * @return Geld/Gold im Spielerinventar.
     */
    public static int getMoney() {
        return money.get();
    }

    /**
     * @return Geld/Gold im Spielerinventar. Als Integer-Wert.
     */
    public static IntegerProperty getMoneyAsSimpleInt() {
        return money;
    }

    /**
     * Fügt dem Inventar Geld/Gold hinnzu.
     * @param money
     */
    public static void addMoney(int money) {
        Inventory.money.set(Inventory.money.get() + money);
    }

    /**
     * @return Reputation, die der Spieler besitzt.
     */
    public static IntegerProperty getReputation() {
        if(Inventory.reputation == null) {
            Inventory.reputation = new SimpleIntegerProperty(0);
        }
        return reputation;
    }

    /**
     * Fügt dem Spieler Reputation hinzu.
     * <br>
     * Gegen Werte unter 0 abgesichert.
     * @param reputation Reputation, die hinzugefügt werden soll.
     */
    public static void addReputation(int reputation) {
        if(Inventory.reputation == null) {
            Inventory.reputation = new SimpleIntegerProperty(reputation);
            return;
        }
        if (reputation < 0 && Math.abs(reputation) > Math.abs(Inventory.reputation.get())) {
            Inventory.reputation.set(0);
        } else {
            Inventory.reputation.set(Inventory.reputation.get() + reputation);
        }
    }

    /**
     * @return {@code true}, wenn das Inventar nach einem Spielstart oder Laden eines Spielstandes initialisiert wurde.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Leert das Inventar komplett und setzt Resourcen und Tage zurück.
     */
    public static void clear() {
        weapons.clear();
        armor.clear();
        pawns.clear();
        money.set(0);
        pawnsNum.set(0);
        reputation.set(0);
        Game.resetDay();
    }

    /**
     * @return {@code int} - ID des letzten Pawns im Inventar, bzw -1, wenn kein Pawn im Inv ist.
     */
    public static int getLastPawnId() {
        return pawns.size() == 0 ? -1 : pawns.get(pawns.size()-1).getId();
    }

    /**
     * Gibt einen Pawn basierend auf dessen Id zurück. Für besondere Fälle, in denen
     * die Pawn Id von dem Index des Pawns im Inventar abweichen sollte.
     * @return {@code Pawn} mit der entsprechenden Id
     */
    public static Pawn getPawnById(int id) {
        for(Pawn p : pawns) {
            if(p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
