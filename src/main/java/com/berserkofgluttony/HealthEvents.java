package com.berserkofgluttony;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(
        modid = BerserkOfGluttony.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class HealthEvents {

    private static final ResourceLocation ENDURANCE_HEALTH_ID =
            ResourceLocation.fromNamespaceAndPath(
                    BerserkOfGluttony.MOD_ID,
                    "endurance_health"
            );

    private HealthEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateHealthBonus(player);
            ModNetwork.syncStats(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(
            PlayerEvent.PlayerRespawnEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateHealthBonus(player);
            ModNetwork.syncStats(player);
        }
    }

    public static void updateHealthBonus(ServerPlayer player) {
        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        AttributeInstance maxHealth =
                player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        maxHealth.removeModifier(ENDURANCE_HEALTH_ID);

        double bonus =
                StatEffects.getEnduranceHealthBonus(stats);

        if (bonus > 0.0D) {
            maxHealth.addPermanentModifier(
                    new AttributeModifier(
                            ENDURANCE_HEALTH_ID,
                            bonus,
                            AttributeModifier.Operation.ADD_VALUE
                    )
            );
        }

        player.setHealth(
                Math.min(player.getHealth(), player.getMaxHealth())
        );
    }
}
