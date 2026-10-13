package com.berserkofgluttony;

public class RpgStats {

    public static final int BASE_STAT = 10;

    private int strength = BASE_STAT;
    private int endurance = BASE_STAT;
    private int perception = BASE_STAT;
    private int agility = BASE_STAT;
    private int intelligence = BASE_STAT;

    private int availablePoints = 0;
    private boolean autoPointsAllocated = true;

    public int getStrength() {
        return strength;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getPerception() {
        return perception;
    }

    public int getAgility() {
        return agility;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public boolean isAutoPointsAllocated() {
        return autoPointsAllocated;
    }

    public void setAutoPointsAllocated(boolean enabled) {
        this.autoPointsAllocated = enabled;
    }

    public void addAvailablePoints(int points) {
        availablePoints = Math.max(0, availablePoints + points);
    }

    public boolean addStrength(int points) {
        if (points <= 0 || availablePoints < points) return false;
        strength += points;
        availablePoints -= points;
        return true;
    }

    public boolean addEndurance(int points) {
        if (points <= 0 || availablePoints < points) return false;
        endurance += points;
        availablePoints -= points;
        return true;
    }

    public boolean addPerception(int points) {
        if (points <= 0 || availablePoints < points) return false;
        perception += points;
        availablePoints -= points;
        return true;
    }

    public boolean addAgility(int points) {
        if (points <= 0 || availablePoints < points) return false;
        agility += points;
        availablePoints -= points;
        return true;
    }

    public boolean addIntelligence(int points) {
        if (points <= 0 || availablePoints < points) return false;
        intelligence += points;
        availablePoints -= points;
        return true;
    }

    public void distributeAutomaticPoints() {
        while (availablePoints > 0) {
            if (strength <= endurance
                    && strength <= perception
                    && strength <= agility
                    && strength <= intelligence) {
                strength++;
            } else if (endurance <= perception
                    && endurance <= agility
                    && endurance <= intelligence) {
                endurance++;
            } else if (perception <= agility
                    && perception <= intelligence) {
                perception++;
            } else if (agility <= intelligence) {
                agility++;
            } else {
                intelligence++;
            }

            availablePoints--;
        }
    }
}
