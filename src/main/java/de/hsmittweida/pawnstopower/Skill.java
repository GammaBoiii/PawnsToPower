package de.hsmittweida.pawnstopower;

// lol

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

/**
 * Skill Class, die das Fundament für das Skillbasierende Kämpfen bereitstellt.
 */
public class Skill implements Serializable {
    /**
     * Id des Skills als {@code String}
     */
    private final String id;
    /**
     * Name des Skills
     */
    private final String name;
    /**
     * Level des Skills
     */
    private byte level;
    /**
     * Basiswert des Skills. (StarterLevel)
     */
    private int baseVal; //transient final IntegerProperty baseVal;
    /**
     * Multiplikator auf den Skillwert.
     */
    private double multiplier;
    /**
     * Pawn, dem das Skill zugeordnet ist.
     */
    private Pawn pawn;

    /**
     * Konstruktor der Skill Klasse.
     *
     * @param id      Dient zur Eindeutigen Identifikation des Skills.
     * @param name    Name des Skills.
     * @param baseVal Standardwert des Skills.
     * @param pawn    Pawn, dem der SKill zugeordnet wird.
     */
    public Skill(String id, String name, int baseVal, Pawn pawn) {
        this.id = id;
        this.name = name;
        this.baseVal = baseVal;
        System.out.println("baseVal!!!!!!!!: " + baseVal);
        this.pawn = pawn;
        level = 0;
        multiplier = 1.0;
    }

    /**
     * Berechnet den Wert des Skills anhand aller Modifikatoren.
     *
     * @return {@code double}
     */
    public double getSkillValue() {
        double bV = baseVal;//.get();
        double lvlFactor = (((pawn.getLvl() - 1.0) * 5.0) / 100.0) * bV;
        return (bV * getFactor() + lvlFactor) * multiplier;
    }

    /**
     * Gibt den Basis-Wert des SKills zurück.
     *
     * @return {@code SimpleDoubleProperty} - Basiswert des Skills.
     * @deprecated
     */
    public SimpleDoubleProperty getBaseVal() {
        return new SimpleDoubleProperty(((9.0 + pawn.getLvl()) / 10.0) * getFactor() + baseVal);
    }

    /**
     * Gibt den Wert des Skills basierend auf dessen Level zurück.
     *
     * @return {@code double}
     */
    private double getFactor() {
        return 1.0 + (this.level * 0.05);
    }

    /**
     * Gibt den Level des Skills zurück.
     *
     * @return {@code int}
     */
    public int getSkillLevel() {
        return this.level;
    }

    /**
     * Fügt dem Skill einen Level hinzu.
     */
    public void addSkillLevel() {
        this.level++;
    }

    /**
     * Gibt die Id des Skills zurück.
     *
     * @return {@code String}
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gibt den Namen des Skill zurück.
     *
     * @return {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt dem Skill einen Multiplikator auf dessen Wert.
     *
     * @param multiplier Multiplikator auf die Skillwerte.
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
