package com.berserkofgluttony;

public enum ShopCategory {

    WEAPONS("Зброя"),
    FOOD("Їжа"),
    POTIONS("Зілля");

    private final String displayName;

    ShopCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
