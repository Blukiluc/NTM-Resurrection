package com.hbm.items.machine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemFELCrystal extends Item {

    public final EnumWavelengths wavelength;

    public ItemFELCrystal(Properties properties, EnumWavelengths wavelength) {
        super(properties.stacksTo(1));
        this.wavelength = wavelength;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if(this.wavelength == EnumWavelengths.DRX) {
            components.add(Component.literal("THERADIANCEOFATHOUSANDSUNS").withStyle(ChatFormatting.OBFUSCATED));
        } else {
            components.add(Component.translatable(this.getDescriptionId() + ".desc"));
        }

        components.add(Component.translatable(this.wavelength.name)
                .withStyle(this.wavelength.textColor)
                .append(" - ")
                .append(Component.translatable(this.wavelength.wavelengthRange).withStyle(this.wavelength.textColor)));
    }

    public enum EnumWavelengths {
        NULL("la creatura", "6 dollar", 0x010101, 0x010101, ChatFormatting.WHITE, 0F), //why do you exist?
        IR("wavelengths.name.ir", "wavelengths.waveRange.ir", 0xBB1010, 0xCC4040, ChatFormatting.RED, 0.75F),
        VISIBLE("wavelengths.name.visible", "wavelengths.waveRange.visible", 0, 0, ChatFormatting.GREEN, 1.4F),
        UV("wavelengths.name.uv", "wavelengths.waveRange.uv", 0x0A1FC4, 0x00EFFF, ChatFormatting.AQUA, 2.05F),
        GAMMA("wavelengths.name.gamma", "wavelengths.waveRange.gamma", 0x150560, 0xEF00FF, ChatFormatting.LIGHT_PURPLE, 2.7F),
        DRX("wavelengths.name.drx", "wavelengths.waveRange.drx", 0xFF0000, 0xFF0000, ChatFormatting.DARK_RED, 30.35F);

        public final String name;
        public final String wavelengthRange;
        public final int renderedBeamColor;
        public final int guiColor;
        public final ChatFormatting textColor;
        public final float waveFrequency;

        EnumWavelengths(String name, String wavelengthRange, int renderedBeamColor, int guiColor, ChatFormatting textColor, float waveFrequency) {
            this.name = name;
            this.wavelengthRange = wavelengthRange;
            this.renderedBeamColor = renderedBeamColor;
            this.guiColor = guiColor;
            this.textColor = textColor;
            this.waveFrequency = waveFrequency;
        }

        public int getBeamColor(long gameTime) {
            return this.renderedBeamColor == 0 ? Mth.hsvToRgb((gameTime % 50L) / 50F, 0.5F, 0.1F) : this.renderedBeamColor;
        }

        public int getGuiColor(long gameTime) {
            return this.guiColor == 0 ? Mth.hsvToRgb((gameTime % 50L) / 50F, 0.5F, 1F) : this.guiColor;
        }
    }
}
