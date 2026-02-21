package de.hsmittweida.pawnstopower;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Die Pawn Klasse beinhaltet die Logik zur Erstellung und Verwaltung der einzelen Kämpfer (Pawns)
 */
public class Pawn implements Serializable {
    /**
     * Name des Pawns.
     */
    private final String name;
    /**
     * Rüstung, die der Pawn angelegt hat.
     */
    private Armor[] armors = new Armor[4];
    /**
     * Waffen, die der Pawn angelegt hat.
     */
    private Weapon[] weapons = new Weapon[2];
    /**
     * Skills des Pawns.
     */
    private final ArrayList<Skill> skills;
    /**
     * Verfügbare Skillpunkte des Pawns.
     */
    private int skillPoints;
    /**
     * Fortschritt zum nächsten Level.
     */
    private transient DoubleProperty progress;
    /**
     * Akutelle Erfahrungspunkte des Pawns.
     */
    private transient IntegerProperty xp;
    /**
     * Binding für das Level des Pawns.
     */
    private transient IntegerBinding lvl;
    /**
     * Boolean, ob der Pawn am aktuellen Tag bereits in der Arena gekämpft hat.
     * {@code true} ~ Pawn hat an dem Tag bereits gekämpft
     */
    private boolean foughtToday = false;
    /**
     * ID des Pawns im Inventar des Spielers
     */
    private int id;

    /**
     * Konstruktor
     * Initialisiert alle Variablen des Pawns.
     */
    Pawn(boolean generateInPlayerInv) {
        for (Weapon w : this.getWeapons()) {
            w = null;
        }

        for (Armor c : this.getArmors()) {
            c = null;
        }

        this.name = getRandomName();

        Skill health = new Skill("health", "Leben", 100, this);
        Skill damage = new Skill("damage", "Schaden", 5, this);
        Skill resistance = new Skill("resistance", "Widerstand", 10, this);
        Skill speed = new Skill("speed", "Agilität", 15, this);
        skills = new ArrayList<>();
        skills.add(health);
        skills.add(damage);
        skills.add(resistance);
        skills.add(speed);

        initVars();

        if(generateInPlayerInv) {
            this.setId();
        }
    }

    /**
     * Generiert per Zufalle einen Namen für den Pawn.
     * @return {@code String}
     */
    private static String getRandomName() {
        String[] vornamen = {"Hans", "Peter", "Klaus", "Heinrich", "Wilhelm", "Hermann", "Konrad", "Friedrich", "Ludwig", "Otto", "Walter", "Dietrich", "Ulrich", "Berthold", "Gerhard", "Arnold", "Eberhard", "Rüdiger", "Wolfgang", "Hartmut", "Werner", "Bruno", "Erich", "Kurt", "Herbert", "Günter", "Helmut", "Horst", "Jürgen", "Wolf", "Fritz", "Karl", "Paul", "Willi", "Richard", "Robert", "Theodor", "Viktor", "Albert", "Alfred", "Egon", "Ernst", "Franz", "Georg", "Gustav", "Hugo", "Jakob", "Johann", "Josef", "Julius", "Karlheinz", "Kaspar", "Kilian", "Konstantin", "Leonhard", "Lorenz", "Magnus", "Martin", "Matthias", "Maximilian", "Nikolaus", "Oskar", "Raimund", "Reinhard", "Roland", "Sebastian", "Sigmund", "Silvester", "Simon", "Stefan", "Valentin", "Veit", "Vinzenz", "Wenzel", "Wendelin", "Wigbert", "Winfried", "Wulf", "Zeno", "Zoltan", "Adalbert", "Adolf", "Albert", "Alexander", "Alfons", "Alois", "Andreas", "Anton", "August", "Baldur", "Bartholomäus", "Benedikt", "Benjamin", "Bernhard", "Bertram", "Blasius", "Burkhard", "Clemens", "Dagobert", "Dankwart", "Detlev", "Dominik", "Eckart", "Edgar", "Eduard", "Edwin", "Egon", "Einhard", "Engelbert", "Erasmus", "Erhard", "Ernst", "Eugen", "Falk", "Felix", "Ferdinand", "Florian", "Frank", "Franz", "Friedemann", "Gabriel", "Gebhard", "Gerd", "Gernot", "Gottfried", "Gregor", "Gunther", "Guntram", "Hartwig", "Hartmut", "Heiko", "Heimo", "Helge", "Helmuth", "Henning", "Herbert", "Heribert", "Hermann", "Hilmar", "Hubert", "Hugo", "Huldreich", "Humbert", "Ingo", "Ingolf", "Irmin", "Ivo", "Jens", "Joachim", "Jörg", "Justus", "Kajetan", "Kalle", "Kanzian", "Karl", "Karsten", "Kasimir", "Kilian", "Klemens", "Konrad", "Korbinian", "Kuno", "Kurt", "Lander", "Lars", "Laurentius", "Leander", "Leo", "Leon", "Leonhard", "Lorenz", "Lothar", "Lucas", "Ludger", "Magnus", "Malte", "Manfred", "Markus", "Martin", "Matthias", "Max", "Maximilian", "Meinolf", "Melchior", "Moritz", "Nikolaus", "Norbert", "Olaf", "Oscar", "Oswald", "Ottmar", "Otto", "Pankraz", "Pascual", "Patrick", "Paul", "Peter", "Philipp", "Quirin", "Raban", "Raffael", "Raimund", "Ralf", "Randolf", "Reinhard", "Reinhold", "Remigius", "René", "Richard", "Robert", "Rochus", "Roland", "Roman", "Rüdiger", "Rupert", "Ruprecht", "Sander", "Sascha", "Sebastian", "Severin", "Sigismund", "Sigmund", "Silas", "Silvester", "Simon", "Stefan", "Steffen", "Sten", "Sven", "Thaddäus", "Theobald", "Theodor", "Thilo", "Thomas", "Thorsten", "Till", "Tim", "Timo", "Titus", "Tobias", "Torben", "Traugott", "Tristan", "Udo", "Ulrich", "Urban", "Valentin", "Valerius", "Veit", "Viktor", "Vinzenz", "Volker", "Walter", "Walther", "Wenzel", "Werner", "Wickbert", "Wigbert", "Wilfried", "Wilhelm", "Willibald", "Willi", "Winfried", "Wolf", "Wolfgang", "Wulf", "Wunibald", "Xaver", "Yannic", "Yannick", "Yuri", "Yves", "Zeno", "Zoltan"};
        String[] nachnamen = {"Bauer", "Müller", "Huber", "Maier", "Schulz", "Becker", "Koch", "Weber", "Schmitt", "Hoffmann", "Jung", "Klein", "Lang", "Braun", "Roth", "Keller", "Busch", "Vogel", "Fuchs", "Zimmermann", "Wagner", "Schuster", "Dietz", "Lehmann"};

        Random rnd = new Random();
        return vornamen[rnd.nextInt(vornamen.length)] + " " + nachnamen[rnd.nextInt(nachnamen.length)];
    }

    /**
     * Gibt dem Pawn ein Rüstungsstück. Jeder Pawn hat ein Slot für: Kopf, Torso, Arme und Beine
     * @param armor Rüstung, die angelegt werden soll.
     * @param slot Slot, an dem {@code armor} angelegt werden soll.
     * @return {@code true}, wenn die Rüstung erfolgreich ausgerüstet wurde, {@code false}, wenn der Slot bereits belegt ist
     */
    public boolean giveArmor(Armor armor, byte slot) {
        System.out.println(armor.getName());
        if (!clothingSlotUsed(slot)) {
            this.getArmors()[slot] = armor;
            armor.equip(this);
            System.out.println("giveArmor: " + armor.getOwner());
            return true;
        }
        return false;
    }

    /**
     * Gibt dem Pawn ein Rüstungsstück. Jeder Pawn hat ein Slot für: Kopf, Torso, Arme und Beine
     * @param ref Buttonelement, welches als Referenz mit übergeben wird.
     * @param armor Rüstung, die angelegt werden soll.
     * @param slot Slot, an dem {@code armor} angelegt werden soll.
     * @return {@code true}, wenn die Rüstung erfolgreich ausgerüstet wurde, {@code false}, wenn der Slot bereits belegt ist
     */
    public boolean giveArmor(StackPane ref, Armor armor, byte slot) {
        if (!clothingSlotUsed(slot)) {
            this.getArmors()[slot] = armor;
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
     * @param ref Referenz für den entsprechenden Button, der das aktualisierte Bild erhalten soll.
     * @param weapon Die Waffe, die ausgerüstet werden soll.
     * @param slot Der Waffenslot, an dem {@code weapon} ausgerüstet werden soll.
     * @return {@code true}, wenn die Waffe erfolgreich ausgerüstet wurde, {@code false}, wenn der Slot bereits belegt ist.
     */
    public boolean giveWeapon(StackPane ref, Weapon weapon, byte slot) {
        if (!weaponSlotUsed(slot)) {
            this.getWeapons()[slot] = weapon;
            Inspector.setImage(ref, weapon.getWeaponClass());
            weapon.equip(this);
            return true;
        }
        return false;
    }

    /**
     * Gibt dem Bauer eine Waffe. Jeder Bauer hat 2 Hände, in die er 2
     * verschiedene Waffen legen kann, oder eine große Waffe in beiden Händen tragen kann.
     *
     * @param weapon Die Waffe, die ausgerüstet werden soll.
     * @param slot Der Waffenslot, an dem {@code weapon} ausgerüstet werden soll.
     */
    public void giveWeapon(Weapon weapon, byte slot) {
        this.getWeapons()[slot] = weapon;
        weapon.equip(this);
    }

    /**
     * Gibt eine Waffe an einem Slot zurück
     * @param slot Slot, von dem die Waffe returned werden soll.
     * @return {@code Weapon} - Waffe, die am Slot angebracht wurde.
     */
    public Weapon getWeapon(byte slot) {
        return this.getWeapons()[slot];
    }

    /**
     * Entfernt eine Waffe von dem Pawn
     * @param w Waffe, die entfernt werden soll.
     * @return {@code true}, wenn die Waffe entfernt werden konnte.
     */
    public boolean removeWeapon(Weapon w) {
        for (byte b = 0; b <= 1; b++) {
            if (getWeapon(b) != null && getWeapon(b).equals(w)) {
                this.getWeapons()[b] = null;
                w.unequip();
                return true;
            }
        }
        return false;
    }

    /**
     * Entfernt ein Rüstungsteil von dem Pawn
     * @param a Armor, die entfernt werden soll.
     * @return {@code true}, wenn das Rüstungsteil entfernt werden konnte.
     */
    public boolean removeArmor(Armor a) {
        for (byte b = 0; b <= 3; b++) {
            if (getArmor(b) != null && getArmor(b).equals(a)) {
                this.getArmors()[b] = null;
                a.unequip();
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt ein Rüstungsteil an einem Slot zurück.
     * @param slot Slot, von dem das Rüstungsteil returned werden soll.
     * @return {@code Armor} - Rüstungsteil, das am Slot angebracht wurde.
     */
    public Armor getArmor(byte slot) {
        return this.getArmors()[slot];
    }

    /**
     * Prüft, ob ein Slot mit einer Rüstung belegt ist.
     * @param slotId Die ID des Slots, der geprüft werden soll:
     *               0 - Kopf
     *               1 - Torso
     *               2 - Arme
     *               3 - Beine
     * @return {@code true}, wenn der gefragte Slot belegt ist.
     */
    private boolean clothingSlotUsed(int slotId) {
        return this.getArmors()[slotId] != null;
    }

    /**
     * Prüft, ob ein Slot mit einer Waffe belegt ist.
     * @param slotId Die ID des Slots, der geprüft werden soll:
     *               0 - Hand rechts
     *               1 - Hand links
     * @return {@code true}, wenn der gefragte Slot belegt ist.
     */
    private boolean weaponSlotUsed(int slotId) {
        return this.getWeapons()[slotId] != null;
    }

    /**
     * Gibt den Namen des Pawns zurück.
     * @return {@code String}
     */
    public String getName() {
        return this.name;
    }

    /** Gibt die Skill des Pawns zurück
     * <br><br>
     * Skills: <br> <blockquote> 0 = health <br> 1 = damage <br> 2 = resistance <br> 3 = speed</blockquote>
     * @return {@code ArrayList<Skill>}
     */
    public ArrayList<Skill> getSkills() {
        return this.skills;
    }

    /**
     * Gibt die Skillpunkte des Pawns zurück
     * @return {@code int}
     */
    public int getSkillPoints() {
        return this.skillPoints;
    }

    /**
     * Fügt dem Pawn Skillpunkte hinzu.
     * @param num Anzahl der hinzuzufügenden Skillpunkte.
     */
    public void addSkillPoints(int num) {
        this.skillPoints += num;
    }

    /**
     * Fügt dem Pawn Erfahrungspunkte hinzu.
     * @param x Anzahl der Erfahrungspunkte, die der Pawn erhalten soll.
     * @return {@code IntegerBinding} - Level des Pawns.
     */
    public IntegerBinding addXp(int x) {
        if (this.xp == null) {
            initVars();
        }
        this.xp.set(this.xp.get() + x);
        return this.lvl;
    }

    /**
     * Gibt das Level des Pawns zurück.
     * @return {@code int}
     */
    public int getLvl() {
        if(this.lvl == null) {
            initVars();
        }
        return this.lvl.get();
    }

    /**
     * Gibt der Erfahrungspunkte des Pawns zurück.
     * @return {@code int}
     */
    public int getXpAsInt() {
        return this.xp.get();
    }

    /**
     * Setzt das Level des Pawns
     * <br><br>
     * Da das Level nun ein IntegerBinding ist, der an den xp "hängt", kann man das level nicht ohne weiteres einfach setzen.
     * Daher muss man mit den Erfahrungspunkten (xp) direkt arbeiten. Hierfür muss man also das Level erst in die benötigten
     * xp "umrechnen" und die xp selbst updaten. Das Level aktualisiert sich automatisch.
     * Da diese Methode eigentlich nur zur Generierung der Gegner genutzt wird, und diese immer mit dem StandardLevel (1) generiert
     * werden, ist es nicht nötig vorher abzufragen, wie viel xp der Pawn aktuell hat.
     *
     * @param l Level, welches gesetzt werden soll.
     */
    public void setLvl(int l) {
        if (l <= 1) return;
        int nextLvlXp = 50;
        for (int i = 1; i < l; i++) {
            addXp(nextLvlXp);
            nextLvlXp += 5;
        }
    }

    /**
     * Gibt den Fortschritt zum nächsten Level zurück
     * @return {@code DoubleProperty}
     */
    public DoubleProperty getLevelProgress() {
        return progress;
    }

    /**
     * Prüft, ob eine Pawn dem Spieler gehört.
     * @return {@code true}, wenn der Pawn im Inventar des Spielers enthalten ist, <br>
     * {@code false}, wenn es sich um einen Gegner handelt.
     */
    public boolean ownedByPlayer() {
        for(Pawn p : Inventory.getPawns()){
            if (this.equals(p)) return true;
        }
        return false;
    }

    /**
     * Errechnet den Schaden, der ein Pawn einem anderen, gegnerischen Pawn hinzufügt.
     *
     * <p>Berücksicht dabei:
     *  <ul>
     *  <li>den Schaden, die Waffe und Geschwindigkeit des Angreifers ("{@code this}"-Pawn)
     *  <li>die Leben, Rüstung, Verteidigung (inkl. Verteidigungsaktion in der Arena) und Geschwindigkeit des angegriffenen Pawns "{@code enemy}"
     *  </ul>
     * @param enemy {@code Pawn}, der angegriffen wird.
     * @return {@code int[]}, mit: <br> {@code int[0]} -> Schaden, <br> {@code int[1]} -> StreifschussParameter für Kampfkommentierung.
     */
    public int[] calcDamage(Pawn enemy) {
        double damage = 0;

        System.out.println("--------------------------------");

        /* Streifschuss von 0 ergibt 100% Trefferchance */
        double graze = 0.0;
        Double pSpeed = this.getSkills().get(3).getSkillValue();
        Double eSpeed = enemy.getSkills().get(3).getSkillValue();
        System.out.println("Geschwindigkeit des Angreifers: " + pSpeed);
        System.out.println("Geschwindigkeit des gegnerischen Pawns: " + eSpeed);
        if(pSpeed < eSpeed) {
            graze = 1 - (pSpeed / eSpeed);
            System.out.println("Grazewahrscheinlichkeit: " + graze);
            /* Damit die Wahrscheinlichkeit für einen Streifschuss nie über 70% geht: */
            graze = Math.min(graze, 0.70);
        }
        /* Damit auch der schnellere Kämpfer garantiert einmal einen Schlag versemmelt. */
        graze = Math.max(graze, 0.1);
        System.out.println("Fehltreffwahrscheinlichkeit: " + graze);
        /* Weiterhin wird der Angriffswert der Waffe mit dem Schadenswert des Angreifers zusammengefügt
         * und mit dem Rüstungswert des Gegners, sowie dessen Resistance Wert verglichen. */
        double weapondamage1 = this.getWeapon((byte) 0) != null ? this.getWeapon((byte) 0).getTotalDamage() : 0;
        double weapondamage2 = this.getWeapon((byte) 1) != null ? this.getWeapon((byte) 1).getTotalDamage() : 0;
        damage += this.getSkills().get(1).getSkillValue() * 2;
        System.out.println("Schadenswert des Angreifers: " + damage);
        damage += weapondamage1 + weapondamage2;
        System.out.println("Schadenswert mit Waffe: " + damage + " \tmit Waffenschaden:  " + weapondamage1 + " + " + weapondamage2);
        damage -= enemy.getTotalProtectionValue() * 0.1;
        damage = Math.max(damage, 0);
        System.out.println("Schadenswert abzüglich der Gegnerischen Rüstung: " + damage + " Mit einr Rüstungsstärrke von: " + enemy.getTotalProtectionValue());
        damage /= enemy.getSkills().get(2).getSkillValue() / 100 +1;
        System.out.println("Schadenswert mit Widerstand des Gegners " + damage);

        System.out.println("» Gesamter Berechneter Schaden: " + damage);

        /* Falls der fehlgeschlagene Angriff ein Streifschuss war, dann wird auf den gesamten vorher
         * berechneten Schaden eine Verringerung um 90% gelegt. Daher steht die Überprüfung
         * danach erst am Ende dieser Methode.
         * Bei einem kompletten Fehltreffer ist der Schaden einfach 0.
         * Zwischen Streifschuss und Fehltreffer wird einfach per 50/50 Wahrscheinlichkeit entschieden.
         * die graze-Variable wird hier recycelt, um ein Identifikator dafür zu erstellen, ob es ein Volltreffer,
         * Streifschuss oder Fehlschuss war.
         *
         */
        if(Math.random()<graze) {
            System.out.println("Streifschuss!");
            damage = Math.random() < 0.5 ? damage * 0.1 : 0.0;
            if(damage == 0.0) {
                graze = 2;
            } else {
                graze = 1;
            }
        } else {
            graze = 0;
        }

        System.out.println("Schaden nach Fehltreffer: " + damage);

        /* Der Schaden wird noch um +- 50% randomisiert, damit es spannend bleibt. */
        damage = Math.round(damage * (1+(0.5-Math.random())));
        System.out.println("Schaden nach Randomisierung: " + damage);
        System.out.println((int) graze + "--------------------------------");
        return new int[]{(int) damage, (int)graze} ;
    }

    /**
     * Gibt den gesamten Schutzwert eines Pawns zurück.
     * @return {@code int}
     */
    public int getTotalProtectionValue() {
        int val = 0;
        for(Armor a : this.getArmors()){
            if(a == null) continue;
            val += (int) Math.floor(a.getTotalProtection());
        }
        return val;
    }

    /**
     * Wird in der Arena benutzt, wenn der Pawn sich in die Defensive (Verteidigung) begibt
     * @param defense Ob der Pawn sich im Defensive Modus befindet oder nicht.
     */
    public void goInDefenseMode(boolean defense) {
        if(defense) {
            /* Verteidigung wird um 50% erhöht und Speed um 25% */
            this.getSkills().get(2).setMultiplier(1.5);
            this.getSkills().get(3).setMultiplier(1.25);
        } else  {
            this.getSkills().get(2).setMultiplier(1.0);
            this.getSkills().get(3).setMultiplier(1.0);
        }
    }

    /**
     * Dient der Initialisierung der Variablen, die nicht Serialisiert werden können.
     * Insbesondere nach Laden eines Spielstandes!
     */
    private void initVars() {
        /* Im folgenden Abschnitt wird mittels IntegerBinding und der dazugehörigen computeValue() Methode
         * das Level an die Erfahrungspunkte gebunden. Dabei braucht ein weiteres Level immer 5 Erfahrungspunkte (xp)
         * mehr, als das vorherige Level - beginnend bei 50 benötigten Erfahrungspunkten für Level 2.
         */
        this.progress = new SimpleDoubleProperty();
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

                progress.set((double) currentXP / requiredXP);
                return l;
            }

        };
        /* Da bei einem Integer Bindung das Compute Value nur bei "Benutzung" des Integer Values aufgerufen wird,
         * wird dieses hier einfach jedes mal abgefragt, wenn man XP erhält.
         */
        xp.addListener((observable, oldValue, newValue) -> {
            this.lvl.get();
        });
    }

    /**
     * Gibt die (angelegten) Rüstungen des Pawns zurück
     * @return {@code Armor[]}
     */
    public Armor[] getArmors() {
        return this.armors;
    }

    /**
     * Gibt die (angelegten) Waffen des Pawns zurück.
     * @return {@code Weapon[]}
     */
    public Weapon[] getWeapons() {
        return this.weapons;
    }

    /**
     * Prüft, ob der Pawn am aktellen Tag bereits gekämpft hat.
     * @return {@code true}, wenn der Pawn an dem aktuellen Spieltag bereits in der Arena gekämpft hat.
     */
    public boolean hashFoughtToday() {
        return foughtToday;
    }

    /**
     * Setzt die Variable, dass der Pawn am aktuellen Tag bereits gekämpft hat.
     * @param foughtToday Ob der Pawn schon an dem Spieltag in der Arena gekämpt hat {@code true}
     *                    oder nicht {@code false}.
     */
    public void setFoughtToday(boolean foughtToday) {
        this.foughtToday = foughtToday;
    }

    /**
     * Setzt die Id des Pawns, basierend auf der Id des letzten Pawns.
     */
    public void setId() {
        this.id = Inventory.getLastPawnId() + 1;
    }

    /**
     * Gibt die ID des Pawns zurück.
     * @return {@code int}
     */
    public int getId() {
        return this.id;
    }
}
