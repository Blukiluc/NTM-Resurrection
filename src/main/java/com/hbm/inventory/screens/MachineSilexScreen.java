package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineSilexBlockEntity;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.menus.MachineSilexMenu;
import com.hbm.inventory.recipes.SilexRecipes;
import com.hbm.items.machine.ItemFELCrystal.EnumWavelengths;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

import java.util.List;

public class MachineSilexScreen extends InfoScreen<MachineSilexMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/silex.png");
    private static final int WAVE_LEFT = 81;
    private static final int WAVE_TOP = 36;
    private static final int WAVE_WIDTH = 84;
    private static final int WAVE_HEIGHT = 20;
    private static final int WAVE_SEGMENTS = 96;

    private final MachineSilexBlockEntity be;

    public MachineSilexScreen(MachineSilexMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if(this.be.mode != EnumWavelengths.NULL && this.be.getLevel() != null) {
            float cycles = this.be.mode.waveFrequency;
            int color = 0xFF000000 | this.be.mode.getGuiColor(this.be.getLevel().getGameTime());
            double phase = (this.be.getLevel().getGameTime() + partialTicks) * 0.18D;
            this.drawSmoothWave(guiGraphics, WAVE_LEFT, WAVE_TOP, WAVE_WIDTH, WAVE_HEIGHT, cycles, phase, color);
            if(this.be.mode == EnumWavelengths.DRX) {
                this.drawSmoothWave(guiGraphics, WAVE_LEFT, WAVE_TOP, WAVE_WIDTH, WAVE_HEIGHT, cycles, phase, color);
            }
        }

        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 42, 52, 7);

        if(!this.be.current.isEmpty()) {
            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 27, this.topPos + 72, 16, 52, mouseX, mouseY,
                    List.of(
                            Component.literal(this.be.currentFill + "/" + MachineSilexBlockEntity.MAX_FILL + "mB"),
                            this.be.current.getHoverName()
                    ));
        }

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 10, this.topPos + 92, 10, 10, mouseX, mouseY,
                Component.translatable("gui.hbm.silex.void"));

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.checkClick(mouseX, mouseY, 10, 92, 12, 12)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("void", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2 - 54, 8, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);

        if(this.be.mode != EnumWavelengths.NULL) {
            Component wavelength = Component.translatable(this.be.mode.name).withStyle(this.be.mode.textColor);
            guiGraphics.drawString(this.font, wavelength, 132 - this.font.width(wavelength) / 2, 16, this.be.mode.textColor.getColor(), false);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.tank.getFill() > 0) {
            boolean valid = this.be.tank.getTankType() == Fluids.PEROXIDE || SilexRecipes.INSTANCE.hasFluidInput(this.be.tank.getTankType());
            guiGraphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 41, 176, valid ? 118 : 109, 54, 9);
        }

        int progress = this.be.getProgressScaled(69);
        guiGraphics.blit(TEXTURE, this.leftPos + 45, this.topPos + 82, 176, 0, progress, 43);

        int fill = this.be.getFillScaled(52);
        guiGraphics.blit(TEXTURE, this.leftPos + 26, this.topPos + 124 - fill, 176, 109 - fill, 16, fill);

        int fluid = this.be.getFluidScaled(52);
        guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 42, 176, this.be.tank.getTankType() == Fluids.PEROXIDE ? 43 : 50, fluid, 7);
    }

    private void drawSmoothWave(GuiGraphics guiGraphics, int x, int y, int width, int height, float cycles, double phase, int color) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0F, 0F, 0F);
        PoseStack.Pose pose = guiGraphics.pose().last();
        VertexConsumer buffer = guiGraphics.bufferSource().getBuffer(RenderType.guiOverlay());
        float left = this.leftPos + x + 1.5F;
        float top = this.topPos + y;
        float drawableWidth = width - 3F;
        float centerY = top + height * 0.5F;
        float amplitude = (height - 5F) * 0.5F;

        this.drawWaveLayer(buffer, pose.pose(), left, centerY, drawableWidth, amplitude, cycles, phase,0.75F, color);

        guiGraphics.pose().popPose();
        guiGraphics.flush();
    }

    private void drawWaveLayer(VertexConsumer buffer, Matrix4f pose, float left, float centerY, float width, float amplitude, float cycles, double phase, float thickness, int color) {
        float previousX = left;
        float previousY = waveY(centerY, amplitude, cycles, phase, 0F);

        for(int i = 1; i <= WAVE_SEGMENTS; i++) {
            float progress = i / (float) WAVE_SEGMENTS;
            float currentX = left + width * progress;
            float currentY = waveY(centerY, amplitude, cycles, phase, progress);
            addThickLineSegment(buffer, pose, previousX, previousY, currentX, currentY, thickness, color);
            previousX = currentX;
            previousY = currentY;
        }
    }

    private static float waveY(float centerY, float amplitude, float cycles, double phase, float progress) {
        double angle = phase + progress * cycles * Math.PI * 2D;
        return centerY + (float) (Math.sin(angle) * amplitude);
    }

    private static void addThickLineSegment(VertexConsumer buffer, Matrix4f pose, float x1, float y1, float x2, float y2, float thickness, int color) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if(length <= 0F) return;

        float normalX = -deltaY / length * thickness * 0.5F;
        float normalY = deltaX / length * thickness * 0.5F;

        buffer.addVertex(pose, x1 - normalX, y1 - normalY, 0F).setColor(color);
        buffer.addVertex(pose, x1 + normalX, y1 + normalY, 0F).setColor(color);
        buffer.addVertex(pose, x2 + normalX, y2 + normalY, 0F).setColor(color);
        buffer.addVertex(pose, x2 - normalX, y2 - normalY, 0F).setColor(color);
    }

    private static int withAlpha(int color, int alpha) {
        return alpha << 24 | color & 0x00FFFFFF;
    }
}
