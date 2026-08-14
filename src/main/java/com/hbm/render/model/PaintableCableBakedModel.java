package com.hbm.render.model;

import com.hbm.blockentity.network.PaintableCableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaintableCableBakedModel extends AbstractBakedModel {

    private static final ChunkRenderTypeSet RENDER_TYPES = ChunkRenderTypeSet.of(RenderType.cutout());

    private final TextureAtlasSprite base;
    private final TextureAtlasSprite overlay;
    private final Map<CacheKey, List<BakedQuad>> cache = new ConcurrentHashMap<>();

    public PaintableCableBakedModel(ItemTransforms transforms, TextureAtlasSprite base, TextureAtlasSprite overlay) {
        super(true, true, false, true, transforms, ItemOverrides.EMPTY);
        this.base = base;
        this.overlay = overlay;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType type) {
        if(direction != null) return Collections.emptyList();

        BlockState paintedState = data.get(PaintableCableBlockEntity.PAINTED_STATE);
        boolean portsVisible = !data.has(PaintableCableBlockEntity.PORTS_VISIBLE) || Boolean.TRUE.equals(data.get(PaintableCableBlockEntity.PORTS_VISIBLE));
        CacheKey key = new CacheKey(paintedState, portsVisible);
        return this.cache.computeIfAbsent(key, ignored -> this.buildQuads(paintedState, portsVisible, random, type));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        if(state != null || direction != null) return Collections.emptyList();

        CacheKey key = new CacheKey(null, true);
        return this.cache.computeIfAbsent(key, ignored -> this.buildQuads(null, true, random, null));
    }

    private List<BakedQuad> buildQuads(@Nullable BlockState paintedState, boolean portsVisible, RandomSource random, @Nullable RenderType type) {
        List<BakedQuad> quads = new ArrayList<>();

        if(paintedState == null) {
            CuboidBakingUtil.addBox(quads, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, direction -> this.base);
        } else {
            BakedModel paintedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(paintedState);
            quads.addAll(paintedModel.getQuads(paintedState, null, random, ModelData.EMPTY, type));
            for(Direction direction : Direction.values()) {
                quads.addAll(paintedModel.getQuads(paintedState, direction, random, ModelData.EMPTY, type));
            }
        }

        if(portsVisible) CuboidBakingUtil.addBox(quads, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, direction -> this.overlay);
        return quads;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        return data;
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource random, ModelData data) {
        return RENDER_TYPES;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.base;
    }

    private record CacheKey(@Nullable BlockState state, boolean portsVisible) {
    }
}
