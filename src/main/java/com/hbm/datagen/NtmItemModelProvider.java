package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.items.ICustomItemModelRegister;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class NtmItemModelProvider extends ItemModelProvider {

    public NtmItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NuclearTechMod.MODID, existingFileHelper);
    }

    /**
     * True if assets/hbm/textures/item/{itemName}.png exists (a hand-made texture for this
     * specific item). Used to prefer real art over the generic tinted template.
     */
    private boolean hasCustomTexture(String itemName) {
        return existingFileHelper.exists(
                modLoc("item/" + itemName),
                net.minecraft.server.packs.PackType.CLIENT_RESOURCES,
                ".png",
                "textures"
        );
    }

    @Override
    protected void registerModels() {

        NtmItems.ITEMS.getEntries().forEach(holder -> {
            Item item = holder.get();

            if(item instanceof ICustomItemModelRegister icimr) {
                ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
                icimr.registerItemModel(this, loc);
            }
        });

        this.basicItem(NtmItems.INGOT_URANIUM.get());
        this.basicItem(NtmItems.INGOT_U233.get());
        this.basicItem(NtmItems.INGOT_U235.get());
        this.basicItem(NtmItems.INGOT_U238.get());
        this.basicItem(NtmItems.INGOT_U238M2.get());
        this.basicItem(NtmItems.INGOT_TH232.get());
        this.basicItem(NtmItems.INGOT_PLUTONIUM.get());
        this.basicItem(NtmItems.INGOT_PU238.get());
        this.basicItem(NtmItems.INGOT_PU239.get());
        this.basicItem(NtmItems.INGOT_PU240.get());
        this.basicItem(NtmItems.INGOT_PU241.get());
        this.basicItem(NtmItems.INGOT_PU_MIX.get());
        this.basicItem(NtmItems.INGOT_AM241.get());
        this.basicItem(NtmItems.INGOT_AM242.get());
        this.basicItem(NtmItems.INGOT_AM_MIX.get());
        this.basicItem(NtmItems.INGOT_NEPTUNIUM.get());
        this.basicItem(NtmItems.INGOT_PO210.get());
        this.basicItem(NtmItems.INGOT_TC99.get());
        this.basicItem(NtmItems.INGOT_CO60.get());
        this.basicItem(NtmItems.INGOT_SR90.get());
        this.basicItem(NtmItems.INGOT_AU198.get());
        this.basicItem(NtmItems.INGOT_PB209.get());
        this.basicItem(NtmItems.INGOT_RA226.get());
        this.basicItem(NtmItems.INGOT_TITANIUM.get());
        this.basicItem(NtmItems.INGOT_INDUSTRIAL_COPPER.get());
        this.basicItem(NtmItems.INGOT_RED_COPPER.get());
        this.basicItem(NtmItems.INGOT_TUNGSTEN.get());
        this.basicItem(NtmItems.INGOT_TUNGSTEN_CARBIDE.get());
        this.basicItem(NtmItems.INGOT_ALUMINIUM.get());
        this.basicItem(NtmItems.INGOT_STEEL.get());
        this.basicItem(NtmItems.INGOT_TCALLOY.get());
        this.basicItem(NtmItems.INGOT_CDALLOY.get());
        this.basicItem(NtmItems.INGOT_BISMUTH_BRONZE.get());
        this.basicItem(NtmItems.INGOT_ARSENIC_BRONZE.get());
        this.basicItem(NtmItems.INGOT_BSCCO.get());
        this.basicItem(NtmItems.INGOT_LEAD.get());
        this.basicItem(NtmItems.INGOT_BISMUTH.get());
        this.basicItem(NtmItems.INGOT_ARSENIC.get());
        this.basicItem(NtmItems.INGOT_CALCIUM.get());
        this.basicItem(NtmItems.INGOT_CADMIUM.get());
        this.basicItem(NtmItems.INGOT_TANTALIUM.get());
        this.basicItem(NtmItems.INGOT_SILICON.get());
        this.basicItem(NtmItems.INGOT_NIOBIUM.get());
        this.basicItem(NtmItems.INGOT_BERYLLIUM.get());
        this.basicItem(NtmItems.INGOT_COBALT.get());
        this.basicItem(NtmItems.INGOT_BORON.get());
        this.basicItem(NtmItems.INGOT_GRAPHITE.get());
        this.basicItem(NtmItems.INGOT_FIREBRICK.get());
        this.basicItem(NtmItems.INGOT_DURA_STEEL.get());
        this.basicItem(NtmItems.INGOT_POLYMER.get());
        this.basicItem(NtmItems.INGOT_BAKELITE.get());
        this.basicItem(NtmItems.INGOT_BIORUBBER.get());
        this.basicItem(NtmItems.INGOT_RUBBER.get());
        this.basicItem(NtmItems.INGOT_PC.get());
        this.basicItem(NtmItems.INGOT_PVC.get());
        this.basicItem(NtmItems.INGOT_MUD.get());
        this.basicItem(NtmItems.INGOT_CTF.get());
        this.basicItem(NtmItems.INGOT_SCHRARANIUM.get());
        this.basicItem(NtmItems.INGOT_SCHRABIDIUM.get());
        this.basicItem(NtmItems.INGOT_SCHRABIDATE.get());
        this.basicItem(NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get());
        this.basicItem(NtmItems.INGOT_COMBINE_STEEL.get());
        this.basicItem(NtmItems.INGOT_SOLINIUM.get());
        this.basicItem(NtmItems.INGOT_GH336.get());
        this.basicItem(NtmItems.INGOT_URANIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_THORIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_PLUTONIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_NEPTUNIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_MOX_FUEL.get());
        this.basicItem(NtmItems.INGOT_AMERICIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_HES.get());
        this.basicItem(NtmItems.INGOT_LES.get());
        this.basicItem(NtmItems.INGOT_AUSTRALIUM.get());
        this.basicItem(NtmItems.INGOT_LANTHANIUM.get());
        this.basicItem(NtmItems.INGOT_AC227.get());
        this.basicItem(NtmItems.INGOT_DESH.get());
        this.basicItem(NtmItems.INGOT_FERROURANIUM.get());
        this.basicItem(NtmItems.INGOT_STARMETAL.get());
        this.basicItem(NtmItems.INGOT_GUNMETAL.get());
        this.basicItem(NtmItems.INGOT_WEAPONSTEEL.get());
        this.basicItem(NtmItems.INGOT_SATURNITE.get());
        this.basicItem(NtmItems.INGOT_EUPHEMIUM.get());
        this.basicItem(NtmItems.INGOT_DINEUTRONIUM.get());
        this.basicItem(NtmItems.INGOT_ELECTRONIUM.get());
        this.basicItem(NtmItems.INGOT_SMORE.get());
        this.basicItem(NtmItems.INGOT_OSMIRIDIUM.get());
        this.basicItem(NtmItems.INGOT_DUSTED_STEEL.get());
        this.basicItem(NtmItems.INGOT_CHAINSTEEL.get());
        this.basicItem(NtmItems.INGOT_METEORITE.get());
        this.basicItem(NtmItems.INGOT_METEORITE_FORGED.get());
        this.basicItem(NtmItems.METEORITE_BLADE.get());
        this.basicItem(NtmItems.INGOT_PHOSPHORUS.get());
        this.basicItem(NtmItems.INGOT_LITHIUM.get());
        this.basicItem(NtmItems.INGOT_ZIRCONIUM.get());
        this.basicItem(NtmItems.INGOT_SEMTEX.get());
        this.basicItem(NtmItems.INGOT_C4.get());
        this.basicItem(NtmItems.TAR_OIL.get());
        this.basicItem(NtmItems.TAR_CRACK_OIL.get());
        this.basicItem(NtmItems.TAR_COAL.get());
        this.basicItem(NtmItems.TAR_WOOD.get());;
        this.basicItem(NtmItems.WAX_PETROLEUM.get());;
        this.basicItem(NtmItems.WAX_PARAFFIN.get());
        this.basicItem(NtmItems.SOLID_FUEL.get());
        this.basicItem(NtmItems.SOLID_FUEL_PRESTO.get());
        this.basicItem(NtmItems.SOLID_FUEL_PRESTO_TRIPLET.get());
        this.basicItem(NtmItems.SOLID_FUEL_BF.get());
        this.basicItem(NtmItems.SOLID_FUEL_PRESTO_BF.get());
        this.basicItem(NtmItems.SOLID_FUEL_PRESTO_TRIPLET_BF.get());
        this.basicItem(NtmItems.ROCKET_FUEL.get());
        this.basicItem(NtmItems.INGOT_REDSTONE.get());
        this.basicItem(NtmItems.INGOT_NEODYMIUM.get());
        this.basicItem(NtmItems.INGOT_BORAX.get());
        this.basicItem(NtmItems.INGOT_SODIUM.get());
        this.basicItem(NtmItems.INGOT_STRONTIUM.get());
        this.basicItem(NtmItems.INGOT_SLAG.get());

        this.basicItem(NtmItems.BILLET_URANIUM.get());
        this.basicItem(NtmItems.BILLET_U233.get());
        this.basicItem(NtmItems.BILLET_U235.get());
        this.basicItem(NtmItems.BILLET_U238.get());
        this.basicItem(NtmItems.BILLET_UZH.get());
        this.basicItem(NtmItems.BILLET_TH232.get());
        this.basicItem(NtmItems.BILLET_PLUTONIUM.get());
        this.basicItem(NtmItems.BILLET_PU238.get());
        this.basicItem(NtmItems.BILLET_PU239.get());
        this.basicItem(NtmItems.BILLET_PU240.get());
        this.basicItem(NtmItems.BILLET_PU241.get());
        this.basicItem(NtmItems.BILLET_PU_MIX.get());
        this.basicItem(NtmItems.BILLET_AM241.get());
        this.basicItem(NtmItems.BILLET_AM242.get());
        this.basicItem(NtmItems.BILLET_AM_MIX.get());
        this.basicItem(NtmItems.BILLET_NEPTUNIUM.get());
        this.basicItem(NtmItems.BILLET_PO210.get());
        this.basicItem(NtmItems.BILLET_TC99.get());
        this.basicItem(NtmItems.BILLET_COBALT.get());
        this.basicItem(NtmItems.BILLET_CO60.get());
        this.basicItem(NtmItems.BILLET_SR90.get());
        this.basicItem(NtmItems.BILLET_AU198.get());
        this.basicItem(NtmItems.BILLET_PB209.get());
        this.basicItem(NtmItems.BILLET_RA226.get());
        this.basicItem(NtmItems.BILLET_AC227.get());
        this.basicItem(NtmItems.BILLET_SCHRABIDIUM.get());
        this.basicItem(NtmItems.BILLET_SOLINIUM.get());
        this.basicItem(NtmItems.BILLET_GH336.get());
        this.basicItem(NtmItems.BILLET_AUSTRALIUM.get());
        this.basicItem(NtmItems.BILLET_AUSTRALIUM_LESSER.get());
        this.basicItem(NtmItems.BILLET_AUSTRALIUM_GREATER.get());
        this.basicItem(NtmItems.BILLET_URANIUM_FUEL.get());
        this.basicItem(NtmItems.BILLET_THORIUM_FUEL.get());
        this.basicItem(NtmItems.BILLET_PLUTONIUM_FUEL.get());
        this.basicItem(NtmItems.BILLET_NEPTUNIUM_FUEL.get());
        this.basicItem(NtmItems.BILLET_MOX_FUEL.get());
        this.basicItem(NtmItems.BILLET_AMERICIUM_FUEL.get());
        this.basicItem(NtmItems.BILLET_LES.get());
        this.basicItem(NtmItems.BILLET_SCHRABIDIUM_FUEL.get());
        this.basicItem(NtmItems.BILLET_HES.get());
        this.basicItem(NtmItems.BILLET_PO210BE.get());
        this.basicItem(NtmItems.BILLET_RA226BE.get());
        this.basicItem(NtmItems.BILLET_PU238BE.get());
        this.basicItem(NtmItems.BILLET_BERYLLIUM.get());
        this.basicItem(NtmItems.BILLET_BISMUTH.get());
        this.basicItem(NtmItems.BILLET_SILICON.get());
        this.basicItem(NtmItems.BILLET_ZIRCONIUM.get());
        this.basicItem(NtmItems.BILLET_ZFB_BISMUTH.get());
        this.basicItem(NtmItems.BILLET_ZFB_PU241.get());
        this.basicItem(NtmItems.BILLET_ZFB_AM_MIX.get());
        this.basicItem(NtmItems.BILLET_YHARONITE.get());
        this.basicItem(NtmItems.BILLET_BALEFIRE_GOLD.get());
        this.basicItem(NtmItems.BILLET_FLASHLEAD.get());
        this.basicItem(NtmItems.BILLET_NUCLEAR_WASTE.get());

        this.basicItem(NtmItems.CINNABAR.get());
        this.basicItem(NtmItems.MERCURY_NUGGET_TINY.get());
        this.basicItem(NtmItems.MERCURY_NUGGET.get());
        this.basicItem(NtmItems.MERCURY_BOTTLE.get());
        this.basicItem(NtmItems.COKE_COAL.get());
        this.basicItem(NtmItems.COKE_LIGNITE.get());
        this.basicItem(NtmItems.COKE_PETROLEUM.get());
        this.basicItem(NtmItems.LIGNITE.get());
        this.basicItem(NtmItems.COAL_INFERNAL.get());
        this.basicItem(NtmItems.BRIQUETTE_COAL.get());
        this.basicItem(NtmItems.BRIQUETTE_LIGNITE.get());
        this.basicItem(NtmItems.BRIQUETTE_WOOD.get());
        this.basicItem(NtmItems.SULFUR.get());
        this.basicItem(NtmItems.NITER.get());
        this.basicItem(NtmItems.NITRA.get());
        this.basicItem(NtmItems.NITRA_SMALL.get());
        this.basicItem(NtmItems.FLUORITE.get());

        this.basicItem(NtmItems.POWDER_COAL.get());
        this.basicItem(NtmItems.POWDER_COAL_TINY.get());
        this.basicItem(NtmItems.POWDER_IRON.get());
        this.basicItem(NtmItems.POWDER_GOLD.get());
        this.basicItem(NtmItems.POWDER_LAPIS.get());
        this.basicItem(NtmItems.POWDER_QUARTZ.get());
        this.basicItem(NtmItems.POWDER_DIAMOND.get());
        this.basicItem(NtmItems.POWDER_EMERALD.get());
        this.basicItem(NtmItems.POWDER_URANIUM.get());
        this.basicItem(NtmItems.POWDER_PLUTONIUM.get());
        this.basicItem(NtmItems.POWDER_NEPTUNIUM.get());
        this.basicItem(NtmItems.POWDER_PO210.get());
        this.basicItem(NtmItems.POWDER_CO60.get());
        this.basicItem(NtmItems.POWDER_SR90.get());
        this.basicItem(NtmItems.POWDER_SR90_TINY.get());
        this.basicItem(NtmItems.POWDER_I131.get());
        this.basicItem(NtmItems.POWDER_I131_TINY.get());
        this.basicItem(NtmItems.POWDER_XE135.get());
        this.basicItem(NtmItems.POWDER_XE135_TINY.get());
        this.basicItem(NtmItems.POWDER_CS137.get());
        this.basicItem(NtmItems.POWDER_CS137_TINY.get());
        this.basicItem(NtmItems.POWDER_AU198.get());
        this.basicItem(NtmItems.POWDER_RA226.get());
        this.basicItem(NtmItems.POWDER_AT209.get());
        this.basicItem(NtmItems.POWDER_TITANIUM.get());
        this.basicItem(NtmItems.POWDER_COPPER.get());
        this.basicItem(NtmItems.POWDER_RED_COPPER.get());
        this.basicItem(NtmItems.POWDER_TUNGSTEN.get());
        this.basicItem(NtmItems.POWDER_ALUMINIUM.get());
        this.basicItem(NtmItems.POWDER_STEEL.get());
        this.basicItem(NtmItems.POWDER_STEEL_TINY.get());
        this.basicItem(NtmItems.POWDER_TECHNETIUM.get());
        this.basicItem(NtmItems.POWDER_LEAD.get());
        this.basicItem(NtmItems.POWDER_BISMUTH.get());
        this.basicItem(NtmItems.POWDER_CALCIUM.get());
        this.basicItem(NtmItems.POWDER_CADMIUM.get());
        this.basicItem(NtmItems.POWDER_COLTAN_ORE.get());
        this.basicItem(NtmItems.POWDER_COLTAN.get());
        this.basicItem(NtmItems.POWDER_TANTALIUM.get());
        this.basicItem(NtmItems.POWDER_TEKTITE.get());
        this.basicItem(NtmItems.POWDER_PALEOGENITE.get());
        this.basicItem(NtmItems.POWDER_PALEOGENITE_TINY.get());
        this.basicItem(NtmItems.POWDER_IMPURE_OSMIRIDIUM.get());
        this.basicItem(NtmItems.POWDER_BORAX.get());
        this.basicItem(NtmItems.POWDER_CHLOROCALCITE.get());
        this.basicItem(NtmItems.POWDER_MOLYSITE.get());
        this.basicItem(NtmItems.POWDER_YELLOWCAKE.get());
        this.basicItem(NtmItems.POWDER_BERYLLIUM.get());
        this.basicItem(NtmItems.POWDER_DURA_STEEL.get());
        this.basicItem(NtmItems.POWDER_POLYMER.get());
        this.basicItem(NtmItems.POWDER_BAKELITE.get());
        this.basicItem(NtmItems.POWDER_SCHRABIDIUM.get());
        this.basicItem(NtmItems.POWDER_SCHRABIDATE.get());
        this.basicItem(NtmItems.POWDER_MAGNETIZED_TUNGSTEN.get());
        this.basicItem(NtmItems.POWDER_CHLOROPHYTE.get());
        this.basicItem(NtmItems.POWDER_COMBINE_STEEL.get());
        this.basicItem(NtmItems.POWDER_LITHIUM.get());
        this.basicItem(NtmItems.POWDER_LITHIUM_TINY.get());
        this.basicItem(NtmItems.POWDER_ZIRCONIUM.get());
        this.basicItem(NtmItems.POWDER_SODIUM.get());
        this.basicItem(NtmItems.POWDER_LIGNITE.get());
        this.basicItem(NtmItems.POWDER_IODINE.get());
        this.basicItem(NtmItems.POWDER_THORIUM.get());
        this.basicItem(NtmItems.POWDER_NEODYMIUM.get());
        this.basicItem(NtmItems.POWDER_NEODYMIUM_TINY.get());
        this.basicItem(NtmItems.POWDER_ASTATINE.get());
        this.basicItem(NtmItems.POWDER_CAESIUM.get());
        this.basicItem(NtmItems.POWDER_AUSTRALIUM.get());
        this.basicItem(NtmItems.POWDER_STRONTIUM.get());
        this.basicItem(NtmItems.POWDER_COBALT.get());
        this.basicItem(NtmItems.POWDER_COBALT_TINY.get());
        this.basicItem(NtmItems.POWDER_BROMINE.get());
        this.basicItem(NtmItems.POWDER_NIOBIUM.get());
        this.basicItem(NtmItems.POWDER_NIOBIUM_TINY.get());
        this.basicItem(NtmItems.POWDER_TENNESSINE.get());
        this.basicItem(NtmItems.POWDER_CERIUM.get());
        this.basicItem(NtmItems.POWDER_CERIUM_TINY.get());
        this.basicItem(NtmItems.POWDER_LANTHANIUM.get());
        this.basicItem(NtmItems.POWDER_LANTHANIUM_TINY.get());
        this.basicItem(NtmItems.POWDER_ACTINIUM.get());
        this.basicItem(NtmItems.POWDER_ACTINIUM_TINY.get());
        this.basicItem(NtmItems.POWDER_BORON.get());
        this.basicItem(NtmItems.POWDER_BORON_TINY.get());
        this.basicItem(NtmItems.POWDER_ASBESTOS.get());
        this.basicItem(NtmItems.POWDER_MAGIC.get());
        this.basicItem(NtmItems.POWDER_SAWDUST.get());
        this.basicItem(NtmItems.POWDER_FLUX.get());
        this.basicItem(NtmItems.POWDER_FERTILIZER.get());
        this.basicItem(NtmItems.POWDER_BALEFIRE.get());
        this.basicItem(NtmItems.POWDER_SEMTEX_MIX.get());
        this.basicItem(NtmItems.POWDER_DESH_MIX.get());
        this.basicItem(NtmItems.POWDER_DESH_READY.get());
        this.basicItem(NtmItems.POWDER_DESH.get());
        this.basicItem(NtmItems.POWDER_NITAN_MIX.get());
        this.basicItem(NtmItems.POWDER_SPARK_MIX.get());
        this.basicItem(NtmItems.POWDER_METEORITE.get());
        this.basicItem(NtmItems.POWDER_METEORITE_TINY.get());
        this.basicItem(NtmItems.POWDER_EUPHEMIUM.get());
        this.basicItem(NtmItems.POWDER_DINEUTRONIUM.get());
        this.basicItem(NtmItems.DUST.get());
        this.basicItem(NtmItems.DUST_TINY.get());
        this.basicItem(NtmItems.DUST_FALLOUT.get());
        this.basicItem(NtmItems.POWDER_ASH_WOOD.get());
        this.basicItem(NtmItems.POWDER_ASH_COAL.get());
        this.basicItem(NtmItems.POWDER_ASH.get());
        this.basicItem(NtmItems.POWDER_ASH_FLY.get());
        this.basicItem(NtmItems.POWDER_ASH_SOOT.get());
        this.basicItem(NtmItems.POWDER_ASH_FULLERENE.get());
        this.basicItem(NtmItems.POWDER_LIMESTONE.get());
        this.basicItem(NtmItems.POWDER_CEMENT.get());
        this.basicItem(NtmItems.POWDER_RED_PHOSPHOROUS.get());
        this.basicItem(NtmItems.POWDER_ICE.get());
        this.basicItem(NtmItems.POWDER_POISON.get());
        this.basicItem(NtmItems.POWDER_THERMITE.get());
        this.basicItem(NtmItems.POWDER_ENERGY.get());
        this.basicItem(NtmItems.CORDITE.get());
        this.basicItem(NtmItems.BALLISTITE.get());
        this.basicItem(NtmItems.BALL_DYNAMITE.get());
        this.basicItem(NtmItems.BALL_TNT.get());
        this.basicItem(NtmItems.BALL_TATB.get());
        this.basicItem(NtmItems.BALL_RESIN.get());
        this.basicItem(NtmItems.BALL_FIRECLAY.get());

        this.basicItem(NtmItems.SCRAP.get());

        this.basicItem(NtmItems.CRYSTAL_COAL.get());
        this.basicItem(NtmItems.CRYSTAL_IRON.get());
        this.basicItem(NtmItems.CRYSTAL_GOLD.get());
        this.basicItem(NtmItems.CRYSTAL_REDSTONE.get());
        this.basicItem(NtmItems.CRYSTAL_LAPIS.get());
        this.basicItem(NtmItems.CRYSTAL_DIAMOND.get());
        this.basicItem(NtmItems.CRYSTAL_URANIUM.get());
        this.basicItem(NtmItems.CRYSTAL_THORIUM.get());
        this.basicItem(NtmItems.CRYSTAL_PLUTONIUM.get());
        this.basicItem(NtmItems.CRYSTAL_TITANIUM.get());
        this.basicItem(NtmItems.CRYSTAL_SULFUR.get());
        this.basicItem(NtmItems.CRYSTAL_NITER.get());
        this.basicItem(NtmItems.CRYSTAL_COPPER.get());
        this.basicItem(NtmItems.CRYSTAL_TUNGSTEN.get());
        this.basicItem(NtmItems.CRYSTAL_ALUMINIUM.get());
        this.basicItem(NtmItems.CRYSTAL_FLUORITE.get());
        this.basicItem(NtmItems.CRYSTAL_BERYLLIUM.get());
        this.basicItem(NtmItems.CRYSTAL_LEAD.get());
        this.basicItem(NtmItems.CRYSTAL_SCHRARANIUM.get());
        this.basicItem(NtmItems.CRYSTAL_SCHRABIDIUM.get());
        this.basicItem(NtmItems.CRYSTAL_RARE.get());
        this.basicItem(NtmItems.CRYSTAL_PHOSPHORUS.get());
        this.basicItem(NtmItems.CRYSTAL_LITHIUM.get());
        this.basicItem(NtmItems.CRYSTAL_COBALT.get());
        this.basicItem(NtmItems.CRYSTAL_STARMETAL.get());
        this.basicItem(NtmItems.CRYSTAL_CINNABAR.get());
        this.basicItem(NtmItems.CRYSTAL_TRIXITE.get());
        this.basicItem(NtmItems.CRYSTAL_OSMIRIDIUM.get());

        this.basicItem(NtmItems.GEM_SODALITE.get());
        this.basicItem(NtmItems.GEM_TANTALIUM.get());
        this.basicItem(NtmItems.GEM_VOLCANIC.get());
        this.basicItem(NtmItems.GEM_RAD.get());
        this.basicItem(NtmItems.GEM_ALEXANDRITE.get());
        this.basicItem(NtmItems.FRAGMENT_NEODYMIUM.get());
        this.basicItem(NtmItems.FRAGMENT_COBALT.get());
        this.basicItem(NtmItems.FRAGMENT_NIOBIUM.get());
        this.basicItem(NtmItems.FRAGMENT_CERIUM.get());
        this.basicItem(NtmItems.FRAGMENT_LANTHANIUM.get());
        this.basicItem(NtmItems.FRAGMENT_AC227.get());
        this.basicItem(NtmItems.FRAGMENT_BORON.get());
        this.basicItem(NtmItems.FRAGMENT_METEORITE.get());
        this.basicItem(NtmItems.FRAGMENT_COLTAN.get());
        this.basicItem(NtmItems.CHUNK_RARE.get());
        this.basicItem(NtmItems.CHUNK_MALACHITE.get());
        this.basicItem(NtmItems.CHUNK_CRYOLITE.get());
        this.basicItem(NtmItems.MOONSTONE.get());
        this.basicItem(NtmItems.BIOMASS.get());
        this.basicItem(NtmItems.BIOMASS_COMPRESSED.get());
        this.basicItem(NtmItems.BIO_WAFER.get());

        this.basicItem(NtmItems.NUGGET_URANIUM.get());
        this.basicItem(NtmItems.NUGGET_U233.get());
        this.basicItem(NtmItems.NUGGET_U235.get());
        this.basicItem(NtmItems.NUGGET_U238.get());
        this.basicItem(NtmItems.NUGGET_TH232.get());
        this.basicItem(NtmItems.NUGGET_PLUTONIUM.get());
        this.basicItem(NtmItems.NUGGET_PU238.get());
        this.basicItem(NtmItems.NUGGET_PU239.get());
        this.basicItem(NtmItems.NUGGET_PU240.get());
        this.basicItem(NtmItems.NUGGET_PU241.get());
        this.basicItem(NtmItems.NUGGET_PU_MIX.get());
        this.basicItem(NtmItems.NUGGET_AM241.get());
        this.basicItem(NtmItems.NUGGET_AM242.get());
        this.basicItem(NtmItems.NUGGET_AM_MIX.get());
        this.basicItem(NtmItems.NUGGET_NEPTUNIUM.get());
        this.basicItem(NtmItems.NUGGET_PO210.get());
        this.basicItem(NtmItems.NUGGET_COBALT.get());
        this.basicItem(NtmItems.NUGGET_CO60.get());
        this.basicItem(NtmItems.NUGGET_SR90.get());
        this.basicItem(NtmItems.NUGGET_TC99.get());
        this.basicItem(NtmItems.NUGGET_AU198.get());
        this.basicItem(NtmItems.NUGGET_PB209.get());
        this.basicItem(NtmItems.NUGGET_RA226.get());
        this.basicItem(NtmItems.NUGGET_AC227.get());
        this.basicItem(NtmItems.NUGGET_LEAD.get());
        this.basicItem(NtmItems.NUGGET_BISMUTH.get());
        this.basicItem(NtmItems.NUGGET_ARSENIC.get());
        this.basicItem(NtmItems.NUGGET_TANTALIUM.get());
        this.basicItem(NtmItems.NUGGET_SILICON.get());
        this.basicItem(NtmItems.NUGGET_NIOBIUM.get());
        this.basicItem(NtmItems.NUGGET_BERYLLIUM.get());
        this.basicItem(NtmItems.NUGGET_SCHRABIDIUM.get());
        this.basicItem(NtmItems.NUGGET_SOLINIUM.get());
        this.basicItem(NtmItems.NUGGET_GH336.get());
        this.basicItem(NtmItems.NUGGET_URANIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_THORIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_PLUTONIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_NEPTUNIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_MOX_FUEL.get());
        this.basicItem(NtmItems.NUGGET_AMERICIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_SCHRABIDIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_HES.get());
        this.basicItem(NtmItems.NUGGET_LES.get());
        this.basicItem(NtmItems.NUGGET_ZIRCONIUM.get());
        this.basicItem(NtmItems.NUGGET_AUSTRALIUM.get());
        this.basicItem(NtmItems.NUGGET_AUSTRALIUM_LESSER.get());
        this.basicItem(NtmItems.NUGGET_AUSTRALIUM_GREATER.get());
        this.basicItem(NtmItems.NUGGET_DESH.get());
        this.basicItem(NtmItems.NUGGET_EUPHEMIUM.get());
        this.basicItem(NtmItems.NUGGET_DINEUTRONIUM.get());
        this.basicItem(NtmItems.NUGGET_OSMIRIDIUM.get());

        this.basicItem(NtmItems.NEUTRON_REFLECTOR.get());

        this.basicItem(NtmItems.PLATE_IRON.get());
        this.basicItem(NtmItems.PLATE_GOLD.get());
        this.basicItem(NtmItems.PLATE_TITANIUM.get());
        this.basicItem(NtmItems.PLATE_ALUMINIUM.get());
        this.basicItem(NtmItems.PLATE_STEEL.get());
        this.basicItem(NtmItems.PLATE_LEAD.get());
        this.basicItem(NtmItems.PLATE_COPPER.get());
        this.basicItem(NtmItems.PLATE_DURA_STEEL.get());
        this.basicItem(NtmItems.PLATE_SCHRABIDIUM.get());
        this.basicItem(NtmItems.PLATE_COMBINE_STEEL.get());
        this.basicItem(NtmItems.PLATE_MIXED.get());
        this.basicItem(NtmItems.PLATE_GUNMETAL.get());
        this.basicItem(NtmItems.PLATE_WEAPONSTEEL.get());
        this.basicItem(NtmItems.PLATE_SATURNITE.get());
        this.basicItem(NtmItems.PLATE_PAA.get());
        this.basicItem(NtmItems.PLATE_POLYMER.get());
        this.basicItem(NtmItems.PLATE_KEVLAR.get());
        this.basicItem(NtmItems.PLATE_DALEKANIUM.get());
        this.basicItem(NtmItems.PLATE_DESH.get());
        this.basicItem(NtmItems.PLATE_BISMUTH.get());
        this.basicItem(NtmItems.PLATE_EUPHEMIUM.get());
        this.basicItem(NtmItems.PLATE_DINEUTRONIUM.get());
        this.basicItem(NtmItems.PLATE_ARMOR_TITANIUM.get());
        this.basicItem(NtmItems.PLATE_ARMOR_AJR.get());
        this.basicItem(NtmItems.PLATE_ARMOR_HEV.get());
        this.basicItem(NtmItems.PLATE_ARMOR_LUNAR.get());
        this.basicItem(NtmItems.PLATE_ARMOR_FAU.get());
        this.basicItem(NtmItems.PLATE_ARMOR_DNT.get());

        this.basicItem(NtmItems.PELLET_RTG.get());

        this.basicItem(NtmItems.CELL_EMPTY.get());
        this.basicItem(NtmItems.CELL_UF6.get());
        this.basicItem(NtmItems.CELL_PUF6.get());
        this.basicItem(NtmItems.CELL_ANTIMATTER.get());
        this.basicItem(NtmItems.CELL_DEUTERIUM.get());
        this.basicItem(NtmItems.CELL_TRITIUM.get());
        this.basicItem(NtmItems.CELL_SAS3.get());
        this.basicItem(NtmItems.CELL_ANTI_SCHARBIDIUM.get());
        this.basicItem(NtmItems.CELL_BALEFIRE.get());

        this.basicItem(NtmItems.PARTICLE_DIGAMMA.get());
        this.basicItem(NtmItems.PARTICLE_LUTECE.get());

        this.basicItem(NtmItems.STAMP_FLAT_STONE.get());
        this.basicItem(NtmItems.STAMP_PLATE_STONE.get());
        this.basicItem(NtmItems.STAMP_WIRE_STONE.get());
        this.basicItem(NtmItems.STAMP_CIRCUIT_STONE.get());
        this.basicItem(NtmItems.STAMP_FLAT_IRON.get());
        this.basicItem(NtmItems.STAMP_PLATE_IRON.get());
        this.basicItem(NtmItems.STAMP_WIRE_IRON.get());
        this.basicItem(NtmItems.STAMP_CIRCUIT_IRON.get());
        this.basicItem(NtmItems.STAMP_FLAT_STEEL.get());
        this.basicItem(NtmItems.STAMP_PLATE_STEEL.get());
        this.basicItem(NtmItems.STAMP_WIRE_STEEL.get());
        this.basicItem(NtmItems.STAMP_CIRCUIT_STEEL.get());
        this.basicItem(NtmItems.STAMP_FLAT_TITANIUM.get());
        this.basicItem(NtmItems.STAMP_PLATE_TITANIUM.get());
        this.basicItem(NtmItems.STAMP_WIRE_TITANIUM.get());
        this.basicItem(NtmItems.STAMP_CIRCUIT_TITANIUM.get());
        this.basicItem(NtmItems.STAMP_FLAT_OBSIDIAN.get());
        this.basicItem(NtmItems.STAMP_PLATE_OBSIDIAN.get());
        this.basicItem(NtmItems.STAMP_WIRE_OBSIDIAN.get());
        this.basicItem(NtmItems.STAMP_CIRCUIT_OBSIDIAN.get());
        this.basicItem(NtmItems.STAMP_FLAT_DESH.get());
        this.basicItem(NtmItems.STAMP_PLATE_DESH.get());
        this.basicItem(NtmItems.STAMP_WIRE_DESH.get());
        this.basicItem(NtmItems.STAMP_CIRCUIT_DESH.get());
        this.basicItem(NtmItems.STAMP_IRON_357.get());
        this.basicItem(NtmItems.STAMP_IRON_44.get());
        this.basicItem(NtmItems.STAMP_IRON_9.get());
        this.basicItem(NtmItems.STAMP_IRON_50.get());
        this.basicItem(NtmItems.STAMP_DESH_357.get());
        this.basicItem(NtmItems.STAMP_DESH_44.get());
        this.basicItem(NtmItems.STAMP_DESH_9.get());
        this.basicItem(NtmItems.STAMP_DESH_50.get());

        this.basicItem(NtmItems.BLADES_IRON.get());
        this.basicItem(NtmItems.BLADES_STEEL.get());
        this.basicItem(NtmItems.BLADES_DESH.get());

        this.basicItem(NtmItems.CATALYTIC_CONVERTER.get());

        this.basicItem(NtmItems.SINGULARITY.get());
        this.basicItem(NtmItems.SINGULARITY_COUNTER_RESONANT.get());
        this.basicItem(NtmItems.SINGULARITY_SUPER_HEATED.get());
        this.basicItem(NtmItems.BLACK_HOLE.get());
        this.basicItem(NtmItems.SINGULARITY_SPARK.get());
        this.basicItem(NtmItems.PELLET_ANTIMATTER.get());

        this.basicItem(NtmItems.INF_WATER.get());
        this.basicItem(NtmItems.INF_WATER_MK2.get());

        this.basicCustomLayerItem(NtmItems.FLUID_TANK_EMPTY.get(), "fluid_tank");
        this.layeredItem(NtmItems.FLUID_TANK_FULL.get(), "fluid_tank", "fluid_tank_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_TANK_LEAD_EMPTY.get(), "fluid_tank_lead");
        this.layeredItem(NtmItems.FLUID_TANK_LEAD_FULL.get(), "fluid_tank_lead", "fluid_tank_lead_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_BARREL_EMPTY.get(), "fluid_barrel_empty");
        this.layeredItem(NtmItems.FLUID_BARREL_FULL.get(), "fluid_barrel", "fluid_barrel_overlay");
        this.basicItem(NtmItems.FLUID_BARREL_INFINITE.get());

        this.basicCustomLayerItem(NtmItems.FLUID_PACK_EMPTY.get(), "fluid_pack");
        this.layeredItem(NtmItems.FLUID_PACK_FULL.get(), "fluid_pack", "fluid_pack_overlay");

        this.basicItem(NtmItems.BATTERY_SPARK.get());
        this.basicItem(NtmItems.BATTERY_TRIXITE.get());

        this.entityItem(NtmItems.BATTERY_PACK.get(), false);
        // BATTERY_SC uses ICustomItemModelRegister
        this.basicItem(NtmItems.BATTERY_CREATIVE.get());

        // todo register BLUEPRINTS

        this.basicItem(NtmItems.FLUID_ICON.get());
        this.layeredItem(NtmItems.FLUID_IDENTIFIER_MULTI.get(), "fluid_identifier_multi", "fluid_identifier_overlay");

        this.handheldItem(NtmItems.SCREWDRIVER.get());
        this.handheldItem(NtmItems.SCREWDRIVER_DESH.get());
        this.handheldItem(NtmItems.BLOWTORCH.get());
        this.handheldItem(NtmItems.ACETYLENE_TORCH.get());

        this.basicItem(NtmItems.ROD_EMPTY.get());
        this.basicItem(NtmItems.ROD_DUAL_EMPTY.get());
        this.basicItem(NtmItems.ROD_QUAD_EMPTY.get());

        this.basicItem(NtmItems.SPAWN_DUCK.get());

        this.basicItem(NtmItems.DESIGNATOR.get());
        this.handheldItem(NtmItems.DESIGNATOR_RANGE.get());
        this.basicItem(NtmItems.DOSIMETER.get());
        this.basicItem(NtmItems.GEIGER_COUNTER.get());
        this.basicItem(NtmItems.DIGAMMA_DIAGNOSTIC.get());

        this.basicItem(NtmItems.PIN.get());
        this.basicItem(NtmItems.KEY.get());
        this.basicItem(NtmItems.KEY_RED.get());
        this.basicItem(NtmItems.KEY_RED_CRACKED.get());
        this.basicItem(NtmItems.KEY_KIT.get());
        this.basicItem(NtmItems.KEY_FAKE.get());

        this.entityItem(NtmItems.MISSILE_TAINT.get(), true);
        this.entityItem(NtmItems.MISSILE_MICRO.get(), true);
        this.entityItem(NtmItems.MISSILE_BHOLE.get(), true);
        this.entityItem(NtmItems.MISSILE_SCHRABIDIUM.get(), true);
        this.entityItem(NtmItems.MISSILE_EMP.get(), true);
        this.entityItem(NtmItems.MISSILE_GENERIC.get(), true);
        this.entityItem(NtmItems.MISSILE_INCENDIARY.get(), true);
        this.entityItem(NtmItems.MISSILE_CLUSTER.get(), true);
        this.entityItem(NtmItems.MISSILE_BUSTER.get(), true);
        this.entityItem(NtmItems.MISSILE_DECOY.get(), true);
        this.entityItem(NtmItems.MISSILE_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_INCENDIARY_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_CLUSTER_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_BUSTER_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_EMP_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_BURST.get(), true);
        this.entityItem(NtmItems.MISSILE_INFERNO.get(), true);
        this.entityItem(NtmItems.MISSILE_RAIN.get(), true);
        this.entityItem(NtmItems.MISSILE_DRILL.get(), true);
        this.entityItem(NtmItems.MISSILE_NUCLEAR.get(), true);
        this.entityItem(NtmItems.MISSILE_NUCLEAR_CLUSTER.get(), true);
        this.entityItem(NtmItems.MISSILE_VOLCANO.get(), true);
        this.entityItem(NtmItems.MISSILE_DOOMSDAY.get(), true);
        this.entityItem(NtmItems.MISSILE_DOOMSDAY_RUSTED.get(), true);
        this.entityItem(NtmItems.MISSILE_SHUTTLE.get(), true);
        this.entityItem(NtmItems.MISSILE_STEALTH.get(), true);

        // MISSILE_SOYUZ uses ICustomItemModelRegister

        this.basicItem(NtmItems.SATELLITE_RADAR.get());
        this.basicItem(NtmItems.SATELLITE_LASER.get());
        this.basicItem(NtmItems.SATELLITE_INTERFACE.get());

        this.basicItem(NtmItems.BALEFIRE_AND_STEEL.get());

        // DRINK uses ICustomItemModelRegister
        this.handheldItem(NtmItems.BOTTLE_OPENER.get());

        // CANNED_CONSERVE uses ICustomItemModelRegister

        // CAP uses ICustomItemModelRegister
        this.basicItem(NtmItems.RING_PULL.get());
        this.basicItem(NtmItems.CAN_KEY.get());

        this.basicItem(NtmItems.CHOCOLATE_MILK.get());
        this.handheldItem(NtmItems.CIGARETTE.get());
        this.handheldItem(NtmItems.CRACKPIPE.get());

        this.basicItem(NtmItems.EARLY_EXPLOSIVE_LENSES.get());
        this.basicItem(NtmItems.EXPLOSIVE_LENSES.get());

        this.basicItem(NtmItems.GADGET_WIREING.get());
        this.basicItem(NtmItems.GADGET_CORE.get());

        this.basicItem(NtmItems.LITTLE_BOY_SHIELDING.get());
        this.basicItem(NtmItems.LITTLE_BOY_TARGET.get());
        this.basicItem(NtmItems.LITTLE_BOY_BULLET.get());
        this.basicItem(NtmItems.LITTLE_BOY_PROPELLANT.get());
        this.basicItem(NtmItems.LITTLE_BOY_IGNITER.get());

        this.basicItem(NtmItems.FAT_MAN_IGNITER.get());
        this.basicItem(NtmItems.FAT_MAN_CORE.get());

        this.basicItem(NtmItems.IVY_MIKE_CORE.get());
        this.basicItem(NtmItems.IVY_MIKE_DEUT.get());
        this.basicItem(NtmItems.IVY_MIKE_COOLING_UNIT.get());

        this.basicItem(NtmItems.TSAR_BOMBA_CORE.get());

        this.basicItem(NtmItems.FLEIJA_IGNITER.get());
        this.basicItem(NtmItems.FLEIJA_PROPELLANT.get());
        this.basicItem(NtmItems.FLEIJA_CORE.get());

        this.basicItem(NtmItems.SOLINIUM_IGNITER.get());
        this.basicItem(NtmItems.SOLINIUM_PROPELLANT.get());
        this.basicItem(NtmItems.SOLINIUM_CORE.get());

        this.basicItem(NtmItems.N2_CHARGE.get());

        this.basicItem(NtmItems.EGG_BALEFIRE_SHARD.get());
        this.basicItem(NtmItems.EGG_BALEFIRE.get());

        this.handheldItem(NtmItems.IGNITER.get());
        this.handheldItem(NtmItems.DETONATOR.get());
        this.handheldItem(NtmItems.DETONATOR_MULTI.get());
        this.entityItem(NtmItems.DETONATOR_LASER.get(), true);
        this.handheldItem(NtmItems.DETONATOR_DEADMAN.get());
        this.handheldItem(NtmItems.DETONATOR_DE.get());
        this.handheldItem(NtmItems.BOMB_CALLER.get());
        this.handheldItem(NtmItems.DEFUSER.get());
        this.handheldItem(NtmItems.REACHER.get());

        this.registerPolaroid();
        this.basicItem(NtmItems.BURNT_BARK.get());

        // STARTER_KIT uses ICustomItemModelRegister

        this.basicItem(NtmItems.TEMPLATE_FOLDER.get());
        this.basicItem(NtmItems.NOTHING.get());

        // welp
        this.getBuilder(NtmBlocks.LEAVES_LAYER.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/layering_1")));
        this.getBuilder(NtmBlocks.OIL_SPILL.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/oil_spill_1")));
        this.getBuilder(NtmBlocks.FALLOUT.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/fallout")));
        this.getBuilder(NtmBlocks.WASTE_LOG.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/waste_log")));
        this.getBuilder(NtmBlocks.FROZEN_LOG.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/frozen_log")));
    }

    private void registerPolaroid() {
        ItemModelBuilder builder = getBuilder("polaroid");
        for (int i = 1; i <= 18; i++) {
            builder.override()
                    .predicate(NuclearTechMod.withDefaultNamespace("polaroid_id"), i)
                    .model(getBuilder("polaroid_" + i)
                            .parent(getExistingFile(mcLoc("item/generated")))
                            .texture("layer0", modLoc("item/polaroid_" + i)))
                    .end();
        }

        // Auto-generated material item models (ingots, dusts, plates...) from Mats.java
        // If a hand-made texture exists at item/{itemName}.png, use it as-is (no tint - it's
        // already the real art). Otherwise fall back to the shared white/grey template for that
        // shape (item/template/<shape>) which gets colorized at render time by NtmItemColors
        // via NTMMaterial#solidColorLight (see that class).
        // Uses UncheckedModelFile so runData doesn't crash if textures are missing yet.
        for (com.hbm.inventory.material.NTMMaterial mat : com.hbm.inventory.material.Mats.orderedList) {
            for (com.hbm.inventory.material.MaterialShapes shape : mat.autogen) {
                String itemName = shape.itemNameFor(mat);
                if (itemName == null) continue;

                String texturePath = hasCustomTexture(itemName)
                        ? "item/" + itemName
                        : "item/template/" + shape.templateName();

                this.getBuilder(itemName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", modLoc(texturePath));
            }
        }
    }

    private ItemModelBuilder layeredItem(Item item, String layer0, String layer1) {
        return this.getBuilder(BuiltInRegistries.ITEM.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + layer0))
                .texture("layer1", modLoc("item/" + layer1));
    }

    private ItemModelBuilder basicCustomLayerItem(Item item, String layer0) {
        return this.getBuilder(BuiltInRegistries.ITEM.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + layer0));
    }

    private void entityItem(Item item, boolean frontLight) {
        this.getBuilder(BuiltInRegistries.ITEM.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).guiLight(frontLight ? BlockModel.GuiLight.FRONT : BlockModel.GuiLight.SIDE);
    }

}
