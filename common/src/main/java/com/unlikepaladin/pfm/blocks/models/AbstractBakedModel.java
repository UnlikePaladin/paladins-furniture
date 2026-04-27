package com.unlikepaladin.pfm.blocks.models;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.blocks.LogStoolBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.util.Tuple;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractBakedModel implements BakedModel {
    protected final ModelState settings;
    private final List<BakedModel> templateBakedModels;

    public static boolean reloading = false;
    public AbstractBakedModel(ModelState settings, List<BakedModel> templateBakedModels) {
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
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public ItemTransforms getTransforms() {
        return templateBakedModels.get(0).getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    Map<Block, VariantBase<?>> blockVariantMap = new HashMap<>();
    public VariantBase<?> getVariant(BlockState state) {
        VariantBase<?> variant;
        if (blockVariantMap.containsKey(state.getBlock())) {
            variant = blockVariantMap.get(state.getBlock());
        } else if (state != null && PaladinFurnitureModBlocksItems.furnitureEntryMap.containsKey(state.getBlock().getClass())) {
            variant = PaladinFurnitureModBlocksItems.furnitureEntryMap.get(state.getBlock().getClass()).getVariantFromEntry(state.getBlock());
            blockVariantMap.put(state.getBlock(), variant);
        } else {
            variant = null;
        }
        return variant;
    }


    public <T> List<TextureAtlasSprite> getSpriteList(T element) {
        List<TextureAtlasSprite> spriteList = getSpriteListWrapped(element);
        if (spriteList == null || spriteList.isEmpty() || spriteList.size() < 2) {
            VariantBase<?> variant = WoodVariantRegistry.OAK;
            Material mainTexture = new Material(InventoryMenu.BLOCK_ATLAS, ModelHelper.getTextureId(variant.getBaseBlock()));
            Material secondTexture = new Material(InventoryMenu.BLOCK_ATLAS, ModelHelper.getTextureId(variant.getSecondaryBlock()));
            return Arrays.asList(mainTexture.sprite(), secondTexture.sprite());
        }
        return spriteList;
    }
    private final Map<BlockItem, BlockState> blockItemBlockStateMap = new HashMap<>();
    /**
        Accepts an ItemStack, a BlockState, a Block or a BlockItem
     */
    public <T> List<TextureAtlasSprite> getSpriteListWrapped(T element) {
        if (element instanceof BlockState) {
            BlockState state = (BlockState) element;
            return getSpriteFromState(state);
        }
        else if (element instanceof BlockItem) {
            BlockItem blockItem = (BlockItem) element;
            if (blockItemBlockStateMap.containsKey(blockItem))
                return getSpriteFromState(blockItemBlockStateMap.get(blockItem));

            BlockState state = blockItem.getBlock().defaultBlockState();
            blockItemBlockStateMap.put(blockItem, state);
            return getSpriteFromState(state);
        } else if (element instanceof Block) {
            Block block = (Block) element;
            if (spriteList.containsKey(block))
                return spriteList.get(block);

            BlockState state = block.defaultBlockState();
            return getSpriteFromState(state);
        } else if (element instanceof ItemStack && ((ItemStack) element).getItem() instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)((ItemStack) element).getItem();
            if (blockItemBlockStateMap.containsKey(blockItem))
                return getSpriteFromState(blockItemBlockStateMap.get(blockItem));

            BlockState state = blockItem.getBlock().defaultBlockState();
            blockItemBlockStateMap.put(blockItem, state);
            return getSpriteFromState(state);
        } else if (element == null) {
            return Collections.singletonList(getTemplateBakedModels().get(0).getParticleIcon());
        } else {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Invalid element for sprite list method");
        }
        return Collections.singletonList(getTemplateBakedModels().get(0).getParticleIcon());
    }
    protected List<TextureAtlasSprite> getBedSprites(DyeColor color, BlockState state) {
        List<TextureAtlasSprite> list = new ArrayList<>(3);
        VariantBase<?> variant = getVariant(state);

        return list;
    }
    private final Map<Block, List<TextureAtlasSprite>> spriteList = new HashMap<>();
    private List<TextureAtlasSprite> getSpriteFromState(BlockState state) {
        if (spriteList.containsKey(state.getBlock()))
            return spriteList.get(state.getBlock());

        VariantBase<?> variant = getVariant(state);
        boolean stripped = state.getBlock().getDescriptionId().contains("stripped");
        List<TextureAtlasSprite> list = new ArrayList<>(3);
        if (state.getBlock() instanceof SimpleBedBlock) {
            DyeColor color = ModelHelper.getColor(Registry.BLOCK.getKey(state.getBlock()));
            Material mainTexture = new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.PLANKS));
            Material spriteIdentifier = Sheets.BED_TEXTURES[color.getId()];
            list.add(mainTexture.sprite());
            list.add(spriteIdentifier.sprite());
        }  else if (state.getBlock() instanceof LogStoolBlock) {
            Material mainTexture = stripped ? new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG)) : new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.LOG));
            Material secondTexture = stripped ? new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG_TOP)) : new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.LOG_TOP));
            list.add(mainTexture.sprite());
            list.add(secondTexture.sprite());
        } else if (!state.getBlock().getDescriptionId().contains("_raw_")) {
            Material mainTexture = stripped ? new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG)) : new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.PRIMARY));
            Material secondTexture = stripped ? new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.PRIMARY)) : new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.SECONDARY));
            list.add(mainTexture.sprite());
            list.add(secondTexture.sprite());
        } else {
            Material mainTexture = stripped ? new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG)) : new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.SECONDARY));
            list.add(mainTexture.sprite());
            list.add(mainTexture.sprite());
        }
        boolean isKitchen = state.getBlock().getDescriptionId().contains("kitchen_");
        if (isKitchen && !(variant instanceof WoodVariant)) {
            Tuple<Block, Block> counterMaterials = PFMRecipeProvider.getCounterMaterials(variant);
            Material mainTexture = new Material(InventoryMenu.BLOCK_ATLAS, ModelHelper.getTextureId(counterMaterials.getA()));
            Material secondTexture = new Material(InventoryMenu.BLOCK_ATLAS, ModelHelper.getTextureId(counterMaterials.getB()));
            list.set(0, mainTexture.sprite());
            list.set(1, secondTexture.sprite());
        }
        spriteList.put(state.getBlock(), list);
        return list;
    }
}