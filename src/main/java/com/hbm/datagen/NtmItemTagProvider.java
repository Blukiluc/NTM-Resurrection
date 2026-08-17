package com.hbm.datagen;

import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.hbm.inventory.NtmTags.Items.*;

public class NtmItemTagProvider extends ItemTagsProvider {

    public NtmItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper helper) {
        super(output, provider, blockTags, NuclearTechMod.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        /*
         * TANKS
         */
        this.tag(UNIVERSAL_TANK).add(NtmItems.FLUID_TANK_FULL.get());
        this.tag(HAZARD_TANK).add(NtmItems.FLUID_TANK_LEAD_FULL.get());
        this.tag(UNIVERSAL_BARREL).add(NtmItems.FLUID_BARREL_FULL.get());
        // W COKES?
        this.tag(COKE).add(NtmItems.COKE_COAL.get());
        this.tag(COKE).add(NtmItems.COKE_PETROLEUM.get());
        this.tag(COKE).add(NtmItems.COKE_LIGNITE.get());
        // SOLID FUELS
        this.tag(SOLID_FUEL).add(NtmItems.SOLID_FUEL.get());
        this.tag(SOLID_FUEL).add(NtmItems.SOLID_FUEL_BF.get());
        // PRESTO LOGS
        this.tag(PRESTO_LOG).add(NtmItems.SOLID_FUEL_PRESTO.get());
        this.tag(PRESTO_LOG).add(NtmItems.SOLID_FUEL_PRESTO_BF.get());
        this.tag(PRESTO_LOG).add(NtmItems.SOLID_FUEL_PRESTO_TRIPLET.get());
        this.tag(PRESTO_LOG).add(NtmItems.SOLID_FUEL_PRESTO_TRIPLET_BF.get());








    }
}
