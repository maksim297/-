package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;

public final class GoldManager {

    private GoldManager() {}

    public static int getGold(ServerPlayer player) {
        if (player == null) {
            return 0;
        }

        return player.getData(
                ModAttachments.PLAYER_GOLD.get()
        ).getGold();
    }

    public static void addGold(ServerPlayer player, int amount) {
        if (player == null || amount <= 0) {
            return;
        }

        PlayerGold gold = player.getData(
                ModAttachments.PLAYER_GOLD.get()
        );

        gold.addGold(amount);

        player.setData(
                ModAttachments.PLAYER_GOLD.get(),
                gold
        );
    }

    public static boolean spendGold(ServerPlayer player, int amount) {
        if (player == null || amount <= 0) {
            return false;
        }

        PlayerGold gold = player.getData(
                ModAttachments.PLAYER_GOLD.get()
        );

        if (!gold.spendGold(amount)) {
            return false;
        }

        player.setData(
                ModAttachments.PLAYER_GOLD.get(),
                gold
        );

        return true;
    }

    public static void setGold(ServerPlayer player, int amount) {
        if (player == null) {
            return;
        }

        PlayerGold gold = player.getData(
                ModAttachments.PLAYER_GOLD.get()
        );

        gold.setGold(amount);

        player.setData(
                ModAttachments.PLAYER_GOLD.get(),
                gold
        );
    }
}
