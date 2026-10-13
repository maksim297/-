
package com.berserkofgluttony;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;

public class StatusScreen extends Screen {

    private final Player player;

    public StatusScreen(Player player) {
        super(Component.literal("Статус персонажа"));
        this.player = player;
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        int panelWidth = 240;
        int panelHeight = 230;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;

        graphics.fill(
                left, top,
                left + panelWidth, top + panelHeight,
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
                this.font,
                Component.literal("СТАТУС ПЕРСОНАЖА")
                        .withStyle(ChatFormatting.GOLD),
                this.width / 2,
                y,
                0xFFFFFF
        );

        y += 22;

        graphics.drawString(
                this.font,
                Component.literal("Ім'я: " + player.getName().getString()),
                x, y, 0xFFFFFF
        );

        y += 15;
        graphics.drawString(
                this.font,
                Component.literal("Здоров'я: "
                        + String.format("%.1f", player.getHealth())
                        + " / "
                        + String.format("%.1f", player.getMaxHealth())),
                x, y, 0xFFFF7777
        );

        y += 15;
        graphics.drawString(
                this.font,
                Component.literal("Броня: " + player.getArmorValue()),
                x, y, 0xFFCCCCCC
        );

        y += 15;
        graphics.drawString(
                this.font,
                Component.literal("Рівень: " + player.experienceLevel),
                x, y, 0xFFFFFF
        );

        y += 15;
        graphics.drawString(
                this.font,
                Component.literal("Досвід: "
                        + Math.round(player.experienceProgress * 100)
                        + "%"),
                x, y, 0xFF80FF80
        );

        y += 20;
        graphics.drawString(this.font, "ХАРАКТЕРИСТИКИ", x, y, 0xFFFFD080);

        y += 15;
        graphics.drawString(this.font, "Сила", x, y, 0xFFFFFFFF);
        graphics.drawString(this.font, "Витривалість", x + 100, y, 0xFFFFFFFF);

        y += 15;
        graphics.drawString(this.font, "Сприйняття", x, y, 0xFFFFFFFF);
        graphics.drawString(this.font, "Спритність", x + 100, y, 0xFFFFFFFF);

        y += 15;
        graphics.drawString(this.font, "Інтелект", x, y, 0xFFFFFFFF);

        y += 18;
        graphics.drawString(this.font, "Клас: Берсерк", x, y, 0xFFFF7777);

        y += 15;
        graphics.drawString(this.font, "Навичка: Поглинання", x, y, 0xFFB58AFF);

        graphics.drawCenteredString(
                this.font,
                Component.literal("ESC — закрити")
                        .withStyle(ChatFormatting.GRAY),
                this.width / 2,
                top + panelHeight - 13,
                0xFFFFFF
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
