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

    private static final int PANEL_WIDTH = 340;
    private static final int PANEL_HEIGHT = 360;

    private enum Page {
        STATUS,
        SHOP,
        GROUP,
        QUESTS,
        CRAFTING,
        REWARDS,
        SKILLS
    }

    private Page currentPage = Page.STATUS;
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

        addTab("Статус", Page.STATUS,
                left + 10, top + 10, 100);
        addTab("Магазин", Page.SHOP,
                left + 120, top + 10, 100);
        addTab("Група", Page.GROUP,
                left + 230, top + 10, 100);

        addTab("Квести", Page.QUESTS,
                left + 10, top + 36, 100);
        addTab("Ремесло", Page.CRAFTING,
                left + 120, top + 36, 100);
        addTab("Нагороди", Page.REWARDS,
                left + 230, top + 36, 100);

        addTab("Навички", Page.SKILLS,
                left + 10, top + 62, 100);

        if (currentPage == Page.STATUS) {
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

                                ModNetwork.setAutomaticDistribution(
                                        enabled
                                );

                                stats.setAutoPointsAllocated(enabled);
                                rebuildWidgets();
                            }
                    ).bounds(
                            left + 12, top + 328, 150, 20
                    ).build()
            );
        }

        if (currentPage == Page.SHOP) {
            addCategoryButton(
                    ShopCategory.WEAPONS,
                    left + 12, top + 105
            );
            addCategoryButton(
                    ShopCategory.FOOD,
                    left + 122, top + 105
            );
            addCategoryButton(
                    ShopCategory.POTIONS,
                    left + 232, top + 105
            );

            int itemY = top + 140;

            for (ShopItem item :
                    ShopRegistry.getItems(selectedCategory)) {
                String itemId = item.id();

                addRenderableWidget(
                        Button.builder(
                                Component.literal(
                                        item.name() + " — "
                                                + item.price()
                                                + " золота"
                                ),
                                button ->
                                        ModNetwork.purchaseItem(itemId)
                        ).bounds(
                                left + 12, itemY, 316, 22
                        ).build()
                );

                itemY += 27;

                if (itemY > top + 275) {
                    break;
                }
            }
        }
    }

    private void addTab(
            String title,
            Page page,
            int x,
            int y,
            int buttonWidth
    ) {
        addRenderableWidget(
                Button.builder(
                        Component.literal(title),
                        button -> {
                            currentPage = page;
                            rebuildWidgets();
                        }
                ).bounds(x, y, buttonWidth, 20).build()
        );
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
                ).bounds(x, y, 96, 20).build()
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
                left + 8, top + 91,
                left + PANEL_WIDTH - 8, top + 93,
                GOLD
        );

        graphics.drawCenteredString(
                font,
                Component.literal("СТАТУС ПЕРСОНАЖА")
                        .withStyle(
                                ChatFormatting.BOLD,
                                ChatFormatting.GOLD
                        ),
                width / 2,
                top + 80,
                GOLD
        );

        switch (currentPage) {
            case STATUS ->
                    renderStatus(graphics, left, top, x);
            case SHOP ->
                    renderShop(graphics, left, top, x);
            case GROUP ->
                    renderInfoPage(
                            graphics, left, top,
                            "ГРУПА",
                            "Система груп буде додана окремо."
                    );
            case QUESTS ->
                    renderInfoPage(
                            graphics, left, top,
                            "КВЕСТИ",
                            "Система завдань буде додана окремо."
                    );
            case CRAFTING ->
                    renderInfoPage(
                            graphics, left, top,
                            "РЕМЕСЛО",
                            "Рецепти створення будуть додані окремо."
                    );
            case REWARDS ->
                    renderInfoPage(
                            graphics, left, top,
                            "НАГОРОДИ",
                            "Система нагород буде додана окремо."
                    );
            case SKILLS ->
                    renderInfoPage(
                            graphics, left, top,
                            "НАВИЧКИ",
                            "Навички Берсерка будуть додані окремо."
                    );
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderStatus(
            GuiGraphics graphics,
            int left,
            int top,
            int x
    ) {
        graphics.drawString(
                font,
                Component.literal("Ім'я: "
                        + player.getName().getString()),
                x, top + 103, WHITE
        );

        graphics.drawString(
                font,
                Component.literal("Звання: Берсерк")
                        .withStyle(ChatFormatting.RED),
                x + 170, top + 103, RED
        );

        graphics.drawString(
                font,
                Component.literal("Рівень: "
                        + player.experienceLevel),
                x, top + 120, WHITE
        );

        graphics.drawString(
                font,
                Component.literal("Досвід: "
                        + Math.round(
                                player.experienceProgress * 100
                        ) + "%"),
                x + 170, top + 120, GREEN
        );

        graphics.drawString(
                font,
                Component.literal(String.format(
                        java.util.Locale.ROOT,
                        "Здоров'я: %.1f / %.1f",
                        player.getHealth(),
                        player.getMaxHealth()
                )),
                x, top + 137, RED
        );

        graphics.drawString(
                font,
                Component.literal("Броня: "
                        + player.getArmorValue()),
                x + 170, top + 137, BLUE
        );

        graphics.drawString(
                font,
                Component.literal("ХАРАКТЕРИСТИКИ")
                        .withStyle(ChatFormatting.GOLD),
                x, top + 157, GOLD
        );

        int y = top + 177;
        int gap = 27;

        drawStat(graphics, "Сила",
                stats.getStrength(), x, y, RED);
        drawStat(graphics, "Витривалість",
                stats.getEndurance(), x, y + gap, GREEN);
        drawStat(graphics, "Сприйняття",
                stats.getPerception(), x, y + gap * 2, BLUE);
        drawStat(graphics, "Спритність",
                stats.getAgility(), x, y + gap * 3, YELLOW);
        drawStat(graphics, "Інтелект",
                stats.getIntelligence(), x, y + gap * 4, PURPLE);

        graphics.drawString(
                font,
                Component.literal("Вільні очки: "
                        + stats.getAvailablePoints())
                        .withStyle(ChatFormatting.GREEN),
                x + 150, top + 177, GREEN
        );

        graphics.drawString(
                font,
                Component.literal("Навичка: Поглинання")
                        .withStyle(ChatFormatting.LIGHT_PURPLE),
                x + 150, top + 204, PURPLE
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
                x, y, WHITE
        );

        graphics.drawString(
                font,
                Component.literal(String.valueOf(value))
                        .withStyle(ChatFormatting.BOLD),
                x + 112, y, color
        );

        int barX = x + 145;
        int barY = y + 2;
        int barWidth = 90;
        int barHeight = 7;

        int filledWidth = (int) (
                barWidth * Math.min(
                        1.0,
                        Math.max(0, value) / 100.0
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
        graphics.drawString(
                font,
                Component.literal("Обери предмет для купівлі"),
                x, top + 285, WHITE
        );

        graphics.drawString(
                font,
                Component.literal(
                        "Баланс золота буде синхронізовано окремо."
                ).withStyle(ChatFormatting.GOLD),
                x, top + 306, GOLD
        );
    }

    private void renderInfoPage(
            GuiGraphics graphics,
            int left,
            int top,
            String title,
            String message
    ) {
        graphics.drawCenteredString(
                font,
                Component.literal(title)
                        .withStyle(ChatFormatting.GOLD),
                width / 2,
                top + 125,
                GOLD
        );

        graphics.drawCenteredString(
                font,
                Component.literal(message)
                        .withStyle(ChatFormatting.GRAY),
                width / 2,
                top + 155,
                MUTED
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
