package com.hbm.config;

import com.hbm.main.NuclearTechMod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

// YES im making one class for every config
public class CommonConfig {

    // GENERAL (01)
    public final BooleanValue ENABLE_MOTD;
    public final BooleanValue ENABLE_EXTENDED_LOGGING;
    public final BooleanValue ENABLE_BOMBER_SHORT_MODE;
    public final BooleanValue ENABLE_SILENT_COMPSTACK_ERRORS;
    public final BooleanValue ENABLE_KEYBIND_OVERLAP;

    public final BooleanValue ENABLE_EXPENSIVE_MODE;

    // NUKES (03)
    public final IntValue GADGET_RADIUS;
    public final IntValue BOY_RADIUS;
    public final IntValue MAN_RADIUS;
    public final IntValue MIKE_RADIUS;
    public final IntValue TSAR_RADIUS;
    public final IntValue PROTOTYPE_RADIUS;
    public final IntValue FLEIJA_RADIUS;
    public final IntValue SOLINIUM_RADIUS;
    public final IntValue N2_RADIUS;
    public final IntValue MISSLE_RADIUS;
    public final IntValue MIRV_RADIUS;
    public final IntValue FATMAN_RADIUS;
    public final IntValue NUKA_RADIUS;
    public final IntValue ASCHRAB_RADIUS;

    // METEORS (05)
    public final BooleanValue ENABLE_METEOR_STRIKES;
    public final BooleanValue ENABLE_METEOR_SHOWERS;
    public final BooleanValue ENABLE_METEOR_TAILS;
    public final BooleanValue ENABLE_SPECIAL_METEORS;
    public final IntValue METEOR_STRIKE_CHACE;
    public final IntValue METEOR_SHOWER_CHACE;
    public final IntValue METEOR_SHOWER_DURATION;

    // EXPLOSIONS (06)
    public final IntValue MK5;
    public final IntValue BLAST_SPEED;
    public final IntValue FALLOUT_RANGE;
    public final IntValue FALLOUT_DELAY;
    public final IntValue LIMIT_EXPLOSION_LIFESPAN;

    // RADIATION (13)
    public final IntValue FOG_RAD;
    public final IntValue FOG_RAD_CH;
    public final DoubleValue HELL_RAD;
    public final BooleanValue WORLD_RAD_EFFECTS;

    public final BooleanValue ENABLE_CONTAMINATION;
    public final BooleanValue ENABLE_CHUNK_RADS;

    // DANGEROUS DROPS (10)
    public final BooleanValue DROP_CELL;
    public final BooleanValue DROP_SINGULARITY;
    public final BooleanValue DROP_STAR;
    public final BooleanValue DROP_CRYSTAL;
    public final BooleanValue DROP_DEAD_MANS_EXPLOSIVE;

    // TOOLS (11)
    public final IntValue RECURSION_DEPTH;
    public final BooleanValue RECURSION_STONE;
    public final BooleanValue RECURSION_NETHERRACK;
    public final BooleanValue ABILITY_HAMMER;
    public final BooleanValue ABILITY_VEIN;
    public final BooleanValue ABILITY_LUCK;
    public final BooleanValue ABILITY_SILK;
    public final BooleanValue ABILITY_FURNACE;
    public final BooleanValue ABILITY_SHREDDER;
    public final BooleanValue ABILITY_CENTRIFUGE;
    public final BooleanValue ABILITY_CRYSTALLIZER;
    public final BooleanValue ABILITY_MERCURY;
    public final BooleanValue ABILITY_EXPLOSION;

    // MOBS (12)
    public final DoubleValue POLLUTION_MULT;

    // HAZARDS (14)
    public final BooleanValue DISABLE_ASBESTOS;
    public final BooleanValue DISABLE_COAL;
    public final BooleanValue DISABLE_HOT;
    public final BooleanValue DISABLE_EXPLOSIVE;
    public final BooleanValue DISABLE_HYDROACTIVE;
    public final BooleanValue DISABLE_BLINDING;

    // POLLUTION (16)
    public final BooleanValue ENABLE_POLLUTION;

    // BIOMES (17)
    public final BooleanValue ENABLE_CRATER_BIOMES;
    public final DoubleValue CRATER_RAD;
    public final DoubleValue CRATER_INNER_RAD;
    public final DoubleValue CRATER_OUTER_RAD;
    public final DoubleValue CRATER_WATER_MULT;

    // 528
    public final ModConfigSpec.BooleanValue ENABLE_528;

    CommonConfig(ModConfigSpec.Builder builder) {

        /// GENERAL ///
        builder.push("general");

        ENABLE_MOTD = builder
                .comment("If enabled, shows the 'Loaded mod!' chat message as well as update notifications when joining a world.")
                .translation(NuclearTechMod.MODID + ".configuration.enableMOTD")
                        .define("enableMOTD", true);
        ENABLE_EXTENDED_LOGGING = builder
                .comment("Logs uses of the detonator, nuclear explosions, missile launches, grenades, etc.")
                .translation(NuclearTechMod.MODID + ".configuration.enableExtendedLogging")
                        .define("enableExtendedLogging", true);
        ENABLE_BOMBER_SHORT_MODE = builder
                .comment("Has bomber planes spawn in closer to the target for use with smaller render distances.")
                .translation(NuclearTechMod.MODID + ".configuration.enableBomberShortMode")
                        .define("enableBomberShortMode", false);
        ENABLE_SILENT_COMPSTACK_ERRORS = builder
                .comment("Enabling this will disable log spam created by unregistered items in ComparableStack instances.")
                .translation(NuclearTechMod.MODID + ".configuration.enableSilentCompStackErrors")
                        .define("enableSilentCompStackErrors", false);
        ENABLE_KEYBIND_OVERLAP = builder
                .comment("If enabled, will handle keybinds that would otherwise be ignored due to overlapping.")
                .translation(NuclearTechMod.MODID + ".configuration.enableKeybindOverlap")
                        .define("enableKeybindOverlap", true);

        ENABLE_EXPENSIVE_MODE = builder
                .comment("It does what the name implies.")
                .translation(NuclearTechMod.MODID + ".configuration.enableExpensiveMode")
                        .define("enableExpensiveMode", false);

        builder.pop();

        /// NUKES ///
        builder.push("nukes");

        GADGET_RADIUS = builder
                .comment("Radius of the Gadget.")
                .translation(NuclearTechMod.MODID + ".configuration.gadgetRadius")
                        .defineInRange("gadgetRadius", 150, 0, Integer.MAX_VALUE);
        BOY_RADIUS = builder
                .comment("Radius of Little Boy.")
                .translation(NuclearTechMod.MODID + ".configuration.boyRadius")
                        .defineInRange("boyRadius", 120, 0, Integer.MAX_VALUE);
        MAN_RADIUS = builder
                .comment("Radius of Fat Man.")
                .translation(NuclearTechMod.MODID + ".configuration.manRadius")
                        .defineInRange("manRadius", 175, 0, Integer.MAX_VALUE);
        MIKE_RADIUS = builder
                .comment("Radius of Ivy Mike.")
                .translation(NuclearTechMod.MODID + ".configuration.mikeRadius")
                        .defineInRange("mikeRadius", 250, 0, Integer.MAX_VALUE);
        TSAR_RADIUS = builder
                .comment("Radius of Tsar Bomba.")
                .translation(NuclearTechMod.MODID + ".configuration.tsarRadius")
                        .defineInRange("tsarRadius", 500, 0, Integer.MAX_VALUE);
        PROTOTYPE_RADIUS = builder
                .comment("Radius of the Prototype.")
                .translation(NuclearTechMod.MODID + ".configuration.prototypeRadius")
                        .defineInRange("prototypeRadius", 150, 0, Integer.MAX_VALUE);
        FLEIJA_RADIUS = builder
                .comment("Radius of F.L.E.I.J.A.")
                .translation(NuclearTechMod.MODID + ".configuration.fleijaRadius")
                        .defineInRange("fleijaRadius", 50, 0, Integer.MAX_VALUE);
        SOLINIUM_RADIUS = builder
                .comment("Radius of the blue rinse.")
                .translation(NuclearTechMod.MODID + ".configuration.soliniumRadius")
                        .defineInRange("soliniumRadius", 150, 0, Integer.MAX_VALUE);
        N2_RADIUS = builder
                .comment("Radius of the N2 mine.")
                .translation(NuclearTechMod.MODID + ".configuration.n2Radius")
                        .defineInRange("n2Radius", 200, 0, Integer.MAX_VALUE);
        MISSLE_RADIUS = builder
                .comment("Radius of the nuclear missile")
                .translation(NuclearTechMod.MODID + ".configuration.missileRadius")
                        .defineInRange("missileRadius", 100, 0, Integer.MAX_VALUE);
        MIRV_RADIUS = builder
                .comment("Radius of a MIRV.")
                .translation(NuclearTechMod.MODID + ".configuration.mirvRadius")
                        .defineInRange("mirvRadius", 100, 0, Integer.MAX_VALUE);
        FATMAN_RADIUS = builder
                .comment("Radius of the Fatman Launcher.")
                .translation(NuclearTechMod.MODID + ".configuration.fatmanRadius")
                        .defineInRange("fatmanRadius", 35, 0, Integer.MAX_VALUE);
        NUKA_RADIUS = builder
                .comment("Radius of the nuka grenade.")
                .translation(NuclearTechMod.MODID + ".configuration.nukaRadius")
                        .defineInRange("nukaRadius", 25, 0, Integer.MAX_VALUE);
        ASCHRAB_RADIUS = builder
                .comment("Radius of dropped anti schrabidium.")
                .translation(NuclearTechMod.MODID + ".configuration.aSchrabRadius")
                        .defineInRange("aSchrabRadius", 20, 0, Integer.MAX_VALUE);

        builder.pop();

        /// METEORS ///
        builder.push("explosion");

        ENABLE_METEOR_STRIKES = builder
                .comment("Toggles the spawning of meteors.")
                .translation(NuclearTechMod.MODID + ".configuration.enableMeteorStrikes")
                        .define("enableMeteorStrikes", true);
        ENABLE_METEOR_SHOWERS = builder
                .comment("Toggles meteor showers, which start with a 1% chance for every spawned meteor.")
                .translation(NuclearTechMod.MODID + ".configuration.enableMeteorShowers")
                        .define("enableMeteorShowers", true);
        ENABLE_METEOR_TAILS = builder
                .comment("Toggles the particle effect created by falling meteors.")
                .translation(NuclearTechMod.MODID + ".configuration.enableMeteorTails")
                        .define("enableMeteorTails", true);
        ENABLE_SPECIAL_METEORS = builder
                .comment("Toggles rare, special meteor types with different impact effects.")
                .translation(NuclearTechMod.MODID + ".configuration.enableSpecialMeteors")
                        .define("enableSpecialMeteors", true);
        METEOR_STRIKE_CHACE = builder
                .comment("The probability of a meteor spawning (an average of once every nTH ticks).")
                .translation(NuclearTechMod.MODID + ".configuration.meteorStrikeChance")
                        .defineInRange("meteorStrikeChance", 360000, 1, Integer.MAX_VALUE);
        METEOR_SHOWER_CHACE = builder
                .comment("The probability of a meteor spawning during meteor shower (an average of once every nTH ticks).")
                .translation(NuclearTechMod.MODID + ".configuration.meteorStrikeChance")
                        .defineInRange("meteorStrikeChance", 18000, 1, Integer.MAX_VALUE);
        METEOR_SHOWER_DURATION = builder
                .comment("Max duration of meteor shower in ticks.")
                .translation(NuclearTechMod.MODID + ".configuration.meteorShowerDuration")
                        .defineInRange("meteorShowerDuration", 36000, 1, Integer.MAX_VALUE);

        builder.pop();

        /// EXPLOSIONS ///
        builder.push("explosion");

        MK5 = builder
                .comment("Minimum amount of milliseconds per tick allocated for mk5 chunk processing.")
                .translation(NuclearTechMod.MODID + ".configuration.mk5BlastTime")
                        .defineInRange("mk5BlastTime", 50, 0, Integer.MAX_VALUE);
        BLAST_SPEED = builder
                .comment("Base speed of MK3 system (old and schrabidium) detonations (Blocks / tick)")
                .translation(NuclearTechMod.MODID + ".configuration.propBlastSpeed")
                        .defineInRange("propBlastSpeed", 1024, 0, Integer.MAX_VALUE);
        FALLOUT_RANGE = builder
                .comment("Radius of fallout area (base radius * value in percent)")
                .translation(NuclearTechMod.MODID + ".configuration.falloutRange")
                        .defineInRange("falloutRange", 100, 0, Integer.MAX_VALUE);
        FALLOUT_DELAY = builder
                .comment("How many ticks to wait for the next fallout chunk computation")
                .translation(NuclearTechMod.MODID + ".configuration.falloutDelay")
                        .defineInRange("falloutDelay", 4, 0, Integer.MAX_VALUE);
        LIMIT_EXPLOSION_LIFESPAN = builder
                .comment("How long an explosion can be unloaded until it dies in seconds. Based of system time. 0 disables the effect.")
                .translation(NuclearTechMod.MODID + ".configuration.limitExplosionLifespan")
                        .defineInRange("limitExplosionLifespan", 0, 0, Integer.MAX_VALUE);

        builder.pop();

        /// RADIATION ///
        builder.push("radiation");

        HELL_RAD = builder
                .comment("RAD/s in the nether.")
                .translation(NuclearTechMod.MODID + ".configuration.hellRad")
                        .defineInRange("hellRad", 0.1D, 0, Double.MAX_VALUE);
        FOG_RAD = builder
                .comment("Radiation in RADs required for fog to spawn.")
                .translation(NuclearTechMod.MODID + ".configuration.fogRad")
                        .defineInRange("fogRad", 100, 0, Integer.MAX_VALUE);
        FOG_RAD_CH = builder
                .comment("1:n chance of fog spawning every second.")
                .translation(NuclearTechMod.MODID + ".configuration.fogRadChance")
                        .defineInRange("fogRadChance", 20, 0, Integer.MAX_VALUE);
        WORLD_RAD_EFFECTS = builder
                .comment("Whether high radiation levels should perform changes in the world.")
                .translation(NuclearTechMod.MODID + ".configuration.worldRadEffects")
                        .define("worldRadEffects", true);

        ENABLE_CONTAMINATION = builder
                .comment("Toggles player contamination (and negative effects from radiation poisoning).")
                .translation(NuclearTechMod.MODID + ".configuration.enableContamination")
                        .define("enableContamination", true);
        ENABLE_CHUNK_RADS = builder
                .comment("Toggles the world radiation system (chunk radiation only, some blocks use an AoE!).")
                .translation(NuclearTechMod.MODID + ".configuration.enableChunkRads")
                        .define("enableChunkRads", true);

        builder.pop();

        /// DANGEROUS DROPS ///
        builder.push("dangerous_drops");

        DROP_CELL = builder
                .comment("Whether antimatter cells should explode when dropped.")
                .translation(NuclearTechMod.MODID + ".configuration.dropCell")
                        .define("dropCell", true);
        DROP_SINGULARITY = builder
                .comment("Whether singularities and black holes should spawn when dropped.")
                .translation(NuclearTechMod.MODID + ".configuration.dropSing")
                        .define("dropSing", true);
        DROP_STAR = builder
                .comment("Whether rigged star blaster cells should explode when dropped.")
                .translation(NuclearTechMod.MODID + ".configuration.dropStar")
                        .define("dropStar", true);
        DROP_CRYSTAL = builder
                .comment("Whether xen crystals should move blocks when dropped.")
                .translation(NuclearTechMod.MODID + ".configuration.dropCrys")
                        .define("dropCrys", true);
        DROP_DEAD_MANS_EXPLOSIVE = builder
                .comment("Whether dead man's explosives should explode when dropped.")
                .translation(NuclearTechMod.MODID + ".configuration.dropDead")
                        .define("dropDead", true);

        builder.pop();

        /// TOOLS ///
        builder.push("tools");

        RECURSION_DEPTH = builder
                .comment("Limits veinminer's recursive function")
                .translation(NuclearTechMod.MODID + ".configuration.recursionDepth")
                        .defineInRange("recursionDepth", 1000, 0, Integer.MAX_VALUE);
        RECURSION_STONE = builder
                .comment("Determines whether veinminer can break stone")
                .translation(NuclearTechMod.MODID + ".configuration.recursionStone")
                        .define("recursionStone", false);
        RECURSION_NETHERRACK = builder
                .comment("Determines whether veinminer can break netherrack")
                .translation(NuclearTechMod.MODID + ".configuration.recursionNetherrack")
                        .define("recursionNetherrack", false);
        ABILITY_HAMMER = builder
                .comment("Allows AoE ability")
                .translation(NuclearTechMod.MODID + ".configuration.hammerAbility")
                        .define("hammerAbility", true);
        ABILITY_VEIN = builder
                .comment("Allows veinminer ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityVein")
                        .define("abilityVein", true);
        ABILITY_LUCK = builder
                .comment("Allow luck (fortune) ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityLuck")
                        .define("abilityLuck", true);
        ABILITY_SILK = builder
                .comment("Allow silk touch ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilitySilk")
                        .define("abilitySilk", true);
        ABILITY_FURNACE = builder
                .comment("Allow auto-smelter ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityFurnace")
                        .define("abilityFurnace", true);
        ABILITY_SHREDDER = builder
                .comment("Allow auto-shredder ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityShredder")
                        .define("abilityShredder", true);
        ABILITY_CENTRIFUGE = builder
                .comment("Allow auto-centrifuge ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityCentrifuge")
                        .define("abilityCentrifuge", true);
        ABILITY_CRYSTALLIZER = builder
                .comment("Allow auto-ore_acidizer ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityCrystallizer")
                        .define("abilityCrystallizer", true);
        ABILITY_MERCURY = builder
                .comment("Allow mercury touch ability (digging redstone gives mercury)")
                .translation(NuclearTechMod.MODID + ".configuration.abilityMercury")
                        .define("abilityMercury", true);
        ABILITY_EXPLOSION = builder
                .comment("Allow explosion ability")
                .translation(NuclearTechMod.MODID + ".configuration.abilityExplosion")
                        .define("abilityExplosion", true);

        builder.pop();

        /// MOBS ///
        builder.push("mobs");

        POLLUTION_MULT = builder
                .comment("A multiplier for soot emitted.")
                .translation(NuclearTechMod.MODID + ".configuration.pollutionMult")
                        .defineInRange("pollutionMult", 1.0, Double.MIN_VALUE, Double.MAX_VALUE);

        builder.pop();

        /// HAZARDS ///
        builder.push("hazards");

        DISABLE_ASBESTOS = builder
                .comment("When turned on, all asbestos hazards are disabled.")
                .translation(NuclearTechMod.MODID + ".configuration.disableAsbestos")
                        .define("disableAsbestos", false);
        DISABLE_COAL = builder
                .comment("When turned on, all coal dust hazards are disabled.")
                .translation(NuclearTechMod.MODID + ".configuration.disableCoaldust")
                        .define("disableCoaldust", false);
        DISABLE_HOT = builder
                .comment("When turned on, all hot hazards are disabled.")
                .translation(NuclearTechMod.MODID + ".configuration.disableHot")
                        .define("disableHot", false);
        DISABLE_EXPLOSIVE = builder
                .comment("When turned on, all explosive hazards are disabled.")
                .translation(NuclearTechMod.MODID + ".configuration.disableExplosive")
                        .define("disableExplosive", false);
        DISABLE_HYDROACTIVE = builder
                .comment("When turned on, all hydroactive hazards are disabled.")
                .translation(NuclearTechMod.MODID + ".configuration.disableHydroactive")
                        .define("disableHydroactive", false);
        DISABLE_BLINDING = builder
                .comment("When turned on, all blinding hazards are disabled.")
                .translation(NuclearTechMod.MODID + ".configuration.disableBlinding")
                        .define("disableBlinding", false);

        builder.pop();

        /// HAZARDS ///
        builder.push("pollution");

        ENABLE_POLLUTION = builder
                .comment("If disabled, none of the pollution related things will work.")
                .translation(NuclearTechMod.MODID + ".configuration.enablePol")
                        .define("enablePol", true);

        builder.pop();

        /// BIOMES ///
        builder.push("biomes");

        ENABLE_CRATER_BIOMES = builder
                .comment("Enables the biome change caused by nuclear explosions")
                .translation(NuclearTechMod.MODID + ".configuration.craterBiome")
                .define("craterBiome", true);
        CRATER_RAD = builder
                .comment("RAD/s for the crater biome")
                .translation(NuclearTechMod.MODID + ".configuration.craterBiomeRad")
                .defineInRange("craterBiomeRad", 5D, 0D, Double.MAX_VALUE);
        CRATER_INNER_RAD = builder
                .comment("RAD/s for the inner crater biome")
                .translation(NuclearTechMod.MODID + ".configuration.craterBiomeInnerRad")
                .defineInRange("craterBiomeInnerRad", 25D, 0D, Double.MAX_VALUE);
        CRATER_OUTER_RAD = builder
                .comment("RAD/s for the outer crater biome")
                .translation(NuclearTechMod.MODID + ".configuration.craterBiomeOuterRad")
                        .defineInRange("craterBiomeOuterRad", 0.5D, 0D, Double.MAX_VALUE);
        CRATER_WATER_MULT = builder
                .comment("Multiplier for RAD/s in crater biomes when in water")
                .translation(NuclearTechMod.MODID + ".configuration.craterBiomeWaterMultiplier")
                        .defineInRange("craterBiomeWaterMultiplier", 5D, 0D, Double.MAX_VALUE);

        builder.pop();

        /// 528 ///
        builder.comment("528 Mode: Please proceed with caution!");
        builder.comment("528-Modus: Lassen Sie Vorsicht walten!");
        builder.comment("способ-528: действовать с осторожностью!");
        builder.push("528");

        ENABLE_528 = builder
                .comment("The central toggle for 528 mode.")
                .translation(NuclearTechMod.MODID + ".configuration.enable528")
                .define("enable528", false);

        builder.pop();
    }
}

