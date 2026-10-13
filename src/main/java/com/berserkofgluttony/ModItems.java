package com.berserkofgluttony;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModItems {

    private ModItems() {}

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(
                    BerserkOfGluttony.MOD_ID
            );

    public static final Supplier<Item> GREED_SWORD =
            ITEMS.register(
                    "greed_sword",
                    () -> new SwordItem(
                            Tiers.DIAMOND,
                            new Item.Properties().attributes(
                                    SwordItem.createAttributes(
                                            Tiers.DIAMOND,
                                            3,
                                            -2.4F
                                    )
                            )
                    )
            );
}
