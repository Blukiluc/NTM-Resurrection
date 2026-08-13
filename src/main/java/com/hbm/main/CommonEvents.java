package com.hbm.main;

import com.hbm.blockentity.bomb.LaunchPadBaseBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.commands.ChunkRadCommand;
import com.hbm.commands.LivingPropsCommand;
import com.hbm.commands.SatellitesCommand;
import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.NtmConfig;
import com.hbm.entity.NtmEntityTypes;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.handler.HTTPHandler;
import com.hbm.handler.HazmatRegistry;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.inventory.screens.*;
import com.hbm.network.toclient.InformPlayer;
import com.hbm.saveddata.satellite.Satellite;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.ArmorUtil;
import com.hbm.util.DamageResistanceHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = NuclearTechMod.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {

        // to make sure that foreign registered fluids are accounted for,
        // even when the reload listener is registered too late due to load order
        // IMPORTANT: fluids have to load before recipes. weird shit happens if not.
        Fluids.reloadFluids();
        FluidContainerRegistry.register();

        //the good stuff
        SerializableRecipe.registerAllHandlers();
        SerializableRecipe.initialize();

        HTTPHandler.loadStats();
        FalloutConfigJSON.initialize();
        DamageResistanceHandler.init();
        HazardRegistry.registerItems();
        HazmatRegistry.registerHazmats();
        ArmorUtil.register();
        Satellite.register();
        LaunchPadBaseBlockEntity.registerLaunchables();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {

        // Networks! All of them!
        UniNodespace.updateNodespace(event.getServer());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (NtmConfig.COMMON.ENABLE_MOTD.get()) {

            player.sendSystemMessage(Component.translatable("message." + NuclearTechMod.MODID + ".loaded", Component.translatable("message." + NuclearTechMod.MODID + ".resurrection").withStyle(ChatFormatting.LIGHT_PURPLE), NuclearTechMod.VERSION));

            if (HTTPHandler.newVersion) {
                player.sendSystemMessage(
                        Component.translatable("message." + NuclearTechMod.MODID + ".new_version", HTTPHandler.versionNumber)
                                .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                                .append(Component.translatable("message." + NuclearTechMod.MODID + ".click_here").withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Blukiluc/HBM-Resurrection/releases"))))
                                .append(Component.translatable("message." + NuclearTechMod.MODID + ".to_download").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)))
                );
            }
        }

        if (!HbmPlayerAttachments.getData(player).ducked) {
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new InformPlayer(Component.translatable("o.to_duck"), NuclearTechModClient.ID_DUCK, 30_000));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(NtmEntityTypes.DUCK.get(), Duck.createAttributes().build());
        event.put(NtmEntityTypes.CREEPER_NUCLEAR.get(), CreeperNuclear.createAttributes().build());
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player) {
            HazardSystem.updatePlayerInventory(player);
        }
        if (entity instanceof ItemEntity itemEntity) {
            HazardSystem.updateDroppedItem(itemEntity);
        }
        if (entity instanceof LivingEntity livingEntity) {
            HazardSystem.updateLivingInventory(livingEntity);
            EntityEffectHandler.tick(livingEntity);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BreakEvent event) {
        BlockPos pos = event.getPos();
        Level level = (Level) event.getLevel();

        if (!level.isClientSide) {
            if (event.getState() == Blocks.COAL_ORE.defaultBlockState() || event.getState() == Blocks.DEEPSLATE_COAL_ORE.defaultBlockState() || event.getState() == Blocks.COAL_BLOCK.defaultBlockState()) {
                for (Direction dir : Direction.values()) {
                    BlockPos offsetPos = pos.relative(dir);

                    if (level.random.nextInt(2) == 0 && level.getBlockState(offsetPos).isAir()) {
                        level.setBlock(offsetPos, NtmBlocks.GAS_COAL.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        LivingPropsCommand.register(event.getDispatcher());
        SatellitesCommand.register(event.getDispatcher());
        ChunkRadCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(NtmMenuTypes.SAT_LINKER.get(), MachineSatLinkerScreen::new);

        event.register(NtmMenuTypes.FLUID_TANK.get(), MachineFluidTankScreen::new);

        event.register(NtmMenuTypes.ASSEMBLY_MACHINE.get(), MachineAssemblyMachineScreen::new);
        event.register(NtmMenuTypes.ASSEMBLY_FACTORY.get(), MachineAssemblyFactoryScreen::new);
        event.register(NtmMenuTypes.PREC_ASS.get(), MachinePrecAssScreen::new);
        event.register(NtmMenuTypes.CHEMICAL_PLANT.get(), MachineChemicalPlantScreen::new);
        event.register(NtmMenuTypes.CHEMICAL_FACTORY.get(), MachineChemicalFactoryScreen::new);
        event.register(NtmMenuTypes.CRYSTALLIZER.get(), MachineCrystallizerScreen::new);
        event.register(NtmMenuTypes.WOOD_BURNER.get(), MachineWoodBurnerScreen::new);
        event.register(NtmMenuTypes.DIESEL.get(), MachineDieselScreen::new);
        event.register(NtmMenuTypes.COMBUSTION_ENGINE.get(), MachineCombustionEngineScreen::new);
        event.register(NtmMenuTypes.SHREDDER.get(), MachineShredderScreen::new);
        event.register(NtmMenuTypes.PRESS.get(), MachinePressScreen::new);
        event.register(NtmMenuTypes.ELECTRIC_PRESS.get(), MachineElectricPressScreen::new);
        event.register(NtmMenuTypes.CENTRIFUGE.get(), MachineCentrifugeScreen::new);
        event.register(NtmMenuTypes.GAS_CENTRIFUGE.get(), MachineGasCentrifugeScreen::new);
        event.register(NtmMenuTypes.SOLDERING_STATION.get(), MachineSolderingStationScreen::new);
        event.register(NtmMenuTypes.ARC_WELDER.get(), MachineArcWelderScreen::new);
        event.register(NtmMenuTypes.MIXER.get(), MachineMixerScreen::new);
        event.register(NtmMenuTypes.FEL.get(), MachineFELScreen::new);
        event.register(NtmMenuTypes.SILEX.get(), MachineSILEXScreen::new);
        event.register(NtmMenuTypes.FURNACE_IRON.get(), MachineFurnaceIronScreen::new);
        event.register(NtmMenuTypes.FURNACE_STEEL.get(), MachineFurnaceSteelScreen::new);
        event.register(NtmMenuTypes.FURNACE_COMBINATION.get(), MachineFurnaceCombinationScreen::new);
        event.register(NtmMenuTypes.MACHINE_BLAST_FURNACE.get(), MachineBlastFurnaceScreen::new);
        event.register(NtmMenuTypes.MACHINE_OIL.get(), MachineOilScreen::new);
        event.register(NtmMenuTypes.REFINERY.get(), MachineRefineryScreen::new);
        event.register(NtmMenuTypes.VACUUM_REFINERY.get(), MachineVacuumDistillScreen::new);
        event.register(NtmMenuTypes.COMPRESSOR.get(), MachineCompressorScreen::new);
        event.register(NtmMenuTypes.CATALYTIC_REFORMER.get(), MachineCatalyticReformerScreen::new);

        event.register(NtmMenuTypes.CRATE.get(), CrateScreen::new);

        event.register(NtmMenuTypes.HEATER_FIREBOX.get(), HeaterFireboxScreen::new);
        event.register(NtmMenuTypes.HEATER_OVEN.get(), HeaterOvenScreen::new);
        event.register(NtmMenuTypes.HEATER_OILBURNER.get(), HeaterFluidBurnerScreen::new);
        event.register(NtmMenuTypes.HEATER_HEATEX.get(), HeaterHeatexScreen::new);
      
        event.register(NtmMenuTypes.BATTERY_SOCKET.get(), BatterySocketScreen::new);
        event.register(NtmMenuTypes.BATTERY_REDD.get(), BatteryREDDScreen::new);

        event.register(NtmMenuTypes.NUKE_GADGET.get(), NukeGadgetScreen::new);
        event.register(NtmMenuTypes.NUKE_LITTLE_BOY.get(), NukeLittleBoyScreen::new);
        event.register(NtmMenuTypes.NUKE_FAT_MAN.get(), NukeFatManScreen::new);
        event.register(NtmMenuTypes.NUKE_IVY_MIKE.get(), NukeIvyMikeScreen::new);
        event.register(NtmMenuTypes.NUKE_TSAR_BOMBA.get(), NukeTsarBombaScreen::new);
        event.register(NtmMenuTypes.NUKE_PROTOTYPE.get(), NukePrototypeScreen::new);
        event.register(NtmMenuTypes.NUKE_FLEIJA.get(), NukeFleijaScreen::new);
        event.register(NtmMenuTypes.NUKE_N2.get(), NukeN2Screen::new);
        event.register(NtmMenuTypes.NUKE_FSTBMB.get(), NukeFstbmbScreen::new);

        event.register(NtmMenuTypes.LAUNCH_PAD_LARGE.get(), LaunchPadLargeScreen::new);
    }
}
