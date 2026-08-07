package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_Pheromone extends FluidTrait {

    public float strength;

    public FT_Pheromone() { }

    public FT_Pheromone(float strength) {
        this.strength = strength;
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(Component.translatable("fluid.trait.pheromone")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("strength").value(strength);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        strength = obj.get("strength").getAsFloat();
    }
}