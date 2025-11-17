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
    private int baseVal; //transient final IntegerProperty baseVal;
    private double multiplier;

    Pawn pawn;
    public Skill(String id, String name, int baseVal, Pawn pawn) {
        this.id = id;
        this.name = name;
        this.baseVal = baseVal; //new SimpleIntegerProperty(baseVal);
        System.out.println("baseVal!!!!!!!!: " + baseVal);
        this.pawn = pawn;
        level = 0;
        multiplier = 1.0;
    }

    public double getSkillValue() {
        double bV = baseVal;//.get();
        double lvlFactor = (((pawn.getLvl()-1.0) * 5.0) / 100.0) * bV;
        return (bV * getFactor() + lvlFactor ) * multiplier;
    }

    @Deprecated
    public SimpleDoubleProperty getBaseVal() {
        return new SimpleDoubleProperty(((9.0+pawn.getLvl()) / 10.0) * getFactor() + baseVal);//.get());
    }

    private double getFactor() {
        return 1.0 + (this.level * 0.05);
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
