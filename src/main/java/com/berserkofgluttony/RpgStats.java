package com.berserkofgluttony;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class RpgStats {

    public static final int BASE_STAT = 10;

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
                                    .forGetter(RpgStats::isAutoPointsAllocated)
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

    public RpgStats() {
        this(
                BASE_STAT,
                BASE_STAT,
                BASE_STAT,
                BASE_STAT,
                BASE_STAT,
                0,
                true
        );
    }

    public RpgStats(
            int strength,
            int endurance,
            int perception,
            int agility,
            int intelligence,
            int availablePoints,
            boolean autoPointsAllocated
    ) {
        this.strength = Math.max(0, strength);
        this.endurance = Math.max(0, endurance);
        this.perception = Math.max(0, perception);
        this.agility = Math.max(0, agility);
        this.intelligence = Math.max(0, intelligence);
        this.availablePoints = Math.max(0, availablePoints);
        this.autoPointsAllocated = autoPointsAllocated;
    }

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
