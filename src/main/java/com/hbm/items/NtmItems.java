package com.hbm.items;

import api.hbm.block.IToolable.ToolType;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.NtmFoods;
import com.hbm.inventory.NtmTiers;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.material.MatsItemGen;
import com.hbm.items.ItemEnums.CapType;
import com.hbm.items.ItemEnums.CasingType;
import com.hbm.items.food.ConserveItem;
import com.hbm.items.food.DrinkItem;
import com.hbm.items.food.EnergyItem;
import com.hbm.items.machine.*;
import com.hbm.items.special.*;
import com.hbm.items.tools.*;
import com.hbm.items.weapon.MissileItem;
import com.hbm.items.weapon.MissileItem.MissileFormFactor;
import com.hbm.items.weapon.MissileItem.MissileFuel;
import com.hbm.items.weapon.MissileItem.MissileTier;
import com.hbm.items.weapon.sedna.factory.GunFactory;
import com.hbm.main.NuclearTechMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class NtmItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NuclearTechMod.MODID);

    // Ingots
    public static final DeferredItem<Item> INGOT_URANIUM = ITEMS.register("ingot_uranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U233 = ITEMS.register("ingot_u233", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U235 = ITEMS.register("ingot_u235", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U238 = ITEMS.register("ingot_u238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U238M2 = ITEMS.register("ingot_u238m2", () -> new Item(new Item.Properties())); // not in hbm 1.7.10
    public static final DeferredItem<Item> INGOT_TH232 = ITEMS.register("ingot_th232", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PLUTONIUM = ITEMS.register("ingot_plutonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU238 = ITEMS.register("ingot_pu238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU239 = ITEMS.register("ingot_pu239", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU240 = ITEMS.register("ingot_pu240", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU241 = ITEMS.register("ingot_pu241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU_MIX = ITEMS.register("ingot_pu_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AM241 = ITEMS.register("ingot_am241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AM242 = ITEMS.register("ingot_am242", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AM_MIX = ITEMS.register("ingot_am_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NEPTUNIUM = ITEMS.register("ingot_neptunium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PO210 = ITEMS.register("ingot_po210", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TC99 = ITEMS.register("ingot_tc99", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CO60 = ITEMS.register("ingot_co60", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SR90 = ITEMS.register("ingot_sr90", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AU198 = ITEMS.register("ingot_au198", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PB209 = ITEMS.register("ingot_pb209", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_RA226 = ITEMS.register("ingot_ra226", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TITANIUM = ITEMS.register("ingot_titanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_INDUSTRIAL_COPPER = ITEMS.register("ingot_industrial_copper", () -> new Item(new Item.Properties())); // minecraft already has copper, but its very cheap, we gotta balance that
    public static final DeferredItem<Item> INGOT_RED_COPPER = ITEMS.register("ingot_red_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TUNGSTEN = ITEMS.register("ingot_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TUNGSTEN_CARBIDE = ITEMS.register("ingot_tungsten_carbide", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ALUMINIUM = ITEMS.register("ingot_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_STEEL = ITEMS.register("ingot_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TCALLOY = ITEMS.register("ingot_tcalloy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CDALLOY = ITEMS.register("ingot_cdalloy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BISMUTH_BRONZE = ITEMS.register("ingot_bismuth_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ARSENIC_BRONZE = ITEMS.register("ingot_arsenic_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BSCCO = ITEMS.register("ingot_bscco", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LEAD = ITEMS.register("ingot_lead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BISMUTH = ITEMS.register("ingot_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ARSENIC = ITEMS.register("ingot_arsenic", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CALCIUM = ITEMS.register("ingot_calcium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CADMIUM = ITEMS.register("ingot_cadmium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TANTALIUM = ITEMS.register("ingot_tantalium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SILICON = ITEMS.register("ingot_silicon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NIOBIUM = ITEMS.register("ingot_niobium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BERYLLIUM = ITEMS.register("ingot_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_COBALT = ITEMS.register("ingot_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BORON = ITEMS.register("ingot_boron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_GRAPHITE = ITEMS.register("ingot_graphite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_FIREBRICK = ITEMS.register("ingot_firebrick", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DURA_STEEL = ITEMS.register("ingot_dura_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_POLYMER = ITEMS.register("ingot_polymer", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BAKELITE = ITEMS.register("ingot_bakelite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BIORUBBER = ITEMS.register("ingot_biorubber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_RUBBER = ITEMS.register("ingot_rubber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PC = ITEMS.register("ingot_pc", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PVC = ITEMS.register("ingot_pvc", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_MUD = ITEMS.register("ingot_mud", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CTF = ITEMS.register("ingot_cft", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRARANIUM = ITEMS.register("ingot_schraranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRABIDIUM = ITEMS.register("ingot_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRABIDATE = ITEMS.register("ingot_schrabidate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_MAGNETIZED_TUNGSTEN = ITEMS.register("ingot_magnetized_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_COMBINE_STEEL = ITEMS.register("ingot_combine_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SOLINIUM = ITEMS.register("ingot_solinium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_GH336 = ITEMS.register("ingot_gh336", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_URANIUM_FUEL = ITEMS.register("ingot_uranium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_THORIUM_FUEL = ITEMS.register("ingot_thorium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PLUTONIUM_FUEL = ITEMS.register("ingot_plutonium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NEPTUNIUM_FUEL = ITEMS.register("ingot_neptunium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_MOX_FUEL = ITEMS.register("ingot_mox_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AMERICIUM_FUEL = ITEMS.register("ingot_americium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRABIDIUM_FUEL = ITEMS.register("ingot_schrabidium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_HES = ITEMS.register("ingot_hes", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LES = ITEMS.register("ingot_les", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AUSTRALIUM = ITEMS.register("ingot_australium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LANTHANIUM = ITEMS.register("ingot_lanthanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AC227 = ITEMS.register("ingot_ac227", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DESH = ITEMS.register("ingot_desh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_FERROURANIUM = ITEMS.register("ingot_ferrouranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_STARMETAL = ITEMS.register("ingot_starmetal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_GUNMETAL = ITEMS.register("ingot_gunmetal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_WEAPONSTEEL = ITEMS.register("ingot_weaponsteel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SATURNITE = ITEMS.register("ingot_saturnite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_EUPHEMIUM = ITEMS.register("ingot_euphemium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DINEUTRONIUM = ITEMS.register("ingot_dineutronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ELECTRONIUM = ITEMS.register("ingot_electronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SMORE = ITEMS.register("ingot_smore", () -> new Item(new Item.Properties().food(NtmFoods.SMORE)));
    public static final DeferredItem<Item> INGOT_OSMIRIDIUM = ITEMS.register("ingot_osmiridium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DUSTED_STEEL = ITEMS.register("ingot_steel_dusted", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CHAINSTEEL = ITEMS.register("ingot_chainsteel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_METEORITE = ITEMS.register("ingot_meteorite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_METEORITE_FORGED = ITEMS.register("ingot_meteorite_forged", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> METEORITE_BLADE = ITEMS.register("blade_meteorite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PHOSPHORUS = ITEMS.register("ingot_phosphorus", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LITHIUM = ITEMS.register("ingot_lithium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ZIRCONIUM = ITEMS.register("ingot_zirconium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SEMTEX = ITEMS.register("ingot_semtex", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_C4 = ITEMS.register("ingot_c4", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TAR_OIL = ITEMS.register("tar_oil", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TAR_CRACK_OIL = ITEMS.register("tar_crack_oil", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TAR_COAL = ITEMS.register("tar_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TAR_WOOD = ITEMS.register("tar_wood", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAX_PETROLEUM = ITEMS.register("wax_petroleum", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAX_PARAFFIN = ITEMS.register("wax_paraffin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLID_FUEL = ITEMS.register("solid_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLID_FUEL_PRESTO = ITEMS.register("solid_fuel_presto", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLID_FUEL_PRESTO_TRIPLET = ITEMS.register("solid_fuel_presto_triplet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLID_FUEL_BF = ITEMS.register("solid_fuel_bf", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLID_FUEL_PRESTO_BF = ITEMS.register("solid_fuel_presto_bf", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLID_FUEL_PRESTO_TRIPLET_BF = ITEMS.register("solid_fuel_presto_triplet_bf", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROCKET_FUEL = ITEMS.register("rocket_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_REDSTONE = ITEMS.register("ingot_redstone", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NEODYMIUM = ITEMS.register("ingot_neodymium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BORAX = ITEMS.register("ingot_borax", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SODIUM = ITEMS.register("ingot_sodium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_STRONTIUM = ITEMS.register("ingot_strontium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SLAG = ITEMS.register("ingot_slag", () -> new Item(new Item.Properties()));

    // Billets
    public static final DeferredItem<Item> BILLET_URANIUM = ITEMS.register("billet_uranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_U233 = ITEMS.register("billet_u233", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_U235 = ITEMS.register("billet_u235", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_U238 = ITEMS.register("billet_u238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_UZH = ITEMS.register("billet_uzh", () -> new Item(new Item.Properties())); // not in hbm 1.7.10
    public static final DeferredItem<Item> BILLET_TH232 = ITEMS.register("billet_th232", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PLUTONIUM = ITEMS.register("billet_plutonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PU238 = ITEMS.register("billet_pu238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PU239 = ITEMS.register("billet_pu239", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PU240 = ITEMS.register("billet_pu240", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PU241 = ITEMS.register("billet_pu241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PU_MIX = ITEMS.register("billet_pu_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AM241 = ITEMS.register("billet_am241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AM242 = ITEMS.register("billet_am242", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AM_MIX = ITEMS.register("billet_am_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_NEPTUNIUM = ITEMS.register("billet_neptunium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PO210 = ITEMS.register("billet_po210", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_TC99 = ITEMS.register("billet_tc99", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_COBALT = ITEMS.register("billet_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_CO60 = ITEMS.register("billet_co60", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_SR90 = ITEMS.register("billet_sr90", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AU198 = ITEMS.register("billet_au198", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PB209 = ITEMS.register("billet_pb209", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_RA226 = ITEMS.register("billet_ra226", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AC227 = ITEMS.register("billet_ac227", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_SCHRABIDIUM = ITEMS.register("billet_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_SOLINIUM = ITEMS.register("billet_solinium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_GH336 = ITEMS.register("billet_gh336", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AUSTRALIUM = ITEMS.register("billet_australium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AUSTRALIUM_LESSER = ITEMS.register("billet_australium_lesser", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AUSTRALIUM_GREATER = ITEMS.register("billet_australium_greater", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_URANIUM_FUEL = ITEMS.register("billet_uranium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_THORIUM_FUEL = ITEMS.register("billet_thorium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PLUTONIUM_FUEL = ITEMS.register("billet_plutonium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_NEPTUNIUM_FUEL = ITEMS.register("billet_neptunium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_MOX_FUEL = ITEMS.register("billet_mox_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_AMERICIUM_FUEL = ITEMS.register("billet_americium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_LES = ITEMS.register("billet_les", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_SCHRABIDIUM_FUEL = ITEMS.register("billet_schrabidium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_HES = ITEMS.register("billet_hes", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PO210BE = ITEMS.register("billet_po210be", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_RA226BE = ITEMS.register("billet_ra226be", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_PU238BE = ITEMS.register("billet_pu238be", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_BERYLLIUM = ITEMS.register("billet_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_BISMUTH = ITEMS.register("billet_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_SILICON = ITEMS.register("billet_silicon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_ZIRCONIUM = ITEMS.register("billet_zirconium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_ZFB_BISMUTH = ITEMS.register("billet_zfb_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_ZFB_PU241 = ITEMS.register("billet_zfb_pu241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_ZFB_AM_MIX = ITEMS.register("billet_zfb_am_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_YHARONITE = ITEMS.register("billet_yharonite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_BALEFIRE_GOLD = ITEMS.register("billet_balefire_gold", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_FLASHLEAD = ITEMS.register("billet_flashlead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BILLET_NUCLEAR_WASTE = ITEMS.register("billet_nuclear_waste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CINNABAR = ITEMS.register("cinnabar", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MERCURY_NUGGET_TINY = ITEMS.register("mercury_nugget_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MERCURY_NUGGET = ITEMS.register("mercury_nugget", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MERCURY_BOTTLE = ITEMS.register("mercury_bottle", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COKE_COAL = ITEMS.register("coke_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COKE_LIGNITE = ITEMS.register("coke_lignite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COKE_PETROLEUM = ITEMS.register("coke_petroleum", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGNITE = ITEMS.register("lignite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COAL_INFERNAL = ITEMS.register("coal_infernal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRIQUETTE_COAL = ITEMS.register("briquette_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRIQUETTE_LIGNITE = ITEMS.register("briquette_lignite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRIQUETTE_WOOD = ITEMS.register("briquette_wood", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SULFUR = ITEMS.register("sulfur", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NITER = ITEMS.register("niter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NITRA = ITEMS.register("nitra", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NITRA_SMALL = ITEMS.register("nitra_small", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUORITE = ITEMS.register("fluorite", () -> new Item(new Item.Properties()));

    // Powders
    public static final DeferredItem<Item> POWDER_COAL = ITEMS.register("powder_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COAL_TINY = ITEMS.register("powder_coal_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_IRON = ITEMS.register("powder_iron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_GOLD = ITEMS.register("powder_gold", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LAPIS = ITEMS.register("powder_lapis", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_QUARTZ = ITEMS.register("powder_quartz", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_DIAMOND = ITEMS.register("powder_diamond", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_EMERALD = ITEMS.register("powder_emerald", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_URANIUM = ITEMS.register("powder_uranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_PLUTONIUM = ITEMS.register("powder_plutonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_NEPTUNIUM = ITEMS.register("powder_neptunium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_PO210 = ITEMS.register("powder_po210", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CO60 = ITEMS.register("powder_co60", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SR90 = ITEMS.register("powder_sr90", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SR90_TINY = ITEMS.register("powder_sr90_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_I131 = ITEMS.register("powder_i131", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_I131_TINY = ITEMS.register("powder_i131_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_XE135 = ITEMS.register("powder_xe135", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_XE135_TINY = ITEMS.register("powder_xe135_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CS137 = ITEMS.register("powder_cs137", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CS137_TINY = ITEMS.register("powder_cs137_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_AU198 = ITEMS.register("powder_au198", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_RA226 = ITEMS.register("powder_ra226", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_AT209 = ITEMS.register("powder_at209", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_TITANIUM = ITEMS.register("powder_titanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COPPER = ITEMS.register("powder_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_RED_COPPER = ITEMS.register("powder_red_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_TUNGSTEN = ITEMS.register("powder_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ALUMINIUM = ITEMS.register("powder_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_STEEL = ITEMS.register("powder_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_STEEL_TINY = ITEMS.register("powder_steel_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_TECHNETIUM = ITEMS.register("powder_technetium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LEAD = ITEMS.register("powder_lead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BISMUTH = ITEMS.register("powder_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CALCIUM = ITEMS.register("powder_calcium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CADMIUM = ITEMS.register("powder_cadmium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COLTAN_ORE = ITEMS.register("powder_coltan_ore", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COLTAN = ITEMS.register("powder_coltan", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_TANTALIUM = ITEMS.register("powder_tantalium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_TEKTITE = ITEMS.register("powder_tektite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_PALEOGENITE = ITEMS.register("powder_paleogenite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_PALEOGENITE_TINY = ITEMS.register("powder_paleogenite_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_IMPURE_OSMIRIDIUM = ITEMS.register("powder_impure_osmiridium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BORAX = ITEMS.register("powder_borax", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CHLOROCALCITE = ITEMS.register("powder_chlorocalcite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_MOLYSITE = ITEMS.register("powder_molysite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_YELLOWCAKE = ITEMS.register("powder_yellowcake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BERYLLIUM = ITEMS.register("powder_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_DURA_STEEL = ITEMS.register("powder_dura_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_POLYMER = ITEMS.register("powder_polymer", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BAKELITE = ITEMS.register("powder_bakelite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SCHRABIDIUM = ITEMS.register("powder_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SCHRABIDATE = ITEMS.register("powder_schrabidate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_MAGNETIZED_TUNGSTEN = ITEMS.register("powder_magnetized_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CHLOROPHYTE = ITEMS.register("powder_chlorophyte", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COMBINE_STEEL = ITEMS.register("powder_combine_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LITHIUM = ITEMS.register("powder_lithium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LITHIUM_TINY = ITEMS.register("powder_lithium_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ZIRCONIUM = ITEMS.register("powder_zirconium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SODIUM = ITEMS.register("powder_sodium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LIGNITE = ITEMS.register("powder_lignite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_IODINE = ITEMS.register("powder_iodine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_THORIUM = ITEMS.register("powder_thorium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_NEODYMIUM = ITEMS.register("powder_neodymium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_NEODYMIUM_TINY = ITEMS.register("powder_neodymium_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASTATINE = ITEMS.register("powder_astatine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CAESIUM = ITEMS.register("powder_caesium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_AUSTRALIUM = ITEMS.register("powder_australium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_STRONTIUM = ITEMS.register("powder_strontium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COBALT = ITEMS.register("powder_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_COBALT_TINY = ITEMS.register("powder_cobalt_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BROMINE = ITEMS.register("powder_bromine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_NIOBIUM = ITEMS.register("powder_niobium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_NIOBIUM_TINY = ITEMS.register("powder_niobium_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_TENNESSINE = ITEMS.register("powder_tennessine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CERIUM = ITEMS.register("powder_cerium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CERIUM_TINY = ITEMS.register("powder_cerium_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LANTHANIUM = ITEMS.register("powder_lanthanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LANTHANIUM_TINY = ITEMS.register("powder_lanthanium_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ACTINIUM = ITEMS.register("powder_actinium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ACTINIUM_TINY = ITEMS.register("powder_actinium_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BORON = ITEMS.register("powder_boron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BORON_TINY = ITEMS.register("powder_boron_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASBESTOS = ITEMS.register("powder_asbestos", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_MAGIC = ITEMS.register("powder_magic", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SAWDUST = ITEMS.register("powder_sawdust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEAR_LARGE = ITEMS.register("gear_large", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GEAR_LARGE_STEEL = ITEMS.register("gear_large_steel", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SAWBLADE = ITEMS.register("sawblade", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> POWDER_FLUX = ITEMS.register("powder_flux", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_FERTILIZER = ITEMS.register("powder_fertilizer", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_BALEFIRE = ITEMS.register("powder_balefire", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SEMTEX_MIX = ITEMS.register("powder_semtex_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_DESH_MIX = ITEMS.register("powder_desh_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_DESH_READY = ITEMS.register("powder_desh_ready", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_DESH = ITEMS.register("powder_desh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_NITAN_MIX = ITEMS.register("powder_nitan_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_SPARK_MIX = ITEMS.register("powder_spark_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_METEORITE = ITEMS.register("powder_meteorite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_METEORITE_TINY = ITEMS.register("powder_meteorite_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_EUPHEMIUM = ITEMS.register("powder_euphemium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_DINEUTRONIUM = ITEMS.register("powder_dineutronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DUST = ITEMS.register("dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DUST_TINY = ITEMS.register("dust_tiny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DUST_FALLOUT = ITEMS.register("dust_fallout", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASH_WOOD = ITEMS.register("powder_ash_wood", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASH_COAL = ITEMS.register("powder_ash_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASH = ITEMS.register("powder_ash", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASH_FLY = ITEMS.register("powder_ash_fly", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASH_SOOT = ITEMS.register("powder_ash_soot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ASH_FULLERENE = ITEMS.register("powder_ash_fullerene", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_LIMESTONE = ITEMS.register("powder_limestone", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_CEMENT = ITEMS.register("powder_cement", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_RED_PHOSPHOROUS = ITEMS.register("powder_red_phosphorus", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ICE = ITEMS.register("powder_ice", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_POISON = ITEMS.register("powder_poison", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_THERMITE = ITEMS.register("powder_thermite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDER_ENERGY = ITEMS.register("powder_energy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CORDITE  = ITEMS.register("cordite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BALLISTITE = ITEMS.register("ballistite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BALL_DYNAMITE  = ITEMS.register("ball_dynamite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BALL_TNT = ITEMS.register("ball_tnt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BALL_TATB = ITEMS.register("ball_tatb", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BALL_RESIN = ITEMS.register("ball_resin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BALL_FIRECLAY = ITEMS.register("ball_fireclay", () -> new Item(new Item.Properties()));

    // Crystals
    public static final DeferredItem<Item> CRYSTAL_COAL = ITEMS.register("crystal_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_IRON = ITEMS.register("crystal_iron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_GOLD = ITEMS.register("crystal_gold", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_REDSTONE = ITEMS.register("crystal_redstone", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_LAPIS = ITEMS.register("crystal_lapis", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_DIAMOND = ITEMS.register("crystal_diamond", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_URANIUM = ITEMS.register("crystal_uranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_THORIUM = ITEMS.register("crystal_thorium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_PLUTONIUM = ITEMS.register("crystal_plutonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_TITANIUM = ITEMS.register("crystal_titanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_SULFUR = ITEMS.register("crystal_sulfur", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_NITER = ITEMS.register("crystal_niter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_COPPER = ITEMS.register("crystal_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_TUNGSTEN = ITEMS.register("crystal_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_ALUMINIUM = ITEMS.register("crystal_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_FLUORITE = ITEMS.register("crystal_fluorite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_BERYLLIUM = ITEMS.register("crystal_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_LEAD = ITEMS.register("crystal_lead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_SCHRARANIUM = ITEMS.register("crystal_schraranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_SCHRABIDIUM = ITEMS.register("crystal_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_RARE = ITEMS.register("crystal_rare", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_PHOSPHORUS = ITEMS.register("crystal_phosphorus", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_LITHIUM = ITEMS.register("crystal_lithium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_COBALT = ITEMS.register("crystal_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_STARMETAL = ITEMS.register("crystal_starmetal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_CINNABAR = ITEMS.register("crystal_cinnabar", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_TRIXITE = ITEMS.register("crystal_trixite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTAL_OSMIRIDIUM = ITEMS.register("crystal_osmiridium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEM_SODALITE  = ITEMS.register("gem_sodalite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEM_TANTALIUM  = ITEMS.register("gem_tantalium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEM_VOLCANIC  = ITEMS.register("gem_volcanic", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEM_RAD  = ITEMS.register("gem_rad", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEM_ALEXANDRITE  = ITEMS.register("gem_alexandrite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_NEODYMIUM  = ITEMS.register("fragment_neodymium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_COBALT  = ITEMS.register("fragment_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_NIOBIUM  = ITEMS.register("fragment_niobium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_CERIUM  = ITEMS.register("fragment_cerium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_LANTHANIUM  = ITEMS.register("fragment_lanthanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_AC227  = ITEMS.register("fragment_ac227", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_BORON  = ITEMS.register("fragment_boron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_METEORITE  = ITEMS.register("fragment_meteorite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRAGMENT_COLTAN  = ITEMS.register("fragment_coltan", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHUNK_RARE = ITEMS.register("chunk_rare", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHUNK_MALACHITE = ITEMS.register("chunk_malachite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHUNK_CRYOLITE = ITEMS.register("chunk_cryolite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MOONSTONE = ITEMS.register("moonstone", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BIOMASS  = ITEMS.register("biomass", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BIOMASS_COMPRESSED  = ITEMS.register("biomass_compressed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BIO_WAFER  = ITEMS.register("bio_wafer", () -> new Item(new Item.Properties()));

    // Nuggets
    public static final DeferredItem<Item> NUGGET_URANIUM = ITEMS.register("nugget_uranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_U233 = ITEMS.register("nugget_u233", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_U235 = ITEMS.register("nugget_u235", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_U238 = ITEMS.register("nugget_u238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_TH232 = ITEMS.register("nugget_th232", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PLUTONIUM = ITEMS.register("nugget_plutonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PU238 = ITEMS.register("nugget_pu238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PU239 = ITEMS.register("nugget_pu239", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PU240 = ITEMS.register("nugget_pu240", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PU241 = ITEMS.register("nugget_pu241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PU_MIX = ITEMS.register("nugget_pu_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AM241 = ITEMS.register("nugget_am241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AM242 = ITEMS.register("nugget_am242", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AM_MIX = ITEMS.register("nugget_am_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_NEPTUNIUM = ITEMS.register("nugget_neptunium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PO210 = ITEMS.register("nugget_po210", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_COBALT = ITEMS.register("nugget_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_CO60 = ITEMS.register("nugget_co60", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_SR90 = ITEMS.register("nugget_sr90", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_TC99 = ITEMS.register("nugget_tc99", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AU198 = ITEMS.register("nugget_au198", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PB209 = ITEMS.register("nugget_pb209", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_RA226 = ITEMS.register("nugget_ra226", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AC227 = ITEMS.register("nugget_ac227", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_LEAD = ITEMS.register("nugget_lead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_BISMUTH = ITEMS.register("nugget_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_ARSENIC = ITEMS.register("nugget_arsenic", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_TANTALIUM = ITEMS.register("nugget_tantalium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_SILICON = ITEMS.register("nugget_silicon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_NIOBIUM = ITEMS.register("nugget_niobium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_BERYLLIUM = ITEMS.register("nugget_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_SCHRABIDIUM = ITEMS.register("nugget_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_SOLINIUM = ITEMS.register("nugget_solinium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_GH336 = ITEMS.register("nugget_gh336", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_URANIUM_FUEL = ITEMS.register("nugget_uranium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_THORIUM_FUEL = ITEMS.register("nugget_thorium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_PLUTONIUM_FUEL = ITEMS.register("nugget_plutonium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_NEPTUNIUM_FUEL = ITEMS.register("nugget_neptunium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_MOX_FUEL = ITEMS.register("nugget_mox_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AMERICIUM_FUEL = ITEMS.register("nugget_americium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_SCHRABIDIUM_FUEL = ITEMS.register("nugget_schrabidium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_HES = ITEMS.register("nugget_hes", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_LES = ITEMS.register("nugget_les", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_ZIRCONIUM = ITEMS.register("nugget_zirconium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AUSTRALIUM = ITEMS.register("nugget_australium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AUSTRALIUM_LESSER = ITEMS.register("nugget_australium_lesser", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_AUSTRALIUM_GREATER = ITEMS.register("nugget_australium_greater", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_DESH = ITEMS.register("nugget_desh", () -> new Item(new Item.Properties()));;
    public static final DeferredItem<Item> NUGGET_EUPHEMIUM = ITEMS.register("nugget_euphemium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_DINEUTRONIUM = ITEMS.register("nugget_dineutronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NUGGET_OSMIRIDIUM = ITEMS.register("nugget_osmiridium", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NEUTRON_REFLECTOR = ITEMS.register("neutron_reflector", () -> new Item(new Item.Properties()));

    // Plates
    public static final DeferredItem<Item> PLATE_IRON = ITEMS.register("plate_iron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_GOLD = ITEMS.register("plate_gold", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_TITANIUM = ITEMS.register("plate_titanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ALUMINIUM = ITEMS.register("plate_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_STEEL = ITEMS.register("plate_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_LEAD = ITEMS.register("plate_lead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_COPPER = ITEMS.register("plate_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_DURA_STEEL = ITEMS.register("plate_dura_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_SCHRABIDIUM = ITEMS.register("plate_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_COMBINE_STEEL = ITEMS.register("plate_combine_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_MIXED = ITEMS.register("plate_mixed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_GUNMETAL = ITEMS.register("plate_gunmetal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_WEAPONSTEEL = ITEMS.register("plate_weaponsteel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_SATURNITE = ITEMS.register("plate_saturnite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_PAA = ITEMS.register("plate_paa", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_POLYMER = ITEMS.register("plate_polymer", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_KEVLAR = ITEMS.register("plate_kevlar", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_DALEKANIUM = ITEMS.register("plate_dalekanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_DESH = ITEMS.register("plate_desh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_BISMUTH = ITEMS.register("plate_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_EUPHEMIUM = ITEMS.register("plate_euphemium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_DINEUTRONIUM = ITEMS.register("plate_dineutronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ARMOR_TITANIUM = ITEMS.register("plate_armor_titanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ARMOR_AJR = ITEMS.register("plate_armor_ajr", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ARMOR_HEV = ITEMS.register("plate_armor_hev", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ARMOR_LUNAR = ITEMS.register("plate_armor_lunar", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ARMOR_FAU = ITEMS.register("plate_armor_fau", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLATE_ARMOR_DNT = ITEMS.register("plate_armor_dnt", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SCRAP = ITEMS.register("scrap", () -> new Item(new Item.Properties()));

    // Pellets
    public static final DeferredItem<Item> PELLET_RTG = ITEMS.register("pellet_rtg", () -> new Item(new Item.Properties()));

    // Cells
    public static final DeferredItem<Item> CELL_EMPTY = ITEMS.register("cell_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CELL_UF6 = ITEMS.register("cell_uf6", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CELL_PUF6 = ITEMS.register("cell_puf6", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CELL_ANTIMATTER = ITEMS.register("cell_antimatter", () -> new DangerousDropItem(new Item.Properties()));
    public static final DeferredItem<Item> CELL_DEUTERIUM = ITEMS.register("cell_deuterium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CELL_TRITIUM = ITEMS.register("cell_tritium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CELL_SAS3 = ITEMS.register("cell_sas3", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> CELL_ANTI_SCHARBIDIUM = ITEMS.register("cell_anti_schrabidium", () -> new DangerousDropItem(new Item.Properties()));
    public static final DeferredItem<Item> CELL_BALEFIRE = ITEMS.register("cell_balefire", () -> new Item(new Item.Properties()));

    // Particle Containers
    public static final DeferredItem<Item> PARTICLE_DIGAMMA = ITEMS.register("particle_digamma", () -> new DangerousDropItem(new Item.Properties()));
    public static final DeferredItem<Item> PARTICLE_LUTECE = ITEMS.register("particle_lutece", () -> new Item(new Item.Properties()));

    // Singularities, black holes and other cosmic horrors
    public static final DeferredItem<Item> SINGULARITY = ITEMS.register("singularity", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SINGULARITY_COUNTER_RESONANT = ITEMS.register("singularity_counter_resonant", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SINGULARITY_SUPER_HEATED = ITEMS.register("singularity_super_heated", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BLACK_HOLE = ITEMS.register("black_hole", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SINGULARITY_SPARK = ITEMS.register("singularity_spark", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    // todo crystal_xen
    public static final DeferredItem<Item> PELLET_ANTIMATTER = ITEMS.register("pellet_antimatter", () -> new DangerousDropItem(new Item.Properties()));

    // Infinite Tanks
    public static final DeferredItem<Item> INF_WATER = ITEMS.register("inf_water", () -> new InfiniteFluidItem(new Item.Properties().stacksTo(1), Fluids.WATER, 50));
    public static final DeferredItem<Item> INF_WATER_MK2 = ITEMS.register("inf_water_mk2", () -> new InfiniteFluidItem(new Item.Properties().stacksTo(1), Fluids.WATER, 500));

    // Universal Tank
    public static final DeferredItem<Item> FLUID_TANK_EMPTY = ITEMS.register("fluid_tank_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_FULL = ITEMS.register("fluid_tank_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_LEAD_EMPTY = ITEMS.register("fluid_tank_lead_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_LEAD_FULL = ITEMS.register("fluid_tank_lead_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_EMPTY = ITEMS.register("fluid_barrel_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_FULL = ITEMS.register("fluid_barrel_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_INFINITE = ITEMS.register("fluid_barrel_infinite", () -> new InfiniteFluidItem(new Item.Properties().stacksTo(1), null, 1_000_000_000));

    // Packaged fluids
    public static final DeferredItem<Item> FLUID_PACK_EMPTY = ITEMS.register("fluid_pack_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_PACK_FULL = ITEMS.register("fluid_pack_full", () -> new FluidTankItem(new Item.Properties()));

    // Batteries
    public static final DeferredItem<Item> BATTERY_SPARK = ITEMS.register("battery_spark", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BATTERY_TRIXITE = ITEMS.register("battery_trixite", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BATTERY_PACK = ITEMS.register("battery_pack", () -> new BatteryPackItem(new Item.Properties()));
    public static final DeferredItem<Item> BATTERY_SC = ITEMS.register("battery_sc", () -> new BatterySCItem(new Item.Properties()));
    public static final DeferredItem<Item> BATTERY_CREATIVE = ITEMS.register("battery_creative", () -> new BatteryCreativeItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<ItemFELCrystal> LASER_CRYSTAL_CO2 = ITEMS.register("laser_crystal_co2", () -> new ItemFELCrystal(new Item.Properties(), ItemFELCrystal.EnumWavelengths.IR));
    public static final DeferredItem<ItemFELCrystal> LASER_CRYSTAL_BISMUTH = ITEMS.register("laser_crystal_bismuth", () -> new ItemFELCrystal(new Item.Properties(), ItemFELCrystal.EnumWavelengths.VISIBLE));
    public static final DeferredItem<ItemFELCrystal> LASER_CRYSTAL_CMB = ITEMS.register("laser_crystal_cmb", () -> new ItemFELCrystal(new Item.Properties(), ItemFELCrystal.EnumWavelengths.UV));
    public static final DeferredItem<ItemFELCrystal> LASER_CRYSTAL_DNT = ITEMS.register("laser_crystal_dnt", () -> new ItemFELCrystal(new Item.Properties(), ItemFELCrystal.EnumWavelengths.GAMMA));
    public static final DeferredItem<ItemFELCrystal> LASER_CRYSTAL_DIGAMMA = ITEMS.register("laser_crystal_digamma", () -> new ItemFELCrystal(new Item.Properties(), ItemFELCrystal.EnumWavelengths.DRX));

    // Folders
    public static final DeferredItem<Item> BLUEPRINTS = ITEMS.register("blueprints", () -> new BlueprintsItem(new Item.Properties()));

    // Machine Templates
    public static final DeferredItem<FluidIconItem> FLUID_ICON = ITEMS.register("fluid_icon", () -> new FluidIconItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_IDENTIFIER_MULTI = ITEMS.register("fluid_identifier_multi", () -> new FluidIDMultiItem(new Item.Properties()));

    // Machine Items
    //by using these in crafting table recipes, i'm running the risk of making my recipes too greg-ian (which i don't like)
    //in the event that i forget about the meaning of the word "sparingly", please throw a brick at my head
    public static final DeferredItem<Item> SCREWDRIVER = ITEMS.register("screwdriver", () -> new ToolingItem(ToolType.SCREWDRIVER, new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> SCREWDRIVER_DESH = ITEMS.register("screwdriver_desh", () -> new ToolingItem(ToolType.SCREWDRIVER, new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> BLOWTORCH = ITEMS.register("blowtorch", () -> new BlowtorchItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ACETYLENE_TORCH = ITEMS.register("acetylene_torch", () -> new BlowtorchItem(new Item.Properties().stacksTo(1)));

    // Press Stamps
    // TODO durability
    public static final DeferredItem<Item> STAMP_FLAT_STONE = ITEMS.register("stamp_stone_flat", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_PLATE_STONE = ITEMS.register("stamp_stone_plate", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_WIRE_STONE = ITEMS.register("stamp_stone_wire", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_CIRCUIT_STONE = ITEMS.register("stamp_stone_circuit", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_FLAT_IRON = ITEMS.register("stamp_iron_flat", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_PLATE_IRON = ITEMS.register("stamp_iron_plate", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_WIRE_IRON = ITEMS.register("stamp_iron_wire", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_CIRCUIT_IRON = ITEMS.register("stamp_iron_circuit", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_FLAT_STEEL = ITEMS.register("stamp_steel_flat", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_PLATE_STEEL = ITEMS.register("stamp_steel_plate", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_WIRE_STEEL = ITEMS.register("stamp_steel_wire", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_CIRCUIT_STEEL = ITEMS.register("stamp_steel_circuit", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_FLAT_TITANIUM = ITEMS.register("stamp_titanium_flat", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_PLATE_TITANIUM = ITEMS.register("stamp_titanium_plate", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_WIRE_TITANIUM = ITEMS.register("stamp_titanium_wire", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_CIRCUIT_TITANIUM = ITEMS.register("stamp_titanium_circuit", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_FLAT_OBSIDIAN = ITEMS.register("stamp_obsidian_flat", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_PLATE_OBSIDIAN = ITEMS.register("stamp_obsidian_plate", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_WIRE_OBSIDIAN = ITEMS.register("stamp_obsidian_wire", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_CIRCUIT_OBSIDIAN = ITEMS.register("stamp_obsidian_circuit", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_FLAT_DESH = ITEMS.register("stamp_desh_flat", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_PLATE_DESH = ITEMS.register("stamp_desh_plate", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_WIRE_DESH = ITEMS.register("stamp_desh_wire", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_CIRCUIT_DESH = ITEMS.register("stamp_desh_circuit", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_IRON_357 = ITEMS.register("stamp_iron_357", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_IRON_44 = ITEMS.register("stamp_iron_44", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_IRON_9 = ITEMS.register("stamp_iron_9", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_IRON_50 = ITEMS.register("stamp_iron_50", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));
    public static final DeferredItem<Item> STAMP_DESH_357 = ITEMS.register("stamp_desh_357", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.FLAT));
    public static final DeferredItem<Item> STAMP_DESH_44 = ITEMS.register("stamp_desh_44", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.PLATE));
    public static final DeferredItem<Item> STAMP_DESH_9 = ITEMS.register("stamp_desh_9", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.WIRE));
    public static final DeferredItem<Item> STAMP_DESH_50 = ITEMS.register("stamp_desh_50", () -> new ItemStamp(new Item.Properties().durability(250), ItemStamp.StampType.CIRCUIT));

    // Shredder Blades
    // TODO durability
    public static final DeferredItem<Item> BLADES_IRON = ITEMS.register("blades_iron", () -> new ItemBlades(new Item.Properties().stacksTo(1), 256));
    public static final DeferredItem<Item> BLADES_STEEL = ITEMS.register("blades_steel", () -> new ItemBlades(new Item.Properties().stacksTo(1), 1024));
    public static final DeferredItem<Item> BLADES_DESH = ITEMS.register("blades_desh", () -> new ItemBlades(new Item.Properties().stacksTo(1), 4096));

    // Random Stuff
    public static final DeferredItem<Item> CATALYTIC_CONVERTER = ITEMS.register("catalytic_converter", () -> new Item(new Item.Properties().stacksTo(1)));

    // Breeding Rods
    public static final DeferredItem<Item> ROD_EMPTY = ITEMS.register("rod_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROD = ITEMS.register("rod", () -> new BreedingRodItem(new Item.Properties()));
    public static final DeferredItem<Item> ROD_DUAL_EMPTY = ITEMS.register("rod_dual_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROD_DUAL = ITEMS.register("rod_dual", () -> new BreedingRodItem(new Item.Properties()));
    public static final DeferredItem<Item> ROD_QUAD_EMPTY = ITEMS.register("rod_quad_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROD_QUAD = ITEMS.register("rod_quad", () -> new BreedingRodItem(new Item.Properties()));

    // Spawners
    public static final DeferredItem<Item> SPAWN_DUCK = ITEMS.register("spawn_duck", () -> new EntitySpawnerItem(new Item.Properties().stacksTo(16)));

    // Computer Tools
    public static final DeferredItem<Item> DESIGNATOR = ITEMS.register("designator", () -> new DesignatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DESIGNATOR_RANGE = ITEMS.register("designator_range", () -> new DesignatorRangeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOSIMETER = ITEMS.register("dosimeter", () -> new DosimeterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter", () -> new GeigerCounterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DIGAMMA_DIAGNOSTIC = ITEMS.register("digamma_diagnostic", () -> new DigammaDiagnosticItem(new Item.Properties().stacksTo(1)));

    // Keys and Locks
    public static final DeferredItem<Item> PIN = ITEMS.register("pin", () -> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> KEY = ITEMS.register("key", () -> new KeyItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_RED = ITEMS.register("key_red", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_RED_CRACKED = ITEMS.register("key_red_cracked", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_KIT = ITEMS.register("key_kit", () -> new KeyItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_FAKE = ITEMS.register("key_fake", () -> new KeyItem(new Item.Properties().stacksTo(1)));

    // Missiles
    // Tier 0
    public static final DeferredItem<Item> MISSILE_TAINT =       ITEMS.register("missile_taint",       () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_MICRO =       ITEMS.register("missile_micro",       () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_BHOLE =       ITEMS.register("missile_bhole",       () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_SCHRABIDIUM = ITEMS.register("missile_schrabidium", () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_EMP =         ITEMS.register("missile_emp",         () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    // Tier 1
    public static final DeferredItem<Item> MISSILE_GENERIC =    ITEMS.register("missile_generic",    () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_INCENDIARY = ITEMS.register("missile_incendiary", () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_CLUSTER =    ITEMS.register("missile_cluster",    () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_BUSTER =     ITEMS.register("missile_buster",     () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_STEALTH =    ITEMS.register("missile_stealth",    () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_DECOY =      ITEMS.register("missile_decoy",      () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    // Tier 2
    public static final DeferredItem<Item> MISSILE_STRONG =            ITEMS.register("missile_strong",            () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_INCENDIARY_STRONG = ITEMS.register("missile_incendiary_strong", () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_CLUSTER_STRONG =    ITEMS.register("missile_cluster_strong",    () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_BUSTER_STRONG =     ITEMS.register("missile_buster_strong",     () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_EMP_STRONG =        ITEMS.register("missile_emp_strong",        () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    // Tier 3
    public static final DeferredItem<Item> MISSILE_BURST =   ITEMS.register("missile_burst",   () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_INFERNO = ITEMS.register("missile_inferno", () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_RAIN =    ITEMS.register("missile_rain",    () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_DRILL =   ITEMS.register("missile_drill",   () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_SHUTTLE = ITEMS.register("missile_shuttle", () -> new MissileItem(MissileFormFactor.OTHER, MissileTier.TIER3, MissileFuel.KEROSENE_PEROXIDE));
    // Tier 4
    public static final DeferredItem<Item> MISSILE_NUCLEAR =         ITEMS.register("missile_nuclear",         () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_NUCLEAR_CLUSTER = ITEMS.register("missile_nuclear_cluster", () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_VOLCANO =         ITEMS.register("missile_volcano",         () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_DOOMSDAY =        ITEMS.register("missile_doomsday",        () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_DOOMSDAY_RUSTED = ITEMS.register("missile_doomsday_rusted", () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4).notLaunchable());
    // Rockets
    public static final DeferredItem<Item> MISSILE_SOYUZ = ITEMS.register("missile_soyuz", () -> new SoyuzItem(new Item.Properties()));

    // Satellites
    public static final DeferredItem<Item> SATELLITE_RADAR = ITEMS.register("satellite_radar", () -> new SatChipItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SATELLITE_LASER = ITEMS.register("satellite_laser", () -> new SatChipItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SATELLITE_INTERFACE = ITEMS.register("satellite_interface", () -> new SatelliteInterfaceItem(new Item.Properties().stacksTo(1)));

    // Tools
    public static final DeferredItem<Item> BALEFIRE_AND_STEEL = ITEMS.register("balefire_and_steel", () -> new BalefireAndSteelItem(new Item.Properties().stacksTo(1).durability(256)));
    public static final DeferredItem<Item> WIRING_RED_COPPER = ITEMS.register("wiring_red_copper", () -> new CableDrumItem(new Item.Properties().stacksTo(1)));

    // Energy Drinks
    public static final DeferredItem<Item> DRINK = ITEMS.register("drink", () -> new DrinkItem(new Item.Properties()));
    public static final DeferredItem<Item> BOTTLE_OPENER = ITEMS.register(
            "bottle_opener",
            () -> new SpecialSwordItem(
                    NtmTiers.BOTTLE_OPENER,
                    new Item.Properties()
                            .stacksTo(1)
                            .attributes(SwordItem.createAttributes(NtmTiers.BOTTLE_OPENER, 3, -2.4F))
            ).setHurtEnemy(SpecialSwordItem.LAMBDA_OPENER_HURT_ENEMY)
    );

    // Canned Food
    public static final DeferredItem<Item> CANNED_CONSERVE = ITEMS.register("canned_conserve", () -> new ConserveItem(new Item.Properties()));

    // Money
    public static final DeferredItem<Item> CAP = ITEMS.register("cap", () -> new EnumMultiItem(new Item.Properties(), CapType.class, true, true));
    public static final DeferredItem<Item> RING_PULL = ITEMS.register("ring_pull", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CAN_KEY = ITEMS.register("can_key", () -> new Item(new Item.Properties()));

    // Chaos
    public static final DeferredItem<Item> CHOCOLATE_MILK = ITEMS.register("chocolate_milk", () -> new EnergyItem(new Item.Properties()));
    public static final DeferredItem<Item> CIGARETTE = ITEMS.register("cigarette", () -> new CigaretteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> CRACKPIPE = ITEMS.register("crackpipe", () -> new CigaretteItem(new Item.Properties().stacksTo(1)));

    // High Explosive Lenses
    public static final DeferredItem<Item> EARLY_EXPLOSIVE_LENSES = ITEMS.register("early_explosive_lenses", () -> new LoreItem(new Item.Properties()));
    public static final DeferredItem<Item> EXPLOSIVE_LENSES = ITEMS.register("explosive_lenses", () -> new LoreItem(new Item.Properties()));

    // The Gadget
    public static final DeferredItem<Item> GADGET_WIREING = ITEMS.register("gadget_wireing", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GADGET_CORE =    ITEMS.register("gadget_core",    () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    // Little Boy
    public static final DeferredItem<Item> LITTLE_BOY_SHIELDING =  ITEMS.register("little_boy_shielding",  () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LITTLE_BOY_TARGET =     ITEMS.register("little_boy_target",     () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_BULLET =     ITEMS.register("little_boy_bullet",     () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_PROPELLANT = ITEMS.register("little_boy_propellant", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LITTLE_BOY_IGNITER =    ITEMS.register("little_boy_igniter",    () -> new Item(new Item.Properties().stacksTo(1)));

    // Fat Man
    public static final DeferredItem<Item> FAT_MAN_IGNITER = ITEMS.register("fat_man_igniter", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FAT_MAN_CORE =    ITEMS.register("fat_man_core",    () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    // Ivy Mike
    public static final DeferredItem<Item> IVY_MIKE_CORE =         ITEMS.register("ivy_mike_core",         () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> IVY_MIKE_DEUT =         ITEMS.register("ivy_mike_deut",         () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> IVY_MIKE_COOLING_UNIT = ITEMS.register("ivy_mike_cooling_unit", () -> new Item(new Item.Properties().stacksTo(1)));

    // Tsar Bomba
    public static final DeferredItem<Item> TSAR_BOMBA_CORE = ITEMS.register("tsar_bomba_core", () -> new Item(new Item.Properties().stacksTo(1)));

    // FLEIJA
    public static final DeferredItem<Item> FLEIJA_IGNITER =    ITEMS.register("fleija_igniter",    () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> FLEIJA_PROPELLANT = ITEMS.register("fleija_propellant", () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> FLEIJA_CORE =       ITEMS.register("fleija_core",       () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_FLEIJA.get())));

    // Solinium
    public static final DeferredItem<Item> SOLINIUM_IGNITER =    ITEMS.register("solinium_igniter",    () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> SOLINIUM_PROPELLANT = ITEMS.register("solinium_propellant", () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> SOLINIUM_CORE =       ITEMS.register("solinium_core",       () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_FLEIJA.get())));

    // N2
    public static final DeferredItem<Item> N2_CHARGE = ITEMS.register("n2_charge", () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(NtmBlocks.NUKE_N2.get())));

    // FSTBMB
    public static final DeferredItem<Item> EGG_BALEFIRE_SHARD = ITEMS.register("egg_balefire_shard", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> EGG_BALEFIRE = ITEMS.register("egg_balefire", () -> new Item(new Item.Properties().stacksTo(1)));

    // Nobody will ever read this anyway, so it shouldn't matter.
    public static final DeferredItem<Item> IGNITER = ITEMS.register("igniter", () -> new LoreItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR = ITEMS.register("detonator", () -> new DetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_MULTI = ITEMS.register("detonator_multi", () -> new MultiDetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_LASER = ITEMS.register("detonator_laser", () -> new LaserDetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DEADMAN = ITEMS.register("detonator_deadman", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DE = ITEMS.register("detonator_de", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BOMB_CALLER = ITEMS.register("bomb_caller", () -> new BombCallerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DEFUSER = ITEMS.register("defuser", () -> new ToolingItem(ToolType.DEFUSER, new Item.Properties().durability(100)));
    public static final DeferredItem<Item> REACHER = ITEMS.register("reacher", () -> new Item(new Item.Properties().stacksTo(1)));

    // Wands, Tools, Other Crap
    public static final DeferredItem<Item> POLAROID = ITEMS.register("polaroid", () -> new PolaroidItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BURNT_BARK = ITEMS.register("burnt_bark", () -> new LoreItem(new Item.Properties()));

    // Kits
    public static final DeferredItem<Item> STARTER_KIT = ITEMS.register("starter_kit", () -> new StarterKitItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> TEMPLATE_FOLDER = ITEMS.register("template_folder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NOTHING = ITEMS.register("nothing", () -> new Item(new Item.Properties()));


    // ...
    public static final DeferredItem<Item> CASING = ITEMS.register("casing", () -> new EnumMultiItem(new Item.Properties(), CasingType.class, true, true));
    public static DeferredItem<Item> AMMO_DEBUG;
    public static DeferredItem<Item> AMMO_STANDARD;
    public static DeferredItem<Item> AMMO_SECRET;

    public static void registerOther(DeferredRegister.Items itemRegistry) {
        MatsItemGen.init(itemRegistry);
        GunFactory.init(itemRegistry);
    }

    public static void register(IEventBus eventBus) {
        registerOther(ITEMS);

        ITEMS.register(eventBus);
    }
}
