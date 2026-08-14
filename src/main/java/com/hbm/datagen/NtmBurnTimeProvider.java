package com.hbm.datagen;

import com.hbm.inventory.NtmTags;
import com.hbm.items.NtmItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class NtmBurnTimeProvider extends DataMapProvider {

    public NtmBurnTimeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void gather(HolderLookup.@NotNull Provider lookupProvider) {
        var furnaceFuels = builder(NeoForgeDataMaps.FURNACE_FUELS);


        furnaceFuels.add(NtmItems.LIGNITE.getId(), new FurnaceFuel(1200), false);
        furnaceFuels.add(NtmTags.Items.COKE, new FurnaceFuel(3200), false);
        furnaceFuels.add(NtmItems.SOLID_FUEL.getId(), new FurnaceFuel(3200), false);
        furnaceFuels.add(NtmItems.ROCKET_FUEL.getId(), new FurnaceFuel(6400), false);
        furnaceFuels.add(NtmItems.SOLID_FUEL_PRESTO.getId(), new FurnaceFuel(9600), false);
        furnaceFuels.add(NtmItems.SOLID_FUEL_PRESTO_TRIPLET.getId(), new FurnaceFuel(28800), false);





        furnaceFuels.add(NtmItems.SOLID_FUEL_BF.getId(), new FurnaceFuel(12800), false);
        furnaceFuels.add(NtmItems.SOLID_FUEL_PRESTO_BF.getId(), new FurnaceFuel(38400), false);
        furnaceFuels.add(NtmItems.SOLID_FUEL_PRESTO_TRIPLET_BF.getId(), new FurnaceFuel(115200), false);




    }
}