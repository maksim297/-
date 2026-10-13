package com.berserkofgluttony;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class StatusScreen extends Screen {

    private final Player player;
    private RpgStats stats;

    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 350;

    private boolean shopOpen = false;
    private ShopCategory selectedCategory =
            ShopCategory.WEAPONS;

    private static final int GOLD = 0xFFFFD45A;
    private static final int WHITE = 0xFFF2F2F2;
    private static final int RED = 0xFFFF5555;
    private static final int GREEN = 0xFF70FF9A;
    private static final int MUTED = 0xFFB8B8C8;

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
                ).bounds(left + 12, top + 12, 144, 22).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal("МАГАЗИН"),
                        button -> {
                            shopOpen = true;
                            rebuildWidgets();
                        }
                ).bounds(left + 164, top + 12, 144, 22).build()
        );

        if (shopOpen) {
            addCategoryButton(
                    ShopCategory.WEAPONS, left + 12, top + 48
            );
            addCategoryButton(
                    ShopCategory.FOOD, left + 116, top + 48
            );
            addCategoryButton(
                    ShopCategory.POTIONS, left + 220, top + 48
            );

            int itemY = top + 86;

            for (ShopItem item :
                    ShopRegistry.getItems(selectedCategory)) {
                String itemId = item.id();

                addRenderableWidget(
                        Button.builder(
                                Component.literal(
                                        item.name() + "  |  "
                                                + item.price()
                                                + " золота"
                                ),
                                button ->
                                        ModNetwork.purchaseItem(itemId)
                        ).bounds(
                                left + 12, itemY, 296, 22
                        ).build()
                );

                itemY += 27;

                if (itemY > top + 270) {
                    break;
                }
            }
        } else {
            int buttonX = left + 274;
            int y = top + 150;

            addStatButton("strength", buttonX, y);
            addStatButton("endurance", buttonX, y + 28);
            addStatButton("perception", buttonX, y + 56);
            addStatButton("agility", buttonX, y + 84);
            addStatButton("intelligence", buttonX, y + 112);

            addRenderableWidget(
                    Button.builder(
                            Component.literal(
                                    stats.isAutoPointsAllocated()
                                            ? "АВТОРОЗПОДІЛ: УВІМК."
                                            : "АВТОРОЗПОДІЛ: ВИМК."
                            ),
                            button -> {
                                boolean enabled =
                                        !stats.isAutoPointsAllocated();

                                ModNetwork.setAutomaticDistribution(
                                        enabled
                                );
                                stats.setAutoPointsAllocated(enabled);
                                rebuildWidgets();
                            }
                    ).bounds(
                            left + 12, top + 315, 200, 22
                    ).build()
            );
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
                ).bounds(x, y, 88, 22).build()
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
                ).bounds(x, y, 24, 20).build()
        );
    }

    private void refreshStats() {
        stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        refreshStats();

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        int x = left + 16;

        // Темна панель
        graphics.fill(
                left, top,
                left + PANEL_WIDTH, top + PANEL_HEIGHT,
                0xF0181820
        );

        // Золота зовнішня рамка
        graphics.renderOutline(
                left, top,
                PANEL_WIDTH, PANEL_HEIGHT,
                0xFFD2A64C
        );

        // Внутрішня рамка
        graphics.renderOutline(
                left + 4, top + 4,
                PANEL_WIDTH - 8, PANEL_HEIGHT - 8,
                0xFF59452C
        );

        // Верхня декоративна смуга
        graphics.fill(
                left + 8, top + 40,
                left + PANEL_WIDTH - 8, top + 42,
                GOLD
        );

        if (shopOpen) {
            graphics.drawCenteredString(
                    font,
                    Component.literal("МАГАЗИН")
                            .withStyle(ChatFormatting.GOLD),
                    width / 2,
                    top + 76,
                    GOLD
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            "Обери предмет для купівлі"
                    ).withStyle(ChatFormatting.GRAY),
                    x + 4,
                    top + 292,
                    MUTED
            );

            // Баланс золота синхронізуємо окремим кроком.
            graphics.drawString(
                    font,
                    Component.literal(
                            "Золото: перевір баланс у чаті/на сервері"
                    ),
                    x + 4,
                    top + 306,
                    GOLD
            );
        } else {
            graphics.drawCenteredString(
                    font,
                    Component.literal("СТАТУС ПЕРСОНАЖА")
                            .withStyle(
                                    ChatFormatting.BOLD,
                                    ChatFormatting.GOLD
                            ),
                    width / 2,
                    top + 25,
                    GOLD
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            player.getName().getString()
                    ).withStyle(ChatFormatting.WHITE),
                    x,
                    top + 54,
                    WHITE
            );

            graphics.drawString(
                    font,
                    Component.literal("КЛАС: БЕРСЕРК")
                            .withStyle(ChatFormatting.RED),
                    x + 150,
                    top + 54,
                    RED
            );

            graphics.fill(
                    x, top + 70,
                    left + PANEL_WIDTH - 16, top + 71,
                    0xFF51412C
            );

            graphics.drawString(
                    font,
                    Component.literal("ЗДОРОВ'Я"),
                    x,
                    top + 82,
                    RED
            );

            graphics.drawString(
                    font,
                    Component.literal(String.format(
                            java.util.Locale.ROOT,
                            "%.1f / %.1f",
                            player.getHealth(),
                            player.getMaxHealth()
                    )),
                    x + 100,
                    top + 82,
                    WHITE
            );

            float maxHealth = Math.max(
                    1.0F, player.getMaxHealth()
            );
            int healthWidth = (int) (
                    170 * Math.min(
                            1.0F, player.getHealth() / maxHealth
                    )
            );

            graphics.fill(
                    x, top + 96,
                    x + 170, top + 102,
                    0xFF49272D
            );
            graphics.fill(
                    x, top + 96,
                    x + healthWidth, top + 102,
                    RED
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            "Броня: " + player.getArmorValue()
                    ),
                    x + 190,
                    top + 82,
                    0xFFB9D9FF
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            "Рівень: " + player.experienceLevel
                    ),
                    x,
                    top + 113,
                    WHITE
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            "Досвід: "
                                    + Math.round(
                                            player.experienceProgress
                                                    * 100
                                    )
                                    + "%"
                    ),
                    x + 120,
                    top + 113,
                    GREEN
            );

            graphics.fill(
                    x, top + 132,
                    left + PANEL_WIDTH - 16, top + 133,
                    0xFF51412C
            );

            graphics.drawString(
                    font,
                    Component.literal("ХАРАКТЕРИСТИКИ")
                            .withStyle(ChatFormatting.GOLD),
                    x,
                    top + 140,
                    GOLD
            );

            drawStat(
                    graphics, "Сила",
                    stats.getStrength(), x, top + 169
            );
            drawStat(
                    graphics, "Витривалість",
                    stats.getEndurance(), x, top + 197
            );
            drawStat(
                    graphics, "Сприйняття",
                    stats.getPerception(), x, top + 225
            );
            drawStat(
                    graphics, "Спритність",
                    stats.getAgility(), x, top + 253
            );
            drawStat(
                    graphics, "Інтелект",
                    stats.getIntelligence(), x, top + 281
            );

            graphics.drawString(
                    font,
                    Component.literal(
                            "Вільні очки: "
                                    + stats.getAvailablePoints()
                    ).withStyle(ChatFormatting.GREEN),
                    x + 150,
                    top + 169,
                    GREEN
            );

            graphics.drawString(
                    font,
                    Component.literal("Навичка: Поглинання")
                            .withStyle(ChatFormatting.LIGHT_PURPLE),
                    x + 150,
                    top + 197,
                    0xFFD5A6FF
            );
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawStat(
            GuiGraphics graphics,
            String name,
            int value,
            int x,
            int y
    ) {
        graphics.drawString(
                font,
                Component.literal(name),
                x,
                y,
                WHITE
        );

        graphics.drawString(
                font,
                Component.literal(String.valueOf(value))
                        .withStyle(ChatFormatting.GOLD),
                x + 150,
                y,
                GOLD
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

