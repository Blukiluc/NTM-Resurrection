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

}