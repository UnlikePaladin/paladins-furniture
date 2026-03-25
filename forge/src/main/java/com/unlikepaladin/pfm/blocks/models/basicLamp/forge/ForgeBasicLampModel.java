package com.unlikepaladin.pfm.blocks.models.basicLamp.forge;

import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeBasicLampModel extends PFMForgeBakedModel {
    public ForgeBasicLampModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }


    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<WoodVariant> VARIANT = new ModelProperty<>();
    @Override
    public void createBlockStateDefinition(ModelDataMap.Builder builder) {
        super.createBlockStateDefinition(builder);
        builder.withProperty(CONNECTIONS);
        builder.withProperty(VARIANT);
    }

    @Override
    public void getPropertiesForItem(ItemStack stack, IModelData data) {
        super.getPropertiesForItem(stack, data);
        if (stack.hasTag() && stack.getTag().contains("BlockEntityTag") && stack.getTagElement("BlockEntityTag").contains("variant")) {
            WoodVariant variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(stack.getTagElement("BlockEntityTag").getString("variant")));
            data.setData(VARIANT, variant);
        }
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof BasicLampBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            WoodVariant variant = WoodVariantRegistry.OAK;
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof LampBlockEntity) {
                variant = ((LampBlockEntity) entity).getVariant();
            }
            BitSet set = new BitSet();
            set.set(0, world.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock);
            set.set(1, world.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            data.setData(VARIANT, variant);
            return data;
        }
        return tileData;
    }

    static List<TextureAtlasSprite> oakSprite = new ArrayList<>();
    static List<TextureAtlasSprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        TextureAtlasSprite wood = new Material(InventoryMenu.BLOCK_ATLAS,  new ResourceLocation("minecraft:block/stripped_oak_log")).sprite();
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<TextureAtlasSprite>> sprites = new HashMap<>();
    List<TextureAtlasSprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        TextureAtlasSprite wood = new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG)).sprite();
        List<TextureAtlasSprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            List<BakedQuad> quads = new ArrayList<>();
            int onOffset = state.getValue(BlockStateProperties.LIT) ? 1 : 0;
            WoodVariant variant = extraData.getData(VARIANT);
            TextureAtlasSprite sprite = new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG)).sprite();
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
    public TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        if (data != null && data.hasProperty(VARIANT)) {
            return getVariantStrippedLogSprite(data.getData(VARIANT)).get(0);
        }
        return super.getParticleIcon(data);
    }

    @Override
    public ItemTransforms getTransforms() {
        return getTemplateBakedModels().get(2).getTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<BakedQuad> quads = new ArrayList<>();
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (stack.hasTag()) {
            variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(stack.getTagElement("BlockEntityTag").getString("variant")));
        }
        quads.addAll(getTemplateBakedModels().get(4).getQuads(state, face, random));
        quads.addAll(getTemplateBakedModels().get(2).getQuads(state, face, random));
        quads.addAll(getTemplateBakedModels().get(5).getQuads(state, face, random));
        return getQuadsWithTexture(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
    }
}
