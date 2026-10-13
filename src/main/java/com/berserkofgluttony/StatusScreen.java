
package com.berserkofgluttony;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class StatusScreen extends Screen {

    private final Player player;
    private final RpgStats stats;

    public StatusScreen(Player player) {
        super(Component.translatable(
                "screen.berserkofgluttony.status"
        ));

        this.player = player;
        this.stats = new RpgStats();
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        this.renderBackground(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        int panelWidth = 260;
        int panelHeight = 285;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;

        graphics.fill(
                left,
                top,
                left + panelWidth,
                top + panelHeight,
                0xEE17191F
        );

        graphics.renderOutline(
                left,
                top,
                panelWidth,
                panelHeight,
                0xFFB58A52
        );

        int x = left + 12;
        int y = top + 12;

        graphics.drawCenteredString(
                this.font,
                Component.translatable(
                        "screen.berserkofgluttony.status"
                ).withStyle(ChatFormatting.GOLD),
                this.width / 2,
                y,
                0xFFFFFF
        );

        y += 22;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Ім'я: " + player.getName().getString()
                ),
                x, y, 0xFFFFFFFF
        );

        y += 15;

        graphics.drawString(
                this.font,
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
                this.font,
                Component.literal(
                        "Броня: " + player.getArmorValue()
                ),
                x, y, 0xFFCCCCCC
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Рівень: " + player.experienceLevel
                ),
                x, y, 0xFFFFFFFF
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Досвід: " +
                        Math.round(player.experienceProgress * 100) +
                        "%"
                ),
                x, y, 0xFF80FF80
        );

        y += 20;

        graphics.drawString(
                this.font,
                Component.literal("ХАРАКТЕРИСТИКИ")
                        .withStyle(ChatFormatting.GOLD),
                x, y, 0xFFFFD080
        );

        y += 17;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Сила: " + stats.getStrength()
                ),
                x, y, 0xFFFFFFFF
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Витривалість: " + stats.getEndurance()
                ),
                x, y, 0xFFFFFFFF
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Сприйняття: " + stats.getPerception()
                ),
                x, y, 0xFFFFFFFF
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Спритність: " + stats.getAgility()
                ),
                x, y, 0xFFFFFFFF
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal(
                        "Інтелект: " + stats.getIntelligence()
                ),
                x, y, 0xFFFFFFFF
        );

        y += 20;

        graphics.drawString(
                this.font,
                Component.literal("Клас: Берсерк"),
                x, y, 0xFFFF7777
        );

        y += 15;

        graphics.drawString(
                this.font,
                Component.literal("Навичка: Поглинання"),
                x, y, 0xFFB58AFF
        );

        graphics.drawCenteredString(
                this.font,
                Component.literal("ESC — закрити")
                        .withStyle(ChatFormatting.GRAY),
                this.width / 2,
                top + panelHeight - 13,
                0xFFFFFFFF
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
