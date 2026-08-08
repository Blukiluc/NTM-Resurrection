package com.hbm.items.machine;

import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FoundryScrapItem extends Item {

    public FoundryScrapItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(Item item, MaterialStack material) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = TagsUtil.getCustomData(stack);
        tag.putInt("material", material.material.id);
        tag.putInt("amount", material.amount);
        TagsUtil.putCustomData(stack, tag);
        return stack;
    }

    public static MaterialStack getMaterial(ItemStack stack) {
        if (!(stack.getItem() instanceof FoundryScrapItem)) return null;
        CompoundTag tag = TagsUtil.getCustomData(stack);
        NTMMaterial material = Mats.matById.get(tag.getInt("material"));
        int amount = tag.getInt("amount");
        return material == null || amount <= 0 ? null : new MaterialStack(material, amount);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        MaterialStack material = getMaterial(stack);
        if (material != null) {
            components.add(Component.translatable(material.material.getTranslationKey()).withStyle(ChatFormatting.GRAY));
            components.add(Component.literal(material.amount + " q").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
