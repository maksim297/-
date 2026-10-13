package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ShopManager {

    private ShopManager() {
    }

    public enum PurchaseResult {
        SUCCESS,
        ITEM_NOT_FOUND,
        NOT_ENOUGH_GOLD,
        INVENTORY_FULL,
        ITEM_NOT_IMPLEMENTED
    }

    public static PurchaseResult purchase(
            ServerPlayer player,
            String itemId
    ) {
        if (player == null || itemId == null) {
            return PurchaseResult.ITEM_NOT_FOUND;
        }

        ShopItem shopItem = ShopRegistry.getItem(itemId);

        if (shopItem == null) {
            return PurchaseResult.ITEM_NOT_FOUND;
        }

        Item item = getMinecraftItem(itemId);

        if (item == null) {
            return PurchaseResult.ITEM_NOT_IMPLEMENTED;
        }

        int price = shopItem.price();

        if (price < 0) {
            return PurchaseResult.ITEM_NOT_FOUND;
        }

        if (!GoldManager.hasEnoughGold(player, price)) {
            return PurchaseResult.NOT_ENOUGH_GOLD;
        }

        ItemStack stack = new ItemStack(item, 1);

        if (!GoldManager.spendGold(player, price)) {
            return PurchaseResult.NOT_ENOUGH_GOLD;
        }

        boolean added = player.getInventory().add(stack);

        if (!added || !stack.isEmpty()) {
            if (!stack.isEmpty()) {
                player.drop(stack, false);
            }

            GoldManager.addGold(player, price);
            return PurchaseResult.INVENTORY_FULL;
        }

        player.getInventory().setChanged();

        return PurchaseResult.SUCCESS;
    }

    private static Item getMinecraftItem(String itemId) {
        return switch (itemId) {
            case "greed_sword" -> ModItems.GREED_SWORD.get();
            case "iron_sword" -> Items.IRON_SWORD;
            case "bread" -> Items.BREAD;
            case "golden_apple" -> Items.GOLDEN_APPLE;
            case "healing_potion", "strength_potion" -> Items.POTION;
            default -> null;
        };
    }
}
