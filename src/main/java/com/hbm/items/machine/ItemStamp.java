package com.hbm.items.machine;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ItemStamp extends Item {

    protected final StampType type;

    public static final Map<StampType, List<ItemStack>> STAMPS = new EnumMap<>(StampType.class);

    public ItemStamp(Properties properties, StampType type) {
        super(properties);
        this.type = type;

        if (type != null) {
            addStampToList(this, type);
        }
    }

    protected void addStampToList(Item item, StampType type) {
        List<ItemStack> list = STAMPS.computeIfAbsent(type, k -> new ArrayList<>());
        list.add(new ItemStack(item));
    }

    public StampType getStampType() {
        return type;
    }

    public enum StampType {
        FLAT,
        PLATE,
        WIRE,
        CIRCUIT,
        C357,
        C44,
        C9,
        C50,
        PRINTING1,
        PRINTING2,
        PRINTING3,
        PRINTING4,
        PRINTING5,
        PRINTING6,
        PRINTING7,
        PRINTING8
    }
}