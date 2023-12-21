package com.unlikepaladin.pfm.blocks.models;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractBakedModel implements BakedModel {
    protected final ModelBakeSettings settings;
    private final List<BakedModel> templateBakedModels;
    public AbstractBakedModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        this.settings = settings;
        this.templateBakedModels = templateBakedModels;
    }

    public List<BakedModel> getTemplateBakedModels() {
        return templateBakedModels;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public ModelTransformation getTransformation() {
        return templateBakedModels.get(0).getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    Map<Block, VariantBase<?>> blockVariantMap = new HashMap<>();
    protected VariantBase<?> getVariant(BlockState state) {
        VariantBase<?> variant;
        if (blockVariantMap.containsKey(state.getBlock())) {
            variant = blockVariantMap.get(state.getBlock());
        } else {
            variant = PaladinFurnitureModBlocksItems.furnitureEntryMap.get(state.getBlock().getClass()).getVariantFromEntry(state.getBlock());
            blockVariantMap.put(state.getBlock(), variant);
        }
        return variant;
    }


    private final Map<BlockItem, BlockState> blockItemBlockStateMap = new HashMap<>();
    /**
        Accepts an ItemStack, a BlockState, a Block or a BlockItem
     */
    public <T> List<Sprite> getSpriteList(T element) {
        if (element instanceof BlockState) {
            BlockState state = (BlockState) element;
            return getSpriteFromState(state);
        }
        else if (element instanceof BlockItem) {
            BlockItem blockItem = (BlockItem) element;
            if (blockItemBlockStateMap.containsKey(blockItem))
                return getSpriteFromState(blockItemBlockStateMap.get(blockItem));

            BlockState state = blockItem.getBlock().getDefaultState();
            blockItemBlockStateMap.put(blockItem, state);
            return getSpriteFromState(state);
        } else if (element instanceof Block) {
            Block block = (Block) element;
            if (spriteList.containsKey(block))
                return spriteList.get(block);

            BlockState state = block.getDefaultState();
            return getSpriteFromState(state);
        } else if (element instanceof ItemStack && ((ItemStack) element).getItem() instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)((ItemStack) element).getItem();
            if (blockItemBlockStateMap.containsKey(blockItem))
                return getSpriteFromState(blockItemBlockStateMap.get(blockItem));

            BlockState state = blockItem.getBlock().getDefaultState();
            blockItemBlockStateMap.put(blockItem, state);
            return getSpriteFromState(state);
        } else if (element == null) {
            return Collections.singletonList(getTemplateBakedModels().get(0).getParticleSprite());
        } else {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Invalid element for sprite list method");
        }
        return Collections.singletonList(getTemplateBakedModels().get(0).getParticleSprite());
    }

    private final Map<Block, List<Sprite>> spriteList = new HashMap<>();
    private List<Sprite> getSpriteFromState(BlockState state) {
        if (spriteList.containsKey(state.getBlock()))
            return spriteList.get(state.getBlock());

        VariantBase<?> variant = getVariant(state);
        boolean stripped = state.getBlock().getTranslationKey().contains("stripped");
        List<Sprite> list = new ArrayList<>(3);
        if (!state.getBlock().getTranslationKey().contains("_raw_")) {
            SpriteIdentifier mainTexture = stripped ? new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)) : new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
            SpriteIdentifier secondTexture = stripped ? new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY)) : new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.SECONDARY));
            list.add(mainTexture.getSprite());
            list.add(secondTexture.getSprite());
        } else {
            SpriteIdentifier mainTexture = stripped ? new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)) : new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.SECONDARY));
            list.add(mainTexture.getSprite());
            list.add(mainTexture.getSprite());
        }
        spriteList.put(state.getBlock(), list);
        return list;
    }
}