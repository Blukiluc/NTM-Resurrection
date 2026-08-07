package com.hbm.inventory.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a "shape" a material can come in (ingot, nugget, dust, plate, wire...).
 * Each shape has:
 * - a quantity in quanta (1 ingot = 72 quanta, chosen to divide evenly through 2, 3, 4, 6, 8, 9)
 * - a tagSegment used to build common item tags (e.g. "ingots" -> c:ingots/<material>)
 * - a noAutogen flag for technical/compat shapes that never produce real items
 *
 * Mirrors the original 1.7.10 MaterialShapes but uses common item tags instead of OreDictionary.
 */
public class MaterialShapes {

    public static final List<MaterialShapes> allShapes = new ArrayList<>();

    // Special/technical shapes - never auto-generate real items
    public static final MaterialShapes ANY = new MaterialShapes(0, "any").noAutogen();
    public static final MaterialShapes ORE = new MaterialShapes(0, "ores").noAutogen();
    public static final MaterialShapes ONLY_ORE = ORE; // alias used in MatDistribution

    public static final MaterialShapes QUANTUM = new MaterialShapes(1, null).noAutogen();
    public static final MaterialShapes TINY = new MaterialShapes(8, "tiny").noAutogen();

    // Real shapes - auto-generate items for materials that declare them in setAutogen(...)
    public static final MaterialShapes DUSTTINY = new MaterialShapes(8, "tiny_dusts");
    public static final MaterialShapes NUGGET = new MaterialShapes(8, "nuggets"); // missing
    public static final MaterialShapes FRAGMENT = new MaterialShapes(8, "fragments"); // missing
    public static final MaterialShapes BOLT = new MaterialShapes(9, "bolts");
    public static final MaterialShapes WIRE = new MaterialShapes(9, "wires");
    public static final MaterialShapes QUART = new MaterialShapes(18, "quarters"); // missing
    public static final MaterialShapes BILLET = new MaterialShapes(48, "billets"); // missing
    public static final MaterialShapes INGOT = new MaterialShapes(72, "ingots");
    public static final MaterialShapes GEM = new MaterialShapes(72, "gems"); // missing
    public static final MaterialShapes CRYSTAL = new MaterialShapes(72, "crystals"); // missing
    public static final MaterialShapes DUST = new MaterialShapes(72, "dusts");
    public static final MaterialShapes DENSEWIRE = new MaterialShapes(72, "dense_wires");
    public static final MaterialShapes PLATE = new MaterialShapes(72, "plates");
    public static final MaterialShapes GRIP = new MaterialShapes(144, "grips"); // gun
    public static final MaterialShapes CASTPLATE = new MaterialShapes(216, "cast_plates");
    public static final MaterialShapes SHELL = new MaterialShapes(288, "shells");
    public static final MaterialShapes PIPE = new MaterialShapes(216, "pipes");
    public static final MaterialShapes STOCK = new MaterialShapes(288, "stocks"); // gun
    public static final MaterialShapes WELDEDPLATE = new MaterialShapes(432, "welded_plates");
    public static final MaterialShapes BLOCK = new MaterialShapes(648, "blocks"); // missing

    // ---------------------------------------------------------------

    public boolean noAutogen = false;
    public final int quantity;
    /** Plural tag segment, e.g. "ingots" -> c:ingots/<material>. Null for QUANTUM. */
    public final String tagSegment;

    private MaterialShapes(int quantity, String tagSegment) {
        this.quantity = quantity;
        this.tagSegment = tagSegment;
        allShapes.add(this);
    }

    /** Mark this shape as technical/compat-only — no real items generated */
    public MaterialShapes noAutogen() {
        this.noAutogen = true;
        return this;
    }

    /** Amount in quanta for <amount> units of this shape. E.g. INGOT.q(3) = 3 ingots worth. */
    public int q(int amount) {
        return this.quantity * amount;
    }

    /**
     * Amount in quanta for a recipe that uses <unitsIn> of this shape to produce <itemsProduced> items.
     * E.g. INGOT.q(6, 16) = using 6 ingots of material to make 16 rails -> quantum per rail.
     */
    public int q(int unitsIn, int itemsProduced) {
        return this.quantity * unitsIn / itemsProduced;
    }

    /** The common item tag for this shape + material, e.g. c:ingots/iron */
    public TagKey<Item> tagFor(NTMMaterial material) {
        if (tagSegment == null) return null;
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", tagSegment + "/" + material.names[0].toLowerCase(java.util.Locale.US)));
    }

    /**
     * Singular form of the tag segment (ingots->ingot, dusts->dust, wires->wire...).
     * Also the filename (without extension) of this shape's shared tintable template texture,
     * e.g. assets/hbm/textures/item/template/ingot.png
     */
    public String templateName() {
        if (tagSegment == null) return null;
        return tagSegment.endsWith("s") ? tagSegment.substring(0, tagSegment.length() - 1) : tagSegment;
    }

    /**
     * Registry name for the auto-generated item, e.g. "ingot_iron".
     * Uses singular form by stripping trailing 's' (ingots->ingot, dusts->dust, etc.)
     */
    public String itemNameFor(NTMMaterial material) {
        String singular = templateName();
        if (singular == null) return null;
        return singular + "_" + material.names[0].toLowerCase(java.util.Locale.US);
    }
}