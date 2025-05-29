package de.hsmittweida.pawnstopower;

// lol

public class Skill {
    private final String id;
    private final String name;
    private byte level;
    private final int baseVal;

    Pawn pawn;
    public Skill(String id, String name, int baseVal, Pawn pawn) {
        this.id = id;
        this.name = name;
        this.baseVal = baseVal;
        this.pawn = pawn;
        level = 0;
    }

    /**
     * Gibt die (finalen) Stats des Pawns an, inkl. des Levelbonus und des Skill-Bonus.
     * <br> <br>
     * Funktioniert, indem zuerst jedes Level ab level 2 um den Factor 0.1 erhöht wird <em>(Level 2 -> *1.1,
     * Level 3 -> *1.2, Level 4 -> *1.3,...)</em>, dann der Skillfactor damit multipliziert wird (aus {@code skillFactor})
     * und dann auf den Basiswert addiert wird.
     * @return Den totalen Skill-Wert
     */
    public double getSkillValue() {
        return ((9.0+pawn.getLvl()) / 10.0) * getFactor() + baseVal;
    }

    private double getFactor() {
        return 1.0 + (this.level * 0.2);
    }

    public int getSkillLevel() {
        return this.level;
    }

    public void addSkillLevel() {
        this.level++;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }
}
