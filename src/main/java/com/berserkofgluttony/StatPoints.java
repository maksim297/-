package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;

public final class StatPoints {

    private StatPoints() {
    }

    public static boolean spendPoint(
            ServerPlayer player,
            String statName
    ) {
        if (player == null || statName == null) {
            return false;
        }

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

        updatePlayerEffects(player);
        ModNetwork.syncStats(player);

        return true;
    }

    public static void setAutomaticDistribution(
            ServerPlayer player,
            boolean enabled
    ) {
        if (player == null) {
            return;
        }

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

        updatePlayerEffects(player);
        ModNetwork.syncStats(player);
    }

    public static void awardPoints(
            ServerPlayer player,
            int points
    ) {
        if (player == null || points <= 0) {
            return;
        }

        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        stats.addAvailablePoints(points);

        player.setData(
                ModAttachments.RPG_STATS.get(),
                stats
        );

        updatePlayerEffects(player);
        ModNetwork.syncStats(player);
    }

    private static void updatePlayerEffects(ServerPlayer player) {
        HealthEvents.updateHealthBonus(player);
        MovementEvents.updateSpeedBonus(player);
    }
}
