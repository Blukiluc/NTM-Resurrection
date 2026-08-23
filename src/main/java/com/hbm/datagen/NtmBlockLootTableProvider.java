package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.items.NtmItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class NtmBlockLootTableProvider extends BlockLootSubProvider {

    protected NtmBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        this.dropSelf(NtmBlocks.ORE_OIL.get());
        this.dropSelf(NtmBlocks.ORE_OIL_EMPTY.get());
        this.dropSelf(NtmBlocks.ORE_BEDROCK_OIL.get());
        this.dropSelf(NtmBlocks.ORE_URANIUM.get());
        this.dropSelf(NtmBlocks.ORE_URANIUM_SCORCHED.get());
        this.dropSelf(NtmBlocks.ORE_SCHRABIDIUM.get());
        this.dropSelf(NtmBlocks.ORE_NETHER_URANIUM.get());
        this.dropSelf(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED.get());
        this.dropSelf(NtmBlocks.ORE_NETHER_PLUTONIUM.get());
        this.dropSelf(NtmBlocks.ORE_NETHER_SCHRABIDIUM.get());
        this.dropSelf(NtmBlocks.ORE_TIKITE.get());
        this.dropSelf(NtmBlocks.ORE_GNEISS_URANIUM.get());
        this.dropSelf(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED.get());
        this.dropSelf(NtmBlocks.ORE_GNEISS_SCHRABIDIUM.get());

        this.dropSelf(NtmBlocks.BLOCK_SCRAP.get());

        this.dropSelf(NtmBlocks.BOBBLEHEAD.get());

        this.dropSelf(NtmBlocks.GRAVEL_OBSIDIAN.get());
        this.dropSelf(NtmBlocks.GRAVEL_DIAMOND.get());

        this.dropSelf(NtmBlocks.ASPHALT.get());
        this.dropSelf(NtmBlocks.ASPHALT_LIGHT.get());

        this.dropSelf(NtmBlocks.BRICK_CONCRETE.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_MOSSY.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_BROKEN.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_CRACKED.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_MARKED.get());
        this.dropSelf(NtmBlocks.BRICK_OBSIDIAN.get());
        this.dropSelf(NtmBlocks.BRICK_LIGHT.get());
        this.dropSelf(NtmBlocks.BRICK_ASBESTOS.get());
        this.dropSelf(NtmBlocks.BRICK_FIRE.get());

        this.add(NtmBlocks.BRICK_CONCRETE_SLAB.get(), this::createSlabItemTable);
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), this::createSlabItemTable);
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), this::createSlabItemTable);
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), this::createSlabItemTable);

        this.dropSelf(NtmBlocks.BRICK_CONCRETE_STAIRS.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get());
        this.dropSelf(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get());

        this.dropSelf(NtmBlocks.BARBED_WIRE.get());
        this.dropSelf(NtmBlocks.SPIKES.get());

        this.dropSelf(NtmBlocks.STONE_CRACKED.get());
        this.dropSelf(NtmBlocks.DIRT_OILY.get());
        this.dropSelf(NtmBlocks.DIRT_DEAD.get());
        this.dropSelf(NtmBlocks.ORE_OIL_SAND.get());
        this.dropSelf(NtmBlocks.SAND_OILY.get());
        this.dropSelf(NtmBlocks.SAND_RED_OILY.get());


        //ores
        // 1.Drop items :
        this.add(NtmBlocks.ORE_CINNABAR.get(),
                block -> createOreDrop(block, NtmItems.CINNABAR.get()));
        this.add(NtmBlocks.ORE_CINNABAR_DEEPSLATE.get(),
                block -> createOreDrop(block, NtmItems.CINNABAR.get()));

        this.add(NtmBlocks.ORE_LIGNITE.get(),
                block -> createOreDrop(block, NtmItems.LIGNITE.get()));
        this.add(NtmBlocks.ORE_DEEPSLATE_BROWNCOAL.get(),
                block -> createOreDrop(block, NtmItems.LIGNITE.get()));

        this.add(NtmBlocks.ORE_ASBESTOS.get(),
                block -> createOreDrop(block, NtmItems.INGOT_ASBESTOS.get()));
        this.add(NtmBlocks.ORE_DEEPSLATE_ASBESTOS.get(),
                block -> createOreDrop(block, NtmItems.INGOT_ASBESTOS.get()));

        this.add(NtmBlocks.ORE_RAREGROUND.get(),
                block -> createOreDrop(block, NtmItems.CHUNK_RARE.get()));
        this.add(NtmBlocks.ORE_RAREGROUND_DEEPSLATE.get(),
                block -> createOreDrop(block, NtmItems.CHUNK_RARE.get()));

        this.add(NtmBlocks.ORE_DEEPSLATE_ALEXANDRITE.get(),
                block -> createOreDrop(block, NtmItems.GEM_ALEXANDRITE.get()));


// 2.Dropself
        this.dropSelf(NtmBlocks.ORE_ALUMINUM.get());
        this.dropSelf(NtmBlocks.ORE_ALUMINUM_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_BERYLLIUM.get());
        this.dropSelf(NtmBlocks.ORE_BERYLLIUM_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_COBALT.get());
        this.dropSelf(NtmBlocks.ORE_COBALT_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_FLUORITE.get());
        this.dropSelf(NtmBlocks.ORE_DEEPSLATE_FLUORITE.get());
        this.dropSelf(NtmBlocks.ORE_LEAD.get());
        this.dropSelf(NtmBlocks.ORE_LEAD_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_SULFUR.get());
        this.dropSelf(NtmBlocks.ORE_DEEPSLATE_SULFUR.get());
        this.dropSelf(NtmBlocks.ORE_THORIUM.get());
        this.dropSelf(NtmBlocks.ORE_THORIUM_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_TITANIUM.get());
        this.dropSelf(NtmBlocks.ORE_TITANIUM_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_TUNGSTEN.get());
        this.dropSelf(NtmBlocks.ORE_DEEPSLATE_TUNGSTEN.get());
        this.dropSelf(NtmBlocks.ORE_URANIUM_H.get());
        this.dropSelf(NtmBlocks.ORE_URANIUM_DEEPSLATE.get());
        this.dropSelf(NtmBlocks.ORE_DEEPSLATE_COLTAN.get());
        this.dropSelf(NtmBlocks.ORE_DEEPSLATE_NITER.get());
        this.dropSelf(NtmBlocks.ORE_SEQUESTRUM.get());

        this.add(NtmBlocks.WASTE_EARTH.get(), block -> createSingleItemTable(Blocks.DIRT));
        this.dropSelf(NtmBlocks.WASTE_MYCELIUM.get());
        this.dropSelf(NtmBlocks.WASTE_TRINITITE.get());
        this.dropSelf(NtmBlocks.WASTE_TRINITITE_RED.get());
        this.add(NtmBlocks.WASTE_LOG.get(), block ->
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(NtmItems.BURNT_BARK.get()).when(LootItemRandomChanceCondition.randomChance(0.001f)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))),
                                        LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                                ))
                )
        );
        this.add(NtmBlocks.WASTE_LEAVES.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.dropSelf(NtmBlocks.WASTE_PLANKS.get());
        this.add(NtmBlocks.FROZEN_DIRT.get(), block -> createSingleItemTable(Items.SNOWBALL));
        this.add(NtmBlocks.FROZEN_GRASS.get(), block -> createSingleItemTable(Items.SNOWBALL));
        this.add(NtmBlocks.FROZEN_LOG.get(), block -> createSingleItemTable(Items.SNOWBALL));
        this.add(NtmBlocks.FROZEN_PLANKS.get(), block -> createSingleItemTable(Items.SNOWBALL));
        this.add(NtmBlocks.LEAVES_LAYER.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.dropSelf(NtmBlocks.OIL_SPILL.get());
        this.dropSelf(NtmBlocks.FALLOUT.get()); // todo make item drop
        this.dropSelf(NtmBlocks.SELLAFIELD_SLAKED.get());
        this.add(NtmBlocks.ORE_SELLAFIELD_EMERALD.get(), block -> this.createOreDrop(block, Items.EMERALD));
        this.add(NtmBlocks.ORE_SELLAFIELD_DIAMOND.get(), block -> this.createOreDrop(block, Items.DIAMOND));
        // SELLAFIELD_BEDROCK has no drops

        this.dropSelf(NtmBlocks.NUKE_GADGET.get());
        this.dropSelf(NtmBlocks.NUKE_LITTLE_BOY.get());
        this.dropSelf(NtmBlocks.NUKE_FAT_MAN.get());
        this.dropSelf(NtmBlocks.NUKE_IVY_MIKE.get());
        this.dropSelf(NtmBlocks.NUKE_TSAR_BOMBA.get());
        this.dropSelf(NtmBlocks.NUKE_PROTOTYPE.get());
        this.dropSelf(NtmBlocks.NUKE_FLEIJA.get());
        this.dropSelf(NtmBlocks.NUKE_N2.get());
        this.dropSelf(NtmBlocks.NUKE_FSTBMB.get());

        // CRASHED_BOMB has no drops
        this.dropSelf(NtmBlocks.DYNAMITE.get());
        this.dropSelf(NtmBlocks.TNT.get());
        this.dropSelf(NtmBlocks.SEMTEX.get());
        this.dropSelf(NtmBlocks.C4.get());
        this.dropSelf(NtmBlocks.FISSURE_BOMB.get());

        this.dropSelf(NtmBlocks.MINE_AP.get());
        this.dropSelf(NtmBlocks.MINE_HE.get());
        this.dropSelf(NtmBlocks.MINE_SHRAP.get());
        this.dropSelf(NtmBlocks.MINE_FAT.get());
        this.dropSelf(NtmBlocks.MINE_NAVAL.get());

        this.dropSelf(NtmBlocks.DET_CORD.get());
        this.dropSelf(NtmBlocks.DET_CHARGE.get());
        this.dropSelf(NtmBlocks.DET_NUKE.get());
        this.dropSelf(NtmBlocks.DET_MINER.get());
        this.dropSelf(NtmBlocks.BARREL_RED.get());
        this.dropSelf(NtmBlocks.BARREL_PINK.get());
        this.dropSelf(NtmBlocks.BARREL_LOX.get());
        this.dropSelf(NtmBlocks.BARREL_TAINT.get());

        this.dropSelf(NtmBlocks.GEIGER.get());

        this.dropSelf(NtmBlocks.MACHINE_PRESS.get());
        this.dropSelf(NtmBlocks.MACHINE_ELECTRIC_PRESS.get());
        this.dropSelf(NtmBlocks.PRESS_PREHEATER.get());
        this.dropSelf(NtmBlocks.MACHINE_SHREDDER.get());

        this.dropSelf(NtmBlocks.RED_CABLE.get());
        this.dropSelf(NtmBlocks.RED_CABLE_CLASSIC.get());
        this.dropSelf(NtmBlocks.RED_CABLE_PAINTABLE.get());
        this.dropSelf(NtmBlocks.RED_CABLE_BOX_HUGE.get());
        this.dropSelf(NtmBlocks.RED_CABLE_BOX_LARGE.get());
        this.dropSelf(NtmBlocks.RED_CABLE_BOX_MEDIUM.get());
        this.dropSelf(NtmBlocks.RED_CABLE_BOX_SMALL.get());
        this.dropSelf(NtmBlocks.RED_CABLE_BOX_TINY.get());
        this.dropSelf(NtmBlocks.RED_WIRE_COATED.get());
        this.dropSelf(NtmBlocks.RED_CONNECTOR.get());
        this.dropSelf(NtmBlocks.RED_CONNECTOR_SUPER.get());
        this.dropSelf(NtmBlocks.RED_PYLON.get());
        this.dropSelf(NtmBlocks.RED_PYLON_MEDIUM_WOOD.get());
        this.dropSelf(NtmBlocks.RED_PYLON_MEDIUM_WOOD_TRANSFORMER.get());
        this.dropSelf(NtmBlocks.RED_PYLON_MEDIUM_STEEL.get());
        this.dropSelf(NtmBlocks.RED_PYLON_MEDIUM_STEEL_TRANSFORMER.get());
        this.dropSelf(NtmBlocks.RED_PYLON_LARGE.get());
        this.dropSelf(NtmBlocks.RED_PYLON_STEEL.get());
        this.dropSelf(NtmBlocks.SUBSTATION.get());

        this.dropSelf(NtmBlocks.FLUID_DUCT_NEO.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_IRON_HUGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_COPPER_HUGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_HUGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_IRON_LARGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_COPPER_LARGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_LARGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_IRON_MEDIUM.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_COPPER_MEDIUM.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_MEDIUM.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_IRON_SMALL.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_COPPER_SMALL.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_SMALL.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_IRON_TINY.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_COPPER_TINY.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_TINY.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_EXHAUST_HUGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_EXHAUST_LARGE.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_EXHAUST_MEDIUM.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_EXHAUST_SMALL.get());
        this.dropSelf(NtmBlocks.FLUID_DUCT_EXHAUST_TINY.get());
        this.dropSelf(NtmBlocks.PIPE_ANCHOR.get());

        this.dropSelf(NtmBlocks.HEATER_FIREBOX.get());
        this.dropSelf(NtmBlocks.HEATER_OVEN.get());
        this.dropSelf(NtmBlocks.HEATER_FLUID_BURNER.get());
        this.dropSelf(NtmBlocks.HEATER_ELECTRIC.get());
        this.dropSelf(NtmBlocks.HEATER_HEATEX.get());
        this.dropSelf(NtmBlocks.MACHINE_BATTERY_SOCKET.get());
        this.dropSelf(NtmBlocks.MACHINE_BATTERY_REDD.get());
        this.dropSelf(NtmBlocks.MACHINE_ASSEMBLY_MACHINE.get());
        this.dropSelf(NtmBlocks.MACHINE_ASSEMBLY_FACTORY.get());
        this.dropSelf(NtmBlocks.MACHINE_PREC_ASS.get());
        this.dropSelf(NtmBlocks.MACHINE_CHEMICAL_PLANT.get());
        this.dropSelf(NtmBlocks.MACHINE_CHEMICAL_FACTORY.get());
        this.dropSelf(NtmBlocks.MACHINE_PUREX.get());
        this.dropSelf(NtmBlocks.MACHINE_ORE_ACIDIZER.get());
        this.dropSelf(NtmBlocks.MACHINE_WOOD_BURNER.get());
        this.dropSelf(NtmBlocks.MACHINE_DIESEL.get());
        this.dropSelf(NtmBlocks.MACHINE_COMBUSTION_ENGINE.get());
        this.dropSelf(NtmBlocks.MACHINE_TURBOFAN.get());
        this.dropSelf(NtmBlocks.MACHINE_STIRLING.get());
        this.dropSelf(NtmBlocks.MACHINE_STIRLING_STEEL.get());
        this.dropSelf(NtmBlocks.MACHINE_STIRLING_CREATIVE.get());
        this.dropSelf(NtmBlocks.MACHINE_SAWMILL.get());
        this.dropSelf(NtmBlocks.PUMP_STEAM.get());
        this.dropSelf(NtmBlocks.PUMP_ELECTRIC.get());
        this.dropSelf(NtmBlocks.MACHINE_CONDENSER.get());
        this.dropSelf(NtmBlocks.MACHINE_CONDENSER_POWERED.get());
        this.dropSelf(NtmBlocks.MACHINE_STEAM_ENGINE.get());
        this.dropSelf(NtmBlocks.MACHINE_CENTRIFUGE.get());
        this.dropSelf(NtmBlocks.MACHINE_GAS_CENTRIFUGE.get());
        this.dropSelf(NtmBlocks.MACHINE_SOLDERING_STATION.get());
        this.dropSelf(NtmBlocks.MACHINE_ARC_WELDER.get());
        this.dropSelf(NtmBlocks.MACHINE_MIXER.get());
        this.dropSelf(NtmBlocks.MACHINE_FEL.get());
        this.dropSelf(NtmBlocks.MACHINE_SILEX.get());
        this.dropSelf(NtmBlocks.MACHINE_INTAKE.get());
        this.dropSelf(NtmBlocks.MACHINE_ELECTROLYSER.get());
        this.dropSelf(NtmBlocks.MACHINE_HEAT_BOILER.get());
        this.dropSelf(NtmBlocks.MACHINE_SOLAR_BOILER.get());
        this.dropSelf(NtmBlocks.SOLAR_MIRROR.get());
        this.dropSelf(NtmBlocks.MACHINE_INDUSTRIAL_BOILER.get());
        this.dropSelf(NtmBlocks.MACHINE_INDUSTRIAL_TURBINE.get());
        this.dropSelf(NtmBlocks.MACHINE_CHUNGUS.get());
        this.dropSelf(NtmBlocks.MACHINE_TOWER_SMALL.get());
        this.dropSelf(NtmBlocks.MACHINE_TOWER_LARGE.get());
        this.dropSelf(NtmBlocks.MACHINE_GEOTHERMAL_HEAT_EXCHANGER.get());
        this.dropSelf(NtmBlocks.MACHINE_FURNACE_BRICK.get());
        this.dropSelf(NtmBlocks.MACHINE_ELECTRIC_FURNACE.get());
        this.dropSelf(NtmBlocks.MACHINE_FURNACE_IRON.get());
        this.dropSelf(NtmBlocks.MACHINE_FURNACE_STEEL.get());
        this.dropSelf(NtmBlocks.MACHINE_COMBINATION_OVEN.get());
        this.dropSelf(NtmBlocks.MACHINE_BLAST_FURNACE.get());
        this.dropSelf(NtmBlocks.MACHINE_ROTARY_FURNACE.get());

        this.dropSelf(NtmBlocks.MACHINE_FLUID_TANK.get());
        this.dropSelf(NtmBlocks.MACHINE_BIG_ASS_TANK.get());
        this.dropSelf(NtmBlocks.MACHINE_DRAIN.get());

        this.dropSelf(NtmBlocks.MACHINE_OIL_DERRICK.get());
        this.dropSelf(NtmBlocks.MACHINE_PUMPJACK.get());
        this.dropSelf(NtmBlocks.MACHINE_FRACKING_TOWER.get());
        this.dropSelf(NtmBlocks.MACHINE_REFINERY.get());
        this.dropSelf(NtmBlocks.MACHINE_VACUUM_REFINERY.get());
        this.dropSelf(NtmBlocks.MACHINE_FRACTION_TOWER.get());
        this.dropSelf(NtmBlocks.FRACTION_SPACER.get());
        this.dropSelf(NtmBlocks.MACHINE_CATALYTIC_REFORMER.get());
        this.dropSelf(NtmBlocks.MACHINE_CATALYTIC_CRACKING_TOWER.get());
        this.dropSelf(NtmBlocks.MACHINE_COMPRESSOR.get());
        this.dropSelf(NtmBlocks.MACHINE_COMPRESSOR_COMPACT.get());
        this.dropSelf(NtmBlocks.MACHINE_FLARE.get());
        this.dropSelf(NtmBlocks.MACHINE_SMOKESTACK.get());
        this.dropSelf(NtmBlocks.MACHINE_SMOKESTACK_INDUSTRIAL.get());

        this.dropSelf(NtmBlocks.MACHINE_SATLINKER.get());

        this.dropSelf(NtmBlocks.CRATE_IRON.get());
        this.dropSelf(NtmBlocks.CRATE_TUNGSTEN.get());
        this.dropSelf(NtmBlocks.CRATE_STEEL.get());
        this.dropSelf(NtmBlocks.CRATE_DESH.get());

        this.dropSelf(NtmBlocks.DECONTAMINATOR.get());

        this.dropSelf(NtmBlocks.PWR_CONTROLLER.get());

        this.add(NtmBlocks.BALEFIRE.get(), noDrop());
        this.add(NtmBlocks.FIRE_DIGAMMA.get(), noDrop());
        this.add(NtmBlocks.VOLCANO_CORE.get(), noDrop());
        this.add(NtmBlocks.VOLCANO_RAD_CORE.get(), noDrop());

        this.dropSelf(NtmBlocks.LAUNCH_PAD.get());
        this.dropSelf(NtmBlocks.SOYUZ_LAUNCHER.get());

        // liquid blocks has no drops

        // gas blocks has no drops

        // ??? blocks has no drops
    }

    @Override protected Iterable<Block> getKnownBlocks() { return NtmBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator; }
}
