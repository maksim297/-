
package com.berserkofgluttony;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachments {

    private ModAttachments() {
    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(
                    NeoForgeRegistries.ATTACHMENT_TYPES,
                    BerserkOfGluttony.MOD_ID
            );

    public static final Supplier<AttachmentType<RpgStats>> RPG_STATS =
            ATTACHMENT_TYPES.register(
                    "rpg_stats",
                    () -> AttachmentType.builder(RpgStats::new)
                            .serialize(RpgStats.CODEC)
                            .copyOnDeath()
                            .build()
            );
}
