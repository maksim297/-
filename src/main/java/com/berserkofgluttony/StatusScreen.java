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

    public StatusScreen(Player player) {
        super(Component.translatable(
                "screen.berserkofgluttony.status"
        ));
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();

        stats = player.getData(
                ModAttachments.RPG_STATS.get()
        );

        int panelWidth = 280;
        int panelHeight = 310;
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;

        int buttonX = left + 220;
        int y = top + 104;

        addStatButton("strength", buttonX, y);
        y += 15;

        addStatButton("endurance", buttonX, y);
        y += 15;

        addStatButton("perception", buttonX, y);
        y += 15;

        addStatButton("agility", buttonX, y);
        y += 15;

        addStatButton("intelligence", buttonX, y);

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
                ).bounds(left + 12, top + 275, 130, 20).build()
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
                ).bounds(x, y, 25, 14).build()
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

        int panelWidth = 280;
        int panelHeight = 310;
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;

        graphics.fill(
                left, top,
                left + panelWidth,
                top + panelHeight,
                0xEE17191F
        );

        graphics.renderOutline(
                left, top,
                panelWidth, panelHeight,
                0xFFB58A52
        );

        int x = left + 12;
        int y = top + 12;

        graphics.drawCenteredString(
                font,
                Component.translatable(
                        "screen.berserkofgluttony.status"
                ).withStyle(ChatFormatting.GOLD),
                width / 2, y, 0xFFFFFF
        );

        y += 22;

        graphics.drawString(
                font,
                Component.literal(
                        "Ім'я: " + player.getName().getString()
                ),
                x, y, 0xFFFFFFFF
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        String.format(
                                java.util.Locale.ROOT,
                                "Здоров'я: %.1f / %.1f",
                                player.getHealth(),
                                player.getMaxHealth()
                        )
                ),
                x, y, 0xFFFF7777
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Броня: " + player.getArmorValue()
                ),
                x, y, 0xFFCCCCCC
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Рівень: " + player.experienceLevel
                ),
                x, y, 0xFFFFFFFF
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Досвід: " +
                        Math.round(player.experienceProgress * 100) +
                        "%"
                ),
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
                Component.literal(
                        "Сила: " + stats.getStrength()
                ),
                x, y, 0xFFFFFFFF
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Витривалість: " + stats.getEndurance()
                ),
                x, y, 0xFFFFFFFF
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Сприйняття: " + stats.getPerception()
                ),
                x, y, 0xFFFFFFFF
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Спритність: " + stats.getAgility()
                ),
                x, y, 0xFFFFFFFF
        );
        y += 15;

        graphics.drawString(
                font,
                Component.literal(
                        "Інтелект: " + stats.getIntelligence()
                ),
                x, y, 0xFFFFFFFF
        );
        y += 20;

        graphics.drawString(
                font,
                Component.literal(
                        "Вільні очки: " + stats.getAvailablePoints()
                ).withStyle(ChatFormatting.GREEN),
                x, y, 0xFF80FF80
        );

        graphics.drawString(
                font,
                Component.literal("Клас: Берсерк"),
                x, top + 250, 0xFFFF7777
        );

        graphics.drawString(
                font,
                Component.literal("Навичка: Поглинання"),
                x, top + 265, 0xFFB58AFF
        );

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
