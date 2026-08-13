package com.hbm.items.machine;

import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.trait.FT_Combustible.FuelGrade;
import com.hbm.items.EnumMultiItem;
import com.hbm.util.EnumUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class PistonSetItem extends EnumMultiItem {

    public PistonSetItem(Properties properties) {
        super(properties, PistonType.class, true, true);
    }

    public static PistonType getType(ItemStack stack) {
        return EnumUtil.grabEnumSafely(PistonType.class, MetaHelper.getMeta(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        PistonType type = getType(stack);

        components.add(Component.literal("Fuel efficiency:").withStyle(ChatFormatting.YELLOW));
        for(int i = 0; i < type.efficiency.length; i++) {
            components.add(
                    Component.literal("-" + FuelGrade.values()[i].getGrade() + ": ")
                            .withStyle(ChatFormatting.YELLOW)
                            .append(Component.literal((int) (type.efficiency[i] * 100) + "%").withStyle(ChatFormatting.RED))
            );
        }
    }

    public enum PistonType {
        STEEL(1.00, 0.75, 0.25, 0.00, 0.00),
        DURA(0.50, 1.00, 0.90, 0.50, 0.00),
        DESH(0.00, 0.50, 1.00, 0.75, 0.00),
        STARMETAL(0.50, 0.75, 1.00, 0.90, 0.50);

        public final double[] efficiency;

        PistonType(double... efficiency) {
            this.efficiency = new double[Math.min(FuelGrade.values().length, efficiency.length)];
            System.arraycopy(efficiency, 0, this.efficiency, 0, this.efficiency.length);
        }

        public double getEfficiency(FuelGrade grade) {
            return this.efficiency[grade.ordinal()];
        }
    }
}
