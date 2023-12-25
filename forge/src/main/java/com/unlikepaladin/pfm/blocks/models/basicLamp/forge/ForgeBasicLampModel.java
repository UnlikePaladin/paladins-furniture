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
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeBasicLampModel extends PFMForgeBakedModel {
    public ForgeBasicLampModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }


    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<WoodVariant> VARIANT = new ModelProperty<>();
    @Override
    public void appendProperties(ModelDataMap.Builder builder) {
        super.appendProperties(builder);
        builder.withProperty(CONNECTIONS);
        builder.withProperty(VARIANT);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof BasicLampBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            appendProperties(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            WoodVariant variant = WoodVariantRegistry.OAK;
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof LampBlockEntity) {
                variant = ((LampBlockEntity) entity).getVariant();
            }
            BitSet set = new BitSet();
            set.set(0, world.getBlockState(pos.up()).getBlock() instanceof BasicLampBlock);
            set.set(1, world.getBlockState(pos.down()).getBlock() instanceof BasicLampBlock);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            data.setData(VARIANT, variant);
            return data;
        }
        return tileData;
    }

    static List<Sprite> oakSprite = new ArrayList<>();
    static List<Sprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        Sprite wood = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, WoodVariantRegistry.OAK.getTexture(BlockType.STRIPPED_LOG)).getSprite();
        oakSprite.add(wood);
        return oakSprite;
    }

    static Map<WoodVariant, List<Sprite>> sprites = new HashMap<>();
    static List<Sprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        Sprite wood = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)).getSprite();
        List<Sprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            List<BakedQuad> quads = new ArrayList<>();
            int onOffset = state.get(Properties.LIT) ? 1 : 0;
            WoodVariant variant = extraData.getData(VARIANT);
            Sprite sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)).getSprite();
            BitSet set = extraData.getData(CONNECTIONS).connections;
            if (set.get(0) && set.get(1)) {
                quads.addAll(getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData));
            } else if (set.get(0)) {
                quads.addAll(getTemplateBakedModels().get(0).getQuads(state, side, rand, extraData));
            } else if (set.get(1))
            {
                quads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData));
                quads.addAll(getTemplateBakedModels().get(5+onOffset).getQuads(state, side, rand, extraData));
                quads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData));
            }
            else {
                quads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData));
                quads.addAll(getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData));
                quads.addAll(getTemplateBakedModels().get(5+onOffset).getQuads(state, side, rand, extraData));
            }
            return getQuadsWithTexture(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public Sprite getParticleTexture(@NotNull IModelData data) {
        if (data != null && data.hasProperty(VARIANT)) {
            return getVariantStrippedLogSprite(data.getData(VARIANT)).get(0);
        }
        return super.getParticleTexture(data);
    }

    @Override
    public ModelTransformation getTransformation() {
        return getTemplateBakedModels().get(2).getTransformation();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<BakedQuad> quads = new ArrayList<>();
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (stack.hasTag()) {
            variant = WoodVariantRegistry.getVariant(Identifier.tryParse(stack.getSubTag("BlockEntityTag").getString("variant")));
        }
        quads.addAll(getTemplateBakedModels().get(4).getQuads(state, face, random));
        quads.addAll(getTemplateBakedModels().get(2).getQuads(state, face, random));
        quads.addAll(getTemplateBakedModels().get(5).getQuads(state, face, random));
        return getQuadsWithTexture(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
    }
}
