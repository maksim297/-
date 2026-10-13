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

        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        stats.addAvailablePoints(
                levelsGained * RpgStats.POINTS_PER_LEVEL
        );

        player.setData(
                ModAttachments.RPG_STATS.get(),
                stats
        );
    }
}
