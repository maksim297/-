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

    public static ShopCategory fromId(String id) {
        if (id == null) {
            return null;
        }

        return switch (id.toLowerCase(java.util.Locale.ROOT)) {
            case "weapons" -> WEAPONS;
            case "food" -> FOOD;
            case "potions" -> POTIONS;
            default -> null;
        };
    }

    public String getId() {
        return switch (this) {
            case WEAPONS -> "weapons";
            case FOOD -> "food";
            case POTIONS -> "potions";
        };
    }
}
