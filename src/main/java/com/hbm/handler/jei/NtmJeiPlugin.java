package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.handler.jei.subtypes.BatterySubtypeInterpreter;
import com.hbm.handler.jei.subtypes.MetaSubtypeInterpreter;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class NtmJeiPlugin implements IModPlugin {

    @Override public ResourceLocation getPluginUid() { return NuclearTechMod.withDefaultNamespace("jei_plugin"); }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration regs) {

        List<Item> ignoreMeta = List.of(

                NtmItems.ROD.get(),
                NtmItems.ROD_DUAL.get(),
                NtmItems.ROD_QUAD.get(),

                NtmItems.STARTER_KIT.get(),

                NtmItems.BATTERY_SC.get(),

                NtmItems.FLUID_TANK_FULL.get(),
                NtmItems.FLUID_TANK_LEAD_FULL.get(),
                NtmItems.FLUID_BARREL_FULL.get(),
                NtmItems.FLUID_PACK_FULL.get(),

                NtmItems.FLUID_ICON.get(),
                NtmItems.FLUID_IDENTIFIER_MULTI.get(),

                NtmItems.DRINK.get(),
                NtmItems.CANNED_CONSERVE.get(),
                NtmItems.CAP.get(),

                NtmBlocks.BOBBLEHEAD.asItem(),

                NtmBlocks.BARBED_WIRE.asItem(),

                NtmBlocks.FLUID_DUCT_NEO.asItem(),

                NtmBlocks.CRASHED_BOMB.asItem()
        );

        for(Item item : ignoreMeta) {
            regs.registerSubtypeInterpreter(item, MetaSubtypeInterpreter.INSTANCE);
        }

        regs.registerSubtypeInterpreter(NtmItems.BATTERY_PACK.get(), BatterySubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration regs) {
        List<ItemStack> extra = new ArrayList<>();

        FluidType[] types = Fluids.getInNiceOrder();
        for(int i = 1; i < types.length; ++i) {
            FluidType type = types[i];

            extra.add(FluidIconItem.make(type, 1000));
        }

        regs.addExtraItemStacks(extra);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ArcWelderRecipeCategory(helper),
                new AssemblyMachineRecipeCategory(helper),
                new BlastFurnaceRecipeCategory(helper),
                new CatalyticCrackingTowerRecipeCategory(helper),
                new CatalyticReformerRecipeCategory(helper),
                new CentrifugeRecipeCategory(helper),
                new ChemicalPlantRecipeCategory(helper),
                new CombinationOvenRecipeCategory(helper),
                new CompressorRecipeCategory(helper),
                new ElectrolyserFluidRecipeCategory(helper),
                new ElectrolyserMetalRecipeCategory(helper),
                new FractioningTowerRecipeCategory(helper),
                new GasCentrifugeRecipeCategory(helper),
                new MixerRecipeCategory(helper),
                new OreAcidizerRecipeCategory(helper),
                new PrecisionAssemblyMachineRecipeCategory(helper),
                new PressRecipeCategory(helper),
                new PurexRecipeCategory(helper),
                new RefineryRecipeCategory(helper),
                new RotaryFurnaceRecipeCategory(helper),
                new ShredderRecipeCategory(helper),
                new SilexRecipeCategory(helper),
                new SolderingStationRecipeCategory(helper),
                new VacuumRefineryRecipeCategory(helper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(NtmJeiRecipeTypes.ARC_WELDER, NtmJeiRecipes.arcWelder());
        registration.addRecipes(NtmJeiRecipeTypes.ASSEMBLY_MACHINE, NtmJeiRecipes.assemblyMachine());
        registration.addRecipes(NtmJeiRecipeTypes.BLAST_FURNACE, NtmJeiRecipes.blastFurnace());
        registration.addRecipes(NtmJeiRecipeTypes.CATALYTIC_CRACKING_TOWER, NtmJeiRecipes.catalyticCrackingTower());
        registration.addRecipes(NtmJeiRecipeTypes.CATALYTIC_REFORMER, NtmJeiRecipes.catalyticReformer());
        registration.addRecipes(NtmJeiRecipeTypes.CENTRIFUGE, NtmJeiRecipes.centrifuge());
        registration.addRecipes(NtmJeiRecipeTypes.CHEMICAL_PLANT, NtmJeiRecipes.chemicalPlant());
        registration.addRecipes(NtmJeiRecipeTypes.COMBINATION_OVEN, NtmJeiRecipes.combinationOven());
        registration.addRecipes(NtmJeiRecipeTypes.COMPRESSOR, NtmJeiRecipes.compressor());
        registration.addRecipes(NtmJeiRecipeTypes.ELECTROLYSER_FLUID, NtmJeiRecipes.electrolyserFluid());
        registration.addRecipes(NtmJeiRecipeTypes.ELECTROLYSER_METAL, NtmJeiRecipes.electrolyserMetal());
        registration.addRecipes(NtmJeiRecipeTypes.FRACTIONING_TOWER, NtmJeiRecipes.fractioningTower());
        registration.addRecipes(NtmJeiRecipeTypes.GAS_CENTRIFUGE, NtmJeiRecipes.gasCentrifuge());
        registration.addRecipes(NtmJeiRecipeTypes.MIXER, NtmJeiRecipes.mixer());
        registration.addRecipes(NtmJeiRecipeTypes.ORE_ACIDIZER, NtmJeiRecipes.oreAcidizer());
        registration.addRecipes(NtmJeiRecipeTypes.PRECISION_ASSEMBLY_MACHINE, NtmJeiRecipes.precisionAssemblyMachine());
        registration.addRecipes(NtmJeiRecipeTypes.PRESS, NtmJeiRecipes.press());
        registration.addRecipes(NtmJeiRecipeTypes.PUREX, NtmJeiRecipes.purex());
        registration.addRecipes(NtmJeiRecipeTypes.REFINERY, NtmJeiRecipes.refinery());
        registration.addRecipes(NtmJeiRecipeTypes.ROTARY_FURNACE, NtmJeiRecipes.rotaryFurnace());
        registration.addRecipes(NtmJeiRecipeTypes.SHREDDER, NtmJeiRecipes.shredder());
        registration.addRecipes(NtmJeiRecipeTypes.SILEX, NtmJeiRecipes.silex());
        registration.addRecipes(NtmJeiRecipeTypes.SOLDERING_STATION, NtmJeiRecipes.solderingStation());
        registration.addRecipes(NtmJeiRecipeTypes.VACUUM_REFINERY, NtmJeiRecipes.vacuumRefinery());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_ARC_WELDER.get()), NtmJeiRecipeTypes.ARC_WELDER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_ASSEMBLY_MACHINE.get()), NtmJeiRecipeTypes.ASSEMBLY_MACHINE);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_BLAST_FURNACE.get()), NtmJeiRecipeTypes.BLAST_FURNACE);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_CATALYTIC_CRACKING_TOWER.get()), NtmJeiRecipeTypes.CATALYTIC_CRACKING_TOWER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_CATALYTIC_REFORMER.get()), NtmJeiRecipeTypes.CATALYTIC_REFORMER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_CENTRIFUGE.get()), NtmJeiRecipeTypes.CENTRIFUGE);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_CHEMICAL_PLANT.get()), NtmJeiRecipeTypes.CHEMICAL_PLANT);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_COMBINATION_OVEN.get()), NtmJeiRecipeTypes.COMBINATION_OVEN);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_COMPRESSOR.get()), NtmJeiRecipeTypes.COMPRESSOR);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_ELECTROLYSER.get()), NtmJeiRecipeTypes.ELECTROLYSER_FLUID);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_ELECTROLYSER.get()), NtmJeiRecipeTypes.ELECTROLYSER_METAL);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_FRACTION_TOWER.get()), NtmJeiRecipeTypes.FRACTIONING_TOWER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_GAS_CENTRIFUGE.get()), NtmJeiRecipeTypes.GAS_CENTRIFUGE);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_MIXER.get()), NtmJeiRecipeTypes.MIXER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_ORE_ACIDIZER.get()), NtmJeiRecipeTypes.ORE_ACIDIZER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_PREC_ASS.get()), NtmJeiRecipeTypes.PRECISION_ASSEMBLY_MACHINE);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_PRESS.get()), NtmJeiRecipeTypes.PRESS);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_PUREX.get()), NtmJeiRecipeTypes.PUREX);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_REFINERY.get()), NtmJeiRecipeTypes.REFINERY);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_ROTARY_FURNACE.get()), NtmJeiRecipeTypes.ROTARY_FURNACE);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_SHREDDER.get()), NtmJeiRecipeTypes.SHREDDER);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_SILEX.get()), NtmJeiRecipeTypes.SILEX);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_SOLDERING_STATION.get()), NtmJeiRecipeTypes.SOLDERING_STATION);
        registration.addRecipeCatalyst(new ItemStack(NtmBlocks.MACHINE_VACUUM_REFINERY.get()), NtmJeiRecipeTypes.VACUUM_REFINERY);
    }
}
