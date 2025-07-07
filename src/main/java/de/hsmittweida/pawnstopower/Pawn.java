package de.hsmittweida.pawnstopower;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;

public class Pawn {
    private final String name;
    Armor[] armors = new Armor[4];
    Weapon[] weapons = new Weapon[2];
    private final ArrayList<Skill> skills;
    private int skillPoints;

    private final IntegerProperty xp;
    private final IntegerBinding lvl;

    Pawn() {
        for (Weapon w : weapons) {
            w = null;
        }
        for (Armor c : armors) {
            c = null;
        }
        this.name = getRandomName();
        // this.level = 1;
        // this.experience = 0;

        Skill health = new Skill("health", "Leben", 100, this);
        Skill damage = new Skill("damage", "Schaden", 5, this);
        Skill resistance = new Skill("resistance", "Widerstand", 10, this);
        Skill speed = new Skill("speed", "Agilität", 15, this);
        skills = new ArrayList<>();
        skills.add(health);
        skills.add(damage);
        skills.add(resistance);
        skills.add(speed);

        /* Im folgenden Abschnitt wird mittels IntegerBinding und der dazugehörigen computeValue() Methode
         * das Level an die Erfahrungspunkte gebunden. Dabei braucht ein weiteres Level immer 5 Erfahrungspunkte (xp)
         * mehr, als das vorherige Level - beginnend bei 50 benötigten Erfahrungspunkten für Level 2.
         */
        this.xp = new SimpleIntegerProperty(0);
        this.lvl = new IntegerBinding() {

            {
                super.bind(xp);
            }

            @Override
            protected int computeValue() {
                int currentXP = xp.get();
                int requiredXP = 50;
                int l = 1;

                while (currentXP >= requiredXP) {
                    l++;
                    skillPoints++;

                    /* Steigerung: jedes Level braucht 5 XP mehr */
                    currentXP -= requiredXP;
                    requiredXP += 5;
                }

                return l;
            }

        };
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

    public boolean giveArmor(Button ref, Armor armor, byte slot) {
        if (!clothingSlotUsed(slot)) {
            armors[slot] = armor;
            Inspector.setImage(ref, armor.getArmorClass(), slot);
            armor.equip(this);
            return true;
        }
        return false;
    }

    /**
     * Gibt dem Bauer eine Waffe. Jeder Bauer hat 2 Hände, in die er 2
     * verschiedene Waffen legen kann, oder eine große Waffe in beiden Händen tragen kann.
     *
     * @param ref    - Referenz für den entsprechenden Button, der das aktualisierte Bild erhalten soll.
     * @param weapon - Die Waffe, die ausgerüstet werden soll.
     * @param slot   - Der Waffenslot, an dem die Waffe ausgerüstet werden soll.
     * @return {@code true}, wenn die Waffe erfolgreich ausgerüstet wurde, {@code false}, wenn der Slot bereits belegt ist.
     */
    public boolean giveWeapon(Button ref, Weapon weapon, byte slot) {
        if (!weaponSlotUsed(slot)) {
            weapons[slot] = weapon;
            Inspector.setImage(ref, weapon.getWeaponClass());
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
        for (byte b = 0; b <= 3; b++) {
            if (getArmor(b) != null && getArmor(b).equals(a)) {
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

    public Armor[] getAllArmor() {
        return armors;
    }

    public Weapon[] getAllWeapons() {
        return weapons;
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

    /**
     * Diese Methode beinhaltet die Logik zum Aufleveln. Dazu wird die Formel {@code f(x) = 5 * ( x - 1 ) + 50} verwendet <i>(umgestellt
     * nach x, da die Erfahrungspunkte (y-Achse) gegeben sind, und x (das Level) eigentlich gesucht wird.) </i> <br>
     * Diese gibt die benötigten Erfahrungspunkte ({@code experience}) an. Level 1 braucht also 50 Erfahrungspunkt, Level 2 55,
     * Level 3 60, [...] Erfahrungspunkte für das jeweilige nächste Level.
     */
    /* private void calcLevel(int val) {
        this.level = (byte) Math.floor((double) (val - 45) / 5);
    }

    public int xpForLevelup(byte level) {
        return 5 * (level - 1) + 50;
    }

    public byte getLevel() {
        return this.level;
    }

    public void addExperience(int xp) {
        this.experience += xp;
        if (this.getExperience() >= this.xpForLevelup(this.getLevel())) {
            this.experience = 0;
            this.level++;
        }
    }

    public int getExperience() {
        return experience;
    } */

    /**
     * Gibt die (finalen) Stats des Pawns an, inkl. des Levelbonus und des Skill-Bonus.
     * <br> <br>
     * Funktioniert, indem zuerst jedes Level ab level 2 um den Factor 0.1 erhöht wird <em>(Level 2 -> *1.1,
     * Level 3 -> *1.2, Level 4 -> *1.3,...)</em>, dann der Skillfactor damit multipliziert wird (aus {@code skillFactor})
     * und dann auf den Basiswert addiert wird.
     * <p>
     * param skill Der abzufragende Skill ({@code health, damage, resistance} oder {@code speed})
     * return Den totalen Skill-Wert
     */
    /*public double getSkillValue(String skill) {
        return switch (skill) {
            case "health" -> ((9.0+this.getLevel()) / 10.0) * skillFactor.get("health") + baseHealth;
            case "damage" -> ((9.0+this.getLevel()) / 10.0) * skillFactor.get("damage") + baseDamage;
            case "resistance" -> ((9.0+this.getLevel()) / 10.0) * skillFactor.get("resistance") + baseResistance;
            case "speed" -> ((9.0+this.getLevel()) / 10.0) * skillFactor.get("speed") + baseSpeed;
            default -> 0.0;
        };
    }*/

    /**
     *
     * @return Skills: <br> <blockquote> 0 = health <br> 1 = damage <br> 2 = resistance <br> 3 = speed</blockquote>
     */
    public ArrayList<Skill> getSkills() {
        return this.skills;
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }

    public void addSkillPoints(int num) {
        this.skillPoints += num;
    }
/*
    /**
     * Setzt das Level eines Pawns direkt. Wird eigentlich nur bei der Genereirung von Gegnern in der Arena verwendet.
     * @param level Das zu setzende Level
     */
     /* public void setLevel(byte level) {
        this.level = level;
    } */

    public IntegerBinding addXp(int x) {
        this.xp.set(this.xp.get() + x);
        // System.out.println("xp: " + this.xp.get() + " - " + this.lvl.get());
        return this.lvl;
    }

    public int getLvl() {
        return this.lvl.get();
    }

    /**
     * Da das Level nun ein IntegerBinding ist, der an den xp "hängt", kann man das level nicht ohne weiteres einfach setzen.
     * Daher muss man mit den Erfahrungspunkten (xp) direkt arbeiten. Hierfür muss man also das Level erst in die benötigten
     * xp "umrechnen" und die xp selbst updaten. Das Level aktualisiert sich automatisch.
     * Da diese Methode eigentlich nur zur Generierung der Gegner genutzt wird, und diese immer mit dem StandardLevel (1) generiert
     * werden, ist es nicht nötig vorher abzufragen, wie viel xp der Pawn aktuell hat.
     *
     * @param l Level, welches gesetzt - oder besser gesagt: "erreicht" - werden soll.
     */
    public void setLvl(int l) {
        if (l <= 1) return;
        int nextLvlXp = 50;
        for (int i = 1; i < l; i++) {
            addXp(nextLvlXp);
            nextLvlXp += 5;
            // System.out.println("xxx");
        }
    }

    public boolean ownedByPlayer() {
        for(Pawn p : Inventory.getPawns()){
            if (this.equals(p)) return true;
        }
        return false;
    }

    /**
     * Errechnet den Schaden, der ein Pawn einem anderen gnerischen Pawn hinzufügt.
     *
     * <p></pr>Berücksicht dabei: <br>
     *  - den Schaden, die Waffe und Geschwindigkeit des Angreifers ("{@code this}"-Pawn")</p>
     *  - die Leben, Rüstung, Verteidigung (inkl Verteidigungsaktion in der Arena) und Geschwindigkeit des angegriffenen Pawns "{@code enemy}"
     * @param enemy
     * @return
     */
    public int calcDamage(Pawn enemy) {
        double damage = 0;
        /* Zunächst wird die Geschwindigkeit verglichen. Der schneller Pawn hat hier den Vorteil.
         * Beim Angriff führt eine geringere Geschwindigkeit zu Fehltreffern oder Streifschüssen.
         * Damit der Kampf spannend bleibt, gibt es immer die Chance für den Langsameren zu treffen
         * oder auszuweichen. (Dafür die {@code graze = 0.05} und {@code graze = 0.95}*/
        double graze = 0.0; // Streifschuss von 0 ergibt 100% Trefferchance..
        Skill pSpeed = this.getSkills().get(3);
        Skill eSpeed = enemy.getSkills().get(3);
        if(pSpeed.getSkillValue() - eSpeed.getSkillValue() > 3) {
            graze = 0.05;
        } else if(pSpeed.getSkillValue() - eSpeed.getSkillValue() > 0){
            graze = 0.1;
        } else {
            graze = Math.pow(Math.log(1-(pSpeed.getSkillValue() / eSpeed.getSkillValue())), -1) * -1;
            System.out.println("graze: " + graze + "-> " + pSpeed.getSkillValue() + " | " +  eSpeed.getSkillValue());
        }
        if(graze >= 0.99) {
            graze = 0.95;
        }

        /* Weiterhin wird der Angriffswert der Waffe mit dem Schadenswert des Angreifers zusammengefügt
         * und mit dem Rüstungswert des Gegners, sowie dessen Reistance Wert verglichen.
         */
        double weapondamage1 = this.getWeapon((byte) 0) != null ? this.getWeapon((byte) 0).getTotalDamage() : 0;
        double weapondamage2 = this.getWeapon((byte) 1) != null ? this.getWeapon((byte) 1).getTotalDamage() : 0;
        damage += this.getSkills().get(1).getSkillValue();
        damage += weapondamage1 + weapondamage2;
        System.out.println("damage without enemy values: " + damage);
        System.out.println("gegner rüstung: " + enemy.getTotalProtectionValue());



        /* Falls der fehlgeschlagene Angriff ein Streifschuss war, dann wird auf den gesamten vorher
         * berechneten Schaden eine Verringerung um 90% gelegt. Daher steht die Überprüfung
         * danach erst am Ende dieser Methode.
         * Bei einem kompletten Fehltreffer ist der Schaden einfach 0.
         * Zwischen Streifschuss und Fehltreffer wird einfach per 50/50 Wahrscheinlichkeit entschieden.
         */
        if(Math.random()<graze) {
            damage = Math.random() < 0.5 ? damage * 0.1 : 0;
        }
        return (int) Math.floor(damage);
    }

    public int getTotalProtectionValue() {
        int val = 0;
        for(Armor a : this.armors){
            if(a == null) continue;
            val += (int) Math.floor(a.getTotalProtection());
        }
        return val;
    }
}
