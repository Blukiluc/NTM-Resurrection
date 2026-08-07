package com.hbm.config;

import com.hbm.main.NuclearTechMod;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public final ModConfigSpec.BooleanValue ENABLE_NTM_SKYBOX;

    public final ModConfigSpec.BooleanValue ENABLE_NUKE_HUD_FLASH;
    public final ModConfigSpec.BooleanValue ENABLE_NUKE_HUD_SHAKE;

    public final ModConfigSpec.IntValue INFO_POSITION;
    public final ModConfigSpec.IntValue INFO_OFFSET_X;
    public final ModConfigSpec.IntValue INFO_OFFSET_Y;

    public final ModConfigSpec.BooleanValue ENABLE_MAIN_MENU_WACKY_SPLASHES;
    public final ModConfigSpec.BooleanValue ENABLE_TIPS;

    public final ModConfigSpec.IntValue TOOL_HUD_OFFSET_HORIZONTAL;
    public final ModConfigSpec.IntValue TOOL_HUD_OFFSET_VERTICAL;

    ClientConfig(ModConfigSpec.Builder builder) {

        ENABLE_NTM_SKYBOX = builder
                .comment("Toggles ntm skybox.")
                .translation(NuclearTechMod.MODID + ".configuration.enableNtmSkybox")
                .define("enableNtmSkybox", true);

        ENABLE_NUKE_HUD_FLASH = builder
                .comment("Toggles flash from nuke explosion.")
                .translation(NuclearTechMod.MODID + ".configuration.enableNukeHudFlash")
                .define("enableNukeHudFlash", true);
        ENABLE_NUKE_HUD_SHAKE = builder
                .comment("Toggles hud shake from nuke explosion.")
                .translation(NuclearTechMod.MODID + ".configuration.enableNukeHudShake")
                .define("enableNukeHudShake", true);

        INFO_POSITION = builder
                .comment("Info position: 0 - top left, 1 - top right, 2 - next to the crosshair.")
                .translation(NuclearTechMod.MODID + ".configuration.infoPosition")
                .defineInRange("infoPosition", 0, 0, 2);
        INFO_OFFSET_X = builder
                .comment("Offset for the y position of the info panel")
                .translation(NuclearTechMod.MODID + ".configuration.infoOffsetHorizontal")
                .defineInRange("infoOffsetHorizontal", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        INFO_OFFSET_Y = builder
                .comment("Offset for the x position of the info panel")
                .translation(NuclearTechMod.MODID + ".configuration.infoOffsetVertical")
                .defineInRange("infoOffsetVertical", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        ENABLE_MAIN_MENU_WACKY_SPLASHES = builder
                .comment("Toggles wacky splashes in the main menu.")
                .translation(NuclearTechMod.MODID + ".configuration.mainMenuWackySplashes")
                .define("mainMenuWackySplashes", true);

        ENABLE_TIPS = builder
                .comment("Toggles tips in the world loading screen.")
                .translation(NuclearTechMod.MODID + ".configuration.tips")
                .define("tips", true);

        TOOL_HUD_OFFSET_HORIZONTAL = builder
                .comment("Tool hud indicator x offset.")
                .translation(NuclearTechMod.MODID + ".configuration.toolHudIndicatorX")
                .defineInRange("toolHudIndicatorX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        TOOL_HUD_OFFSET_VERTICAL = builder
                .comment("Tool hud indicator x offset.")
                .translation(NuclearTechMod.MODID + ".configuration.toolHudIndicatorY")
                .defineInRange("toolHudIndicatorY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
