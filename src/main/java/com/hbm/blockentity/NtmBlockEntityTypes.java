package com.hbm.blockentity;

import com.hbm.blockentity.bomb.*;
import com.hbm.blockentity.machine.*;
import com.hbm.blockentity.machine.boiler.MachineHeatBoilerBlockEntity;
import com.hbm.blockentity.machine.boiler.MachineIndustrialBoilerBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterElectricBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterFireboxBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterHeatexBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterFluidBurnerBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterOvenBlockEntity;
import com.hbm.blockentity.machine.oil.*;
import com.hbm.blockentity.machine.storage.*;
import com.hbm.blockentity.machine.tower.*;
import com.hbm.blockentity.machine.turbine.MachineIndustrialTurbineBlockEntity;
import com.hbm.blockentity.machine.turbine.MachineLeviathanTurbineBlockEntity;
import com.hbm.blockentity.machine.turbine.MachineSteamEngineBlockEntity;
import com.hbm.blockentity.network.*;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.bomb.VolcanoBlock.VolcanoCoreBlockEntity;
import com.hbm.blocks.generic.BobbleBlock.BobbleBlockEntity;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
public class NtmBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, NuclearTechMod.MODID);

    // Machines
    public static final Supplier<BlockEntityType<MachineFurnaceBrickBlockEntity>> FURNACE_BRICK = REGISTER.register(
            "furnace_brick",
            () -> BlockEntityType.Builder.of(
                            MachineFurnaceBrickBlockEntity::new,
                            NtmBlocks.FURNACE_BRICK.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineElectricFurnaceBlockEntity>> ELECTRIC_FURNACE = REGISTER.register(
            "electric_furnace",
            () -> BlockEntityType.Builder.of(
                            MachineElectricFurnaceBlockEntity::new,
                            NtmBlocks.MACHINE_ELECTRIC_FURNACE.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineFurnaceIronBlockEntity>> FURNACE_IRON = REGISTER.register(
            "furnace_iron",
            () -> BlockEntityType.Builder.of(
                            MachineFurnaceIronBlockEntity::new,
                            NtmBlocks.FURNACE_IRON.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineFurnaceSteelBlockEntity>> FURNACE_STEEL = REGISTER.register(
            "furnace_steel",
            () -> BlockEntityType.Builder.of(
                            MachineFurnaceSteelBlockEntity::new,
                            NtmBlocks.FURNACE_STEEL.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineCombinationOvenBlockEntity>> COMBINATION_OVEN = REGISTER.register(
            "combination_oven",
            () -> BlockEntityType.Builder.of(
                            MachineCombinationOvenBlockEntity::new,
                            NtmBlocks.COMBINATION_OVEN.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineBlastFurnaceBlockEntity>> MACHINE_BLAST_FURNACE = REGISTER.register(
            "machine_blast_furnace",
            () -> BlockEntityType.Builder.of(
                            MachineBlastFurnaceBlockEntity::new,
                            NtmBlocks.MACHINE_BLAST_FURNACE.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineRotaryFurnaceBlockEntity>> MACHINE_ROTARY_FURNACE = REGISTER.register(
            "machine_rotary_furnace",
            () -> BlockEntityType.Builder.of(
                            MachineRotaryFurnaceBlockEntity::new,
                            NtmBlocks.MACHINE_ROTARY_FURNACE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineHeatBoilerBlockEntity>> HEAT_BOILER = REGISTER.register(
            "heat_boiler",
            () -> BlockEntityType.Builder.of(
                            MachineHeatBoilerBlockEntity::new,
                            NtmBlocks.HEAT_BOILER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineIndustrialBoilerBlockEntity>> MACHINE_INDUSTRIAL_BOILER = REGISTER.register(
            "machine_industrial_boiler",
            () -> BlockEntityType.Builder.of(
                            MachineIndustrialBoilerBlockEntity::new,
                            NtmBlocks.MACHINE_INDUSTRIAL_BOILER.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineIndustrialTurbineBlockEntity>> MACHINE_INDUSTRIAL_TURBINE = REGISTER.register(
            "machine_industrial_turbine",
            () -> BlockEntityType.Builder.of(
                            MachineIndustrialTurbineBlockEntity::new,
                            NtmBlocks.MACHINE_INDUSTRIAL_TURBINE.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineLeviathanTurbineBlockEntity>> MACHINE_CHUNGUS = REGISTER.register(
            "machine_chungus",
            () -> BlockEntityType.Builder.of(
                            MachineLeviathanTurbineBlockEntity::new,
                            NtmBlocks.MACHINE_CHUNGUS.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineAuxiliaryCoolingTowerBlockEntity>> MACHINE_TOWER_SMALL = REGISTER.register(
            "machine_tower_small",
            () -> BlockEntityType.Builder.of(
                            MachineAuxiliaryCoolingTowerBlockEntity::new,
                            NtmBlocks.MACHINE_TOWER_SMALL.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineCoolingTowerBlockEntity>> MACHINE_TOWER_LARGE = REGISTER.register(
            "machine_tower_large",
            () -> BlockEntityType.Builder.of(
                            MachineCoolingTowerBlockEntity::new,
                            NtmBlocks.MACHINE_TOWER_LARGE.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineGeothermalHeatExchangerBlockEntity>> MACHINE_GEOTHERMAL_HEAT_EXCHANGER = REGISTER.register(
            "machine_geothermal_heat_exchanger",
            () -> BlockEntityType.Builder.of(
                            MachineGeothermalHeatExchangerBlockEntity::new,
                            NtmBlocks.MACHINE_GEOTHERMAL_HEAT_EXCHANGER.get())
                    .build(null));
    public static final Supplier<BlockEntityType<HeaterFireboxBlockEntity>> HEATER_FIREBOX = REGISTER.register(
            "heater_firebox",
            () -> BlockEntityType.Builder.of(
                            HeaterFireboxBlockEntity::new,
                            NtmBlocks.HEATER_FIREBOX.get())
                    .build(null));

    public static final Supplier<BlockEntityType<HeaterOvenBlockEntity>> HEATER_OVEN = REGISTER.register(
            "heater_oven",
            () -> BlockEntityType.Builder.of(
                            HeaterOvenBlockEntity::new,
                            NtmBlocks.HEATER_OVEN.get())
                    .build(null));

    public static final Supplier<BlockEntityType<HeaterFluidBurnerBlockEntity>> HEATER_FLUID_BURNER = REGISTER.register(
            "heater_fluid_burner",
            () -> BlockEntityType.Builder.of(
                            HeaterFluidBurnerBlockEntity::new,
                            NtmBlocks.HEATER_FLUID_BURNER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<HeaterElectricBlockEntity>> HEATER_ELECTRIC = REGISTER.register(
            "heater_electric",
            () -> BlockEntityType.Builder.of(
                            HeaterElectricBlockEntity::new,
                            NtmBlocks.HEATER_ELECTRIC.get())
                    .build(null));

    public static final Supplier<BlockEntityType<HeaterHeatexBlockEntity>> HEATER_HEATEX = REGISTER.register(
            "heater_heatex",
            () -> BlockEntityType.Builder.of(
                            HeaterHeatexBlockEntity::new,
                            NtmBlocks.HEATER_HEATEX.get())
                    .build(null));
    public static final Supplier<BlockEntityType<MachineSatLinkerBlockEntity>> MACHINE_SATLINKER = REGISTER.register(
            "machine_satlinker",
            () -> BlockEntityType.Builder.of(
                            MachineSatLinkerBlockEntity::new,
                            NtmBlocks.MACHINE_SATLINKER.get())
                    .build(null));
    public static final Supplier<BlockEntityType<CrateIronBlockEntity>> CRATE_IRON = REGISTER.register(
            "crate_iron",
            () -> BlockEntityType.Builder.of(
                            CrateIronBlockEntity::new,
                            NtmBlocks.CRATE_IRON.get())
                    .build(null));
    public static final Supplier<BlockEntityType<CrateTungstenBlockEntity>> CRATE_TUNGSTEN = REGISTER.register(
            "crate_tungsten",
            () -> BlockEntityType.Builder.of(
                            CrateTungstenBlockEntity::new,
                            NtmBlocks.CRATE_TUNGSTEN.get())
                    .build(null));
    public static final Supplier<BlockEntityType<CrateSteelBlockEntity>> CRATE_STEEL = REGISTER.register(
            "crate_steel",
            () -> BlockEntityType.Builder.of(
                            CrateSteelBlockEntity::new,
                            NtmBlocks.CRATE_STEEL.get())
                    .build(null));
    public static final Supplier<BlockEntityType<CrateDeshBlockEntity>> CRATE_DESH = REGISTER.register(
            "crate_desh",
            () -> BlockEntityType.Builder.of(
                            CrateDeshBlockEntity::new,
                            NtmBlocks.CRATE_DESH.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachinePressBlockEntity>> PRESS = REGISTER.register(
            "press",
            () -> BlockEntityType.Builder.of(
                            MachinePressBlockEntity::new,
                            NtmBlocks.MACHINE_PRESS.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineElectricPressBlockEntity>> ELECTRIC_PRESS = REGISTER.register(
            "electric_press",
            () -> BlockEntityType.Builder.of(
                            MachineElectricPressBlockEntity::new,
                            NtmBlocks.MACHINE_ELECTRIC_PRESS.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineShredderBlockEntity>> SHREDDER = REGISTER.register(
            "shredder",
            () -> BlockEntityType.Builder.of(
                            MachineShredderBlockEntity::new,
                            NtmBlocks.MACHINE_SHREDDER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineFluidTankBlockEntity>> FLUID_TANK = REGISTER.register(
            "fluid_tank",
            () -> BlockEntityType.Builder.of(
                            MachineFluidTankBlockEntity::new,
                            NtmBlocks.MACHINE_FLUID_TANK.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineBigAssTankBlockEntity>> BIG_ASS_TANK = REGISTER.register(
            "big_ass_tank",
            () -> BlockEntityType.Builder.of(
                            MachineBigAssTankBlockEntity::new,
                            NtmBlocks.MACHINE_BIG_ASS_TANK.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineDrainBlockEntity>> MACHINE_DRAIN = REGISTER.register(
            "machine_drain",
            () -> BlockEntityType.Builder.of(
                            MachineDrainBlockEntity::new,
                            NtmBlocks.MACHINE_DRAIN.get())
                    .build(null));

    public static final Supplier<BlockEntityType<BatterySocketBlockEntity>> BATTERY_SOCKET = REGISTER.register(
            "battery_socket",
            () -> BlockEntityType.Builder.of(
                            BatterySocketBlockEntity::new,
                            NtmBlocks.MACHINE_BATTERY_SOCKET.get())
                    .build(null));

    public static final Supplier<BlockEntityType<BatteryREDDBlockEntity>> BATTERY_REDD = REGISTER.register(
            "battery_redd",
            () -> BlockEntityType.Builder.of(
                            BatteryREDDBlockEntity::new,
                            NtmBlocks.MACHINE_BATTERY_REDD.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineAssemblyMachineBlockEntity>> ASSEMBLY_MACHINE = REGISTER.register(
            "assembly_machine",
            () -> BlockEntityType.Builder.of(
                            MachineAssemblyMachineBlockEntity::new,
                            NtmBlocks.MACHINE_ASSEMBLY_MACHINE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineAssemblyFactoryBlockEntity>> ASSEMBLY_FACTORY = REGISTER.register(
            "assembly_factory",
            () -> BlockEntityType.Builder.of(
                            MachineAssemblyFactoryBlockEntity::new,
                            NtmBlocks.MACHINE_ASSEMBLY_FACTORY.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachinePrecAssBlockEntity>> PREC_ASS = REGISTER.register(
            "prec_ass",
            () -> BlockEntityType.Builder.of(
                            MachinePrecAssBlockEntity::new,
                            NtmBlocks.MACHINE_PREC_ASS.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineChemicalPlantBlockEntity>> CHEMICAL_PLANT = REGISTER.register(
            "chemical_plant",
            () -> BlockEntityType.Builder.of(
                            MachineChemicalPlantBlockEntity::new,
                            NtmBlocks.MACHINE_CHEMICAL_PLANT.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineChemicalFactoryBlockEntity>> CHEMICAL_FACTORY = REGISTER.register(
            "chemical_factory",
            () -> BlockEntityType.Builder.of(
                            MachineChemicalFactoryBlockEntity::new,
                            NtmBlocks.MACHINE_CHEMICAL_FACTORY.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachinePUREXBlockEntity>> PUREX = REGISTER.register(
            "purex",
            () -> BlockEntityType.Builder.of(
                            MachinePUREXBlockEntity::new,
                            NtmBlocks.MACHINE_PUREX.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineOreAcidizerBlockEntity>> ORE_ACIDIZER = REGISTER.register(
            "ore_acidizer",
            () -> BlockEntityType.Builder.of(
                            MachineOreAcidizerBlockEntity::new,
                            NtmBlocks.MACHINE_ORE_ACIDIZER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineWoodBurnerBlockEntity>> WOOD_BURNER = REGISTER.register(
            "wood_burner",
            () -> BlockEntityType.Builder.of(
                            MachineWoodBurnerBlockEntity::new,
                            NtmBlocks.MACHINE_WOOD_BURNER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineDieselBlockEntity>> DIESEL = REGISTER.register(
            "diesel",
            () -> BlockEntityType.Builder.of(
                            MachineDieselBlockEntity::new,
                            NtmBlocks.MACHINE_DIESEL.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCombustionEngineBlockEntity>> COMBUSTION_ENGINE = REGISTER.register(
            "combustion_engine",
            () -> BlockEntityType.Builder.of(
                            MachineCombustionEngineBlockEntity::new,
                            NtmBlocks.MACHINE_COMBUSTION_ENGINE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineStirlingBlockEntity>> STIRLING = REGISTER.register(
            "stirling",
            () -> BlockEntityType.Builder.of(
                            MachineStirlingBlockEntity::new,
                            NtmBlocks.MACHINE_STIRLING.get(),
                            NtmBlocks.MACHINE_STIRLING_STEEL.get(),
                            NtmBlocks.MACHINE_STIRLING_CREATIVE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineSawmillBlockEntity>> SAWMILL = REGISTER.register(
            "sawmill",
            () -> BlockEntityType.Builder.of(
                            MachineSawmillBlockEntity::new,
                            NtmBlocks.MACHINE_SAWMILL.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineGroundwaterPumpBlockEntity>> GROUNDWATER_PUMP = REGISTER.register(
            "groundwater_pump",
            () -> BlockEntityType.Builder.of(
                            MachineGroundwaterPumpBlockEntity::new,
                            NtmBlocks.PUMP_STEAM.get(),
                            NtmBlocks.PUMP_ELECTRIC.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCondenserBlockEntity>> CONDENSER = REGISTER.register(
            "condenser",
            () -> BlockEntityType.Builder.of(
                            MachineCondenserBlockEntity::new,
                            NtmBlocks.MACHINE_CONDENSER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCondenserPoweredBlockEntity>> CONDENSER_POWERED = REGISTER.register(
            "condenser_powered",
            () -> BlockEntityType.Builder.of(
                            MachineCondenserPoweredBlockEntity::new,
                            NtmBlocks.MACHINE_CONDENSER_POWERED.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineSteamEngineBlockEntity>> STEAM_ENGINE = REGISTER.register(
            "steam_engine",
            () -> BlockEntityType.Builder.of(
                            MachineSteamEngineBlockEntity::new,
                            NtmBlocks.MACHINE_STEAM_ENGINE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCentrifugeBlockEntity>> CENTRIFUGE = REGISTER.register(
            "centrifuge",
            () -> BlockEntityType.Builder.of(
                            MachineCentrifugeBlockEntity::new,
                            NtmBlocks.MACHINE_CENTRIFUGE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineGasCentrifugeBlockEntity>> GAS_CENTRIFUGE = REGISTER.register(
            "gas_centrifuge",
            () -> BlockEntityType.Builder.of(
                            MachineGasCentrifugeBlockEntity::new,
                            NtmBlocks.MACHINE_GAS_CENTRIFUGE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineSolderingStationBlockEntity>> SOLDERING_STATION = REGISTER.register(
            "soldering_station",
            () -> BlockEntityType.Builder.of(
                            MachineSolderingStationBlockEntity::new,
                            NtmBlocks.MACHINE_SOLDERING_STATION.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineArcWelderBlockEntity>> ARC_WELDER = REGISTER.register(
            "arc_welder",
            () -> BlockEntityType.Builder.of(
                            MachineArcWelderBlockEntity::new,
                            NtmBlocks.MACHINE_ARC_WELDER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineMixerBlockEntity>> MIXER = REGISTER.register(
            "mixer",
            () -> BlockEntityType.Builder.of(
                            MachineMixerBlockEntity::new,
                            NtmBlocks.MACHINE_MIXER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineFELBlockEntity>> FEL = REGISTER.register(
            "fel",
            () -> BlockEntityType.Builder.of(
                            MachineFELBlockEntity::new,
                            NtmBlocks.MACHINE_FEL.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineSILEXBlockEntity>> SILEX = REGISTER.register(
            "silex",
            () -> BlockEntityType.Builder.of(
                            MachineSILEXBlockEntity::new,
                            NtmBlocks.MACHINE_SILEX.get())
                    .build(null));

    // Oil
    public static final Supplier<BlockEntityType<MachineOilDerrickBlockEntity>> OIL_DERRICK = REGISTER.register(
            "oil_derrick",
            () -> BlockEntityType.Builder.of(
                            MachineOilDerrickBlockEntity::new,
                            NtmBlocks.MACHINE_OIL_DERRICK.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachinePumpjackBlockEntity>> PUMPJACK = REGISTER.register(
            "pumpjack",
            () -> BlockEntityType.Builder.of(
                            MachinePumpjackBlockEntity::new,
                            NtmBlocks.MACHINE_PUMPJACK.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineFrackingTowerBlockEntity>> FRACKING_TOWER = REGISTER.register(
            "fracking_tower",
            () -> BlockEntityType.Builder.of(
                            MachineFrackingTowerBlockEntity::new,
                            NtmBlocks.MACHINE_FRACKING_TOWER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineRefineryBlockEntity>> REFINERY = REGISTER.register(
            "refinery",
            () -> BlockEntityType.Builder.of(
                            MachineRefineryBlockEntity::new,
                            NtmBlocks.MACHINE_REFINERY.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineVacuumRefineryBlockEntity>> VACUUM_REFINERY = REGISTER.register(
            "vacuum_refinery",
            () -> BlockEntityType.Builder.of(
                            MachineVacuumRefineryBlockEntity::new,
                            NtmBlocks.MACHINE_VACUUM_REFINERY.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineFractioningTowerBlockEntity>> FRACTION_TOWER = REGISTER.register(
            "fraction_tower",
            () -> BlockEntityType.Builder.of(
                            MachineFractioningTowerBlockEntity::new,
                            NtmBlocks.MACHINE_FRACTION_TOWER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<FractioningSpacerBlockEntity>> FRACTION_SPACER = REGISTER.register(
            "fraction_spacer",
            () -> BlockEntityType.Builder.of(
                            FractioningSpacerBlockEntity::new,
                            NtmBlocks.FRACTION_SPACER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCatalyticReformerBlockEntity>> CATALYTIC_REFORMER = REGISTER.register(
            "catalytic_reformer",
            () -> BlockEntityType.Builder.of(
                            MachineCatalyticReformerBlockEntity::new,
                            NtmBlocks.MACHINE_CATALYTIC_REFORMER.get())
                    .build(null));


    public static final Supplier<BlockEntityType<MachineCatalyticCrackingTowerBlockEntity>> CATALYTIC_CRACKING_TOWER = REGISTER.register(
            "catalytic_cracking_tower",
            () -> BlockEntityType.Builder.of(
                            MachineCatalyticCrackingTowerBlockEntity::new,
                            NtmBlocks.MACHINE_CATALYTIC_CRACKING_TOWER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCompressorBlockEntity>> COMPRESSOR = REGISTER.register(
            "compressor",
            () -> BlockEntityType.Builder.of(
                            MachineCompressorBlockEntity::new,
                            NtmBlocks.MACHINE_COMPRESSOR.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineCompressorCompactBlockEntity>> COMPRESSOR_COMPACT = REGISTER.register(
            "compressor_compact",
            () -> BlockEntityType.Builder.of(
                            MachineCompressorCompactBlockEntity::new,
                            NtmBlocks.MACHINE_COMPRESSOR_COMPACT.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineGasFlareBlockEntity>> GAS_FLARE = REGISTER.register(
            "gas_flare",
            () -> BlockEntityType.Builder.of(
                            MachineGasFlareBlockEntity::new,
                            NtmBlocks.MACHINE_FLARE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<ChimneyBrickBlockEntity>> CHIMNEY_BRICK = REGISTER.register(
            "chimney_brick",
            () -> BlockEntityType.Builder.of(
                            ChimneyBrickBlockEntity::new,
                            NtmBlocks.CHIMNEY_BRICK.get())
                    .build(null));

    public static final Supplier<BlockEntityType<ChimneyIndustrialBlockEntity>> CHIMNEY_INDUSTRIAL = REGISTER.register(
            "chimney_industrial",
            () -> BlockEntityType.Builder.of(
                            ChimneyIndustrialBlockEntity::new,
                            NtmBlocks.CHIMNEY_INDUSTRIAL.get())
                    .build(null));

    public static final Supplier<BlockEntityType<ProxyComboBlockEntity>> PROXY_COMBO = REGISTER.register("proxy_combo", () -> BlockEntityType.Builder.of(ProxyComboBlockEntity::new).build(null));

    public static final Supplier<BlockEntityType<BobbleBlockEntity>> BOBBLEHEAD = REGISTER.register("bobblehead", () -> BlockEntityType.Builder.of(BobbleBlockEntity::new, NtmBlocks.BOBBLEHEAD.get()).build(null));
    public static final Supplier<BlockEntityType<CableBaseBlockEntity>> NETWORK_CABLE = REGISTER.register(
            "network_cable",
            () -> BlockEntityType.Builder.of(
                            CableBaseBlockEntity::new,
                            NtmBlocks.RED_CABLE.get(),
                            NtmBlocks.RED_CABLE_CLASSIC.get(),
                            NtmBlocks.RED_CABLE_PAINTABLE.get(),
                            NtmBlocks.RED_CABLE_BOX_HUGE.get(),
                            NtmBlocks.RED_CABLE_BOX_LARGE.get(),
                            NtmBlocks.RED_CABLE_BOX_MEDIUM.get(),
                            NtmBlocks.RED_CABLE_BOX_SMALL.get(),
                            NtmBlocks.RED_CABLE_BOX_TINY.get(),
                            NtmBlocks.RED_WIRE_COATED.get())
                    .build(null));
    public static final Supplier<BlockEntityType<PylonConnectorBlockEntity>> PYLON_CONNECTOR = REGISTER.register(
            "pylon_connector",
            () -> BlockEntityType.Builder.of(
                            PylonConnectorBlockEntity::new,
                            NtmBlocks.RED_CONNECTOR.get(),
                            NtmBlocks.RED_CONNECTOR_SUPER.get())
                    .build(null));
    public static final Supplier<BlockEntityType<PylonBlockEntity>> PYLON = REGISTER.register(
            "pylon",
            () -> BlockEntityType.Builder.of(
                            PylonBlockEntity::new,
                            NtmBlocks.RED_PYLON.get(),
                            NtmBlocks.RED_PYLON_MEDIUM_WOOD.get(),
                            NtmBlocks.RED_PYLON_MEDIUM_WOOD_TRANSFORMER.get(),
                            NtmBlocks.RED_PYLON_MEDIUM_STEEL.get(),
                            NtmBlocks.RED_PYLON_MEDIUM_STEEL_TRANSFORMER.get(),
                            NtmBlocks.RED_PYLON_LARGE.get(),
                            NtmBlocks.RED_PYLON_STEEL.get(),
                            NtmBlocks.SUBSTATION.get())
                    .build(null));
    public static final Supplier<BlockEntityType<PipeBaseBlockEntity>> FLUID_DUCT = REGISTER.register(
            "fluid_duct",
            () -> BlockEntityType.Builder.of(
                            PipeBaseBlockEntity::new,
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
                            NtmBlocks.FLUID_DUCT_BOX_ALUMINIUM_TINY.get())
                    .build(null));
    public static final Supplier<BlockEntityType<ExhaustPipeBlockEntity>> EXHAUST_PIPE = REGISTER.register(
            "exhaust_pipe",
            () -> BlockEntityType.Builder.of(
                            ExhaustPipeBlockEntity::new,
                            NtmBlocks.FLUID_DUCT_EXHAUST_HUGE.get(),
                            NtmBlocks.FLUID_DUCT_EXHAUST_LARGE.get(),
                            NtmBlocks.FLUID_DUCT_EXHAUST_MEDIUM.get(),
                            NtmBlocks.FLUID_DUCT_EXHAUST_SMALL.get(),
                            NtmBlocks.FLUID_DUCT_EXHAUST_TINY.get())
                    .build(null));
    public static final Supplier<BlockEntityType<PipeAnchorBlockEntity>> PIPE_ANCHOR = REGISTER.register(
            "pipe_anchor",
            () -> BlockEntityType.Builder.of(
                            PipeAnchorBlockEntity::new,
                            NtmBlocks.PIPE_ANCHOR.get())
                    .build(null));

    public static final Supplier<BlockEntityType<DecontaminatorBlockEntity>> DECONTAMINATOR = REGISTER.register(
            "decontaminator",
            () -> BlockEntityType.Builder.of(
                            DecontaminatorBlockEntity::new,
                            NtmBlocks.DECONTAMINATOR.get())
                    .build(null));

    public static final Supplier<BlockEntityType<NukeGadgetBlockEntity>> NUKE_GADGET = REGISTER.register("nuke_gadget", () -> BlockEntityType.Builder.of(NukeGadgetBlockEntity::new, NtmBlocks.NUKE_GADGET.get()).build(null));
    public static final Supplier<BlockEntityType<NukeLittleBoyBlockEntity>> NUKE_LITTLE_BOY = REGISTER.register("nuke_little_boy", () -> BlockEntityType.Builder.of(NukeLittleBoyBlockEntity::new, NtmBlocks.NUKE_LITTLE_BOY.get()).build(null));
    public static final Supplier<BlockEntityType<NukeFatManBlockEntity>> NUKE_FAT_MAN = REGISTER.register("nuke_fat_man", () -> BlockEntityType.Builder.of(NukeFatManBlockEntity::new, NtmBlocks.NUKE_FAT_MAN.get()).build(null));
    public static final Supplier<BlockEntityType<NukeIvyMikeBlockEntity>> NUKE_IVY_MIKE = REGISTER.register("nuke_ivy_mike", () -> BlockEntityType.Builder.of(NukeIvyMikeBlockEntity::new, NtmBlocks.NUKE_IVY_MIKE.get()).build(null));
    public static final Supplier<BlockEntityType<NukeTsarBombaBlockEntity>> NUKE_TSAR_BOMBA = REGISTER.register("nuke_tsar_bomba", () -> BlockEntityType.Builder.of(NukeTsarBombaBlockEntity::new, NtmBlocks.NUKE_TSAR_BOMBA.get()).build(null));
    public static final Supplier<BlockEntityType<NukePrototypeBlockEntity>> NUKE_PROTOTYPE = REGISTER.register("nuke_prototype", () -> BlockEntityType.Builder.of(NukePrototypeBlockEntity::new, NtmBlocks.NUKE_PROTOTYPE.get()).build(null));
    public static final Supplier<BlockEntityType<NukeFleijaBlockEntity>> NUKE_FLEIJA = REGISTER.register("nuke_fleija", () -> BlockEntityType.Builder.of(NukeFleijaBlockEntity::new, NtmBlocks.NUKE_FLEIJA.get()).build(null));
    public static final Supplier<BlockEntityType<NukeN2BlockEntity>> NUKE_N2 = REGISTER.register("nuke_n2", () -> BlockEntityType.Builder.of(NukeN2BlockEntity::new, NtmBlocks.NUKE_N2.get()).build(null));
    public static final Supplier<BlockEntityType<NukeBalefireBlockEntity>> NUKE_FSTBMB = REGISTER.register("nuke_fstbmb", () -> BlockEntityType.Builder.of(NukeBalefireBlockEntity::new, NtmBlocks.NUKE_FSTBMB.get()).build(null));

    public static final Supplier<BlockEntityType<LaunchPadBlockEntity>> LAUNCH_PAD = REGISTER.register("launch_pad", () -> BlockEntityType.Builder.of(LaunchPadBlockEntity::new, NtmBlocks.LAUNCH_PAD.get()).build(null));
    public static final Supplier<BlockEntityType<SoyuzLauncherBlockEntity>> SOYUZ_LAUNCHER = REGISTER.register("soyuz_launcher", () -> BlockEntityType.Builder.of(SoyuzLauncherBlockEntity::new, NtmBlocks.SOYUZ_LAUNCHER.get()).build(null));

    public static final Supplier<BlockEntityType<GeigerBlockEntity>> GEIGER_COUNTER = REGISTER.register("geiger_counter", () -> BlockEntityType.Builder.of(GeigerBlockEntity::new, NtmBlocks.GEIGER.get()).build(null));

    public static final Supplier<BlockEntityType<LandMineBlockEntity>> LANDMINE = REGISTER.register("landmine", () -> BlockEntityType.Builder.of(LandMineBlockEntity::new, NtmBlocks.MINE_AP.get(), NtmBlocks.MINE_HE.get(), NtmBlocks.MINE_SHRAP.get(), NtmBlocks.MINE_FAT.get(), NtmBlocks.MINE_NAVAL.get()).build(null));
    public static final Supplier<BlockEntityType<VolcanoCoreBlockEntity>> VOLCANO_CORE = REGISTER.register("volcano_core", () -> BlockEntityType.Builder.of(VolcanoCoreBlockEntity::new, NtmBlocks.VOLCANO_CORE.get(), NtmBlocks.VOLCANO_RAD_CORE.get()).build(null));

    public static final Supplier<BlockEntityType<CrashedBombBlockEntity>> CRASHED_BOMB = REGISTER.register("crashed_bomb", () -> BlockEntityType.Builder.of(CrashedBombBlockEntity::new, NtmBlocks.CRASHED_BOMB.get()).build(null));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
