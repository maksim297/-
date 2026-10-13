package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(
        modid = BerserkOfGluttony.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class PerceptionEvents {

    private PerceptionEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updatePerception(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(
            PlayerEvent.PlayerRespawnEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updatePerception(player);
        }
    }

    public static double updatePerception(ServerPlayer player) {
        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        return StatEffects.getPerceptionBonus(stats);
    }
}
