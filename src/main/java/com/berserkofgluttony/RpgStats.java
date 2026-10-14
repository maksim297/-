
package com.berserkofgluttony;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class RpgStats {

    public static final int BASE_STAT = 10;
    public static final int POINTS_PER_LEVEL = 5;
    public static final int GLUTTONY_BASE_XP_REQUIRED = 100;

    // Бойові звання: кожне має окремий прапорець.
    public static final int RANK_SLIME_KILLER = 1;
    public static final int RANK_ZOMBIE_KILLER = 1 << 1;
    public static final int RANK_ENDER_KILLER = 1 << 2;
    public static final int RANK_CREEPER_KILLER = 1 << 3;
    public static final int RANK_SPIDER_KILLER = 1 << 4;
    public static final int RANK_SKELETON_KILLER = 1 << 5;

    public static final Codec<RpgStats> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.fieldOf("strength")
                                    .orElse(BASE_STAT)
                                    .forGetter(RpgStats::getStrength),
                            Codec.INT.fieldOf("endurance")
                                    .orElse(BASE_STAT)
                                    .forGetter(RpgStats::getEndurance),
                            Codec.INT.fieldOf("perception")
                                    .orElse(BASE_STAT)
                                    .forGetter(RpgStats::getPerception),
                            Codec.INT.fieldOf("agility")
                                    .orElse(BASE_STAT)
                                    .forGetter(RpgStats::getAgility),
                            Codec.INT.fieldOf("intelligence")
                                    .orElse(BASE_STAT)
                                    .forGetter(RpgStats::getIntelligence),
                            Codec.INT.fieldOf("available_points")
                                    .orElse(0)
                                    .forGetter(RpgStats::getAvailablePoints),
                            Codec.BOOL.fieldOf("auto_points_allocated")
                                    .orElse(true)
                                    .forGetter(RpgStats::isAutoPointsAllocated),
                            Codec.INT.fieldOf("gluttony_experience")
                                    .orElse(0)
                                    .forGetter(RpgStats::getGluttonyExperience),
                            Codec.INT.fieldOf("gluttony_level")
                                    .orElse(1)
                                    .forGetter(RpgStats::getGluttonyLevel),
                            Codec.INT.fieldOf("battle_ranks")
                                    .orElse(0)
                                    .forGetter(RpgStats::getBattleRanks)
                    ).apply(instance, RpgStats::new)
            );

    public static final StreamCodec<ByteBuf, RpgStats> STREAM_CODEC =
            ByteBufCodecs.fromCodec(CODEC);

    private int strength;
    private int endurance;
    private int perception;
    private int agility;
    private int intelligence;

    private int availablePoints;
    private boolean autoPointsAllocated;

    private int gluttonyExperience;
    private int gluttonyLevel;

    private int battleRanks;

    public RpgStats() {
        this(BASE_STAT, BASE_STAT, BASE_STAT, BASE_STAT, BASE_STAT,
                0, true, 0, 1, 0);
    }

    // Сумісність зі старим конструктором.
    public RpgStats(
            int strength,
            int endurance,
            int perception,
            int agility,
            int intelligence,
            int availablePoints,
            boolean autoPointsAllocated
    ) {
        this(strength, endurance, perception, agility, intelligence,
                availablePoints, autoPointsAllocated, 0, 1, 0);
    }

    // Сумісність із попереднім форматом статистики.
    public RpgStats(
            int strength,
            int endurance,
            int perception,
            int agility,
            int intelligence,
            int availablePoints,
            boolean autoPointsAllocated,
            int gluttonyExperience,
            int gluttonyLevel
    ) {
        this(strength, endurance, perception, agility, intelligence,
                availablePoints, autoPointsAllocated,
                gluttonyExperience, gluttonyLevel, 0);
    }

    public RpgStats(
            int strength,
            int endurance,
            int perception,
            int agility,
            int intelligence,
            int availablePoints,
            boolean autoPointsAllocated,
            int gluttonyExperience,
            int gluttonyLevel,
            int battleRanks
    ) {
        this.strength = Math.max(0, strength);
        this.endurance = Math.max(0, endurance);
        this.perception = Math.max(0, perception);
        this.agility = Math.max(0, agility);
        this.intelligence = Math.max(0, intelligence);
        this.availablePoints = Math.max(0, availablePoints);
        this.autoPointsAllocated = autoPointsAllocated;
        this.gluttonyExperience = Math.max(0, gluttonyExperience);
        this.gluttonyLevel = Math.max(1, gluttonyLevel);

        int validRanks = RANK_SLIME_KILLER
                | RANK_ZOMBIE_KILLER
                | RANK_ENDER_KILLER
                | RANK_CREEPER_KILLER
                | RANK_SPIDER_KILLER
                | RANK_SKELETON_KILLER;

        this.battleRanks = Math.max(0, battleRanks) & validRanks;
    }

    public int getStrength() { return strength; }
    public int getEndurance() { return endurance; }
    public int getPerception() { return perception; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }

    public int getAvailablePoints() { return availablePoints; }
    public boolean isAutoPointsAllocated() { return autoPointsAllocated; }

    public int getGluttonyExperience() { return gluttonyExperience; }
    public int getGluttonyLevel() { return gluttonyLevel; }

    public int getBattleRanks() { return battleRanks; }

    public boolean hasBattleRank(int rank) {
        return (battleRanks & rank) != 0;
    }

    // Повертає true лише тоді, коли звання щойно відкрилося.
    public boolean unlockBattleRank(int rank) {
        int validRanks = RANK_SLIME_KILLER
                | RANK_ZOMBIE_KILLER
                | RANK_ENDER_KILLER
                | RANK_CREEPER_KILLER
                | RANK_SPIDER_KILLER
                | RANK_SKELETON_KILLER;

        if (rank == 0 || (rank & validRanks) != rank) {
            return false;
        }

        if (hasBattleRank(rank)) {
            return false;
        }

        battleRanks |= rank;
        return true;
    }

    public boolean hasSlimeKillerRank() {
        return hasBattleRank(RANK_SLIME_KILLER);
    }

    public boolean hasZombieKillerRank() {
        return hasBattleRank(RANK_ZOMBIE_KILLER);
    }

    public boolean hasEnderKillerRank() {
        return hasBattleRank(RANK_ENDER_KILLER);
    }

    public boolean hasCreeperKillerRank() {
        return hasBattleRank(RANK_CREEPER_KILLER);
    }

    public boolean hasSpiderKillerRank() {
        return hasBattleRank(RANK_SPIDER_KILLER);
    }

    public boolean hasSkeletonKillerRank() {
        return hasBattleRank(RANK_SKELETON_KILLER);
    }

    public int getGluttonyExperienceRequired() {
        return GLUTTONY_BASE_XP_REQUIRED
                + (gluttonyLevel - 1) * 50;
    }

    public void addGluttonyExperience(int amount) {
        if (amount <= 0) return;

        gluttonyExperience += amount;

        while (gluttonyExperience >= getGluttonyExperienceRequired()) {
            gluttonyExperience -= getGluttonyExperienceRequired();
            gluttonyLevel++;
        }
    }

    public void setAutoPointsAllocated(boolean enabled) {
        autoPointsAllocated = enabled;
    }

    public void addAvailablePoints(int points) {
        if (points <= 0) return;

        availablePoints += points;

        if (autoPointsAllocated) {
            distributeAutomaticPoints();
        }
    }

    public void awardLevelUpPoints() {
        addAvailablePoints(POINTS_PER_LEVEL);
    }

    public boolean addStrength(int points) {
        if (!canSpend(points)) return false;
        strength += points;
        availablePoints -= points;
        return true;
    }

    public boolean addEndurance(int points) {
        if (!canSpend(points)) return false;
        endurance += points;
        availablePoints -= points;
        return true;
    }

    public boolean addPerception(int points) {
        if (!canSpend(points)) return false;
        perception += points;
        availablePoints -= points;
        return true;
    }

    public boolean addAgility(int points) {
        if (!canSpend(points)) return false;
        agility += points;
        availablePoints -= points;
        return true;
    }

    public boolean addIntelligence(int points) {
        if (!canSpend(points)) return false;
        intelligence += points;
        availablePoints -= points;
        return true;
    }

    private boolean canSpend(int points) {
        return points > 0 && availablePoints >= points;
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
