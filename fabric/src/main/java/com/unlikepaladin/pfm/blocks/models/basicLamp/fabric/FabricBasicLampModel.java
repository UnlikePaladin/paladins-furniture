package com.unlikepaladin.pfm.blocks.models.basicLamp.fabric;

import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Predicate;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class FabricBasicLampModel extends PFMFabricBakedModel {
    public FabricBasicLampModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = blockView.getBlockEntity(pos);
        int onOffset = state.getValue(BlockStateProperties.LIT) ? 1 : 0;
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        boolean up = blockView.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock;
        boolean down = blockView.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock;
        pushTextureTransform(context, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        if (up && down) {
            (getTemplateBakedModels().get(1)).emitQuads(context, cullTest);
        } else if (up) {
            (getTemplateBakedModels().get(0)).emitQuads(context, cullTest);
        } else if (down)
        {
            (getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
            (getTemplateBakedModels().get(5+onOffset)).emitQuads(context, cullTest);
            (getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
        }
        else {
            (getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
            (getTemplateBakedModels().get(2)).emitQuads(context, cullTest);
            (getTemplateBakedModels().get(5+onOffset)).emitQuads(context, cullTest);
        }
        context.popTransform();
    }

    @Override
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (getVariant() != null) {
            variant = (WoodVariant) getVariant();
        }

        Predicate<Direction> anyPredicate = d -> false;
        pushTextureTransform(context, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        (getTemplateBakedModels().get(4)).emitQuads(context, anyPredicate);
        (getTemplateBakedModels().get(2)).emitQuads(context, anyPredicate);
        (getTemplateBakedModels().get(5)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    static List<TextureAtlasSprite> oakSprite = new ArrayList<>();
    static List<TextureAtlasSprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        TextureAtlasSprite wood = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse("minecraft:block/stripped_oak_log")).sprite();
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<TextureAtlasSprite>> sprites = new HashMap<>();
    List<TextureAtlasSprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        TextureAtlasSprite wood = new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.STRIPPED_LOG)).sprite();
        List<TextureAtlasSprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getTemplateBakedModels().get(4).particleIcon();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(Level world, BlockPos pos, BlockState state) {
        BlockEntity entity = world.getBlockEntity(pos);
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (world.getBlockEntity(pos) instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        return getVariantStrippedLogSprite(variant).get(0);
    }
}
