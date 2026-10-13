package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

@EventBusSubscriber(
        modid = BerserkOfGluttony.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class PlayerLevelEvents {

    private PlayerLevelEvents() {
    }

    @SubscribeEvent
    public static void onLevelChange(
            PlayerXpEvent.LevelChange event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        int levelsGained = event.getLevels();

        if (levelsGained <= 0) {
            return;
        }

        int points = levelsGained * RpgStats.POINTS_PER_LEVEL;

        StatPoints.awardPoints(player, points);
    }
}
