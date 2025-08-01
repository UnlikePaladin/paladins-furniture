package com.unlikepaladin.pfm.blocks.models.basicLamp.forge;

import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeBasicLampModel extends PFMForgeBakedModel {
    public ForgeBasicLampModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }


    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<WoodVariant> VARIANT = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof BasicLampBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            WoodVariant variant = WoodVariantRegistry.OAK;
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof LampBlockEntity) {
                variant = ((LampBlockEntity) entity).getVariant();
            }
            BitSet set = new BitSet();
            set.set(0, world.getBlockState(pos.up()).getBlock() instanceof BasicLampBlock);
            set.set(1, world.getBlockState(pos.down()).getBlock() instanceof BasicLampBlock);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).with(VARIANT, variant).build();
            return data;
        }
        return tileData;
    }

    static List<Sprite> oakSprite = new ArrayList<>();
    static List<Sprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        Sprite wood = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,  Identifier.of("minecraft:block/stripped_oak_log")).getSprite();
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<Sprite>> sprites = new HashMap<>();
    List<Sprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        Sprite wood = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)).getSprite();
        List<Sprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable RenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BlockModelPart> quads = new ArrayList<>();
            int onOffset = state.get(Properties.LIT) ? 1 : 0;
            WoodVariant variant = extraData.get(VARIANT);
            BitSet set = extraData.get(CONNECTIONS).connections;
            if (set.get(0) && set.get(1)) {
                quads.add(getTemplateBakedModels().get(1));
            } else if (set.get(0)) {
                quads.add(getTemplateBakedModels().get(0));
            } else if (set.get(1))
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
            dest.addAll(getTexturedParts(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant)));
        }
    }
    
    @Override
    public Sprite particleIcon(@NotNull ModelData data) {
        if (data != null && data.has(VARIANT)) {
            return getVariantStrippedLogSprite(data.get(VARIANT)).get(0);
        }
        return super.particleIcon(data);
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
}
