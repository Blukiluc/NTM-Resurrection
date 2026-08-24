package com.hbm.render.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class CuboidBakingUtil {

    private static final FaceBakery FACE_BAKERY = new FaceBakery();

    private CuboidBakingUtil() {
    }

    public static void addBox(List<BakedQuad> quads, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Function<Direction, TextureAtlasSprite> sprites) {
        addBox(quads, minX, minY, minZ, maxX, maxY, maxZ, sprites, direction -> 0);
    }

    public static void addBox(List<BakedQuad> quads, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Function<Direction, TextureAtlasSprite> sprites, ToIntFunction<Direction> rotations) {
        Vector3f from = new Vector3f(minX * 16.0F, minY * 16.0F, minZ * 16.0F);
        Vector3f to = new Vector3f(maxX * 16.0F, maxY * 16.0F, maxZ * 16.0F);
        BlockElement element = new BlockElement(from, to, Map.of(), null, true);

        for(Direction direction : Direction.values()) {
            BlockFaceUV uv = new BlockFaceUV(element.uvsByFace(direction), rotationDegrees(rotations.applyAsInt(direction)));
            BlockElementFace face = new BlockElementFace(null, -1, "", uv);
            quads.add(FACE_BAKERY.bakeQuad(from, to, face, sprites.apply(direction), direction, BlockModelRotation.X0_Y0, null, true));
        }
    }

    private static int rotationDegrees(int rotation) {
        return switch(Math.floorMod(rotation, 4)) {
            case 1 -> 270;
            case 2 -> 90;
            case 3 -> 180;
            default -> 0;
        };
    }
}
