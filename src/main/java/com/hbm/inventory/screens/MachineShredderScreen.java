package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineShredderBlockEntity;
import com.hbm.inventory.menus.MachineShredderMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineShredderScreen extends InfoScreen<MachineShredderMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/shredder.png");

    // Energy gauge: empty frame lives at (8,18)-(24,106) in the main texture;
    // the green->blue "full" gradient used to fill it lives at (176,72)-(192,160).
    private static final int GAUGE_X = 8;
    private static final int GAUGE_Y = 18;
    private static final int GAUGE_W = 16;
    private static final int GAUGE_H = 88;
    private static final int GAUGE_SRC_X = 176;
    private static final int GAUGE_SRC_Y = 72;

    // Blade wear icons: displayed at (43,71) for the left blade and (79,71) for the right blade,
    // each 18x18. The lit (white/yellow/red) versions live in the reservoir area at x=176/194,
    // one row per wear state (y=0 white, y=18 yellow, y=36 red) - never drawn at their source spot.
    private static final int ICON_LEFT_X = 43;
    private static final int ICON_RIGHT_X = 79;
    private static final int ICON_Y = 71;
    private static final int ICON_SIZE = 18;
    private static final int ICON_SRC_LEFT_X = 176;
    private static final int ICON_SRC_RIGHT_X = 194;
    private static final int ICON_SRC_WHITE_Y = 0;
    private static final int ICON_SRC_YELLOW_Y = 18;
    private static final int ICON_SRC_RED_Y = 36;

    // Progress arrow: empty/dim version already drawn at its onscreen spot in the base texture;
    // the lit version lives in the reservoir and gets cropped left-to-right as progress advances.
    private static final int ARROW_X = 63;
    private static final int ARROW_Y = 90;
    private static final int ARROW_W = 34;
    private static final int ARROW_H = 13;
    private static final int ARROW_SRC_X = 176;
    private static final int ARROW_SRC_Y = 54;

    private final MachineShredderBlockEntity be;

    public MachineShredderScreen(MachineShredderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 233;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + GAUGE_X, this.topPos + GAUGE_Y, GAUGE_W, GAUGE_H, be.power, be.getMaxPower());

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int panelCenterX = (36 + 176) / 2; // center of the beige machine panel (excludes the left energy gauge)
        int titleX = panelCenterX - this.font.width(this.title) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        // Energy gauge: fill from the bottom up, cropping the top of the source gradient
        // so an empty machine shows nothing and a full machine shows the whole green->blue strip.
        int filled = (int) be.getPowerScaled(GAUGE_H);
        if (filled > 0) {
            guiGraphics.blit(
                    TEXTURE,
                    this.leftPos + GAUGE_X, this.topPos + GAUGE_Y + (GAUGE_H - filled),
                    GAUGE_SRC_X, GAUGE_SRC_Y + (GAUGE_H - filled),
                    GAUGE_W, filled
            );
        }

        // Blade indicators: light up the icon matching the blade's current wear state
        // (1=white/fresh, 2=yellow/worn, 3=red/almost broken), reading from the reservoir.
        // gear == 0 (no blade) draws nothing extra, leaving the dim default icon as-is.
        drawBladeIcon(guiGraphics, ICON_LEFT_X, ICON_SRC_LEFT_X, be.getGearLeft());
        drawBladeIcon(guiGraphics, ICON_RIGHT_X, ICON_SRC_RIGHT_X, be.getGearRight());

        // Progress arrow: fills left-to-right as the current shredding cycle advances,
        // like a furnace's burn/cook gauge. Empty at progress=0, fully lit at progress=processingSpeed.
        int arrowProgress = be.getProgressScaled(ARROW_W);
        if (arrowProgress > 0) {
            guiGraphics.blit(
                    TEXTURE,
                    this.leftPos + ARROW_X, this.topPos + ARROW_Y,
                    ARROW_SRC_X, ARROW_SRC_Y,
                    arrowProgress, ARROW_H
            );
        }
    }

    private void drawBladeIcon(GuiGraphics guiGraphics, int destX, int srcX, int gear) {
        int srcY = switch (gear) {
            case 1 -> ICON_SRC_WHITE_Y;
            case 2 -> ICON_SRC_YELLOW_Y;
            case 3 -> ICON_SRC_RED_Y;
            default -> -1; // no blade inserted, nothing to draw
        };

        if (srcY >= 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + destX, this.topPos + ICON_Y, srcX, srcY, ICON_SIZE, ICON_SIZE);
        }
    }
}
