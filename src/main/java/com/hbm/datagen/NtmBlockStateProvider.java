package com.hbm.datagen;

import com.google.gson.JsonObject;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ICustomBlockModelRegister;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BarbedWireBlock;
import com.hbm.blocks.generic.LayeringBlock;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.blocks.machine.MachineElectricFurnaceBlock;
import com.hbm.blocks.machine.MachineFurnaceBrickBlock;
import com.hbm.blocks.network.FluidDuctConnectingBlock;
import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.model.loader.NtmGeometry.BakedModelType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class NtmBlockStateProvider extends BlockStateProvider {

    public NtmBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, NuclearTechMod.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {

        NtmBlocks.BLOCKS.getEntries().forEach(holder -> {
            Block block = holder.get();

            if(block instanceof ICustomBlockModelRegister icbmr) {
                ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
                icbmr.registerModel(this, loc);
            }
        });

        this.simpleCubeAllBlock(NtmBlocks.ORE_OIL);
        this.simpleCubeAllBlock(NtmBlocks.ORE_OIL_EMPTY);
        this.simpleCubeAllBlock(NtmBlocks.ORE_BEDROCK_OIL);
        this.simpleCubeAllBlock(NtmBlocks.ORE_URANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_URANIUM_SCORCHED);
        this.simpleCubeAllBlock(NtmBlocks.ORE_SCHRABIDIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_URANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_SCHRABIDIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_TIKITE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_GNEISS_URANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_PLUTONIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_GNEISS_SCHRABIDIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_ALUMINUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_ALUMINUM_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_ASBESTOS);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_ASBESTOS);
        this.simpleCubeAllBlock(NtmBlocks.ORE_BERYLLIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_BERYLLIUM_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_CINNABAR);
        this.simpleCubeAllBlock(NtmBlocks.ORE_CINNABAR_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_COBALT);
        this.simpleCubeAllBlock(NtmBlocks.ORE_COBALT_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_LIGNITE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_BROWNCOAL);
        this.simpleCubeAllBlock(NtmBlocks.ORE_FLUORITE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_FLUORITE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_LEAD);
        this.simpleCubeAllBlock(NtmBlocks.ORE_LEAD_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_RAREGROUND);
        this.simpleCubeAllBlock(NtmBlocks.ORE_RAREGROUND_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_SULFUR);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_SULFUR);
        this.simpleCubeAllBlock(NtmBlocks.ORE_THORIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_THORIUM_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_TITANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_TITANIUM_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_TUNGSTEN);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_TUNGSTEN);
        this.simpleCubeAllBlock(NtmBlocks.ORE_URANIUM_H);
        this.simpleCubeAllBlock(NtmBlocks.ORE_URANIUM_DEEPSLATE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_ALEXANDRITE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_COLTAN);
        this.simpleCubeAllBlock(NtmBlocks.ORE_DEEPSLATE_NITER);
        this.simpleCubeAllBlock(NtmBlocks.ORE_SEQUESTRUM);

        this.simpleCubeAllBlock(NtmBlocks.BLOCK_SCRAP);

        this.particleOnlyBlock(NtmBlocks.BOBBLEHEAD, modLoc("block/block_steel"));
        this.simpleCubeAllBlock(NtmBlocks.GRAVEL_OBSIDIAN);
        this.simpleCubeAllBlock(NtmBlocks.GRAVEL_DIAMOND);

        this.simpleCubeAllBlock(NtmBlocks.ASPHALT);
        this.simpleCubeAllBlock(NtmBlocks.ASPHALT_LIGHT);

        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE_MOSSY);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE_CRACKED);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE_BROKEN);
        this.simpleBlockWithItem(
                NtmBlocks.BRICK_CONCRETE_MARKED,
                this.models().cubeColumn(
                        name(NtmBlocks.BRICK_CONCRETE_MARKED),
                        blockTexture(NtmBlocks.BRICK_CONCRETE_MARKED),
                        blockTexture(NtmBlocks.BRICK_CONCRETE)
                )
        );
        this.simpleCubeAllBlock(NtmBlocks.BRICK_OBSIDIAN);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_LIGHT);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_ASBESTOS);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_FIRE);

        this.slabBlock(NtmBlocks.BRICK_CONCRETE_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE), blockTexture(NtmBlocks.BRICK_CONCRETE));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN));
        this.blockItem(NtmBlocks.BRICK_CONCRETE_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB);

        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN));
        this.blockItem(NtmBlocks.BRICK_CONCRETE_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

        this.registerBarbedWire();
        this.registerSpikes();

        this.simpleCubeAllBlock(NtmBlocks.OIL_PIPE);
        // todo grass
        this.simpleCubeAllBlock(NtmBlocks.STONE_CRACKED);
        this.simpleCubeAllBlock(NtmBlocks.DIRT_OILY);
        this.simpleCubeAllBlock(NtmBlocks.DIRT_DEAD);
        this.simpleCubeAllBlock(NtmBlocks.ORE_OIL_SAND);
        this.simpleCubeAllBlock(NtmBlocks.SAND_OILY);
        this.simpleCubeAllBlock(NtmBlocks.SAND_RED_OILY);

        this.simpleCubeBottomTopBlock(NtmBlocks.WASTE_EARTH);
        this.simpleBlockWithItem(
                NtmBlocks.WASTE_MYCELIUM,
                this.models().cubeBottomTop(
                        name(NtmBlocks.WASTE_MYCELIUM),
                        blockTexture(NtmBlocks.WASTE_MYCELIUM, "_side"),
                        blockTexture(NtmBlocks.WASTE_EARTH, "_bottom"),
                        blockTexture(NtmBlocks.WASTE_MYCELIUM, "_top")
                )
        );
        this.simpleCubeAllBlock(NtmBlocks.WASTE_TRINITITE);
        this.simpleCubeAllBlock(NtmBlocks.WASTE_TRINITITE_RED);
        this.logBlock(NtmBlocks.WASTE_LOG.get());
        this.simpleBlockWithItem(
                NtmBlocks.WASTE_LEAVES,
                this.models().cubeAll(
                        name(NtmBlocks.WASTE_LEAVES),
                        blockTexture(NtmBlocks.WASTE_LEAVES)
                ).renderType("cutout_mipped")
        );
        this.simpleCubeAllBlock(NtmBlocks.WASTE_PLANKS);
        this.simpleCubeAllBlock(NtmBlocks.FROZEN_DIRT);
        this.simpleBlockWithItem(
                NtmBlocks.FROZEN_GRASS,
                this.models().cubeBottomTop(
                        name(NtmBlocks.FROZEN_GRASS),
                        blockTexture(NtmBlocks.FROZEN_GRASS, "_side"),
                        blockTexture(NtmBlocks.FROZEN_DIRT),
                        blockTexture(NtmBlocks.FROZEN_GRASS, "_top")
                )
        );
        this.logBlock(NtmBlocks.FROZEN_LOG.get());
        this.simpleCubeAllBlock(NtmBlocks.FROZEN_PLANKS);
        this.layeringBlock(NtmBlocks.LEAVES_LAYER.get(), modLoc("block/waste_leaves"), "layering");
        this.layeringBlock(NtmBlocks.OIL_SPILL.get(), modLoc("block/oil_spill"), "oil_spill");
        ResourceLocation texture = modLoc("block/ash");
        ModelFile falloutModel = models()
                .getBuilder("fallout")
                .parent(new ModelFile.UncheckedModelFile("block/block"))
                .texture("all", texture)
                .texture("particle", texture)
                .element()
                .from(0, 0, 0)
                .to(16, 2, 16)
                .face(Direction.UP).texture("#all").end()
                .face(Direction.DOWN).texture("#all").end()
                .face(Direction.NORTH).texture("#all").end()
                .face(Direction.SOUTH).texture("#all").end()
                .face(Direction.WEST).texture("#all").end()
                .face(Direction.EAST).texture("#all").end()
                .end();
        this.getVariantBuilder(NtmBlocks.FALLOUT.get()).partialState().setModels(new ConfiguredModel(falloutModel));
        this.sellafieldSlaked(NtmBlocks.SELLAFIELD_SLAKED.get(), "sellafield_slaked");
        this.sellafieldOre(NtmBlocks.ORE_SELLAFIELD_DIAMOND.get(), "sellafield_ore_diamond", "block/ore_diamond_overlay");
        this.sellafieldOre(NtmBlocks.ORE_SELLAFIELD_EMERALD.get(), "sellafield_ore_emerald", "block/ore_emerald_overlay");
        this.sellafieldSlaked(NtmBlocks.SELLAFIELD_BEDROCK.get(), "sellafield_bedrock");

        this.particleOnlyBlock(NtmBlocks.NUKE_GADGET, blockTexture(NtmBlocks.NUKE_GADGET));
        this.particleOnlyBlock(NtmBlocks.NUKE_LITTLE_BOY, blockTexture(NtmBlocks.NUKE_LITTLE_BOY));
        this.particleOnlyBlock(NtmBlocks.NUKE_FAT_MAN, blockTexture(NtmBlocks.NUKE_FAT_MAN));
        this.particleOnlyBlock(NtmBlocks.NUKE_IVY_MIKE, blockTexture(NtmBlocks.NUKE_IVY_MIKE));
        this.particleOnlyBlock(NtmBlocks.NUKE_TSAR_BOMBA, blockTexture(NtmBlocks.NUKE_TSAR_BOMBA));
        this.particleOnlyBlock(NtmBlocks.NUKE_PROTOTYPE, blockTexture(NtmBlocks.NUKE_PROTOTYPE));
        this.particleOnlyBlock(NtmBlocks.NUKE_FLEIJA, blockTexture(NtmBlocks.NUKE_FLEIJA));
        this.particleOnlyBlock(NtmBlocks.NUKE_N2, blockTexture(NtmBlocks.NUKE_N2));
        this.particleOnlyBlock(NtmBlocks.NUKE_FSTBMB, blockTexture(NtmBlocks.NUKE_FSTBMB));

        this.particleOnlyBlock(NtmBlocks.CRASHED_BOMB, modLoc("block/block_rust"), true);
        this.simpleCubeBottomTopBlock(NtmBlocks.DYNAMITE);
        this.simpleCubeBottomTopBlock(NtmBlocks.TNT);
        this.simpleCubeBottomTopBlock(NtmBlocks.SEMTEX);
        this.simpleCubeBottomTopBlock(NtmBlocks.C4);
        this.simpleCubeBottomTopBlock(NtmBlocks.FISSURE_BOMB);

        this.particleOnlyBlock(NtmBlocks.MINE_AP, blockTexture(NtmBlocks.MINE_AP));
        this.particleOnlyBlock(NtmBlocks.MINE_HE, blockTexture(NtmBlocks.MINE_HE));
        this.particleOnlyBlock(NtmBlocks.MINE_SHRAP, blockTexture(NtmBlocks.MINE_SHRAP));
        this.particleOnlyBlock(NtmBlocks.MINE_FAT, blockTexture(NtmBlocks.MINE_FAT));
        this.particleOnlyBlock(NtmBlocks.MINE_NAVAL, blockTexture(NtmBlocks.MINE_NAVAL));

        this.simpleCubeAllBlock(NtmBlocks.DET_CHARGE);
        this.registerDetCord();
        this.cubeTop(NtmBlocks.DET_NUKE);
        this.cubeTop(NtmBlocks.DET_MINER);
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_RED.get(), blockTexture(NtmBlocks.BARREL_RED));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_PINK.get(), blockTexture(NtmBlocks.BARREL_PINK));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_LOX.get(), blockTexture(NtmBlocks.BARREL_LOX));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_TAINT.get(), blockTexture(NtmBlocks.BARREL_TAINT));

        this.particleOnlyBlock(NtmBlocks.GEIGER, blockTexture(NtmBlocks.GEIGER));

        this.particleOnlyBlock(NtmBlocks.MACHINE_PRESS, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ELECTRIC_PRESS, modLoc("block/block_steel"));
        this.simpleCubeAllBlock(NtmBlocks.PRESS_PREHEATER);
        this.registerShredder();

        this.registerCables();

        this.particleOnlyBlock(NtmBlocks.RED_CONNECTOR, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_CONNECTOR_SUPER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON_MEDIUM_WOOD, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON_MEDIUM_WOOD_TRANSFORMER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON_MEDIUM_STEEL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON_MEDIUM_STEEL_TRANSFORMER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON_LARGE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.RED_PYLON_STEEL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.SUBSTATION, modLoc("block/block_steel"));

        this.registerFluidDucts();
        this.particleOnlyBlock(NtmBlocks.PIPE_ANCHOR, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.HEATER_FIREBOX, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.HEATER_OVEN, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.HEATER_FLUID_BURNER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.HEATER_ELECTRIC, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.HEATER_HEATEX, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BATTERY_SOCKET, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BATTERY_REDD, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ASSEMBLY_MACHINE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ASSEMBLY_FACTORY, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_PREC_ASS, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_CHEMICAL_PLANT, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_CHEMICAL_FACTORY, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_PUREX, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ORE_ACIDIZER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_WOOD_BURNER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_DIESEL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_COMBUSTION_ENGINE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_TURBOFAN, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_STIRLING, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_STIRLING_STEEL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_STIRLING_CREATIVE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_SAWMILL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.PUMP_STEAM, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.PUMP_ELECTRIC, modLoc("block/block_steel"));
        this.simpleCubeAllBlock(NtmBlocks.MACHINE_CONDENSER);
        this.particleOnlyBlock(NtmBlocks.MACHINE_CONDENSER_POWERED, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_STEAM_ENGINE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_CENTRIFUGE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_GAS_CENTRIFUGE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_SOLDERING_STATION, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ARC_WELDER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_MIXER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_FEL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_SILEX, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ELECTROLYSER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_INTAKE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.HEAT_BOILER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_INDUSTRIAL_BOILER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_INDUSTRIAL_TURBINE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_CHUNGUS, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_TOWER_SMALL, modLoc("block/brick_concrete"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_TOWER_LARGE, modLoc("block/concrete"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_GEOTHERMAL_HEAT_EXCHANGER, modLoc("block/block_steel"));
        this.registerFurnace(NtmBlocks.FURNACE_BRICK.get(), MachineFurnaceBrickBlock.FACING, MachineFurnaceBrickBlock.LIT, "machine_furnace_brick");
        this.registerFurnace(NtmBlocks.MACHINE_ELECTRIC_FURNACE.get(), MachineElectricFurnaceBlock.FACING, MachineElectricFurnaceBlock.LIT, "machine_electric_furnace");
        this.particleOnlyBlock(NtmBlocks.FURNACE_IRON, modLoc("block/block_aluminium"));
        this.particleOnlyBlock(NtmBlocks.FURNACE_STEEL, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.COMBINATION_OVEN, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BLAST_FURNACE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ROTARY_FURNACE, modLoc("block/brick_fire"));

        this.particleOnlyBlock(NtmBlocks.MACHINE_FLUID_TANK, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BIG_ASS_TANK, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_DRAIN, modLoc("block/block_steel"));

        this.particleOnlyBlock(NtmBlocks.MACHINE_OIL_DERRICK, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_PUMPJACK, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_FRACKING_TOWER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_REFINERY, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_VACUUM_REFINERY, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_FRACTION_TOWER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.FRACTION_SPACER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_CATALYTIC_REFORMER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_CATALYTIC_CRACKING_TOWER, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_COMPRESSOR, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_COMPRESSOR_COMPACT, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_FLARE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.CHIMNEY_BRICK, modLoc("block/brick_fire"));
        this.particleOnlyBlock(NtmBlocks.CHIMNEY_INDUSTRIAL, modLoc("block/concrete"));

        this.cubeTop(NtmBlocks.MACHINE_SATLINKER);

        this.simpleBlockWithItem(
                NtmBlocks.DECONTAMINATOR,
                this.models().cubeBottomTop(
                        name(NtmBlocks.DECONTAMINATOR),
                        blockTexture(NtmBlocks.DECONTAMINATOR, "_side"),
                        blockTexture(NtmBlocks.DECONTAMINATOR, "_side"),
                        blockTexture(NtmBlocks.DECONTAMINATOR, "_top")
                )
        );

        this.simpleBlockWithItem(NtmBlocks.PWR_CONTROLLER.get(),
                this.models().cube(
                        NtmBlocks.PWR_CONTROLLER.getId().getPath(),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_controller"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank")
                )
        );

        registerCrate(NtmBlocks.CRATE_IRON);
        registerCrate(NtmBlocks.CRATE_TUNGSTEN);
        registerCrate(NtmBlocks.CRATE_STEEL);
        registerCrate(NtmBlocks.CRATE_DESH);

        this.simpleBlock(NtmBlocks.BALEFIRE.get(), this.models().withExistingParent("balefire", mcLoc("block/cross")).renderType("cutout_mipped").texture("cross", modLoc("block/balefire")));
        this.simpleBlock(NtmBlocks.FIRE_DIGAMMA.get(), this.models().withExistingParent("fire_digamma", mcLoc("block/cross")).renderType("cutout_mipped").texture("cross", modLoc("block/fire_digamma")));
        // VOLCANO_CORE uses custom register!
        // VOLCANO_RAD_CORE uses custom register!

        this.particleOnlyBlock(NtmBlocks.LAUNCH_PAD, blockTexture(NtmBlocks.LAUNCH_PAD));
        // todo make item thing
        this.particleOnlyBlock(NtmBlocks.SOYUZ_LAUNCHER, modLoc("block/block_steel"));

        this.itemModels().basicItem(NtmBlocks.GAS_RADON.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_RADON_DENSE.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_RADON_TOMB.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_MELTDOWN.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_MONOXIDE.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_ASBESTOS.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_COAL.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_FLAMMABLE.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_EXPLOSIVE.asItem());

        this.simpleCubeAllBlock(NtmBlocks.TAINT);
    }

    private void registerShredder() {
        Block block = NtmBlocks.MACHINE_SHREDDER.get();

        ModelFile model = this.models().cube(
                name(NtmBlocks.MACHINE_SHREDDER),
                modLoc("block/machine_shredder_bottom"),
                modLoc("block/machine_shredder_top"),
                modLoc("block/machine_shredder_front"), // north (front)
                modLoc("block/machine_shredder_front"), // south (back) - same as front
                modLoc("block/machine_shredder_side"),  // east (right)
                modLoc("block/machine_shredder_side")   // west (left) - same as right
        ).renderType("solid").texture("particle", modLoc("block/machine_shredder_front"));

        this.getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(DummyableBlock.FACING);

            int rotY = switch (dir) {
                case NORTH -> 0;
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0; // UP/DOWN never occur in practice (getDirModified forces horizontal)
            };

            return ConfiguredModel.builder().modelFile(model).rotationY(rotY).build();
        });

        this.itemModels().getBuilder(this.key(block).getPath()).parent(model);
    }

    private void registerFurnace(Block block, DirectionProperty facing, BooleanProperty lit, String name) {
        ModelFile off = this.models().cube(
                name + "_off",
                modLoc("block/" + name + "_bottom"),
                modLoc("block/" + name + "_top"),
                modLoc("block/" + name + "_front_off"),
                modLoc("block/" + name + "_side"),
                modLoc("block/" + name + "_side"),
                modLoc("block/" + name + "_side")
        ).texture("particle", modLoc("block/" + name + "_side"));

        ModelFile on = this.models().cube(
                name + "_on",
                modLoc("block/" + name + "_bottom"),
                modLoc("block/" + name + "_top"),
                modLoc("block/" + name + "_front_on"),
                modLoc("block/" + name + "_side"),
                modLoc("block/" + name + "_side"),
                modLoc("block/" + name + "_side")
        ).texture("particle", modLoc("block/" + name + "_side"));

        this.getVariantBuilder(block).forAllStates(state -> {
            Direction direction = state.getValue(facing);
            ModelFile model = state.getValue(lit) ? on : off;
            int rotation = switch(direction) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            return ConfiguredModel.builder().modelFile(model).rotationY(rotation).build();
        });

        this.itemModels().getBuilder(this.key(block).getPath()).parent(off);
    }

    private void registerCrate(DeferredBlock<? extends Block> block) {
        String blockName = name(block);

        this.simpleBlockWithItem(
                block,
                this.models().cubeBottomTop(
                        blockName,
                        modLoc("block/" + blockName + "_side"),
                        modLoc("block/" + blockName + "_top"),
                        modLoc("block/" + blockName + "_top")
                )
        );
    }

    private void registerCables() {
        Block block = NtmBlocks.RED_CABLE.get();

        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(CableBlockLoaderBuilder::new).texture("texture", modLoc("block/cable_neo")).end());
        this.entityBlockItem(block, false);

        this.registerBoxDuctModel(NtmBlocks.RED_CABLE_CLASSIC.get(), "red_cable_classic", "red_cable_classic", "red_cable_classic", 5, 5);

        Block paintable = NtmBlocks.RED_CABLE_PAINTABLE.get();
        ModelFile paintableModel = this.models().withExistingParent(this.name(paintable), mcLoc("block/block"))
                .customLoader(PaintableCableLoaderBuilder::new)
                .texture("texture", modLoc("block/red_cable_base"))
                .texture("overlay", modLoc("block/red_cable_overlay"))
                .end();
        this.simpleBlock(paintable, paintableModel);
        this.simpleBlockItem(paintable, paintableModel);

        this.registerCableBox(NtmBlocks.RED_CABLE_BOX_HUGE.get(), 0, 12);
        this.registerCableBox(NtmBlocks.RED_CABLE_BOX_LARGE.get(), 1, 10);
        this.registerCableBox(NtmBlocks.RED_CABLE_BOX_MEDIUM.get(), 2, 8);
        this.registerCableBox(NtmBlocks.RED_CABLE_BOX_SMALL.get(), 3, 6);
        this.registerCableBox(NtmBlocks.RED_CABLE_BOX_TINY.get(), 4, 4);

        this.simpleCubeAllBlock(NtmBlocks.RED_WIRE_COATED);
    }

    private void registerDetCord() {
        Block block = NtmBlocks.DET_CORD.get();

        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(DetCordBlockLoaderBuilder::new).texture("texture", modLoc("block/det_cord")).end());
        this.entityBlockItem(block, false);
    }

    private void registerFluidDucts() {
        Block block = NtmBlocks.FLUID_DUCT_NEO.get();

        this.getVariantBuilder(block).forAllStatesExcept(state -> {

            int meta = state.getValue(NtmBlockStateProperties.META);
            
            ModelFile model;

            switch(meta) {
                case 2 -> model = this.models().getBuilder(NuclearTechMod.MODID + ":block/fluid_duct_silver").customLoader(DuctBlockLoaderBuilder::new).texture("texture", modLoc("block/pipe_silver")).texture("overlay", modLoc("block/pipe_silver_overlay")).end();
                case 1 -> model = this.models().getBuilder(NuclearTechMod.MODID + ":block/fluid_duct_colored").customLoader(DuctBlockLoaderBuilder::new).texture("texture", modLoc("block/pipe_colored")).texture("overlay", modLoc("block/pipe_colored_overlay")).end();
                default -> model = this.models().getBuilder(NuclearTechMod.MODID + ":block/fluid_duct_neo").customLoader(DuctBlockLoaderBuilder::new).texture("texture", modLoc("block/pipe_neo")).texture("overlay", modLoc("block/pipe_neo_overlay")).end();
            }

            return ConfiguredModel.builder().modelFile(model).build();
        }, FluidDuctConnectingBlock.NORTH, FluidDuctConnectingBlock.SOUTH, FluidDuctConnectingBlock.EAST, FluidDuctConnectingBlock.WEST, FluidDuctConnectingBlock.UP, FluidDuctConnectingBlock.DOWN);

        this.entityBlockItem(block, false);

        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_IRON_HUGE.get(), "silver", 0, 12, 14);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_COPPER_HUGE.get(), "copper", 0, 12, 14);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_HUGE.get(), "white", 0, 12, 14);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_IRON_LARGE.get(), "silver", 1, 10, 12);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_COPPER_LARGE.get(), "copper", 1, 10, 12);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_LARGE.get(), "white", 1, 10, 12);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_IRON_MEDIUM.get(), "silver", 2, 8, 10);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_COPPER_MEDIUM.get(), "copper", 2, 8, 10);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_MEDIUM.get(), "white", 2, 8, 10);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_IRON_SMALL.get(), "silver", 3, 6, 8);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_COPPER_SMALL.get(), "copper", 3, 6, 8);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_SMALL.get(), "white", 3, 6, 8);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_IRON_TINY.get(), "silver", 4, 4, 6);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_COPPER_TINY.get(), "copper", 4, 4, 6);
        this.registerFluidDuctBox(NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_TINY.get(), "white", 4, 4, 6);
        this.registerFluidDuctExhaust(NtmBlocks.FLUID_DUCT_EXHAUST_HUGE.get(), 0, 12, 14);
        this.registerFluidDuctExhaust(NtmBlocks.FLUID_DUCT_EXHAUST_LARGE.get(), 1, 10, 12);
        this.registerFluidDuctExhaust(NtmBlocks.FLUID_DUCT_EXHAUST_MEDIUM.get(), 2, 8, 10);
        this.registerFluidDuctExhaust(NtmBlocks.FLUID_DUCT_EXHAUST_SMALL.get(), 3, 6, 8);
        this.registerFluidDuctExhaust(NtmBlocks.FLUID_DUCT_EXHAUST_TINY.get(), 4, 4, 6);
    }

    private void registerCableBox(Block block, int sizeIndex, int diameter) {
        this.registerBoxDuctModel(block, "boxduct_cable_straight", "boxduct_cable_end_" + sizeIndex, "boxduct_cable_junction", diameter, diameter);
    }

    private void registerFluidDuctBox(Block block, String material, int sizeIndex, int diameter, int junctionDiameter) {
        this.registerBoxDuctModel(block, "boxduct_" + material + "_straight", "boxduct_" + material + "_end", "boxduct_" + material + "_junction_" + sizeIndex, diameter, junctionDiameter);
    }

    private void registerFluidDuctExhaust(Block block, int sizeIndex, int diameter, int junctionDiameter) {
        this.registerBoxDuctModel(block, "boxduct_exhaust_straight", "boxduct_exhaust_end", "boxduct_exhaust_junction_" + sizeIndex, diameter, junctionDiameter);
    }

    private void registerBoxDuctModel(Block block, String straightTexture, String endTexture, String junctionTexture, int diameter, int junctionDiameter) {
        String curvePrefix = straightTexture.substring(0, straightTexture.length() - "straight".length());
        boolean singleTexture = straightTexture.equals(endTexture) && straightTexture.equals(junctionTexture);
        BoxDuctBlockLoaderBuilder loader = this.models().withExistingParent(this.name(block), mcLoc("block/block"))
                .customLoader(BoxDuctBlockLoaderBuilder::new)
                .dimensions(diameter / 16.0F, junctionDiameter / 16.0F);
        loader.texture("straight", modLoc("block/" + straightTexture));
        loader.texture("end", modLoc("block/" + endTexture));
        loader.texture("curve_tl", modLoc("block/" + (singleTexture ? straightTexture : curvePrefix + "curve_tl")));
        loader.texture("curve_tr", modLoc("block/" + (singleTexture ? straightTexture : curvePrefix + "curve_tr")));
        loader.texture("curve_bl", modLoc("block/" + (singleTexture ? straightTexture : curvePrefix + "curve_bl")));
        loader.texture("curve_br", modLoc("block/" + (singleTexture ? straightTexture : curvePrefix + "curve_br")));
        loader.texture("junction", modLoc("block/" + junctionTexture));
        ModelFile model = loader.end();
        this.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }

    private void registerBarbedWire() {
        Block block = NtmBlocks.BARBED_WIRE.get();

        this.getVariantBuilder(block).forAllStates(state -> {

            int subType = state.getValue(BarbedWireBlock.SUBTYPE);

            ModelFile model;

            switch(subType) {
                case 5 -> model = this.models().getBuilder(this.name(block) + "_ultradeath").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_ultradeath")).end();
                case 4 -> model = this.models().getBuilder(this.name(block) + "_wither").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_wither")).end();
                case 3 -> model = this.models().getBuilder(this.name(block) + "_acid").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_acid")).end();
                case 2 -> model = this.models().getBuilder(this.name(block) + "_poison").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_poison")).end();
                case 1 -> model = this.models().getBuilder(this.name(block) + "_fire").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_fire")).end();
                default -> model = this.models().getBuilder(this.name(block)).customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire")).end();
            }

            return ConfiguredModel.builder().modelFile(model).build();
        });

        this.entityBlockItem(block, false);
    }

    private void registerSpikes() {
        Block block = NtmBlocks.SPIKES.get();

        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(SpikesLoaderBuilder::new).texture("texture", modLoc("block/spikes")).end());
        this.entityBlockItem(block, false);
    }

    private void barrelLoaderBlockItem(Block block, ResourceLocation texture) {
        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(BarrelBlockModelBuilder::new).texture("texture", texture).end());
        this.entityBlockItem(block, false);
    }

    private void layeringBlock(Block block, ResourceLocation texture, String modelPrefix) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        for(int i = 1; i <= 8; i++) {
            float height = i * 2f / 16f;

            ModelFile model = models()
                    .withExistingParent(modelPrefix + "_" + i, mcLoc("block/block"))
                    .texture("all", texture)
                    .texture("particle", texture)
                    .renderType("cutout_mipped")
                    .element()
                    .from(0, 0, 0)
                    .to(16, height * 16, 16)
                    .face(Direction.UP).texture("#all").end()
                    .face(Direction.DOWN).texture("#all").end()
                    .face(Direction.NORTH).texture("#all").end()
                    .face(Direction.SOUTH).texture("#all").end()
                    .face(Direction.WEST).texture("#all").end()
                    .face(Direction.EAST).texture("#all").end()
                    .end();

            builder.part()
                    .modelFile(model)
                    .addModel()
                    .condition(LayeringBlock.LAYERS, i)
                    .end();
        }
    }

    private void sellafieldSlaked(Block block, String modelBaseName) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int variant = state.getValue(SellafieldSlakedBlock.VARIANT);
            String modelName = modelBaseName + (variant == 0 ? "" : "_" + variant);
            String texName = "sellafield_slaked" + (variant == 0 ? "" : "_" + variant);

            ModelFile tintedModel = models().withExistingParent(modelName, mcLoc("block/cube"))
                    .texture("particle", modLoc("block/" + texName))
                    .texture("down", modLoc("block/" + texName))
                    .texture("up", modLoc("block/" + texName))
                    .texture("north", modLoc("block/" + texName))
                    .texture("south", modLoc("block/" + texName))
                    .texture("west", modLoc("block/" + texName))
                    .texture("east", modLoc("block/" + texName))
                    .element()
                    .from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, face) -> face.texture("#" + dir.getName()).tintindex(0))
                    .end();

            return ConfiguredModel.builder().modelFile(tintedModel).build();
            }, SellafieldSlakedBlock.COLOR_LEVEL);

        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/sellafield_slaked"));
    }

    private void sellafieldOre(Block block, String baseName, String overlayTexture) {

        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            int variant = state.getValue(SellafieldSlakedBlock.VARIANT);
            String modelName = baseName + (variant == 0 ? "" : "_" + variant);
            String baseTex = "sellafield_slaked" + (variant == 0 ? "" : "_" + variant);

            ModelFile oreModel = models().withExistingParent(modelName, mcLoc("block/cube"))
                    .renderType("cutout")
                    .texture("base", modLoc("block/" + baseTex))
                    .texture("overlay", modLoc(overlayTexture))
                    .texture("particle", modLoc(overlayTexture))
                    .element()
                    .from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, face) -> face.texture("#base").tintindex(0))
                    .end()
                    .element()
                    .from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, face) -> face.texture("#overlay"))
                    .end();
            return ConfiguredModel.builder().modelFile(oreModel).build();

            }, SellafieldSlakedBlock.COLOR_LEVEL);

        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/" + baseName));
    }

    public void simpleBlockWithItem(DeferredBlock<? extends Block> block, ModelFile model) {
        this.simpleBlockWithItem(block.get(), model);
    }

    /** Creates block with item, uses cube all model */
    public void simpleCubeAllBlock(DeferredBlock<? extends Block> block) { this.simpleBlockWithItem(block.get(), this.cubeAll(block.get())); }

    private void particleOnlyBlock(DeferredBlock<? extends Block> block, ResourceLocation particleTexture) { this.particleOnlyBlock(block, particleTexture, false); }
    private void particleOnlyBlock(DeferredBlock<? extends Block> block, ResourceLocation particleTexture, boolean frontLight) {
        this.simpleBlock(block.get(), this.models().getBuilder(name(block) + "_particle").texture("particle", particleTexture));
        this.entityBlockItem(block.get(), frontLight);
    }

    private void entityBlockItem(Block block, boolean frontLight) {
        this.itemModels().getBuilder(this.key(block).getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).guiLight(frontLight ? BlockModel.GuiLight.FRONT : BlockModel.GuiLight.SIDE);
    }

    public void simpleCubeBottomTopBlock(DeferredBlock<? extends Block> block) {
        String blockName = name(block);
        this.simpleBlockWithItem(block, this.models().cubeBottomTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_bottom"), modLoc("block/" + blockName + "_top")));
    }

    public void cubeTop(DeferredBlock<? extends Block> block) {
        String blockName = name(block);
        this.simpleBlockWithItem(block, this.models().cubeTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_top")));
    }

    protected String name(Block block) { return this.key(block).getPath(); }
    protected String name(DeferredBlock<? extends Block> block) { return this.key(block).getPath(); }
    protected ResourceLocation key(Block block) { return BuiltInRegistries.BLOCK.getKey(block); }
    protected ResourceLocation key(DeferredBlock<? extends Block> block) { return BuiltInRegistries.BLOCK.getKey(block.get()); }
    protected ResourceLocation blockTexture(DeferredBlock<? extends Block> block) { ResourceLocation name = this.key(block); return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath()); }
    protected ResourceLocation blockTexture(DeferredBlock<? extends Block> block, String toAppend) { ResourceLocation name = this.key(block); return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath() + toAppend); }

    private void blockItem(DeferredBlock<? extends Block> block) { this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(NuclearTechMod.MODID + ":block/" + block.getId().getPath())); }

    protected static class DuctBlockLoaderBuilder extends BlockModelBuilderBase {
        public DuctBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.PIPE; }
    }
    protected static class BarrelBlockModelBuilder extends BlockModelBuilderBase {
        public BarrelBlockModelBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.BARREL; }
    }
    protected static class CableBlockLoaderBuilder extends BlockModelBuilderBase {
        public CableBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.CABLE; }
    }
    protected static class BoxDuctBlockLoaderBuilder extends BlockModelBuilderBase {
        private float diameter;
        private float junctionDiameter;

        public BoxDuctBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }

        public BoxDuctBlockLoaderBuilder dimensions(float diameter, float junctionDiameter) {
            this.diameter = diameter;
            this.junctionDiameter = junctionDiameter;
            return this;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            super.toJson(json);
            json.addProperty("diameter", this.diameter);
            json.addProperty("junction_diameter", this.junctionDiameter);
            return json;
        }

        @Override public BakedModelType getType() { return BakedModelType.BOX_DUCT; }
    }
    protected static class PaintableCableLoaderBuilder extends BlockModelBuilderBase {
        public PaintableCableLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.PAINTABLE_CABLE; }
    }
    protected static class DetCordBlockLoaderBuilder extends BlockModelBuilderBase {
        public DetCordBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.DET_CORD; }
    }
    protected static class BarbedWireBlockLoaderBuilder extends BlockModelBuilderBase {
        public BarbedWireBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.BARBED_WIRE; }
    }
    protected static class SpikesLoaderBuilder extends BlockModelBuilderBase {
        public SpikesLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.SPIKES; }
    }

    public static abstract class BlockModelBuilderBase extends CustomLoaderBuilder<BlockModelBuilder> {

        private final Map<String, ResourceLocation> textures = new LinkedHashMap<>();

        protected BlockModelBuilderBase(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("ntm_geometry_loader"), parent, helper, false);
        }

        public BlockModelBuilderBase texture(String key, ResourceLocation location) {
            this.textures.put(key, location);
            return this;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            super.toJson(json);

            JsonObject texturesObject = new JsonObject();
            for(Entry<String, ResourceLocation> entry : this.textures.entrySet()) {
                texturesObject.addProperty(entry.getKey(), entry.getValue().toString());
            }
            json.add("textures", texturesObject);
            json.addProperty("type", this.getType().name().toLowerCase(Locale.US));

            return json;
        }

        public abstract BakedModelType getType();
    }
}
