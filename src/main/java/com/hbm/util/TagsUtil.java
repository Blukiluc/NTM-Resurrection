package com.hbm.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class TagsUtil {

    public static boolean hasCustomData(ItemStack stack) { return stack.has(DataComponents.CUSTOM_DATA); }
    public static void putCustomData(ItemStack stack, CompoundTag newTag) { stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag)); }

    public static CompoundTag getCustomData(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : new CompoundTag();
    }
}
