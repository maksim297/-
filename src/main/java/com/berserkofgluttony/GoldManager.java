package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;

public final class GoldManager {

    private GoldManager() {
    }

    /**
     * Повертає кількість золота гравця.
     */
    public static int getGold(ServerPlayer player) {
        if (player == null) {
            return 0;
        }

        return player.getData(
                ModAttachments.PLAYER_GOLD.get()
        ).getGold();
    }

    /**
     * Додає золото гравцеві.
     * Від'ємні та нульові значення ігноруються.
     */
    public static void addGold(ServerPlayer player, int amount) {
        if (player == null || amount <= 0) {
            return;
        }

        PlayerGold gold = player.getData(
                ModAttachments.PLAYER_GOLD.get()
        );

        long newBalance = (long) gold.getGold() + amount;

        gold.setGold(
                (int) Math.min(newBalance, Integer.MAX_VALUE)
        );

        player.setData(
                ModAttachments.PLAYER_GOLD.get(),
                gold
        );
    }

    /**
     * Списує золото, якщо його достатньо.
     *
     * @return true, якщо списання виконано.
     */
    public static boolean spendGold(ServerPlayer player, int amount) {
        if (player == null || amount <= 0) {
            return false;
        }

        PlayerGold gold = player.getData(
                ModAttachments.PLAYER_GOLD.get()
        );

        if (gold.getGold() < amount) {
            return false;
        }

        if (!gold.spendGold(amount)) {
            return false;
        }

        player.setData(
                ModAttachments.PLAYER_GOLD.get(),
                gold
        );

        return true;
    }

    /**
     * Встановлює баланс золота.
     * Від'ємне значення перетворюється на 0.
     */
    public static void setGold(ServerPlayer player, int amount) {
        if (player == null) {
            return;
        }

        PlayerGold gold = player.getData(
                ModAttachments.PLAYER_GOLD.get()
        );

        gold.setGold(Math.max(0, amount));

        player.setData(
                ModAttachments.PLAYER_GOLD.get(),
                gold
        );
    }

    /**
     * Перевіряє, чи вистачає золота на покупку.
     */
    public static boolean hasEnoughGold(
            ServerPlayer player,
            int amount
    ) {
        return player != null
                && amount > 0
                && getGold(player) >= amount;
    }
}
