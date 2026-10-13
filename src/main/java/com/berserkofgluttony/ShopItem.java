package com.berserkofgluttony;

import java.util.Objects;

public record ShopItem(
        String id,
        String name,
        ShopCategory category,
        int price,
        String description
) {

    public ShopItem {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "ID товару не може бути порожнім"
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Назва товару не може бути порожньою"
            );
        }

        Objects.requireNonNull(
                category,
                "Категорія товару не може бути null"
        );

        if (price < 0) {
            throw new IllegalArgumentException(
                    "Ціна товару не може бути від'ємною"
            );
        }

        if (description == null) {
            description = "";
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ShopCategory getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFree() {
        return price == 0;
    }
}
