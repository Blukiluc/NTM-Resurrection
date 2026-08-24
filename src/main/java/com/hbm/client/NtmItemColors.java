package com.hbm.client;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.main.NuclearTechMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Registers the dynamic tint ("IItemColor") for every auto-generated material item
 * (ingot, dust, plate, wire, block...).
 *
 * By default, materials sharing a shape reuse ONE white/grey template texture for that
 * shape (see NtmItemModelProvider, item/template/<shape>.png), colorized here at render
 * time from NTMMaterial#solidColorLight - the same mechanism vanilla uses for tinted
 * leaves or dyed leather armor.
 *
 * If a hand-made texture exists at item/{itemName}.png (e.g. item/ingot_uranium.png),
 * NtmItemModelProvider points that item's model straight at it instead of the template,
 * so it must NOT be tinted here - this class mirrors that same "does a custom texture
 * exist" check (see #hasCustomTexture) to stay in sync with the model provider.
 *
 * IMPORTANT: this must be registered on the MOD (client) event bus, not the game/common
 * bus, since RegisterColorHandlersEvent.Item only fires client-side. Call
 * NtmItemColors.register(modEventBus) from your client-only setup (e.g. the client-side
 * mod constructor / FMLClientSetupEvent listener registration), NOT from common setup.
 */
public class NtmItemColors {

    // Cache: itemName -> whether a hand-made item/{itemName}.png exists on the classpath.
    // Avoids re-doing a classloader resource lookup for every tint call.
    private static final Map<String, Boolean> CUSTOM_TEXTURE_CACHE = new ConcurrentHashMap<>();

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(NtmItemColors::onRegisterItemColors);
    }

    private static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        for (NTMMaterial mat : Mats.orderedList) {
            for (MaterialShapes shape : mat.autogen) {
                Supplier<? extends Item> generated = mat.generatedItems.get(shape);
                if (generated == null) continue;

                String itemName = shape.itemNameFor(mat);
                if (itemName == null) continue;

                // tintIndex 0 == the "layer0" texture of the item/generated model.
                // If this item uses its own hand-made texture (not the shared template),
                // don't tint it - the real art already has the right colors.
                event.register(
                        (stack, tintIndex) -> tintIndex == 0 && !hasCustomTexture(itemName)
                                ? (0xFF000000 | mat.solidColorLight)
                                : -1,
                        generated.get()
                );
            }
        }
    }

    /**
     * True if a hand-made texture exists at assets/hbm/textures/item/{itemName}.png,
     * bundled in the mod jar (dev resources or shipped jar - both are on the classpath).
     */
    private static boolean hasCustomTexture(String itemName) {
        return CUSTOM_TEXTURE_CACHE.computeIfAbsent(itemName, name -> {
            String path = "/assets/" + NuclearTechMod.MODID + "/textures/item/" + name + ".png";
            return NtmItemColors.class.getResource(path) != null;
        });
    }
}
