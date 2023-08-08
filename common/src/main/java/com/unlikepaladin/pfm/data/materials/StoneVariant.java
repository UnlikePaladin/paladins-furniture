package com.unlikepaladin.pfm.data.materials;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
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

public class StoneVariant extends VariantBase<StoneVariant> {
    public static StoneVariant QUARTZ = new StoneVariant(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, "quartz");
    public static StoneVariant NETHERITE = new StoneVariant(Blocks.NETHERITE_BLOCK, Blocks.ANCIENT_DEBRIS,"netherite");
    public static StoneVariant LIGHT_WOOD = new StoneVariant(Blocks.QUARTZ_BLOCK, Blocks.STRIPPED_OAK_LOG, "light_wood");
    public static StoneVariant DARK_WOOD = new StoneVariant(Blocks.QUARTZ_BLOCK, Blocks.STRIPPED_DARK_OAK_LOG,"dark_wood");
    public static StoneVariant GRANITE = new StoneVariant(Blocks.POLISHED_GRANITE, Blocks.GRANITE,"granite");
    public static StoneVariant ANDESITE = new StoneVariant(Blocks.POLISHED_ANDESITE, Blocks.ANDESITE, "andesite");
    public static StoneVariant DIORITE = new StoneVariant(Blocks.POLISHED_DIORITE, Blocks.DIORITE,"diorite");
    public static StoneVariant BLACKSTONE = new StoneVariant(Blocks.POLISHED_BLACKSTONE, Blocks.BLACKSTONE,"blackstone");
    public static StoneVariant STONE = new StoneVariant(Blocks.STONE,  Blocks.COBBLESTONE, "stone");

    static final List<StoneVariant> DEFAULT_VARIANTS = new ArrayList<>();

    static {
        DEFAULT_VARIANTS.add(QUARTZ);
        DEFAULT_VARIANTS.add(NETHERITE);
        DEFAULT_VARIANTS.add(LIGHT_WOOD);
        DEFAULT_VARIANTS.add(DARK_WOOD);
        DEFAULT_VARIANTS.add(GRANITE);
        DEFAULT_VARIANTS.add(ANDESITE);
        DEFAULT_VARIANTS.add(DIORITE);
        DEFAULT_VARIANTS.add(BLACKSTONE);
        DEFAULT_VARIANTS.add(STONE);
    }

    public static List<StoneVariant> values() {
        return DEFAULT_VARIANTS;
    }
    private final String name;
    private final Block baseBlock;
    private final Block secondaryBlock;
    StoneVariant(Identifier identifier, Block baseBlock, Block secondaryBlock) {
        super(identifier);
        this.name = identifier.getPath();
        this.baseBlock = baseBlock;
        this.secondaryBlock = secondaryBlock;
    }

    StoneVariant(Block baseBlock, Block secondaryBlock, String name) {
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
    public StoneVariant getVariantType() {
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
        return STONE.baseBlock;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Identifier getTexture(BlockType type) {
        return ModelHelper.getTextureId(baseBlock);
    }

    @Override
    public String getPath() {
        return this.identifier.getPath();
    }
}