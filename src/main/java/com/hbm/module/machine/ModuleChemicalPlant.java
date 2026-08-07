package com.hbm.module.machine;

import api.hbm.energymk2.IEnergyHandlerMK2;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.util.BobMathUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ModuleChemicalPlant extends ModuleMachineBase {

    public ModuleChemicalPlant(int index, IEnergyHandlerMK2 battery, NonNullList<ItemStack> slots) {
        super(index, battery, slots);
        this.inputSlots = new int[3];
        this.outputSlots = new int[1];
        this.inputTanks = new FluidTank[3];
        this.outputTanks = new FluidTank[3];
    }

    @Override
    public GenericRecipes<GenericRecipe> getRecipeSet() {
        return ChemicalPlantRecipes.INSTANCE;
    }

    @Override
    public void setupTanks(GenericRecipe recipe) {
        super.setupTanks(recipe);
        if(recipe == null) return;
        for(int i = 0; i < inputTanks.length; i++) if(recipe.inputFluid != null && recipe.inputFluid.length > i) inputTanks[i].changeTankSize(BobMathUtil.max(inputTanks[i].getFill(), recipe.inputFluid[i].fill * 2, 24_000));
        for(int i = 0; i < outputTanks.length; i++) if(recipe.outputFluid != null && recipe.outputFluid.length > i) outputTanks[i].changeTankSize(BobMathUtil.max(outputTanks[i].getFill(), recipe.outputFluid[i].fill * 2, 24_000));
    }

    public ModuleChemicalPlant itemInput(int from) {
        for(int i = 0; i < inputSlots.length; i++) inputSlots[i] = from + i;
        return this;
    }
    public ModuleChemicalPlant itemOutput(int a) {
        outputSlots[0] = a;
        return this;
    }

    public ModuleChemicalPlant fluidInput(FluidTank a, FluidTank b, FluidTank c) {
        inputTanks[0] = a;
        inputTanks[1] = b;
        inputTanks[2] = c;
        return this;
    }

    public ModuleChemicalPlant fluidOutput(FluidTank a, FluidTank b, FluidTank c) {
        outputTanks[0] = a;
        outputTanks[1] = b;
        outputTanks[2] = c;
        return this;
    }
}