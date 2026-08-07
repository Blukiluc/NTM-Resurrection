package com.hbm.inventory.fluid.trait;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;

import java.io.IOException;
import java.util.List;

public class FT_Polluting extends FluidTrait {
    public HashMap<PollutionType, Float> releaseMap = new HashMap();
    public HashMap<PollutionType, Float> burnMap = new HashMap();

    public float pollution;

    public FT_Polluting() { }

    public FT_Polluting(float pollution) {
        this.pollution = pollution;
    }
    public FT_Polluting release(PollutionType type, float amount) {
        releaseMap.put(type, amount);
        return this;
    }

    public FT_Polluting burn(PollutionType type, float amount) {
        burnMap.put(type, amount);
        return this;
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(Component.translatable("[Polluting]", pollution)
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("pollution").value(pollution);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        pollution = obj.get("pollution").getAsFloat();
    }
}
