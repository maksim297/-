package com.berserkofgluttony;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(
        modid = BerserkOfGluttony.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public class GluttonyEvents {

    private GluttonyEvents() {
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();

        if (!(target.level()
                instanceof net.minecraft.server.level.ServerLevel)) {
            return;
        }

        if (!(event.getSource().getEntity()
                instanceof ServerPlayer player)) {
            return;
        }

        if (target == player || target instanceof ServerPlayer) {
            return;
        }

        RpgStats stats =
                player.getData(ModAttachments.RPG_STATS.get());

        int experience = Math.max(
                5,
                Math.min(50, (int) Math.ceil(target.getMaxHealth()))
        );

        stats.addGluttonyExperience(experience);

        player.setData(
                ModAttachments.RPG_STATS.get(),
                stats
        );

        // Синхронізуємо актуальні характеристики, рівень і досвід.
        ModNetwork.syncStats(player);

        player.displayClientMessage(
                Component.literal(
                        "Обжерливість: +" + experience
                                + " досвіду | Рівень "
                                + stats.getGluttonyLevel()
                ),
                true
        );
    }
}
