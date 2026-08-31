package com.hbm.items.machine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RTGPelletItem extends Item {

    private static final List<RTGPelletItem> PELLETS = new ArrayList<>();

    private final int heat;

    public RTGPelletItem(Properties properties, int heat) {
        super(properties.stacksTo(1));
        this.heat = heat;
        PELLETS.add(this);
    }

    public int getHeat() {
        return this.heat;
    }

    public int getPower() {
        return this.heat * 5;
    }

    public static List<RTGPelletItem> getPellets() {
        return Collections.unmodifiableList(PELLETS);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("desc.item.rtgHeat", this.heat).withStyle(ChatFormatting.YELLOW));
    }
}
