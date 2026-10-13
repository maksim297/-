package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;

public final class StatPoints {

    private StatPoints() {}

    public static boolean spendPoint(
            ServerPlayer player,
            String statName
    ) {
        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        if (stats.getAvailablePoints() < 1) {
            return false;
        }

        boolean success;

        switch (statName) {
            case "strength" ->
                    success = stats.addStrength(1);
            case "endurance" ->
                    success = stats.addEndurance(1);
            case "perception" ->
                    success = stats.addPerception(1);
            case "agility" ->
                    success = stats.addAgility(1);
            case "intelligence" ->
                    success = stats.addIntelligence(1);
            default -> {
                return false;
            }
        }

        if (!success) {
            return false;
        }

        player.setData(
                ModAttachments.RPG_STATS.get(),
                stats
        );

        HealthEvents.updateHealthBonus(player);
        MovementEvents.updateSpeedBonus(player);

        ModNetwork.syncStats(player);

        return true;
    }

    public static void setAutomaticDistribution(
            ServerPlayer player,
            boolean enabled
    ) {
        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        stats.setAutoPointsAllocated(enabled);

        if (enabled) {
            stats.distributeAutomaticPoints();
        }

        player.setData(
                ModAttachments.RPG_STATS.get(),
                stats
        );

        HealthEvents.updateHealthBonus(player);
        MovementEvents.updateSpeedBonus(player);

        ModNetwork.syncStats(player);
    }
}
