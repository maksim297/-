package com.berserkofgluttony;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;

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

    public static boolean hasMatchingBattleRank(
            RpgStats stats,
            LivingEntity target
    ) {
        if (target instanceof Slime) {
            return stats.hasSlimeKillerRank();
        }

        if (target instanceof Zombie) {
            return stats.hasZombieKillerRank();
        }

        if (target instanceof EnderMan) {
            return stats.hasEnderKillerRank();
        }

        if (target instanceof Creeper) {
            return stats.hasCreeperKillerRank();
        }

        if (target instanceof Spider) {
            return stats.hasSpiderKillerRank();
        }

        if (target instanceof AbstractSkeleton) {
            return stats.hasSkeletonKillerRank();
        }

        return false;
    }

    public static float getBattleRankDamageMultiplier(
            RpgStats stats,
            LivingEntity target
    ) {
        return hasMatchingBattleRank(stats, target)
                ? 1.10F
                : 1.0F;
    }
}
