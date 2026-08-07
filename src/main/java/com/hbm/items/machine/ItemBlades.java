package com.hbm.items.machine;

import com.hbm.main.NuclearTechMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Shredder blades. Wear down (lose durability) every time the Shredder processes an item.
 * Once depleted, the blades break and need to be replaced.
 */
public class ItemBlades extends Item {

    public ItemBlades(Properties properties, int dura) {
        super(properties.durability(dura));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item." + NuclearTechMod.MODID + ".blades.desc"));
    }
}
