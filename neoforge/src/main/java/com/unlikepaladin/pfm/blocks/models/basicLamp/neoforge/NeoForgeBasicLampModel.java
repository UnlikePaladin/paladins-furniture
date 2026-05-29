package com.unlikepaladin.pfm.blocks.models.basicLamp.neoforge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import net.minecraft.util.RandomSource;

public class NeoForgeBasicLampModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicLampModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof BasicLampBlock))
            return;

        List<BlockModelPart> quads = new ArrayList<>();

        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }

        boolean up = world.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock;
        boolean down = world.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock;
        int onOffset = state.getValue(BlockStateProperties.LIT) ? 1 : 0;

        if (up && down) {
            quads.add(getTemplateBakedModels().get(1));
        } else if (up) {
            quads.add(getTemplateBakedModels().get(0));
        } else if (down)
        {
            quads.add(getTemplateBakedModels().get(3));
            quads.add(getTemplateBakedModels().get(5+onOffset));
            quads.add(getTemplateBakedModels().get(4));
        }
        else {
            quads.add(getTemplateBakedModels().get(4));
            quads.add(getTemplateBakedModels().get(2));
            quads.add(getTemplateBakedModels().get(5+onOffset));
        }
        parts.addAll(getTexturedParts(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant)));
    }


    static List<TextureAtlasSprite> oakSprite = new ArrayList<>();
    static List<TextureAtlasSprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        TextureAtlasSprite wood = ModelHelper.getSprite(new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse("minecraft:block/stripped_oak_log")));
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<TextureAtlasSprite>> sprites = new HashMap<>();
    List<TextureAtlasSprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        TextureAtlasSprite wood = ModelHelper.getSprite(new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.STRIPPED_LOG)));
        List<TextureAtlasSprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @Override
    public TextureAtlasSprite particleIcon(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof BasicLampBlock) {
            WoodVariant variant = WoodVariantRegistry.OAK;
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof LampBlockEntity) {
                variant = ((LampBlockEntity) entity).getVariant();
            }
            return getVariantStrippedLogSprite(variant).getFirst();
        }
        return super.particleIcon(world, pos, state);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<BakedQuad> quads = new ArrayList<>();
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (this.variant != null) {
            variant = (WoodVariant) this.variant;
        }
        quads.addAll(getTemplateBakedModels().get(4).getQuads(face));
        quads.addAll(getTemplateBakedModels().get(2).getQuads(face));
        quads.addAll(getTemplateBakedModels().get(5).getQuads(face));
        return getQuadsWithTextureInner(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
    }

    protected Map<Pair<VariantBase<?>, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random) {
        Pair<VariantBase<?>, Direction> directionPair = new Pair<>(variant, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }
}
