package com.berserkofgluttony;

import java.util.List;

public final class ShopRegistry {

    private ShopRegistry() {}

    public static final List<ShopItem> ITEMS = List.of(

            // ЗБРОЯ
            new ShopItem(
                    "greed_sword",
                    "Меч Жадібності",
                    ShopCategory.WEAPONS,
                    500,
                    "Легендарний меч із силою Жадібності."
            ),

            new ShopItem(
                    "iron_sword",
                    "Залізний меч",
                    ShopCategory.WEAPONS,
                    50,
                    "Надійний меч для початківця."
            ),

            // ЇЖА
            new ShopItem(
                    "bread",
                    "Хліб",
                    ShopCategory.FOOD,
                    5,
                    "Звичайна їжа для відновлення голоду."
            ),

            new ShopItem(
                    "golden_apple",
                    "Золоте яблуко",
                    ShopCategory.FOOD,
                    100,
                    "Особлива їжа з корисними ефектами."
            ),

            // ЗІЛЛЯ
            new ShopItem(
                    "healing_potion",
                    "Зілля лікування",
                    ShopCategory.POTIONS,
                    30,
                    "Магічне зілля для відновлення здоров'я."
            ),

            new ShopItem(
                    "strength_potion",
                    "Зілля сили",
                    ShopCategory.POTIONS,
                    75,
                    "Зілля для посилення атаки."
            )
    );

    public static List<ShopItem> getItems(ShopCategory category) {
        return ITEMS.stream()
                .filter(item -> item.category() == category)
                .toList();
    }

    public static ShopItem getItem(String id) {
        return ITEMS.stream()
                .filter(item -> item.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
