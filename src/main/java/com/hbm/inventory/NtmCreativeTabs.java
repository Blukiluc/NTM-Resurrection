package com.hbm.inventory;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.items.IMetaItem;
import com.hbm.items.NtmItems;
import com.hbm.items.food.DrinkItem.DrinkType;
import com.hbm.items.machine.FluidIDMultiItem;
import com.hbm.items.special.StarterKitItem.KitType;
import com.hbm.main.NuclearTechMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

@SuppressWarnings("unused")
public class NtmCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(CREATIVE_MODE_TAB, NuclearTechMod.MODID);

    // ingots, nuggets, wires, machine parts
    public static final Supplier<CreativeModeTab> PARTS = CREATIVE_MODE_TABS.register(
            "parts",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.INGOT_URANIUM.get()))
                    .title(Component.translatable("itemGroup.parts"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmItems.INGOT_URANIUM.get());
                        output.accept(NtmItems.INGOT_U233.get());
                        output.accept(NtmItems.INGOT_U235.get());
                        output.accept(NtmItems.INGOT_U238.get());
                        output.accept(NtmItems.INGOT_TH232.get());
                        output.accept(NtmItems.INGOT_PLUTONIUM.get());
                        output.accept(NtmItems.INGOT_PU238.get());
                        output.accept(NtmItems.INGOT_PU239.get());
                        output.accept(NtmItems.INGOT_PU240.get());
                        output.accept(NtmItems.INGOT_PU241.get());
                        output.accept(NtmItems.INGOT_PU_MIX.get());
                        output.accept(NtmItems.INGOT_AM241.get());
                        output.accept(NtmItems.INGOT_AM242.get());
                        output.accept(NtmItems.INGOT_AM_MIX.get());
                        output.accept(NtmItems.INGOT_NEPTUNIUM.get());
                        output.accept(NtmItems.INGOT_PO210.get());
                        output.accept(NtmItems.INGOT_TC99.get());
                        output.accept(NtmItems.INGOT_CO60.get());
                        output.accept(NtmItems.INGOT_SR90.get());
                        output.accept(NtmItems.INGOT_AU198.get());
                        output.accept(NtmItems.INGOT_PB209.get());
                        output.accept(NtmItems.INGOT_RA226.get());
                        output.accept(NtmItems.INGOT_TITANIUM.get());
                        output.accept(NtmItems.INGOT_INDUSTRIAL_COPPER.get());
                        output.accept(NtmItems.INGOT_RED_COPPER.get());
                        output.accept(NtmItems.INGOT_TUNGSTEN.get());
                        output.accept(NtmItems.INGOT_TUNGSTEN_CARBIDE.get());
                        output.accept(NtmItems.INGOT_ALUMINIUM.get());
                        output.accept(NtmItems.INGOT_STEEL.get());
                        output.accept(NtmItems.INGOT_TCALLOY.get());
                        output.accept(NtmItems.INGOT_CDALLOY.get());
                        output.accept(NtmItems.INGOT_BISMUTH_BRONZE.get());
                        output.accept(NtmItems.INGOT_ARSENIC_BRONZE.get());
                        output.accept(NtmItems.INGOT_BSCCO.get());
                        output.accept(NtmItems.INGOT_LEAD.get());
                        output.accept(NtmItems.INGOT_BISMUTH.get());
                        output.accept(NtmItems.INGOT_ARSENIC.get());
                        output.accept(NtmItems.INGOT_CALCIUM.get());
                        output.accept(NtmItems.INGOT_CADMIUM.get());
                        output.accept(NtmItems.INGOT_TANTALIUM.get());
                        output.accept(NtmItems.INGOT_SILICON.get());
                        output.accept(NtmItems.INGOT_NIOBIUM.get());
                        output.accept(NtmItems.INGOT_BERYLLIUM.get());
                        output.accept(NtmItems.INGOT_COBALT.get());
                        output.accept(NtmItems.INGOT_BORON.get());
                        output.accept(NtmItems.INGOT_GRAPHITE.get());
                        output.accept(NtmItems.INGOT_FIREBRICK.get());
                        output.accept(NtmItems.INGOT_DURA_STEEL.get());
                        output.accept(NtmItems.INGOT_POLYMER.get());
                        output.accept(NtmItems.INGOT_BAKELITE.get());
                        output.accept(NtmItems.INGOT_BIORUBBER.get());
                        output.accept(NtmItems.INGOT_RUBBER.get());
                        output.accept(NtmItems.INGOT_PC.get());
                        output.accept(NtmItems.INGOT_PVC.get());
                        output.accept(NtmItems.INGOT_MUD.get());
                        output.accept(NtmItems.INGOT_CTF.get());
                        output.accept(NtmItems.INGOT_SCHRARANIUM.get());
                        output.accept(NtmItems.INGOT_SCHRABIDIUM.get());
                        output.accept(NtmItems.INGOT_SCHRABIDATE.get());
                        output.accept(NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get());
                        output.accept(NtmItems.INGOT_COMBINE_STEEL.get());
                        output.accept(NtmItems.INGOT_SOLINIUM.get());
                        output.accept(NtmItems.INGOT_GH336.get());
                        output.accept(NtmItems.INGOT_URANIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_THORIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_PLUTONIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_NEPTUNIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_MOX_FUEL.get());
                        output.accept(NtmItems.INGOT_AMERICIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_HES.get());
                        output.accept(NtmItems.INGOT_LES.get());
                        output.accept(NtmItems.INGOT_AUSTRALIUM.get());
                        output.accept(NtmItems.INGOT_LANTHANIUM.get());
                        output.accept(NtmItems.INGOT_AC227.get());
                        output.accept(NtmItems.INGOT_DESH.get());
                        output.accept(NtmItems.INGOT_FERROURANIUM.get());
                        output.accept(NtmItems.INGOT_STARMETAL.get());
                        output.accept(NtmItems.INGOT_GUNMETAL.get());
                        output.accept(NtmItems.INGOT_WEAPONSTEEL.get());
                        output.accept(NtmItems.INGOT_SATURNITE.get());
                        output.accept(NtmItems.INGOT_EUPHEMIUM.get());
                        output.accept(NtmItems.INGOT_DINEUTRONIUM.get());
                        output.accept(NtmItems.INGOT_ELECTRONIUM.get());
                        output.accept(NtmItems.INGOT_SMORE.get());
                        output.accept(NtmItems.INGOT_OSMIRIDIUM.get());
                        output.accept(NtmItems.INGOT_DUSTED_STEEL.get());
                        output.accept(NtmItems.INGOT_CHAINSTEEL.get());
                        output.accept(NtmItems.INGOT_METEORITE.get());
                        output.accept(NtmItems.INGOT_METEORITE_FORGED.get());
                        output.accept(NtmItems.METEORITE_BLADE.get());
                        output.accept(NtmItems.INGOT_PHOSPHORUS.get());
                        output.accept(NtmItems.INGOT_LITHIUM.get());
                        output.accept(NtmItems.INGOT_ZIRCONIUM.get());
                        output.accept(NtmItems.INGOT_SEMTEX.get());
                        output.accept(NtmItems.INGOT_C4.get());
                        output.accept(NtmItems.TAR_OIL.get());
                        output.accept(NtmItems.TAR_CRACK_OIL.get());
                        output.accept(NtmItems.TAR_COAL.get());
                        output.accept(NtmItems.TAR_WOOD.get());
                        output.accept(NtmItems.WAX_PETROLEUM.get());
                        output.accept(NtmItems.WAX_PARAFFIN.get());
                        output.accept(NtmItems.SOLID_FUEL.get());
                        output.accept(NtmItems.SOLID_FUEL_PRESTO.get());
                        output.accept(NtmItems.SOLID_FUEL_PRESTO_TRIPLET.get());
                        output.accept(NtmItems.SOLID_FUEL_BF.get());
                        output.accept(NtmItems.SOLID_FUEL_PRESTO_BF.get());
                        output.accept(NtmItems.SOLID_FUEL_PRESTO_TRIPLET_BF.get());
                        output.accept(NtmItems.ROCKET_FUEL.get());
                        output.accept(NtmItems.INGOT_REDSTONE.get());
                        output.accept(NtmItems.INGOT_NEODYMIUM.get());
                        output.accept(NtmItems.INGOT_BORAX.get());
                        output.accept(NtmItems.INGOT_SODIUM.get());
                        output.accept(NtmItems.INGOT_STRONTIUM.get());
                        output.accept(NtmItems.INGOT_SLAG.get());
                        output.accept(NtmItems.BILLET_URANIUM.get());
                        output.accept(NtmItems.BILLET_U233.get());
                        output.accept(NtmItems.BILLET_U235.get());
                        output.accept(NtmItems.BILLET_U238.get());
                        output.accept(NtmItems.BILLET_UZH.get());
                        output.accept(NtmItems.BILLET_TH232.get());
                        output.accept(NtmItems.BILLET_PLUTONIUM.get());
                        output.accept(NtmItems.BILLET_PU238.get());
                        output.accept(NtmItems.BILLET_PU239.get());
                        output.accept(NtmItems.BILLET_PU240.get());
                        output.accept(NtmItems.BILLET_PU241.get());
                        output.accept(NtmItems.BILLET_PU_MIX.get());
                        output.accept(NtmItems.BILLET_AM241.get());
                        output.accept(NtmItems.BILLET_AM242.get());
                        output.accept(NtmItems.BILLET_AM_MIX.get());
                        output.accept(NtmItems.BILLET_NEPTUNIUM.get());
                        output.accept(NtmItems.BILLET_PO210.get());
                        output.accept(NtmItems.BILLET_TC99.get());
                        output.accept(NtmItems.BILLET_COBALT.get());
                        output.accept(NtmItems.BILLET_CO60.get());
                        output.accept(NtmItems.BILLET_SR90.get());
                        output.accept(NtmItems.BILLET_AU198.get());
                        output.accept(NtmItems.BILLET_PB209.get());
                        output.accept(NtmItems.BILLET_RA226.get());
                        output.accept(NtmItems.BILLET_AC227.get());
                        output.accept(NtmItems.BILLET_SCHRABIDIUM.get());
                        output.accept(NtmItems.BILLET_SOLINIUM.get());
                        output.accept(NtmItems.BILLET_GH336.get());
                        output.accept(NtmItems.BILLET_AUSTRALIUM.get());
                        output.accept(NtmItems.BILLET_AUSTRALIUM_LESSER.get());
                        output.accept(NtmItems.BILLET_AUSTRALIUM_GREATER.get());
                        output.accept(NtmItems.BILLET_URANIUM_FUEL.get());
                        output.accept(NtmItems.BILLET_THORIUM_FUEL.get());
                        output.accept(NtmItems.BILLET_PLUTONIUM_FUEL.get());
                        output.accept(NtmItems.BILLET_NEPTUNIUM_FUEL.get());
                        output.accept(NtmItems.BILLET_MOX_FUEL.get());
                        output.accept(NtmItems.BILLET_AMERICIUM_FUEL.get());
                        output.accept(NtmItems.BILLET_LES.get());
                        output.accept(NtmItems.BILLET_SCHRABIDIUM_FUEL.get());
                        output.accept(NtmItems.BILLET_HES.get());
                        output.accept(NtmItems.BILLET_PO210BE.get());
                        output.accept(NtmItems.BILLET_RA226BE.get());
                        output.accept(NtmItems.BILLET_PU238BE.get());
                        output.accept(NtmItems.BILLET_BERYLLIUM.get());
                        output.accept(NtmItems.BILLET_BISMUTH.get());
                        output.accept(NtmItems.BILLET_SILICON.get());
                        output.accept(NtmItems.BILLET_ZIRCONIUM.get());
                        output.accept(NtmItems.BILLET_ZFB_BISMUTH.get());
                        output.accept(NtmItems.BILLET_ZFB_PU241.get());
                        output.accept(NtmItems.BILLET_ZFB_AM_MIX.get());
                        output.accept(NtmItems.BILLET_YHARONITE.get());
                        output.accept(NtmItems.BILLET_BALEFIRE_GOLD.get());
                        output.accept(NtmItems.BILLET_FLASHLEAD.get());
                        output.accept(NtmItems.BILLET_NUCLEAR_WASTE.get());

                        output.accept(NtmItems.CINNABAR.get());
                        output.accept(NtmItems.MERCURY_NUGGET_TINY.get());
                        output.accept(NtmItems.MERCURY_NUGGET.get());
                        output.accept(NtmItems.MERCURY_BOTTLE.get());
                        output.accept(NtmItems.COKE_COAL.get());
                        output.accept(NtmItems.COKE_LIGNITE.get());
                        output.accept(NtmItems.COKE_PETROLEUM.get());
                        output.accept(NtmItems.LIGNITE.get());
                        output.accept(NtmItems.COAL_INFERNAL.get());
                        output.accept(NtmItems.BRIQUETTE_COAL.get());
                        output.accept(NtmItems.BRIQUETTE_LIGNITE.get());
                        output.accept(NtmItems.BRIQUETTE_WOOD.get());
                        output.accept(NtmItems.SULFUR.get());
                        output.accept(NtmItems.NITER.get());
                        output.accept(NtmItems.NITRA.get());
                        output.accept(NtmItems.NITRA_SMALL.get());
                        output.accept(NtmItems.FLUORITE.get());

                        output.accept(NtmItems.POWDER_COAL.get());
                        output.accept(NtmItems.POWDER_COAL_TINY.get());
                        output.accept(NtmItems.POWDER_IRON.get());
                        output.accept(NtmItems.POWDER_GOLD.get());
                        output.accept(NtmItems.POWDER_LAPIS.get());
                        output.accept(NtmItems.POWDER_QUARTZ.get());
                        output.accept(NtmItems.POWDER_DIAMOND.get());
                        output.accept(NtmItems.POWDER_EMERALD.get());
                        output.accept(NtmItems.POWDER_URANIUM.get());
                        output.accept(NtmItems.POWDER_PLUTONIUM.get());
                        output.accept(NtmItems.POWDER_NEPTUNIUM.get());
                        output.accept(NtmItems.POWDER_PO210.get());
                        output.accept(NtmItems.POWDER_CO60.get());
                        output.accept(NtmItems.POWDER_SR90.get());
                        output.accept(NtmItems.POWDER_SR90_TINY.get());
                        output.accept(NtmItems.POWDER_I131.get());
                        output.accept(NtmItems.POWDER_I131_TINY.get());
                        output.accept(NtmItems.POWDER_XE135.get());
                        output.accept(NtmItems.POWDER_XE135_TINY.get());
                        output.accept(NtmItems.POWDER_CS137.get());
                        output.accept(NtmItems.POWDER_CS137_TINY.get());
                        output.accept(NtmItems.POWDER_AU198.get());
                        output.accept(NtmItems.POWDER_RA226.get());
                        output.accept(NtmItems.POWDER_AT209.get());
                        output.accept(NtmItems.POWDER_TITANIUM.get());
                        output.accept(NtmItems.POWDER_COPPER.get());
                        output.accept(NtmItems.POWDER_RED_COPPER.get());
                        output.accept(NtmItems.POWDER_TUNGSTEN.get());
                        output.accept(NtmItems.POWDER_ALUMINIUM.get());
                        output.accept(NtmItems.POWDER_STEEL.get());
                        output.accept(NtmItems.POWDER_STEEL_TINY.get());
                        output.accept(NtmItems.POWDER_TECHNETIUM.get());
                        output.accept(NtmItems.POWDER_LEAD.get());
                        output.accept(NtmItems.POWDER_BISMUTH.get());
                        output.accept(NtmItems.POWDER_CALCIUM.get());
                        output.accept(NtmItems.POWDER_CADMIUM.get());
                        output.accept(NtmItems.POWDER_COLTAN_ORE.get());
                        output.accept(NtmItems.POWDER_COLTAN.get());
                        output.accept(NtmItems.POWDER_TANTALIUM.get());
                        output.accept(NtmItems.POWDER_TEKTITE.get());
                        output.accept(NtmItems.POWDER_PALEOGENITE.get());
                        output.accept(NtmItems.POWDER_PALEOGENITE_TINY.get());
                        output.accept(NtmItems.POWDER_IMPURE_OSMIRIDIUM.get());
                        output.accept(NtmItems.POWDER_BORAX.get());
                        output.accept(NtmItems.POWDER_CHLOROCALCITE.get());
                        output.accept(NtmItems.POWDER_MOLYSITE.get());
                        output.accept(NtmItems.POWDER_YELLOWCAKE.get());
                        output.accept(NtmItems.POWDER_BERYLLIUM.get());
                        output.accept(NtmItems.POWDER_DURA_STEEL.get());
                        output.accept(NtmItems.POWDER_POLYMER.get());
                        output.accept(NtmItems.POWDER_BAKELITE.get());
                        output.accept(NtmItems.POWDER_SCHRABIDIUM.get());
                        output.accept(NtmItems.POWDER_SCHRABIDATE.get());
                        output.accept(NtmItems.POWDER_MAGNETIZED_TUNGSTEN.get());
                        output.accept(NtmItems.POWDER_CHLOROPHYTE.get());
                        output.accept(NtmItems.POWDER_COMBINE_STEEL.get());
                        output.accept(NtmItems.POWDER_LITHIUM.get());
                        output.accept(NtmItems.POWDER_LITHIUM_TINY.get());
                        output.accept(NtmItems.POWDER_ZIRCONIUM.get());
                        output.accept(NtmItems.POWDER_SODIUM.get());
                        output.accept(NtmItems.POWDER_LIGNITE.get());
                        output.accept(NtmItems.POWDER_IODINE.get());
                        output.accept(NtmItems.POWDER_THORIUM.get());
                        output.accept(NtmItems.POWDER_NEODYMIUM.get());
                        output.accept(NtmItems.POWDER_NEODYMIUM_TINY.get());
                        output.accept(NtmItems.POWDER_ASTATINE.get());
                        output.accept(NtmItems.POWDER_CAESIUM.get());
                        output.accept(NtmItems.POWDER_AUSTRALIUM.get());
                        output.accept(NtmItems.POWDER_STRONTIUM.get());
                        output.accept(NtmItems.POWDER_COBALT.get());
                        output.accept(NtmItems.POWDER_COBALT_TINY.get());
                        output.accept(NtmItems.POWDER_BROMINE.get());
                        output.accept(NtmItems.POWDER_NIOBIUM.get());
                        output.accept(NtmItems.POWDER_NIOBIUM_TINY.get());
                        output.accept(NtmItems.POWDER_TENNESSINE.get());
                        output.accept(NtmItems.POWDER_CERIUM.get());
                        output.accept(NtmItems.POWDER_CERIUM_TINY.get());
                        output.accept(NtmItems.POWDER_LANTHANIUM.get());
                        output.accept(NtmItems.POWDER_LANTHANIUM_TINY.get());
                        output.accept(NtmItems.POWDER_ACTINIUM.get());
                        output.accept(NtmItems.POWDER_ACTINIUM_TINY.get());
                        output.accept(NtmItems.POWDER_BORON.get());
                        output.accept(NtmItems.POWDER_BORON_TINY.get());
                        output.accept(NtmItems.POWDER_ASBESTOS.get());
                        output.accept(NtmItems.POWDER_MAGIC.get());
                        output.accept(NtmItems.POWDER_SAWDUST.get());
                        output.accept(NtmItems.POWDER_FLUX.get());
                        output.accept(NtmItems.POWDER_FERTILIZER.get());
                        output.accept(NtmItems.POWDER_BALEFIRE.get());
                        output.accept(NtmItems.POWDER_SEMTEX_MIX.get());
                        output.accept(NtmItems.POWDER_DESH_MIX.get());
                        output.accept(NtmItems.POWDER_DESH_READY.get());
                        output.accept(NtmItems.POWDER_DESH.get());
                        output.accept(NtmItems.POWDER_NITAN_MIX.get());
                        output.accept(NtmItems.POWDER_SPARK_MIX.get());
                        output.accept(NtmItems.POWDER_METEORITE.get());
                        output.accept(NtmItems.POWDER_METEORITE_TINY.get());
                        output.accept(NtmItems.POWDER_EUPHEMIUM.get());
                        output.accept(NtmItems.POWDER_DINEUTRONIUM.get());
                        output.accept(NtmItems.DUST.get());
                        output.accept(NtmItems.DUST_TINY.get());
                        output.accept(NtmItems.DUST_FALLOUT.get());
                        output.accept(NtmItems.POWDER_ASH_WOOD.get());
                        output.accept(NtmItems.POWDER_ASH_COAL.get());
                        output.accept(NtmItems.POWDER_ASH.get());
                        output.accept(NtmItems.POWDER_ASH_FLY.get());
                        output.accept(NtmItems.POWDER_ASH_SOOT.get());
                        output.accept(NtmItems.POWDER_ASH_FULLERENE.get());
                        output.accept(NtmItems.POWDER_LIMESTONE.get());
                        output.accept(NtmItems.POWDER_CEMENT.get());
                        output.accept(NtmItems.POWDER_RED_PHOSPHOROUS.get());
                        output.accept(NtmItems.POWDER_ICE.get());
                        output.accept(NtmItems.POWDER_POISON.get());
                        output.accept(NtmItems.POWDER_THERMITE.get());
                        output.accept(NtmItems.POWDER_ENERGY.get());
                        output.accept(NtmItems.CORDITE.get());
                        output.accept(NtmItems.BALLISTITE.get());
                        output.accept(NtmItems.BALL_DYNAMITE.get());
                        output.accept(NtmItems.BALL_TNT.get());
                        output.accept(NtmItems.BALL_TATB.get());
                        output.accept(NtmItems.BALL_RESIN.get());
                        output.accept(NtmItems.BALL_FIRECLAY.get());

                        output.accept(NtmItems.CRYSTAL_COAL.get());
                        output.accept(NtmItems.CRYSTAL_IRON.get());
                        output.accept(NtmItems.CRYSTAL_GOLD.get());
                        output.accept(NtmItems.CRYSTAL_REDSTONE.get());
                        output.accept(NtmItems.CRYSTAL_LAPIS.get());
                        output.accept(NtmItems.CRYSTAL_DIAMOND.get());
                        output.accept(NtmItems.CRYSTAL_URANIUM.get());
                        output.accept(NtmItems.CRYSTAL_THORIUM.get());
                        output.accept(NtmItems.CRYSTAL_PLUTONIUM.get());
                        output.accept(NtmItems.CRYSTAL_TITANIUM.get());
                        output.accept(NtmItems.CRYSTAL_SULFUR.get());
                        output.accept(NtmItems.CRYSTAL_NITER.get());
                        output.accept(NtmItems.CRYSTAL_COPPER.get());
                        output.accept(NtmItems.CRYSTAL_TUNGSTEN.get());
                        output.accept(NtmItems.CRYSTAL_ALUMINIUM.get());
                        output.accept(NtmItems.CRYSTAL_FLUORITE.get());
                        output.accept(NtmItems.CRYSTAL_BERYLLIUM.get());
                        output.accept(NtmItems.CRYSTAL_LEAD.get());
                        output.accept(NtmItems.CRYSTAL_SCHRARANIUM.get());
                        output.accept(NtmItems.CRYSTAL_SCHRABIDIUM.get());
                        output.accept(NtmItems.CRYSTAL_RARE.get());
                        output.accept(NtmItems.CRYSTAL_PHOSPHORUS.get());
                        output.accept(NtmItems.CRYSTAL_LITHIUM.get());
                        output.accept(NtmItems.CRYSTAL_COBALT.get());
                        output.accept(NtmItems.CRYSTAL_STARMETAL.get());
                        output.accept(NtmItems.CRYSTAL_CINNABAR.get());
                        output.accept(NtmItems.CRYSTAL_TRIXITE.get());
                        output.accept(NtmItems.CRYSTAL_OSMIRIDIUM.get());

                        output.accept(NtmItems.GEM_SODALITE.get());
                        output.accept(NtmItems.GEM_TANTALIUM.get());
                        output.accept(NtmItems.GEM_VOLCANIC.get());
                        output.accept(NtmItems.GEM_RAD.get());
                        output.accept(NtmItems.GEM_ALEXANDRITE.get());
                        output.accept(NtmItems.FRAGMENT_NEODYMIUM.get());
                        output.accept(NtmItems.FRAGMENT_COBALT.get());
                        output.accept(NtmItems.FRAGMENT_NIOBIUM.get());
                        output.accept(NtmItems.FRAGMENT_CERIUM.get());
                        output.accept(NtmItems.FRAGMENT_LANTHANIUM.get());
                        output.accept(NtmItems.FRAGMENT_AC227.get());
                        output.accept(NtmItems.FRAGMENT_BORON.get());
                        output.accept(NtmItems.FRAGMENT_METEORITE.get());
                        output.accept(NtmItems.FRAGMENT_COLTAN.get());
                        output.accept(NtmItems.CHUNK_RARE.get());
                        output.accept(NtmItems.CHUNK_MALACHITE.get());
                        output.accept(NtmItems.CHUNK_CRYOLITE.get());
                        output.accept(NtmItems.MOONSTONE.get());
                        output.accept(NtmItems.BIOMASS.get());
                        output.accept(NtmItems.BIOMASS_COMPRESSED.get());
                        output.accept(NtmItems.BIO_WAFER.get());

                        output.accept(NtmItems.NUGGET_URANIUM.get());
                        output.accept(NtmItems.NUGGET_U233.get());
                        output.accept(NtmItems.NUGGET_U235.get());
                        output.accept(NtmItems.NUGGET_U238.get());
                        output.accept(NtmItems.NUGGET_TH232.get());
                        output.accept(NtmItems.NUGGET_PLUTONIUM.get());
                        output.accept(NtmItems.NUGGET_PU238.get());
                        output.accept(NtmItems.NUGGET_PU239.get());
                        output.accept(NtmItems.NUGGET_PU240.get());
                        output.accept(NtmItems.NUGGET_PU241.get());
                        output.accept(NtmItems.NUGGET_PU_MIX.get());
                        output.accept(NtmItems.NUGGET_AM241.get());
                        output.accept(NtmItems.NUGGET_AM242.get());
                        output.accept(NtmItems.NUGGET_AM_MIX.get());
                        output.accept(NtmItems.NUGGET_NEPTUNIUM.get());
                        output.accept(NtmItems.NUGGET_PO210.get());
                        output.accept(NtmItems.NUGGET_COBALT.get());
                        output.accept(NtmItems.NUGGET_CO60.get());
                        output.accept(NtmItems.NUGGET_SR90.get());
                        output.accept(NtmItems.NUGGET_TC99.get());
                        output.accept(NtmItems.NUGGET_AU198.get());
                        output.accept(NtmItems.NUGGET_PB209.get());
                        output.accept(NtmItems.NUGGET_RA226.get());
                        output.accept(NtmItems.NUGGET_AC227.get());
                        output.accept(NtmItems.NUGGET_LEAD.get());
                        output.accept(NtmItems.NUGGET_BISMUTH.get());
                        output.accept(NtmItems.NUGGET_ARSENIC.get());
                        output.accept(NtmItems.NUGGET_TANTALIUM.get());
                        output.accept(NtmItems.NUGGET_SILICON.get());
                        output.accept(NtmItems.NUGGET_NIOBIUM.get());
                        output.accept(NtmItems.NUGGET_BERYLLIUM.get());
                        output.accept(NtmItems.NUGGET_SCHRABIDIUM.get());
                        output.accept(NtmItems.NUGGET_SOLINIUM.get());
                        output.accept(NtmItems.NUGGET_GH336.get());
                        output.accept(NtmItems.NUGGET_URANIUM_FUEL.get());
                        output.accept(NtmItems.NUGGET_THORIUM_FUEL.get());
                        output.accept(NtmItems.NUGGET_PLUTONIUM_FUEL.get());
                        output.accept(NtmItems.NUGGET_NEPTUNIUM_FUEL.get());
                        output.accept(NtmItems.NUGGET_MOX_FUEL.get());
                        output.accept(NtmItems.NUGGET_AMERICIUM_FUEL.get());
                        output.accept(NtmItems.NUGGET_SCHRABIDIUM_FUEL.get());
                        output.accept(NtmItems.NUGGET_HES.get());
                        output.accept(NtmItems.NUGGET_LES.get());
                        output.accept(NtmItems.NUGGET_ZIRCONIUM.get());
                        output.accept(NtmItems.NUGGET_AUSTRALIUM.get());
                        output.accept(NtmItems.NUGGET_AUSTRALIUM_LESSER.get());
                        output.accept(NtmItems.NUGGET_AUSTRALIUM_GREATER.get());
                        output.accept(NtmItems.NUGGET_DESH.get());
                        output.accept(NtmItems.NUGGET_EUPHEMIUM.get());
                        output.accept(NtmItems.NUGGET_DINEUTRONIUM.get());
                        output.accept(NtmItems.NUGGET_OSMIRIDIUM.get());

                        output.accept(NtmItems.NEUTRON_REFLECTOR.get());

                        output.accept(NtmItems.PLATE_IRON.get());
                        output.accept(NtmItems.PLATE_GOLD.get());
                        output.accept(NtmItems.PLATE_TITANIUM.get());
                        output.accept(NtmItems.PLATE_ALUMINIUM.get());
                        output.accept(NtmItems.PLATE_STEEL.get());
                        output.accept(NtmItems.PLATE_LEAD.get());
                        output.accept(NtmItems.PLATE_COPPER.get());
                        output.accept(NtmItems.PLATE_DURA_STEEL.get());
                        output.accept(NtmItems.PLATE_SCHRABIDIUM.get());
                        output.accept(NtmItems.PLATE_COMBINE_STEEL.get());
                        output.accept(NtmItems.PLATE_MIXED.get());
                        output.accept(NtmItems.PLATE_GUNMETAL.get());
                        output.accept(NtmItems.PLATE_WEAPONSTEEL.get());
                        output.accept(NtmItems.PLATE_SATURNITE.get());
                        output.accept(NtmItems.PLATE_PAA.get());
                        output.accept(NtmItems.PLATE_POLYMER.get());
                        output.accept(NtmItems.PLATE_KEVLAR.get());
                        output.accept(NtmItems.PLATE_DALEKANIUM.get());
                        output.accept(NtmItems.PLATE_DESH.get());
                        output.accept(NtmItems.PLATE_BISMUTH.get());
                        output.accept(NtmItems.PLATE_EUPHEMIUM.get());
                        output.accept(NtmItems.PLATE_DINEUTRONIUM.get());
                        output.accept(NtmItems.PLATE_ARMOR_TITANIUM.get());
                        output.accept(NtmItems.PLATE_ARMOR_AJR.get());
                        output.accept(NtmItems.PLATE_ARMOR_HEV.get());
                        output.accept(NtmItems.PLATE_ARMOR_LUNAR.get());
                        output.accept(NtmItems.PLATE_ARMOR_FAU.get());
                        output.accept(NtmItems.PLATE_ARMOR_DNT.get());
                    }).build());

    // items that belong in machines, fuels, etc
    public static final Supplier<CreativeModeTab> CONTROL = CREATIVE_MODE_TABS.register(
            "control",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.PELLET_RTG.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("parts"))
                    .title(Component.translatable("itemGroup.control"))
                    .displayItems((itemDisplayParameters, output) -> {
                        addMetaItems(output, NtmItems.BLUEPRINTS.get());
                        output.accept(NtmItems.CELL_EMPTY);
                        output.accept(NtmItems.CELL_UF6);
                        output.accept(NtmItems.CELL_PUF6);
                        output.accept(NtmItems.CELL_DEUTERIUM);
                        output.accept(NtmItems.CELL_TRITIUM);
                        output.accept(NtmItems.CELL_SAS3);
                        output.accept(NtmItems.CELL_ANTIMATTER);
                        output.accept(NtmItems.CELL_ANTI_SCHARBIDIUM);
                        output.accept(NtmItems.CELL_BALEFIRE);

                        output.accept(NtmItems.PARTICLE_DIGAMMA);
                        output.accept(NtmItems.PARTICLE_LUTECE);

                        output.accept(NtmItems.SINGULARITY);
                        output.accept(NtmItems.SINGULARITY_COUNTER_RESONANT);
                        output.accept(NtmItems.SINGULARITY_SUPER_HEATED);
                        output.accept(NtmItems.BLACK_HOLE);
                        output.accept(NtmItems.SINGULARITY_SPARK);
                        output.accept(NtmItems.PELLET_ANTIMATTER);

                        FluidType[] types = Fluids.getInNiceOrder();
                        // tanks
                        output.accept(NtmItems.FLUID_TANK_EMPTY.get());
                        for(int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_TANK_FULL.get(), 1), id));
                        }
                        // lead tanks
                        output.accept(NtmItems.FLUID_TANK_LEAD_EMPTY.get());
                        for(int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_TANK_LEAD_FULL.get(), 1), id));
                        }
                        // barrels
                        output.accept(NtmItems.FLUID_BARREL_EMPTY.get());
                        for(int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_BARREL_FULL.get(), 1), id));
                        }
                        // fluid packs
                        output.accept(NtmItems.FLUID_PACK_EMPTY.get());
                        for(int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_PACK_FULL.get(), 1), id));
                        }
                        output.accept(NtmItems.FLUID_BARREL_INFINITE.get());
                        output.accept(NtmItems.INF_WATER.get());
                        output.accept(NtmItems.INF_WATER_MK2.get());

                        addMetaItems(output, NtmItems.BATTERY_PACK.get());
                        addMetaItems(output, NtmItems.BATTERY_SC.get());

                        output.accept(NtmItems.BATTERY_CREATIVE);

                        output.accept(NtmItems.ROD_EMPTY);
                        addMetaItems(output, NtmItems.ROD.get());
                        output.accept(NtmItems.ROD_DUAL_EMPTY);
                        addMetaItems(output, NtmItems.ROD_DUAL.get());
                        output.accept(NtmItems.ROD_QUAD_EMPTY);
                        addMetaItems(output, NtmItems.ROD_QUAD.get());

                        output.accept(NtmItems.REACHER);
                    }).build());

    // templates, siren tracks
    /** SKIP */

    // ore and mineral blocks
    public static final Supplier<CreativeModeTab> BLOCKS = CREATIVE_MODE_TABS.register(
            "blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmBlocks.ORE_URANIUM.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("control"))
                    .title(Component.translatable("itemGroup.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {

                        addMetaItems(output, NtmBlocks.BOBBLEHEAD.asItem());
                        output.accept(NtmBlocks.GRAVEL_OBSIDIAN);
                        output.accept(NtmBlocks.GRAVEL_DIAMOND);

                        output.accept(NtmBlocks.ASPHALT);
                        output.accept(NtmBlocks.ASPHALT_LIGHT);

                        output.accept(NtmBlocks.BRICK_CONCRETE);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MOSSY);
                        output.accept(NtmBlocks.BRICK_CONCRETE_CRACKED);
                        output.accept(NtmBlocks.BRICK_CONCRETE_BROKEN);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MARKED);
                        output.accept(NtmBlocks.BRICK_OBSIDIAN);
                        output.accept(NtmBlocks.BRICK_LIGHT);
                        output.accept(NtmBlocks.BRICK_ASBESTOS);
                        output.accept(NtmBlocks.BRICK_FIRE);

                        output.accept(NtmBlocks.BRICK_CONCRETE_SLAB);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB);
                        output.accept(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB);
                        output.accept(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB);

                        output.accept(NtmBlocks.BRICK_CONCRETE_STAIRS);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
                        output.accept(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
                        output.accept(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

                        addMetaItems(output, NtmBlocks.BARBED_WIRE.asItem());
                        output.accept(NtmBlocks.SPIKES);

                        output.accept(NtmBlocks.ORE_OIL);
                        output.accept(NtmBlocks.ORE_OIL_EMPTY);
                        output.accept(NtmBlocks.ORE_BEDROCK_OIL);
                        output.accept(NtmBlocks.WASTE_EARTH);
                        output.accept(NtmBlocks.STONE_CRACKED);
                        output.accept(NtmBlocks.DIRT_OILY);
                        output.accept(NtmBlocks.DIRT_DEAD);
                        output.accept(NtmBlocks.ORE_OIL_SAND);
                        output.accept(NtmBlocks.SAND_OILY);
                        output.accept(NtmBlocks.SAND_RED_OILY);
                        output.accept(NtmBlocks.WASTE_MYCELIUM);
                        output.accept(NtmBlocks.WASTE_TRINITITE);
                        output.accept(NtmBlocks.WASTE_TRINITITE_RED);
                        output.accept(NtmBlocks.WASTE_LOG);
                        output.accept(NtmBlocks.WASTE_LEAVES);
                        output.accept(NtmBlocks.WASTE_PLANKS);
                        output.accept(NtmBlocks.FROZEN_GRASS);
                        output.accept(NtmBlocks.FROZEN_DIRT);
                        output.accept(NtmBlocks.FROZEN_PLANKS);
                        output.accept(NtmBlocks.FROZEN_LOG);
                        output.accept(NtmBlocks.FALLOUT);
                        output.accept(NtmBlocks.LEAVES_LAYER);

                        output.accept(NtmBlocks.SELLAFIELD_SLAKED);
                        output.accept(NtmBlocks.SELLAFIELD_BEDROCK);
                        output.accept(NtmBlocks.ORE_SELLAFIELD_DIAMOND);
                        output.accept(NtmBlocks.ORE_SELLAFIELD_EMERALD);
                    }).build());

    // machines, structure parts
    public static final Supplier<CreativeModeTab> MACHINE = CREATIVE_MODE_TABS.register(
            "machine",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmBlocks.PWR_CONTROLLER.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("blocks"))
                    .title(Component.translatable("itemGroup.machine"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmBlocks.GAS_RADON);
                        output.accept(NtmBlocks.GAS_RADON_DENSE);
                        output.accept(NtmBlocks.GAS_RADON_TOMB);
                        output.accept(NtmBlocks.GAS_MELTDOWN);
                        output.accept(NtmBlocks.GAS_MONOXIDE);
                        output.accept(NtmBlocks.GAS_ASBESTOS);
                        output.accept(NtmBlocks.GAS_COAL);
                        output.accept(NtmBlocks.GAS_FLAMMABLE);
                        output.accept(NtmBlocks.GAS_EXPLOSIVE);

                        output.accept(NtmBlocks.GEIGER);

                        output.accept(NtmBlocks.RED_CABLE);

                        addMetaItems(output, NtmBlocks.FLUID_DUCT_NEO.asItem());

                        output.accept(NtmBlocks.MACHINE_BATTERY_SOCKET);
                        output.accept(NtmBlocks.MACHINE_BATTERY_REDD);
                        output.accept(NtmBlocks.MACHINE_ASSEMBLY_MACHINE);
                        output.accept(NtmBlocks.MACHINE_CHEMICAL_PLANT);
                        output.accept(NtmBlocks.MACHINE_PRESS);
                        output.accept(NtmBlocks.MACHINE_CENTRIFUGE);
                        output.accept(NtmBlocks.MACHINE_GAS_CENTRIFUGE);
                        output.accept(NtmBlocks.MACHINE_FLUID_TANK);
                        output.accept(NtmBlocks.MACHINE_SHREDDER);
                        output.accept(NtmBlocks.MACHINE_SOLDERING_STATION);
                        output.accept(NtmBlocks.MACHINE_ARC_WELDER);
                        output.accept(NtmBlocks.HEAT_BOILER);
                        output.accept(NtmBlocks.MACHINE_INDUSTRIAL_BOILER);

                        output.accept(NtmBlocks.MACHINE_OIL_DERRICK);
                        output.accept(NtmBlocks.MACHINE_PUMPJACK);
                        output.accept(NtmBlocks.MACHINE_FRACKING_TOWER);
                        output.accept(NtmBlocks.MACHINE_REFINERY);
                        output.accept(NtmBlocks.FURNACE_COMBINATION);
                        output.accept(NtmBlocks.MACHINE_BLAST_FURNACE);

                        output.accept(NtmBlocks.HEATER_OVEN);
                        output.accept(NtmBlocks.HEATER_FIREBOX);
                        output.accept(NtmBlocks.HEATER_FLUID_BURNER);
                        output.accept(NtmBlocks.HEATER_ELECTRIC);
                        output.accept(NtmBlocks.HEATER_HEATEX);

                        addMetaItems(output, NtmBlocks.ANVIL.asItem());
                        output.accept(NtmBlocks.MACHINE_SATLINKER);
                        output.accept(NtmBlocks.DECONTAMINATOR);

                        output.accept(NtmBlocks.CRATE_IRON);
                        output.accept(NtmBlocks.CRATE_TUNGSTEN);
                        output.accept(NtmBlocks.CRATE_STEEL);
                        output.accept(NtmBlocks.CRATE_DESH);

                        output.accept(NtmItems.STAMP_FLAT_STONE.get());
                        output.accept(NtmItems.STAMP_PLATE_STONE.get());
                        output.accept(NtmItems.STAMP_WIRE_STONE.get());
                        output.accept(NtmItems.STAMP_CIRCUIT_STONE.get());
                        output.accept(NtmItems.STAMP_FLAT_IRON.get());
                        output.accept(NtmItems.STAMP_PLATE_IRON.get());
                        output.accept(NtmItems.STAMP_WIRE_IRON.get());
                        output.accept(NtmItems.STAMP_CIRCUIT_IRON.get());
                        output.accept(NtmItems.STAMP_FLAT_STEEL.get());
                        output.accept(NtmItems.STAMP_PLATE_STEEL.get());
                        output.accept(NtmItems.STAMP_WIRE_STEEL.get());
                        output.accept(NtmItems.STAMP_CIRCUIT_STEEL.get());
                        output.accept(NtmItems.STAMP_FLAT_TITANIUM.get());
                        output.accept(NtmItems.STAMP_PLATE_TITANIUM.get());
                        output.accept(NtmItems.STAMP_WIRE_TITANIUM.get());
                        output.accept(NtmItems.STAMP_CIRCUIT_TITANIUM.get());
                        output.accept(NtmItems.STAMP_FLAT_OBSIDIAN.get());
                        output.accept(NtmItems.STAMP_PLATE_OBSIDIAN.get());
                        output.accept(NtmItems.STAMP_WIRE_OBSIDIAN.get());
                        output.accept(NtmItems.STAMP_CIRCUIT_OBSIDIAN.get());
                        output.accept(NtmItems.STAMP_FLAT_DESH.get());
                        output.accept(NtmItems.STAMP_PLATE_DESH.get());
                        output.accept(NtmItems.STAMP_WIRE_DESH.get());
                        output.accept(NtmItems.STAMP_CIRCUIT_DESH.get());
                        output.accept(NtmItems.STAMP_IRON_357.get());
                        output.accept(NtmItems.STAMP_IRON_44.get());
                        output.accept(NtmItems.STAMP_IRON_9.get());
                        output.accept(NtmItems.STAMP_IRON_50.get());
                        output.accept(NtmItems.STAMP_DESH_357.get());
                        output.accept(NtmItems.STAMP_DESH_44.get());
                        output.accept(NtmItems.STAMP_DESH_9.get());
                        output.accept(NtmItems.STAMP_DESH_50.get());

                        FluidType[] types = Fluids.getInNiceOrder();
                        // multi identifiers
                        for(int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            output.accept(FluidIDMultiItem.createStack(type));
                        }
                    }).build());

    // bombs
    public static final Supplier<CreativeModeTab> NUKE = CREATIVE_MODE_TABS.register(
            "nuke",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmBlocks.NUKE_FAT_MAN.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("machine"))
                    .title(Component.translatable("itemGroup.nuke"))
                    .backgroundTexture(ResourceLocation.fromNamespaceAndPath(NuclearTechMod.MODID, "textures/gui/nuke_tab.png"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmBlocks.NUKE_GADGET);
                        output.accept(NtmBlocks.NUKE_LITTLE_BOY);
                        output.accept(NtmBlocks.NUKE_FAT_MAN);
                        output.accept(NtmBlocks.NUKE_IVY_MIKE);
                        output.accept(NtmBlocks.NUKE_TSAR_BOMBA);
                        output.accept(NtmBlocks.NUKE_PROTOTYPE);
                        output.accept(NtmBlocks.NUKE_FLEIJA);
                        output.accept(NtmBlocks.NUKE_N2);
                        output.accept(NtmBlocks.NUKE_FSTBMB);

                        addMetaItems(output, NtmBlocks.CRASHED_BOMB.asItem());

                        output.accept(NtmBlocks.DYNAMITE);
                        output.accept(NtmBlocks.TNT);
                        output.accept(NtmBlocks.SEMTEX);
                        output.accept(NtmBlocks.C4);
                        output.accept(NtmBlocks.FISSURE_BOMB);

                        output.accept(NtmBlocks.MINE_AP);
                        output.accept(NtmBlocks.MINE_SHRAP);
                        output.accept(NtmBlocks.MINE_HE);
                        output.accept(NtmBlocks.MINE_FAT);
                        output.accept(NtmBlocks.MINE_NAVAL);

                        output.accept(NtmBlocks.DET_CORD);
                        output.accept(NtmBlocks.DET_CHARGE);
                        output.accept(NtmBlocks.DET_NUKE);
                        output.accept(NtmBlocks.DET_MINER);

                        output.accept(NtmBlocks.BARREL_RED);
                        output.accept(NtmBlocks.BARREL_PINK);
                        output.accept(NtmBlocks.BARREL_LOX);
                        output.accept(NtmBlocks.BARREL_TAINT);

                        addMetaItems(output, NtmBlocks.VOLCANO_CORE.asItem());
                        addMetaItems(output, NtmBlocks.VOLCANO_RAD_CORE.asItem());

                        output.accept(NtmItems.BATTERY_SPARK);
                        output.accept(NtmItems.BATTERY_TRIXITE);

                        output.accept(NtmItems.EARLY_EXPLOSIVE_LENSES);
                        output.accept(NtmItems.EXPLOSIVE_LENSES);

                        output.accept(NtmItems.GADGET_WIREING);
                        output.accept(NtmItems.GADGET_CORE);

                        output.accept(NtmItems.LITTLE_BOY_SHIELDING);
                        output.accept(NtmItems.LITTLE_BOY_TARGET);
                        output.accept(NtmItems.LITTLE_BOY_BULLET);
                        output.accept(NtmItems.LITTLE_BOY_PROPELLANT);
                        output.accept(NtmItems.LITTLE_BOY_IGNITER);

                        output.accept(NtmItems.FAT_MAN_CORE);
                        output.accept(NtmItems.FAT_MAN_IGNITER);

                        output.accept(NtmItems.IVY_MIKE_CORE);
                        output.accept(NtmItems.IVY_MIKE_DEUT);
                        output.accept(NtmItems.IVY_MIKE_COOLING_UNIT);

                        output.accept(NtmItems.TSAR_BOMBA_CORE);

                        output.accept(NtmItems.FLEIJA_IGNITER);
                        output.accept(NtmItems.FLEIJA_PROPELLANT);
                        output.accept(NtmItems.FLEIJA_CORE);

                        output.accept(NtmItems.N2_CHARGE);

                        output.accept(NtmItems.EGG_BALEFIRE_SHARD);
                        output.accept(NtmItems.EGG_BALEFIRE);

                        output.accept(NtmItems.IGNITER);
                        output.accept(NtmItems.DETONATOR);
                        output.accept(NtmItems.DETONATOR_MULTI);
                        output.accept(NtmItems.DETONATOR_LASER);
                        output.accept(NtmItems.DETONATOR_DEADMAN);
                        output.accept(NtmItems.DETONATOR_DE);

                        // this is sucks
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.GADGET));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.LITTLE_BOY));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.FAT_MAN));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.IVY_MIKE));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.TSAR_BOMBA));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.PROTOTYPE));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.FLEIJA));
                    }).build());

    // missiles, satellites
    public static final Supplier<CreativeModeTab> MISSILE = CREATIVE_MODE_TABS.register(
            "missile",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.MISSILE_DOOMSDAY.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("nuke"))
                    .title(Component.translatable("itemGroup.missile"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(NtmBlocks.LAUNCH_PAD);

                        output.accept(NtmItems.DESIGNATOR);
                        output.accept(NtmItems.DESIGNATOR_RANGE);

                        output.accept(NtmItems.MISSILE_TAINT);
                        output.accept(NtmItems.MISSILE_MICRO);
                        output.accept(NtmItems.MISSILE_BHOLE);
                        output.accept(NtmItems.MISSILE_SCHRABIDIUM);
                        output.accept(NtmItems.MISSILE_EMP);
                        output.accept(NtmItems.MISSILE_GENERIC);
                        output.accept(NtmItems.MISSILE_DECOY);
                        output.accept(NtmItems.MISSILE_INCENDIARY);
                        output.accept(NtmItems.MISSILE_CLUSTER);
                        output.accept(NtmItems.MISSILE_BUSTER);
                        output.accept(NtmItems.MISSILE_STEALTH);
                        output.accept(NtmItems.MISSILE_STRONG);
                        output.accept(NtmItems.MISSILE_INCENDIARY_STRONG);
                        output.accept(NtmItems.MISSILE_CLUSTER_STRONG);
                        output.accept(NtmItems.MISSILE_BUSTER_STRONG);
                        output.accept(NtmItems.MISSILE_EMP_STRONG);
                        output.accept(NtmItems.MISSILE_BURST);
                        output.accept(NtmItems.MISSILE_INFERNO);
                        output.accept(NtmItems.MISSILE_RAIN);
                        output.accept(NtmItems.MISSILE_DRILL);
                        output.accept(NtmItems.MISSILE_SHUTTLE);
                        output.accept(NtmItems.MISSILE_NUCLEAR);
                        output.accept(NtmItems.MISSILE_NUCLEAR_CLUSTER);
                        output.accept(NtmItems.MISSILE_VOLCANO);
                        output.accept(NtmItems.MISSILE_DOOMSDAY);
                        output.accept(NtmItems.MISSILE_DOOMSDAY_RUSTED);

                        output.accept(NtmItems.SATELLITE_RADAR);
                        output.accept(NtmItems.SATELLITE_LASER);
                        output.accept(NtmItems.SATELLITE_INTERFACE);
                    }).build());

    // turrets, weapons, ammo
    /** SKIP */

    // drinks, kits, tools
    public static final Supplier<CreativeModeTab> CONSUMABLE = CREATIVE_MODE_TABS.register(
            "consumable",
            () -> CreativeModeTab.builder().icon(() -> MetaHelper.newStack(NtmItems.DRINK, DrinkType.NUKA))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("missile"))
                    .title(Component.translatable("itemGroup.consumable"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmItems.SPAWN_DUCK);

                        output.accept(NtmItems.DOSIMETER);
                        output.accept(NtmItems.GEIGER_COUNTER);
                        output.accept(NtmItems.DIGAMMA_DIAGNOSTIC);

                        output.accept(NtmItems.BALEFIRE_AND_STEEL);

                        addMetaItems(output, NtmItems.DRINK.get());
                        output.accept(NtmItems.BOTTLE_OPENER);
                        addMetaItems(output, NtmItems.CANNED_CONSERVE.get());
                        addMetaItems(output, NtmItems.CAP.get());
                        output.accept(NtmItems.RING_PULL);
                        output.accept(NtmItems.CAN_KEY);

                        output.accept(NtmItems.CHOCOLATE_MILK);
                        output.accept(NtmItems.CIGARETTE);
                        output.accept(NtmItems.CRACKPIPE);

                        addMetaItems(output, NtmItems.BOMB_CALLER.get());
                        output.accept(NtmItems.POLAROID);
                    }).build());

    public static final Supplier<CreativeModeTab> AUTOGEN_TAB = CREATIVE_MODE_TABS.register(
            "autogen",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.SPAWN_DUCK.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("consumable"))
                    .title(Component.translatable("itemGroup.autogen"))
                    .displayItems((itemDisplayParameters, output) -> {

                        for (NTMMaterial mat : Mats.orderedList) {
                            for (MaterialShapes shape : mat.autogen) {
                                DeferredItem<Item> item = mat.generatedItems.get(shape);
                                if (item != null) output.accept(item.get());
                            }
                        }

                    }).build());

    private static void addMetaItems(CreativeModeTab.Output output, Item item) {
        if(item instanceof IMetaItem metaItem) {
            List<ItemStack> stacks = new ArrayList<>();
            metaItem.getSubItems(item, stacks);

            for(ItemStack stack : stacks) {
                output.accept(stack);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}