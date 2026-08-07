package com.hbm.datagen;

import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class NtmSoundDefinitionsProvider extends SoundDefinitionsProvider {

    protected NtmSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, NuclearTechMod.MODID, helper);
    }

    @Override
    public void registerSounds() {

        /// WEAPONS
        this.add(NtmSoundEvents.RICOCHET, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":weapon/ric1"),
                        sound(NuclearTechMod.MODID + ":weapon/ric2"),
                        sound(NuclearTechMod.MODID + ":weapon/ric3"),
                        sound(NuclearTechMod.MODID + ":weapon/ric4"),
                        sound(NuclearTechMod.MODID + ":weapon/ric5")
                )
        );
        this.add(NtmSoundEvents.MISSILE_TAKEOFF, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/missile_takeoff"))
        );
        this.add(NtmSoundEvents.MUKE_EXPLOSION, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/muke_explosion"))
        );
        this.add(NtmSoundEvents.ROBIN_EXPLOSION, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/robin_explosion"))
        );
        this.add(NtmSoundEvents.NUCLEAR_EXPLOSION, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/nuclear_explosion").stream())
        );
        this.add(NtmSoundEvents.EXPLOSION_LARGE_NEAR, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/explosion_large_near"))
        );
        this.add(NtmSoundEvents.EXPLOSION_LARGE_FAR, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/explosion_large_far"))
        );
        this.add(NtmSoundEvents.EXPLOSION_SMALL_NEAR, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":weapon/explosion_small_near1"),
                        sound(NuclearTechMod.MODID + ":weapon/explosion_small_near2"),
                        sound(NuclearTechMod.MODID + ":weapon/explosion_small_near3")
                )
        );
        this.add(NtmSoundEvents.EXPLOSION_SMALL_FAR, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":weapon/explosion_small_far1"),
                        sound(NuclearTechMod.MODID + ":weapon/explosion_small_far2")
                )
        );
        this.add(NtmSoundEvents.EXPLOSION_TINY, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":weapon/explosion_tiny1"),
                        sound(NuclearTechMod.MODID + ":weapon/explosion_tiny2")
                )
        );
        this.add(NtmSoundEvents.FSTBMB_START, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/fstbmb_start"))
        );
        this.add(NtmSoundEvents.FSTBMB_PING, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/fstbmb_ping"))
        );
        /// FIRE WEAPONS
        this.add(NtmSoundEvents.FIRE_DISINTEGRATION, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":weapon/fire/disintegration"))
        );
        /// ENTITIES
        this.add(NtmSoundEvents.OLD_EXPLOSION, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/old_explosion"))
        );
        this.add(NtmSoundEvents.BOMB_WHISTLE, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/bomb_whistle"))
        );
        this.add(NtmSoundEvents.BOMBER_LOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/bomber_loop"))
        );
        this.add(NtmSoundEvents.BOMBER_SMALL_LOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/bomber_small_loop"))
        );
        this.add(NtmSoundEvents.PLANE_CRASH, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/plane_crash"))
        );
        this.add(NtmSoundEvents.PLANE_SHOT_DOWN, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/plane_shot_down"))
        );
        this.add(NtmSoundEvents.DUCC, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":entity/ducc1"),
                        sound(NuclearTechMod.MODID + ":entity/ducc2")
                )
        );
        this.add(NtmSoundEvents.SLICER, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":entity/slicer1"),
                        sound(NuclearTechMod.MODID + ":entity/slicer2"),
                        sound(NuclearTechMod.MODID + ":entity/slicer3"),
                        sound(NuclearTechMod.MODID + ":entity/slicer4")
                )
        );
        this.add(NtmSoundEvents.METEORITE_FALLING_LOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":entity/meteorite_falling_loop"))
        );
        /// PLAYERS
        this.add(NtmSoundEvents.VOMIT, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":player/vomit"))
        );
        this.add(NtmSoundEvents.COUGH, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":player/cough1"),
                        sound(NuclearTechMod.MODID + ":player/cough2"),
                        sound(NuclearTechMod.MODID + ":player/cough3"),
                        sound(NuclearTechMod.MODID + ":player/cough4")
                )
        );
        /// BLOCKS
        this.add(NtmSoundEvents.PIPE_PLACED, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/pipe_placed"))
        );
        this.add(NtmSoundEvents.BOBBLE, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/bobble"))
        );
        this.add(NtmSoundEvents.FENSU_HUM, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/fensu_hum"))
        );
        this.add(NtmSoundEvents.DEBRIS, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":block/debris1"),
                        sound(NuclearTechMod.MODID + ":block/debris2"),
                        sound(NuclearTechMod.MODID + ":block/debris3")
                )
        );
        this.add(NtmSoundEvents.LOCK_OPEN, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/lock_open"))
        );
        this.add(NtmSoundEvents.SOYUZ_READY, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/soyuz_ready"))
        );
        this.add(NtmSoundEvents.CRATE_CLOSE, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/crate_close"))
        );
        this.add(NtmSoundEvents.CRATE_OPEN, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/crate_open"))
        );
        this.add(NtmSoundEvents.SQUEAKY_TOY, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/squeaky_toy"))
        );
        this.add(NtmSoundEvents.HUNDUNS_MAGNIFICENT_HOWL, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/hunduns_magnificent_howl"))
        );
        this.add(NtmSoundEvents.ELECTRIC_MOTOR_LOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/motor"))
        );
        this.add(NtmSoundEvents.ASSEMBLER_STRIKE, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":block/assembler_strike1"),
                        sound(NuclearTechMod.MODID + ":block/assembler_strike2")
                )
        );
        this.add(NtmSoundEvents.ASSEMBLER_CUT, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/assembler_cut"))
        );
        this.add(NtmSoundEvents.ASSEMBLER_START, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/assembler_start"))
        );
        this.add(NtmSoundEvents.ASSEMBLER_STOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/assembler_stop"))
        );
        this.add(NtmSoundEvents.CHEMICAL_PLANT_LOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/chemical_plant"))
        );
        this.add(NtmSoundEvents.PRESS_OPERATE, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/press"))
        );
        this.add(NtmSoundEvents.CENTRIFUGE_LOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":block/centrifuge_loop"))
        );
        this.add(NtmSoundEvents.METAL_IMPACT, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":block/metal_impact1"),
                        sound(NuclearTechMod.MODID + ":block/metal_impact2")
                )
        );
        /// ITEMS
        this.add(NtmSoundEvents.TECH_BLEEP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":tool/tech_bleep"))
        );
        this.add(NtmSoundEvents.TECH_BOOP, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":tool/tech_boop"))
        );
        this.add(NtmSoundEvents.GEIGER1, SoundDefinition.definition().with(sound(NuclearTechMod.MODID + ":tool/geiger1")));
        this.add(NtmSoundEvents.GEIGER2, SoundDefinition.definition().with(sound(NuclearTechMod.MODID + ":tool/geiger2")));
        this.add(NtmSoundEvents.GEIGER3, SoundDefinition.definition().with(sound(NuclearTechMod.MODID + ":tool/geiger3")));
        this.add(NtmSoundEvents.GEIGER4, SoundDefinition.definition().with(sound(NuclearTechMod.MODID + ":tool/geiger4")));
        this.add(NtmSoundEvents.GEIGER5, SoundDefinition.definition().with(sound(NuclearTechMod.MODID + ":tool/geiger5")));
        this.add(NtmSoundEvents.GEIGER6, SoundDefinition.definition().with(sound(NuclearTechMod.MODID + ":tool/geiger6")));
        this.add(NtmSoundEvents.PIN_UNLOCK, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":tool/pin_unlock"))
        );
        this.add(NtmSoundEvents.PIN_BREAK, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":tool/pin_break"))
        );
        this.add(NtmSoundEvents.UNPACK, SoundDefinition.definition()
                .with(
                        sound(NuclearTechMod.MODID + ":tool/extract1"),
                        sound(NuclearTechMod.MODID + ":tool/extract2")
                )
        );
        this.add(NtmSoundEvents.UPGRADE_PLUG, SoundDefinition.definition()
                .with(sound(NuclearTechMod.withDefaultNamespace("tool/upgrade_plug")))
        );
        /// ALARMS
        this.add(NtmSoundEvents.ALARM_HATCH, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":alarm/lpfhaiwg"))
        );
        this.add(NtmSoundEvents.ALARM_SOYUZED, SoundDefinition.definition()
                .with(sound(NuclearTechMod.MODID + ":alarm/soyuzed"))
        );
    }
}

