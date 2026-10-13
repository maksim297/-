package com.berserkofgluttony;

public final class StatEffects {

    private StatEffects() {
    }

    public static double getStrengthDamageBonus(RpgStats stats) {
        return stats.getStrength() * 0.5D;
    }

    public static double getEnduranceHealthBonus(RpgStats stats) {
        return stats.getEndurance() * 2.0D;
    }

    public static double getPerceptionBonus(RpgStats stats) {
        return stats.getPerception() * 0.01D;
    }

    public static double getAgilityBonus(RpgStats stats) {
        return stats.getAgility() * 0.01D;
    }

    public static double getIntelligenceBonus(RpgStats stats) {
        return stats.getIntelligence() * 0.01D;
    }
}
