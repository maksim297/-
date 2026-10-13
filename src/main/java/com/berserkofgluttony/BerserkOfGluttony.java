
package com.berserkofgluttony;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BerserkOfGluttony.MOD_ID)
public class BerserkOfGluttony {

    public static final String MOD_ID = "berserkofgluttony";

    public BerserkOfGluttony(
            IEventBus modEventBus,
            ModContainer modContainer
    ) {
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        System.out.println(
                "Berserk Of Gluttony mod loaded!"
        );
    }
}
