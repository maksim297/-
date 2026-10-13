package com.berserkofgluttony;

public record ShopItem(
        String id,
        String name,
        ShopCategory category,
        int price,
        String description
) {
    public ShopItem {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID товару не може бути порожнім");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою");
        }

        if (category == null) {
            throw new IllegalArgumentException("Категорія не може бути порожньою");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною");
        }

        if (description == null) {
            description = "";
        }
    }
}
