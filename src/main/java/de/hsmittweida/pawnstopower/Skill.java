package de.hsmittweida.pawnstopower;

// lol

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

public class Skill implements Serializable {
    private final String id;
    private final String name;
    private byte level;
    private transient final IntegerProperty baseVal;
    private double multiplier;

    Pawn pawn;
    public Skill(String id, String name, int baseVal, Pawn pawn) {
        this.id = id;
        this.name = name;
        this.baseVal = new SimpleIntegerProperty(baseVal);
        this.pawn = pawn;
        level = 0;
        multiplier = 1.0;
    }


    /**
     * Gibt die (finalen) Stats des Pawns an, inkl. des Levelbonus und des Skill-Bonus.
     * <br> <br>
     * Funktioniert, indem zuerst jedes Level die Leben um 5 erhöht,
     * dann der Skillfactor damit multipliziert wird (aus {@code skillFactor})
     * und dann auf den Basiswert addiert wird.
     * Ein eventueller {@code multiplier} wird durch Kampfsituationen mit drauf multipliziert.
     * @return Den totalen Skill-Wert
     */
    public double getSkillValue() {
        return (((pawn.getLvl()-1) * 5) * getFactor() + baseVal.get()) * multiplier;
    }

    @Deprecated
    public SimpleDoubleProperty getBaseVal() {
        return new SimpleDoubleProperty(((9.0+pawn.getLvl()) / 10.0) * getFactor() + baseVal.get());
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

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
