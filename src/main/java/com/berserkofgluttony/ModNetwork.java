package com.berserkofgluttony;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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
                new Type<>(
                        ResourceLocation.fromNamespaceAndPath(
                                BerserkOfGluttony.MOD_ID,
                                "spend_point"
                        )
                );

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                SpendPointPayload
                > STREAM_CODEC = StreamCodec.composite(
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
                new Type<>(
                        ResourceLocation.fromNamespaceAndPath(
                                BerserkOfGluttony.MOD_ID,
                                "auto_distribution"
                        )
                );

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                AutoDistributionPayload
                > STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                AutoDistributionPayload::enabled,
                AutoDistributionPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
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
                RegisterPayloadHandlersEvent event
        ) {
            var registrar = event.registrar("1");

            registrar.playToServer(
                    SpendPointPayload.TYPE,
                    SpendPointPayload.STREAM_CODEC,
                    (payload, context) -> {
                        if (context.player()
                                instanceof ServerPlayer player) {
                            StatPoints.spendPoint(
                                    player,
                                    payload.statName()
                            );
                        }
                    }
            );

            registrar.playToServer(
                    AutoDistributionPayload.TYPE,
                    AutoDistributionPayload.STREAM_CODEC,
                    (payload, context) -> {
                        if (context.player()
                                instanceof ServerPlayer player) {
                            StatPoints.setAutomaticDistribution(
                                    player,
                                    payload.enabled()
                            );
                        }
                    }
            );
        }
    }

    public static void spendPoint(String statName) {
        PacketDistributor.sendToServer(
                new SpendPointPayload(statName)
        );
    }

    public static void setAutomaticDistribution(
            boolean enabled
    ) {
        PacketDistributor.sendToServer(
                new AutoDistributionPayload(enabled)
        );
    }
}
