package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_Poison extends FluidTrait {

    public int amplifier;
    public int duration;

    public FT_Poison() { }

    public FT_Poison(int duration, int amplifier) {
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(Component.translatable("fluid.trait.poison")
                .withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("duration").value(duration);
        writer.name("amplifier").value(amplifier);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        duration = obj.get("duration").getAsInt();
        amplifier = obj.get("amplifier").getAsInt();
    }
}