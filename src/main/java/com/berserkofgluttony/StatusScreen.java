package com.berserkofgluttony;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;

public class StatusScreen extends Screen {

    private final Player player;
    private RpgStats stats;

    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 330;

    private boolean shopOpen = false;
    private ShopCategory selectedCategory = ShopCategory.WEAPONS;

    public StatusScreen(Player player) {
        super(Component.literal("СТАТУС ПЕРСОНАЖА"));
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        refreshStats();

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;

        addRenderableWidget(
                Button.builder(
                        Component.literal("СТАТУС"),
                        button -> {
                            shopOpen = false;
                            rebuildWidgets();
                        }
                ).bounds(left + 10, top + 10, 135, 20).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal("МАГАЗИН"),
                        button -> {
                            shopOpen = true;
                            rebuildWidgets();
                        }
                ).bounds(left + 155, top + 10, 135, 20).build()
        );

        if (!shopOpen) {
            int buttonX = left + 255;
            int y = top + 105;

            addStatButton("strength", buttonX, y);
            addStatButton("endurance", buttonX, y + 25);
            addStatButton("perception", buttonX, y + 50);
            addStatButton("agility", buttonX, y + 75);
            addStatButton("intelligence", buttonX, y + 100);

            addRenderableWidget(
                    Button.builder(
                            Component.literal(
                                    stats.isAutoPointsAllocated()
                                            ? "Авто: УВІМК."
                                            : "Авто: ВИМК."
                            ),
                            button -> {
                                boolean enabled =
                                        !stats.isAutoPointsAllocated();

                                ModNetwork.setAutomaticDistribution(enabled);
                                stats.setAutoPointsAllocated(enabled);
                                rebuildWidgets();
                            }
                    ).bounds(left + 10, top + 300, 130, 20).build()
            );
        } else {
            int categoryY = top + 42;

            addCategoryButton(
                    ShopCategory.WEAPONS, left + 10, categoryY
            );
            addCategoryButton(
                    ShopCategory.FOOD, left + 108, categoryY
            );
            addCategoryButton(
                    ShopCategory.POTIONS, left + 206, categoryY
            );

            int itemY = top + 78;

            for (ShopItem item :
                    ShopRegistry.getItems(selectedCategory)) {

                String itemId = item.id();

                addRenderableWidget(
                        Button.builder(
                                Component.literal(
                                        item.name()
                                                + " — "
                                                + item.price()
                                                + " золота"
                                ),
                                button -> purchaseItem(itemId)
                        ).bounds(left + 10, itemY, 280, 20).build()
                );

                itemY += 25;

                if (itemY > top + 275) {
                    break;
                }
            }
        }
    }

    private void addCategoryButton(
            ShopCategory category,
            int x,
            int y
    ) {
        addRenderableWidget(
                Button.builder(
                        Component.literal(category.getDisplayName()),
                        button -> {
                            selectedCategory = category;
                            rebuildWidgets();
                        }
                ).bounds(x, y, 88, 20).build()
        );
    }

    private void purchaseItem(String itemId) {
        ModNetwork.purchaseItem(itemId);
    }

    private void refreshStats() {
        stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );
    }

    private void addStatButton(
            String statName,
            int x,
            int y
    ) {
        addRenderableWidget(
                Button.builder(
                        Component.literal("+"),
                        button -> {
                            ModNetwork.spendPoint(statName);
                            button.active = false;
                        }
                ).bounds(x, y, 25, 20).build()
        );
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        renderBackground(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        refreshStats();

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        int x = left + 10;

        graphics.fill(
                left,
                top,
                left + PANEL_WIDTH,
                top + PANEL_HEIGHT,
                0xEE17191F
        );

        graphics.renderOutline(
                left,
                top,
                PANEL_WIDTH,
                PANEL_HEIGHT,
                0xFFB58A52
        );

        if (shopOpen) {
            graphics.drawCenteredString(
                    font,
                    Component.literal("МАГАЗИН")
                            .withStyle(ChatFormatting.GOLD),
                    width / 2,
                    top + 35,
                    0xFFFFFFFF
            );

            int gold = 0;

            if (player instanceof ServerPlayer serverPlayer) {
                gold = GoldManager.getGold(serverPlayer);
            }

            graphics.drawString(
                    font,
                    Component.literal(
                            "Всього золота: " + gold
                    ).withStyle(ChatFormatting.GOLD),
                    x + 5,
                    top + 290,
                    0xFFFFD700
            );
        } else {
            graphics.drawCenteredString(
                    font,
                    Component.literal("СТАТУС ПЕРСОНАЖА")
                            .withStyle(ChatFormatting.GOLD),
                    width / 2,
                    top + 35,
                    0xFFFFFFFF
            );

            int y = top + 60;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Ім'я: " + player.getName().getString()
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            y += 15;

            graphics.drawString(
                    font,
                    Component.literal(String.format(
                            java.util.Locale.ROOT,
                            "Здоров'я: %.1f / %.1f",
                            player.getHealth(),
                            player.getMaxHealth()
                    )),
                    x,
                    y,
                    0xFFFF7777
            );

            y += 15;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Броня: " + player.getArmorValue()
                    ),
                    x,
                    y,
                    0xFFCCCCCC
            );

            y += 15;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Рівень: " + player.experienceLevel
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            y += 15;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Досвід: "
                                    + Math.round(
                                            player.experienceProgress * 100
                                    )
                                    + "%"
                    ),
                    x,
                    y,
                    0xFF80FF80
            );

            y += 20;

            graphics.drawString(
                    font,
                    Component.literal("ХАРАКТЕРИСТИКИ")
                            .withStyle(ChatFormatting.GOLD),
                    x,
                    y,
                    0xFFFFD080
            );

            y += 17;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Сила: " + stats.getStrength()
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            y += 25;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Витривалість: " + stats.getEndurance()
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            y += 25;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Сприйняття: " + stats.getPerception()
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            y += 25;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Спритність: " + stats.getAgility()
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            y += 25;

            graphics.drawString(
                    font,
                    Component.literal(
                            "Інтелект: " + stats.getIntelligence()
                    ),
                    x,
                    y,
                    0xFFFFFFFF
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            "Вільні очки: "
                                    + stats.getAvailablePoints()
                    ).withStyle(ChatFormatting.GREEN),
                    x,
                    top + 265,
                    0xFF80FF80
            );

            graphics.drawString(
                    font,
                    Component.literal("Клас: Берсерк"),
                    x,
                    top + 280,
                    0xFFFF7777
            );

            graphics.drawString(
                    font,
                    Component.literal("Навичка: Поглинання"),
                    x,
                    top + 295,
                    0xFFB58AFF
            );
        }

        super.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
