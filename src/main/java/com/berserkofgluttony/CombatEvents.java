
package com.berserkofgluttony;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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

        LivingEntity target = event.getEntity();

        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        float damage = event.getNewDamage();

        // Бонус сили.
        damage += (float)
                StatEffects.getStrengthDamageBonus(stats);

        // Бонус звання: +10% проти відповідного моба.
        damage *= StatEffects.getBattleRankDamageMultiplier(
                stats,
                target
        );

        event.setNewDamage(damage);
    }
}
