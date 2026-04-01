package com.unlikepaladin.pfm.data.materials;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.mixin.PFMFeatureFlagFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class StoneVariant extends VariantBase<StoneVariant> {
    private final Block polishedBlock;
    private final Block rawBlock;

    StoneVariant(ResourceLocation identifier, Block polishedBlock, Block rawBlock) {
        super(identifier);
        this.polishedBlock = polishedBlock;
        this.rawBlock = rawBlock;
    }

    @Override
    public String getSerializedName() {
        String postfix = this.isVanilla() ? "" : "_"+this.getNamespace();
        return this.identifier.getPath()+postfix;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ResourceLocation getTextureLocation(BlockType type) {
        if (type == BlockType.SECONDARY)
            return ModelHelper.getTextureId(rawBlock);
        return ModelHelper.getTextureId(polishedBlock);
    }

    @Override
    public String getPath() {
        return this.identifier.getPath();
    }

    @Override
    public Block getBaseBlock() {
        return this.polishedBlock;
    }

    @Override
    public Block getSecondaryBlock() {
        return rawBlock;
    }

    public Block getRawBlock() {
        return rawBlock;
    }

    public String toString() {
            return this.identifier.toString();
        }

    public boolean isNetherWood() {
        return this.identifier.getPath().contains("warped") || this.identifier.getPath().contains("crimson");
    }

    @Override
    public boolean isVanilla() {
        return identifier.getNamespace().equals("") || identifier.getNamespace().equals("minecraft");
    }

    @Override
    public List<FeatureFlag> getFeatureList() {
        FeatureFlag flag = PFMFeatureFlagFactory.newFlag(getBaseBlock().requiredFeatures().universe, 0);
        flag.mask = getBaseBlock().requiredFeatures().mask;
        return List.of(flag);
    }

    @Override
    public StoneVariant getVariantType() {
        return StoneVariant.this;
    }

    @Override
    public void initializeChildrenBlocks() {
        this.addChild("slab", this.findRelatedEntry("slab", BuiltInRegistries.BLOCK));
        this.addChild("stairs", this.findRelatedEntry("stairs", BuiltInRegistries.BLOCK));
        this.addChild("wall", this.findRelatedEntry("fence", BuiltInRegistries.BLOCK));
    }

    @Override
    public @Nullable ItemLike getItemForRecipe(String key, Class<? extends Block> blockClass) {
        ItemLike itemConvertible = super.getItemForRecipe(key, blockClass);
        if ((identifier.getPath().equals("calcite") || identifier.getPath().equals("netherite")) && (key.equals("base") || key.equals("secondary")) && blockClass.getSimpleName().contains("Kitchen")) {
            if (itemConvertible == getBaseBlock())
                return getSecondaryBlock();
            else
                return getBaseBlock();
        }
        return itemConvertible;
    }

    @Override
    public void initializeChildrenItems() {
    }

    public boolean hasStripped() {
        Object child = this.getChild("stripped_log");
        return child != null && child != this.getBaseBlock();
    }
    @Override
    public Block mainChild() {
        return this.polishedBlock;
    }

    public static class Finder implements SetFinder<StoneVariant> {

        private final Map<String, ResourceLocation> childNames = new HashMap<>();
        private final Supplier<Block> polishedFinder;
        private final Supplier<Block> rawFinder;
        private final ResourceLocation id;

        public Finder(ResourceLocation id, Supplier<Block> polished, Supplier<Block> raw) {
            this.id = id;
            this.polishedFinder = polished;
            this.rawFinder = raw;
        }

        public static Finder simple(String modId, String stoneTypeName, String polishedName, String rawName) {
            return simple(new ResourceLocation(modId, stoneTypeName), new ResourceLocation(modId, polishedName), new ResourceLocation(modId, rawName));
        }

        public static Finder simple(ResourceLocation stoneTypeName, ResourceLocation polishedName, ResourceLocation rawName) {
            return new Finder(stoneTypeName,
                    () -> BuiltInRegistries.BLOCK.get(polishedName),
                    () -> BuiltInRegistries.BLOCK.get(rawName));
        }

        public void addChild(String childType, String childName) {
            addChild(childType, new ResourceLocation(id.getNamespace(), childName));
        }

        public void addChild(String childType, ResourceLocation childName) {
            this.childNames.put(childType, childName);
        }

        public Optional<StoneVariant> get() {
            if (BlockItemRegistry.isModLoaded(id.getNamespace())) {
                try {
                    Block plank = polishedFinder.get();
                    Block log = rawFinder.get();
                    Block d = BuiltInRegistries.BLOCK.get(new ResourceLocation("minecraft","air"));
                    if (plank != d && log != d && plank != null && log != null) {
                        StoneVariant w = new StoneVariant(id, plank, log);
                        for (Map.Entry<String, ResourceLocation> entry : childNames.entrySet()){
                            Object child = BuiltInRegistries.BLOCK.getOptional(entry.getValue()).isPresent() ? BuiltInRegistries.BLOCK.get(entry.getValue()) : BuiltInRegistries.ITEM.get(entry.getValue());
                            w.addChild(entry.getKey(), child);
                        }
                        return Optional.of(w);
                    }
                } catch (Exception ignored) {
                }
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Failed to find custom stone type {}", id);
            }
            return Optional.empty();
        }
    }
}