package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(
        modid = BerserkOfGluttony.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class CombatEvents {

    private CombatEvents() {
    }

    @SubscribeEvent
    public static void onLivingDamage(
            LivingDamageEvent.Pre event
    ) {
        if (!(event.getSource().getEntity()
                instanceof ServerPlayer player)) {
            return;
        }

        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        float bonus = (float)
                StatEffects.getStrengthDamageBonus(stats);

        if (bonus > 0.0F) {
            event.setNewDamage(
                    event.getNewDamage() + bonus
            );
        }
    }
}
