package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.hbm.inventory.NtmTags.Blocks.*;
import static net.neoforged.neoforge.common.Tags.Blocks.PUMPKINS;

public class NtmBlockTagProvider extends BlockTagsProvider {

    public NtmBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NuclearTechMod.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked") // no
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(PLANTS)
                .addTags(
                        BlockTags.FLOWERS,
                        BlockTags.SAPLINGS,
                        BlockTags.CROPS,
                        PUMPKINS
                )
                .add(
                        Blocks.SHORT_GRASS,
                        Blocks.FERN,
                        Blocks.DEAD_BUSH,
                        Blocks.VINE,
                        Blocks.TALL_GRASS,
                        Blocks.LARGE_FERN
                );

        // im probably dumb but i dont know any tags like this
        tag(ACTUALLY_STONE)
                .add(Blocks.COAL_ORE)
                .add(Blocks.IRON_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.DIAMOND_ORE)

                .add(Blocks.DEEPSLATE_COAL_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE)
                .add(Blocks.DEEPSLATE_COPPER_ORE)
                .add(Blocks.DEEPSLATE_LAPIS_ORE)
                .add(Blocks.DEEPSLATE_DIAMOND_ORE)

                .add(Blocks.GRANITE)
                .add(Blocks.ANDESITE)
                .add(Blocks.DIORITE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.TUFF)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.SANDSTONE)

                .add(NtmBlocks.BRICK_CONCRETE.get())
                .add(NtmBlocks.BRICK_CONCRETE_CRACKED.get())
                .add(NtmBlocks.BRICK_CONCRETE_BROKEN.get())
                .add(NtmBlocks.BRICK_CONCRETE_MOSSY.get())
                .add(NtmBlocks.BRICK_CONCRETE_MARKED.get())
                .add(NtmBlocks.BRICK_LIGHT.get())

                .add(Blocks.STONE);

        tag(GROUND)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.MUD)
                .add(Blocks.MUDDY_MANGROVE_ROOTS)
                .add(Blocks.MANGROVE_ROOTS)
                .add(Blocks.GRAVEL)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.MYCELIUM)
                .add(Blocks.SAND)
                .add(Blocks.DIRT);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        NtmBlocks.ASPHALT.get(),
                        NtmBlocks.ASPHALT_LIGHT.get(),

                        NtmBlocks.BRICK_CONCRETE.get(),
                        NtmBlocks.BRICK_CONCRETE_MOSSY.get(),
                        NtmBlocks.BRICK_CONCRETE_CRACKED.get(),
                        NtmBlocks.BRICK_CONCRETE_BROKEN.get(),
                        NtmBlocks.BRICK_CONCRETE_MARKED.get(),
                        NtmBlocks.BRICK_OBSIDIAN.get(),
                        NtmBlocks.BRICK_LIGHT.get(),
                        NtmBlocks.BRICK_ASBESTOS.get(),
                        NtmBlocks.BRICK_FIRE.get(),

                        NtmBlocks.BARBED_WIRE.get(),
                        NtmBlocks.SPIKES.get(),

                        NtmBlocks.BRICK_CONCRETE_STAIRS.get(),
                        NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(),
                        NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(),
                        NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(),

                        NtmBlocks.BRICK_CONCRETE_SLAB.get(),
                        NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(),
                        NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(),
                        NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(),

                        NtmBlocks.NUKE_GADGET.get(),
                        NtmBlocks.NUKE_LITTLE_BOY.get(),
                        NtmBlocks.NUKE_FAT_MAN.get(),
                        NtmBlocks.NUKE_IVY_MIKE.get(),
                        NtmBlocks.NUKE_TSAR_BOMBA.get(),
                        NtmBlocks.NUKE_PROTOTYPE.get(),
                        NtmBlocks.NUKE_FLEIJA.get(),
                        NtmBlocks.NUKE_N2.get(),
                        NtmBlocks.NUKE_FSTBMB.get(),

                        NtmBlocks.SELLAFIELD_SLAKED.get(),
                        NtmBlocks.ORE_SELLAFIELD_EMERALD.get(),
                        NtmBlocks.ORE_SELLAFIELD_DIAMOND.get(),

                        NtmBlocks.MACHINE_FLUID_TANK.get(),
                        NtmBlocks.MACHINE_BIG_ASS_TANK.get(),
                        NtmBlocks.MACHINE_DRAIN.get(),
                        NtmBlocks.MACHINE_FLARE.get(),
                        NtmBlocks.MACHINE_SMOKESTACK.get(),
                        NtmBlocks.MACHINE_SMOKESTACK_INDUSTRIAL.get(),
                        NtmBlocks.MACHINE_INDUSTRIAL_TURBINE.get(),
                        NtmBlocks.MACHINE_CHUNGUS.get(),
                        NtmBlocks.MACHINE_TOWER_SMALL.get(),
                        NtmBlocks.MACHINE_TOWER_LARGE.get(),
                        NtmBlocks.MACHINE_BATTERY_REDD.get(),
                        NtmBlocks.MACHINE_BATTERY_SOCKET.get(),
                        NtmBlocks.RED_CABLE.get(),
                        NtmBlocks.RED_CABLE_CLASSIC.get(),
                        NtmBlocks.RED_CABLE_PAINTABLE.get(),
                        NtmBlocks.RED_CABLE_BOX_HUGE.get(),
                        NtmBlocks.RED_CABLE_BOX_LARGE.get(),
                        NtmBlocks.RED_CABLE_BOX_MEDIUM.get(),
                        NtmBlocks.RED_CABLE_BOX_SMALL.get(),
                        NtmBlocks.RED_CABLE_BOX_TINY.get(),
                        NtmBlocks.RED_WIRE_COATED.get(),
                        NtmBlocks.RED_CONNECTOR.get(),
                        NtmBlocks.RED_CONNECTOR_SUPER.get(),
                        NtmBlocks.RED_PYLON_MEDIUM_STEEL.get(),
                        NtmBlocks.RED_PYLON_MEDIUM_STEEL_TRANSFORMER.get(),
                        NtmBlocks.RED_PYLON_LARGE.get(),
                        NtmBlocks.RED_PYLON_STEEL.get(),
                        NtmBlocks.SUBSTATION.get(),
                        NtmBlocks.FLUID_DUCT_NEO.get(),
                        NtmBlocks.FLUID_DUCT_BOX_IRON_HUGE.get(),
                        NtmBlocks.FLUID_DUCT_BOX_COPPER_HUGE.get(),
                        NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_HUGE.get(),
                        NtmBlocks.FLUID_DUCT_BOX_IRON_LARGE.get(),
                        NtmBlocks.FLUID_DUCT_BOX_COPPER_LARGE.get(),
                        NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_LARGE.get(),
                        NtmBlocks.FLUID_DUCT_BOX_IRON_MEDIUM.get(),
                        NtmBlocks.FLUID_DUCT_BOX_COPPER_MEDIUM.get(),
                        NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_MEDIUM.get(),
                        NtmBlocks.FLUID_DUCT_BOX_IRON_SMALL.get(),
                        NtmBlocks.FLUID_DUCT_BOX_COPPER_SMALL.get(),
                        NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_SMALL.get(),
                        NtmBlocks.FLUID_DUCT_BOX_IRON_TINY.get(),
                        NtmBlocks.FLUID_DUCT_BOX_COPPER_TINY.get(),
                        NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_TINY.get(),
                        NtmBlocks.FLUID_DUCT_EXHAUST_HUGE.get(),
                        NtmBlocks.FLUID_DUCT_EXHAUST_LARGE.get(),
                        NtmBlocks.FLUID_DUCT_EXHAUST_MEDIUM.get(),
                        NtmBlocks.FLUID_DUCT_EXHAUST_SMALL.get(),
                        NtmBlocks.FLUID_DUCT_EXHAUST_TINY.get(),
                        NtmBlocks.PIPE_ANCHOR.get(),
                        NtmBlocks.MACHINE_STIRLING.get(),
                        NtmBlocks.MACHINE_STIRLING_STEEL.get(),
                        NtmBlocks.MACHINE_STIRLING_CREATIVE.get(),
                        NtmBlocks.MACHINE_SAWMILL.get(),
                        NtmBlocks.PUMP_STEAM.get(),
                        NtmBlocks.PUMP_ELECTRIC.get(),
                        NtmBlocks.MACHINE_CONDENSER.get(),
                        NtmBlocks.MACHINE_CONDENSER_POWERED.get(),
                        NtmBlocks.MACHINE_STEAM_ENGINE.get(),
                        NtmBlocks.MACHINE_RTG.get(),
                        NtmBlocks.LAUNCH_PAD.get(),
                        NtmBlocks.SOYUZ_LAUNCHER.get(),
                        NtmBlocks.MACHINE_SATLINKER.get(),

                        NtmBlocks.MACHINE_CRUCIBLE.get(),
                        NtmBlocks.FOUNDRY_MOLD.get(),
                        NtmBlocks.FOUNDRY_BASIN.get(),
                        NtmBlocks.FOUNDRY_CHANNEL.get(),
                        NtmBlocks.FOUNDRY_TANK.get(),
                        NtmBlocks.FOUNDRY_OUTLET.get(),
                        NtmBlocks.FOUNDRY_SLAGTAP.get(),
                        NtmBlocks.MOLTEN_SLAG.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        NtmBlocks.WASTE_LOG.get(),
                        NtmBlocks.WASTE_PLANKS.get(),
                        NtmBlocks.FROZEN_LOG.get(),
                        NtmBlocks.FROZEN_PLANKS.get(),
                        NtmBlocks.RED_PYLON.get(),
                        NtmBlocks.RED_PYLON_MEDIUM_WOOD.get(),
                        NtmBlocks.RED_PYLON_MEDIUM_WOOD_TRANSFORMER.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(
                        NtmBlocks.WASTE_EARTH.get(),
                        NtmBlocks.WASTE_MYCELIUM.get(),
                        NtmBlocks.WASTE_TRINITITE.get(),
                        NtmBlocks.WASTE_TRINITITE_RED.get(),
                        NtmBlocks.FROZEN_DIRT.get(),
                        NtmBlocks.FROZEN_GRASS.get(),
                        NtmBlocks.OIL_SPILL.get(),
                        NtmBlocks.FALLOUT.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(
                        NtmBlocks.WASTE_LEAVES.get(),
                        NtmBlocks.LEAVES_LAYER.get()
                );

        // vanilla compat
        this.tag(BlockTags.STRIDER_WARM_BLOCKS).add(NtmBlocks.VOLCANIC_LAVA.get());
    }
}
