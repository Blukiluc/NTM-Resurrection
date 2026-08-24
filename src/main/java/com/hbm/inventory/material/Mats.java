package com.hbm.inventory.material;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.material.NTMMaterial.SmeltingBehavior;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
        public int amount;

        public MaterialStack(NTMMaterial material, int amount) {
            this.material = material;
            this.amount = amount;
        }

        public MaterialStack copy() {
            return new MaterialStack(this.material, this.amount);
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
                    MaterialShapes.CASTPLATE,
                    MaterialShapes.SHELL,
                    MaterialShapes.PIPE,
                    MaterialShapes.BLOCK)
            .setTranslationKey("item.hbm.ingot_tungtungsten")
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


    static {
        registerIngot(29996, "iron", 0xFFFFFF, 0x353535, 0xFFA259, () -> Items.IRON_INGOT, Items.IRON_INGOT.getDescriptionId());
        registerIngot(29997, "gold", 0xFFFF8B, 0xC26E00, 0xE8D754, () -> Items.GOLD_INGOT, Items.GOLD_INGOT.getDescriptionId());
        registerIngot(29998, "copper", 0xFDCA88, 0x601E0D, 0xC18336, () -> Items.COPPER_INGOT, Items.COPPER_INGOT.getDescriptionId());
        registerIngot(29999, "netherite", 0x806E6E, 0x291F1F, 0x554545, () -> Items.NETHERITE_INGOT, Items.NETHERITE_INGOT.getDescriptionId());
        registerIngot(30000, "uranium", 0x979E93, 0x3C4238, 0x686F64, NtmItems.INGOT_URANIUM);
        registerIngot(30001, "u233", 0xAEAD7F, 0x3D4339, 0x71755F, NtmItems.INGOT_U233);
        registerIngot(30002, "u235", 0x8FB47F, 0x3D4339, 0x67785F, NtmItems.INGOT_U235);
        registerIngot(30003, "u238", 0x969C92, 0x3D4339, 0x65705F, NtmItems.INGOT_U238);
        registerIngot(30004, "u238m2", 0xFFEADF, 0xFF9268, 0xFFC6AD, NtmItems.INGOT_U238M2);
        registerIngot(30005, "th232", 0x915431, 0x320600, 0x612912, NtmItems.INGOT_TH232);
        registerIngot(30006, "plutonium", 0x757E7B, 0x202927, 0x4A5350, NtmItems.INGOT_PLUTONIUM);
        registerIngot(30007, "pu238", 0xFFB14E, 0xFF8824, 0xFF9E3B, NtmItems.INGOT_PU238);
        registerIngot(30008, "pu239", 0x72996A, 0x212A28, 0x4A5D4B, NtmItems.INGOT_PU239);
        registerIngot(30009, "pu240", 0x6C826A, 0x212A28, 0x48554B, NtmItems.INGOT_PU240);
        registerIngot(30010, "pu241", 0x91926A, 0x212A28, 0x545B4B, NtmItems.INGOT_PU241);
        registerIngot(30011, "pu_mix", 0x737C79, 0x172549, 0x46505A, NtmItems.INGOT_PU_MIX);
        registerIngot(30012, "am241", 0xB9887A, 0x4B2E33, 0x7E5A5A, NtmItems.INGOT_AM241);
        registerIngot(30013, "am242", 0x9AA27A, 0x4B2E33, 0x74635A, NtmItems.INGOT_AM242);
        registerIngot(30014, "am_mix", 0xA3878C, 0x392852, 0x705668, NtmItems.INGOT_AM_MIX);
        registerIngot(30015, "neptunium", 0x788478, 0x192519, 0x475347, NtmItems.INGOT_NEPTUNIUM);
        registerIngot(30016, "po210", 0x7A6A59, 0x3D170C, 0x633D2E, NtmItems.INGOT_PO210);
        registerIngot(30017, "tc99", 0xCBDCDC, 0x6D8282, 0x9BAEAE, NtmItems.INGOT_TC99);
        registerIngot(30018, "co60", 0x91A0BF, 0x5E3750, 0x6F6C89, NtmItems.INGOT_CO60);
        registerIngot(30019, "sr90", 0xD4CB9D, 0x562202, 0x8E774D, NtmItems.INGOT_SR90);
        registerIngot(30020, "au198", 0xFEF783, 0xAC3F09, 0xDDA53C, NtmItems.INGOT_AU198);
        registerIngot(30021, "pb209", 0x866670, 0x311621, 0x643A44, NtmItems.INGOT_PB209);
        registerIngot(30022, "ra226", 0xEFF8F6, 0xBACCC8, 0xD5E4E0, NtmItems.INGOT_RA226);
        registerIngot(30023, "titanium", 0xD1CDC7, 0x605C59, 0x96928D, NtmItems.INGOT_TITANIUM);
        registerIngot(30024, "industrial_copper", 0xE99D67, 0x862E17, 0xBA633C, NtmItems.INGOT_INDUSTRIAL_COPPER);
        registerIngot(30025, "red_copper", 0xFE8B4E, 0xC52D04, 0xE75B25, NtmItems.INGOT_RED_COPPER);
        registerIngot(30026, "tungsten", 0x575757, 0x070707, 0x2C2C2C, NtmItems.INGOT_TUNGSTEN);
        registerIngot(30027, "tungsten_carbide", 0xA5A5A5, 0x272727, 0x5F5F5F, NtmItems.INGOT_TUNGSTEN_CARBIDE);
        registerIngot(30028, "aluminium", 0xE3EAEF, 0x57646C, 0x9BA4AB, NtmItems.INGOT_ALUMINIUM);
        registerIngot(30029, "steel", 0x828282, 0x191919, 0x494949, NtmItems.INGOT_STEEL);
        registerIngot(30030, "tcalloy", 0xAEB6B6, 0x424D4D, 0x747D7D, NtmItems.INGOT_TCALLOY);
        registerIngot(30031, "cdalloy", 0xE5C260, 0x765408, 0xAD882A, NtmItems.INGOT_CDALLOY);
        registerIngot(30032, "bismuth_bronze", 0xA47E60, 0x6C6257, 0x8A6F59, NtmItems.INGOT_BISMUTH_BRONZE);
        registerIngot(30033, "arsenic_bronze", 0xA8714B, 0x41473E, 0x745C45, NtmItems.INGOT_ARSENIC_BRONZE);
        registerIngot(30034, "bscco", 0x575BB2, 0x101122, 0x34366B, NtmItems.INGOT_BSCCO);
        registerIngot(30035, "lead", 0x787884, 0x191925, 0x474753, NtmItems.INGOT_LEAD);
        registerIngot(30036, "bismuth", 0x9DA19D, 0x5A5D6B, 0x7A7E80, NtmItems.INGOT_BISMUTH);
        registerIngot(30037, "arsenic", 0x497A7A, 0x344040, 0x3F5D5D, NtmItems.INGOT_ARSENIC);
        registerIngot(30038, "calcium", 0xD7D6A4, 0x4F504B, 0x909379, NtmItems.INGOT_CALCIUM);
        registerIngot(30039, "cadmium", 0xE3D531, 0x5A0200, 0x9C6010, NtmItems.INGOT_CADMIUM);
        registerIngot(30040, "tantalium", 0xD5D4DD, 0x333349, 0x7F7F8D, NtmItems.INGOT_TANTALIUM);
        registerIngot(30041, "silicon", 0xA2A7B6, 0x242546, 0x64677F, NtmItems.INGOT_SILICON);
        registerIngot(30042, "niobium", 0x785791, 0x51355D, 0x624675, NtmItems.INGOT_NIOBIUM);
        registerIngot(30043, "beryllium", 0x848478, 0x252519, 0x535347, NtmItems.INGOT_BERYLLIUM);
        registerIngot(30044, "cobalt", 0x93A2C1, 0x3F4765, 0x667391, NtmItems.INGOT_COBALT);
        registerIngot(30045, "boron", 0x7E8993, 0x48535D, 0x616C76, NtmItems.INGOT_BORON);
        registerIngot(30046, "graphite", 0x545454, 0x151515, 0x363636, NtmItems.INGOT_GRAPHITE);
        registerIngot(30047, "firebrick", 0x8C4E3D, 0x4C2E27, 0x6A3D31, NtmItems.INGOT_FIREBRICK);
        registerIngot(30048, "dura_steel", 0x55796F, 0x000D0B, 0x223E36, NtmItems.INGOT_DURA_STEEL);
        registerIngot(30049, "polymer", 0x2D2D2D, 0x050505, 0x191919, NtmItems.INGOT_POLYMER);
        registerIngot(30050, "bakelite", 0xD9555C, 0x3B0305, 0x962B30, NtmItems.INGOT_BAKELITE);
        registerIngot(30051, "biorubber", 0xB89D66, 0x6D511B, 0x937741, NtmItems.INGOT_BIORUBBER);
        registerIngot(30052, "rubber", 0x605F54, 0x151309, 0x3B392F, NtmItems.INGOT_RUBBER);
        registerIngot(30053, "pc", 0xE8E2BF, 0xBAB491, 0xD5CFAC, NtmItems.INGOT_PC);
        registerIngot(30054, "pvc", 0xF7F7F7, 0xC9C9C9, 0xE4E4E4, NtmItems.INGOT_PVC);
        registerIngot(30055, "mud", 0xA08656, 0x2D0810, 0x623D2B, NtmItems.INGOT_MUD);
        registerIngot(30056, "cft", 0xC5D6E3, 0x253643, 0x6A7B88, NtmItems.INGOT_CTF);
        registerIngot(30057, "schraranium", 0x26C8C6, 0x394339, 0x37817A, NtmItems.INGOT_SCHRARANIUM);
        registerIngot(30058, "schrabidium", 0x0ED1D1, 0x006B6B, 0x059B9B, NtmItems.INGOT_SCHRABIDIUM);
        registerIngot(30059, "schrabidate", 0x6B9BBF, 0x3C0963, 0x524D8E, NtmItems.INGOT_SCHRABIDATE);
        registerIngot(30060, "magnetized_tungsten", 0x487E7E, 0x0C1D1D, 0x264949, NtmItems.INGOT_MAGNETIZED_TUNGSTEN);
        registerIngot(30061, "combine_steel", 0x414186, 0x000027, 0x191954, NtmItems.INGOT_COMBINE_STEEL);
        registerIngot(30062, "solinium", 0x72B6B0, 0x155953, 0x438680, NtmItems.INGOT_SOLINIUM);
        registerIngot(30063, "gh336", 0xCCC9A4, 0x303B09, 0x7E844D, NtmItems.INGOT_GH336);
        registerIngot(30064, "uranium_fuel", 0x979D93, 0x4A3453, 0x6C6A6D, NtmItems.INGOT_URANIUM_FUEL);
        registerIngot(30065, "thorium_fuel", 0x73665A, 0x4D1E3B, 0x5D3F40, NtmItems.INGOT_THORIUM_FUEL);
        registerIngot(30066, "plutonium_fuel", 0x747D7A, 0x342045, 0x4F4F59, NtmItems.INGOT_PLUTONIUM_FUEL);
        registerIngot(30067, "neptunium_fuel", 0x868F84, 0x3B2746, 0x5C5C5E, NtmItems.INGOT_NEPTUNIUM_FUEL);
        registerIngot(30068, "mox_fuel", 0x858D86, 0x3F2A4C, 0x5D5C62, NtmItems.INGOT_MOX_FUEL);
        registerIngot(30069, "americium_fuel", 0x9D9290, 0x502B51, 0x725F6A, NtmItems.INGOT_AMERICIUM_FUEL);
        registerIngot(30070, "schrabidium_fuel", 0x9BBAAF, 0x4C4A69, 0x708486, NtmItems.INGOT_SCHRABIDIUM_FUEL);
        registerIngot(30071, "hes", 0x3DACA6, 0x243F61, 0x2A777E, NtmItems.INGOT_HES);
        registerIngot(30072, "les", 0x7B8478, 0x341D3A, 0x535152, NtmItems.INGOT_LES);
        registerIngot(30073, "australium", 0xFFF300, 0xA76F00, 0xDCB300, NtmItems.INGOT_AUSTRALIUM);
        registerIngot(30074, "lanthanium", 0xADC5C5, 0x112828, 0x5E7575, NtmItems.INGOT_LANTHANIUM);
        registerIngot(30075, "ac227", 0xCFC3C3, 0x521B1B, 0x866C6C, NtmItems.INGOT_AC227);
        registerIngot(30076, "desh", 0xF33B3B, 0x970000, 0xC91717, NtmItems.INGOT_DESH);
        registerIngot(30077, "ferrouranium", 0x6E6E8B, 0x1A1A31, 0x3E3E5B, NtmItems.INGOT_FERROURANIUM);
        registerIngot(30078, "starmetal", 0xADADD7, 0x14141D, 0x5F5F7D, NtmItems.INGOT_STARMETAL);
        registerIngot(30079, "gunmetal", 0xF3C62D, 0xC15C01, 0xD98D12, NtmItems.INGOT_GUNMETAL);
        registerIngot(30080, "weaponsteel", 0x7D7D7D, 0x292929, 0x505050, NtmItems.INGOT_WEAPONSTEEL);
        registerIngot(30081, "saturnite", 0x32A4B7, 0x134850, 0x217380, NtmItems.INGOT_SATURNITE);
        registerIngot(30082, "euphemium", 0xEE60B3, 0x9B0A55, 0xC53383, NtmItems.INGOT_EUPHEMIUM);
        registerIngot(30083, "dineutronium", 0x50518C, 0x02062C, 0x22285B, NtmItems.INGOT_DINEUTRONIUM);
        registerIngot(30084, "electronium", 0x4BECFF, 0x416EFE, 0x44AEFF, NtmItems.INGOT_ELECTRONIUM);
        registerIngot(30085, "smore", 0xD8C69F, 0x71532D, 0xA78E61, NtmItems.INGOT_SMORE);
        registerIngot(30086, "osmiridium", 0xAEBEDA, 0x8CA2C8, 0x9BAFD0, NtmItems.INGOT_OSMIRIDIUM);
        registerIngot(30087, "steel_dusted", 0x7D7D7D, 0x181818, 0x454545, NtmItems.INGOT_DUSTED_STEEL);
        registerIngot(30088, "chainsteel", 0x93A2C0, 0x3F4766, 0x677392, NtmItems.INGOT_CHAINSTEEL);
        registerIngot(30089, "meteorite", 0x5F5F5F, 0x272727, 0x444444, NtmItems.INGOT_METEORITE);
        registerIngot(30090, "meteorite_forged", 0x646464, 0x252525, 0x444444, NtmItems.INGOT_METEORITE_FORGED);
        registerIngot(30091, "phosphorus", 0xEFEDE2, 0xCBC5A6, 0xDCD8C2, NtmItems.INGOT_PHOSPHORUS);
        registerIngot(30092, "lithium", 0xEAEAEA, 0x9B9B9B, 0xC3C3C3, NtmItems.INGOT_LITHIUM);
        registerIngot(30093, "zirconium", 0xC7C0A2, 0x5F583A, 0x948D6F, NtmItems.INGOT_ZIRCONIUM);
        registerIngot(30094, "semtex", 0xD99B24, 0x7F5B15, 0xAD7C1D, NtmItems.INGOT_SEMTEX);
        registerIngot(30095, "c4", 0xB07892, 0x664554, 0x8B5F73, NtmItems.INGOT_C4);
        registerIngot(30096, "redstone", 0xE3260C, 0x700E06, 0xFF1000, NtmItems.INGOT_REDSTONE);
        registerIngot(30097, "neodymium", 0xE6E6B6, 0x1C1C00, 0x8F8F5F, NtmItems.INGOT_NEODYMIUM);
        registerIngot(30098, "borax", 0xFFFFFF, 0x946E23, 0xFFECC6, NtmItems.INGOT_BORAX);
        registerIngot(30099, "sodium", 0xD3BF9E, 0x3A5A6B, 0x7E9493, NtmItems.INGOT_SODIUM);
        registerIngot(30100, "strontium", 0xF1E8BA, 0x271E00, 0xCAC193, NtmItems.INGOT_STRONTIUM);
        registerIngot(30101, "slag", 0x554940, 0x34281F, 0x6C6562, NtmItems.INGOT_SLAG);
    }

    private static NTMMaterial registerIngot(int id, String name, int solidLight, int solidDark, int molten,
                                             Supplier<? extends Item> item) {
        return registerIngot(id, name, solidLight, solidDark, molten, item, "item.hbm.ingot_" + name);
    }

    private static NTMMaterial registerIngot(int id, String name, int solidLight, int solidDark, int molten,
                                             Supplier<? extends Item> item, String translationKey) {
        return makeSmeltable(id, name, solidLight, solidDark, molten)
                .setItem(MaterialShapes.INGOT, item)
                .setTranslationKey(translationKey);
    }

}
