package com.hbm.inventory.material;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Encapsulates a material (iron, copper, schrabidium...) used by the auto-gen and crucible systems.
 * Faithful port of the 1.7.10 NTMMaterial, with the following adaptations for 1.21.1:
 * - DictFrame (OreDictionary) replaced by a plain String[] of names/aliases
 * - make(Item, amount) no longer sets meta/damage (Data Components era), just count
 * - Registration happens in the constructor directly (same as original)
 */
public class NTMMaterial {

    public enum SmeltingBehavior {
        /** Can't be smelted or otherwise doesn't belong in a smelter (like diamond) */
        NOT_SMELTABLE,
        /** Can't be smelted because the material would vaporize */
        VAPORIZES,
        /** Can't be smelted because the material doesn't survive the temperatures */
        BREAKS,
        /** Mostly metals - can be smelted directly into a liquid form */
        SMELTABLE,
        /** Stuff like coal - not smeltable on its own but can be added to a crucible */
        ADDITIVE
    }

    public final int id;
    /** All names/aliases for this material. names[0] is the canonical name used for tags and registry keys. */
    public String[] names;

    public Set<MaterialShapes> autogen = new HashSet<>();
    public Set<MatTraits> traits = new HashSet<>();

    public final Map<MaterialShapes, Supplier<? extends Item>> generatedItems = new HashMap<>();
    public SmeltingBehavior smeltable = SmeltingBehavior.NOT_SMELTABLE;

    public int solidColorLight = 0xFF4A00;
    public int solidColorDark  = 0x802000;
    public int moltenColor     = 0xFF4A00;
    private String translationKey;

    public NTMMaterial smeltsInto;
    public int convIn  = 1;
    public int convOut = 1;

    /**
     * Creates and registers a material. names[0] is the canonical name (used for tags),
     * any additional names are OreDict-style aliases kept for compatibility.
     */
    public NTMMaterial(int id, String... names) {
        this.id = id;
        this.names = names;
        this.smeltsInto = this;

        for (String name : names) {
            Mats.matByName.put(name, this);
        }
        Mats.orderedList.add(this);
        Mats.matById.put(id, this);
    }

    public String getCanonicalName() {
        return this.names[0].toLowerCase(Locale.US);
    }

    /** Localization key for this material's display name, e.g. "hbmmat.iron" */
    public String getTranslationKey() {
        return this.translationKey != null ? this.translationKey : "hbmmat." + getCanonicalName();
    }

    public NTMMaterial setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
        return this;
    }

    public NTMMaterial setItem(MaterialShapes shape, Supplier<? extends Item> item) {
        this.generatedItems.put(shape, item);
        return this;
    }

    public Component getName() {
        return Component.translatable(this.getTranslationKey());
    }

    public NTMMaterial setConversion(NTMMaterial into, int unitsIn, int unitsOut) {
        this.smeltsInto = into;
        this.convIn  = unitsIn;
        this.convOut = unitsOut;
        return this;
    }

    /** Declares which shapes (ingot, dust, plate...) should have real auto-generated items */
    public NTMMaterial setAutogen(MaterialShapes... shapes) {
        for (MaterialShapes shape : shapes) {
            if (!shape.noAutogen) this.autogen.add(shape);
        }
        return this;
    }

    public NTMMaterial setTraits(MatTraits... traits) {
        for (MatTraits trait : traits) this.traits.add(trait);
        return this;
    }

    /** Convenience: adds METAL trait and returns this for chaining */
    public NTMMaterial m() { this.traits.add(MatTraits.METAL); return this; }

    /** Convenience: adds NONMETAL trait and returns this for chaining */
    public NTMMaterial n() { this.traits.add(MatTraits.NONMETAL); return this; }

    public NTMMaterial smeltable(SmeltingBehavior behavior) {
        this.smeltable = behavior;
        return this;
    }

    public NTMMaterial setSolidColor(int colorLight, int colorDark) {
        this.solidColorLight = colorLight;
        this.solidColorDark  = colorDark;
        return this;
    }

    public NTMMaterial setMoltenColor(int color) {
        this.moltenColor = color;
        return this;
    }

    public boolean hasAutogen(MaterialShapes shape) {
        return this.autogen.contains(shape);
    }

    /** Returns the auto-generated item for this shape, or null if none was generated */
    public Item getItem(MaterialShapes shape) {
        Supplier<? extends Item> item = this.generatedItems.get(shape);
        return item != null ? item.get() : null;
    }

    /** Creates an ItemStack of the auto-generated item for this shape, or ItemStack. EMPTY if none */
    public ItemStack makeStack(MaterialShapes shape, int count) {
        Item item = getItem(shape);
        return item != null ? new ItemStack(item, count) : ItemStack.EMPTY;
    }

    public ItemStack makeStack(MaterialShapes shape) {
        return makeStack(shape, 1);
    }

    public boolean hasTrait(MatTraits trait) {
        return this.traits.contains(trait);
    }

    /**
     * Creates an ItemStack of the given item with the given count.
     * Note: in 1.7.10 this used this.id as the damage/meta value - that concept no longer
     * exists in 1.21.1, so we just return a plain ItemStack. Auto-gen items each have their
     * own distinct registry entry and don't share a single item type with meta variants.
     */
    public ItemStack make(Item item, int amount) {
        return new ItemStack(item, amount);
    }

    public ItemStack make(Item item) {
        return make(item, 1);
    }
}
