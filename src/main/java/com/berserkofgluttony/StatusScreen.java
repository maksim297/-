
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
    private static final int BLUE = 0xFF55CCFF;
    private static final int YELLOW = 0xFFFFD84D;
    private static final int PURPLE = 0xFFC080FF;
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
                ).bounds(
                        left + 12, top + 12, 144, 22
                ).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal("МАГАЗИН"),
                        button -> {
                            shopOpen = true;
                            rebuildWidgets();
                        }
                ).bounds(
                        left + 164, top + 12, 144, 22
                ).build()
        );

        if (shopOpen) {
            addCategoryButton(
                    ShopCategory.WEAPONS,
                    left + 12, top + 48
            );
            addCategoryButton(
                    ShopCategory.FOOD,
                    left + 116, top + 48
            );
            addCategoryButton(
                    ShopCategory.POTIONS,
                    left + 220, top + 48
            );

            int itemY = top + 86;

            for (ShopItem item :
                    ShopRegistry.getItems(selectedCategory)) {

                String itemId = item.id();

                addRenderableWidget(
                        Button.builder(
                                Component.literal(
                                        item.name()
                                                + " | "
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
            int firstY = top + 160;
            int rowGap = 28;

            addStatButton(
                    "strength", buttonX, firstY
            );
            addStatButton(
                    "endurance", buttonX, firstY + rowGap
            );
            addStatButton(
                    "perception", buttonX, firstY + rowGap * 2
            );
            addStatButton(
                    "agility", buttonX, firstY + rowGap * 3
            );
            addStatButton(
                    "intelligence", buttonX, firstY + rowGap * 4
            );

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
                        Component.literal(
                                category.getDisplayName()
                        ),
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
        renderBackground(
                graphics, mouseX, mouseY, partialTick
        );

        refreshStats();

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        int x = left + 16;

        graphics.fill(
                left, top,
                left + PANEL_WIDTH, top + PANEL_HEIGHT,
                0xF0181820
        );

        graphics.renderOutline(
                left, top,
                PANEL_WIDTH, PANEL_HEIGHT,
                0xFFD2A64C
        );

        graphics.renderOutline(
                left + 4, top + 4,
                PANEL_WIDTH - 8, PANEL_HEIGHT - 8,
                0xFF59452C
        );

        graphics.fill(
                left + 8, top + 40,
                left + PANEL_WIDTH - 8, top + 42,
                GOLD
        );

        if (shopOpen) {
            renderShop(graphics, left, top, x);
        } else {
            renderStatus(graphics, left, top, x);
        }

        super.render(
                graphics, mouseX, mouseY, partialTick
        );
    }

    private void renderStatus(
            GuiGraphics graphics,
            int left,
            int top,
            int x
    ) {
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
                ),
                x,
                top + 53,
                WHITE
        );

        graphics.drawString(
                font,
                Component.literal("КЛАС: БЕРСЕРК")
                        .withStyle(ChatFormatting.RED),
                x + 150,
                top + 53,
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
                top + 81,
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
                top + 81,
                WHITE
        );

        float maxHealth = Math.max(
                1.0F, player.getMaxHealth()
        );

        int healthWidth = (int) (
                170 * Math.min(
                        1.0F,
                        player.getHealth() / maxHealth
                )
        );

        graphics.fill(
                x, top + 95,
                x + 170, top + 101,
                0xFF49272D
        );

        graphics.fill(
                x, top + 95,
                x + healthWidth, top + 101,
                RED
        );

        graphics.drawString(
                font,
                Component.literal(
                        "Броня: " + player.getArmorValue()
                ),
                x + 190,
                top + 81,
                0xFFB9D9FF
        );

        graphics.drawString(
                font,
                Component.literal(
                        "Рівень: " + player.experienceLevel
                ),
                x,
                top + 111,
                WHITE
        );

        graphics.drawString(
                font,
                Component.literal(
                        "Досвід: "
                                + Math.round(
                                        player.experienceProgress * 100
                                )
                                + "%"
                ),
                x + 120,
                top + 111,
                GREEN
        );

        graphics.fill(
                x, top + 130,
                left + PANEL_WIDTH - 16, top + 131,
                0xFF51412C
        );

        graphics.drawString(
                font,
                Component.literal("ХАРАКТЕРИСТИКИ")
                        .withStyle(ChatFormatting.GOLD),
                x,
                top + 138,
                GOLD
        );

        int rowStart = top + 160;
        int gap = 28;

        drawStat(
                graphics, "Сила",
                stats.getStrength(),
                x, rowStart, RED
        );

        drawStat(
                graphics, "Витривалість",
                stats.getEndurance(),
                x, rowStart + gap, GREEN
        );

        drawStat(
                graphics, "Сприйняття",
                stats.getPerception(),
                x, rowStart + gap * 2, BLUE
        );

        drawStat(
                graphics, "Спритність",
                stats.getAgility(),
                x, rowStart + gap * 3, YELLOW
        );

        drawStat(
                graphics, "Інтелект",
                stats.getIntelligence(),
                x, rowStart + gap * 4, PURPLE
        );

        graphics.drawString(
                font,
                Component.literal(
                        "Вільні очки: "
                                + stats.getAvailablePoints()
                ).withStyle(ChatFormatting.GREEN),
                x + 150,
                rowStart,
                GREEN
        );

        graphics.drawString(
                font,
                Component.literal("Навичка: Поглинання")
                        .withStyle(ChatFormatting.LIGHT_PURPLE),
                x + 150,
                rowStart + gap,
                0xFFD5A6FF
        );
    }

    private void drawStat(
            GuiGraphics graphics,
            String name,
            int value,
            int x,
            int y,
            int color
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
                        .withStyle(ChatFormatting.BOLD),
                x + 130,
                y,
                color
        );

        int barX = x + 158;
        int barY = y + 2;
        int barWidth = 88;
        int barHeight = 7;

        int safeValue = Math.max(0, value);
        int filledWidth = (int) (
                barWidth * Math.min(
                        1.0,
                        safeValue / 100.0
                )
        );

        graphics.fill(
                barX, barY,
                barX + barWidth, barY + barHeight,
                0xFF30303B
        );

        if (filledWidth > 0) {
            graphics.fill(
                    barX, barY,
                    barX + filledWidth, barY + barHeight,
                    color
            );
        }

        graphics.renderOutline(
                barX, barY,
                barWidth, barHeight,
                0xFF777786
        );
    }

    private void renderShop(
            GuiGraphics graphics,
            int left,
            int top,
            int x
    ) {
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

        graphics.drawString(
                font,
                Component.literal(
                        "Баланс золота буде синхронізовано окремо."
                ),
                x + 4,
                top + 306,
                GOLD
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
