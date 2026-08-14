package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class AssemblyMachineRecipes extends GenericRecipes<GenericRecipe> {

    public static final AssemblyMachineRecipes INSTANCE = new AssemblyMachineRecipes();

    @Override public int inputItemLimit() { return 12; }
    @Override public int inputFluidLimit() { return 1; }
    @Override public int outputItemLimit() { return 1; }
    @Override public int outputFluidLimit() { return 1; }

    @Override public String getFileName() { return "hbmAssemblyMachine.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
        this.register(new GenericRecipe("ass.boytarget").setup(200, 100)
                .outputItems(new ItemStack(NtmItems.LITTLE_BOY_TARGET.get(), 1))
                .inputItems(new ComparableStack(NtmItems.INGOT_URANIUM.get(), 18)));

        this.register(new GenericRecipe("ass.grass_block").setup(200, 100)
                .outputItems(new ItemStack(Blocks.GRASS_BLOCK, 1))
                .inputItems(
                        new ComparableStack(NtmItems.SINGULARITY.get(), 1),
                        new ComparableStack(NtmItems.SINGULARITY_COUNTER_RESONANT.get(), 1),
                        new ComparableStack(NtmItems.MISSILE_DOOMSDAY_RUSTED.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.man").setup(200, 100)
                .outputItems(new ItemStack(NtmBlocks.NUKE_FAT_MAN.get(), 1))
                .inputItems(new ComparableStack(NtmItems.PELLET_ANTIMATTER.get(), 1)));

//        this.register(new GenericRecipe("ass.geothermal_heat_exchanger").setup(200, 100).outputItems(new ItemStack(NtmBlocks.MACHINE_GEOTHERMAL_HEAT_EXCHANGER.get(), 1))
//                .inputItems(new OreDictStack(STEEL.pipe(), 12), new OreDictStack(STEEL.ingot(), 24), new OreDictStack(CU.plate(), 24), new OreDictStack(NB.ingot(), 4), new OreDictStack(RUBBER.ingot(), 12), new ComparableStack(ModBlocks.glass_quartz, 16))
//                .inputItemsEx(new ComparableStack(ModItems.item_expensive, 8, EnumExpensiveType.HEAVY_FRAME), new OreDictStack(NB.ingot(), 16), new OreDictStack(RUBBER.ingot(), 16), new ComparableStack(ModBlocks.glass_quartz, 16)));

//        this.register(new GenericRecipe("ass.precass").setup(1_200, 100)
//                .outputItems(new ItemStack(NtmBlocks.MACHINE_PREC_ASS.get(), 1))
//                .inputItems(
//                        new OreDictStack(STEEL.plateCast(), 8),
//                        new ComparableStack(NtmItems.INGOT_ZIRCONIUM.get(), 8),
//                        new ComparableStack(NtmItems.MOTOR.get(), 4),
//                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR_BOARD.get(), 4)));

//        this.register(new GenericRecipe("ass.assemfac").setup(400, 100)
//                .outputItems(new ItemStack(NtmBlocks.MACHINE_ASSEMBLY_FACTORY.get(), 1))
//                .inputItems(
//                        new ComparableStack(NtmItems.INGOT_DURA_STEEL.get(), 16),
//                        new ComparableStack(NtmItems.INGOT_COMBINE_STEEL.get(), 8),
//                        new ComparableStack(NtmItems.INGOT_RUBBER.get(), 16),
//                        new ComparableStack(NtmItems.INGOT_BORON.get(), 8),
//                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 4),
//                        new ComparableStack(NtmItems.MOTOR.get(), 12),
//                        new ComparableStack(NtmItems.CIRCUIT_BASIC.get(), 16)));

//        this.register(new GenericRecipe("ass.chemfac").setup(400, 100)
//                .outputItems(new ItemStack(NtmBlocks.MACHINE_CHEMICAL_FACTORY.get(), 1))
//                .inputItems(
//                        new ComparableStack(NtmItems.INGOT_DURA_STEEL.get(), 16),
//                        new ComparableStack(NtmItems.INGOT_COMBINE_STEEL.get(), 8),
//                        new ComparableStack(NtmItems.INGOT_RUBBER.get(), 16),
//                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 12),
//                        new ComparableStack(NtmItems.PIPE_COPPER.get(), 8),
//                        new ComparableStack(NtmItems.MOTOR_DESH.get(), 4),
//                        new ComparableStack(NtmItems.COIL_TUNGSTEN.get(), 16),
//                        new ComparableStack(NtmItems.CIRCUIT_BASIC.get(), 16)));

//        this.register(new GenericRecipe("ass.purex").setup(300, 100)
//                .outputItems(new ItemStack(NtmBlocks.MACHINE_PUREX.get()))
//                .inputItems(
//                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 4),
//                        new ComparableStack(NtmItems.PIPE_RUBBER.get(), 8),
//                        new ComparableStack(NtmItems.PLATE_LEAD.get(), 4),
//                        new ComparableStack(NtmItems.MOTOR_DESH.get()),
//                        new ComparableStack(NtmItems.CIRCUIT_BASIC.get(), 4)));

//        // generators
//        this.register(new GenericRecipe("ass.combustiongen").setup(300, 100)
//                .outputItems(new ItemStack(NtmBlocks.MACHINE_COMBUSTION_ENGINE.get(), 1))
//                .inputItems(new OreDictStack(STEEL.plate(), 16), new OreDictStack(CU.ingot(), 12), new OreDictStack(GOLD.wireDense(), 8), new ComparableStack(NtmItems.CANISTER_EMPTY.get(), 4), new ComparableStack(NtmItems.CIRCUIT_BASIC.get(), 1)));
//        this.register(new GenericRecipe("ass.pistonsetsteel").setup(200, 100)
//                .outputItems(MetaHelper.newStack(NtmItems.PISTON_SET, 1, PistonSetItem.PistonType.STEEL.ordinal()))
//                .inputItems(new OreDictStack(STEEL.plate(), 16), new OreDictStack(CU.plate(), 4), new OreDictStack(W.ingot(), 8), new OreDictStack(W.bolt(), 16)));
//        this.register(new GenericRecipe("ass.pistonsetdura").setup(200, 100)
//                .outputItems(MetaHelper.newStack(NtmItems.PISTON_SET, 1, PistonSetItem.PistonType.DURA.ordinal()))
//                .inputItems(new OreDictStack(DURA.ingot(), 24), new OreDictStack(TI.plate(), 8), new OreDictStack(W.ingot(), 8), new OreDictStack(DURA.bolt(), 16)));
//        this.register(new GenericRecipe("ass.pistonsetdesh").setup(200, 100)
//                .outputItems(MetaHelper.newStack(NtmItems.PISTON_SET, 1, PistonSetItem.PistonType.DESH.ordinal()))
//                .inputItems(new OreDictStack(DESH.ingot(), 24), new OreDictStack(ANY_PLASTIC.ingot(), 12), new OreDictStack(CU.plate(), 24), new OreDictStack(W.ingot(), 16), new OreDictStack(DURA.pipe(), 4)));
//        this.register(new GenericRecipe("ass.pistonsetstar").setup(200, 100)
//                .outputItems(MetaHelper.newStack(NtmItems.PISTON_SET, 1, PistonSetItem.PistonType.STARMETAL.ordinal()))
//                .inputItems(new OreDictStack(STAR.ingot(), 24), new OreDictStack(RUBBER.ingot(), 16), new OreDictStack(BIGMT.plate(), 24), new OreDictStack(NB.ingot(), 16), new OreDictStack(DURA.pipe(), 4)));

        FluidType[] order = Fluids.getInNiceOrder();
        for(int i = 1; i < order.length; ++i) {
            FluidType type = order[i];
            if(type.hasNoContainer()) continue;
            this.register(new GenericRecipe("ass.package" + type.getUnlocalizedName()).setup(40, 100).outputItems(MetaHelper.newStack(NtmItems.FLUID_PACK_FULL, 1, type.getID()))
                    .inputItems(new ComparableStack(NtmItems.FLUID_PACK_EMPTY.get())).inputFluids(new FluidStack(type, 32_000)));
            this.register(new GenericRecipe("ass.unpackage" + type.getUnlocalizedName()).setup(40, 100).setIcon(FluidIconItem.make(type, 32_000)).outputItems(new ItemStack(NtmItems.FLUID_PACK_EMPTY.get()))
                    .inputItems(new ComparableStack(MetaHelper.newStack(NtmItems.FLUID_PACK_FULL, 1, type.getID()))).outputFluids(new FluidStack(type, 32_000)));
        }
    }
}
