package com.unlikepaladin.pfm.blocks.models.basicLamp.fabric;

import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Supplier;

public class FabricBasicLampModel extends PFMFabricBakedModel {
    public FabricBasicLampModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = blockView.getBlockEntity(pos);
        int onOffset = state.get(Properties.LIT) ? 1 : 0;
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        boolean up = blockView.getBlockState(pos.up()).getBlock() instanceof BasicLampBlock;
        boolean down = blockView.getBlockState(pos.down()).getBlock() instanceof BasicLampBlock;
        pushTextureTransform(context, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        if (up && down) {
            ((FabricBakedModel)getTemplateBakedModels().get(1)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else if (up) {
            ((FabricBakedModel)getTemplateBakedModels().get(0)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else if (down)
        {
            ((FabricBakedModel)getTemplateBakedModels().get(3)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)getTemplateBakedModels().get(5+onOffset)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)getTemplateBakedModels().get(4)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
        else {
            ((FabricBakedModel)getTemplateBakedModels().get(4)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)getTemplateBakedModels().get(2)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)getTemplateBakedModels().get(5+onOffset)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
        context.popTransform();
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (stack.hasNbt()) {
            variant = WoodVariantRegistry.getVariant(Identifier.tryParse(stack.getSubNbt("BlockEntityTag").getString("variant")));
        }
        pushTextureTransform(context, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        ((FabricBakedModel)getTemplateBakedModels().get(4)).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel)getTemplateBakedModels().get(2)).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel)getTemplateBakedModels().get(5)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public ModelTransformation getTransformation() {
        return getTemplateBakedModels().get(2).getTransformation();
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

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getTemplateBakedModels().get(4).getParticleSprite();
    }

    @Override
    public Sprite pfm$getParticle(World world, BlockPos pos, BlockState state) {
        BlockEntity entity = world.getBlockEntity(pos);
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (world.getBlockEntity(pos) instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        return getVariantStrippedLogSprite(variant).get(0);
    }
}
