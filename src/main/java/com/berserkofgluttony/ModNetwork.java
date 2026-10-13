package com.berserkofgluttony;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class ModNetwork {

    private ModNetwork() {
    }

    public record SpendPointPayload(String statName)
            implements CustomPacketPayload {

        public static final Type<SpendPointPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(
                        BerserkOfGluttony.MOD_ID, "spend_point"));

        public static final StreamCodec<RegistryFriendlyByteBuf,
                SpendPointPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,
                        SpendPointPayload::statName,
                        SpendPointPayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record AutoDistributionPayload(boolean enabled)
            implements CustomPacketPayload {

        public static final Type<AutoDistributionPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(
                        BerserkOfGluttony.MOD_ID, "auto_distribution"));

        public static final StreamCodec<RegistryFriendlyByteBuf,
                AutoDistributionPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.BOOL,
                        AutoDistributionPayload::enabled,
                        AutoDistributionPayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record PurchaseItemPayload(String itemId)
            implements CustomPacketPayload {

        public static final Type<PurchaseItemPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(
                        BerserkOfGluttony.MOD_ID, "purchase_item"));

        public static final StreamCodec<RegistryFriendlyByteBuf,
                PurchaseItemPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,
                        PurchaseItemPayload::itemId,
                        PurchaseItemPayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record StatsSyncPayload(
            int strength,
            int endurance,
            int perception,
            int agility,
            int intelligence,
            int availablePoints,
            boolean autoPointsAllocated
    ) implements CustomPacketPayload {

        public static final Type<StatsSyncPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(
                        BerserkOfGluttony.MOD_ID, "stats_sync"));

        public static final StreamCodec<RegistryFriendlyByteBuf,
                StatsSyncPayload> STREAM_CODEC =
                new StreamCodec<RegistryFriendlyByteBuf,
                        StatsSyncPayload>() {

                    @Override
                    public StatsSyncPayload decode(
                            RegistryFriendlyByteBuf buf) {
                        return new StatsSyncPayload(
                                buf.readVarInt(),
                                buf.readVarInt(),
                                buf.readVarInt(),
                                buf.readVarInt(),
                                buf.readVarInt(),
                                buf.readVarInt(),
                                buf.readBoolean()
                        );
                    }

                    @Override
                    public void encode(
                            RegistryFriendlyByteBuf buf,
                            StatsSyncPayload payload) {
                        buf.writeVarInt(payload.strength());
                        buf.writeVarInt(payload.endurance());
                        buf.writeVarInt(payload.perception());
                        buf.writeVarInt(payload.agility());
                        buf.writeVarInt(payload.intelligence());
                        buf.writeVarInt(payload.availablePoints());
                        buf.writeBoolean(
                                payload.autoPointsAllocated());
                    }
                };

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static StatsSyncPayload from(RpgStats stats) {
            return new StatsSyncPayload(
                    stats.getStrength(),
                    stats.getEndurance(),
                    stats.getPerception(),
                    stats.getAgility(),
                    stats.getIntelligence(),
                    stats.getAvailablePoints(),
                    stats.isAutoPointsAllocated()
            );
        }

        public RpgStats toRpgStats() {
            return new RpgStats(
                    strength,
                    endurance,
                    perception,
                    agility,
                    intelligence,
                    availablePoints,
                    autoPointsAllocated
            );
        }
    }

    @EventBusSubscriber(
            modid = BerserkOfGluttony.MOD_ID,
            bus = EventBusSubscriber.Bus.MOD
    )
    public static final class Registration {

        private Registration() {
        }

        @SubscribeEvent
        public static void registerPayloads(
                RegisterPayloadHandlersEvent event) {

            var registrar = event.registrar("1");

            registrar.playToServer(
                    SpendPointPayload.TYPE,
                    SpendPointPayload.STREAM_CODEC,
                    (payload, context) -> {
                        if (context.player() instanceof ServerPlayer player) {
                            context.enqueueWork(() ->
                                    StatPoints.spendPoint(
                                            player,
                                            payload.statName()));
                        }
                    }
            );

            registrar.playToServer(
                    AutoDistributionPayload.TYPE,
                    AutoDistributionPayload.STREAM_CODEC,
                    (payload, context) -> {
                        if (context.player() instanceof ServerPlayer player) {
                            context.enqueueWork(() ->
                                    StatPoints.setAutomaticDistribution(
                                            player,
                                            payload.enabled()));
                        }
                    }
            );

            registrar.playToServer(
                    PurchaseItemPayload.TYPE,
                    PurchaseItemPayload.STREAM_CODEC,
                    (payload, context) -> {
                        if (context.player() instanceof ServerPlayer player) {
                            context.enqueueWork(() -> {
                                ShopManager.PurchaseResult result =
                                        ShopManager.purchase(
                                                player,
                                                payload.itemId()
                                        );

                                String message = switch (result) {
                                    case SUCCESS ->
                                            "Покупку виконано!";
                                    case ITEM_NOT_FOUND ->
                                            "Товар не знайдено.";
                                    case NOT_ENOUGH_GOLD ->
                                            "Недостатньо золота!";
                                    case INVENTORY_FULL ->
                                            "Не вдалося додати товар до інвентарю. Золото повернуто.";
                                    case ITEM_NOT_IMPLEMENTED ->
                                            "Цей товар ще не доступний.";
                                };

                                player.sendSystemMessage(
                                        Component.literal(message)
                                );
                            });
                        }
                    }
            );

            registrar.playToClient(
                    StatsSyncPayload.TYPE,
                    StatsSyncPayload.STREAM_CODEC,
                    (payload, context) ->
                            context.enqueueWork(() -> {
                                var player = context.player();

                                if (player != null) {
                                    player.setData(
                                            ModAttachments.RPG_STATS.get(),
                                            payload.toRpgStats()
                                    );
                                }
                            })
            );
        }
    }

    public static void spendPoint(String statName) {
        PacketDistributor.sendToServer(
                new SpendPointPayload(statName));
    }

    public static void setAutomaticDistribution(boolean enabled) {
        PacketDistributor.sendToServer(
                new AutoDistributionPayload(enabled));
    }

    public static void purchaseItem(String itemId) {
        PacketDistributor.sendToServer(
                new PurchaseItemPayload(itemId));
    }

    public static void syncStats(ServerPlayer player) {
        RpgStats stats = player.getData(
                ModAttachments.RPG_STATS.get());

        PacketDistributor.sendToPlayer(
                player,
                StatsSyncPayload.from(stats)
        );
    }
}
