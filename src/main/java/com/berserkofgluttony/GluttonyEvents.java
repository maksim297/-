package com.berserkofgluttony;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(
        modid = BerserkOfGluttony.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class GluttonyEvents {

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

        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        int experience = Math.max(
                5,
                Math.min(50, (int) Math.ceil(target.getMaxHealth()))
        );

        stats.addGluttonyExperience(experience);

        String unlockedRank = null;

        if (target instanceof Slime
                && stats.unlockBattleRank(
                        RpgStats.RANK_SLIME_KILLER)) {
            unlockedRank = "Вбивця слизу";
        } else if (target instanceof Zombie
                && stats.unlockBattleRank(
                        RpgStats.RANK_ZOMBIE_KILLER)) {
            unlockedRank = "Вбивця зомбі";
        } else if (target instanceof EnderMan
                && stats.unlockBattleRank(
                        RpgStats.RANK_ENDER_KILLER)) {
            unlockedRank = "Вбивця Ендер";
        } else if (target instanceof Creeper
                && stats.unlockBattleRank(
                        RpgStats.RANK_CREEPER_KILLER)) {
            unlockedRank = "Вбивця Кріпера";
        } else if (target instanceof Spider
                && stats.unlockBattleRank(
                        RpgStats.RANK_SPIDER_KILLER)) {
            unlockedRank = "Вбивця павуків";
        } else if (target instanceof AbstractSkeleton
                && stats.unlockBattleRank(
                        RpgStats.RANK_SKELETON_KILLER)) {
            unlockedRank = "Вбивця скелетів";
        }

        player.setData(
                ModAttachments.RPG_STATS.get(),
                stats
        );

        ModNetwork.syncStats(player);

        player.displayClientMessage(
                Component.literal(
                        "Обжерливість: +" + experience
                                + " досвіду | Рівень "
                                + stats.getGluttonyLevel()
                ),
                true
        );

        if (unlockedRank != null) {
            player.sendSystemMessage(
                    Component.literal(
                            "Нове бойове звання: "
                                    + unlockedRank + "!"
                    )
            );
        }
    }
}
