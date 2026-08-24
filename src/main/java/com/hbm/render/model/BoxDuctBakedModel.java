package com.hbm.render.model;

import com.hbm.blocks.network.CableBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoxDuctBakedModel extends AbstractBakedModel {

    public static final ModelProperty<Boolean> IN_LEVEL = new ModelProperty<>();

    private final TextureAtlasSprite straight;
    private final TextureAtlasSprite end;
    private final TextureAtlasSprite curveTopLeft;
    private final TextureAtlasSprite curveTopRight;
    private final TextureAtlasSprite curveBottomLeft;
    private final TextureAtlasSprite curveBottomRight;
    private final TextureAtlasSprite junction;
    private final float diameter;
    private final float junctionDiameter;
    private final List<BakedQuad>[] cache = new List[64];
    private final List<BakedQuad> itemQuads;

    public BoxDuctBakedModel(ItemTransforms transforms, TextureAtlasSprite straight, TextureAtlasSprite end, TextureAtlasSprite curveTopLeft, TextureAtlasSprite curveTopRight, TextureAtlasSprite curveBottomLeft, TextureAtlasSprite curveBottomRight, TextureAtlasSprite junction, float diameter, float junctionDiameter) {
        super(true, true, false, true, transforms, ItemOverrides.EMPTY);
        this.straight = straight;
        this.end = end;
        this.curveTopLeft = curveTopLeft;
        this.curveTopRight = curveTopRight;
        this.curveBottomLeft = curveBottomLeft;
        this.curveBottomRight = curveBottomRight;
        this.junction = junction;
        this.diameter = diameter;
        this.junctionDiameter = junctionDiameter;
        this.itemQuads = this.buildItemQuads();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType type) {
        if(direction != null) return Collections.emptyList();

        if(!data.has(IN_LEVEL)) return this.itemQuads;

        boolean posX = state != null && state.hasProperty(CableBlock.EAST) && state.getValue(CableBlock.EAST);
        boolean negX = state != null && state.hasProperty(CableBlock.WEST) && state.getValue(CableBlock.WEST);
        boolean posY = state != null && state.hasProperty(CableBlock.UP) && state.getValue(CableBlock.UP);
        boolean negY = state != null && state.hasProperty(CableBlock.DOWN) && state.getValue(CableBlock.DOWN);
        boolean posZ = state != null && state.hasProperty(CableBlock.SOUTH) && state.getValue(CableBlock.SOUTH);
        boolean negZ = state != null && state.hasProperty(CableBlock.NORTH) && state.getValue(CableBlock.NORTH);
        int mask = mask(posX, negX, posY, negY, posZ, negZ);

        List<BakedQuad> quads = this.cache[mask];
        if(quads == null) {
            quads = this.buildQuads(posX, negX, posY, negY, posZ, negZ);
            this.cache[mask] = quads;
        }
        return quads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return state == null && direction == null ? this.itemQuads : Collections.emptyList();
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        return data.derive().with(IN_LEVEL, true).build();
    }

    private List<BakedQuad> buildItemQuads() {
        return this.buildQuads(false, false, false, false, true, true);
    }

    private List<BakedQuad> buildQuads(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        List<BakedQuad> quads = new ArrayList<>();
        float min = (1.0F - this.diameter) * 0.5F;
        float max = 1.0F - min;
        float junctionMin = (1.0F - this.junctionDiameter) * 0.5F;
        float junctionMax = 1.0F - junctionMin;
        int mask = mask(posX, negX, posY, negY, posZ, negZ);
        int count = Integer.bitCount(mask);

        if((mask & 0b001111) == 0 && mask > 0) {
            this.addBox(quads, 0.0F, min, min, 1.0F, max, max, posX, negX, posY, negY, posZ, negZ);
        } else if((mask & 0b111100) == 0 && mask > 0) {
            this.addBox(quads, min, min, 0.0F, max, max, 1.0F, posX, negX, posY, negY, posZ, negZ);
        } else if((mask & 0b110011) == 0 && mask > 0) {
            this.addBox(quads, min, 0.0F, min, max, 1.0F, max, posX, negX, posY, negY, posZ, negZ);
        } else {
            float centerMin = count == 2 ? min : junctionMin;
            float centerMax = count == 2 ? max : junctionMax;
            this.addBox(quads, centerMin, centerMin, centerMin, centerMax, centerMax, centerMax, posX, negX, posY, negY, posZ, negZ);

            if(posX) this.addBox(quads, centerMax, min, min, 1.0F, max, max, posX, negX, posY, negY, posZ, negZ);
            if(negX) this.addBox(quads, 0.0F, min, min, centerMin, max, max, posX, negX, posY, negY, posZ, negZ);
            if(posY) this.addBox(quads, min, centerMax, min, max, 1.0F, max, posX, negX, posY, negY, posZ, negZ);
            if(negY) this.addBox(quads, min, 0.0F, min, max, centerMin, max, posX, negX, posY, negY, posZ, negZ);
            if(posZ) this.addBox(quads, min, min, centerMax, max, max, 1.0F, posX, negX, posY, negY, posZ, negZ);
            if(negZ) this.addBox(quads, min, min, 0.0F, max, max, centerMin, posX, negX, posY, negY, posZ, negZ);
        }

        return quads;
    }

    private void addBox(List<BakedQuad> quads, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        CuboidBakingUtil.addBox(
                quads,
                minX,
                minY,
                minZ,
                maxX,
                maxY,
                maxZ,
                face -> this.getSprite(face, posX, negX, posY, negY, posZ, negZ),
                face -> this.getUvRotation(face, posX, negX, posY, negY, posZ, negZ)
        );
    }

    private int getUvRotation(Direction face, boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        int mask = mask(posX, negX, posY, negY, posZ, negZ);

        if((mask & 0b001111) == 0 && mask > 0) {
            if(face == Direction.UP || face == Direction.DOWN || face == Direction.SOUTH) return 1;
            if(face == Direction.NORTH) return 2;
        } else if((mask & 0b111100) == 0 && mask > 0) {
            if(face == Direction.WEST) return 1;
            if(face == Direction.EAST) return 2;
        } else if(Integer.bitCount(mask) == 2) {
            if((posY || negY) && (posX || negX) && (face == Direction.UP || face == Direction.DOWN)) return 1;
            if(!posY && !negY) {
                if(face == Direction.SOUTH || face == Direction.WEST) return 1;
                if(face == Direction.NORTH || face == Direction.EAST) return 2;
            }
        }

        return 0;
    }

    private TextureAtlasSprite getSprite(Direction face, boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        int mask = mask(posX, negX, posY, negY, posZ, negZ);
        int count = Integer.bitCount(mask);

        if((mask & 0b001111) == 0 && mask > 0) return face.getAxis() == Direction.Axis.X ? this.end : this.straight;
        if((mask & 0b111100) == 0 && mask > 0) return face.getAxis() == Direction.Axis.Z ? this.end : this.straight;
        if((mask & 0b110011) == 0 && mask > 0) return face.getAxis() == Direction.Axis.Y ? this.end : this.straight;

        if((face == Direction.DOWN && negY) || (face == Direction.UP && posY) || (face == Direction.NORTH && negZ) || (face == Direction.SOUTH && posZ) || (face == Direction.WEST && negX) || (face == Direction.EAST && posX)) return this.end;

        if(count == 2) {
            if((face == Direction.UP && negY) || (face == Direction.DOWN && posY) || (face == Direction.SOUTH && negZ) || (face == Direction.NORTH && posZ) || (face == Direction.EAST && negX) || (face == Direction.WEST && posX)) return this.straight;

            if(negY && posZ) return face == Direction.WEST ? this.curveBottomRight : this.curveBottomLeft;
            if(negY && negZ) return face == Direction.EAST ? this.curveBottomRight : this.curveBottomLeft;
            if(negY && posX) return face == Direction.SOUTH ? this.curveBottomRight : this.curveBottomLeft;
            if(negY && negX) return face == Direction.NORTH ? this.curveBottomRight : this.curveBottomLeft;
            if(posY && posZ) return face == Direction.WEST ? this.curveTopRight : this.curveTopLeft;
            if(posY && negZ) return face == Direction.EAST ? this.curveTopRight : this.curveTopLeft;
            if(posY && posX) return face == Direction.SOUTH ? this.curveTopRight : this.curveTopLeft;
            if(posY && negX) return face == Direction.NORTH ? this.curveTopRight : this.curveTopLeft;
            if(posX && negZ) return this.curveTopRight;
            if(posX && posZ) return this.curveBottomRight;
            if(negX && negZ) return this.curveTopLeft;
            if(negX && posZ) return this.curveBottomLeft;
        }

        return this.junction;
    }

    private static int mask(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        return (posX ? 32 : 0) + (negX ? 16 : 0) + (posY ? 8 : 0) + (negY ? 4 : 0) + (posZ ? 2 : 0) + (negZ ? 1 : 0);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.straight;
    }
}
