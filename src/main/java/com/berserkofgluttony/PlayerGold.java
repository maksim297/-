package com.berserkofgluttony;

import com.mojang.serialization.Codec;

public class PlayerGold {
    public static final Codec<PlayerGold> CODEC =
            Codec.INT.xmap(
                    PlayerGold::new,
                    PlayerGold::getGold
            );

    private int gold;

    public PlayerGold() {
        this(0);
    }

    public PlayerGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    public void addGold(int amount) {
        if (amount > 0) {
            gold += amount;
        }
    }

    public boolean spendGold(int amount) {
        if (amount <= 0 || gold < amount) {
            return false;
        }

        gold -= amount;
        return true;
    }
}
