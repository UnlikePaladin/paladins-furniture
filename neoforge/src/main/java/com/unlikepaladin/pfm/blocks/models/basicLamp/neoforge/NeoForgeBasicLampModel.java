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
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import net.minecraft.util.math.random.Random;

public class NeoForgeBasicLampModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicLampModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof BasicLampBlock))
            return;

        List<BlockModelPart> quads = new ArrayList<>();

        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }

        boolean up = world.getBlockState(pos.up()).getBlock() instanceof BasicLampBlock;
        boolean down = world.getBlockState(pos.down()).getBlock() instanceof BasicLampBlock;
        int onOffset = state.get(Properties.LIT) ? 1 : 0;

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


    static List<Sprite> oakSprite = new ArrayList<>();
    static List<Sprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        Sprite wood = ModelHelper.getSprite(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,  Identifier.of("minecraft:block/stripped_oak_log")));
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<Sprite>> sprites = new HashMap<>();
    List<Sprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        Sprite wood = ModelHelper.getSprite(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)));
        List<Sprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @Override
    public Sprite particleIcon(BlockRenderView world, BlockPos pos, BlockState state) {
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
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
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
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, Random random) {
        Pair<VariantBase<?>, Direction> directionPair = new Pair<>(variant, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }
}
