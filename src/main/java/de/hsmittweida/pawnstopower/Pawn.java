package de.hsmittweida.pawnstopower;

import javafx.scene.control.Button;

import java.util.Random;

public class Pawn {
    private final short health = 100;
    private final short max_health = 100;
    private final String name;
    Armor[] armors = new Armor[4];
    Weapon[] weapons = new Weapon[2];
    private byte level;

    Pawn() {
        for (Weapon w : weapons) {
            w = null;
        }
        for (Armor c : armors) {
            c = null;
        }
        this.name = getRandomName();
    }

    /**
     * @return einen zufälligen Namen aus dem Array
     */
    private static String getRandomName() {
        String[] vornamen = {"Hans", "Peter", "Klaus", "Heinrich", "Wilhelm", "Hermann", "Konrad", "Friedrich", "Ludwig", "Otto", "Walter", "Dietrich", "Ulrich", "Berthold", "Gerhard", "Arnold", "Eberhard", "Rüdiger", "Wolfgang", "Hartmut", "Werner", "Bruno", "Erich", "Kurt", "Herbert", "Günter", "Helmut", "Horst", "Jürgen", "Wolf", "Fritz", "Karl", "Paul", "Willi", "Richard", "Robert", "Theodor", "Viktor", "Albert", "Alfred", "Egon", "Ernst", "Franz", "Georg", "Gustav", "Hugo", "Jakob", "Johann", "Josef", "Julius", "Karlheinz", "Kaspar", "Kilian", "Konstantin", "Leonhard", "Lorenz", "Magnus", "Martin", "Matthias", "Maximilian", "Nikolaus", "Oskar", "Raimund", "Reinhard", "Roland", "Sebastian", "Sigmund", "Silvester", "Simon", "Stefan", "Valentin", "Veit", "Vinzenz", "Wenzel", "Wendelin", "Wigbert", "Winfried", "Wulf", "Zeno", "Zoltan", "Adalbert", "Adolf", "Albert", "Alexander", "Alfons", "Alois", "Andreas", "Anton", "August", "Baldur", "Bartholomäus", "Benedikt", "Benjamin", "Bernhard", "Bertram", "Blasius", "Burkhard", "Clemens", "Dagobert", "Dankwart", "Detlev", "Dominik", "Eckart", "Edgar", "Eduard", "Edwin", "Egon", "Einhard", "Engelbert", "Erasmus", "Erhard", "Ernst", "Eugen", "Falk", "Felix", "Ferdinand", "Florian", "Frank", "Franz", "Friedemann", "Gabriel", "Gebhard", "Gerd", "Gernot", "Gottfried", "Gregor", "Gunther", "Guntram", "Hartwig", "Hartmut", "Heiko", "Heimo", "Helge", "Helmuth", "Henning", "Herbert", "Heribert", "Hermann", "Hilmar", "Hubert", "Hugo", "Huldreich", "Humbert", "Ingo", "Ingolf", "Irmin", "Ivo", "Jens", "Joachim", "Jörg", "Justus", "Kajetan", "Kalle", "Kanzian", "Karl", "Karsten", "Kasimir", "Kilian", "Klemens", "Knud", "Konrad", "Korbinian", "Kuno", "Kurt", "Lander", "Lars", "Laurentius", "Leander", "Leo", "Leon", "Leonhard", "Lorenz", "Lothar", "Lucas", "Ludger", "Magnus", "Malte", "Manfred", "Markus", "Martin", "Matthias", "Max", "Maximilian", "Meinolf", "Melchior", "Moritz", "Nikolaus", "Norbert", "Olaf", "Oscar", "Oswald", "Ottmar", "Otto", "Pankraz", "Pascual", "Patrick", "Paul", "Peter", "Philipp", "Quirin", "Raban", "Raffael", "Raimund", "Ralf", "Randolf", "Reinhard", "Reinhold", "Remigius", "René", "Richard", "Robert", "Rochus", "Roland", "Roman", "Rüdiger", "Rupert", "Ruprecht", "Sander", "Sascha", "Sebastian", "Severin", "Sigismund", "Sigmund", "Silas", "Silvester", "Simon", "Stefan", "Steffen", "Sten", "Sven", "Thaddäus", "Theobald", "Theodor", "Thilo", "Thomas", "Thorsten", "Till", "Tim", "Timo", "Titus", "Tobias", "Torben", "Traugott", "Tristan", "Udo", "Ulrich", "Urban", "Valentin", "Valerius", "Veit", "Viktor", "Vinzenz", "Volker", "Walter", "Walther", "Wenzel", "Werner", "Wickbert", "Wigbert", "Wilfried", "Wilhelm", "Willibald", "Willi", "Winfried", "Wolf", "Wolfgang", "Wulf", "Wunibald", "Xaver", "Yannic", "Yannick", "Yuri", "Yves", "Zeno", "Zoltan"};
        String[] nachnamen = {"Bauer", "Müller", "Huber", "Maier", "Schulz", "Becker", "Koch", "Weber", "Schmitt", "Hoffmann", "Jung", "Klein", "Lang", "Braun", "Roth", "Keller", "Busch", "Vogel", "Fuchs", "Zimmermann", "Wagner", "Schuster", "Dietz", "Lehmann"};

        Random rnd = new Random();
        return vornamen[rnd.nextInt(vornamen.length)] + " " + nachnamen[rnd.nextInt(nachnamen.length)];
    }

    /**
     * Gibt dem Bauer ein Rüstungsstück. Jeder Bauer hat ein Slot für: Kopf, Torso, Arme und Beine
     * verschiedene Waffen legen kann, oder eine große Waffe in beiden Händen tragen kann
     *
     * @return {@code true}, wenn die Rüstung erfolgreich ausgerüstet wurde, {@code false}, wenn der Slot bereits belegt ist
     */
    public boolean giveArmor(Armor armor, byte slot) {
        if (!clothingSlotUsed(slot)) {
            armors[slot] = armor;
            return true;
        }
        return false;
    }

    public  boolean giveArmor(Inspector i, Button ref, Armor armor, byte slot) {
        if(!clothingSlotUsed(slot)) {
            armors[slot] = armor;
            i.setImage(ref, armor.getArmorClass(), slot);
            armor.equip(this);
            return true;
        }
        return false;
    }

    /**
     * Gibt dem Bauer eine Waffe. Jeder Bauer hat 2 Hände, in die er 2
     * verschiedene Waffen legen kann, oder eine große Waffe in beiden Händen tragen kann.
     *
     * @param i      - Referenz für den Inspector, damit das Bild sofort aktualisiert werden kann. Nutzt hier nur die Methode {@code setImage()}der {@code Inspector}-Klasse.
     * @param ref    - Referenz für den entsprechenden Button, der das aktualisierte Bild erhalten soll.
     * @param weapon - Die Waffe, die ausgerüstet werden soll.
     * @param slot   - Der Waffenslot, an dem die Waffe ausgerüstet werden soll.
     * @return {@code true}, wenn die Waffe erfolgreich ausgerüstet wurde, {@code false}, wenn der Slot bereits belegt ist.
     */
    public boolean giveWeapon(Inspector i, Button ref, Weapon weapon, byte slot) {
        if (!weaponSlotUsed(slot)) {
            weapons[slot] = weapon;
            i.setImage(ref, weapon.getWeaponClass());
            weapon.equip(this);
            return true;
        }
        return false;
    }

    public void giveWeapon(Weapon w, byte slot) {
        weapons[slot] = w;
        w.equip(this);
    }

    public Weapon getWeapon(byte slot) {
        return weapons[slot];
    }

    public boolean removeWeapon(Weapon w) {
        for (byte b = 0; b <= 1; b++) {
            if (getWeapon(b) != null && getWeapon(b).equals(w)) {
                weapons[b] = null;
                w.unequip();
                return true;
            }
        }
        return false;
    }

    public boolean removeArmor(Armor a) {
        for(byte b = 0; b<=3; b++) {
            if(getArmor(b) != null && getArmor(b).equals(a)) {
                armors[b] = null;
                a.unequip();
                return true;
            }
        }
        return false;
    }


    public Armor getArmor(byte slot) {
        return armors[slot];
    }

    /**
     * @param slotId Die ID des Slots, der geprüft werden soll:
     *               0 - Kopf
     *               1 - Torso
     *               2 - Arme
     *               3 - Beine
     * @return {@code true}, wenn der gefragte Slot belegt ist.
     */
    private boolean clothingSlotUsed(int slotId) {
        return armors[slotId] != null;
    }

    /**
     * @param slotId Die ID des Slots, der geprüft werden soll:
     *               0 - Hand rechts
     *               1 - Hand links
     * @return {@code true}, wenn der gefragte Slot belegt ist.
     */
    private boolean weaponSlotUsed(int slotId) {
        return weapons[slotId] != null;
    }

    public String getName() {
        return this.name;
    }
}
