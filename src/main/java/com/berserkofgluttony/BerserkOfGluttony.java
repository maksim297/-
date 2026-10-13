
package com.berserkofgluttony;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BerserkOfGluttony.MOD_ID)
public class BerserkOfGluttony {

    public static final String MOD_ID = "berserkofgluttony";

    public BerserkOfGluttony(
            FMLJavaModLoadingContext context
    ) {
        IEventBus modEventBus = context.getModEventBus();

        // Реєстрація RPG-характеристик гравця
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        System.out.println(
                "Berserk Of Gluttony mod loaded!"
        );
    }
}
