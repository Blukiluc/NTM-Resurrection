package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_VentRadiation extends FluidTrait {

    public float radiation;

    public FT_VentRadiation() { }

    public FT_VentRadiation(float radiation) {
        this.radiation = radiation;
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(Component.translatable("fluid.trait.radiation", radiation)
                .withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("radiation").value(radiation);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        radiation = obj.get("radiation").getAsFloat();
    }
}