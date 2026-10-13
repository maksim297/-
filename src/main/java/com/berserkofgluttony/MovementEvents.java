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
public final class MovementEvents {

    private static final ResourceLocation AGILITY_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(
                    BerserkOfGluttony.MOD_ID,
                    "agility_speed"
            );

    private MovementEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateSpeedBonus(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(
            PlayerEvent.PlayerRespawnEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateSpeedBonus(player);
        }
    }

    public static void updateSpeedBonus(ServerPlayer player) {
        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        AttributeInstance movementSpeed =
                player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (movementSpeed == null) {
            return;
        }

        movementSpeed.removeModifier(AGILITY_SPEED_ID);

        double bonus =
                StatEffects.getAgilityBonus(stats);

        if (bonus > 0.0D) {
            movementSpeed.addPermanentModifier(
                    new AttributeModifier(
                            AGILITY_SPEED_ID,
                            bonus,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            );
        }
    }
}
