package com.unlikepaladin.pfm.data.materials;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ExtraStoolVariant extends VariantBase<ExtraStoolVariant> {
    public static ExtraStoolVariant GRAY_DARK_OAK = new ExtraStoolVariant(Blocks.GRAY_CONCRETE, Blocks.STRIPPED_DARK_OAK_LOG, "gray_dark_oak");
    public static ExtraStoolVariant WHITE = new ExtraStoolVariant(Blocks.WHITE_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, "white");
    public static ExtraStoolVariant GRAY = new ExtraStoolVariant(PaladinFurnitureModBlocksItems.RAW_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, "gray");
    public static ExtraStoolVariant LIGHT_GRAY_DARK_OAK = new ExtraStoolVariant(Blocks.LIGHT_GRAY_CONCRETE, Blocks.STRIPPED_DARK_OAK_LOG, "light_gray_dark_oak");

    private final String name;
    private final Block baseBlock;
    private final Block secondaryBlock;
    static final List<ExtraStoolVariant> DEFAULT_VARIANTS = new ArrayList<>();

    static {
        DEFAULT_VARIANTS.add(GRAY_DARK_OAK);
        DEFAULT_VARIANTS.add(WHITE);
        DEFAULT_VARIANTS.add(GRAY);
        DEFAULT_VARIANTS.add(LIGHT_GRAY_DARK_OAK);
    }

    public static List<ExtraStoolVariant> values() {
        return DEFAULT_VARIANTS;
    }

    ExtraStoolVariant(Identifier identifier, Block baseBlock, Block secondaryBlock) {
        super(identifier);
        this.name = identifier.getPath();
        this.baseBlock = baseBlock;
        this.secondaryBlock = secondaryBlock;
    }
    ExtraStoolVariant(Block baseBlock, Block secondaryBlock, String name) {
        this(new Identifier("", name), baseBlock, secondaryBlock);
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public Block getBaseBlock() {
        return baseBlock;
    }

    @Override
    public Block getSecondaryBlock() {
        return secondaryBlock;
    }

    @Override
    public boolean isNetherWood() {
        return false;
    }

    @Override
    public Material getVanillaMaterial() {
        return baseBlock.getDefaultState().getMaterial();
    }

    @Override
    public Material getBaseMaterial() {
        return baseBlock.getDefaultState().getMaterial();
    }

    @Override
    public Material getSecondaryMaterial() {
        return secondaryBlock.getDefaultState().getMaterial();
    }

    @Override
    public ExtraStoolVariant getVariantType() {
        return this;
    }

    @Override
    public boolean isVanilla() {
        return identifier.getNamespace().equals("") || identifier.getNamespace().equals("minecraft");
    }

    @Override
    public void initializeChildrenBlocks() {

    }

    @Override
    public void initializeChildrenItems() {

    }

    @Override
    public Block mainChild() {
        return baseBlock;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Identifier getTexture(BlockType type) {
        if (type == BlockType.SECONDARY)
            return ModelHelper.getTextureId(secondaryBlock);
        return ModelHelper.getTextureId(baseBlock);
    }

    @Override
    public String getPath() {
        return this.identifier.getPath();
    }
}