package com.hbm.inventory.material;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Auto-generates DeferredItem entries for every (material, shape) pair declared
 * in Mats.orderedList via NTMMaterial.setAutogen(...).
 *
 * Called from NtmItems.registerOther() so items are registered before the
 * DeferredRegister is submitted to the event bus.
 */
public class MatsItemGen {

    public static void init(DeferredRegister.Items items) {
        for (NTMMaterial mat : Mats.orderedList) {
            for (MaterialShapes shape : mat.autogen) {
                registerShape(items, mat, shape);
            }
        }
    }

    private static void registerShape(DeferredRegister.Items items, NTMMaterial mat, MaterialShapes shape) {
        if (shape.noAutogen) return;

        String registryName = shape.itemNameFor(mat); // e.g. "ingot_iron"
        if (registryName == null) return;

        DeferredItem<Item> item = items.register(registryName, () -> new Item(new Item.Properties()));
        mat.generatedItems.put(shape, item);
    }
}
