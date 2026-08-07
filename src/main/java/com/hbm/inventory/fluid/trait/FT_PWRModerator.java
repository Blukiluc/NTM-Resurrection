package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_PWRModerator extends FluidTrait {

    public float moderation;

    public FT_PWRModerator() { }

    public FT_PWRModerator(float moderation) {
        this.moderation = moderation;
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(Component.translatable("fluid.trait.pwrmoderator", moderation)
                .withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("moderation").value(moderation);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        moderation = obj.get("moderation").getAsFloat();
    }
}