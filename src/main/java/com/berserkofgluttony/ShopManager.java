package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;

public final class ShopManager {

    private ShopManager() {}

    public enum PurchaseResult {
        SUCCESS,
        ITEM_NOT_FOUND,
        NOT_ENOUGH_GOLD,
        INVENTORY_FULL
    }

    public static PurchaseResult purchase(
            ServerPlayer player,
            String itemId
    ) {
        if (player == null || itemId == null) {
            return PurchaseResult.ITEM_NOT_FOUND;
        }

        ShopItem item = ShopRegistry.getItem(itemId);

        if (item == null) {
            return PurchaseResult.ITEM_NOT_FOUND;
        }

        if (GoldManager.getGold(player) < item.price()) {
            return PurchaseResult.NOT_ENOUGH_GOLD;
        }

        return PurchaseResult.ITEM_NOT_FOUND;
    }
}
