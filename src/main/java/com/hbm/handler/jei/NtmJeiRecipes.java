package com.hbm.handler.jei;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.*;
import com.hbm.inventory.recipes.ArcWelderRecipes.ArcWelderRecipe;
import com.hbm.inventory.recipes.CompressorRecipes.CompressorRecipe;
import com.hbm.inventory.recipes.ElectrolyserFluidRecipes.ElectrolysisRecipe;
import com.hbm.inventory.recipes.ElectrolyserMetalRecipes.ElectrolysisMetalRecipe;
import com.hbm.inventory.recipes.MixerRecipes.MixerRecipe;
import com.hbm.inventory.recipes.OreAcidizerRecipes.OreAcidizerRecipe;
import com.hbm.inventory.recipes.SilexRecipes.SILEXRecipe;
import com.hbm.inventory.recipes.SilexRecipes.WeightedOutput;
import com.hbm.inventory.recipes.SolderingStationRecipes.SolderingRecipe;
import com.hbm.inventory.recipes.VacuumRefineryRecipes.VacuumRefineryRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes.ChanceOutput;
import com.hbm.inventory.recipes.loader.GenericRecipes.ChanceOutputMulti;
import com.hbm.inventory.recipes.loader.GenericRecipes.IOutput;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.ItemStamp;
import com.hbm.items.machine.ItemStamp.StampType;
import com.hbm.util.Tuple.Pair;
import com.hbm.util.Tuple.Quintet;
import com.hbm.util.Tuple.Triplet;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Adapts the different legacy recipe containers to the common recipe model used by JEI.
 * The machine code intentionally keeps its native maps and specialized recipe classes;
 * this class is the single translation boundary between those formats and JEI.
 */
final class NtmJeiRecipes {

    private NtmJeiRecipes() { }

    static List<GenericRecipe> arcWelder() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(ArcWelderRecipe data : ArcWelderRecipes.recipes) {
            result.add(recipe("arc_welder", index++, data.ingredients, fluids(data.fluid),
                    items(data.output), null, data.duration, data.consumption));
        }
        return result;
    }

    static List<GenericRecipe> assemblyMachine() {
        return generic(AssemblyMachineRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> blastFurnace() {
        return generic(BlastFurnaceRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> catalyticCrackingTower() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, Pair<FluidStack, FluidStack>> entry : CatalyticCrackingTowerRecipes.cracking.entrySet()) {
            result.add(recipe("catalytic_cracking_tower", index++, null,
                    fluids(new FluidStack(entry.getKey(), 100), new FluidStack(Fluids.STEAM, 200)), null,
                    fluids(entry.getValue().getKey(), entry.getValue().getValue(), new FluidStack(Fluids.SPENTSTEAM, 2)),
                    0, 0));
        }
        return result;
    }

    static List<GenericRecipe> catalyticReformer() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, Triplet<FluidStack, FluidStack, FluidStack>> entry :
                CatalyticReformerRecipes.INSTANCE.catalytic_reformer.entrySet()) {
            Triplet<FluidStack, FluidStack, FluidStack> outputs = entry.getValue();
            result.add(recipe("catalytic_reformer", index++, null,
                    fluids(new FluidStack(entry.getKey(), 100)), null,
                    fluids(outputs.getX(), outputs.getY(), outputs.getZ()), 0, 0));
        }
        return result;
    }

    static List<GenericRecipe> centrifuge() {
        return generic(CentrifugeRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> chemicalPlant() {
        return generic(ChemicalPlantRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> combinationOven() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(CombinationOvenRecipes.JeiRecipe data : CombinationOvenRecipes.getJeiRecipes()) {
            result.add(recipe("combination_oven", index++, astacks(data.input()), null,
                    items(data.outputItem()), fluids(data.outputFluid()), 0, 0));
        }
        return result;
    }

    static List<GenericRecipe> compressor() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<Pair<FluidType, Integer>, CompressorRecipe> entry : CompressorRecipes.INSTANCE.recipes.entrySet()) {
            Pair<FluidType, Integer> input = entry.getKey();
            CompressorRecipe data = entry.getValue();
            result.add(recipe("compressor", index++, null,
                    fluids(new FluidStack(input.getKey(), data.inputAmount, input.getValue())), null,
                    fluids(data.output), data.duration, 0));
        }
        return result;
    }

    static List<GenericRecipe> electrolyserFluid() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, ElectrolysisRecipe> entry : ElectrolyserFluidRecipes.INSTANCE.electrolysis.entrySet()) {
            ElectrolysisRecipe data = entry.getValue();
            result.add(recipe("electrolyser_fluid", index++, null,
                    fluids(new FluidStack(entry.getKey(), data.amount)), data.byproduct,
                    fluids(data.output1, data.output2), data.duration, 0));
        }
        return result;
    }

    static List<GenericRecipe> electrolyserMetal() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<AStack, ElectrolysisMetalRecipe> entry : ElectrolyserMetalRecipes.INSTANCE.electrolysis.entrySet()) {
            ElectrolysisMetalRecipe data = entry.getValue();
            List<ItemStack> outputs = new ArrayList<>();
            addMaterialOutput(outputs, data.output1);
            addMaterialOutput(outputs, data.output2);
            if(data.byproduct != null) Collections.addAll(outputs, data.byproduct);
            result.add(recipe("electrolyser_metal", index++, astacks(entry.getKey()), null,
                    outputs.toArray(ItemStack[]::new), null, data.duration, 0));
        }
        return result;
    }

    static List<GenericRecipe> fractioningTower() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, Pair<FluidStack, FluidStack>> entry : FractioningRecipes.fractions.entrySet()) {
            result.add(recipe("fractioning_tower", index++, null,
                    fluids(new FluidStack(entry.getKey(), 100)), null,
                    fluids(entry.getValue().getKey(), entry.getValue().getValue()), 0, 0));
        }
        return result;
    }

    static List<GenericRecipe> gasCentrifuge() {
        return generic(GasCentrifugeRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> mixer() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, MixerRecipe[]> entry : MixerRecipes.INSTANCE.recipes.entrySet()) {
            for(MixerRecipe data : entry.getValue()) {
                result.add(recipe("mixer", index++, astacks(data.solidInput),
                        fluids(data.input1, data.input2), null,
                        fluids(new FluidStack(entry.getKey(), data.output)), data.processTime, 0));
            }
        }
        return result;
    }

    static List<GenericRecipe> oreAcidizer() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<Pair<ComparableStack, FluidType>, OreAcidizerRecipe> entry :
                OreAcidizerRecipes.INSTANCE.recipes.entrySet()) {
            ComparableStack input = entry.getKey().getKey();
            OreAcidizerRecipe data = entry.getValue();
            AStack sizedInput = new ComparableStack(input.item, data.itemAmount, input.meta);
            result.add(recipe("ore_acidizer", index++, astacks(sizedInput),
                    fluids(new FluidStack(entry.getKey().getValue(), data.acidAmount)), items(data.output), null,
                    data.duration, 0));
        }
        return result;
    }

    static List<GenericRecipe> precisionAssemblyMachine() {
        return generic(PrecisionAssemblyMachineRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> press() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<Pair<AStack, StampType>, ItemStack> entry : PressRecipes.recipes.entrySet()) {
            List<AStack> inputs = new ArrayList<>();
            inputs.add(entry.getKey().getKey());
            List<ItemStack> stamps = ItemStamp.STAMPS.get(entry.getKey().getValue());
            if(stamps != null && !stamps.isEmpty()) inputs.add(new JeiIngredient(stamps));
            result.add(recipe("press", index++, inputs.toArray(AStack[]::new), null,
                    items(entry.getValue()), null, 0, 0));
        }
        return result;
    }

    static List<GenericRecipe> purex() {
        return generic(PurexRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> refinery() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> entry :
                RefineryRecipes.INSTANCE.refinery.entrySet()) {
            Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> outputs = entry.getValue();
            result.add(recipe("refinery", index++, null, fluids(new FluidStack(entry.getKey(), 100)),
                    items(outputs.getZ()), fluids(outputs.getV(), outputs.getW(), outputs.getX(), outputs.getY()),
                    0, 0));
        }
        return result;
    }

    static List<GenericRecipe> rotaryFurnace() {
        return generic(RotaryFurnaceRecipes.INSTANCE.recipeOrderedList);
    }

    static List<GenericRecipe> shredder() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<AStack, ItemStack> entry : ShredderRecipes.recipes.entrySet()) {
            result.add(recipe("shredder", index++, astacks(entry.getKey()), null,
                    items(entry.getValue()), null, 0, 0));
        }
        return result;
    }

    static List<GenericRecipe> silex() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<ComparableStack, SILEXRecipe> entry : SilexRecipes.INSTANCE.recipes.entrySet()) {
            ComparableStack input = entry.getKey();
            SILEXRecipe data = entry.getValue();
            AStack[] inputItems = astacks(input);
            FluidStack[] inputFluids = fluids(new FluidStack(Fluids.PEROXIDE, data.fluidProduced));

            if(input.item == NtmItems.FLUID_ICON.get()) {
                inputItems = null;
                inputFluids = fluids(new FluidStack(Fluids.fromID(input.meta), data.fluidConsumed));
            }

            GenericRecipe recipe = recipe("silex", index++, inputItems, inputFluids, null, null, 0, 0);
            recipe.outputItem = weightedOutputs(data.outputs);
            result.add(recipe);
        }
        return result;
    }

    static List<GenericRecipe> solderingStation() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(SolderingRecipe data : SolderingStationRecipes.recipes) {
            result.add(recipe("soldering_station", index++, concat(data.toppings, data.pcb, data.solder),
                    fluids(data.fluid), items(data.output), null, data.duration, data.consumption));
        }
        return result;
    }

    static List<GenericRecipe> vacuumRefinery() {
        List<GenericRecipe> result = new ArrayList<>();
        int index = 0;
        for(Map.Entry<FluidType, VacuumRefineryRecipe> entry : VacuumRefineryRecipes.INSTANCE.vacuum.entrySet()) {
            result.add(recipe("vacuum_refinery", index++, null,
                    fluids(new FluidStack(entry.getKey(), 100)), null, entry.getValue().outputs, 0, 0));
        }
        return result;
    }

    private static List<GenericRecipe> generic(Collection<? extends GenericRecipe> recipes) {
        return new ArrayList<>(recipes);
    }

    private static GenericRecipe recipe(String type, int index, AStack[] inputItems, FluidStack[] inputFluids,
                                        ItemStack[] outputItems, FluidStack[] outputFluids,
                                        int duration, long power) {
        GenericRecipe recipe = new GenericRecipe("jei." + type + "." + index);
        recipe.inputItem = nonEmpty(inputItems);
        recipe.inputFluid = fluids(inputFluids);
        recipe.outputItem = itemOutputs(outputItems);
        recipe.outputFluid = fluids(outputFluids);
        recipe.duration = duration;
        recipe.power = power;
        return recipe;
    }

    private static AStack[] astacks(AStack... stacks) {
        if(stacks == null) return null;
        return Arrays.stream(stacks).filter(stack -> stack != null).toArray(AStack[]::new);
    }

    private static AStack[] nonEmpty(AStack[] stacks) {
        AStack[] filtered = astacks(stacks);
        return filtered == null || filtered.length == 0 ? null : filtered;
    }

    private static ItemStack[] items(ItemStack... stacks) {
        return stacks;
    }

    private static IOutput[] itemOutputs(ItemStack[] stacks) {
        if(stacks == null) return null;
        return Arrays.stream(stacks)
                .filter(stack -> stack != null && !stack.isEmpty())
                .map(stack -> (IOutput) new ChanceOutput(stack.copy()))
                .toArray(IOutput[]::new);
    }

    private static FluidStack[] fluids(FluidStack... stacks) {
        if(stacks == null) return null;
        FluidStack[] filtered = Arrays.stream(stacks)
                .filter(stack -> stack != null && stack.type != null && stack.type != Fluids.NONE && stack.fill > 0)
                .toArray(FluidStack[]::new);
        return filtered.length == 0 ? null : filtered;
    }

    private static AStack[] concat(AStack[]... arrays) {
        List<AStack> result = new ArrayList<>();
        for(AStack[] array : arrays) {
            if(array != null) Collections.addAll(result, array);
        }
        return result.toArray(AStack[]::new);
    }

    private static IOutput[] weightedOutputs(List<WeightedOutput> outputs) {
        if(outputs == null || outputs.isEmpty()) return null;
        ChanceOutput[] choices = outputs.stream()
                .filter(output -> output != null && output.stack != null && !output.stack.isEmpty())
                .map(output -> new ChanceOutput(output.stack.copy(), output.itemWeight))
                .toArray(ChanceOutput[]::new);
        return choices.length == 0 ? null : new IOutput[] {new ChanceOutputMulti(choices)};
    }

    private static void addMaterialOutput(List<ItemStack> outputs, MaterialStack stack) {
        if(stack == null || stack.material == null || stack.amount <= 0) return;

        MaterialShapes[] shapes = {
                MaterialShapes.BLOCK, MaterialShapes.CASTPLATE, MaterialShapes.INGOT,
                MaterialShapes.PLATE, MaterialShapes.NUGGET, MaterialShapes.DUSTTINY,
                MaterialShapes.BOLT, MaterialShapes.WIRE
        };

        for(MaterialShapes shape : shapes) {
            if(shape.quantity <= 0 || stack.amount % shape.quantity != 0 || !stack.material.hasAutogen(shape)) continue;
            ItemStack output = stack.material.makeStack(shape, stack.amount / shape.quantity);
            if(!output.isEmpty()) outputs.add(output);
            return;
        }
    }

    /** A JEI-only input that cycles through every registered stamp of the required type. */
    private static final class JeiIngredient extends AStack {

        private final List<ItemStack> alternatives;

        private JeiIngredient(List<ItemStack> alternatives) {
            this.alternatives = alternatives.stream().map(ItemStack::copy).toList();
            this.stacksize = 1;
        }

        @Override
        public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
            if(stack == null || stack.isEmpty() || !ignoreSize && stack.getCount() < stacksize) return false;
            return alternatives.stream().anyMatch(candidate -> ItemStack.isSameItemSameComponents(candidate, stack));
        }

        @Override public AStack copy() { return new JeiIngredient(alternatives); }
        @Override public AStack copy(int size) { JeiIngredient copy = new JeiIngredient(alternatives); copy.stacksize = size; return copy; }
        @Override public List<ItemStack> extractForJEI() { return alternatives.stream().map(ItemStack::copy).toList(); }
        @Override public int compareTo(AStack other) { return 0; }
    }
}
