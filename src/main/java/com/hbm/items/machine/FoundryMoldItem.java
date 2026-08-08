package com.hbm.items.machine;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.NTMMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FoundryMoldItem extends Item {

    private final int size;
    private final MaterialShapes shape;
    private final int count;

    public FoundryMoldItem(Properties properties, int size, MaterialShapes shape, int count) {
        super(properties);
        this.size = size;
        this.shape = shape;
        this.count = count;
    }

    public int getSize() {
        return this.size;
    }

    public int getCost() {
        return this.shape.q(this.count);
    }

    public ItemStack getOutput(NTMMaterial material) {
        return material.makeStack(this.shape, this.count);
    }
}
