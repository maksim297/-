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

    private static final int PANEL_WIDTH = 280;
    private static final int PANEL_HEIGHT = 310;

    public StatusScreen(Player player) {
        super(Component.translatable(
                "screen.berserkofgluttony.status"
        ));
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();

        refreshStats();

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        int buttonX = left + 235;
        int y = top + 132;

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
                ).bounds(left + 12, top + 275, 130, 20).build()
        );
    }

    private void refreshStats() {
        stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );
    }

    private void addStatButton(String statName, int x, int y) {
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
        renderBackground(graphics, mouseX, mouseY, partialTick);

        refreshStats();

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        int x = left + 12;
        int y = top + 12;

        graphics.fill(
                left, top,
                left + PANEL_WIDTH, top + PANEL_HEIGHT,
                0xEE17191F
        );

        graphics.renderOutline(
                left, top,
                PANEL_WIDTH, PANEL_HEIGHT,
                0xFFB58A52
        );

        graphics.drawCenteredString(
                font,
                Component.translatable(
                        "screen.berserkofgluttony.status"
                ).withStyle(ChatFormatting.GOLD),
                width / 2, y, 0xFFFFFFFF
        );

        y += 22;
        graphics.drawString(
                font,
                Component.literal(
                        "Ім'я: " + player.getName().getString()),
                x, y, 0xFFFFFFFF
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
                x, y, 0xFFFF7777
        );

        y += 15;
        graphics.drawString(
                font,
                Component.literal("Броня: " + player.getArmorValue()),
                x, y, 0xFFCCCCCC
        );

        y += 15;
        graphics.drawString(
                font,
                Component.literal("Рівень: " + player.experienceLevel),
                x, y, 0xFFFFFFFF
        );

        y += 15;
        graphics.drawString(
                font,
                Component.literal("Досвід: " +
                        Math.round(player.experienceProgress * 100) + "%"),
                x, y, 0xFF80FF80
        );

        y += 20;
        graphics.drawString(
                font,
                Component.literal("ХАРАКТЕРИСТИКИ")
                        .withStyle(ChatFormatting.GOLD),
                x, y, 0xFFFFD080
        );

        y += 17;
        graphics.drawString(
                font,
                Component.literal("Сила: " + stats.getStrength()),
                x, y, 0xFFFFFFFF
        );

        y += 25;
        graphics.drawString(
                font,
                Component.literal("Витривалість: " + stats.getEndurance()),
                x, y, 0xFFFFFFFF
        );

        y += 25;
        graphics.drawString(
                font,
                Component.literal("Сприйняття: " + stats.getPerception()),
                x, y, 0xFFFFFFFF
        );

        y += 25;
        graphics.drawString(
                font,
                Component.literal("Спритність: " + stats.getAgility()),
                x, y, 0xFFFFFFFF
        );

        y += 25;
        graphics.drawString(
                font,
                Component.literal("Інтелект: " + stats.getIntelligence()),
                x, y, 0xFFFFFFFF
        );

        graphics.drawString(
                font,
                Component.literal(
                        "Вільні очки: " + stats.getAvailablePoints()
                ).withStyle(ChatFormatting.GREEN),
                x, top + 250, 0xFF80FF80
        );

        graphics.drawString(
                font,
                Component.literal("Клас: Берсерк"),
                x, top + 265, 0xFFFF7777
        );

        graphics.drawString(
                font,
                Component.literal("Навичка: Поглинання"),
                x, top + 280, 0xFFB58AFF
        );

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
