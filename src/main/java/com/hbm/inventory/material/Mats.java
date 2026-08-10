package com.hbm.inventory.material;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.material.NTMMaterial.SmeltingBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central registry of all NTM materials.
 * Faithful port of the 1.7.10 Mats.java structure.
 *
 * Materials are registered at construction time (see NTMMaterial constructor).
 * materialEntries and materialOreEntries are populated by MatDistribution
 * and used by the crucible/smelter system - they are declared here but not
 * touched by any machine yet, so they are safe and dormant.
 */
public class Mats {

    // Material registries - populated by NTMMaterial constructors
    public static final List<NTMMaterial> orderedList = new ArrayList<>();
    public static final Map<Integer, NTMMaterial> matById = new HashMap<>();
    public static final Map<String, NTMMaterial> matByName = new HashMap<>();

    // Crucible/smelter input registries - populated by MatDistribution.registerDefaults()
    // Safe to declare here: nothing reads these maps until a crucible machine is implemented.
    public static final Map<ComparableStack, List<MaterialStack>> materialEntries = new HashMap<>();
    public static final Map<String, List<MaterialStack>> materialOreEntries = new HashMap<>();

    // ---------------------------------------------------------------
    // Factory methods mirroring the original 1.7.10 API
    // ---------------------------------------------------------------

    public static NTMMaterial makeSmeltable(int id, String name, int solidLight, int solidDark, int molten) {
        return new NTMMaterial(id, name).smeltable(SmeltingBehavior.SMELTABLE).setSolidColor(solidLight, solidDark).setMoltenColor(molten);
    }

    public static NTMMaterial makeSmeltable(int id, String name, int color) {
        return makeSmeltable(id, name, color, color, color);
    }

    public static NTMMaterial makeAdditive(int id, String name, int solidLight, int solidDark, int molten) {
        return new NTMMaterial(id, name).smeltable(SmeltingBehavior.ADDITIVE).setSolidColor(solidLight, solidDark).setMoltenColor(molten);
    }

    public static NTMMaterial makeNonSmeltable(int id, String name, int solidLight, int solidDark, int molten) {
        return new NTMMaterial(id, name).smeltable(SmeltingBehavior.NOT_SMELTABLE).setSolidColor(solidLight, solidDark).setMoltenColor(molten);
    }

    // ---------------------------------------------------------------
    // MaterialStack: a material + a quantity in quanta
    // Used by MatDistribution to describe smelting outputs
    // ---------------------------------------------------------------

    public static class MaterialStack {
        public final NTMMaterial material;
        public final int amount;

        public MaterialStack(NTMMaterial material, int amount) {
            this.material = material;
            this.amount = amount;
        }
    }

    // ---------------------------------------------------------------
    // Materials - add yours here following this pattern:
    // public static final NTMMaterial MAT_XXX = makeSmeltable(id, "name", solidLight, solidDark, molten)
    //         .setAutogen(MaterialShapes.INGOT)
    //         .m();  // .m() = metal, .n() = non-metal
    //
    // The id is arbitrary but must be unique. Use a 4-digit number.
    // The name (lowercase) becomes the registry key: setAutogen(INGOT) -> "ingot_<name>" in game.
    // ---------------------------------------------------------------

    public static final NTMMaterial MAT_TUNGTUNGSTEN = makeSmeltable(1606, "tungtungsten", 0x824F2F, 0x824F2F, 0x824F2F)
            .setAutogen(MaterialShapes.DUSTTINY,
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.INGOT,
                    MaterialShapes.DUST,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.PLATE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE)
            .m();
    public static final NTMMaterial MAT_STEEL = makeSmeltable(1607,"steel", 0xAFAFAF, 0x0F0F0F, 0x4A4A4A)
            .setAutogen(
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE)
            .m();
    public static final NTMMaterial MAT_TUNGSTEN = makeSmeltable(1608, "tungsten", 0x868686, 0x000000, 0x977474)
            .setAutogen(MaterialShapes.DUSTTINY,
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_TITANIUM = makeSmeltable(1609, "titanium", 0xF7F3F2, 0x4F4C4B, 0xA99E79)
            .setAutogen(MaterialShapes.DUSTTINY,
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_COPPER = makeSmeltable(1610, "copper",0xFDCA88, 0x601E0D, 0xC18336)
            .setAutogen(MaterialShapes.DUSTTINY,
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_SATURNITE = makeSmeltable(1611, "saturnite",0x3AC4DA, 0x09282C, 0x30A4B7)
            .setAutogen(MaterialShapes.DUSTTINY,
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_WOOD = makeNonSmeltable(1612, "wooden",0x896727, 0x281E0B, 0x896727)
            .setAutogen(MaterialShapes.GRIP,
                    MaterialShapes.STOCK)
                    ;
    public static final NTMMaterial MAT_REDCOPPER = makeSmeltable(1613, "red_copper",0xFFBA7D, 0xAF1700, 0xE44C0F)
            .setAutogen(MaterialShapes.DUSTTINY,
                    MaterialShapes.BOLT,
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_GUNMETAL = makeSmeltable(1614, "gunmetal",0xFFEF3F, 0xAD3600, 0xF9C62C)
            .setAutogen(
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.STOCK,
                    MaterialShapes.GRIP,
                    MaterialShapes.GEAR,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_WEAPONSTEEL = makeSmeltable(1615, "weaponsteel",0xA0A0A0, 0x000000, 0x808080)
            .setAutogen(
                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE,
                    MaterialShapes.SHELL,
                    MaterialShapes.STOCK,
                    MaterialShapes.GRIP,
                    MaterialShapes.GEAR,
                    MaterialShapes.PIPE);
    public static final NTMMaterial MAT_BSCCO = makeSmeltable(1616, "bscco",0x767BF1, 0x000000, 0x5E62C0)
            .setAutogen(

                    MaterialShapes.DENSEWIRE);
    public static final NTMMaterial MAT_GOLD = makeSmeltable(1617, "golden", 0xFFFF8B, 0xC26E00, 0xE8D754)
            .setAutogen(MaterialShapes.DUSTTINY,

                    MaterialShapes.WIRE,
                    MaterialShapes.DENSEWIRE);


}