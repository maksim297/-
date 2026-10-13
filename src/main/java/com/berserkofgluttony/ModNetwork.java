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
                new StreamCodec<>() {
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
                        buf.writeBoolean(payload.autoPointsAllocated());
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
